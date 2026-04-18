package pharmase.com.user_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmase.com.user_service.model.AuthProvider;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String address;
    private Double latitude,longitude;
    private String phone;
}
