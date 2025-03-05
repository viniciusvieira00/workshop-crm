package com.vabv.crm.web.rest;

import static com.vabv.crm.domain.VehicleAsserts.*;
import static com.vabv.crm.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vabv.crm.IntegrationTest;
import com.vabv.crm.domain.Client;
import com.vabv.crm.domain.Vehicle;
import com.vabv.crm.domain.enumeration.VehicleStatus;
import com.vabv.crm.repository.VehicleRepository;
import com.vabv.crm.service.VehicleService;
import com.vabv.crm.service.dto.VehicleDTO;
import com.vabv.crm.service.mapper.VehicleMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link VehicleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VehicleResourceIT {

    private static final String DEFAULT_LICENSE_PLATE = "ODNXLIL";
    private static final String UPDATED_LICENSE_PLATE = "T4MS4LP";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FABRICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FABRICATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FABRICATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_MODEL_YEAR = 1900;
    private static final Integer UPDATED_MODEL_YEAR = 1901;
    private static final Integer SMALLER_MODEL_YEAR = 1900 - 1;

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_RENAVAM = "1243051137";
    private static final String UPDATED_RENAVAM = "952751612";

    private static final String DEFAULT_FUEL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FUEL_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CHASSI_NUMBER = "42RXBTRMALFCIAMUR";
    private static final String UPDATED_CHASSI_NUMBER = "3GN11BWD4CQEK14GW";

    private static final Integer DEFAULT_CURRENT_MILEAGE = 0;
    private static final Integer UPDATED_CURRENT_MILEAGE = 1;
    private static final Integer SMALLER_CURRENT_MILEAGE = 0 - 1;

    private static final LocalDate DEFAULT_LAST_MAINTENANCE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MAINTENANCE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MAINTENANCE_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_LAST_MAINTENANCE_MILEAGE = 0;
    private static final Integer UPDATED_LAST_MAINTENANCE_MILEAGE = 1;
    private static final Integer SMALLER_LAST_MAINTENANCE_MILEAGE = 0 - 1;

    private static final LocalDate DEFAULT_NEXT_MAINTENANCE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NEXT_MAINTENANCE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_NEXT_MAINTENANCE_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_NEXT_MAINTENANCE_MILEAGE = 0;
    private static final Integer UPDATED_NEXT_MAINTENANCE_MILEAGE = 1;
    private static final Integer SMALLER_NEXT_MAINTENANCE_MILEAGE = 0 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final VehicleStatus DEFAULT_STATUS = VehicleStatus.AVAILABLE;
    private static final VehicleStatus UPDATED_STATUS = VehicleStatus.UNDER_MAINTENANCE;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/vehicles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleRepository vehicleRepositoryMock;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Mock
    private VehicleService vehicleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleMockMvc;

    private Vehicle vehicle;

    private Vehicle insertedVehicle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicle createEntity(EntityManager em) {
        Vehicle vehicle = new Vehicle()
            .licensePlate(DEFAULT_LICENSE_PLATE)
            .model(DEFAULT_MODEL)
            .brand(DEFAULT_BRAND)
            .fabricationDate(DEFAULT_FABRICATION_DATE)
            .modelYear(DEFAULT_MODEL_YEAR)
            .color(DEFAULT_COLOR)
            .renavam(DEFAULT_RENAVAM)
            .fuelType(DEFAULT_FUEL_TYPE)
            .chassiNumber(DEFAULT_CHASSI_NUMBER)
            .currentMileage(DEFAULT_CURRENT_MILEAGE)
            .lastMaintenanceDate(DEFAULT_LAST_MAINTENANCE_DATE)
            .lastMaintenanceMileage(DEFAULT_LAST_MAINTENANCE_MILEAGE)
            .nextMaintenanceDate(DEFAULT_NEXT_MAINTENANCE_DATE)
            .nextMaintenanceMileage(DEFAULT_NEXT_MAINTENANCE_MILEAGE)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createEntity();
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        vehicle.setClient(client);
        return vehicle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicle createUpdatedEntity(EntityManager em) {
        Vehicle updatedVehicle = new Vehicle()
            .licensePlate(UPDATED_LICENSE_PLATE)
            .model(UPDATED_MODEL)
            .brand(UPDATED_BRAND)
            .fabricationDate(UPDATED_FABRICATION_DATE)
            .modelYear(UPDATED_MODEL_YEAR)
            .color(UPDATED_COLOR)
            .renavam(UPDATED_RENAVAM)
            .fuelType(UPDATED_FUEL_TYPE)
            .chassiNumber(UPDATED_CHASSI_NUMBER)
            .currentMileage(UPDATED_CURRENT_MILEAGE)
            .lastMaintenanceDate(UPDATED_LAST_MAINTENANCE_DATE)
            .lastMaintenanceMileage(UPDATED_LAST_MAINTENANCE_MILEAGE)
            .nextMaintenanceDate(UPDATED_NEXT_MAINTENANCE_DATE)
            .nextMaintenanceMileage(UPDATED_NEXT_MAINTENANCE_MILEAGE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createUpdatedEntity();
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        updatedVehicle.setClient(client);
        return updatedVehicle;
    }

    @BeforeEach
    public void initTest() {
        vehicle = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicle != null) {
            vehicleRepository.delete(insertedVehicle);
            insertedVehicle = null;
        }
    }

    @Test
    @Transactional
    void createVehicle() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);
        var returnedVehicleDTO = om.readValue(
            restVehicleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleDTO.class
        );

        // Validate the Vehicle in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVehicle = vehicleMapper.toEntity(returnedVehicleDTO);
        assertVehicleUpdatableFieldsEquals(returnedVehicle, getPersistedVehicle(returnedVehicle));

        insertedVehicle = returnedVehicle;
    }

    @Test
    @Transactional
    void createVehicleWithExistingId() throws Exception {
        // Create the Vehicle with an existing ID
        vehicle.setId(1L);
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLicensePlateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicle.setLicensePlate(null);

        // Create the Vehicle, which fails.
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicle.setModel(null);

        // Create the Vehicle, which fails.
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBrandIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicle.setBrand(null);

        // Create the Vehicle, which fails.
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFabricationDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicle.setFabricationDate(null);

        // Create the Vehicle, which fails.
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModelYearIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicle.setModelYear(null);

        // Create the Vehicle, which fails.
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkColorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicle.setColor(null);

        // Create the Vehicle, which fails.
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFuelTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicle.setFuelType(null);

        // Create the Vehicle, which fails.
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrentMileageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicle.setCurrentMileage(null);

        // Create the Vehicle, which fails.
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicle.setStatus(null);

        // Create the Vehicle, which fails.
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicles() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicle.getId().intValue())))
            .andExpect(jsonPath("$.[*].licensePlate").value(hasItem(DEFAULT_LICENSE_PLATE)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].fabricationDate").value(hasItem(DEFAULT_FABRICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modelYear").value(hasItem(DEFAULT_MODEL_YEAR)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].renavam").value(hasItem(DEFAULT_RENAVAM)))
            .andExpect(jsonPath("$.[*].fuelType").value(hasItem(DEFAULT_FUEL_TYPE)))
            .andExpect(jsonPath("$.[*].chassiNumber").value(hasItem(DEFAULT_CHASSI_NUMBER)))
            .andExpect(jsonPath("$.[*].currentMileage").value(hasItem(DEFAULT_CURRENT_MILEAGE)))
            .andExpect(jsonPath("$.[*].lastMaintenanceDate").value(hasItem(DEFAULT_LAST_MAINTENANCE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastMaintenanceMileage").value(hasItem(DEFAULT_LAST_MAINTENANCE_MILEAGE)))
            .andExpect(jsonPath("$.[*].nextMaintenanceDate").value(hasItem(DEFAULT_NEXT_MAINTENANCE_DATE.toString())))
            .andExpect(jsonPath("$.[*].nextMaintenanceMileage").value(hasItem(DEFAULT_NEXT_MAINTENANCE_MILEAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVehiclesWithEagerRelationshipsIsEnabled() throws Exception {
        when(vehicleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVehicleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vehicleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVehiclesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vehicleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVehicleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vehicleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVehicle() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get the vehicle
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicle.getId().intValue()))
            .andExpect(jsonPath("$.licensePlate").value(DEFAULT_LICENSE_PLATE))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND))
            .andExpect(jsonPath("$.fabricationDate").value(DEFAULT_FABRICATION_DATE.toString()))
            .andExpect(jsonPath("$.modelYear").value(DEFAULT_MODEL_YEAR))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.renavam").value(DEFAULT_RENAVAM))
            .andExpect(jsonPath("$.fuelType").value(DEFAULT_FUEL_TYPE))
            .andExpect(jsonPath("$.chassiNumber").value(DEFAULT_CHASSI_NUMBER))
            .andExpect(jsonPath("$.currentMileage").value(DEFAULT_CURRENT_MILEAGE))
            .andExpect(jsonPath("$.lastMaintenanceDate").value(DEFAULT_LAST_MAINTENANCE_DATE.toString()))
            .andExpect(jsonPath("$.lastMaintenanceMileage").value(DEFAULT_LAST_MAINTENANCE_MILEAGE))
            .andExpect(jsonPath("$.nextMaintenanceDate").value(DEFAULT_NEXT_MAINTENANCE_DATE.toString()))
            .andExpect(jsonPath("$.nextMaintenanceMileage").value(DEFAULT_NEXT_MAINTENANCE_MILEAGE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getVehiclesByIdFiltering() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        Long id = vehicle.getId();

        defaultVehicleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultVehicleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultVehicleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVehiclesByLicensePlateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlate equals to
        defaultVehicleFiltering("licensePlate.equals=" + DEFAULT_LICENSE_PLATE, "licensePlate.equals=" + UPDATED_LICENSE_PLATE);
    }

    @Test
    @Transactional
    void getAllVehiclesByLicensePlateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlate in
        defaultVehicleFiltering(
            "licensePlate.in=" + DEFAULT_LICENSE_PLATE + "," + UPDATED_LICENSE_PLATE,
            "licensePlate.in=" + UPDATED_LICENSE_PLATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLicensePlateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlate is not null
        defaultVehicleFiltering("licensePlate.specified=true", "licensePlate.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByLicensePlateContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlate contains
        defaultVehicleFiltering("licensePlate.contains=" + DEFAULT_LICENSE_PLATE, "licensePlate.contains=" + UPDATED_LICENSE_PLATE);
    }

    @Test
    @Transactional
    void getAllVehiclesByLicensePlateNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where licensePlate does not contain
        defaultVehicleFiltering(
            "licensePlate.doesNotContain=" + UPDATED_LICENSE_PLATE,
            "licensePlate.doesNotContain=" + DEFAULT_LICENSE_PLATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where model equals to
        defaultVehicleFiltering("model.equals=" + DEFAULT_MODEL, "model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllVehiclesByModelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where model in
        defaultVehicleFiltering("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL, "model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllVehiclesByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where model is not null
        defaultVehicleFiltering("model.specified=true", "model.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByModelContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where model contains
        defaultVehicleFiltering("model.contains=" + DEFAULT_MODEL, "model.contains=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllVehiclesByModelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where model does not contain
        defaultVehicleFiltering("model.doesNotContain=" + UPDATED_MODEL, "model.doesNotContain=" + DEFAULT_MODEL);
    }

    @Test
    @Transactional
    void getAllVehiclesByBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where brand equals to
        defaultVehicleFiltering("brand.equals=" + DEFAULT_BRAND, "brand.equals=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllVehiclesByBrandIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where brand in
        defaultVehicleFiltering("brand.in=" + DEFAULT_BRAND + "," + UPDATED_BRAND, "brand.in=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllVehiclesByBrandIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where brand is not null
        defaultVehicleFiltering("brand.specified=true", "brand.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByBrandContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where brand contains
        defaultVehicleFiltering("brand.contains=" + DEFAULT_BRAND, "brand.contains=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllVehiclesByBrandNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where brand does not contain
        defaultVehicleFiltering("brand.doesNotContain=" + UPDATED_BRAND, "brand.doesNotContain=" + DEFAULT_BRAND);
    }

    @Test
    @Transactional
    void getAllVehiclesByFabricationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fabricationDate equals to
        defaultVehicleFiltering("fabricationDate.equals=" + DEFAULT_FABRICATION_DATE, "fabricationDate.equals=" + UPDATED_FABRICATION_DATE);
    }

    @Test
    @Transactional
    void getAllVehiclesByFabricationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fabricationDate in
        defaultVehicleFiltering(
            "fabricationDate.in=" + DEFAULT_FABRICATION_DATE + "," + UPDATED_FABRICATION_DATE,
            "fabricationDate.in=" + UPDATED_FABRICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByFabricationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fabricationDate is not null
        defaultVehicleFiltering("fabricationDate.specified=true", "fabricationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByFabricationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fabricationDate is greater than or equal to
        defaultVehicleFiltering(
            "fabricationDate.greaterThanOrEqual=" + DEFAULT_FABRICATION_DATE,
            "fabricationDate.greaterThanOrEqual=" + UPDATED_FABRICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByFabricationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fabricationDate is less than or equal to
        defaultVehicleFiltering(
            "fabricationDate.lessThanOrEqual=" + DEFAULT_FABRICATION_DATE,
            "fabricationDate.lessThanOrEqual=" + SMALLER_FABRICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByFabricationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fabricationDate is less than
        defaultVehicleFiltering(
            "fabricationDate.lessThan=" + UPDATED_FABRICATION_DATE,
            "fabricationDate.lessThan=" + DEFAULT_FABRICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByFabricationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fabricationDate is greater than
        defaultVehicleFiltering(
            "fabricationDate.greaterThan=" + SMALLER_FABRICATION_DATE,
            "fabricationDate.greaterThan=" + DEFAULT_FABRICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByModelYearIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where modelYear equals to
        defaultVehicleFiltering("modelYear.equals=" + DEFAULT_MODEL_YEAR, "modelYear.equals=" + UPDATED_MODEL_YEAR);
    }

    @Test
    @Transactional
    void getAllVehiclesByModelYearIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where modelYear in
        defaultVehicleFiltering("modelYear.in=" + DEFAULT_MODEL_YEAR + "," + UPDATED_MODEL_YEAR, "modelYear.in=" + UPDATED_MODEL_YEAR);
    }

    @Test
    @Transactional
    void getAllVehiclesByModelYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where modelYear is not null
        defaultVehicleFiltering("modelYear.specified=true", "modelYear.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByModelYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where modelYear is greater than or equal to
        defaultVehicleFiltering(
            "modelYear.greaterThanOrEqual=" + DEFAULT_MODEL_YEAR,
            "modelYear.greaterThanOrEqual=" + (DEFAULT_MODEL_YEAR + 1)
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByModelYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where modelYear is less than or equal to
        defaultVehicleFiltering("modelYear.lessThanOrEqual=" + DEFAULT_MODEL_YEAR, "modelYear.lessThanOrEqual=" + SMALLER_MODEL_YEAR);
    }

    @Test
    @Transactional
    void getAllVehiclesByModelYearIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where modelYear is less than
        defaultVehicleFiltering("modelYear.lessThan=" + (DEFAULT_MODEL_YEAR + 1), "modelYear.lessThan=" + DEFAULT_MODEL_YEAR);
    }

    @Test
    @Transactional
    void getAllVehiclesByModelYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where modelYear is greater than
        defaultVehicleFiltering("modelYear.greaterThan=" + SMALLER_MODEL_YEAR, "modelYear.greaterThan=" + DEFAULT_MODEL_YEAR);
    }

    @Test
    @Transactional
    void getAllVehiclesByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where color equals to
        defaultVehicleFiltering("color.equals=" + DEFAULT_COLOR, "color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllVehiclesByColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where color in
        defaultVehicleFiltering("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR, "color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllVehiclesByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where color is not null
        defaultVehicleFiltering("color.specified=true", "color.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByColorContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where color contains
        defaultVehicleFiltering("color.contains=" + DEFAULT_COLOR, "color.contains=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllVehiclesByColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where color does not contain
        defaultVehicleFiltering("color.doesNotContain=" + UPDATED_COLOR, "color.doesNotContain=" + DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void getAllVehiclesByRenavamIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where renavam equals to
        defaultVehicleFiltering("renavam.equals=" + DEFAULT_RENAVAM, "renavam.equals=" + UPDATED_RENAVAM);
    }

    @Test
    @Transactional
    void getAllVehiclesByRenavamIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where renavam in
        defaultVehicleFiltering("renavam.in=" + DEFAULT_RENAVAM + "," + UPDATED_RENAVAM, "renavam.in=" + UPDATED_RENAVAM);
    }

    @Test
    @Transactional
    void getAllVehiclesByRenavamIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where renavam is not null
        defaultVehicleFiltering("renavam.specified=true", "renavam.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByRenavamContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where renavam contains
        defaultVehicleFiltering("renavam.contains=" + DEFAULT_RENAVAM, "renavam.contains=" + UPDATED_RENAVAM);
    }

    @Test
    @Transactional
    void getAllVehiclesByRenavamNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where renavam does not contain
        defaultVehicleFiltering("renavam.doesNotContain=" + UPDATED_RENAVAM, "renavam.doesNotContain=" + DEFAULT_RENAVAM);
    }

    @Test
    @Transactional
    void getAllVehiclesByFuelTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fuelType equals to
        defaultVehicleFiltering("fuelType.equals=" + DEFAULT_FUEL_TYPE, "fuelType.equals=" + UPDATED_FUEL_TYPE);
    }

    @Test
    @Transactional
    void getAllVehiclesByFuelTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fuelType in
        defaultVehicleFiltering("fuelType.in=" + DEFAULT_FUEL_TYPE + "," + UPDATED_FUEL_TYPE, "fuelType.in=" + UPDATED_FUEL_TYPE);
    }

    @Test
    @Transactional
    void getAllVehiclesByFuelTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fuelType is not null
        defaultVehicleFiltering("fuelType.specified=true", "fuelType.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByFuelTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fuelType contains
        defaultVehicleFiltering("fuelType.contains=" + DEFAULT_FUEL_TYPE, "fuelType.contains=" + UPDATED_FUEL_TYPE);
    }

    @Test
    @Transactional
    void getAllVehiclesByFuelTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fuelType does not contain
        defaultVehicleFiltering("fuelType.doesNotContain=" + UPDATED_FUEL_TYPE, "fuelType.doesNotContain=" + DEFAULT_FUEL_TYPE);
    }

    @Test
    @Transactional
    void getAllVehiclesByChassiNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where chassiNumber equals to
        defaultVehicleFiltering("chassiNumber.equals=" + DEFAULT_CHASSI_NUMBER, "chassiNumber.equals=" + UPDATED_CHASSI_NUMBER);
    }

    @Test
    @Transactional
    void getAllVehiclesByChassiNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where chassiNumber in
        defaultVehicleFiltering(
            "chassiNumber.in=" + DEFAULT_CHASSI_NUMBER + "," + UPDATED_CHASSI_NUMBER,
            "chassiNumber.in=" + UPDATED_CHASSI_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByChassiNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where chassiNumber is not null
        defaultVehicleFiltering("chassiNumber.specified=true", "chassiNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByChassiNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where chassiNumber contains
        defaultVehicleFiltering("chassiNumber.contains=" + DEFAULT_CHASSI_NUMBER, "chassiNumber.contains=" + UPDATED_CHASSI_NUMBER);
    }

    @Test
    @Transactional
    void getAllVehiclesByChassiNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where chassiNumber does not contain
        defaultVehicleFiltering(
            "chassiNumber.doesNotContain=" + UPDATED_CHASSI_NUMBER,
            "chassiNumber.doesNotContain=" + DEFAULT_CHASSI_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByCurrentMileageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where currentMileage equals to
        defaultVehicleFiltering("currentMileage.equals=" + DEFAULT_CURRENT_MILEAGE, "currentMileage.equals=" + UPDATED_CURRENT_MILEAGE);
    }

    @Test
    @Transactional
    void getAllVehiclesByCurrentMileageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where currentMileage in
        defaultVehicleFiltering(
            "currentMileage.in=" + DEFAULT_CURRENT_MILEAGE + "," + UPDATED_CURRENT_MILEAGE,
            "currentMileage.in=" + UPDATED_CURRENT_MILEAGE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByCurrentMileageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where currentMileage is not null
        defaultVehicleFiltering("currentMileage.specified=true", "currentMileage.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByCurrentMileageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where currentMileage is greater than or equal to
        defaultVehicleFiltering(
            "currentMileage.greaterThanOrEqual=" + DEFAULT_CURRENT_MILEAGE,
            "currentMileage.greaterThanOrEqual=" + UPDATED_CURRENT_MILEAGE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByCurrentMileageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where currentMileage is less than or equal to
        defaultVehicleFiltering(
            "currentMileage.lessThanOrEqual=" + DEFAULT_CURRENT_MILEAGE,
            "currentMileage.lessThanOrEqual=" + SMALLER_CURRENT_MILEAGE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByCurrentMileageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where currentMileage is less than
        defaultVehicleFiltering("currentMileage.lessThan=" + UPDATED_CURRENT_MILEAGE, "currentMileage.lessThan=" + DEFAULT_CURRENT_MILEAGE);
    }

    @Test
    @Transactional
    void getAllVehiclesByCurrentMileageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where currentMileage is greater than
        defaultVehicleFiltering(
            "currentMileage.greaterThan=" + SMALLER_CURRENT_MILEAGE,
            "currentMileage.greaterThan=" + DEFAULT_CURRENT_MILEAGE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastMaintenanceDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastMaintenanceDate equals to
        defaultVehicleFiltering(
            "lastMaintenanceDate.equals=" + DEFAULT_LAST_MAINTENANCE_DATE,
            "lastMaintenanceDate.equals=" + UPDATED_LAST_MAINTENANCE_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastMaintenanceDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastMaintenanceDate in
        defaultVehicleFiltering(
            "lastMaintenanceDate.in=" + DEFAULT_LAST_MAINTENANCE_DATE + "," + UPDATED_LAST_MAINTENANCE_DATE,
            "lastMaintenanceDate.in=" + UPDATED_LAST_MAINTENANCE_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastMaintenanceDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastMaintenanceDate is not null
        defaultVehicleFiltering("lastMaintenanceDate.specified=true", "lastMaintenanceDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByLastMaintenanceDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastMaintenanceDate is greater than or equal to
        defaultVehicleFiltering(
            "lastMaintenanceDate.greaterThanOrEqual=" + DEFAULT_LAST_MAINTENANCE_DATE,
            "lastMaintenanceDate.greaterThanOrEqual=" + UPDATED_LAST_MAINTENANCE_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastMaintenanceDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastMaintenanceDate is less than or equal to
        defaultVehicleFiltering(
            "lastMaintenanceDate.lessThanOrEqual=" + DEFAULT_LAST_MAINTENANCE_DATE,
            "lastMaintenanceDate.lessThanOrEqual=" + SMALLER_LAST_MAINTENANCE_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastMaintenanceDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastMaintenanceDate is less than
        defaultVehicleFiltering(
            "lastMaintenanceDate.lessThan=" + UPDATED_LAST_MAINTENANCE_DATE,
            "lastMaintenanceDate.lessThan=" + DEFAULT_LAST_MAINTENANCE_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastMaintenanceDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastMaintenanceDate is greater than
        defaultVehicleFiltering(
            "lastMaintenanceDate.greaterThan=" + SMALLER_LAST_MAINTENANCE_DATE,
            "lastMaintenanceDate.greaterThan=" + DEFAULT_LAST_MAINTENANCE_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastMaintenanceMileageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastMaintenanceMileage equals to
        defaultVehicleFiltering(
            "lastMaintenanceMileage.equals=" + DEFAULT_LAST_MAINTENANCE_MILEAGE,
            "lastMaintenanceMileage.equals=" + UPDATED_LAST_MAINTENANCE_MILEAGE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastMaintenanceMileageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastMaintenanceMileage in
        defaultVehicleFiltering(
            "lastMaintenanceMileage.in=" + DEFAULT_LAST_MAINTENANCE_MILEAGE + "," + UPDATED_LAST_MAINTENANCE_MILEAGE,
            "lastMaintenanceMileage.in=" + UPDATED_LAST_MAINTENANCE_MILEAGE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastMaintenanceMileageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastMaintenanceMileage is not null
        defaultVehicleFiltering("lastMaintenanceMileage.specified=true", "lastMaintenanceMileage.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByLastMaintenanceMileageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastMaintenanceMileage is greater than or equal to
        defaultVehicleFiltering(
            "lastMaintenanceMileage.greaterThanOrEqual=" + DEFAULT_LAST_MAINTENANCE_MILEAGE,
            "lastMaintenanceMileage.greaterThanOrEqual=" + UPDATED_LAST_MAINTENANCE_MILEAGE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastMaintenanceMileageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastMaintenanceMileage is less than or equal to
        defaultVehicleFiltering(
            "lastMaintenanceMileage.lessThanOrEqual=" + DEFAULT_LAST_MAINTENANCE_MILEAGE,
            "lastMaintenanceMileage.lessThanOrEqual=" + SMALLER_LAST_MAINTENANCE_MILEAGE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastMaintenanceMileageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastMaintenanceMileage is less than
        defaultVehicleFiltering(
            "lastMaintenanceMileage.lessThan=" + UPDATED_LAST_MAINTENANCE_MILEAGE,
            "lastMaintenanceMileage.lessThan=" + DEFAULT_LAST_MAINTENANCE_MILEAGE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastMaintenanceMileageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastMaintenanceMileage is greater than
        defaultVehicleFiltering(
            "lastMaintenanceMileage.greaterThan=" + SMALLER_LAST_MAINTENANCE_MILEAGE,
            "lastMaintenanceMileage.greaterThan=" + DEFAULT_LAST_MAINTENANCE_MILEAGE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNextMaintenanceDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nextMaintenanceDate equals to
        defaultVehicleFiltering(
            "nextMaintenanceDate.equals=" + DEFAULT_NEXT_MAINTENANCE_DATE,
            "nextMaintenanceDate.equals=" + UPDATED_NEXT_MAINTENANCE_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNextMaintenanceDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nextMaintenanceDate in
        defaultVehicleFiltering(
            "nextMaintenanceDate.in=" + DEFAULT_NEXT_MAINTENANCE_DATE + "," + UPDATED_NEXT_MAINTENANCE_DATE,
            "nextMaintenanceDate.in=" + UPDATED_NEXT_MAINTENANCE_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNextMaintenanceDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nextMaintenanceDate is not null
        defaultVehicleFiltering("nextMaintenanceDate.specified=true", "nextMaintenanceDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByNextMaintenanceDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nextMaintenanceDate is greater than or equal to
        defaultVehicleFiltering(
            "nextMaintenanceDate.greaterThanOrEqual=" + DEFAULT_NEXT_MAINTENANCE_DATE,
            "nextMaintenanceDate.greaterThanOrEqual=" + UPDATED_NEXT_MAINTENANCE_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNextMaintenanceDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nextMaintenanceDate is less than or equal to
        defaultVehicleFiltering(
            "nextMaintenanceDate.lessThanOrEqual=" + DEFAULT_NEXT_MAINTENANCE_DATE,
            "nextMaintenanceDate.lessThanOrEqual=" + SMALLER_NEXT_MAINTENANCE_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNextMaintenanceDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nextMaintenanceDate is less than
        defaultVehicleFiltering(
            "nextMaintenanceDate.lessThan=" + UPDATED_NEXT_MAINTENANCE_DATE,
            "nextMaintenanceDate.lessThan=" + DEFAULT_NEXT_MAINTENANCE_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNextMaintenanceDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nextMaintenanceDate is greater than
        defaultVehicleFiltering(
            "nextMaintenanceDate.greaterThan=" + SMALLER_NEXT_MAINTENANCE_DATE,
            "nextMaintenanceDate.greaterThan=" + DEFAULT_NEXT_MAINTENANCE_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNextMaintenanceMileageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nextMaintenanceMileage equals to
        defaultVehicleFiltering(
            "nextMaintenanceMileage.equals=" + DEFAULT_NEXT_MAINTENANCE_MILEAGE,
            "nextMaintenanceMileage.equals=" + UPDATED_NEXT_MAINTENANCE_MILEAGE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNextMaintenanceMileageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nextMaintenanceMileage in
        defaultVehicleFiltering(
            "nextMaintenanceMileage.in=" + DEFAULT_NEXT_MAINTENANCE_MILEAGE + "," + UPDATED_NEXT_MAINTENANCE_MILEAGE,
            "nextMaintenanceMileage.in=" + UPDATED_NEXT_MAINTENANCE_MILEAGE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNextMaintenanceMileageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nextMaintenanceMileage is not null
        defaultVehicleFiltering("nextMaintenanceMileage.specified=true", "nextMaintenanceMileage.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByNextMaintenanceMileageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nextMaintenanceMileage is greater than or equal to
        defaultVehicleFiltering(
            "nextMaintenanceMileage.greaterThanOrEqual=" + DEFAULT_NEXT_MAINTENANCE_MILEAGE,
            "nextMaintenanceMileage.greaterThanOrEqual=" + UPDATED_NEXT_MAINTENANCE_MILEAGE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNextMaintenanceMileageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nextMaintenanceMileage is less than or equal to
        defaultVehicleFiltering(
            "nextMaintenanceMileage.lessThanOrEqual=" + DEFAULT_NEXT_MAINTENANCE_MILEAGE,
            "nextMaintenanceMileage.lessThanOrEqual=" + SMALLER_NEXT_MAINTENANCE_MILEAGE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNextMaintenanceMileageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nextMaintenanceMileage is less than
        defaultVehicleFiltering(
            "nextMaintenanceMileage.lessThan=" + UPDATED_NEXT_MAINTENANCE_MILEAGE,
            "nextMaintenanceMileage.lessThan=" + DEFAULT_NEXT_MAINTENANCE_MILEAGE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNextMaintenanceMileageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nextMaintenanceMileage is greater than
        defaultVehicleFiltering(
            "nextMaintenanceMileage.greaterThan=" + SMALLER_NEXT_MAINTENANCE_MILEAGE,
            "nextMaintenanceMileage.greaterThan=" + DEFAULT_NEXT_MAINTENANCE_MILEAGE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where description equals to
        defaultVehicleFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVehiclesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where description in
        defaultVehicleFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where description is not null
        defaultVehicleFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where description contains
        defaultVehicleFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVehiclesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where description does not contain
        defaultVehicleFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVehiclesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where status equals to
        defaultVehicleFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllVehiclesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where status in
        defaultVehicleFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllVehiclesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where status is not null
        defaultVehicleFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where createdBy equals to
        defaultVehicleFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllVehiclesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where createdBy in
        defaultVehicleFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllVehiclesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where createdBy is not null
        defaultVehicleFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where createdBy contains
        defaultVehicleFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllVehiclesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where createdBy does not contain
        defaultVehicleFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllVehiclesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where createdDate equals to
        defaultVehicleFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllVehiclesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where createdDate in
        defaultVehicleFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where createdDate is not null
        defaultVehicleFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastModifiedBy equals to
        defaultVehicleFiltering("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllVehiclesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastModifiedBy in
        defaultVehicleFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastModifiedBy is not null
        defaultVehicleFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastModifiedBy contains
        defaultVehicleFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastModifiedBy does not contain
        defaultVehicleFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastModifiedDate equals to
        defaultVehicleFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastModifiedDate in
        defaultVehicleFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where lastModifiedDate is not null
        defaultVehicleFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByClientIsEqualToSomething() throws Exception {
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            vehicleRepository.saveAndFlush(vehicle);
            client = ClientResourceIT.createEntity();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        em.persist(client);
        em.flush();
        vehicle.setClient(client);
        vehicleRepository.saveAndFlush(vehicle);
        Long clientId = client.getId();
        // Get all the vehicleList where client equals to clientId
        defaultVehicleShouldBeFound("clientId.equals=" + clientId);

        // Get all the vehicleList where client equals to (clientId + 1)
        defaultVehicleShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    private void defaultVehicleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultVehicleShouldBeFound(shouldBeFound);
        defaultVehicleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVehicleShouldBeFound(String filter) throws Exception {
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicle.getId().intValue())))
            .andExpect(jsonPath("$.[*].licensePlate").value(hasItem(DEFAULT_LICENSE_PLATE)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].fabricationDate").value(hasItem(DEFAULT_FABRICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modelYear").value(hasItem(DEFAULT_MODEL_YEAR)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].renavam").value(hasItem(DEFAULT_RENAVAM)))
            .andExpect(jsonPath("$.[*].fuelType").value(hasItem(DEFAULT_FUEL_TYPE)))
            .andExpect(jsonPath("$.[*].chassiNumber").value(hasItem(DEFAULT_CHASSI_NUMBER)))
            .andExpect(jsonPath("$.[*].currentMileage").value(hasItem(DEFAULT_CURRENT_MILEAGE)))
            .andExpect(jsonPath("$.[*].lastMaintenanceDate").value(hasItem(DEFAULT_LAST_MAINTENANCE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastMaintenanceMileage").value(hasItem(DEFAULT_LAST_MAINTENANCE_MILEAGE)))
            .andExpect(jsonPath("$.[*].nextMaintenanceDate").value(hasItem(DEFAULT_NEXT_MAINTENANCE_DATE.toString())))
            .andExpect(jsonPath("$.[*].nextMaintenanceMileage").value(hasItem(DEFAULT_NEXT_MAINTENANCE_MILEAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVehicleShouldNotBeFound(String filter) throws Exception {
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVehicle() throws Exception {
        // Get the vehicle
        restVehicleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicle() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicle
        Vehicle updatedVehicle = vehicleRepository.findById(vehicle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicle are not directly saved in db
        em.detach(updatedVehicle);
        updatedVehicle
            .licensePlate(UPDATED_LICENSE_PLATE)
            .model(UPDATED_MODEL)
            .brand(UPDATED_BRAND)
            .fabricationDate(UPDATED_FABRICATION_DATE)
            .modelYear(UPDATED_MODEL_YEAR)
            .color(UPDATED_COLOR)
            .renavam(UPDATED_RENAVAM)
            .fuelType(UPDATED_FUEL_TYPE)
            .chassiNumber(UPDATED_CHASSI_NUMBER)
            .currentMileage(UPDATED_CURRENT_MILEAGE)
            .lastMaintenanceDate(UPDATED_LAST_MAINTENANCE_DATE)
            .lastMaintenanceMileage(UPDATED_LAST_MAINTENANCE_MILEAGE)
            .nextMaintenanceDate(UPDATED_NEXT_MAINTENANCE_DATE)
            .nextMaintenanceMileage(UPDATED_NEXT_MAINTENANCE_MILEAGE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        VehicleDTO vehicleDTO = vehicleMapper.toDto(updatedVehicle);

        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleToMatchAllProperties(updatedVehicle);
    }

    @Test
    @Transactional
    void putNonExistingVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleWithPatch() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicle using partial update
        Vehicle partialUpdatedVehicle = new Vehicle();
        partialUpdatedVehicle.setId(vehicle.getId());

        partialUpdatedVehicle
            .model(UPDATED_MODEL)
            .brand(UPDATED_BRAND)
            .currentMileage(UPDATED_CURRENT_MILEAGE)
            .lastMaintenanceDate(UPDATED_LAST_MAINTENANCE_DATE)
            .lastMaintenanceMileage(UPDATED_LAST_MAINTENANCE_MILEAGE)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicle))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVehicle, vehicle), getPersistedVehicle(vehicle));
    }

    @Test
    @Transactional
    void fullUpdateVehicleWithPatch() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicle using partial update
        Vehicle partialUpdatedVehicle = new Vehicle();
        partialUpdatedVehicle.setId(vehicle.getId());

        partialUpdatedVehicle
            .licensePlate(UPDATED_LICENSE_PLATE)
            .model(UPDATED_MODEL)
            .brand(UPDATED_BRAND)
            .fabricationDate(UPDATED_FABRICATION_DATE)
            .modelYear(UPDATED_MODEL_YEAR)
            .color(UPDATED_COLOR)
            .renavam(UPDATED_RENAVAM)
            .fuelType(UPDATED_FUEL_TYPE)
            .chassiNumber(UPDATED_CHASSI_NUMBER)
            .currentMileage(UPDATED_CURRENT_MILEAGE)
            .lastMaintenanceDate(UPDATED_LAST_MAINTENANCE_DATE)
            .lastMaintenanceMileage(UPDATED_LAST_MAINTENANCE_MILEAGE)
            .nextMaintenanceDate(UPDATED_NEXT_MAINTENANCE_DATE)
            .nextMaintenanceMileage(UPDATED_NEXT_MAINTENANCE_MILEAGE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicle))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleUpdatableFieldsEquals(partialUpdatedVehicle, getPersistedVehicle(partialUpdatedVehicle));
    }

    @Test
    @Transactional
    void patchNonExistingVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicle() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicle
        restVehicleMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleRepository.count();
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

    protected Vehicle getPersistedVehicle(Vehicle vehicle) {
        return vehicleRepository.findById(vehicle.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleToMatchAllProperties(Vehicle expectedVehicle) {
        assertVehicleAllPropertiesEquals(expectedVehicle, getPersistedVehicle(expectedVehicle));
    }

    protected void assertPersistedVehicleToMatchUpdatableProperties(Vehicle expectedVehicle) {
        assertVehicleAllUpdatablePropertiesEquals(expectedVehicle, getPersistedVehicle(expectedVehicle));
    }
}
