package pharmase.com.pharmacy_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "pharm_service")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthPharm {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String email;
    private String licence;
    private String owner_name;
    private String shop_address;
    private String phone;
//    private Double latitude;
//    private Double longitude;
    private String issued_date;
    private String expire_date;
    private String pharmacist_name;
    private LicenceType licence_type;

    private boolean verified=false;
}
