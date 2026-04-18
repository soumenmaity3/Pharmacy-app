package pharmase.com.user_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("AUTH-SERVICE")
public interface AuthInterface {
    @GetMapping("api/v1/user/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader);

}
