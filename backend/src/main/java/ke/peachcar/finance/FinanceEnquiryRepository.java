package ke.peachcar.finance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface FinanceEnquiryRepository extends JpaRepository<FinanceEnquiry,UUID>{}
