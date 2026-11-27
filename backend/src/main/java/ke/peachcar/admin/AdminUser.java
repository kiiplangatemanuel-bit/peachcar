package ke.peachcar.admin;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "admin_users")
public class AdminUser {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(nullable=false, unique=true, length=190) private String email;
    @Column(nullable=false, length=100) private String passwordHash;
    @Column(nullable=false) private boolean enabled = true;
    @Column(nullable=false) private Instant createdAt = Instant.now();
    protected AdminUser() {}
    public AdminUser(String email, String passwordHash) { this.email=email.toLowerCase().trim(); this.passwordHash=passwordHash; }
    public UUID getId(){return id;}
    public String getEmail(){return email;}
    public String getPasswordHash(){return passwordHash;}
    public boolean isEnabled(){return enabled;}
}
