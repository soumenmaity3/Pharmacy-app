package pharmase.com.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pharmase.com.auth_service.model.AuthPharm;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface AuthPharmRepository extends JpaRepository<AuthPharm, UUID> {
    Optional<AuthPharm> findByEmail(String email);
}
