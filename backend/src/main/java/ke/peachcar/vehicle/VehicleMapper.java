package ke.peachcar.vehicle;
import org.springframework.stereotype.Component;
import java.util.List;
@Component
public class VehicleMapper {
 private final VehicleImageRepository images;
 public VehicleMapper(VehicleImageRepository images){this.images=images;}
 public VehicleView view(Vehicle v){List<VehicleView.ImageView> gallery=images.findByVehicleIdOrderBySortOrderAsc(v.getId()).stream().map(i->new VehicleView.ImageView(i.getId(),i.getImageUrl(),i.getAltText(),i.getSortOrder())).toList();return new VehicleView(v.getId(),v.getMake(),v.getModel(),v.getYear(),v.getPriceKes(),v.getMileageKm(),v.getFuel(),v.getTransmission(),v.getBodyType(),v.getExteriorColour(),v.getDescription(),v.getStatus(),gallery,v.getCreatedAt());}
}
