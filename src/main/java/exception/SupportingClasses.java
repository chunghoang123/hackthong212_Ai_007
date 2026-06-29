package exception;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Các stub minh họa cho Phần 2 (gộp 1 file cho gọn).
 * Trong dự án thật mỗi class 1 file riêng.
 */

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_user_username", columnNames = "username")
})
class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

interface UserRepository extends JpaRepository<User, Long> { }

class UserRegistrationRequest {
    @NotBlank private String username;
    @NotBlank @Email private String email;
    @NotBlank private String password;
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}

class UserResponse {
    private final Long id;
    private final String username;
    private final String email;
    public UserResponse(Long id, String username, String email) {
        this.id = id; this.username = username; this.email = email;
    }
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
}

@RestController
@RequestMapping("/api/users")
class UserRegistrationController {
    private final UserRegistrationService service;
    UserRegistrationController(UserRegistrationService service) { this.service = service; }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@org.springframework.web.bind.annotation.RequestBody
                                 @jakarta.validation.Valid UserRegistrationRequest request) {
        // Controller "mỏng": không bọc try-catch, lỗi để advice xử lý tập trung.
        return service.registerNewUser(request);
    }
}
