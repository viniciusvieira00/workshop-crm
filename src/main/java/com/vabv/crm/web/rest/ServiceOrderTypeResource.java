package com.vabv.crm.web.rest;

import com.vabv.crm.repository.ServiceOrderTypeRepository;
import com.vabv.crm.service.ServiceOrderTypeQueryService;
import com.vabv.crm.service.ServiceOrderTypeService;
import com.vabv.crm.service.criteria.ServiceOrderTypeCriteria;
import com.vabv.crm.service.dto.ServiceOrderTypeDTO;
import com.vabv.crm.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.vabv.crm.domain.ServiceOrderType}.
 */
@RestController
@RequestMapping("/api/service-order-types")
public class ServiceOrderTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceOrderTypeResource.class);

    private static final String ENTITY_NAME = "crmServiceOrderType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceOrderTypeService serviceOrderTypeService;

    private final ServiceOrderTypeRepository serviceOrderTypeRepository;

    private final ServiceOrderTypeQueryService serviceOrderTypeQueryService;

    public ServiceOrderTypeResource(
        ServiceOrderTypeService serviceOrderTypeService,
        ServiceOrderTypeRepository serviceOrderTypeRepository,
        ServiceOrderTypeQueryService serviceOrderTypeQueryService
    ) {
        this.serviceOrderTypeService = serviceOrderTypeService;
        this.serviceOrderTypeRepository = serviceOrderTypeRepository;
        this.serviceOrderTypeQueryService = serviceOrderTypeQueryService;
    }

    /**
     * {@code POST  /service-order-types} : Create a new serviceOrderType.
     *
     * @param serviceOrderTypeDTO the serviceOrderTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceOrderTypeDTO, or with status {@code 400 (Bad Request)} if the serviceOrderType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServiceOrderTypeDTO> createServiceOrderType(@Valid @RequestBody ServiceOrderTypeDTO serviceOrderTypeDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ServiceOrderType : {}", serviceOrderTypeDTO);
        if (serviceOrderTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceOrderType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        serviceOrderTypeDTO = serviceOrderTypeService.save(serviceOrderTypeDTO);
        return ResponseEntity.created(new URI("/api/service-order-types/" + serviceOrderTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, serviceOrderTypeDTO.getId().toString()))
            .body(serviceOrderTypeDTO);
    }

    /**
     * {@code PUT  /service-order-types/:id} : Updates an existing serviceOrderType.
     *
     * @param id the id of the serviceOrderTypeDTO to save.
     * @param serviceOrderTypeDTO the serviceOrderTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceOrderTypeDTO,
     * or with status {@code 400 (Bad Request)} if the serviceOrderTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceOrderTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceOrderTypeDTO> updateServiceOrderType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceOrderTypeDTO serviceOrderTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ServiceOrderType : {}, {}", id, serviceOrderTypeDTO);
        if (serviceOrderTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceOrderTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceOrderTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        serviceOrderTypeDTO = serviceOrderTypeService.update(serviceOrderTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceOrderTypeDTO.getId().toString()))
            .body(serviceOrderTypeDTO);
    }

    /**
     * {@code PATCH  /service-order-types/:id} : Partial updates given fields of an existing serviceOrderType, field will ignore if it is null
     *
     * @param id the id of the serviceOrderTypeDTO to save.
     * @param serviceOrderTypeDTO the serviceOrderTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceOrderTypeDTO,
     * or with status {@code 400 (Bad Request)} if the serviceOrderTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serviceOrderTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceOrderTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceOrderTypeDTO> partialUpdateServiceOrderType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceOrderTypeDTO serviceOrderTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ServiceOrderType partially : {}, {}", id, serviceOrderTypeDTO);
        if (serviceOrderTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceOrderTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceOrderTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceOrderTypeDTO> result = serviceOrderTypeService.partialUpdate(serviceOrderTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceOrderTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /service-order-types} : get all the serviceOrderTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceOrderTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ServiceOrderTypeDTO>> getAllServiceOrderTypes(
        ServiceOrderTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ServiceOrderTypes by criteria: {}", criteria);

        Page<ServiceOrderTypeDTO> page = serviceOrderTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-order-types/count} : count all the serviceOrderTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countServiceOrderTypes(ServiceOrderTypeCriteria criteria) {
        LOG.debug("REST request to count ServiceOrderTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(serviceOrderTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /service-order-types/:id} : get the "id" serviceOrderType.
     *
     * @param id the id of the serviceOrderTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceOrderTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrderTypeDTO> getServiceOrderType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ServiceOrderType : {}", id);
        Optional<ServiceOrderTypeDTO> serviceOrderTypeDTO = serviceOrderTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceOrderTypeDTO);
    }

    /**
     * {@code DELETE  /service-order-types/:id} : delete the "id" serviceOrderType.
     *
     * @param id the id of the serviceOrderTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceOrderType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ServiceOrderType : {}", id);
        serviceOrderTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
