package ke.peachcar.admin;

import ke.peachcar.settings.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin/settings")
public class AdminSettingsController {
 private final DealershipSettingsRepository settings;
 public AdminSettingsController(DealershipSettingsRepository settings){this.settings=settings;}
 @GetMapping public DealershipSettings get(){return settings.findById(1L).orElseThrow();}
 @PutMapping public DealershipSettings update(@Valid @RequestBody SettingsRequest body){DealershipSettings s=settings.findById(1L).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));s.setPhoneNumber(body.phoneNumber());s.setWhatsappNumber(body.whatsappNumber());s.setContactEmail(body.contactEmail());return settings.save(s);}
 public record SettingsRequest(@Size(max=30) String phoneNumber,@Size(max=30) String whatsappNumber,@Size(max=180) String contactEmail){}
}
