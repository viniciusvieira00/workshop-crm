package com.vabv.crm.service;

import com.vabv.crm.domain.ServiceOrderType;
import com.vabv.crm.repository.ServiceOrderTypeRepository;
import com.vabv.crm.service.dto.ServiceOrderTypeDTO;
import com.vabv.crm.service.mapper.ServiceOrderTypeMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.vabv.crm.domain.ServiceOrderType}.
 */
@Service
@Transactional
public class ServiceOrderTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceOrderTypeService.class);

    private final ServiceOrderTypeRepository serviceOrderTypeRepository;

    private final ServiceOrderTypeMapper serviceOrderTypeMapper;

    public ServiceOrderTypeService(ServiceOrderTypeRepository serviceOrderTypeRepository, ServiceOrderTypeMapper serviceOrderTypeMapper) {
        this.serviceOrderTypeRepository = serviceOrderTypeRepository;
        this.serviceOrderTypeMapper = serviceOrderTypeMapper;
    }

    /**
     * Save a serviceOrderType.
     *
     * @param serviceOrderTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public ServiceOrderTypeDTO save(ServiceOrderTypeDTO serviceOrderTypeDTO) {
        LOG.debug("Request to save ServiceOrderType : {}", serviceOrderTypeDTO);

        // Preencher campos de auditoria para novos registros
        if (serviceOrderTypeDTO.getId() == null) {
            serviceOrderTypeDTO.setCreatedBy(getCurrentUserLogin());
            serviceOrderTypeDTO.setCreatedDate(Instant.now());
        }

        // Sempre atualizar os campos de última modificação
        serviceOrderTypeDTO.setLastModifiedBy(getCurrentUserLogin());
        serviceOrderTypeDTO.setLastModifiedDate(Instant.now());

        ServiceOrderType serviceOrderType = serviceOrderTypeMapper.toEntity(serviceOrderTypeDTO);
        serviceOrderType = serviceOrderTypeRepository.save(serviceOrderType);
        return serviceOrderTypeMapper.toDto(serviceOrderType);
    }

    /**
     * Update a serviceOrderType.
     *
     * @param serviceOrderTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public ServiceOrderTypeDTO update(ServiceOrderTypeDTO serviceOrderTypeDTO) {
        LOG.debug("Request to update ServiceOrderType : {}", serviceOrderTypeDTO);

        // Preservar os dados de criação originais
        Optional<ServiceOrderTypeDTO> existingServiceOrderTypeOpt = findOne(serviceOrderTypeDTO.getId());
        if (existingServiceOrderTypeOpt.isPresent()) {
            ServiceOrderTypeDTO existingServiceOrderType = existingServiceOrderTypeOpt.get();
            serviceOrderTypeDTO.setCreatedBy(existingServiceOrderType.getCreatedBy());
            serviceOrderTypeDTO.setCreatedDate(existingServiceOrderType.getCreatedDate());
        }

        // Atualizar os campos de última modificação
        serviceOrderTypeDTO.setLastModifiedBy(getCurrentUserLogin());
        serviceOrderTypeDTO.setLastModifiedDate(Instant.now());

        ServiceOrderType serviceOrderType = serviceOrderTypeMapper.toEntity(serviceOrderTypeDTO);
        serviceOrderType = serviceOrderTypeRepository.save(serviceOrderType);
        return serviceOrderTypeMapper.toDto(serviceOrderType);
    }

    /**
     * Partially update a serviceOrderType.
     *
     * @param serviceOrderTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ServiceOrderTypeDTO> partialUpdate(ServiceOrderTypeDTO serviceOrderTypeDTO) {
        LOG.debug("Request to partially update ServiceOrderType : {}", serviceOrderTypeDTO);

        return serviceOrderTypeRepository
            .findById(serviceOrderTypeDTO.getId())
            .map(existingServiceOrderType -> {
                ServiceOrderTypeDTO existingDTO = serviceOrderTypeMapper.toDto(existingServiceOrderType);

                // Preservar os dados de criação originais
                if (serviceOrderTypeDTO.getCreatedBy() == null) {
                    serviceOrderTypeDTO.setCreatedBy(existingDTO.getCreatedBy());
                }
                if (serviceOrderTypeDTO.getCreatedDate() == null) {
                    serviceOrderTypeDTO.setCreatedDate(existingDTO.getCreatedDate());
                }

                // Atualizar os campos de última modificação
                serviceOrderTypeDTO.setLastModifiedBy(getCurrentUserLogin());
                serviceOrderTypeDTO.setLastModifiedDate(Instant.now());

                serviceOrderTypeMapper.partialUpdate(existingServiceOrderType, serviceOrderTypeDTO);
                return existingServiceOrderType;
            })
            .map(serviceOrderTypeRepository::save)
            .map(serviceOrderTypeMapper::toDto);
    }

    /**
     * Get all the serviceOrderTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ServiceOrderTypeDTO> findAll() {
        LOG.debug("Request to get all ServiceOrderTypes");
        return serviceOrderTypeRepository.findAll().stream().map(serviceOrderTypeMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one serviceOrderType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServiceOrderTypeDTO> findOne(Long id) {
        LOG.debug("Request to get ServiceOrderType : {}", id);
        return serviceOrderTypeRepository.findById(id).map(serviceOrderTypeMapper::toDto);
    }

    /**
     * Delete the serviceOrderType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ServiceOrderType : {}", id);
        serviceOrderTypeRepository.deleteById(id);
    }

    /**
     * Obtém o login do usuário atual.
     *
     * @return o login do usuário atual ou "system" se não houver usuário autenticado
     */
    private String getCurrentUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "system";
    }
}
