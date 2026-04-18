package pharmase.com.inventory_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pharmase.com.inventory_service.feign.MedicineServiceFeign;
import pharmase.com.inventory_service.feign.PharmServiceFeign;
import pharmase.com.inventory_service.model.Medicine;
import pharmase.com.inventory_service.model.dto.ChangeInfoMed;
import pharmase.com.inventory_service.model.dto.ListMedicines;
import pharmase.com.inventory_service.model.dto.MedicineBody;
import pharmase.com.inventory_service.repository.MedicineRepository;
import pharmase.com.inventory_service.security.JWTService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MedicineService {
    @Autowired
    JWTService jwtService;
    @Autowired
    PharmServiceFeign pharmFeign;
    @Autowired
    MedicineRepository repo;
    @Autowired
    MedicineServiceFeign medicineFeign;

    public ResponseEntity<?> addMedicine(String authHeader, MedicineBody med) {
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        boolean isPharm = pharmFeign.isPharm(email).hasBody();
        if (!isPharm) {
            return new ResponseEntity<>("This is not a Pharmacy email..", HttpStatus.NOT_ACCEPTABLE);
        }
        Medicine medicine = new Medicine();
        medicine.setDrug_name(med.getDrug_name());
        medicine.setBrand_name(med.getBrand_name());
        medicine.setOwner_email(email);
        medicine.setGeneric_name(med.getGeneric_name());
        medicine.setDrug_type(med.getDrug_type());
        medicine.setManufacturer(med.getManufacturer());
        medicine.setPrescription_required(med.getPrescription_required());
        medicine.setStrength_value(med.getStrength_value());
        medicine.setStrength_unit(med.getStrength_unit());
        medicine.setPack_size(med.getPack_size());
        medicine.setPack_type(med.getPack_type());
        medicine.setMrp(med.getMrp());
        medicine.setSelling_price(med.getSelling_price());
        medicine.setStock_quantity(med.getStock_quantity());
        medicine.setBatch_num(med.getBatch_num());
        medicine.setExpiry_date(med.getExpiry_date());
        medicine.setManufacture_date(med.getManufacture_date());
        medicine.setSchedule(med.getSchedule());
        boolean isShopValid = (boolean) pharmFeign.isShopValid(authHeader).getBody();
        medicine.setShop_valid(isShopValid);

        repo.save(medicine);

        return new ResponseEntity<>("Medicine add successfully by " + email, HttpStatus.OK);
    }

    public ResponseEntity<?> updateMedicine(String authHeader, ChangeInfoMed infoMed) {
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        if (email.isEmpty() || email.equals(null)) {
            return new ResponseEntity<>("Authentication is needed..", HttpStatus.NOT_FOUND);
        }
        Optional<Medicine> optionalMedicine = repo.findById(infoMed.getId());
        if (optionalMedicine.isEmpty()) {
            return new ResponseEntity<>("Medicine not found", HttpStatus.NOT_FOUND);
        }
        Medicine medicine = optionalMedicine.get();
        medicine.setPack_size(infoMed.getPack_size());
        medicine.setMrp(infoMed.getMrp());
        medicine.setSelling_price(infoMed.getSelling_price());
        medicine.setStock_quantity(infoMed.getStock_quantity());
        medicine.setBatch_num(infoMed.getBatch_num());
        medicine.setExpiry_date(infoMed.getExpiry_date());
        medicine.setManufacture_date(infoMed.getManufacture_date());
        repo.save(medicine);
        return new ResponseEntity<>("Medicine updated successfully", HttpStatus.OK);
    }

    public ResponseEntity<?> getAllMedicine(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Invalid or missing Authorization header", HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        if (email == null || email.isBlank()) {
            return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);
        }
        List<MedicineBody> allMedicine = repo.findAllMedicineByOwnerEmail(email);
        if (allMedicine.isEmpty()) {
            return new ResponseEntity<>("No medicines found", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(allMedicine, HttpStatus.OK);
    }

    public ResponseEntity<?> addMedicineById(String authHeader, Integer id) {
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        if (email.isEmpty() || email.equals(null)) {
            return new ResponseEntity<>("User Not found",HttpStatus.BAD_REQUEST);
        }
        
        try {
            // Convert LinkedHashMap to ListMedicines object
            ObjectMapper mapper = new ObjectMapper();
            Object responseBody = medicineFeign.getById(id).getBody();
            ListMedicines medicines = mapper.convertValue(responseBody, ListMedicines.class);
            
            Medicine medicine = new Medicine();
            medicine.setDrug_name(medicines.getDrug_name());
            medicine.setOwner_email(email);
            medicine.setDrug_type(medicines.getDrug_type());
            medicine.setManufacturer(medicines.getManufacturer());
            medicine.setPrescription_required(medicines.getPrescription_required());
            medicine.setPack_size(medicines.getPack_size());
            medicine.setPack_type(medicines.getPack_type());
            medicine.setMrp(medicines.getMrp());
            medicine.setSelling_price(medicines.getSelling_price());
            boolean isShopValid = (boolean) pharmFeign.isShopValid(authHeader).getBody();
            medicine.setShop_valid(isShopValid);
            repo.save(medicine);
            return new ResponseEntity<>("Medicine add successful by "+email,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing medicine data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getMedicineById(String authHeader, UUID id) {
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        if (email.isBlank() || email == null) {
            return new ResponseEntity<>("User is not find",HttpStatus.NOT_FOUND);
        }
        Optional<ListMedicines> medicine = repo.findMedicineById(id);
        if (medicine.isPresent()) {
            return new ResponseEntity<>(medicine.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Medicine not found", HttpStatus.NOT_FOUND);
        }
    }
}
