package ke.peachcar.customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface CustomerWishlistRepository extends JpaRepository<CustomerWishlist,UUID>{
 List<CustomerWishlist> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);
 Optional<CustomerWishlist> findByCustomerIdAndVehicleId(UUID customerId,UUID vehicleId);
 void deleteByCustomerIdAndVehicleId(UUID customerId,UUID vehicleId);
}
