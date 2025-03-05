package com.vabv.crm.service;

import com.vabv.crm.domain.Vehicle;
import com.vabv.crm.repository.VehicleRepository;
import com.vabv.crm.service.dto.VehicleDTO;
import com.vabv.crm.service.mapper.VehicleMapper;
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
 * Service Implementation for managing {@link com.vabv.crm.domain.Vehicle}.
 */
@Service
@Transactional
public class VehicleService {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleService.class);

    private final VehicleRepository vehicleRepository;

    private final VehicleMapper vehicleMapper;

    public VehicleService(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    /**
     * Save a vehicle.
     *
     * @param vehicleDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleDTO save(VehicleDTO vehicleDTO) {
        LOG.debug("Request to save Vehicle : {}", vehicleDTO);

        // Preencher campos de auditoria para novos registros
        if (vehicleDTO.getId() == null) {
            vehicleDTO.setCreatedBy(getCurrentUserLogin());
            vehicleDTO.setCreatedDate(Instant.now());
        }

        // Sempre atualizar os campos de última modificação
        vehicleDTO.setLastModifiedBy(getCurrentUserLogin());
        vehicleDTO.setLastModifiedDate(Instant.now());

        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        vehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(vehicle);
    }

    /**
     * Update a vehicle.
     *
     * @param vehicleDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleDTO update(VehicleDTO vehicleDTO) {
        LOG.debug("Request to update Vehicle : {}", vehicleDTO);

        // Preservar os dados de criação originais
        Optional<VehicleDTO> existingVehicleOpt = findOne(vehicleDTO.getId());
        if (existingVehicleOpt.isPresent()) {
            VehicleDTO existingVehicle = existingVehicleOpt.get();
            vehicleDTO.setCreatedBy(existingVehicle.getCreatedBy());
            vehicleDTO.setCreatedDate(existingVehicle.getCreatedDate());
        }

        // Atualizar os campos de última modificação
        vehicleDTO.setLastModifiedBy(getCurrentUserLogin());
        vehicleDTO.setLastModifiedDate(Instant.now());

        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        vehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(vehicle);
    }

    /**
     * Partially update a vehicle.
     *
     * @param vehicleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VehicleDTO> partialUpdate(VehicleDTO vehicleDTO) {
        LOG.debug("Request to partially update Vehicle : {}", vehicleDTO);

        return vehicleRepository
            .findById(vehicleDTO.getId())
            .map(existingVehicle -> {
                // Preservar os dados de criação originais
                if (vehicleDTO.getCreatedBy() == null) {
                    vehicleDTO.setCreatedBy(existingVehicle.getCreatedBy());
                }
                if (vehicleDTO.getCreatedDate() == null) {
                    vehicleDTO.setCreatedDate(existingVehicle.getCreatedDate());
                }

                // Atualizar os campos de última modificação
                vehicleDTO.setLastModifiedBy(getCurrentUserLogin());
                vehicleDTO.setLastModifiedDate(Instant.now());

                vehicleMapper.partialUpdate(existingVehicle, vehicleDTO);

                return existingVehicle;
            })
            .map(vehicleRepository::save)
            .map(vehicleMapper::toDto);
    }

    /**
     * Get all the vehicles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<VehicleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return vehicleRepository.findAllWithEagerRelationships(pageable).map(vehicleMapper::toDto);
    }

    /**
     * Get one vehicle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VehicleDTO> findOne(Long id) {
        LOG.debug("Request to get Vehicle : {}", id);
        return vehicleRepository.findOneWithEagerRelationships(id).map(vehicleMapper::toDto);
    }

    /**
     * Delete the vehicle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Vehicle : {}", id);
        vehicleRepository.deleteById(id);
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
