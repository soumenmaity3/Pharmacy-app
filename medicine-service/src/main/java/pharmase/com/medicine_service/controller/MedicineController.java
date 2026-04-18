package pharmase.com.medicine_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pharmase.com.medicine_service.repository.MedicineRepository;
import pharmase.com.medicine_service.service.MedicineService;

@Controller
@RequestMapping("api/v1/medicine")
public class MedicineController {
    @Autowired
    MedicineService service;

    @GetMapping("search_medicine")
    public ResponseEntity<?> searchMedicine(@RequestParam("keyword") String keyword){
        return service.searchMedicine(keyword);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id")Integer id){
        return service.findById(id);
    }
}
