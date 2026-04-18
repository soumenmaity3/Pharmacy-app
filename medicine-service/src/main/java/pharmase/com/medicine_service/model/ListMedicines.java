package pharmase.com.medicine_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class ListMedicines {
    @Id
    private Integer id;
    private String drug_name;
    private String drug_type;
    private String manufacturer;
    private Double mrp;
    private Double pack_size;
    private String pack_type;
    private Boolean prescription_required;
    private Double selling_price;
}
