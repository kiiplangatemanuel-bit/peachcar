package ke.peachcar.customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
public interface CustomerAccountRepository extends JpaRepository<CustomerAccount,UUID>{
 Optional<CustomerAccount> findByEmailIgnoreCase(String email);
}
