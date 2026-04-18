package pharmase.com.auth_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmase.com.auth_service.model.dto.LoginBody;
import pharmase.com.auth_service.model.dto.PharmDTO;
import pharmase.com.auth_service.model.dto.UserDTO;
import pharmase.com.auth_service.service.AuthService;

@RestController
@RequestMapping("api/v1")
public class AuthController {
    @Autowired
    AuthService service;


    @PostMapping("user/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        return service.userRegister(userDTO);
    }

    @PostMapping("pharm/register")
    public ResponseEntity<?> register(@RequestBody PharmDTO pharmDTO) {
        return service.pharmRegister(pharmDTO);
    }

    @GetMapping("user/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        return service.userValidateToken(authHeader);
    }

    @GetMapping("pharm/validate")
    public ResponseEntity<?>pharmValidateToken(@RequestHeader("Authorization") String authHeader){
        return service.pharmValidateToken(authHeader);
    }

    @PostMapping("user/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginBody loginBody) {
        return service.userLogin(loginBody);
    }

    @PostMapping("pharm/login")
    public ResponseEntity<?> pharmLogin(@RequestBody LoginBody loginBody) {
        return service.pharmLogin(loginBody);
    }

}
