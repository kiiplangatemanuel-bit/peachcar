package ke.peachcar.vehicle;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="vehicle_images")
public class VehicleImage {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
 @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="vehicle_id",nullable=false) private Vehicle vehicle;
 @Column(name="storage_public_id",nullable=false) private String storagePublicId;
 @Column(name="image_url",nullable=false,columnDefinition="text") private String imageUrl;
 @Column(name="alt_text") private String altText;
 @Column(name="sort_order",nullable=false) private int sortOrder;
 @Column(name="created_at",nullable=false) private Instant createdAt=Instant.now();
 protected VehicleImage(){}
 public VehicleImage(Vehicle vehicle,String publicId,String url,String alt,int order){this.vehicle=vehicle;this.storagePublicId=publicId;this.imageUrl=url;this.altText=alt;this.sortOrder=order;}
 public UUID getId(){return id;} public String getImageUrl(){return imageUrl;} public String getAltText(){return altText;} public int getSortOrder(){return sortOrder;} public String getStoragePublicId(){return storagePublicId;}
}
