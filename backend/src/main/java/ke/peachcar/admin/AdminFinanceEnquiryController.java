package ke.peachcar.admin;
import ke.peachcar.finance.*;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;
@RestController @RequestMapping("/api/admin/finance-enquiries")
public class AdminFinanceEnquiryController {
 private final FinanceEnquiryRepository enquiries;
 public AdminFinanceEnquiryController(FinanceEnquiryRepository enquiries){this.enquiries=enquiries;}
 @GetMapping @Transactional(readOnly=true) public List<EnquiryView> list(){return enquiries.findAll().stream().sorted(Comparator.comparing(EnquiryView::createdAt).reversed()).map(e->new EnquiryView(e.getId(),e.getFullName(),e.getEmail(),e.getPhone(),e.getVehicle()==null?null:e.getVehicle().getMake()+" "+e.getVehicle().getModel(),e.getMonthlyIncome(),e.getDepositKes(),e.getPreferredTermMonths(),e.getMessage(),e.getStatus(),e.getCreatedAt())).toList();}
 public record EnquiryView(UUID id,String fullName,String email,String phone,String vehicle,java.math.BigDecimal monthlyIncome,java.math.BigDecimal depositKes,int preferredTermMonths,String message,String status,Instant createdAt){}
}
