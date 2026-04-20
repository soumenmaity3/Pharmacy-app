package pharmase.com.reservationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pharmase.com.reservationservice.model.ReserveMedicineDetails;

import java.util.UUID;
@Repository
public interface ReserveMedicineDetailsRepository extends JpaRepository<ReserveMedicineDetails, UUID> {
}
