package pharmase.com.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeInfoMed {
    private UUID id;
    private Double pack_size;
    private Double mrp;
    private Double selling_price;
    private Integer stock_quantity;
    private String batch_num;
    private String expiry_date;
    private String manufacture_date;
    private Boolean prescription_required;
}
