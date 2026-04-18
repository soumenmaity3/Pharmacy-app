package pharmase.com.auth_service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import pharmase.com.auth_service.model.AuthPharm;
import pharmase.com.auth_service.model.AuthProvider;
import pharmase.com.auth_service.model.AuthUser;
import pharmase.com.auth_service.repository.AuthPharmRepository;
import pharmase.com.auth_service.repository.AuthUserRepository;
@Service
public class CusOAuthUserServiceDetails extends DefaultOAuth2UserService {

    @Autowired
    AuthUserRepository userRepo;

    @Autowired
    AuthPharmRepository pharmRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = super.loadUser(userRequest);

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
//        AuthProvider provider = AuthProvider.valueOf(
//                userRequest.getClientRegistration().getRegistrationId().toUpperCase()
//        );

        // ← this is the key: check which redirect URI was used
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // e.g. "google-user" vs "google-pharm"
        AuthProvider provider = registrationId.contains("google")
                ? AuthProvider.GOOGLE
                : AuthProvider.LOCAL;
        if (registrationId.equals("google-pharm")) {
            handlePharmOAuth(email, provider);
        } else {
            handleUserOAuth(email,name,provider);
        }

        return oauthUser;
    }

    private void handleUserOAuth(String email, String name, AuthProvider provider) {

        // 🔥 CRITICAL FIX → cross-table check
        if (pharmRepo.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered as PHARM");
        }

        userRepo.findByEmail(email).ifPresentOrElse(
                existing -> {
                    if (!existing.getProvider().equals(provider)) {
                        throw new RuntimeException("Account already exists with different provider");
                    }
                    existing.setUser_name(name);
                    userRepo.save(existing);
                },
                () -> {
                    AuthUser user = new AuthUser();
                    user.setEmail(email);
                    user.setUser_name(name);
                    user.setProvider(provider);
                    user.setPassword(null); // ✅ OAuth user
                    userRepo.save(user);
                }
        );
    }
    private void handlePharmOAuth(String email, AuthProvider provider) {

        // 🔥 CRITICAL FIX → cross-table check
        if (userRepo.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered as USER");
        }

        pharmRepo.findByEmail(email).ifPresentOrElse(
                existing -> {
                    if (!existing.getProvider().equals(provider)) {
                        throw new RuntimeException("Account already exists with different provider");
                    }
                    pharmRepo.save(existing);
                },
                () -> {
                    AuthPharm pharm = new AuthPharm();
                    pharm.setEmail(email);
                    pharm.setProvider(provider);
                    pharm.setPassword(null); // ✅ OAuth user
                    pharmRepo.save(pharm);
                }
        );
    }
}