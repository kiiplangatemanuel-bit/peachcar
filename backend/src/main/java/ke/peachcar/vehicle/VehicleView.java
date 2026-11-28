package ke.peachcar.vehicle;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
public record VehicleView(UUID id,String make,String model,int year,BigDecimal priceKes,Integer mileageKm,String fuel,String transmission,String bodyType,String exteriorColour,String description,VehicleStatus status,String listingType,String dutyStatus,String inspectionStatus,String inspectionReport,LocalDate inspectionDate,String accidentHistory,List<ImageView> images,Instant createdAt){
 public record ImageView(UUID id,String url,String altText,int sortOrder){}
}
