package pharmase.com.user_service.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmase.com.user_service.feign.AuthInterface;
import pharmase.com.user_service.model.dto.UserDTO;
import pharmase.com.user_service.security.JWTService;
import pharmase.com.user_service.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    AuthInterface authInterface;

    @Autowired
    UserService service;
    @Autowired
    JWTService jwtService;

    @GetMapping("/profile")
    public ResponseEntity<?> profile(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        String email = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);
        String user_name = jwtService.extractUserName2(token);

        Map<String, Object> response = new HashMap<>();
        response.put("email", email);
        response.put("role", role);
        response.put("user_name", user_name);

        return ResponseEntity.ok(response);
    }

    @PostMapping("create")
    public ResponseEntity<?> createProfile(@RequestParam("email") String email) {
        return service.createProfile(email);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody UserDTO userDTO){
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        return service.updateProfile(email,userDTO);
    }

    @GetMapping("address")
    public ResponseEntity<?> getAddress(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        return service.getAddress(email);
    }
}