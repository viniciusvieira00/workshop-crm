package com.vabv.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vabv.crm.domain.enumeration.ServiceOrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidade ServiceOrder
 * - Ordens de serviço para veículos
 * - ID gerado automaticamente pelo JHipster
 * - Relacionada com Vehicle e ServiceOrderType
 */
@Entity
@Table(name = "service_order")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Data de criação da ordem de serviço
     */
    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    /**
     * Data de início do serviço
     */
    @Column(name = "start_date")
    private Instant startDate;

    /**
     * Data de conclusão do serviço
     */
    @Column(name = "completion_date")
    private Instant completionDate;

    /**
     * Custo inicial estimado
     */
    @DecimalMin(value = "0")
    @Column(name = "initial_cost", precision = 21, scale = 2)
    private BigDecimal initialCost;

    /**
     * Custos adicionais
     */
    @DecimalMin(value = "0")
    @Column(name = "additional_cost", precision = 21, scale = 2)
    private BigDecimal additionalCost;

    /**
     * Custo total da ordem de serviço
     */
    @DecimalMin(value = "0")
    @Column(name = "total_cost", precision = 21, scale = 2)
    private BigDecimal totalCost;

    /**
     * Observações sobre o serviço
     */
    @Size(max = 2000)
    @Column(name = "notes", length = 2000)
    private String notes;

    /**
     * Status da ordem de serviço
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ServiceOrderStatus status;

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
    private ServiceOrderType type;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    private Vehicle vehicle;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_service_order__products",
        joinColumns = @JoinColumn(name = "service_order_id"),
        inverseJoinColumns = @JoinColumn(name = "products_id")
    )
    @JsonIgnoreProperties(value = { "serviceOrders" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public ServiceOrder creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public ServiceOrder startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getCompletionDate() {
        return this.completionDate;
    }

    public ServiceOrder completionDate(Instant completionDate) {
        this.setCompletionDate(completionDate);
        return this;
    }

    public void setCompletionDate(Instant completionDate) {
        this.completionDate = completionDate;
    }

    public BigDecimal getInitialCost() {
        return this.initialCost;
    }

    public ServiceOrder initialCost(BigDecimal initialCost) {
        this.setInitialCost(initialCost);
        return this;
    }

    public void setInitialCost(BigDecimal initialCost) {
        this.initialCost = initialCost;
    }

    public BigDecimal getAdditionalCost() {
        return this.additionalCost;
    }

    public ServiceOrder additionalCost(BigDecimal additionalCost) {
        this.setAdditionalCost(additionalCost);
        return this;
    }

    public void setAdditionalCost(BigDecimal additionalCost) {
        this.additionalCost = additionalCost;
    }

    public BigDecimal getTotalCost() {
        return this.totalCost;
    }

    public ServiceOrder totalCost(BigDecimal totalCost) {
        this.setTotalCost(totalCost);
        return this;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getNotes() {
        return this.notes;
    }

    public ServiceOrder notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ServiceOrderStatus getStatus() {
        return this.status;
    }

    public ServiceOrder status(ServiceOrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ServiceOrderStatus status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ServiceOrder createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ServiceOrder createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public ServiceOrder lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public ServiceOrder lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public ServiceOrderType getType() {
        return this.type;
    }

    public void setType(ServiceOrderType serviceOrderType) {
        this.type = serviceOrderType;
    }

    public ServiceOrder type(ServiceOrderType serviceOrderType) {
        this.setType(serviceOrderType);
        return this;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public ServiceOrder vehicle(Vehicle vehicle) {
        this.setVehicle(vehicle);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public ServiceOrder products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public ServiceOrder addProducts(Product product) {
        this.products.add(product);
        return this;
    }

    public ServiceOrder removeProducts(Product product) {
        this.products.remove(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceOrder)) {
            return false;
        }
        return getId() != null && getId().equals(((ServiceOrder) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceOrder{" +
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
            "}";
    }
}
