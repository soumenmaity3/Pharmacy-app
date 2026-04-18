package pharmase.com.inventory_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmase.com.inventory_service.model.Medicine;
import pharmase.com.inventory_service.model.dto.ChangeInfoMed;
import pharmase.com.inventory_service.model.dto.MedicineBody;
import pharmase.com.inventory_service.service.MedicineService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/medicine")
public class MedicineController {
    @Autowired
    MedicineService service;

    @PostMapping("add_medicine")
    public ResponseEntity<?> addMedicine(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody MedicineBody medicine) {
        return service.addMedicine(authHeader, medicine);
    }

    @PutMapping("update_medicine")
    public ResponseEntity<?> updateMedicine(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody ChangeInfoMed infoMed) {
        return service.updateMedicine(authHeader, infoMed);
    }

    @GetMapping("medicines")
    public ResponseEntity<?> getAllMedicine(@RequestHeader("Authorization") String authHeader) {
        return service.getAllMedicine(authHeader);
    }

    @PostMapping("add_medicine/{id}")
    public ResponseEntity<?> addMedicineById(@RequestHeader("Authorization") String authHeader,
                                             @PathVariable("id") Integer id){
        return service.addMedicineById(authHeader,id);
    }

    @GetMapping("get_by_id/{id}")
    public ResponseEntity<?> getById(@RequestHeader("Authorization") String authHeader,
                                     @PathVariable("id") UUID id){
        return service.getMedicineById(authHeader,id);
    }
}
