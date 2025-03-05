package com.vabv.crm.web.rest;

import static com.vabv.crm.domain.ServiceOrderAsserts.*;
import static com.vabv.crm.web.rest.TestUtil.createUpdateProxyForBean;
import static com.vabv.crm.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vabv.crm.IntegrationTest;
import com.vabv.crm.domain.Product;
import com.vabv.crm.domain.ServiceOrder;
import com.vabv.crm.domain.ServiceOrderType;
import com.vabv.crm.domain.Vehicle;
import com.vabv.crm.domain.enumeration.ServiceOrderStatus;
import com.vabv.crm.repository.ServiceOrderRepository;
import com.vabv.crm.service.ServiceOrderService;
import com.vabv.crm.service.dto.ServiceOrderDTO;
import com.vabv.crm.service.mapper.ServiceOrderMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ServiceOrderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ServiceOrderResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_COMPLETION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_INITIAL_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_INITIAL_COST = new BigDecimal(1);
    private static final BigDecimal SMALLER_INITIAL_COST = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_ADDITIONAL_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_ADDITIONAL_COST = new BigDecimal(1);
    private static final BigDecimal SMALLER_ADDITIONAL_COST = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_TOTAL_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_COST = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL_COST = new BigDecimal(0 - 1);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final ServiceOrderStatus DEFAULT_STATUS = ServiceOrderStatus.CREATED;
    private static final ServiceOrderStatus UPDATED_STATUS = ServiceOrderStatus.IN_PROGRESS;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/service-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Mock
    private ServiceOrderRepository serviceOrderRepositoryMock;

    @Autowired
    private ServiceOrderMapper serviceOrderMapper;

    @Mock
    private ServiceOrderService serviceOrderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceOrderMockMvc;

    private ServiceOrder serviceOrder;

    private ServiceOrder insertedServiceOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceOrder createEntity(EntityManager em) {
        ServiceOrder serviceOrder = new ServiceOrder()
            .creationDate(DEFAULT_CREATION_DATE)
            .startDate(DEFAULT_START_DATE)
            .completionDate(DEFAULT_COMPLETION_DATE)
            .initialCost(DEFAULT_INITIAL_COST)
            .additionalCost(DEFAULT_ADDITIONAL_COST)
            .totalCost(DEFAULT_TOTAL_COST)
            .notes(DEFAULT_NOTES)
            .status(DEFAULT_STATUS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        // Add required entity
        ServiceOrderType serviceOrderType;
        if (TestUtil.findAll(em, ServiceOrderType.class).isEmpty()) {
            serviceOrderType = ServiceOrderTypeResourceIT.createEntity();
            em.persist(serviceOrderType);
            em.flush();
        } else {
            serviceOrderType = TestUtil.findAll(em, ServiceOrderType.class).get(0);
        }
        serviceOrder.setType(serviceOrderType);
        // Add required entity
        Vehicle vehicle;
        if (TestUtil.findAll(em, Vehicle.class).isEmpty()) {
            vehicle = VehicleResourceIT.createEntity(em);
            em.persist(vehicle);
            em.flush();
        } else {
            vehicle = TestUtil.findAll(em, Vehicle.class).get(0);
        }
        serviceOrder.setVehicle(vehicle);
        return serviceOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceOrder createUpdatedEntity(EntityManager em) {
        ServiceOrder updatedServiceOrder = new ServiceOrder()
            .creationDate(UPDATED_CREATION_DATE)
            .startDate(UPDATED_START_DATE)
            .completionDate(UPDATED_COMPLETION_DATE)
            .initialCost(UPDATED_INITIAL_COST)
            .additionalCost(UPDATED_ADDITIONAL_COST)
            .totalCost(UPDATED_TOTAL_COST)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        // Add required entity
        ServiceOrderType serviceOrderType;
        if (TestUtil.findAll(em, ServiceOrderType.class).isEmpty()) {
            serviceOrderType = ServiceOrderTypeResourceIT.createUpdatedEntity();
            em.persist(serviceOrderType);
            em.flush();
        } else {
            serviceOrderType = TestUtil.findAll(em, ServiceOrderType.class).get(0);
        }
        updatedServiceOrder.setType(serviceOrderType);
        // Add required entity
        Vehicle vehicle;
        if (TestUtil.findAll(em, Vehicle.class).isEmpty()) {
            vehicle = VehicleResourceIT.createUpdatedEntity(em);
            em.persist(vehicle);
            em.flush();
        } else {
            vehicle = TestUtil.findAll(em, Vehicle.class).get(0);
        }
        updatedServiceOrder.setVehicle(vehicle);
        return updatedServiceOrder;
    }

    @BeforeEach
    public void initTest() {
        serviceOrder = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedServiceOrder != null) {
            serviceOrderRepository.delete(insertedServiceOrder);
            insertedServiceOrder = null;
        }
    }

    @Test
    @Transactional
    void createServiceOrder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServiceOrder
        ServiceOrderDTO serviceOrderDTO = serviceOrderMapper.toDto(serviceOrder);
        var returnedServiceOrderDTO = om.readValue(
            restServiceOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceOrderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServiceOrderDTO.class
        );

        // Validate the ServiceOrder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServiceOrder = serviceOrderMapper.toEntity(returnedServiceOrderDTO);
        assertServiceOrderUpdatableFieldsEquals(returnedServiceOrder, getPersistedServiceOrder(returnedServiceOrder));

        insertedServiceOrder = returnedServiceOrder;
    }

    @Test
    @Transactional
    void createServiceOrderWithExistingId() throws Exception {
        // Create the ServiceOrder with an existing ID
        serviceOrder.setId(1L);
        ServiceOrderDTO serviceOrderDTO = serviceOrderMapper.toDto(serviceOrder);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceOrder.setCreationDate(null);

        // Create the ServiceOrder, which fails.
        ServiceOrderDTO serviceOrderDTO = serviceOrderMapper.toDto(serviceOrder);

        restServiceOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceOrderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceOrder.setStatus(null);

        // Create the ServiceOrder, which fails.
        ServiceOrderDTO serviceOrderDTO = serviceOrderMapper.toDto(serviceOrder);

        restServiceOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceOrderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceOrders() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList
        restServiceOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].completionDate").value(hasItem(DEFAULT_COMPLETION_DATE.toString())))
            .andExpect(jsonPath("$.[*].initialCost").value(hasItem(sameNumber(DEFAULT_INITIAL_COST))))
            .andExpect(jsonPath("$.[*].additionalCost").value(hasItem(sameNumber(DEFAULT_ADDITIONAL_COST))))
            .andExpect(jsonPath("$.[*].totalCost").value(hasItem(sameNumber(DEFAULT_TOTAL_COST))))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiceOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(serviceOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServiceOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(serviceOrderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiceOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(serviceOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServiceOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(serviceOrderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getServiceOrder() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get the serviceOrder
        restServiceOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceOrder.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.completionDate").value(DEFAULT_COMPLETION_DATE.toString()))
            .andExpect(jsonPath("$.initialCost").value(sameNumber(DEFAULT_INITIAL_COST)))
            .andExpect(jsonPath("$.additionalCost").value(sameNumber(DEFAULT_ADDITIONAL_COST)))
            .andExpect(jsonPath("$.totalCost").value(sameNumber(DEFAULT_TOTAL_COST)))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getServiceOrdersByIdFiltering() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        Long id = serviceOrder.getId();

        defaultServiceOrderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultServiceOrderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultServiceOrderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where creationDate equals to
        defaultServiceOrderFiltering("creationDate.equals=" + DEFAULT_CREATION_DATE, "creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where creationDate in
        defaultServiceOrderFiltering(
            "creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE,
            "creationDate.in=" + UPDATED_CREATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where creationDate is not null
        defaultServiceOrderFiltering("creationDate.specified=true", "creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrdersByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where startDate equals to
        defaultServiceOrderFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where startDate in
        defaultServiceOrderFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where startDate is not null
        defaultServiceOrderFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrdersByCompletionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where completionDate equals to
        defaultServiceOrderFiltering(
            "completionDate.equals=" + DEFAULT_COMPLETION_DATE,
            "completionDate.equals=" + UPDATED_COMPLETION_DATE
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByCompletionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where completionDate in
        defaultServiceOrderFiltering(
            "completionDate.in=" + DEFAULT_COMPLETION_DATE + "," + UPDATED_COMPLETION_DATE,
            "completionDate.in=" + UPDATED_COMPLETION_DATE
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByCompletionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where completionDate is not null
        defaultServiceOrderFiltering("completionDate.specified=true", "completionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrdersByInitialCostIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where initialCost equals to
        defaultServiceOrderFiltering("initialCost.equals=" + DEFAULT_INITIAL_COST, "initialCost.equals=" + UPDATED_INITIAL_COST);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByInitialCostIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where initialCost in
        defaultServiceOrderFiltering(
            "initialCost.in=" + DEFAULT_INITIAL_COST + "," + UPDATED_INITIAL_COST,
            "initialCost.in=" + UPDATED_INITIAL_COST
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByInitialCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where initialCost is not null
        defaultServiceOrderFiltering("initialCost.specified=true", "initialCost.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrdersByInitialCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where initialCost is greater than or equal to
        defaultServiceOrderFiltering(
            "initialCost.greaterThanOrEqual=" + DEFAULT_INITIAL_COST,
            "initialCost.greaterThanOrEqual=" + UPDATED_INITIAL_COST
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByInitialCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where initialCost is less than or equal to
        defaultServiceOrderFiltering(
            "initialCost.lessThanOrEqual=" + DEFAULT_INITIAL_COST,
            "initialCost.lessThanOrEqual=" + SMALLER_INITIAL_COST
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByInitialCostIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where initialCost is less than
        defaultServiceOrderFiltering("initialCost.lessThan=" + UPDATED_INITIAL_COST, "initialCost.lessThan=" + DEFAULT_INITIAL_COST);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByInitialCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where initialCost is greater than
        defaultServiceOrderFiltering("initialCost.greaterThan=" + SMALLER_INITIAL_COST, "initialCost.greaterThan=" + DEFAULT_INITIAL_COST);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByAdditionalCostIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where additionalCost equals to
        defaultServiceOrderFiltering(
            "additionalCost.equals=" + DEFAULT_ADDITIONAL_COST,
            "additionalCost.equals=" + UPDATED_ADDITIONAL_COST
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByAdditionalCostIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where additionalCost in
        defaultServiceOrderFiltering(
            "additionalCost.in=" + DEFAULT_ADDITIONAL_COST + "," + UPDATED_ADDITIONAL_COST,
            "additionalCost.in=" + UPDATED_ADDITIONAL_COST
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByAdditionalCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where additionalCost is not null
        defaultServiceOrderFiltering("additionalCost.specified=true", "additionalCost.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrdersByAdditionalCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where additionalCost is greater than or equal to
        defaultServiceOrderFiltering(
            "additionalCost.greaterThanOrEqual=" + DEFAULT_ADDITIONAL_COST,
            "additionalCost.greaterThanOrEqual=" + UPDATED_ADDITIONAL_COST
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByAdditionalCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where additionalCost is less than or equal to
        defaultServiceOrderFiltering(
            "additionalCost.lessThanOrEqual=" + DEFAULT_ADDITIONAL_COST,
            "additionalCost.lessThanOrEqual=" + SMALLER_ADDITIONAL_COST
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByAdditionalCostIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where additionalCost is less than
        defaultServiceOrderFiltering(
            "additionalCost.lessThan=" + UPDATED_ADDITIONAL_COST,
            "additionalCost.lessThan=" + DEFAULT_ADDITIONAL_COST
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByAdditionalCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where additionalCost is greater than
        defaultServiceOrderFiltering(
            "additionalCost.greaterThan=" + SMALLER_ADDITIONAL_COST,
            "additionalCost.greaterThan=" + DEFAULT_ADDITIONAL_COST
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByTotalCostIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where totalCost equals to
        defaultServiceOrderFiltering("totalCost.equals=" + DEFAULT_TOTAL_COST, "totalCost.equals=" + UPDATED_TOTAL_COST);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByTotalCostIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where totalCost in
        defaultServiceOrderFiltering("totalCost.in=" + DEFAULT_TOTAL_COST + "," + UPDATED_TOTAL_COST, "totalCost.in=" + UPDATED_TOTAL_COST);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByTotalCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where totalCost is not null
        defaultServiceOrderFiltering("totalCost.specified=true", "totalCost.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrdersByTotalCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where totalCost is greater than or equal to
        defaultServiceOrderFiltering(
            "totalCost.greaterThanOrEqual=" + DEFAULT_TOTAL_COST,
            "totalCost.greaterThanOrEqual=" + UPDATED_TOTAL_COST
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByTotalCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where totalCost is less than or equal to
        defaultServiceOrderFiltering("totalCost.lessThanOrEqual=" + DEFAULT_TOTAL_COST, "totalCost.lessThanOrEqual=" + SMALLER_TOTAL_COST);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByTotalCostIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where totalCost is less than
        defaultServiceOrderFiltering("totalCost.lessThan=" + UPDATED_TOTAL_COST, "totalCost.lessThan=" + DEFAULT_TOTAL_COST);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByTotalCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where totalCost is greater than
        defaultServiceOrderFiltering("totalCost.greaterThan=" + SMALLER_TOTAL_COST, "totalCost.greaterThan=" + DEFAULT_TOTAL_COST);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where notes equals to
        defaultServiceOrderFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where notes in
        defaultServiceOrderFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where notes is not null
        defaultServiceOrderFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrdersByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where notes contains
        defaultServiceOrderFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where notes does not contain
        defaultServiceOrderFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where status equals to
        defaultServiceOrderFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where status in
        defaultServiceOrderFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where status is not null
        defaultServiceOrderFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrdersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where createdBy equals to
        defaultServiceOrderFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where createdBy in
        defaultServiceOrderFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where createdBy is not null
        defaultServiceOrderFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrdersByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where createdBy contains
        defaultServiceOrderFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where createdBy does not contain
        defaultServiceOrderFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where createdDate equals to
        defaultServiceOrderFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOrdersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where createdDate in
        defaultServiceOrderFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where createdDate is not null
        defaultServiceOrderFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrdersByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where lastModifiedBy equals to
        defaultServiceOrderFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where lastModifiedBy in
        defaultServiceOrderFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where lastModifiedBy is not null
        defaultServiceOrderFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrdersByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where lastModifiedBy contains
        defaultServiceOrderFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where lastModifiedBy does not contain
        defaultServiceOrderFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where lastModifiedDate equals to
        defaultServiceOrderFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where lastModifiedDate in
        defaultServiceOrderFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllServiceOrdersByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrderList where lastModifiedDate is not null
        defaultServiceOrderFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrdersByTypeIsEqualToSomething() throws Exception {
        ServiceOrderType type;
        if (TestUtil.findAll(em, ServiceOrderType.class).isEmpty()) {
            serviceOrderRepository.saveAndFlush(serviceOrder);
            type = ServiceOrderTypeResourceIT.createEntity();
        } else {
            type = TestUtil.findAll(em, ServiceOrderType.class).get(0);
        }
        em.persist(type);
        em.flush();
        serviceOrder.setType(type);
        serviceOrderRepository.saveAndFlush(serviceOrder);
        Long typeId = type.getId();
        // Get all the serviceOrderList where type equals to typeId
        defaultServiceOrderShouldBeFound("typeId.equals=" + typeId);

        // Get all the serviceOrderList where type equals to (typeId + 1)
        defaultServiceOrderShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }

    @Test
    @Transactional
    void getAllServiceOrdersByVehicleIsEqualToSomething() throws Exception {
        Vehicle vehicle;
        if (TestUtil.findAll(em, Vehicle.class).isEmpty()) {
            serviceOrderRepository.saveAndFlush(serviceOrder);
            vehicle = VehicleResourceIT.createEntity(em);
        } else {
            vehicle = TestUtil.findAll(em, Vehicle.class).get(0);
        }
        em.persist(vehicle);
        em.flush();
        serviceOrder.setVehicle(vehicle);
        serviceOrderRepository.saveAndFlush(serviceOrder);
        Long vehicleId = vehicle.getId();
        // Get all the serviceOrderList where vehicle equals to vehicleId
        defaultServiceOrderShouldBeFound("vehicleId.equals=" + vehicleId);

        // Get all the serviceOrderList where vehicle equals to (vehicleId + 1)
        defaultServiceOrderShouldNotBeFound("vehicleId.equals=" + (vehicleId + 1));
    }

    @Test
    @Transactional
    void getAllServiceOrdersByProductsIsEqualToSomething() throws Exception {
        Product products;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            serviceOrderRepository.saveAndFlush(serviceOrder);
            products = ProductResourceIT.createEntity();
        } else {
            products = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(products);
        em.flush();
        serviceOrder.addProducts(products);
        serviceOrderRepository.saveAndFlush(serviceOrder);
        Long productsId = products.getId();
        // Get all the serviceOrderList where products equals to productsId
        defaultServiceOrderShouldBeFound("productsId.equals=" + productsId);

        // Get all the serviceOrderList where products equals to (productsId + 1)
        defaultServiceOrderShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultServiceOrderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultServiceOrderShouldBeFound(shouldBeFound);
        defaultServiceOrderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServiceOrderShouldBeFound(String filter) throws Exception {
        restServiceOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].completionDate").value(hasItem(DEFAULT_COMPLETION_DATE.toString())))
            .andExpect(jsonPath("$.[*].initialCost").value(hasItem(sameNumber(DEFAULT_INITIAL_COST))))
            .andExpect(jsonPath("$.[*].additionalCost").value(hasItem(sameNumber(DEFAULT_ADDITIONAL_COST))))
            .andExpect(jsonPath("$.[*].totalCost").value(hasItem(sameNumber(DEFAULT_TOTAL_COST))))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restServiceOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServiceOrderShouldNotBeFound(String filter) throws Exception {
        restServiceOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServiceOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingServiceOrder() throws Exception {
        // Get the serviceOrder
        restServiceOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceOrder() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceOrder
        ServiceOrder updatedServiceOrder = serviceOrderRepository.findById(serviceOrder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceOrder are not directly saved in db
        em.detach(updatedServiceOrder);
        updatedServiceOrder
            .creationDate(UPDATED_CREATION_DATE)
            .startDate(UPDATED_START_DATE)
            .completionDate(UPDATED_COMPLETION_DATE)
            .initialCost(UPDATED_INITIAL_COST)
            .additionalCost(UPDATED_ADDITIONAL_COST)
            .totalCost(UPDATED_TOTAL_COST)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ServiceOrderDTO serviceOrderDTO = serviceOrderMapper.toDto(updatedServiceOrder);

        restServiceOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServiceOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServiceOrderToMatchAllProperties(updatedServiceOrder);
    }

    @Test
    @Transactional
    void putNonExistingServiceOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceOrder.setId(longCount.incrementAndGet());

        // Create the ServiceOrder
        ServiceOrderDTO serviceOrderDTO = serviceOrderMapper.toDto(serviceOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceOrder.setId(longCount.incrementAndGet());

        // Create the ServiceOrder
        ServiceOrderDTO serviceOrderDTO = serviceOrderMapper.toDto(serviceOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceOrder.setId(longCount.incrementAndGet());

        // Create the ServiceOrder
        ServiceOrderDTO serviceOrderDTO = serviceOrderMapper.toDto(serviceOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceOrderWithPatch() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceOrder using partial update
        ServiceOrder partialUpdatedServiceOrder = new ServiceOrder();
        partialUpdatedServiceOrder.setId(serviceOrder.getId());

        partialUpdatedServiceOrder
            .initialCost(UPDATED_INITIAL_COST)
            .additionalCost(UPDATED_ADDITIONAL_COST)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY);

        restServiceOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceOrder))
            )
            .andExpect(status().isOk());

        // Validate the ServiceOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceOrderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServiceOrder, serviceOrder),
            getPersistedServiceOrder(serviceOrder)
        );
    }

    @Test
    @Transactional
    void fullUpdateServiceOrderWithPatch() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceOrder using partial update
        ServiceOrder partialUpdatedServiceOrder = new ServiceOrder();
        partialUpdatedServiceOrder.setId(serviceOrder.getId());

        partialUpdatedServiceOrder
            .creationDate(UPDATED_CREATION_DATE)
            .startDate(UPDATED_START_DATE)
            .completionDate(UPDATED_COMPLETION_DATE)
            .initialCost(UPDATED_INITIAL_COST)
            .additionalCost(UPDATED_ADDITIONAL_COST)
            .totalCost(UPDATED_TOTAL_COST)
            .notes(UPDATED_NOTES)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restServiceOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceOrder))
            )
            .andExpect(status().isOk());

        // Validate the ServiceOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceOrderUpdatableFieldsEquals(partialUpdatedServiceOrder, getPersistedServiceOrder(partialUpdatedServiceOrder));
    }

    @Test
    @Transactional
    void patchNonExistingServiceOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceOrder.setId(longCount.incrementAndGet());

        // Create the ServiceOrder
        ServiceOrderDTO serviceOrderDTO = serviceOrderMapper.toDto(serviceOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceOrder.setId(longCount.incrementAndGet());

        // Create the ServiceOrder
        ServiceOrderDTO serviceOrderDTO = serviceOrderMapper.toDto(serviceOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceOrder.setId(longCount.incrementAndGet());

        // Create the ServiceOrder
        ServiceOrderDTO serviceOrderDTO = serviceOrderMapper.toDto(serviceOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(serviceOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceOrder() throws Exception {
        // Initialize the database
        insertedServiceOrder = serviceOrderRepository.saveAndFlush(serviceOrder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the serviceOrder
        restServiceOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return serviceOrderRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ServiceOrder getPersistedServiceOrder(ServiceOrder serviceOrder) {
        return serviceOrderRepository.findById(serviceOrder.getId()).orElseThrow();
    }

    protected void assertPersistedServiceOrderToMatchAllProperties(ServiceOrder expectedServiceOrder) {
        assertServiceOrderAllPropertiesEquals(expectedServiceOrder, getPersistedServiceOrder(expectedServiceOrder));
    }

    protected void assertPersistedServiceOrderToMatchUpdatableProperties(ServiceOrder expectedServiceOrder) {
        assertServiceOrderAllUpdatablePropertiesEquals(expectedServiceOrder, getPersistedServiceOrder(expectedServiceOrder));
    }
}
