package com.vabv.crm.service.dto;

import com.vabv.crm.domain.enumeration.ServiceOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.vabv.crm.domain.ServiceOrder} entity.
 */
@Schema(
    description = "Entidade ServiceOrder\n- Ordens de serviço para veículos\n- ID gerado automaticamente pelo JHipster\n- Relacionada com Vehicle e ServiceOrderType"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceOrderDTO implements Serializable {

    private Long id;

    @NotNull
    @Schema(description = "Data de criação da ordem de serviço", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant creationDate;

    @Schema(description = "Data de início do serviço")
    private Instant startDate;

    @Schema(description = "Data de conclusão do serviço")
    private Instant completionDate;

    @DecimalMin(value = "0")
    @Schema(description = "Custo inicial estimado")
    private BigDecimal initialCost;

    @DecimalMin(value = "0")
    @Schema(description = "Custos adicionais")
    private BigDecimal additionalCost;

    @DecimalMin(value = "0")
    @Schema(description = "Custo total da ordem de serviço")
    private BigDecimal totalCost;

    @Size(max = 2000)
    @Schema(description = "Observações sobre o serviço")
    private String notes;

    @NotNull
    @Schema(description = "Status da ordem de serviço", requiredMode = Schema.RequiredMode.REQUIRED)
    private ServiceOrderStatus status;

    @Schema(description = "Campos de auditoria")
    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    @NotNull
    private ServiceOrderTypeDTO type;

    @NotNull
    private VehicleDTO vehicle;

    private Set<ProductDTO> products = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Instant completionDate) {
        this.completionDate = completionDate;
    }

    public BigDecimal getInitialCost() {
        return initialCost;
    }

    public void setInitialCost(BigDecimal initialCost) {
        this.initialCost = initialCost;
    }

    public BigDecimal getAdditionalCost() {
        return additionalCost;
    }

    public void setAdditionalCost(BigDecimal additionalCost) {
        this.additionalCost = additionalCost;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ServiceOrderStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceOrderStatus status) {
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

    public ServiceOrderTypeDTO getType() {
        return type;
    }

    public void setType(ServiceOrderTypeDTO type) {
        this.type = type;
    }

    public VehicleDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleDTO vehicle) {
        this.vehicle = vehicle;
    }

    public Set<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductDTO> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceOrderDTO)) {
            return false;
        }

        ServiceOrderDTO serviceOrderDTO = (ServiceOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serviceOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceOrderDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", completionDate='" + getCompletionDate() + "'" +
            ", initialCost=" + getInitialCost() +
            ", additionalCost=" + getAdditionalCost() +
            ", totalCost=" + getTotalCost() +
            ", notes='" + getNotes() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", type=" + getType() +
            ", vehicle=" + getVehicle() +
            ", products=" + getProducts() +
            "}";
    }
}
