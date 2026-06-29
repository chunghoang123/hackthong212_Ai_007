package exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;

/**
 * BỘ XỬ LÝ LỖI TẬP TRUNG (cross-cutting concern theo tư tưởng AOP).
 *
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody:
 *   - Spring "bọc" (advice) quanh toàn bộ các @RestController.
 *   - Khi BẤT KỲ controller nào ném exception, request bị "chặn" tại đây
 *     trước khi đến client => không còn rò rỉ stack trace của DB.
 *
 * Đây chính là lý do KHÔNG cần try-catch rải rác ở mỗi hàm save() (giải thích trong README).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Bắt vi phạm ràng buộc dữ liệu (UNIQUE, NOT NULL, FK...).
     * Spring Data JPA dịch SQLIntegrityConstraintViolationException của Hibernate
     * thành DataIntegrityViolationException -> ta bắt đúng 1 chỗ tại đây.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException ex, WebRequest request) {

        // Suy ra thông báo cụ thể từ tên constraint, nhưng TUYỆT ĐỐI không lộ stack trace.
        String friendlyMessage = resolveFriendlyMessage(ex);

        ErrorResponse body = new ErrorResponse(
                "DUPLICATE_DATA",
                friendlyMessage,
                HttpStatus.CONFLICT.value(),                 // 409
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    /**
     * Bắt lỗi validation từ @Valid trên DTO -> 400 Bad Request.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, WebRequest request) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .orElse("Dữ liệu không hợp lệ");

        ErrorResponse body = new ErrorResponse(
                "VALIDATION_ERROR",
                message,
                HttpStatus.BAD_REQUEST.value(),              // 400
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Lưới an toàn cuối cùng: mọi lỗi chưa lường trước -> 500 nhưng KHÔNG lộ chi tiết.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception ex, WebRequest request) {
        // Ghi log nội bộ để dev điều tra (không gửi ra client).
        // log.error("Unhandled exception", ex);
        ErrorResponse body = new ErrorResponse(
                "INTERNAL_ERROR",
                "Đã có lỗi xảy ra, vui lòng thử lại sau",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),    // 500
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /**
     * Đọc tên constraint trong message gốc để trả thông báo cụ thể hơn,
     * nhưng chỉ dùng các từ khóa an toàn (không in nguyên message của DB ra ngoài).
     */
    private String resolveFriendlyMessage(DataIntegrityViolationException ex) {
        String raw = ex.getMostSpecificCause().getMessage();
        String lower = raw == null ? "" : raw.toLowerCase(Locale.ROOT);

        if (lower.contains("uk_user_email") || lower.contains("email")) {
            return "Email đã được sử dụng";
        }
        if (lower.contains("uk_user_username") || lower.contains("username")) {
            return "Tên đăng nhập đã tồn tại";
        }
        return "Dữ liệu đã tồn tại trong hệ thống";
    }
}
