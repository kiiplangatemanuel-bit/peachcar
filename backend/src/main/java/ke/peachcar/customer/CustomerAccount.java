package ke.peachcar.customer;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="customer_accounts")
public class CustomerAccount {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
 @Column(nullable=false,length=120) private String fullName;
 @Column(nullable=false,unique=true,length=190) private String email;
 @Column(nullable=false,length=100) private String passwordHash;
 @Column(nullable=false) private boolean enabled=true;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 @Column(nullable=false) private Instant updatedAt=Instant.now();
 protected CustomerAccount(){}
 public CustomerAccount(String fullName,String email,String passwordHash){this.fullName=fullName;this.email=email;this.passwordHash=passwordHash;}
 @PreUpdate void touch(){updatedAt=Instant.now();}
 public UUID getId(){return id;} public String getFullName(){return fullName;} public String getEmail(){return email;}
 public String getPasswordHash(){return passwordHash;} public boolean isEnabled(){return enabled;}
}
