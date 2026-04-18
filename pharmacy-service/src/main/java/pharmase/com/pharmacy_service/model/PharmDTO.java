package pharmase.com.pharmacy_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PharmDTO {
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
