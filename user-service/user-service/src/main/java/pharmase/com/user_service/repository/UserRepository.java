package pharmase.com.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmase.com.user_service.model.AuthUser;
import pharmase.com.user_service.model.dto.AddressDTO;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AuthUser, UUID> {
    Optional<AuthUser> findByEmail(String email);

    @Query(value = "SELECT address,latitude,longitude FROM user_service WHERE email = :email",nativeQuery = true)
    AddressDTO findAddressByEmail(String email);
}
