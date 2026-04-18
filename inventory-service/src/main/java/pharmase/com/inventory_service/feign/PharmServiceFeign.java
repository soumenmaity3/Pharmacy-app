package pharmase.com.inventory_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("PHARMACY-SERVICE")
public interface PharmServiceFeign {
    @GetMapping("/api/v1/pharm/is_pharm")
    public ResponseEntity<?> isPharm(@RequestParam("email") String email);
    @GetMapping("/api/v1/pharm/is_valid")
    public ResponseEntity<?> isShopValid(@RequestHeader("Authorization") String authHeader);
}
