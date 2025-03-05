package com.vabv.crm.service.dto;

import com.vabv.crm.domain.enumeration.VehicleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.vabv.crm.domain.Vehicle} entity.
 */
@Schema(
    description = "Entidade Vehicle\n- ID gerado automaticamente pelo JHipster\n- Campos de descrição do veículo\n- Status como enum\n- Validações para placa e outros campos\n- Campos de auditoria incluídos"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleDTO implements Serializable {

    private Long id;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9]{7}$")
    @Schema(description = "Formato: ABC1234 ou ABC1D23", requiredMode = Schema.RequiredMode.REQUIRED)
    private String licensePlate;

    @NotNull
    @Size(min = 2, max = 50)
    private String model;

    @NotNull
    @Size(min = 2, max = 50)
    private String brand;

    @NotNull
    private LocalDate fabricationDate;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2100)
    private Integer modelYear;

    @NotNull
    private String color;

    @Pattern(regexp = "^[0-9]{9,11}$")
    private String renavam;

    @NotNull
    private String fuelType;

    @Pattern(regexp = "^[A-Z0-9]{17}$")
    private String chassiNumber;

    @NotNull
    @Min(value = 0)
    private Integer currentMileage;

    private LocalDate lastMaintenanceDate;

    @Min(value = 0)
    private Integer lastMaintenanceMileage;

    private LocalDate nextMaintenanceDate;

    @Min(value = 0)
    private Integer nextMaintenanceMileage;

    @Size(max = 1000)
    private String description;

    @NotNull
    private VehicleStatus status;

    @Schema(description = "Campos de auditoria")
    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    @NotNull
    private ClientDTO client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public LocalDate getFabricationDate() {
        return fabricationDate;
    }

    public void setFabricationDate(LocalDate fabricationDate) {
        this.fabricationDate = fabricationDate;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRenavam() {
        return renavam;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getChassiNumber() {
        return chassiNumber;
    }

    public void setChassiNumber(String chassiNumber) {
        this.chassiNumber = chassiNumber;
    }

    public Integer getCurrentMileage() {
        return currentMileage;
    }

    public void setCurrentMileage(Integer currentMileage) {
        this.currentMileage = currentMileage;
    }

    public LocalDate getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public Integer getLastMaintenanceMileage() {
        return lastMaintenanceMileage;
    }

    public void setLastMaintenanceMileage(Integer lastMaintenanceMileage) {
        this.lastMaintenanceMileage = lastMaintenanceMileage;
    }

    public LocalDate getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(LocalDate nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    public Integer getNextMaintenanceMileage() {
        return nextMaintenanceMileage;
    }

    public void setNextMaintenanceMileage(Integer nextMaintenanceMileage) {
        this.nextMaintenanceMileage = nextMaintenanceMileage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleDTO)) {
            return false;
        }

        VehicleDTO vehicleDTO = (VehicleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vehicleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleDTO{" +
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
            ", client=" + getClient() +
            "}";
    }
}
