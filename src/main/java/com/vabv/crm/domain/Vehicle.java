package com.vabv.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vabv.crm.domain.enumeration.VehicleStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Entidade Vehicle
 * - ID gerado automaticamente pelo JHipster
 * - Campos de descrição do veículo
 * - Status como enum
 * - Validações para placa e outros campos
 * - Campos de auditoria incluídos
 */
@Entity
@Table(name = "vehicle")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Formato: ABC1234 ou ABC1D23
     */
    @NotNull
    @Pattern(regexp = "^[A-Z0-9]{7}$")
    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "model", length = 50, nullable = false)
    private String model;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "brand", length = 50, nullable = false)
    private String brand;

    @NotNull
    @Column(name = "fabrication_date", nullable = false)
    private LocalDate fabricationDate;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2100)
    @Column(name = "model_year", nullable = false)
    private Integer modelYear;

    @NotNull
    @Column(name = "color", nullable = false)
    private String color;

    @Pattern(regexp = "^[0-9]{9,11}$")
    @Column(name = "renavam", unique = true)
    private String renavam;

    @NotNull
    @Column(name = "fuel_type", nullable = false)
    private String fuelType;

    @Pattern(regexp = "^[A-Z0-9]{17}$")
    @Column(name = "chassi_number", unique = true)
    private String chassiNumber;

    @NotNull
    @Min(value = 0)
    @Column(name = "current_mileage", nullable = false)
    private Integer currentMileage;

    @Column(name = "last_maintenance_date")
    private LocalDate lastMaintenanceDate;

    @Min(value = 0)
    @Column(name = "last_maintenance_mileage")
    private Integer lastMaintenanceMileage;

    @Column(name = "next_maintenance_date")
    private LocalDate nextMaintenanceDate;

    @Min(value = 0)
    @Column(name = "next_maintenance_mileage")
    private Integer nextMaintenanceMileage;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VehicleStatus status;

    /**
     * Campos de auditoria
     */
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "vehicles" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vehicle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return this.licensePlate;
    }

    public Vehicle licensePlate(String licensePlate) {
        this.setLicensePlate(licensePlate);
        return this;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getModel() {
        return this.model;
    }

    public Vehicle model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return this.brand;
    }

    public Vehicle brand(String brand) {
        this.setBrand(brand);
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public LocalDate getFabricationDate() {
        return this.fabricationDate;
    }

    public Vehicle fabricationDate(LocalDate fabricationDate) {
        this.setFabricationDate(fabricationDate);
        return this;
    }

    public void setFabricationDate(LocalDate fabricationDate) {
        this.fabricationDate = fabricationDate;
    }

    public Integer getModelYear() {
        return this.modelYear;
    }

    public Vehicle modelYear(Integer modelYear) {
        this.setModelYear(modelYear);
        return this;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public String getColor() {
        return this.color;
    }

    public Vehicle color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRenavam() {
        return this.renavam;
    }

    public Vehicle renavam(String renavam) {
        this.setRenavam(renavam);
        return this;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
    }

    public String getFuelType() {
        return this.fuelType;
    }

    public Vehicle fuelType(String fuelType) {
        this.setFuelType(fuelType);
        return this;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getChassiNumber() {
        return this.chassiNumber;
    }

    public Vehicle chassiNumber(String chassiNumber) {
        this.setChassiNumber(chassiNumber);
        return this;
    }

    public void setChassiNumber(String chassiNumber) {
        this.chassiNumber = chassiNumber;
    }

    public Integer getCurrentMileage() {
        return this.currentMileage;
    }

    public Vehicle currentMileage(Integer currentMileage) {
        this.setCurrentMileage(currentMileage);
        return this;
    }

    public void setCurrentMileage(Integer currentMileage) {
        this.currentMileage = currentMileage;
    }

    public LocalDate getLastMaintenanceDate() {
        return this.lastMaintenanceDate;
    }

    public Vehicle lastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.setLastMaintenanceDate(lastMaintenanceDate);
        return this;
    }

    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public Integer getLastMaintenanceMileage() {
        return this.lastMaintenanceMileage;
    }

    public Vehicle lastMaintenanceMileage(Integer lastMaintenanceMileage) {
        this.setLastMaintenanceMileage(lastMaintenanceMileage);
        return this;
    }

    public void setLastMaintenanceMileage(Integer lastMaintenanceMileage) {
        this.lastMaintenanceMileage = lastMaintenanceMileage;
    }

    public LocalDate getNextMaintenanceDate() {
        return this.nextMaintenanceDate;
    }

    public Vehicle nextMaintenanceDate(LocalDate nextMaintenanceDate) {
        this.setNextMaintenanceDate(nextMaintenanceDate);
        return this;
    }

    public void setNextMaintenanceDate(LocalDate nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    public Integer getNextMaintenanceMileage() {
        return this.nextMaintenanceMileage;
    }

    public Vehicle nextMaintenanceMileage(Integer nextMaintenanceMileage) {
        this.setNextMaintenanceMileage(nextMaintenanceMileage);
        return this;
    }

    public void setNextMaintenanceMileage(Integer nextMaintenanceMileage) {
        this.nextMaintenanceMileage = nextMaintenanceMileage;
    }

    public String getDescription() {
        return this.description;
    }

    public Vehicle description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VehicleStatus getStatus() {
        return this.status;
    }

    public Vehicle status(VehicleStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Vehicle createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Vehicle createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Vehicle lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Vehicle lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Vehicle client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehicle)) {
            return false;
        }
        return getId() != null && getId().equals(((Vehicle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicle{" +
            "id=" + getId() +
            ", licensePlate='" + getLicensePlate() + "'" +
            ", model='" + getModel() + "'" +
            ", brand='" + getBrand() + "'" +
            ", fabricationDate='" + getFabricationDate() + "'" +
            ", modelYear=" + getModelYear() +
            ", color='" + getColor() + "'" +
            ", renavam='" + getRenavam() + "'" +
            ", fuelType='" + getFuelType() + "'" +
            ", chassiNumber='" + getChassiNumber() + "'" +
            ", currentMileage=" + getCurrentMileage() +
            ", lastMaintenanceDate='" + getLastMaintenanceDate() + "'" +
            ", lastMaintenanceMileage=" + getLastMaintenanceMileage() +
            ", nextMaintenanceDate='" + getNextMaintenanceDate() + "'" +
            ", nextMaintenanceMileage=" + getNextMaintenanceMileage() +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
