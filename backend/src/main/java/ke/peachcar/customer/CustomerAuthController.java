package ke.peachcar.customer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ke.peachcar.admin.AdminUserRepository;
import java.util.Map;

@RestController
@RequestMapping("/api/customer/auth")
public class CustomerAuthController {
 private final CustomerAccountRepository customers; private final AdminUserRepository admins;
 private final PasswordEncoder passwords; private final AuthenticationManager authentication;
 private final SecurityContextRepository contexts; private final CookieCsrfTokenRepository csrf;
 public CustomerAuthController(CustomerAccountRepository customers,AdminUserRepository admins,PasswordEncoder passwords,AuthenticationManager authentication,SecurityContextRepository contexts,CookieCsrfTokenRepository csrf){this.customers=customers;this.admins=admins;this.passwords=passwords;this.authentication=authentication;this.contexts=contexts;this.csrf=csrf;}
 @PostMapping("/register") public Map<String,String> register(@Valid @RequestBody RegisterRequest body,HttpServletRequest request,HttpServletResponse response){
  String email=body.email().trim().toLowerCase();
  if(admins.findByEmailIgnoreCase(email).isPresent()||customers.findByEmailIgnoreCase(email).isPresent())throw new ResponseStatusException(HttpStatus.CONFLICT,"An account with this email already exists.");
  customers.save(new CustomerAccount(body.fullName().trim(),email,passwords.encode(body.password())));
  return establish(email,body.password(),request,response);
 }
 @PostMapping("/login") public Map<String,String> login(@Valid @RequestBody LoginRequest body,HttpServletRequest request,HttpServletResponse response){return establish(body.email().trim().toLowerCase(),body.password(),request,response);}
 private Map<String,String> establish(String email,String password,HttpServletRequest request,HttpServletResponse response){
  Authentication auth;
  try{auth=authentication.authenticate(new UsernamePasswordAuthenticationToken(email,password));}
  catch(AuthenticationException ex){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password.");}
  if(auth.getAuthorities().stream().noneMatch(a->a.getAuthority().equals("ROLE_CUSTOMER")))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Customer account required.");
  if(request.getSession(false)!=null)request.changeSessionId();
  csrf.saveToken(null,request,response);
  SecurityContext context=SecurityContextHolder.createEmptyContext();context.setAuthentication(auth);SecurityContextHolder.setContext(context);contexts.saveContext(context,request,response);
  return Map.of("email",auth.getName(),"role","CUSTOMER");
 }
 @GetMapping("/me") public Map<String,String> me(Authentication auth){CustomerAccount customer=customers.findByEmailIgnoreCase(auth.getName()).orElseThrow();return Map.of("email",customer.getEmail(),"fullName",customer.getFullName(),"role","CUSTOMER");}
 @PostMapping("/logout") @ResponseStatus(HttpStatus.NO_CONTENT) public void logout(HttpServletRequest request){SecurityContextHolder.clearContext();if(request.getSession(false)!=null)request.getSession(false).invalidate();}
 public record RegisterRequest(@NotBlank @Size(max=120) String fullName,@NotBlank @Email @Size(max=190) String email,@NotBlank @Size(min=10,max=72) String password){}
 public record LoginRequest(@NotBlank @Email @Size(max=190) String email,@NotBlank String password){}
}
