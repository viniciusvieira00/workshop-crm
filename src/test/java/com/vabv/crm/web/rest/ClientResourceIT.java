package com.vabv.crm.web.rest;

import static com.vabv.crm.domain.ClientAsserts.*;
import static com.vabv.crm.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vabv.crm.IntegrationTest;
import com.vabv.crm.domain.Client;
import com.vabv.crm.domain.enumeration.ClientType;
import com.vabv.crm.repository.ClientRepository;
import com.vabv.crm.service.dto.ClientDTO;
import com.vabv.crm.service.mapper.ClientMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientResourceIT {

    private static final String DEFAULT_DOCUMENT_NUMBER = "1077774134032";
    private static final String UPDATED_DOCUMENT_NUMBER = "8813950825489";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "JkKi_o@W2m1Y.3";
    private static final String UPDATED_EMAIL = ")'gCW@};.oS'~cR";

    private static final String DEFAULT_PHONE_NUMBER = "8599263656";
    private static final String UPDATED_PHONE_NUMBER = "7027472746";

    private static final String DEFAULT_ALTERNATIVE_PHONE_NUMBER = "54927234786";
    private static final String UPDATED_ALTERNATIVE_PHONE_NUMBER = "2984347269";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "52686034";
    private static final String UPDATED_ZIP_CODE = "51338792";

    private static final ClientType DEFAULT_CLIENT_TYPE = ClientType.INDIVIDUAL;
    private static final ClientType UPDATED_CLIENT_TYPE = ClientType.COMPANY;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientMockMvc;

    private Client client;

    private Client insertedClient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity() {
        return new Client()
            .documentNumber(DEFAULT_DOCUMENT_NUMBER)
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .alternativePhoneNumber(DEFAULT_ALTERNATIVE_PHONE_NUMBER)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zipCode(DEFAULT_ZIP_CODE)
            .clientType(DEFAULT_CLIENT_TYPE)
            .notes(DEFAULT_NOTES)
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
    public static Client createUpdatedEntity() {
        return new Client()
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .alternativePhoneNumber(UPDATED_ALTERNATIVE_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .clientType(UPDATED_CLIENT_TYPE)
            .notes(UPDATED_NOTES)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        client = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedClient != null) {
            clientRepository.delete(insertedClient);
            insertedClient = null;
        }
    }

    @Test
    @Transactional
    void createClient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);
        var returnedClientDTO = om.readValue(
            restClientMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientDTO.class
        );

        // Validate the Client in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClient = clientMapper.toEntity(returnedClientDTO);
        assertClientUpdatableFieldsEquals(returnedClient, getPersistedClient(returnedClient));

        insertedClient = returnedClient;
    }

    @Test
    @Transactional
    void createClientWithExistingId() throws Exception {
        // Create the Client with an existing ID
        client.setId(1L);
        ClientDTO clientDTO = clientMapper.toDto(client);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setDocumentNumber(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setName(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setEmail(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setPhoneNumber(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setAddress(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setCity(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setState(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkZipCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setZipCode(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClientTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setClientType(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClients() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentNumber").value(hasItem(DEFAULT_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].alternativePhoneNumber").value(hasItem(DEFAULT_ALTERNATIVE_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].clientType").value(hasItem(DEFAULT_CLIENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getClient() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc
            .perform(get(ENTITY_API_URL_ID, client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.documentNumber").value(DEFAULT_DOCUMENT_NUMBER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.alternativePhoneNumber").value(DEFAULT_ALTERNATIVE_PHONE_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.clientType").value(DEFAULT_CLIENT_TYPE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getClientsByIdFiltering() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        Long id = client.getId();

        defaultClientFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultClientFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultClientFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClientsByDocumentNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where documentNumber equals to
        defaultClientFiltering("documentNumber.equals=" + DEFAULT_DOCUMENT_NUMBER, "documentNumber.equals=" + UPDATED_DOCUMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllClientsByDocumentNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where documentNumber in
        defaultClientFiltering(
            "documentNumber.in=" + DEFAULT_DOCUMENT_NUMBER + "," + UPDATED_DOCUMENT_NUMBER,
            "documentNumber.in=" + UPDATED_DOCUMENT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByDocumentNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where documentNumber is not null
        defaultClientFiltering("documentNumber.specified=true", "documentNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByDocumentNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where documentNumber contains
        defaultClientFiltering("documentNumber.contains=" + DEFAULT_DOCUMENT_NUMBER, "documentNumber.contains=" + UPDATED_DOCUMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllClientsByDocumentNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where documentNumber does not contain
        defaultClientFiltering(
            "documentNumber.doesNotContain=" + UPDATED_DOCUMENT_NUMBER,
            "documentNumber.doesNotContain=" + DEFAULT_DOCUMENT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where name equals to
        defaultClientFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where name in
        defaultClientFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where name is not null
        defaultClientFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where name contains
        defaultClientFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where name does not contain
        defaultClientFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where email equals to
        defaultClientFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where email in
        defaultClientFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where email is not null
        defaultClientFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where email contains
        defaultClientFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where email does not contain
        defaultClientFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where phoneNumber equals to
        defaultClientFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllClientsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where phoneNumber in
        defaultClientFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where phoneNumber is not null
        defaultClientFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where phoneNumber contains
        defaultClientFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllClientsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where phoneNumber does not contain
        defaultClientFiltering("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER, "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllClientsByAlternativePhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where alternativePhoneNumber equals to
        defaultClientFiltering(
            "alternativePhoneNumber.equals=" + DEFAULT_ALTERNATIVE_PHONE_NUMBER,
            "alternativePhoneNumber.equals=" + UPDATED_ALTERNATIVE_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByAlternativePhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where alternativePhoneNumber in
        defaultClientFiltering(
            "alternativePhoneNumber.in=" + DEFAULT_ALTERNATIVE_PHONE_NUMBER + "," + UPDATED_ALTERNATIVE_PHONE_NUMBER,
            "alternativePhoneNumber.in=" + UPDATED_ALTERNATIVE_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByAlternativePhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where alternativePhoneNumber is not null
        defaultClientFiltering("alternativePhoneNumber.specified=true", "alternativePhoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByAlternativePhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where alternativePhoneNumber contains
        defaultClientFiltering(
            "alternativePhoneNumber.contains=" + DEFAULT_ALTERNATIVE_PHONE_NUMBER,
            "alternativePhoneNumber.contains=" + UPDATED_ALTERNATIVE_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByAlternativePhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where alternativePhoneNumber does not contain
        defaultClientFiltering(
            "alternativePhoneNumber.doesNotContain=" + UPDATED_ALTERNATIVE_PHONE_NUMBER,
            "alternativePhoneNumber.doesNotContain=" + DEFAULT_ALTERNATIVE_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where address equals to
        defaultClientFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClientsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where address in
        defaultClientFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClientsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where address is not null
        defaultClientFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where address contains
        defaultClientFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClientsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where address does not contain
        defaultClientFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClientsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where city equals to
        defaultClientFiltering("city.equals=" + DEFAULT_CITY, "city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllClientsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where city in
        defaultClientFiltering("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY, "city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllClientsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where city is not null
        defaultClientFiltering("city.specified=true", "city.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByCityContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where city contains
        defaultClientFiltering("city.contains=" + DEFAULT_CITY, "city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllClientsByCityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where city does not contain
        defaultClientFiltering("city.doesNotContain=" + UPDATED_CITY, "city.doesNotContain=" + DEFAULT_CITY);
    }

    @Test
    @Transactional
    void getAllClientsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where state equals to
        defaultClientFiltering("state.equals=" + DEFAULT_STATE, "state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllClientsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where state in
        defaultClientFiltering("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE, "state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllClientsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where state is not null
        defaultClientFiltering("state.specified=true", "state.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByStateContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where state contains
        defaultClientFiltering("state.contains=" + DEFAULT_STATE, "state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllClientsByStateNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where state does not contain
        defaultClientFiltering("state.doesNotContain=" + UPDATED_STATE, "state.doesNotContain=" + DEFAULT_STATE);
    }

    @Test
    @Transactional
    void getAllClientsByZipCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where zipCode equals to
        defaultClientFiltering("zipCode.equals=" + DEFAULT_ZIP_CODE, "zipCode.equals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllClientsByZipCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where zipCode in
        defaultClientFiltering("zipCode.in=" + DEFAULT_ZIP_CODE + "," + UPDATED_ZIP_CODE, "zipCode.in=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllClientsByZipCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where zipCode is not null
        defaultClientFiltering("zipCode.specified=true", "zipCode.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByZipCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where zipCode contains
        defaultClientFiltering("zipCode.contains=" + DEFAULT_ZIP_CODE, "zipCode.contains=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllClientsByZipCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where zipCode does not contain
        defaultClientFiltering("zipCode.doesNotContain=" + UPDATED_ZIP_CODE, "zipCode.doesNotContain=" + DEFAULT_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllClientsByClientTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where clientType equals to
        defaultClientFiltering("clientType.equals=" + DEFAULT_CLIENT_TYPE, "clientType.equals=" + UPDATED_CLIENT_TYPE);
    }

    @Test
    @Transactional
    void getAllClientsByClientTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where clientType in
        defaultClientFiltering("clientType.in=" + DEFAULT_CLIENT_TYPE + "," + UPDATED_CLIENT_TYPE, "clientType.in=" + UPDATED_CLIENT_TYPE);
    }

    @Test
    @Transactional
    void getAllClientsByClientTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where clientType is not null
        defaultClientFiltering("clientType.specified=true", "clientType.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where notes equals to
        defaultClientFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllClientsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where notes in
        defaultClientFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllClientsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where notes is not null
        defaultClientFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where notes contains
        defaultClientFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllClientsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where notes does not contain
        defaultClientFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllClientsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where createdBy equals to
        defaultClientFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllClientsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where createdBy in
        defaultClientFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllClientsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where createdBy is not null
        defaultClientFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where createdBy contains
        defaultClientFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllClientsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where createdBy does not contain
        defaultClientFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllClientsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where createdDate equals to
        defaultClientFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllClientsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where createdDate in
        defaultClientFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where createdDate is not null
        defaultClientFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where lastModifiedBy equals to
        defaultClientFiltering("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllClientsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where lastModifiedBy in
        defaultClientFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllClientsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where lastModifiedBy is not null
        defaultClientFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where lastModifiedBy contains
        defaultClientFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllClientsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where lastModifiedBy does not contain
        defaultClientFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllClientsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where lastModifiedDate equals to
        defaultClientFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where lastModifiedDate in
        defaultClientFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where lastModifiedDate is not null
        defaultClientFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultClientFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultClientShouldBeFound(shouldBeFound);
        defaultClientShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClientShouldBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentNumber").value(hasItem(DEFAULT_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].alternativePhoneNumber").value(hasItem(DEFAULT_ALTERNATIVE_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].clientType").value(hasItem(DEFAULT_CLIENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClientShouldNotBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClient() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClient are not directly saved in db
        em.detach(updatedClient);
        updatedClient
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .alternativePhoneNumber(UPDATED_ALTERNATIVE_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .clientType(UPDATED_CLIENT_TYPE)
            .notes(UPDATED_NOTES)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ClientDTO clientDTO = clientMapper.toDto(updatedClient);

        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClientToMatchAllProperties(updatedClient);
    }

    @Test
    @Transactional
    void putNonExistingClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientWithPatch() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .alternativePhoneNumber(UPDATED_ALTERNATIVE_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .clientType(UPDATED_CLIENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedClient, client), getPersistedClient(client));
    }

    @Test
    @Transactional
    void fullUpdateClientWithPatch() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .alternativePhoneNumber(UPDATED_ALTERNATIVE_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .clientType(UPDATED_CLIENT_TYPE)
            .notes(UPDATED_NOTES)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientUpdatableFieldsEquals(partialUpdatedClient, getPersistedClient(partialUpdatedClient));
    }

    @Test
    @Transactional
    void patchNonExistingClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClient() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the client
        restClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, client.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return clientRepository.count();
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

    protected Client getPersistedClient(Client client) {
        return clientRepository.findById(client.getId()).orElseThrow();
    }

    protected void assertPersistedClientToMatchAllProperties(Client expectedClient) {
        assertClientAllPropertiesEquals(expectedClient, getPersistedClient(expectedClient));
    }

    protected void assertPersistedClientToMatchUpdatableProperties(Client expectedClient) {
        assertClientAllUpdatablePropertiesEquals(expectedClient, getPersistedClient(expectedClient));
    }
}
