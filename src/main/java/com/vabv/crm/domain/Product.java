package com.vabv.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidade Product
 * - Produtos utilizados nas ordens de serviço
 * - ID gerado automaticamente pelo JHipster
 */
@Entity
@Table(name = "product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Nome do produto
     */
    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    /**
     * Descrição do produto
     */
    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * Marca do produto
     */
    @Size(max = 50)
    @Column(name = "brand", length = 50)
    private String brand;

    /**
     * Preço de custo
     */
    @DecimalMin(value = "0")
    @Column(name = "cost_price", precision = 21, scale = 2)
    private BigDecimal costPrice;

    /**
     * Preço de venda
     */
    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "sell_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal sellPrice;

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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    @JsonIgnoreProperties(value = { "type", "vehicle", "products" }, allowSetters = true)
    private Set<ServiceOrder> serviceOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return this.brand;
    }

    public Product brand(String brand) {
        this.setBrand(brand);
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getCostPrice() {
        return this.costPrice;
    }

    public Product costPrice(BigDecimal costPrice) {
        this.setCostPrice(costPrice);
        return this;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getSellPrice() {
        return this.sellPrice;
    }

    public Product sellPrice(BigDecimal sellPrice) {
        this.setSellPrice(sellPrice);
        return this;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Product createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Product createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Product lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Product lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<ServiceOrder> getServiceOrders() {
        return this.serviceOrders;
    }

    public void setServiceOrders(Set<ServiceOrder> serviceOrders) {
        if (this.serviceOrders != null) {
            this.serviceOrders.forEach(i -> i.removeProducts(this));
        }
        if (serviceOrders != null) {
            serviceOrders.forEach(i -> i.addProducts(this));
        }
        this.serviceOrders = serviceOrders;
    }

    public Product serviceOrders(Set<ServiceOrder> serviceOrders) {
        this.setServiceOrders(serviceOrders);
        return this;
    }

    public Product addServiceOrders(ServiceOrder serviceOrder) {
        this.serviceOrders.add(serviceOrder);
        serviceOrder.getProducts().add(this);
        return this;
    }

    public Product removeServiceOrders(ServiceOrder serviceOrder) {
        this.serviceOrders.remove(serviceOrder);
        serviceOrder.getProducts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", brand='" + getBrand() + "'" +
            ", costPrice=" + getCostPrice() +
            ", sellPrice=" + getSellPrice() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
