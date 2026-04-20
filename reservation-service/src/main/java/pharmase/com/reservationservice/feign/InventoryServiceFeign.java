package pharmase.com.reservationservice.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import pharmase.com.reservationservice.model.ListMedicines;

import java.util.UUID;
@FeignClient("INVENTORY-SERVICE")
public interface InventoryServiceFeign {
    @GetMapping("/api/v1/medicine/get_by_id/{id}")
    public ListMedicines getById(@RequestHeader("Authorization") String authHeader,
                                 @PathVariable("id") UUID id);
}
