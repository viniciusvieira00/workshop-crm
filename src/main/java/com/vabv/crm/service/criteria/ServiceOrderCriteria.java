package com.vabv.crm.service.criteria;

import com.vabv.crm.domain.enumeration.ServiceOrderStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.vabv.crm.domain.ServiceOrder} entity. This class is used
 * in {@link com.vabv.crm.web.rest.ServiceOrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /service-orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceOrderCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ServiceOrderStatus
     */
    public static class ServiceOrderStatusFilter extends Filter<ServiceOrderStatus> {

        public ServiceOrderStatusFilter() {}

        public ServiceOrderStatusFilter(ServiceOrderStatusFilter filter) {
            super(filter);
        }

        @Override
        public ServiceOrderStatusFilter copy() {
            return new ServiceOrderStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private InstantFilter startDate;

    private InstantFilter completionDate;

    private BigDecimalFilter initialCost;

    private BigDecimalFilter additionalCost;

    private BigDecimalFilter totalCost;

    private StringFilter notes;

    private ServiceOrderStatusFilter status;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter typeId;

    private LongFilter vehicleId;

    private LongFilter productsId;

    private Boolean distinct;

    public ServiceOrderCriteria() {}

    public ServiceOrderCriteria(ServiceOrderCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.creationDate = other.optionalCreationDate().map(InstantFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.completionDate = other.optionalCompletionDate().map(InstantFilter::copy).orElse(null);
        this.initialCost = other.optionalInitialCost().map(BigDecimalFilter::copy).orElse(null);
        this.additionalCost = other.optionalAdditionalCost().map(BigDecimalFilter::copy).orElse(null);
        this.totalCost = other.optionalTotalCost().map(BigDecimalFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ServiceOrderStatusFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.typeId = other.optionalTypeId().map(LongFilter::copy).orElse(null);
        this.vehicleId = other.optionalVehicleId().map(LongFilter::copy).orElse(null);
        this.productsId = other.optionalProductsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ServiceOrderCriteria copy() {
        return new ServiceOrderCriteria(this);
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

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public Optional<InstantFilter> optionalCreationDate() {
        return Optional.ofNullable(creationDate);
    }

    public InstantFilter creationDate() {
        if (creationDate == null) {
            setCreationDate(new InstantFilter());
        }
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public Optional<InstantFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            setStartDate(new InstantFilter());
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getCompletionDate() {
        return completionDate;
    }

    public Optional<InstantFilter> optionalCompletionDate() {
        return Optional.ofNullable(completionDate);
    }

    public InstantFilter completionDate() {
        if (completionDate == null) {
            setCompletionDate(new InstantFilter());
        }
        return completionDate;
    }

    public void setCompletionDate(InstantFilter completionDate) {
        this.completionDate = completionDate;
    }

    public BigDecimalFilter getInitialCost() {
        return initialCost;
    }

    public Optional<BigDecimalFilter> optionalInitialCost() {
        return Optional.ofNullable(initialCost);
    }

    public BigDecimalFilter initialCost() {
        if (initialCost == null) {
            setInitialCost(new BigDecimalFilter());
        }
        return initialCost;
    }

    public void setInitialCost(BigDecimalFilter initialCost) {
        this.initialCost = initialCost;
    }

    public BigDecimalFilter getAdditionalCost() {
        return additionalCost;
    }

    public Optional<BigDecimalFilter> optionalAdditionalCost() {
        return Optional.ofNullable(additionalCost);
    }

    public BigDecimalFilter additionalCost() {
        if (additionalCost == null) {
            setAdditionalCost(new BigDecimalFilter());
        }
        return additionalCost;
    }

    public void setAdditionalCost(BigDecimalFilter additionalCost) {
        this.additionalCost = additionalCost;
    }

    public BigDecimalFilter getTotalCost() {
        return totalCost;
    }

    public Optional<BigDecimalFilter> optionalTotalCost() {
        return Optional.ofNullable(totalCost);
    }

    public BigDecimalFilter totalCost() {
        if (totalCost == null) {
            setTotalCost(new BigDecimalFilter());
        }
        return totalCost;
    }

    public void setTotalCost(BigDecimalFilter totalCost) {
        this.totalCost = totalCost;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public Optional<StringFilter> optionalNotes() {
        return Optional.ofNullable(notes);
    }

    public StringFilter notes() {
        if (notes == null) {
            setNotes(new StringFilter());
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public ServiceOrderStatusFilter getStatus() {
        return status;
    }

    public Optional<ServiceOrderStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ServiceOrderStatusFilter status() {
        if (status == null) {
            setStatus(new ServiceOrderStatusFilter());
        }
        return status;
    }

    public void setStatus(ServiceOrderStatusFilter status) {
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

    public LongFilter getTypeId() {
        return typeId;
    }

    public Optional<LongFilter> optionalTypeId() {
        return Optional.ofNullable(typeId);
    }

    public LongFilter typeId() {
        if (typeId == null) {
            setTypeId(new LongFilter());
        }
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public LongFilter getVehicleId() {
        return vehicleId;
    }

    public Optional<LongFilter> optionalVehicleId() {
        return Optional.ofNullable(vehicleId);
    }

    public LongFilter vehicleId() {
        if (vehicleId == null) {
            setVehicleId(new LongFilter());
        }
        return vehicleId;
    }

    public void setVehicleId(LongFilter vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LongFilter getProductsId() {
        return productsId;
    }

    public Optional<LongFilter> optionalProductsId() {
        return Optional.ofNullable(productsId);
    }

    public LongFilter productsId() {
        if (productsId == null) {
            setProductsId(new LongFilter());
        }
        return productsId;
    }

    public void setProductsId(LongFilter productsId) {
        this.productsId = productsId;
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
        final ServiceOrderCriteria that = (ServiceOrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(completionDate, that.completionDate) &&
            Objects.equals(initialCost, that.initialCost) &&
            Objects.equals(additionalCost, that.additionalCost) &&
            Objects.equals(totalCost, that.totalCost) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(vehicleId, that.vehicleId) &&
            Objects.equals(productsId, that.productsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            creationDate,
            startDate,
            completionDate,
            initialCost,
            additionalCost,
            totalCost,
            notes,
            status,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            typeId,
            vehicleId,
            productsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceOrderCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCreationDate().map(f -> "creationDate=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalCompletionDate().map(f -> "completionDate=" + f + ", ").orElse("") +
            optionalInitialCost().map(f -> "initialCost=" + f + ", ").orElse("") +
            optionalAdditionalCost().map(f -> "additionalCost=" + f + ", ").orElse("") +
            optionalTotalCost().map(f -> "totalCost=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalTypeId().map(f -> "typeId=" + f + ", ").orElse("") +
            optionalVehicleId().map(f -> "vehicleId=" + f + ", ").orElse("") +
            optionalProductsId().map(f -> "productsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
