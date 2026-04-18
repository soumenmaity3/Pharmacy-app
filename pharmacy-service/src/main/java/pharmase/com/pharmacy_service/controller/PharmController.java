package pharmase.com.pharmacy_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmase.com.pharmacy_service.model.PharmDTO;
import pharmase.com.pharmacy_service.security.JWTService;
import pharmase.com.pharmacy_service.service.PharmService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/pharm")
public class PharmController {

    @Autowired
    JWTService jwtService;

    @Autowired
    PharmService service;

    @GetMapping("/profile")
    public ResponseEntity<?> profile(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        String email = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);
        String shop_name = jwtService.extractShopName(token);

        Map<String, Object> response = new HashMap<>();
        response.put("email", email);
        response.put("role", role);
        response.put("shop_name", shop_name);

        return ResponseEntity.ok(response);
    }

    @GetMapping("is_pharm")
    public ResponseEntity<?> isPharm(@RequestParam("email") String email){
        return service.isPharm(email);
    }

    @GetMapping("is_valid")
    public ResponseEntity<?> isShopValid(@RequestHeader("Authorization") String authHeader){
        return service.isShopValid(authHeader);
    }

    @PostMapping("create")
    public ResponseEntity<?> createProfile(@RequestParam("email") String email) {
        return service.createProfile(email);
    }
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody PharmDTO pharmDTO){
        return service.updateProfile(authHeader,pharmDTO);
    }
}
