package ke.peachcar.finance;
import ke.peachcar.customer.*;
import ke.peachcar.vehicle.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.Map;
@RestController @RequestMapping("/api/public/finance-enquiries")
public class FinanceEnquiryController {
 private final FinanceEnquiryRepository enquiries; private final VehicleRepository vehicles; private final CustomerAccountRepository customers;
 public FinanceEnquiryController(FinanceEnquiryRepository enquiries,VehicleRepository vehicles,CustomerAccountRepository customers){this.enquiries=enquiries;this.vehicles=vehicles;this.customers=customers;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Map<String,String> submit(@Valid @RequestBody Request body,Authentication auth){
  Vehicle vehicle=body.vehicleId()==null?null:vehicles.findById(body.vehicleId()).filter(v->v.getStatus()==VehicleStatus.PUBLISHED).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Vehicle not found."));
  CustomerAccount customer=auth!=null&&auth.isAuthenticated()&&!auth.getName().equals("anonymousUser")?customers.findByEmailIgnoreCase(auth.getName()).orElse(null):null;
  FinanceEnquiry saved=enquiries.save(new FinanceEnquiry(vehicle,customer,body.fullName().trim(),body.email().trim().toLowerCase(),body.phone().trim(),body.monthlyIncome(),body.depositKes(),body.preferredTermMonths(),body.message()));
  return Map.of("id",saved.getId().toString(),"status","RECEIVED");
 }
 public record Request(@NotBlank @Size(max=120) String fullName,@NotBlank @Email @Size(max=190) String email,@NotBlank @Size(max=30) String phone,@NotNull @DecimalMin("0.00") BigDecimal monthlyIncome,@NotNull @DecimalMin("0.00") BigDecimal depositKes,@Min(6) @Max(84) int preferredTermMonths,UUID vehicleId,@Size(max=2000) String message){}
}
