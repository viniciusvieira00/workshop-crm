package com.vabv.crm.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.vabv.crm.domain.ServiceOrderType} entity.
 */
@Schema(
    description = "Entidade ServiceOrderType\n- Tipos de ordens de serviço com preços padrão\n- ID gerado automaticamente pelo JHipster"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceOrderTypeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @Schema(description = "Nome do tipo de serviço", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 1000)
    @Schema(description = "Descrição detalhada do tipo de serviço")
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    @Schema(description = "Preço padrão do serviço", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @Schema(description = "Campos de auditoria")
    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceOrderTypeDTO)) {
            return false;
        }

        ServiceOrderTypeDTO serviceOrderTypeDTO = (ServiceOrderTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serviceOrderTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceOrderTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
