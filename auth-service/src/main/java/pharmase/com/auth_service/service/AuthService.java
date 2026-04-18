package pharmase.com.auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pharmase.com.auth_service.auth.JWTService;
import pharmase.com.auth_service.auth.local.CustomUserServiceDetails;
import pharmase.com.auth_service.feign.PharmServiceFeign;
import pharmase.com.auth_service.feign.UserServiceFeign;
import pharmase.com.auth_service.model.AuthPharm;
import pharmase.com.auth_service.model.AuthProvider;
import pharmase.com.auth_service.model.AuthUser;
import pharmase.com.auth_service.model.dto.LoginBody;
import pharmase.com.auth_service.model.dto.PharmDTO;
import pharmase.com.auth_service.model.dto.UserDTO;
import pharmase.com.auth_service.repository.AuthPharmRepository;
import pharmase.com.auth_service.repository.AuthUserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    AuthUserRepository repo;
    @Autowired
    AuthPharmRepository pharmRepo;
    @Autowired
    JWTService jwtService;
    @Autowired
    CustomUserServiceDetails userServiceDetails;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserServiceFeign userFeign;
    @Autowired
    PharmServiceFeign pharmFeign;

    public ResponseEntity<?> userRegister(UserDTO userDTO) {
        if (repo.findByEmail(userDTO.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email already registered as user.", HttpStatus.CONFLICT);
        }
        if (pharmRepo.findByEmail(userDTO.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email already registered as pharm.", HttpStatus.CONFLICT);
        }
        AuthUser user = new AuthUser();
        user.setEmail(userDTO.getEmail());
        user.setUser_name(userDTO.getUser_name());
        AuthProvider provider = userDTO.getProvider() != null
                ? userDTO.getProvider()
                : AuthProvider.LOCAL;
        user.setProvider(provider);
        if (AuthProvider.LOCAL.equals(provider)) {
            if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
                return new ResponseEntity<>("Password is required for local register.", HttpStatus.BAD_REQUEST);
            }
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            user.setPassword(null);
        }
        repo.save(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("provider", user.getProvider().name());
        claims.put("user_name", user.getUser_name());
        String token = jwtService.generateToken(claims, user.getEmail());
        Map<String, String> acc = Map.of(
                "token", token,
                "user_name", user.getUser_name(),
                "email", user.getEmail(),
                "role", user.getRole()
        );
        userFeign.createProfile(user.getEmail());

        return new ResponseEntity<>(acc, HttpStatus.CREATED);
    }

    public ResponseEntity<?> pharmRegister(PharmDTO pharmDTO) {
        if (repo.findByEmail(pharmDTO.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email already registered as user.", HttpStatus.CONFLICT);
        }
        if (pharmRepo.findByEmail(pharmDTO.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email already registered as pharm.", HttpStatus.CONFLICT);
        }
        AuthPharm pharm = new AuthPharm();
        pharm.setEmail(pharmDTO.getEmail());
        pharm.setShop_name(pharmDTO.getShop_name());
        AuthProvider provider = pharmDTO.getProvider() != null
                ? pharmDTO.getProvider()
                : AuthProvider.LOCAL;
        pharm.setProvider(provider);

        if (pharm.getProvider().equals(AuthProvider.LOCAL)) {
            if (pharmDTO.getPassword() == null || pharmDTO.getPassword().isBlank()) {
                return new ResponseEntity<>("Password is required for local register.", HttpStatus.BAD_REQUEST);
            }
            pharm.setPassword(passwordEncoder.encode(pharmDTO.getPassword()));
        } else {
            pharm.setPassword(null);
        }
        pharmRepo.save(pharm);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", pharm.getRole());
        claims.put("provider", pharm.getProvider().name());
        claims.put("shop_name", pharm.getShop_name());
        String token = jwtService.generateToken(claims, pharm.getEmail());
        Map<String, String> acc = Map.of(
                "token", token,
                "pharm_id", String.valueOf(pharm.getId()),
                "role", pharm.getRole(),
                "email", pharm.getEmail(),
                "shop_name", pharm.getShop_name()
        );
        pharmFeign.createProfile(pharm.getEmail());
        return new ResponseEntity<>(acc, HttpStatus.CREATED);
    }

    public ResponseEntity<?> userValidateToken(String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        UserDetails userDetails = userServiceDetails.loadUserByUsername(email);
        Optional<AuthUser> user = repo.findByEmail(email);
        if (!user.isPresent()) {
            return new ResponseEntity<>("User not found..", HttpStatus.BAD_REQUEST);
        }
        if (jwtService.validateToken(token, userDetails)) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }

    public ResponseEntity<?> pharmValidateToken(String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        UserDetails userDetails = userServiceDetails.loadUserByUsername(email);
        Optional<AuthPharm> user = pharmRepo.findByEmail(email);
        if (!user.isPresent()) {
            return new ResponseEntity<>("User not found..", HttpStatus.BAD_REQUEST);
        }
        if (jwtService.validateToken(token, userDetails)) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }

    public ResponseEntity<?> userLogin(LoginBody loginBody) {

        if (loginBody.getEmail() == null || loginBody.getEmail().isBlank()) {
            return new ResponseEntity<>("Email is required..", HttpStatus.BAD_REQUEST);
        }

        if (loginBody.getPassword() == null || loginBody.getPassword().isBlank()) {
            return new ResponseEntity<>("Password is required..", HttpStatus.BAD_REQUEST);
        }

        Optional<AuthUser> authUser = repo.findByEmail(loginBody.getEmail());

        if (authUser.isEmpty()) {
            return new ResponseEntity<>("User not found..", HttpStatus.NOT_FOUND);
        }

        AuthUser user = authUser.get();
        if (user.getPassword() == null) {
            return new ResponseEntity<>("This account uses Google login. Please sign in with Google.", HttpStatus.BAD_REQUEST);
        }

        if (passwordEncoder.matches(loginBody.getPassword(), user.getPassword())) {

            Map<String, Object> claims = Map.of(
                    "role", user.getRole(),
                    "provider", user.getProvider().name(),
                    "user_name", user.getUser_name()
            );

            String token = jwtService.generateToken(claims, user.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
            response.put("user_name", user.getUser_name());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>("Password not match..", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> pharmLogin(LoginBody loginBody) {

        if (loginBody.getEmail() == null || loginBody.getEmail().isBlank()) {
            return new ResponseEntity<>("Email is required..", HttpStatus.BAD_REQUEST);
        }

        if (loginBody.getPassword() == null || loginBody.getPassword().isBlank()) {
            return new ResponseEntity<>("Password is required..", HttpStatus.BAD_REQUEST);
        }

        Optional<AuthPharm> authPharm = pharmRepo.findByEmail(loginBody.getEmail());

        if (authPharm.isEmpty()) {
            return new ResponseEntity<>("User not found..", HttpStatus.NOT_FOUND);
        }

        AuthPharm pharm = authPharm.get();

        // 🔥 FIX: handle OAuth users
        if (pharm.getPassword() == null) {
            return new ResponseEntity<>("This account uses Google login. Please sign in with Google.", HttpStatus.BAD_REQUEST);
        }

        if (passwordEncoder.matches(loginBody.getPassword(), pharm.getPassword())) {

            Map<String, Object> claims = Map.of(
                    "role", pharm.getRole(),
                    "provider", pharm.getProvider().name(),
                    "shop_name", pharm.getShop_name()
            );

            String token = jwtService.generateToken(claims, pharm.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("email", pharm.getEmail());
            response.put("role", pharm.getRole());
            response.put("phar_id", pharm.getId());
            response.put("shop_name", pharm.getShop_name());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>("Password not match..", HttpStatus.BAD_REQUEST);
    }
}