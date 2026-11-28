package ke.peachcar.customer;
import ke.peachcar.vehicle.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
@RestController @RequestMapping("/api/customer/wishlist")
public class CustomerWishlistController {
 private final CustomerAccountRepository customers; private final CustomerWishlistRepository wishlists; private final VehicleRepository vehicles; private final VehicleMapper mapper;
 public CustomerWishlistController(CustomerAccountRepository customers,CustomerWishlistRepository wishlists,VehicleRepository vehicles,VehicleMapper mapper){this.customers=customers;this.wishlists=wishlists;this.vehicles=vehicles;this.mapper=mapper;}
 @GetMapping public List<VehicleView> list(Authentication auth){CustomerAccount c=customer(auth);return wishlists.findByCustomerIdOrderByCreatedAtDesc(c.getId()).stream().map(CustomerWishlist::getVehicle).filter(v->v.getStatus()==VehicleStatus.PUBLISHED).map(mapper::view).toList();}
 @PostMapping("/{vehicleId}") @ResponseStatus(HttpStatus.CREATED) public void add(@PathVariable UUID vehicleId,Authentication auth){CustomerAccount c=customer(auth);Vehicle v=vehicles.findById(vehicleId).filter(x->x.getStatus()==VehicleStatus.PUBLISHED).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Published vehicle not found."));if(wishlists.findByCustomerIdAndVehicleId(c.getId(),vehicleId).isEmpty())wishlists.save(new CustomerWishlist(c,v));}
 @DeleteMapping("/{vehicleId}") @ResponseStatus(HttpStatus.NO_CONTENT) public void remove(@PathVariable UUID vehicleId,Authentication auth){wishlists.deleteByCustomerIdAndVehicleId(customer(auth).getId(),vehicleId);}
 private CustomerAccount customer(Authentication auth){return customers.findByEmailIgnoreCase(auth.getName()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED));}
}
