package ke.peachcar.admin;

import ke.peachcar.vehicle.*;
import ke.peachcar.media.ImageStorageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/admin/vehicles")
public class AdminVehicleController {
 private final VehicleRepository vehicles; private final VehicleImageRepository images; private final ImageStorageService storage; private final VehicleMapper mapper;
 public AdminVehicleController(VehicleRepository vehicles,VehicleImageRepository images,ImageStorageService storage,VehicleMapper mapper){this.vehicles=vehicles;this.images=images;this.storage=storage;this.mapper=mapper;}
 @GetMapping public List<VehicleView> all(){return vehicles.findAll().stream().sorted(Comparator.comparing(Vehicle::getCreatedAt).reversed()).map(mapper::view).toList();}
 @PostMapping public VehicleView create(@Valid @RequestBody VehicleRequest body){Vehicle v=new Vehicle();apply(v,body);v.setStatus(VehicleStatus.DRAFT);return mapper.view(vehicles.save(v));}
 @PutMapping("/{id}") public VehicleView update(@PathVariable UUID id,@Valid @RequestBody VehicleRequest body){Vehicle v=find(id);apply(v,body);return mapper.view(vehicles.save(v));}
 @PatchMapping("/{id}/status") public VehicleView status(@PathVariable UUID id,@Valid @RequestBody StatusRequest body){Vehicle v=find(id);if(body.status()==VehicleStatus.PUBLISHED&&images.findByVehicleIdOrderBySortOrderAsc(id).isEmpty())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Upload at least one vehicle image before publishing.");v.setStatus(body.status());return mapper.view(vehicles.save(v));}
 @PostMapping(value="/{id}/images",consumes="multipart/form-data") @Transactional public VehicleView upload(@PathVariable UUID id,@RequestPart("files") List<MultipartFile> files) throws IOException {
  Vehicle v=find(id);if(files==null||files.isEmpty()||files.size()>8)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Upload between 1 and 8 images per request.");
  int order=images.findByVehicleIdOrderBySortOrderAsc(id).size();
  if(order+files.size()>20)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"A vehicle may have at most 20 images.");
  for(MultipartFile file:files){ImageStorageService.StoredImage stored=storage.upload(file);images.save(new VehicleImage(v,stored.publicId(),stored.secureUrl(),v.getMake()+" "+v.getModel(),order++));}
  return mapper.view(v);
 }
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @Transactional public void delete(@PathVariable UUID id) throws IOException {find(id);for(VehicleImage image:images.findByVehicleIdOrderBySortOrderAsc(id)){storage.delete(image.getStoragePublicId());}vehicles.deleteById(id);}
 private Vehicle find(UUID id){return vehicles.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Vehicle not found"));}
 private void apply(Vehicle v,VehicleRequest b){v.setMake(b.make().trim());v.setModel(b.model().trim());v.setYear(b.year());v.setPriceKes(b.priceKes());v.setMileageKm(b.mileageKm());v.setFuel(b.fuel());v.setTransmission(b.transmission());v.setBodyType(b.bodyType());v.setExteriorColour(b.exteriorColour());v.setDescription(b.description());}
 public record VehicleRequest(@NotBlank String make,@NotBlank String model,@Min(1950) @Max(2100) int year,@NotNull @DecimalMin("0.00") BigDecimal priceKes,@Min(0) Integer mileageKm,String fuel,String transmission,String bodyType,String exteriorColour,String description){}
 public record StatusRequest(@NotNull VehicleStatus status){}
}
