package pharmase.com.pharmacy_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pharmase.com.pharmacy_service.model.AuthPharm;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PharmRepository extends JpaRepository<AuthPharm, UUID> {
    Optional<AuthPharm> findByEmail(String email);
}
