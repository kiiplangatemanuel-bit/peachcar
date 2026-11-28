package ke.peachcar.admin;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailsService implements UserDetailsService {
    private final AdminUserRepository admins; private final ke.peachcar.customer.CustomerAccountRepository customers;
    public AdminUserDetailsService(AdminUserRepository admins,ke.peachcar.customer.CustomerAccountRepository customers){this.admins=admins;this.customers=customers;}
    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser admin=admins.findByEmailIgnoreCase(username).orElse(null);
        if(admin!=null)return User.withUsername(admin.getEmail()).password(admin.getPasswordHash()).roles("ADMIN").disabled(!admin.isEnabled()).build();
        var customer=customers.findByEmailIgnoreCase(username).orElseThrow(()->new UsernameNotFoundException("Invalid credentials"));
        return User.withUsername(customer.getEmail()).password(customer.getPasswordHash()).roles("CUSTOMER").disabled(!customer.isEnabled()).build();
    }
}
