package pharmase.com.auth_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmase.com.auth_service.model.AuthProvider;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PharmDTO {
    private String email,shop_name,password;
    private AuthProvider provider = AuthProvider.LOCAL;
}
