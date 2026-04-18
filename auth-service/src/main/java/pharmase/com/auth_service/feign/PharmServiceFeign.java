package pharmase.com.auth_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("PHARMACY-SERVICE")
public interface PharmServiceFeign {
    @PostMapping("/api/v1/pharm/create")
    public ResponseEntity<?> createProfile(@RequestParam("email") String email);
}
