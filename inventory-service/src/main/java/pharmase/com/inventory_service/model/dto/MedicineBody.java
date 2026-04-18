package pharmase.com.inventory_service.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmase.com.inventory_service.model.Schedule;

import java.util.UUID;

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class MedicineBody {
    private UUID id;
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
    private Boolean shop_valid = false;

    public MedicineBody(UUID id, String drug_name, String brand_name, String generic_name,
                        String drug_type, String manufacturer, Boolean prescription_required,
                        Integer strength_value, String strength_unit, Double pack_size, String pack_type,
                        Double mrp, Double selling_price, Integer stock_quantity, String batch_num,
                        String expiry_date, String manufacture_date, Schedule schedule, Boolean shop_valid) {
        this.id = id;
        this.drug_name = drug_name;
        this.brand_name = brand_name;
        this.generic_name = generic_name;
        this.drug_type = drug_type;
        this.manufacturer = manufacturer;
        this.prescription_required = prescription_required;
        this.strength_value = strength_value;
        this.strength_unit = strength_unit;
        this.pack_size = pack_size;
        this.pack_type = pack_type;
        this.mrp = mrp;
        this.selling_price = selling_price;
        this.stock_quantity = stock_quantity;
        this.batch_num = batch_num;
        this.expiry_date = expiry_date;
        this.manufacture_date = manufacture_date;
        this.schedule = schedule;
        this.shop_valid = shop_valid;
    }

}
