package com.vabv.crm.web.rest;

import com.vabv.crm.repository.ServiceOrderRepository;
import com.vabv.crm.service.ServiceOrderQueryService;
import com.vabv.crm.service.ServiceOrderService;
import com.vabv.crm.service.criteria.ServiceOrderCriteria;
import com.vabv.crm.service.dto.ServiceOrderDTO;
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
 * REST controller for managing {@link com.vabv.crm.domain.ServiceOrder}.
 */
@RestController
@RequestMapping("/api/service-orders")
public class ServiceOrderResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceOrderResource.class);

    private static final String ENTITY_NAME = "crmServiceOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceOrderService serviceOrderService;

    private final ServiceOrderRepository serviceOrderRepository;

    private final ServiceOrderQueryService serviceOrderQueryService;

    public ServiceOrderResource(
        ServiceOrderService serviceOrderService,
        ServiceOrderRepository serviceOrderRepository,
        ServiceOrderQueryService serviceOrderQueryService
    ) {
        this.serviceOrderService = serviceOrderService;
        this.serviceOrderRepository = serviceOrderRepository;
        this.serviceOrderQueryService = serviceOrderQueryService;
    }

    /**
     * {@code POST  /service-orders} : Create a new serviceOrder.
     *
     * @param serviceOrderDTO the serviceOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceOrderDTO, or with status {@code 400 (Bad Request)} if the serviceOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServiceOrderDTO> createServiceOrder(@Valid @RequestBody ServiceOrderDTO serviceOrderDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ServiceOrder : {}", serviceOrderDTO);
        if (serviceOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        serviceOrderDTO = serviceOrderService.save(serviceOrderDTO);
        return ResponseEntity.created(new URI("/api/service-orders/" + serviceOrderDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, serviceOrderDTO.getId().toString()))
            .body(serviceOrderDTO);
    }

    /**
     * {@code PUT  /service-orders/:id} : Updates an existing serviceOrder.
     *
     * @param id the id of the serviceOrderDTO to save.
     * @param serviceOrderDTO the serviceOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceOrderDTO,
     * or with status {@code 400 (Bad Request)} if the serviceOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceOrderDTO> updateServiceOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceOrderDTO serviceOrderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ServiceOrder : {}, {}", id, serviceOrderDTO);
        if (serviceOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        serviceOrderDTO = serviceOrderService.update(serviceOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceOrderDTO.getId().toString()))
            .body(serviceOrderDTO);
    }

    /**
     * {@code PATCH  /service-orders/:id} : Partial updates given fields of an existing serviceOrder, field will ignore if it is null
     *
     * @param id the id of the serviceOrderDTO to save.
     * @param serviceOrderDTO the serviceOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceOrderDTO,
     * or with status {@code 400 (Bad Request)} if the serviceOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serviceOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceOrderDTO> partialUpdateServiceOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceOrderDTO serviceOrderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ServiceOrder partially : {}, {}", id, serviceOrderDTO);
        if (serviceOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceOrderDTO> result = serviceOrderService.partialUpdate(serviceOrderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceOrderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /service-orders} : get all the serviceOrders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceOrders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ServiceOrderDTO>> getAllServiceOrders(
        ServiceOrderCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ServiceOrders by criteria: {}", criteria);

        Page<ServiceOrderDTO> page = serviceOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-orders/count} : count all the serviceOrders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countServiceOrders(ServiceOrderCriteria criteria) {
        LOG.debug("REST request to count ServiceOrders by criteria: {}", criteria);
        return ResponseEntity.ok().body(serviceOrderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /service-orders/:id} : get the "id" serviceOrder.
     *
     * @param id the id of the serviceOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrderDTO> getServiceOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ServiceOrder : {}", id);
        Optional<ServiceOrderDTO> serviceOrderDTO = serviceOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceOrderDTO);
    }

    /**
     * {@code DELETE  /service-orders/:id} : delete the "id" serviceOrder.
     *
     * @param id the id of the serviceOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ServiceOrder : {}", id);
        serviceOrderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
