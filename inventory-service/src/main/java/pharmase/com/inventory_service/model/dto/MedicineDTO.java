package pharmase.com.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicineDTO {
    private String drug_name;
    private String brand_name;
    private Integer strength_value;
    private String strength_unit;
    private Double pack_size;
    private String pack_type;
    private String drug_type;
    private String mrp;
    private String selling_price;
    private Boolean prescription_required;
}
