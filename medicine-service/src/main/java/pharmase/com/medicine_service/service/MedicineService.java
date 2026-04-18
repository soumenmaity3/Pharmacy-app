package pharmase.com.medicine_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pharmase.com.medicine_service.model.ListMedicines;
import pharmase.com.medicine_service.repository.MedicineRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineService {
    @Autowired
    MedicineRepository repo;

    public ResponseEntity<?> searchMedicine( String keyword) {
       List<ListMedicines> searchMedicines =  repo.searchMedicinesSmart(keyword);
        return new ResponseEntity<>(searchMedicines, HttpStatus.OK);
    }

    public ResponseEntity<?> findById(Integer id) {
        Optional<ListMedicines> medicines = repo.findById(id);
        return new ResponseEntity<>(medicines,HttpStatus.OK);
    }
}
