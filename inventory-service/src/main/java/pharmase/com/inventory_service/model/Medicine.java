package pharmase.com.inventory_service.model;

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
@Table(name = "inv_medicine")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String owner_email;
    private String drug_name;
    private String brand_name;
    private String generic_name;
    private String drug_type;
    private String manufacturer;
    private Boolean prescription_required = false;
    private Integer strength_value;
    private String strength_unit;
    private Double pack_size;
    private String pack_type;
    private Double mrp;
    private Double selling_price;
    private Integer stock_quantity;
    private String batch_num;
    private String expiry_date;
    private String manufacture_date;
    //private Multimedia image;
    private Schedule schedule;
    private Boolean shop_valid=false;
}
