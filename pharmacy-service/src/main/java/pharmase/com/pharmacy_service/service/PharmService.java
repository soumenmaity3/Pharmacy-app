package pharmase.com.pharmacy_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pharmase.com.pharmacy_service.repository.PharmRepository;
import pharmase.com.pharmacy_service.model.AuthPharm;
import pharmase.com.pharmacy_service.model.PharmDTO;
import pharmase.com.pharmacy_service.security.JWTService;

import java.util.Map;
import java.util.Optional;

@Service
public class PharmService {
    @Autowired
    PharmRepository repo;
    @Autowired
    JWTService jwtService;

    public ResponseEntity<?> createProfile(String email) {
        if (email == null || email.isBlank()) {
            return new ResponseEntity<>("Email is required..", HttpStatus.BAD_REQUEST);
        }
        AuthPharm user = new AuthPharm();
        user.setEmail(email);
        repo.save(user);
        return ResponseEntity.ok(
                Map.of("message", "User created successfully")
        );
    }

    public ResponseEntity<?> updateProfile(String authHeader, PharmDTO pharmDTO) {
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        Optional<AuthPharm> user = repo.findByEmail(email);
        if (!user.isPresent()){
            return new ResponseEntity<>("User not found..",HttpStatus.BAD_REQUEST);
        }
        AuthPharm pharm = user.get();
        pharm.setLicence(pharmDTO.getLicence());
        pharm.setPhone(pharmDTO.getPhone());
        pharm.setOwner_name(pharmDTO.getOwner_name());
        pharm.setShop_address(pharmDTO.getShop_address());
        pharm.setIssued_date(pharmDTO.getIssued_date());
        pharm.setExpire_date(pharmDTO.getExpire_date());
        pharm.setPharmacist_name(pharmDTO.getPharmacist_name());
        pharm.setLicence_type(pharmDTO.getLicence_type());
        repo.save(pharm);
        return new ResponseEntity<>(Map.of(
                "Message","Update successfully"
        ),HttpStatus.OK);
    }

    public ResponseEntity<?> isPharm(String email) {
        boolean isPharm = repo.findByEmail(email).isPresent();
        if (false==isPharm) {
            return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(true,HttpStatus.OK);
    }

    public ResponseEntity<?> isShopValid(String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        Optional<AuthPharm> pharm = repo.findByEmail(email);
        return new ResponseEntity<>(pharm.get().isVerified(),HttpStatus.OK);
    }
}
