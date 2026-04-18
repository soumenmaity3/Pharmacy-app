package pharmase.com.inventory_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("MEDICINE-SERVICE")
public interface MedicineServiceFeign {
    @GetMapping("/api/v1/medicine/search_medicine")
    public ResponseEntity<?> searchMedicine(@RequestParam("keyword") String keyword);

    @GetMapping("/api/v1/medicine/{id}")
    public ResponseEntity<?> getById(@PathVariable("id")Integer id);
}
