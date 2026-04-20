package pharmase.com.reservationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservations_med")
public class ReservationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime created_at = LocalDateTime.now();
    private LocalDateTime status_at = LocalDateTime.now();
    private ReservationStatus status = ReservationStatus.Pending;
    private String user_email;
    private String shop_email;
    private String description;
    private Double total_price;
}
