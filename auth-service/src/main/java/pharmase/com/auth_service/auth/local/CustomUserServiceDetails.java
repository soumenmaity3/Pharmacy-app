package pharmase.com.auth_service.auth.local;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pharmase.com.auth_service.model.AuthPharm;
import pharmase.com.auth_service.model.AuthUser;
import pharmase.com.auth_service.repository.AuthPharmRepository;
import pharmase.com.auth_service.repository.AuthUserRepository;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserServiceDetails implements UserDetailsService {
    @Autowired
    AuthUserRepository repo;
    @Autowired
    AuthPharmRepository pharmRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AuthUser> user = repo.findByEmail(username);
        if (user.isPresent()) {
            return new User(
                    user.get().getEmail(),
                    user.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("USER"))
            );
        }

        Optional<AuthPharm> pharm = pharmRepo.findByEmail(username);
        if (pharm.isPresent()) {
            return new User(
                    pharm.get().getEmail(),
                    pharm.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("PHARM"))
                    );

        }
        System.out.println("Authentication Failed: Email [" + username + "] not found in any table.");
        throw new UsernameNotFoundException("User or Doctor not found with email: " + username);
    }
}
