package ke.peachcar.publicapi;

import ke.peachcar.vehicle.*;
import ke.peachcar.settings.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {
    private final VehicleRepository vehicles;
    private final DealershipSettingsRepository settings;

    public PublicController(VehicleRepository vehicles, DealershipSettingsRepository settings) {
        this.vehicles = vehicles;
        this.settings = settings;
    }

    @GetMapping("/vehicles")
    public List<Vehicle> vehicles() {
        return vehicles.findByStatusOrderByCreatedAtDesc(VehicleStatus.PUBLISHED);
    }

    @GetMapping("/settings")
    public DealershipSettings settings() {
        return settings.findById(1L).orElseThrow();
    }
}
