package pharmase.com.reservationservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

//@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListMedicines {
    private UUID id;
    private String drug_name;
    private String drug_type;
    private String manufacturer;
    private Double mrp;
    private Double pack_size;
    private String pack_type;
    private Boolean prescription_required;
    private Double selling_price;
}
