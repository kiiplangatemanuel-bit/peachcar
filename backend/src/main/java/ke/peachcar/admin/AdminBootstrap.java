package ke.peachcar.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrap implements ApplicationRunner {
    private final AdminUserRepository admins;
    private final PasswordEncoder encoder;
    @Value("${PEACHCAR_ADMIN_EMAIL:}") private String email;
    @Value("${PEACHCAR_ADMIN_PASSWORD:}") private String password;
    public AdminBootstrap(AdminUserRepository admins, PasswordEncoder encoder){this.admins=admins;this.encoder=encoder;}
    @Override public void run(ApplicationArguments args) {
        if(email.isBlank() && password.isBlank()) return;
        if(email.isBlank() || password.length()<14) throw new IllegalStateException("Set PEACHCAR_ADMIN_EMAIL and a PEACHCAR_ADMIN_PASSWORD of at least 14 characters together.");
        if(admins.findByEmailIgnoreCase(email).isEmpty()) admins.save(new AdminUser(email,encoder.encode(password)));
    }
}
