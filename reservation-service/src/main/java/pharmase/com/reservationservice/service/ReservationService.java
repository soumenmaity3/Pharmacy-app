package pharmase.com.reservationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmase.com.reservationservice.feign.InventoryServiceFeign;
import pharmase.com.reservationservice.model.ListMedicines;
import pharmase.com.reservationservice.model.QuantityDetails;
import pharmase.com.reservationservice.model.ReservationModel;
import pharmase.com.reservationservice.repository.ReservationRepository;
import pharmase.com.reservationservice.repository.ReserveMedicineDetailsRepository;
import pharmase.com.reservationservice.security.JWTService;
import pharmase.com.reservationservice.model.ReserveMedicineDetails;

import java.util.*;

@Service
public class ReservationService {
    @Autowired
    JWTService jwtService;
    @Autowired
    ReservationRepository reservationRepo;
    @Autowired
    ReserveMedicineDetailsRepository medicineRepo;
    @Autowired
    InventoryServiceFeign inventoryFeign;

    @Transactional
    public ResponseEntity<?> createReservation(String authHeader, List<QuantityDetails> medicineIds) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or missing Authorization header");
        }

        if (medicineIds == null || medicineIds.isEmpty()) {
            return ResponseEntity.badRequest().body("Medicine list cannot be empty");
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        if (!"USER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only USER can create reservation");
        }

        if (email == null || email.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token");
        }

        try {
            List<ReserveMedicineDetails> medicineDetailsList = new ArrayList<>();
            double totalPrice = 0;

            for (QuantityDetails item : medicineIds) {

                if (item.getQuantity() == null || item.getQuantity() <= 0) {
                    return ResponseEntity.badRequest().body("Invalid quantity");
                }

                ListMedicines medicine = inventoryFeign.getById(authHeader, item.getMedicineId());

                double price = medicine.getSelling_price() * item.getQuantity();
                totalPrice += price;

                ReserveMedicineDetails details = new ReserveMedicineDetails();
                details.setDrug_name(medicine.getDrug_name());
                details.setDrug_type(medicine.getDrug_type());
                details.setSelling_price(medicine.getSelling_price());
                details.setQuantity(item.getQuantity());

                medicineDetailsList.add(details);
            }

            // Create reservation (no shop_email)
            ReservationModel reservation = new ReservationModel();
            reservation.setUser_email(email);
            reservation.setDescription("Reservation created");
            reservation.setTotal_price(totalPrice);

            reservation = reservationRepo.save(reservation);

            // Save medicine details (batch)
            for (ReserveMedicineDetails details : medicineDetailsList) {
                details.setReservation_id(reservation);
            }

            medicineRepo.saveAll(medicineDetailsList);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Reservation created successfully");
            response.put("reservationId", reservation.getId());
            response.put("totalPrice", totalPrice);
            response.put("medicineCount", medicineDetailsList.size());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (feign.FeignException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Medicine not found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong");
        }
    }
}
