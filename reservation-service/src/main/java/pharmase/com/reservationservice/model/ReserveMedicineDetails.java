package pharmase.com.reservationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reserve_medicine_details")
public class ReserveMedicineDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String drug_name;
    private String drug_type;
    private Double selling_price;
    private Integer quantity;


    @ManyToOne
    @JoinColumn(name = "reservation_id",referencedColumnName = "id")
    private ReservationModel reservation_id;
}
