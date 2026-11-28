package ke.peachcar.admin;
import ke.peachcar.finance.*;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/api/admin/finance-enquiries")
public class AdminFinanceEnquiryController {
 private final FinanceEnquiryRepository enquiries;
 public AdminFinanceEnquiryController(FinanceEnquiryRepository enquiries){this.enquiries=enquiries;}
 @GetMapping @Transactional(readOnly=true) public List<EnquiryView> list(){return enquiries.findAll().stream().sorted(Comparator.comparing(EnquiryView::createdAt).reversed()).map(e->new EnquiryView(e.getId(),e.getFullName(),e.getEmail(),e.getPhone(),e.getVehicle()==null?null:e.getVehicle().getMake()+" "+e.getVehicle().getModel(),e.getMonthlyIncome(),e.getDepositKes(),e.getPreferredTermMonths(),e.getMessage(),e.getStatus(),e.getCreatedAt())).toList();}
 @org.springframework.web.bind.annotation.PatchMapping("/{id}/status") @Transactional public EnquiryView update(@org.springframework.web.bind.annotation.PathVariable UUID id,@Valid @org.springframework.web.bind.annotation.RequestBody StatusRequest body){FinanceEnquiry e=enquiries.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));e.setStatus(body.status());return new EnquiryView(e.getId(),e.getFullName(),e.getEmail(),e.getPhone(),e.getVehicle()==null?null:e.getVehicle().getMake()+" "+e.getVehicle().getModel(),e.getMonthlyIncome(),e.getDepositKes(),e.getPreferredTermMonths(),e.getMessage(),e.getStatus(),e.getCreatedAt());}
 public record StatusRequest(@jakarta.validation.constraints.NotBlank @Pattern(regexp="NEW|IN_REVIEW|CONTACTED|CLOSED") String status){}
 public record EnquiryView(UUID id,String fullName,String email,String phone,String vehicle,java.math.BigDecimal monthlyIncome,java.math.BigDecimal depositKes,int preferredTermMonths,String message,String status,Instant createdAt){}
}
