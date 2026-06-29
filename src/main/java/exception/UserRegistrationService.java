package exception;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service ĐĂNG KÝ đã được làm sạch.
 *
 * Điểm mấu chốt: KHÔNG có try-catch quanh save() ở đây.
 * - Service chỉ tập trung vào nghiệp vụ (business logic).
 * - Việc dịch lỗi DB -> HTTP response là cross-cutting concern,
 *   đã được GlobalExceptionHandler đảm nhiệm.
 *
 * (Các class User, UserRepository, DTO... là stub minh họa cho phần exception.)
 */
@Service
public class UserRegistrationService {

    private final UserRepository userRepository;

    public UserRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse registerNewUser(UserRegistrationRequest request) {
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(encryptPassword(request.getPassword()));

        // Nếu trùng email/username, save() sẽ ném DataIntegrityViolationException.
        // Ta CỐ Ý để nó "nổi" lên cho @RestControllerAdvice bắt -> trả 409 đồng nhất.
        User savedUser = userRepository.save(newUser);

        return mapToResponse(savedUser);
    }

    private String encryptPassword(String raw) {
        // Demo: thực tế dùng BCryptPasswordEncoder
        return "ENC(" + Integer.toHexString(raw.hashCode()) + ")";
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    }
}
