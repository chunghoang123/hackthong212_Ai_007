package exception;

import java.time.Instant;

/**
 * DTO trả về cho client theo format ĐỒNG NHẤT cho mọi lỗi.
 * VD JSON: {"error":"DUPLICATE_DATA","message":"Dữ liệu đã tồn tại trong hệ thống", ...}
 */
public class ErrorResponse {
    private final String error;     // mã lỗi máy đọc được (machine-readable)
    private final String message;   // thông báo thân thiện cho người dùng
    private final int status;       // HTTP status code
    private final String path;      // endpoint gây lỗi
    private final Instant timestamp;

    public ErrorResponse(String error, String message, int status, String path) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = Instant.now();
    }

    public String getError() { return error; }
    public String getMessage() { return message; }
    public int getStatus() { return status; }
    public String getPath() { return path; }
    public Instant getTimestamp() { return timestamp; }
}
