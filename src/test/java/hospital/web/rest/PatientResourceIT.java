package hospital.web.rest;

import hospital.HospitalApp;
import hospital.domain.Patient;
import hospital.domain.Hospital;
import hospital.repository.PatientRepository;
import hospital.service.PatientService;
import hospital.service.dto.PatientCriteria;
import hospital.service.PatientQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PatientResource} REST controller.
 */
@SpringBootTest(classes = HospitalApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PatientResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BIRTHDAY = "AAAAAAAAAA";
    private static final String UPDATED_BIRTHDAY = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_URL_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_URL_PHOTO = "BBBBBBBBBB";

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientQueryService patientQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientMockMvc;

    private Patient patient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createEntity(EntityManager em) {
        Patient patient = new Patient()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .birthday(DEFAULT_BIRTHDAY)
            .address(DEFAULT_ADDRESS)
            .urlPhoto(DEFAULT_URL_PHOTO);
        return patient;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createUpdatedEntity(EntityManager em) {
        Patient patient = new Patient()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthday(UPDATED_BIRTHDAY)
            .address(UPDATED_ADDRESS)
            .urlPhoto(UPDATED_URL_PHOTO);
        return patient;
    }

    @BeforeEach
    public void initTest() {
        patient = createEntity(em);
    }

    @Test
    @Transactional
    public void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();
        // Create the Patient
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPatient.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPatient.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testPatient.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPatient.getUrlPhoto()).isEqualTo(DEFAULT_URL_PHOTO);
    }

    @Test
    @Transactional
    public void createPatientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // Create the Patient with an existing ID
        patient.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setFirstName(null);

        // Create the Patient, which fails.


        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setLastName(null);

        // Create the Patient, which fails.


        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBirthdayIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setBirthday(null);

        // Create the Patient, which fails.


        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setAddress(null);

        // Create the Patient, which fails.


        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPatients() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].urlPhoto").value(hasItem(DEFAULT_URL_PHOTO)));
    }
    
    @Test
    @Transactional
    public void getPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.urlPhoto").value(DEFAULT_URL_PHOTO));
    }


    @Test
    @Transactional
    public void getPatientsByIdFiltering() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        Long id = patient.getId();

        defaultPatientShouldBeFound("id.equals=" + id);
        defaultPatientShouldNotBeFound("id.notEquals=" + id);

        defaultPatientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPatientShouldNotBeFound("id.greaterThan=" + id);

        defaultPatientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPatientShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPatientsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstName equals to DEFAULT_FIRST_NAME
        defaultPatientShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the patientList where firstName equals to UPDATED_FIRST_NAME
        defaultPatientShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstName not equals to DEFAULT_FIRST_NAME
        defaultPatientShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the patientList where firstName not equals to UPDATED_FIRST_NAME
        defaultPatientShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultPatientShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the patientList where firstName equals to UPDATED_FIRST_NAME
        defaultPatientShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstName is not null
        defaultPatientShouldBeFound("firstName.specified=true");

        // Get all the patientList where firstName is null
        defaultPatientShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstName contains DEFAULT_FIRST_NAME
        defaultPatientShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the patientList where firstName contains UPDATED_FIRST_NAME
        defaultPatientShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstName does not contain DEFAULT_FIRST_NAME
        defaultPatientShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the patientList where firstName does not contain UPDATED_FIRST_NAME
        defaultPatientShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllPatientsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where lastName equals to DEFAULT_LAST_NAME
        defaultPatientShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the patientList where lastName equals to UPDATED_LAST_NAME
        defaultPatientShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where lastName not equals to DEFAULT_LAST_NAME
        defaultPatientShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the patientList where lastName not equals to UPDATED_LAST_NAME
        defaultPatientShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultPatientShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the patientList where lastName equals to UPDATED_LAST_NAME
        defaultPatientShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where lastName is not null
        defaultPatientShouldBeFound("lastName.specified=true");

        // Get all the patientList where lastName is null
        defaultPatientShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where lastName contains DEFAULT_LAST_NAME
        defaultPatientShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the patientList where lastName contains UPDATED_LAST_NAME
        defaultPatientShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where lastName does not contain DEFAULT_LAST_NAME
        defaultPatientShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the patientList where lastName does not contain UPDATED_LAST_NAME
        defaultPatientShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllPatientsByBirthdayIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthday equals to DEFAULT_BIRTHDAY
        defaultPatientShouldBeFound("birthday.equals=" + DEFAULT_BIRTHDAY);

        // Get all the patientList where birthday equals to UPDATED_BIRTHDAY
        defaultPatientShouldNotBeFound("birthday.equals=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllPatientsByBirthdayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthday not equals to DEFAULT_BIRTHDAY
        defaultPatientShouldNotBeFound("birthday.notEquals=" + DEFAULT_BIRTHDAY);

        // Get all the patientList where birthday not equals to UPDATED_BIRTHDAY
        defaultPatientShouldBeFound("birthday.notEquals=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllPatientsByBirthdayIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthday in DEFAULT_BIRTHDAY or UPDATED_BIRTHDAY
        defaultPatientShouldBeFound("birthday.in=" + DEFAULT_BIRTHDAY + "," + UPDATED_BIRTHDAY);

        // Get all the patientList where birthday equals to UPDATED_BIRTHDAY
        defaultPatientShouldNotBeFound("birthday.in=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllPatientsByBirthdayIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthday is not null
        defaultPatientShouldBeFound("birthday.specified=true");

        // Get all the patientList where birthday is null
        defaultPatientShouldNotBeFound("birthday.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByBirthdayContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthday contains DEFAULT_BIRTHDAY
        defaultPatientShouldBeFound("birthday.contains=" + DEFAULT_BIRTHDAY);

        // Get all the patientList where birthday contains UPDATED_BIRTHDAY
        defaultPatientShouldNotBeFound("birthday.contains=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllPatientsByBirthdayNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthday does not contain DEFAULT_BIRTHDAY
        defaultPatientShouldNotBeFound("birthday.doesNotContain=" + DEFAULT_BIRTHDAY);

        // Get all the patientList where birthday does not contain UPDATED_BIRTHDAY
        defaultPatientShouldBeFound("birthday.doesNotContain=" + UPDATED_BIRTHDAY);
    }


    @Test
    @Transactional
    public void getAllPatientsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address equals to DEFAULT_ADDRESS
        defaultPatientShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the patientList where address equals to UPDATED_ADDRESS
        defaultPatientShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPatientsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address not equals to DEFAULT_ADDRESS
        defaultPatientShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the patientList where address not equals to UPDATED_ADDRESS
        defaultPatientShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPatientsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultPatientShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the patientList where address equals to UPDATED_ADDRESS
        defaultPatientShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPatientsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address is not null
        defaultPatientShouldBeFound("address.specified=true");

        // Get all the patientList where address is null
        defaultPatientShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByAddressContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address contains DEFAULT_ADDRESS
        defaultPatientShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the patientList where address contains UPDATED_ADDRESS
        defaultPatientShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPatientsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address does not contain DEFAULT_ADDRESS
        defaultPatientShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the patientList where address does not contain UPDATED_ADDRESS
        defaultPatientShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllPatientsByUrlPhotoIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where urlPhoto equals to DEFAULT_URL_PHOTO
        defaultPatientShouldBeFound("urlPhoto.equals=" + DEFAULT_URL_PHOTO);

        // Get all the patientList where urlPhoto equals to UPDATED_URL_PHOTO
        defaultPatientShouldNotBeFound("urlPhoto.equals=" + UPDATED_URL_PHOTO);
    }

    @Test
    @Transactional
    public void getAllPatientsByUrlPhotoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where urlPhoto not equals to DEFAULT_URL_PHOTO
        defaultPatientShouldNotBeFound("urlPhoto.notEquals=" + DEFAULT_URL_PHOTO);

        // Get all the patientList where urlPhoto not equals to UPDATED_URL_PHOTO
        defaultPatientShouldBeFound("urlPhoto.notEquals=" + UPDATED_URL_PHOTO);
    }

    @Test
    @Transactional
    public void getAllPatientsByUrlPhotoIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where urlPhoto in DEFAULT_URL_PHOTO or UPDATED_URL_PHOTO
        defaultPatientShouldBeFound("urlPhoto.in=" + DEFAULT_URL_PHOTO + "," + UPDATED_URL_PHOTO);

        // Get all the patientList where urlPhoto equals to UPDATED_URL_PHOTO
        defaultPatientShouldNotBeFound("urlPhoto.in=" + UPDATED_URL_PHOTO);
    }

    @Test
    @Transactional
    public void getAllPatientsByUrlPhotoIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where urlPhoto is not null
        defaultPatientShouldBeFound("urlPhoto.specified=true");

        // Get all the patientList where urlPhoto is null
        defaultPatientShouldNotBeFound("urlPhoto.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByUrlPhotoContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where urlPhoto contains DEFAULT_URL_PHOTO
        defaultPatientShouldBeFound("urlPhoto.contains=" + DEFAULT_URL_PHOTO);

        // Get all the patientList where urlPhoto contains UPDATED_URL_PHOTO
        defaultPatientShouldNotBeFound("urlPhoto.contains=" + UPDATED_URL_PHOTO);
    }

    @Test
    @Transactional
    public void getAllPatientsByUrlPhotoNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where urlPhoto does not contain DEFAULT_URL_PHOTO
        defaultPatientShouldNotBeFound("urlPhoto.doesNotContain=" + DEFAULT_URL_PHOTO);

        // Get all the patientList where urlPhoto does not contain UPDATED_URL_PHOTO
        defaultPatientShouldBeFound("urlPhoto.doesNotContain=" + UPDATED_URL_PHOTO);
    }


    @Test
    @Transactional
    public void getAllPatientsByHospitalIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        Hospital hospital = HospitalResourceIT.createEntity(em);
        em.persist(hospital);
        em.flush();
        patient.setHospital(hospital);
        patientRepository.saveAndFlush(patient);
        Long hospitalId = hospital.getId();

        // Get all the patientList where hospital equals to hospitalId
        defaultPatientShouldBeFound("hospitalId.equals=" + hospitalId);

        // Get all the patientList where hospital equals to hospitalId + 1
        defaultPatientShouldNotBeFound("hospitalId.equals=" + (hospitalId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPatientShouldBeFound(String filter) throws Exception {
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].urlPhoto").value(hasItem(DEFAULT_URL_PHOTO)));

        // Check, that the count call also returns 1
        restPatientMockMvc.perform(get("/api/patients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPatientShouldNotBeFound(String filter) throws Exception {
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPatientMockMvc.perform(get("/api/patients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePatient() throws Exception {
        // Initialize the database
        patientService.save(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).get();
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);
        updatedPatient
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthday(UPDATED_BIRTHDAY)
            .address(UPDATED_ADDRESS)
            .urlPhoto(UPDATED_URL_PHOTO);

        restPatientMockMvc.perform(put("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPatient)))
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPatient.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPatient.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testPatient.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPatient.getUrlPhoto()).isEqualTo(UPDATED_URL_PHOTO);
    }

    @Test
    @Transactional
    public void updateNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc.perform(put("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePatient() throws Exception {
        // Initialize the database
        patientService.save(patient);

        int databaseSizeBeforeDelete = patientRepository.findAll().size();

        // Delete the patient
        restPatientMockMvc.perform(delete("/api/patients/{id}", patient.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
