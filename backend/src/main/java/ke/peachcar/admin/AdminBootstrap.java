package ke.peachcar.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrap implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(AdminBootstrap.class);

    private final AdminUserRepository admins;
    private final PasswordEncoder encoder;

    @Value("${PEACHCAR_ADMIN_EMAIL:}") private String email;
    @Value("${PEACHCAR_ADMIN_PASSWORD:}") private String password;
    @Value("${PEACHCAR_ADMIN_BOOTSTRAP_REQUIRED:false}") private boolean bootstrapRequired;

    public AdminBootstrap(AdminUserRepository admins, PasswordEncoder encoder) {
        this.admins = admins;
        this.encoder = encoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        boolean emailMissing = email == null || email.isBlank();
        boolean passwordMissing = password == null || password.isBlank();

        if (emailMissing && passwordMissing && !bootstrapRequired) {
            log.warn("Administrator bootstrap skipped: PEACHCAR_ADMIN_EMAIL and PEACHCAR_ADMIN_PASSWORD are not configured.");
            return;
        }
        if (emailMissing || passwordMissing || password.length() < 14) {
            throw new IllegalStateException(
                    "Administrator bootstrap requires PEACHCAR_ADMIN_EMAIL and a PEACHCAR_ADMIN_PASSWORD of at least 14 characters.");
        }

        String normalizedEmail = email.trim().toLowerCase();
        if (admins.findByEmailIgnoreCase(normalizedEmail).isPresent()) {
            log.info("Administrator bootstrap verified: configured administrator already exists.");
            return;
        }

        admins.save(new AdminUser(normalizedEmail, encoder.encode(password)));
        log.info("Administrator bootstrap completed: initial administrator created.");
    }
}
