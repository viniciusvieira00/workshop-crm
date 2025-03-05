package com.vabv.crm.service;

import com.vabv.crm.domain.*; // for static metamodels
import com.vabv.crm.domain.Vehicle;
import com.vabv.crm.repository.VehicleRepository;
import com.vabv.crm.service.criteria.VehicleCriteria;
import com.vabv.crm.service.dto.VehicleDTO;
import com.vabv.crm.service.mapper.VehicleMapper;
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
 * Service for executing complex queries for {@link Vehicle} entities in the database.
 * The main input is a {@link VehicleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link VehicleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VehicleQueryService extends QueryService<Vehicle> {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleQueryService.class);

    private final VehicleRepository vehicleRepository;

    private final VehicleMapper vehicleMapper;

    public VehicleQueryService(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    /**
     * Return a {@link Page} of {@link VehicleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VehicleDTO> findByCriteria(VehicleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vehicle> specification = createSpecification(criteria);
        return vehicleRepository.findAll(specification, page).map(vehicleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VehicleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Vehicle> specification = createSpecification(criteria);
        return vehicleRepository.count(specification);
    }

    /**
     * Function to convert {@link VehicleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vehicle> createSpecification(VehicleCriteria criteria) {
        Specification<Vehicle> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vehicle_.id));
            }
            if (criteria.getLicensePlate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLicensePlate(), Vehicle_.licensePlate));
            }
            if (criteria.getModel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModel(), Vehicle_.model));
            }
            if (criteria.getBrand() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBrand(), Vehicle_.brand));
            }
            if (criteria.getFabricationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFabricationDate(), Vehicle_.fabricationDate));
            }
            if (criteria.getModelYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModelYear(), Vehicle_.modelYear));
            }
            if (criteria.getColor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getColor(), Vehicle_.color));
            }
            if (criteria.getRenavam() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRenavam(), Vehicle_.renavam));
            }
            if (criteria.getFuelType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFuelType(), Vehicle_.fuelType));
            }
            if (criteria.getChassiNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChassiNumber(), Vehicle_.chassiNumber));
            }
            if (criteria.getCurrentMileage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCurrentMileage(), Vehicle_.currentMileage));
            }
            if (criteria.getLastMaintenanceDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastMaintenanceDate(), Vehicle_.lastMaintenanceDate));
            }
            if (criteria.getLastMaintenanceMileage() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getLastMaintenanceMileage(), Vehicle_.lastMaintenanceMileage)
                );
            }
            if (criteria.getNextMaintenanceDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNextMaintenanceDate(), Vehicle_.nextMaintenanceDate));
            }
            if (criteria.getNextMaintenanceMileage() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getNextMaintenanceMileage(), Vehicle_.nextMaintenanceMileage)
                );
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Vehicle_.description));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Vehicle_.status));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Vehicle_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Vehicle_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Vehicle_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Vehicle_.lastModifiedDate));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getClientId(), root -> root.join(Vehicle_.client, JoinType.LEFT).get(Client_.id))
                );
            }
        }
        return specification;
    }
}
