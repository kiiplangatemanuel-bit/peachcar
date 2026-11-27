package ke.peachcar.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.context.*;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository contexts;
    private final CookieCsrfTokenRepository csrfTokens;
    public AdminAuthController(AuthenticationManager manager, SecurityContextRepository contexts, CookieCsrfTokenRepository csrfTokens){this.authenticationManager=manager;this.contexts=contexts;this.csrfTokens=csrfTokens;}
    @PostMapping("/login")
    public Map<String,String> login(@RequestBody LoginRequest body,HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response){
        Authentication authentication;\n        try { authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(body.email(),body.password())); }\n        catch (AuthenticationException ex) { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password"); }
        if(request.getSession(false)!=null) request.changeSessionId();\n        csrfTokens.saveToken(null,request,response);\n        SecurityContext context=SecurityContextHolder.createEmptyContext();context.setAuthentication(authentication);SecurityContextHolder.setContext(context);contexts.saveContext(context,request,response);
        return Map.of("email",authentication.getName(),"role","ADMIN");
    }
    @GetMapping("/me") public Map<String,String> me(Authentication auth){return Map.of("email",auth.getName(),"role","ADMIN");}
    public record LoginRequest(String email,String password){}
}
