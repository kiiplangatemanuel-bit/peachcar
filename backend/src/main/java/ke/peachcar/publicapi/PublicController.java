package ke.peachcar.publicapi;

import ke.peachcar.vehicle.*;
import ke.peachcar.settings.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public")
public class PublicController {
 private final VehicleRepository vehicles; private final DealershipSettingsRepository settings; private final VehicleMapper mapper;
 public PublicController(VehicleRepository vehicles,DealershipSettingsRepository settings,VehicleMapper mapper){this.vehicles=vehicles;this.settings=settings;this.mapper=mapper;}
 @GetMapping("/vehicles") public List<VehicleView> vehicles(){return vehicles.findByStatusOrderByCreatedAtDesc(VehicleStatus.PUBLISHED).stream().map(mapper::view).toList();}
 @GetMapping("/vehicles/{id}") public VehicleView vehicle(@PathVariable UUID id){Vehicle v=vehicles.findById(id).filter(x->x.getStatus()==VehicleStatus.PUBLISHED).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));return mapper.view(v);}
 @GetMapping("/settings") public DealershipSettings settings(){return settings.findById(1L).orElseThrow();}
}
