package com.vabv.crm.service.criteria;

import com.vabv.crm.domain.enumeration.VehicleStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.vabv.crm.domain.Vehicle} entity. This class is used
 * in {@link com.vabv.crm.web.rest.VehicleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vehicles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleCriteria implements Serializable, Criteria {

    /**
     * Class for filtering VehicleStatus
     */
    public static class VehicleStatusFilter extends Filter<VehicleStatus> {

        public VehicleStatusFilter() {}

        public VehicleStatusFilter(VehicleStatusFilter filter) {
            super(filter);
        }

        @Override
        public VehicleStatusFilter copy() {
            return new VehicleStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter licensePlate;

    private StringFilter model;

    private StringFilter brand;

    private LocalDateFilter fabricationDate;

    private IntegerFilter modelYear;

    private StringFilter color;

    private StringFilter renavam;

    private StringFilter fuelType;

    private StringFilter chassiNumber;

    private IntegerFilter currentMileage;

    private LocalDateFilter lastMaintenanceDate;

    private IntegerFilter lastMaintenanceMileage;

    private LocalDateFilter nextMaintenanceDate;

    private IntegerFilter nextMaintenanceMileage;

    private StringFilter description;

    private VehicleStatusFilter status;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter clientId;

    private Boolean distinct;

    public VehicleCriteria() {}

    public VehicleCriteria(VehicleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.licensePlate = other.optionalLicensePlate().map(StringFilter::copy).orElse(null);
        this.model = other.optionalModel().map(StringFilter::copy).orElse(null);
        this.brand = other.optionalBrand().map(StringFilter::copy).orElse(null);
        this.fabricationDate = other.optionalFabricationDate().map(LocalDateFilter::copy).orElse(null);
        this.modelYear = other.optionalModelYear().map(IntegerFilter::copy).orElse(null);
        this.color = other.optionalColor().map(StringFilter::copy).orElse(null);
        this.renavam = other.optionalRenavam().map(StringFilter::copy).orElse(null);
        this.fuelType = other.optionalFuelType().map(StringFilter::copy).orElse(null);
        this.chassiNumber = other.optionalChassiNumber().map(StringFilter::copy).orElse(null);
        this.currentMileage = other.optionalCurrentMileage().map(IntegerFilter::copy).orElse(null);
        this.lastMaintenanceDate = other.optionalLastMaintenanceDate().map(LocalDateFilter::copy).orElse(null);
        this.lastMaintenanceMileage = other.optionalLastMaintenanceMileage().map(IntegerFilter::copy).orElse(null);
        this.nextMaintenanceDate = other.optionalNextMaintenanceDate().map(LocalDateFilter::copy).orElse(null);
        this.nextMaintenanceMileage = other.optionalNextMaintenanceMileage().map(IntegerFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(VehicleStatusFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public VehicleCriteria copy() {
        return new VehicleCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLicensePlate() {
        return licensePlate;
    }

    public Optional<StringFilter> optionalLicensePlate() {
        return Optional.ofNullable(licensePlate);
    }

    public StringFilter licensePlate() {
        if (licensePlate == null) {
            setLicensePlate(new StringFilter());
        }
        return licensePlate;
    }

    public void setLicensePlate(StringFilter licensePlate) {
        this.licensePlate = licensePlate;
    }

    public StringFilter getModel() {
        return model;
    }

    public Optional<StringFilter> optionalModel() {
        return Optional.ofNullable(model);
    }

    public StringFilter model() {
        if (model == null) {
            setModel(new StringFilter());
        }
        return model;
    }

    public void setModel(StringFilter model) {
        this.model = model;
    }

    public StringFilter getBrand() {
        return brand;
    }

    public Optional<StringFilter> optionalBrand() {
        return Optional.ofNullable(brand);
    }

    public StringFilter brand() {
        if (brand == null) {
            setBrand(new StringFilter());
        }
        return brand;
    }

    public void setBrand(StringFilter brand) {
        this.brand = brand;
    }

    public LocalDateFilter getFabricationDate() {
        return fabricationDate;
    }

    public Optional<LocalDateFilter> optionalFabricationDate() {
        return Optional.ofNullable(fabricationDate);
    }

    public LocalDateFilter fabricationDate() {
        if (fabricationDate == null) {
            setFabricationDate(new LocalDateFilter());
        }
        return fabricationDate;
    }

    public void setFabricationDate(LocalDateFilter fabricationDate) {
        this.fabricationDate = fabricationDate;
    }

    public IntegerFilter getModelYear() {
        return modelYear;
    }

    public Optional<IntegerFilter> optionalModelYear() {
        return Optional.ofNullable(modelYear);
    }

    public IntegerFilter modelYear() {
        if (modelYear == null) {
            setModelYear(new IntegerFilter());
        }
        return modelYear;
    }

    public void setModelYear(IntegerFilter modelYear) {
        this.modelYear = modelYear;
    }

    public StringFilter getColor() {
        return color;
    }

    public Optional<StringFilter> optionalColor() {
        return Optional.ofNullable(color);
    }

    public StringFilter color() {
        if (color == null) {
            setColor(new StringFilter());
        }
        return color;
    }

    public void setColor(StringFilter color) {
        this.color = color;
    }

    public StringFilter getRenavam() {
        return renavam;
    }

    public Optional<StringFilter> optionalRenavam() {
        return Optional.ofNullable(renavam);
    }

    public StringFilter renavam() {
        if (renavam == null) {
            setRenavam(new StringFilter());
        }
        return renavam;
    }

    public void setRenavam(StringFilter renavam) {
        this.renavam = renavam;
    }

    public StringFilter getFuelType() {
        return fuelType;
    }

    public Optional<StringFilter> optionalFuelType() {
        return Optional.ofNullable(fuelType);
    }

    public StringFilter fuelType() {
        if (fuelType == null) {
            setFuelType(new StringFilter());
        }
        return fuelType;
    }

    public void setFuelType(StringFilter fuelType) {
        this.fuelType = fuelType;
    }

    public StringFilter getChassiNumber() {
        return chassiNumber;
    }

    public Optional<StringFilter> optionalChassiNumber() {
        return Optional.ofNullable(chassiNumber);
    }

    public StringFilter chassiNumber() {
        if (chassiNumber == null) {
            setChassiNumber(new StringFilter());
        }
        return chassiNumber;
    }

    public void setChassiNumber(StringFilter chassiNumber) {
        this.chassiNumber = chassiNumber;
    }

    public IntegerFilter getCurrentMileage() {
        return currentMileage;
    }

    public Optional<IntegerFilter> optionalCurrentMileage() {
        return Optional.ofNullable(currentMileage);
    }

    public IntegerFilter currentMileage() {
        if (currentMileage == null) {
            setCurrentMileage(new IntegerFilter());
        }
        return currentMileage;
    }

    public void setCurrentMileage(IntegerFilter currentMileage) {
        this.currentMileage = currentMileage;
    }

    public LocalDateFilter getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public Optional<LocalDateFilter> optionalLastMaintenanceDate() {
        return Optional.ofNullable(lastMaintenanceDate);
    }

    public LocalDateFilter lastMaintenanceDate() {
        if (lastMaintenanceDate == null) {
            setLastMaintenanceDate(new LocalDateFilter());
        }
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(LocalDateFilter lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public IntegerFilter getLastMaintenanceMileage() {
        return lastMaintenanceMileage;
    }

    public Optional<IntegerFilter> optionalLastMaintenanceMileage() {
        return Optional.ofNullable(lastMaintenanceMileage);
    }

    public IntegerFilter lastMaintenanceMileage() {
        if (lastMaintenanceMileage == null) {
            setLastMaintenanceMileage(new IntegerFilter());
        }
        return lastMaintenanceMileage;
    }

    public void setLastMaintenanceMileage(IntegerFilter lastMaintenanceMileage) {
        this.lastMaintenanceMileage = lastMaintenanceMileage;
    }

    public LocalDateFilter getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public Optional<LocalDateFilter> optionalNextMaintenanceDate() {
        return Optional.ofNullable(nextMaintenanceDate);
    }

    public LocalDateFilter nextMaintenanceDate() {
        if (nextMaintenanceDate == null) {
            setNextMaintenanceDate(new LocalDateFilter());
        }
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(LocalDateFilter nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    public IntegerFilter getNextMaintenanceMileage() {
        return nextMaintenanceMileage;
    }

    public Optional<IntegerFilter> optionalNextMaintenanceMileage() {
        return Optional.ofNullable(nextMaintenanceMileage);
    }

    public IntegerFilter nextMaintenanceMileage() {
        if (nextMaintenanceMileage == null) {
            setNextMaintenanceMileage(new IntegerFilter());
        }
        return nextMaintenanceMileage;
    }

    public void setNextMaintenanceMileage(IntegerFilter nextMaintenanceMileage) {
        this.nextMaintenanceMileage = nextMaintenanceMileage;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public VehicleStatusFilter getStatus() {
        return status;
    }

    public Optional<VehicleStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public VehicleStatusFilter status() {
        if (status == null) {
            setStatus(new VehicleStatusFilter());
        }
        return status;
    }

    public void setStatus(VehicleStatusFilter status) {
        this.status = status;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Optional<StringFilter> optionalLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            setLastModifiedBy(new StringFilter());
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public Optional<LongFilter> optionalClientId() {
        return Optional.ofNullable(clientId);
    }

    public LongFilter clientId() {
        if (clientId == null) {
            setClientId(new LongFilter());
        }
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VehicleCriteria that = (VehicleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(licensePlate, that.licensePlate) &&
            Objects.equals(model, that.model) &&
            Objects.equals(brand, that.brand) &&
            Objects.equals(fabricationDate, that.fabricationDate) &&
            Objects.equals(modelYear, that.modelYear) &&
            Objects.equals(color, that.color) &&
            Objects.equals(renavam, that.renavam) &&
            Objects.equals(fuelType, that.fuelType) &&
            Objects.equals(chassiNumber, that.chassiNumber) &&
            Objects.equals(currentMileage, that.currentMileage) &&
            Objects.equals(lastMaintenanceDate, that.lastMaintenanceDate) &&
            Objects.equals(lastMaintenanceMileage, that.lastMaintenanceMileage) &&
            Objects.equals(nextMaintenanceDate, that.nextMaintenanceDate) &&
            Objects.equals(nextMaintenanceMileage, that.nextMaintenanceMileage) &&
            Objects.equals(description, that.description) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            licensePlate,
            model,
            brand,
            fabricationDate,
            modelYear,
            color,
            renavam,
            fuelType,
            chassiNumber,
            currentMileage,
            lastMaintenanceDate,
            lastMaintenanceMileage,
            nextMaintenanceDate,
            nextMaintenanceMileage,
            description,
            status,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            clientId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLicensePlate().map(f -> "licensePlate=" + f + ", ").orElse("") +
            optionalModel().map(f -> "model=" + f + ", ").orElse("") +
            optionalBrand().map(f -> "brand=" + f + ", ").orElse("") +
            optionalFabricationDate().map(f -> "fabricationDate=" + f + ", ").orElse("") +
            optionalModelYear().map(f -> "modelYear=" + f + ", ").orElse("") +
            optionalColor().map(f -> "color=" + f + ", ").orElse("") +
            optionalRenavam().map(f -> "renavam=" + f + ", ").orElse("") +
            optionalFuelType().map(f -> "fuelType=" + f + ", ").orElse("") +
            optionalChassiNumber().map(f -> "chassiNumber=" + f + ", ").orElse("") +
            optionalCurrentMileage().map(f -> "currentMileage=" + f + ", ").orElse("") +
            optionalLastMaintenanceDate().map(f -> "lastMaintenanceDate=" + f + ", ").orElse("") +
            optionalLastMaintenanceMileage().map(f -> "lastMaintenanceMileage=" + f + ", ").orElse("") +
            optionalNextMaintenanceDate().map(f -> "nextMaintenanceDate=" + f + ", ").orElse("") +
            optionalNextMaintenanceMileage().map(f -> "nextMaintenanceMileage=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
