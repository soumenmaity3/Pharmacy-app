package pharmase.com.auth_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "auth_pharm")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthPharm {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String email;
    @Column(nullable = true)
    private String password;
    private String shop_name;
    private String role = "PHARM";
    @Enumerated(EnumType.STRING)
    private AuthProvider provider = AuthProvider.LOCAL;
    private LocalDateTime create_at = LocalDateTime.now();

}
