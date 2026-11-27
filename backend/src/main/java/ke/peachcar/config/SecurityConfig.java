package ke.peachcar.config;

import ke.peachcar.admin.AdminUserDetailsService;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.*;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder(12);}
    @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{return config.getAuthenticationManager();}
    @Bean SecurityContextRepository securityContextRepository(){return new HttpSessionSecurityContextRepository();}
    @Bean org.springframework.security.web.csrf.CookieCsrfTokenRepository csrfTokenRepository(){org.springframework.security.web.csrf.CookieCsrfTokenRepository repo=org.springframework.security.web.csrf.CookieCsrfTokenRepository.withHttpOnlyFalse();repo.setCookiePath("/");return repo;}
    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityContextRepository repository, org.springframework.security.web.csrf.CookieCsrfTokenRepository csrf) throws Exception {
        http
          .cors(c->{}).csrf(c->c.csrfTokenRepository(csrf).csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
          .securityContext(c->c.securityContextRepository(repository))
          .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).sessionFixation(f->f.migrateSession()))
          .authorizeHttpRequests(a->a
             .requestMatchers("/api/public/**","/api/admin/auth/login","/api/csrf","/actuator/health").permitAll()
             .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
             .requestMatchers("/api/admin/**").hasRole("ADMIN")
             .anyRequest().permitAll())
          .logout(l->l.logoutUrl("/api/admin/auth/logout").logoutSuccessHandler((req,res,auth)->res.setStatus(204)));
        return http.build();
    }
    @Bean CorsConfigurationSource corsConfigurationSource(@org.springframework.beans.factory.annotation.Value("${APP_ORIGIN:http://localhost:3000}") String origin){
        CorsConfiguration c=new CorsConfiguration(); c.setAllowedOrigins(List.of(origin)); c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS")); c.setAllowedHeaders(List.of("Content-Type","X-XSRF-TOKEN")); c.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();source.registerCorsConfiguration("/**",c);return source;
    }
}
