package com.vabv.crm.service;

import com.vabv.crm.domain.ServiceOrder;
import com.vabv.crm.domain.enumeration.ServiceOrderStatus;
import com.vabv.crm.repository.ServiceOrderRepository;
import com.vabv.crm.service.dto.ProductDTO;
import com.vabv.crm.service.dto.ServiceOrderDTO;
import com.vabv.crm.service.mapper.ServiceOrderMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.vabv.crm.domain.ServiceOrder}.
 */
@Service
@Transactional
public class ServiceOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceOrderService.class);

    private final ServiceOrderRepository serviceOrderRepository;

    private final ServiceOrderMapper serviceOrderMapper;

    public ServiceOrderService(ServiceOrderRepository serviceOrderRepository, ServiceOrderMapper serviceOrderMapper) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.serviceOrderMapper = serviceOrderMapper;
    }

    /**
     * Save a serviceOrder.
     *
     * @param serviceOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public ServiceOrderDTO save(ServiceOrderDTO serviceOrderDTO) {
        LOG.debug("Request to save ServiceOrder : {}", serviceOrderDTO);

        // Set default values for new service orders
        if (serviceOrderDTO.getId() == null) {
            // Set creation date to now if not provided
            if (serviceOrderDTO.getCreationDate() == null) {
                serviceOrderDTO.setCreationDate(Instant.now());
            }

            // Set default status to CREATED if not provided
            if (serviceOrderDTO.getStatus() == null) {
                serviceOrderDTO.setStatus(ServiceOrderStatus.CREATED);
            }

            // Set audit fields for new records
            serviceOrderDTO.setCreatedBy(getCurrentUserLogin());
            serviceOrderDTO.setCreatedDate(Instant.now());
        }

        // Always update last modified fields
        serviceOrderDTO.setLastModifiedBy(getCurrentUserLogin());
        serviceOrderDTO.setLastModifiedDate(Instant.now());

        // Calculate total cost
        calculateTotalCost(serviceOrderDTO);

        ServiceOrder serviceOrder = serviceOrderMapper.toEntity(serviceOrderDTO);
        serviceOrder = serviceOrderRepository.save(serviceOrder);
        return serviceOrderMapper.toDto(serviceOrder);
    }

    /**
     * Update a serviceOrder.
     *
     * @param serviceOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public ServiceOrderDTO update(ServiceOrderDTO serviceOrderDTO) {
        LOG.debug("Request to update ServiceOrder : {}", serviceOrderDTO);

        // Preserve original creation data
        Optional<ServiceOrderDTO> existingServiceOrderOpt = findOne(serviceOrderDTO.getId());
        if (existingServiceOrderOpt.isPresent()) {
            ServiceOrderDTO existingServiceOrder = existingServiceOrderOpt.get();
            serviceOrderDTO.setCreatedBy(existingServiceOrder.getCreatedBy());
            serviceOrderDTO.setCreatedDate(existingServiceOrder.getCreatedDate());

            // Set completion date if status changed to COMPLETED
            if (
                ServiceOrderStatus.COMPLETED.equals(serviceOrderDTO.getStatus()) &&
                !ServiceOrderStatus.COMPLETED.equals(existingServiceOrder.getStatus())
            ) {
                serviceOrderDTO.setCompletionDate(Instant.now());
            }

            // Set start date if status changed to IN_PROGRESS
            if (
                ServiceOrderStatus.IN_PROGRESS.equals(serviceOrderDTO.getStatus()) &&
                !ServiceOrderStatus.IN_PROGRESS.equals(existingServiceOrder.getStatus())
            ) {
                serviceOrderDTO.setStartDate(Instant.now());
            }
        }

        // Update last modified fields
        serviceOrderDTO.setLastModifiedBy(getCurrentUserLogin());
        serviceOrderDTO.setLastModifiedDate(Instant.now());

        // Calculate total cost
        calculateTotalCost(serviceOrderDTO);

        ServiceOrder serviceOrder = serviceOrderMapper.toEntity(serviceOrderDTO);
        serviceOrder = serviceOrderRepository.save(serviceOrder);
        return serviceOrderMapper.toDto(serviceOrder);
    }

    /**
     * Partially update a serviceOrder.
     *
     * @param serviceOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ServiceOrderDTO> partialUpdate(ServiceOrderDTO serviceOrderDTO) {
        LOG.debug("Request to partially update ServiceOrder : {}", serviceOrderDTO);

        return serviceOrderRepository
            .findById(serviceOrderDTO.getId())
            .map(existingServiceOrder -> {
                ServiceOrderDTO existingDTO = serviceOrderMapper.toDto(existingServiceOrder);

                // Preserve original creation data
                if (serviceOrderDTO.getCreatedBy() == null) {
                    serviceOrderDTO.setCreatedBy(existingDTO.getCreatedBy());
                }
                if (serviceOrderDTO.getCreatedDate() == null) {
                    serviceOrderDTO.setCreatedDate(existingDTO.getCreatedDate());
                }

                // Set completion date if status changed to COMPLETED
                if (
                    ServiceOrderStatus.COMPLETED.equals(serviceOrderDTO.getStatus()) &&
                    !ServiceOrderStatus.COMPLETED.equals(existingDTO.getStatus())
                ) {
                    serviceOrderDTO.setCompletionDate(Instant.now());
                }

                // Set start date if status changed to IN_PROGRESS
                if (
                    ServiceOrderStatus.IN_PROGRESS.equals(serviceOrderDTO.getStatus()) &&
                    !ServiceOrderStatus.IN_PROGRESS.equals(existingDTO.getStatus())
                ) {
                    serviceOrderDTO.setStartDate(Instant.now());
                }

                // Update last modified fields
                serviceOrderDTO.setLastModifiedBy(getCurrentUserLogin());
                serviceOrderDTO.setLastModifiedDate(Instant.now());

                // For partial updates, we need to calculate total cost if either initialCost or additionalCost is provided
                if (serviceOrderDTO.getInitialCost() != null || serviceOrderDTO.getAdditionalCost() != null) {
                    // Use existing values for any null fields
                    if (serviceOrderDTO.getInitialCost() == null) {
                        serviceOrderDTO.setInitialCost(existingDTO.getInitialCost());
                    }
                    if (serviceOrderDTO.getAdditionalCost() == null) {
                        serviceOrderDTO.setAdditionalCost(existingDTO.getAdditionalCost());
                    }
                    calculateTotalCost(serviceOrderDTO);
                }

                serviceOrderMapper.partialUpdate(existingServiceOrder, serviceOrderDTO);
                return existingServiceOrder;
            })
            .map(serviceOrderRepository::save)
            .map(serviceOrderMapper::toDto);
    }

    /**
     * Get all the serviceOrders with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ServiceOrderDTO> findAllWithEagerRelationships(Pageable pageable) {
        return serviceOrderRepository.findAllWithEagerRelationships(pageable).map(serviceOrderMapper::toDto);
    }

    /**
     * Get one serviceOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServiceOrderDTO> findOne(Long id) {
        LOG.debug("Request to get ServiceOrder : {}", id);
        return serviceOrderRepository.findOneWithEagerRelationships(id).map(serviceOrderMapper::toDto);
    }

    /**
     * Delete the serviceOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ServiceOrder : {}", id);
        serviceOrderRepository.deleteById(id);
    }

    /**
     * Calculate the total cost of a service order based on initial and additional costs.
     *
     * @param serviceOrderDTO the service order DTO
     */
    private void calculateTotalCost(ServiceOrderDTO serviceOrderDTO) {
        BigDecimal total = BigDecimal.ZERO;

        // Add initial cost if present
        if (serviceOrderDTO.getInitialCost() != null) {
            total = total.add(serviceOrderDTO.getInitialCost());
        }

        // Add additional cost if present
        if (serviceOrderDTO.getAdditionalCost() != null) {
            total = total.add(serviceOrderDTO.getAdditionalCost());
        }

        // Add cost of products if present
        if (serviceOrderDTO.getProducts() != null && !serviceOrderDTO.getProducts().isEmpty()) {
            for (ProductDTO product : serviceOrderDTO.getProducts()) {
                if (product.getSellPrice() != null) {
                    total = total.add(product.getSellPrice());
                }
            }
        }

        // Set the total cost
        serviceOrderDTO.setTotalCost(total);
    }

    /**
     * Get the current user login.
     *
     * @return the current user login or "system" if no authenticated user
     */
    private String getCurrentUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "system";
    }
}
