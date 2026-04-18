package pharmase.com.auth_service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pharmase.com.auth_service.auth.local.CustomUserServiceDetails;
import pharmase.com.auth_service.auth.local.JWTFilter;
import pharmase.com.auth_service.auth.local.RateLimitFilter;
import pharmase.com.auth_service.model.AuthPharm;
import pharmase.com.auth_service.model.AuthProvider;
import pharmase.com.auth_service.model.AuthUser;
import pharmase.com.auth_service.repository.AuthPharmRepository;
import pharmase.com.auth_service.repository.AuthUserRepository;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    CusOAuthUserServiceDetails oAuthUserService;
    @Autowired
    CustomUserServiceDetails customUserService;
    @Autowired
    JWTFilter jwtFilter;
    @Autowired
    JWTService jwtService;
    @Autowired
    AuthPharmRepository pharmRepo;
    @Autowired
    AuthUserRepository userRepo;
//    @Autowired
//    RateLimitFilter rateLimitFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(customize -> customize.disable())
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/api/v1/user/register",
                                        "/api/v1/pharm/register",
//                                        "/api/v1/auth/validate",
                                        "/api/v1/user/login",
                                        "/api/v1/pharm/login"
                                )
                                .permitAll()
                                .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(user -> user
                                .userService(oAuthUserService)
                        )
                        .successHandler((request, response, authentication) -> {
                            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
                            String email = oauthUser.getAttribute("email");

                            String registrationId = ((OAuth2AuthenticationToken) authentication)
                                    .getAuthorizedClientRegistrationId();

                            String role = registrationId.equals("google-pharm") ? "PHARM" : "USER";

                            Map<String, Object> claims = new HashMap<>();
                            claims.put("role", role);
                            if (role.equals("USER")) {
                                AuthUser user = userRepo.findByEmail(email)
                                        .orElseGet(() -> {
                                            AuthUser newUser = new AuthUser();
                                            newUser.setEmail(email);
                                            newUser.setProvider(AuthProvider.GOOGLE);
                                            newUser.setRole("USER");
                                            return userRepo.save(newUser);
                                        });
                                claims.put("user_name",user.getUser_name());
                            } else {
                                AuthPharm pharm = pharmRepo.findByEmail(email)
                                        .orElseGet(() -> {
                                            AuthPharm newPharm = new AuthPharm();
                                            newPharm.setEmail(email);
                                            newPharm.setProvider(AuthProvider.GOOGLE);
                                            newPharm.setRole("PHARM");
                                            return pharmRepo.save(newPharm);
                                        });

                                claims.put("pharm_id", pharm.getId());
                            }
                            String token = jwtService.generateToken(claims, email);

                            response.setContentType("application/json");
                            response.getWriter().write("{\"token\":\"" + token + "\"}");
                        }))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
