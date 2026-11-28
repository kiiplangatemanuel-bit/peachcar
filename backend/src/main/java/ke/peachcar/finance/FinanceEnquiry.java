package ke.peachcar.finance;
import ke.peachcar.vehicle.Vehicle;
import ke.peachcar.customer.CustomerAccount;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="finance_enquiries")
public class FinanceEnquiry {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="vehicle_id") private Vehicle vehicle;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="customer_id") private CustomerAccount customer;
 @Column(nullable=false,length=120) private String fullName;
 @Column(nullable=false,length=190) private String email;
 @Column(nullable=false,length=30) private String phone;
 @Column(nullable=false,precision=14,scale=2) private BigDecimal monthlyIncome;
 @Column(nullable=false,precision=14,scale=2) private BigDecimal depositKes;
 @Column(nullable=false) private int preferredTermMonths;
 @Column(columnDefinition="text") private String message;
 @Column(nullable=false,length=20) private String status="NEW";
 @Column(nullable=false) private Instant createdAt=Instant.now();
 protected FinanceEnquiry(){}
 public UUID getId(){return id;} public String getFullName(){return fullName;} public String getEmail(){return email;} public String getPhone(){return phone;} public Vehicle getVehicle(){return vehicle;} public BigDecimal getMonthlyIncome(){return monthlyIncome;} public BigDecimal getDepositKes(){return depositKes;} public int getPreferredTermMonths(){return preferredTermMonths;} public String getMessage(){return message;} public String getStatus(){return status;} public void setStatus(String value){status=value;} public Instant getCreatedAt(){return createdAt;}
 public FinanceEnquiry(Vehicle vehicle,CustomerAccount customer,String fullName,String email,String phone,BigDecimal monthlyIncome,BigDecimal depositKes,int term,String message){this.vehicle=vehicle;this.customer=customer;this.fullName=fullName;this.email=email;this.phone=phone;this.monthlyIncome=monthlyIncome;this.depositKes=depositKes;this.preferredTermMonths=term;this.message=message;}
}
