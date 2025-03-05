package com.vabv.crm.service;

import com.vabv.crm.domain.*; // for static metamodels
import com.vabv.crm.domain.ServiceOrderType;
import com.vabv.crm.repository.ServiceOrderTypeRepository;
import com.vabv.crm.service.criteria.ServiceOrderTypeCriteria;
import com.vabv.crm.service.dto.ServiceOrderTypeDTO;
import com.vabv.crm.service.mapper.ServiceOrderTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ServiceOrderType} entities in the database.
 * The main input is a {@link ServiceOrderTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ServiceOrderTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServiceOrderTypeQueryService extends QueryService<ServiceOrderType> {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceOrderTypeQueryService.class);

    private final ServiceOrderTypeRepository serviceOrderTypeRepository;

    private final ServiceOrderTypeMapper serviceOrderTypeMapper;

    public ServiceOrderTypeQueryService(
        ServiceOrderTypeRepository serviceOrderTypeRepository,
        ServiceOrderTypeMapper serviceOrderTypeMapper
    ) {
        this.serviceOrderTypeRepository = serviceOrderTypeRepository;
        this.serviceOrderTypeMapper = serviceOrderTypeMapper;
    }

    /**
     * Return a {@link Page} of {@link ServiceOrderTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ServiceOrderTypeDTO> findByCriteria(ServiceOrderTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ServiceOrderType> specification = createSpecification(criteria);
        return serviceOrderTypeRepository.findAll(specification, page).map(serviceOrderTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServiceOrderTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ServiceOrderType> specification = createSpecification(criteria);
        return serviceOrderTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ServiceOrderTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ServiceOrderType> createSpecification(ServiceOrderTypeCriteria criteria) {
        Specification<ServiceOrderType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ServiceOrderType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ServiceOrderType_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ServiceOrderType_.description));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ServiceOrderType_.price));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), ServiceOrderType_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), ServiceOrderType_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), ServiceOrderType_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getLastModifiedDate(), ServiceOrderType_.lastModifiedDate)
                );
            }
        }
        return specification;
    }
}
