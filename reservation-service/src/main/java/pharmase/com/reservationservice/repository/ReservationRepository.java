package pharmase.com.reservationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pharmase.com.reservationservice.model.ReservationModel;

import java.util.UUID;
@Repository
public interface ReservationRepository extends JpaRepository<ReservationModel, UUID> {
}
