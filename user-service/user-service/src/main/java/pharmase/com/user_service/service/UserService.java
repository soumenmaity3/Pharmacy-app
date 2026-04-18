package pharmase.com.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pharmase.com.user_service.model.AuthUser;
import pharmase.com.user_service.model.dto.AddressDTO;
import pharmase.com.user_service.model.dto.UserDTO;
import pharmase.com.user_service.repository.UserRepository;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository repo;

    public ResponseEntity<?> createProfile(String email) {
        if (email == null || email.isBlank()) {
            return new ResponseEntity<>("Email is required..", HttpStatus.BAD_REQUEST);
        }
        AuthUser user = new AuthUser();
        user.setEmail(email);
        repo.save(user);
        return ResponseEntity.ok(
                Map.of("message", "User created successfully")
        );
    }


    public ResponseEntity<?> updateProfile(String email, UserDTO userDTO) {
        Optional<AuthUser> optionalUser = repo.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        AuthUser user = optionalUser.get();
        user.setAddress(userDTO.getAddress());
        user.setLatitude(userDTO.getLatitude());
        user.setLongitude(userDTO.getLongitude());
        user.setPhone(userDTO.getPhone());

        repo.save(user);

        return ResponseEntity.ok(Map.of(
                "status", "updated",
                "email", user.getEmail()
        ));
    }

    public ResponseEntity<?> getAddress(String email) {
        AddressDTO address = repo.findAddressByEmail(email);
        return new ResponseEntity<>(Map.of(
                "address",address,
                "email",email
        ),HttpStatus.OK);
    }
}
