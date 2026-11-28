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
    @Column(name="listing_type",nullable=false,length=24) private String listingType = "LOCALLY_USED";
    @Column(name="duty_status",nullable=false,length=24) private String dutyStatus = "DUTY_PAID";
    @Column(name="inspection_status",nullable=false,length=24) private String inspectionStatus = "NOT_INSPECTED";
    @Column(name="inspection_report",columnDefinition="text") private String inspectionReport;
    @Column(name="inspection_date") private java.time.LocalDate inspectionDate;
    @Column(name="accident_history",nullable=false,length=24) private String accidentHistory = "UNKNOWN";
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
    public String getListingType() { return listingType; }
    public String getDutyStatus() { return dutyStatus; }
    public String getInspectionStatus() { return inspectionStatus; }
    public String getInspectionReport() { return inspectionReport; }
    public java.time.LocalDate getInspectionDate() { return inspectionDate; }
    public String getAccidentHistory() { return accidentHistory; }
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
    public void setListingType(String v) { listingType = v; }
    public void setDutyStatus(String v) { dutyStatus = v; }
    public void setInspectionStatus(String v) { inspectionStatus = v; }
    public void setInspectionReport(String v) { inspectionReport = v; }
    public void setInspectionDate(java.time.LocalDate v) { inspectionDate = v; }
    public void setAccidentHistory(String v) { accidentHistory = v; }
    public void setStatus(VehicleStatus v) { status = v; }
}
