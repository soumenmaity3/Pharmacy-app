package pharmase.com.reservationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmase.com.reservationservice.model.QuantityDetails;
import pharmase.com.reservationservice.service.ReservationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/reserve")
public class ReservationController {
    @Autowired
    private ReservationService service;

    @PostMapping("create")
    public ResponseEntity<?> createReservation(@RequestHeader("Authorization") String authHeader,
                                               @RequestParam("medicineId") List<QuantityDetails> medicineId){
        return service.createReservation(authHeader,medicineId);
    }
}
