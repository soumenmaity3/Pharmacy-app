package pharmase.com.auth_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("USER-SERVICE")
public interface UserServiceFeign {
    @PostMapping("/api/v1/user/create")
    public ResponseEntity<?> createProfile(@RequestParam("email")String email);
}
