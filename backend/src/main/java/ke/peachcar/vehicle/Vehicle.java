package ke.peachcar.vehicle;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, length = 80) private String make;
    @Column(nullable = false, length = 100) private String model;
    @Column(nullable = false) private int year;
    @Column(nullable = false, precision = 14, scale = 2) private BigDecimal priceKes;
    private Integer mileageKm;
    @Column(length = 30) private String fuel;
    @Column(length = 30) private String transmission;
    @Column(length = 40) private String bodyType;
    @Column(length = 40) private String exteriorColour;
    @Column(columnDefinition = "text") private String description;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private VehicleStatus status = VehicleStatus.DRAFT;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    @Column(nullable = false) private Instant updatedAt = Instant.now();

    @PreUpdate void touch() { updatedAt = Instant.now(); }
    public UUID getId() { return id; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public BigDecimal getPriceKes() { return priceKes; }
    public Integer getMileageKm() { return mileageKm; }
    public String getFuel() { return fuel; }
    public String getTransmission() { return transmission; }
    public String getBodyType() { return bodyType; }
    public String getExteriorColour() { return exteriorColour; }
    public String getDescription() { return description; }
    public VehicleStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setMake(String v) { make = v; }
    public void setModel(String v) { model = v; }
    public void setYear(int v) { year = v; }
    public void setPriceKes(BigDecimal v) { priceKes = v; }
    public void setMileageKm(Integer v) { mileageKm = v; }
    public void setFuel(String v) { fuel = v; }
    public void setTransmission(String v) { transmission = v; }
    public void setBodyType(String v) { bodyType = v; }
    public void setExteriorColour(String v) { exteriorColour = v; }
    public void setDescription(String v) { description = v; }
    public void setStatus(VehicleStatus v) { status = v; }
}
