package com.vabv.crm.web.rest;

import static com.vabv.crm.domain.ServiceOrderTypeAsserts.*;
import static com.vabv.crm.web.rest.TestUtil.createUpdateProxyForBean;
import static com.vabv.crm.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vabv.crm.IntegrationTest;
import com.vabv.crm.domain.ServiceOrderType;
import com.vabv.crm.repository.ServiceOrderTypeRepository;
import com.vabv.crm.service.dto.ServiceOrderTypeDTO;
import com.vabv.crm.service.mapper.ServiceOrderTypeMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ServiceOrderTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceOrderTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(0 - 1);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/service-order-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServiceOrderTypeRepository serviceOrderTypeRepository;

    @Autowired
    private ServiceOrderTypeMapper serviceOrderTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceOrderTypeMockMvc;

    private ServiceOrderType serviceOrderType;

    private ServiceOrderType insertedServiceOrderType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceOrderType createEntity() {
        return new ServiceOrderType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceOrderType createUpdatedEntity() {
        return new ServiceOrderType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        serviceOrderType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedServiceOrderType != null) {
            serviceOrderTypeRepository.delete(insertedServiceOrderType);
            insertedServiceOrderType = null;
        }
    }

    @Test
    @Transactional
    void createServiceOrderType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServiceOrderType
        ServiceOrderTypeDTO serviceOrderTypeDTO = serviceOrderTypeMapper.toDto(serviceOrderType);
        var returnedServiceOrderTypeDTO = om.readValue(
            restServiceOrderTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceOrderTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServiceOrderTypeDTO.class
        );

        // Validate the ServiceOrderType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServiceOrderType = serviceOrderTypeMapper.toEntity(returnedServiceOrderTypeDTO);
        assertServiceOrderTypeUpdatableFieldsEquals(returnedServiceOrderType, getPersistedServiceOrderType(returnedServiceOrderType));

        insertedServiceOrderType = returnedServiceOrderType;
    }

    @Test
    @Transactional
    void createServiceOrderTypeWithExistingId() throws Exception {
        // Create the ServiceOrderType with an existing ID
        serviceOrderType.setId(1L);
        ServiceOrderTypeDTO serviceOrderTypeDTO = serviceOrderTypeMapper.toDto(serviceOrderType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceOrderTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceOrderTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceOrderType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceOrderType.setName(null);

        // Create the ServiceOrderType, which fails.
        ServiceOrderTypeDTO serviceOrderTypeDTO = serviceOrderTypeMapper.toDto(serviceOrderType);

        restServiceOrderTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceOrderTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceOrderType.setPrice(null);

        // Create the ServiceOrderType, which fails.
        ServiceOrderTypeDTO serviceOrderTypeDTO = serviceOrderTypeMapper.toDto(serviceOrderType);

        restServiceOrderTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceOrderTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypes() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList
        restServiceOrderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOrderType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getServiceOrderType() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get the serviceOrderType
        restServiceOrderTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceOrderType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceOrderType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getServiceOrderTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        Long id = serviceOrderType.getId();

        defaultServiceOrderTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultServiceOrderTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultServiceOrderTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where name equals to
        defaultServiceOrderTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where name in
        defaultServiceOrderTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where name is not null
        defaultServiceOrderTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where name contains
        defaultServiceOrderTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where name does not contain
        defaultServiceOrderTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where description equals to
        defaultServiceOrderTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where description in
        defaultServiceOrderTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where description is not null
        defaultServiceOrderTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where description contains
        defaultServiceOrderTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where description does not contain
        defaultServiceOrderTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where price equals to
        defaultServiceOrderTypeFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where price in
        defaultServiceOrderTypeFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where price is not null
        defaultServiceOrderTypeFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where price is greater than or equal to
        defaultServiceOrderTypeFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where price is less than or equal to
        defaultServiceOrderTypeFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where price is less than
        defaultServiceOrderTypeFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where price is greater than
        defaultServiceOrderTypeFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where createdBy equals to
        defaultServiceOrderTypeFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where createdBy in
        defaultServiceOrderTypeFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where createdBy is not null
        defaultServiceOrderTypeFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where createdBy contains
        defaultServiceOrderTypeFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where createdBy does not contain
        defaultServiceOrderTypeFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where createdDate equals to
        defaultServiceOrderTypeFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where createdDate in
        defaultServiceOrderTypeFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where createdDate is not null
        defaultServiceOrderTypeFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where lastModifiedBy equals to
        defaultServiceOrderTypeFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where lastModifiedBy in
        defaultServiceOrderTypeFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where lastModifiedBy is not null
        defaultServiceOrderTypeFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where lastModifiedBy contains
        defaultServiceOrderTypeFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where lastModifiedBy does not contain
        defaultServiceOrderTypeFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where lastModifiedDate equals to
        defaultServiceOrderTypeFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where lastModifiedDate in
        defaultServiceOrderTypeFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllServiceOrderTypesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        // Get all the serviceOrderTypeList where lastModifiedDate is not null
        defaultServiceOrderTypeFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultServiceOrderTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultServiceOrderTypeShouldBeFound(shouldBeFound);
        defaultServiceOrderTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServiceOrderTypeShouldBeFound(String filter) throws Exception {
        restServiceOrderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOrderType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restServiceOrderTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServiceOrderTypeShouldNotBeFound(String filter) throws Exception {
        restServiceOrderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServiceOrderTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingServiceOrderType() throws Exception {
        // Get the serviceOrderType
        restServiceOrderTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceOrderType() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceOrderType
        ServiceOrderType updatedServiceOrderType = serviceOrderTypeRepository.findById(serviceOrderType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceOrderType are not directly saved in db
        em.detach(updatedServiceOrderType);
        updatedServiceOrderType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ServiceOrderTypeDTO serviceOrderTypeDTO = serviceOrderTypeMapper.toDto(updatedServiceOrderType);

        restServiceOrderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceOrderTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceOrderTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServiceOrderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServiceOrderTypeToMatchAllProperties(updatedServiceOrderType);
    }

    @Test
    @Transactional
    void putNonExistingServiceOrderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceOrderType.setId(longCount.incrementAndGet());

        // Create the ServiceOrderType
        ServiceOrderTypeDTO serviceOrderTypeDTO = serviceOrderTypeMapper.toDto(serviceOrderType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceOrderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceOrderTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceOrderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOrderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceOrderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceOrderType.setId(longCount.incrementAndGet());

        // Create the ServiceOrderType
        ServiceOrderTypeDTO serviceOrderTypeDTO = serviceOrderTypeMapper.toDto(serviceOrderType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceOrderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceOrderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOrderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceOrderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceOrderType.setId(longCount.incrementAndGet());

        // Create the ServiceOrderType
        ServiceOrderTypeDTO serviceOrderTypeDTO = serviceOrderTypeMapper.toDto(serviceOrderType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceOrderTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceOrderTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceOrderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceOrderTypeWithPatch() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceOrderType using partial update
        ServiceOrderType partialUpdatedServiceOrderType = new ServiceOrderType();
        partialUpdatedServiceOrderType.setId(serviceOrderType.getId());

        partialUpdatedServiceOrderType.description(UPDATED_DESCRIPTION).price(UPDATED_PRICE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restServiceOrderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceOrderType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceOrderType))
            )
            .andExpect(status().isOk());

        // Validate the ServiceOrderType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceOrderTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServiceOrderType, serviceOrderType),
            getPersistedServiceOrderType(serviceOrderType)
        );
    }

    @Test
    @Transactional
    void fullUpdateServiceOrderTypeWithPatch() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceOrderType using partial update
        ServiceOrderType partialUpdatedServiceOrderType = new ServiceOrderType();
        partialUpdatedServiceOrderType.setId(serviceOrderType.getId());

        partialUpdatedServiceOrderType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restServiceOrderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceOrderType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceOrderType))
            )
            .andExpect(status().isOk());

        // Validate the ServiceOrderType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceOrderTypeUpdatableFieldsEquals(
            partialUpdatedServiceOrderType,
            getPersistedServiceOrderType(partialUpdatedServiceOrderType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingServiceOrderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceOrderType.setId(longCount.incrementAndGet());

        // Create the ServiceOrderType
        ServiceOrderTypeDTO serviceOrderTypeDTO = serviceOrderTypeMapper.toDto(serviceOrderType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceOrderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceOrderTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceOrderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOrderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceOrderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceOrderType.setId(longCount.incrementAndGet());

        // Create the ServiceOrderType
        ServiceOrderTypeDTO serviceOrderTypeDTO = serviceOrderTypeMapper.toDto(serviceOrderType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceOrderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceOrderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOrderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceOrderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceOrderType.setId(longCount.incrementAndGet());

        // Create the ServiceOrderType
        ServiceOrderTypeDTO serviceOrderTypeDTO = serviceOrderTypeMapper.toDto(serviceOrderType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceOrderTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(serviceOrderTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceOrderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceOrderType() throws Exception {
        // Initialize the database
        insertedServiceOrderType = serviceOrderTypeRepository.saveAndFlush(serviceOrderType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the serviceOrderType
        restServiceOrderTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceOrderType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return serviceOrderTypeRepository.count();
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

    protected ServiceOrderType getPersistedServiceOrderType(ServiceOrderType serviceOrderType) {
        return serviceOrderTypeRepository.findById(serviceOrderType.getId()).orElseThrow();
    }

    protected void assertPersistedServiceOrderTypeToMatchAllProperties(ServiceOrderType expectedServiceOrderType) {
        assertServiceOrderTypeAllPropertiesEquals(expectedServiceOrderType, getPersistedServiceOrderType(expectedServiceOrderType));
    }

    protected void assertPersistedServiceOrderTypeToMatchUpdatableProperties(ServiceOrderType expectedServiceOrderType) {
        assertServiceOrderTypeAllUpdatablePropertiesEquals(
            expectedServiceOrderType,
            getPersistedServiceOrderType(expectedServiceOrderType)
        );
    }
}
