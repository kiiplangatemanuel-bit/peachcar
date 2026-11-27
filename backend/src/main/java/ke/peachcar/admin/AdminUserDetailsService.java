package ke.peachcar.admin;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailsService implements UserDetailsService {
    private final AdminUserRepository admins;
    public AdminUserDetailsService(AdminUserRepository admins){this.admins=admins;}
    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser admin=admins.findByEmailIgnoreCase(username).orElseThrow(()->new UsernameNotFoundException("Invalid credentials"));
        return User.withUsername(admin.getEmail()).password(admin.getPasswordHash()).roles("ADMIN").disabled(!admin.isEnabled()).build();
    }
}
