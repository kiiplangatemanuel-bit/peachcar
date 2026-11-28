package ke.peachcar.customer;
import ke.peachcar.vehicle.Vehicle;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="customer_wishlists",uniqueConstraints=@UniqueConstraint(name="uq_customer_wishlist_vehicle",columnNames={"customer_id","vehicle_id"}))
public class CustomerWishlist {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="customer_id",nullable=false) private CustomerAccount customer;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="vehicle_id",nullable=false) private Vehicle vehicle;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 protected CustomerWishlist(){}
 public CustomerWishlist(CustomerAccount customer,Vehicle vehicle){this.customer=customer;this.vehicle=vehicle;}
 public UUID getId(){return id;} public Vehicle getVehicle(){return vehicle;} public Instant getCreatedAt(){return createdAt;}
}
