package ke.peachcar.settings;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "dealership_settings")
public class DealershipSettings {
    @Id private Long id = 1L;
    @Column(nullable = false, length = 120) private String dealershipName = "Peachcar";
    @Column(length = 30) private String phoneNumber;
    @Column(length = 30) private String whatsappNumber;
    @Column(length = 180) private String contactEmail;
    @Column(nullable = false) private Instant updatedAt = Instant.now();
    @PreUpdate void touch() { updatedAt = Instant.now(); }
    public Long getId() { return id; }
    public String getDealershipName() { return dealershipName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getWhatsappNumber() { return whatsappNumber; }
    public String getContactEmail() { return contactEmail; }
}
