package pharmase.com.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pharmase.com.inventory_service.model.Medicine;
import pharmase.com.inventory_service.model.dto.ListMedicines;
import pharmase.com.inventory_service.model.dto.MedicineBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, UUID> {

    @Query(value = "SELECT new pharmase.com.inventory_service.model.dto.MedicineBody(" +
            "m.id, m.drug_name, m.brand_name, m.generic_name, m.drug_type, " +
            "m.manufacturer, m.prescription_required, m.strength_value, m.strength_unit, " +
            "m.pack_size, m.pack_type,m.mrp, m.selling_price, m.stock_quantity, m.batch_num, " +
            "m.expiry_date, m.manufacture_date, m.schedule,m.shop_valid) " +
            "FROM Medicine m WHERE m.owner_email = :email")
    List<MedicineBody> findAllMedicineByOwnerEmail(String email);

    @Query(value = "SELECT new pharmase.com.inventory_service.model.dto.ListMedicines(" +
            "m.id, m.drug_name, m.drug_type, m.manufacturer, m.mrp, m.pack_size, " +
            "m.pack_type, m.prescription_required, m.selling_price) " +
            "FROM Medicine m WHERE m.id = :id")
    Optional<ListMedicines> findMedicineById(UUID id);
}
