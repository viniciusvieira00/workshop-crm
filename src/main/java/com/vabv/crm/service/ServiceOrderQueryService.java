package com.vabv.crm.service;

import com.vabv.crm.domain.*; // for static metamodels
import com.vabv.crm.domain.ServiceOrder;
import com.vabv.crm.repository.ServiceOrderRepository;
import com.vabv.crm.service.criteria.ServiceOrderCriteria;
import com.vabv.crm.service.dto.ServiceOrderDTO;
import com.vabv.crm.service.mapper.ServiceOrderMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ServiceOrder} entities in the database.
 * The main input is a {@link ServiceOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ServiceOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServiceOrderQueryService extends QueryService<ServiceOrder> {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceOrderQueryService.class);

    private final ServiceOrderRepository serviceOrderRepository;

    private final ServiceOrderMapper serviceOrderMapper;

    public ServiceOrderQueryService(ServiceOrderRepository serviceOrderRepository, ServiceOrderMapper serviceOrderMapper) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.serviceOrderMapper = serviceOrderMapper;
    }

    /**
     * Return a {@link Page} of {@link ServiceOrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ServiceOrderDTO> findByCriteria(ServiceOrderCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ServiceOrder> specification = createSpecification(criteria);
        return serviceOrderRepository
            .fetchBagRelationships(serviceOrderRepository.findAll(specification, page))
            .map(serviceOrderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServiceOrderCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ServiceOrder> specification = createSpecification(criteria);
        return serviceOrderRepository.count(specification);
    }

    /**
     * Function to convert {@link ServiceOrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ServiceOrder> createSpecification(ServiceOrderCriteria criteria) {
        Specification<ServiceOrder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ServiceOrder_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), ServiceOrder_.creationDate));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), ServiceOrder_.startDate));
            }
            if (criteria.getCompletionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCompletionDate(), ServiceOrder_.completionDate));
            }
            if (criteria.getInitialCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInitialCost(), ServiceOrder_.initialCost));
            }
            if (criteria.getAdditionalCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAdditionalCost(), ServiceOrder_.additionalCost));
            }
            if (criteria.getTotalCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalCost(), ServiceOrder_.totalCost));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), ServiceOrder_.notes));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ServiceOrder_.status));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), ServiceOrder_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), ServiceOrder_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), ServiceOrder_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), ServiceOrder_.lastModifiedDate));
            }
            if (criteria.getTypeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTypeId(), root -> root.join(ServiceOrder_.type, JoinType.LEFT).get(ServiceOrderType_.id))
                );
            }
            if (criteria.getVehicleId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getVehicleId(), root -> root.join(ServiceOrder_.vehicle, JoinType.LEFT).get(Vehicle_.id))
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root -> root.join(ServiceOrder_.products, JoinType.LEFT).get(Product_.id))
                );
            }
        }
        return specification;
    }
}
