package pharmase.com.medicine_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pharmase.com.medicine_service.model.ListMedicines;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<ListMedicines,Integer> {
    @Query(value = """
    SELECT * FROM list_medicines
    WHERE LOWER(drug_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    ORDER BY 
        CASE 
            WHEN LOWER(drug_name) LIKE LOWER(CONCAT(:keyword, '%')) THEN 1
            ELSE 2
        END,
        drug_name ASC
    LIMIT 10
""", nativeQuery = true)
    List<ListMedicines> searchMedicinesSmart(@Param("keyword") String keyword);
}
