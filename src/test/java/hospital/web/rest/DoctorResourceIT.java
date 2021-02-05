package hospital.web.rest;

import hospital.HospitalApp;
import hospital.domain.Doctor;
import hospital.domain.Hospital;
import hospital.repository.DoctorRepository;
import hospital.service.DoctorService;
import hospital.service.dto.DoctorCriteria;
import hospital.service.DoctorQueryService;

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
 * Integration tests for the {@link DoctorResource} REST controller.
 */
@SpringBootTest(classes = HospitalApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class DoctorResourceIT {

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
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DoctorQueryService doctorQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDoctorMockMvc;

    private Doctor doctor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctor createEntity(EntityManager em) {
        Doctor doctor = new Doctor()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .birthday(DEFAULT_BIRTHDAY)
            .address(DEFAULT_ADDRESS)
            .urlPhoto(DEFAULT_URL_PHOTO);
        return doctor;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctor createUpdatedEntity(EntityManager em) {
        Doctor doctor = new Doctor()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthday(UPDATED_BIRTHDAY)
            .address(UPDATED_ADDRESS)
            .urlPhoto(UPDATED_URL_PHOTO);
        return doctor;
    }

    @BeforeEach
    public void initTest() {
        doctor = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoctor() throws Exception {
        int databaseSizeBeforeCreate = doctorRepository.findAll().size();
        // Create the Doctor
        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(doctor)))
            .andExpect(status().isCreated());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeCreate + 1);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testDoctor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testDoctor.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testDoctor.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testDoctor.getUrlPhoto()).isEqualTo(DEFAULT_URL_PHOTO);
    }

    @Test
    @Transactional
    public void createDoctorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doctorRepository.findAll().size();

        // Create the Doctor with an existing ID
        doctor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(doctor)))
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorRepository.findAll().size();
        // set the field null
        doctor.setFirstName(null);

        // Create the Doctor, which fails.


        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(doctor)))
            .andExpect(status().isBadRequest());

        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorRepository.findAll().size();
        // set the field null
        doctor.setLastName(null);

        // Create the Doctor, which fails.


        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(doctor)))
            .andExpect(status().isBadRequest());

        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorRepository.findAll().size();
        // set the field null
        doctor.setAddress(null);

        // Create the Doctor, which fails.


        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(doctor)))
            .andExpect(status().isBadRequest());

        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDoctors() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList
        restDoctorMockMvc.perform(get("/api/doctors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].urlPhoto").value(hasItem(DEFAULT_URL_PHOTO)));
    }
    
    @Test
    @Transactional
    public void getDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get the doctor
        restDoctorMockMvc.perform(get("/api/doctors/{id}", doctor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(doctor.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.urlPhoto").value(DEFAULT_URL_PHOTO));
    }


    @Test
    @Transactional
    public void getDoctorsByIdFiltering() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        Long id = doctor.getId();

        defaultDoctorShouldBeFound("id.equals=" + id);
        defaultDoctorShouldNotBeFound("id.notEquals=" + id);

        defaultDoctorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDoctorShouldNotBeFound("id.greaterThan=" + id);

        defaultDoctorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDoctorShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDoctorsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName equals to DEFAULT_FIRST_NAME
        defaultDoctorShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the doctorList where firstName equals to UPDATED_FIRST_NAME
        defaultDoctorShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName not equals to DEFAULT_FIRST_NAME
        defaultDoctorShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the doctorList where firstName not equals to UPDATED_FIRST_NAME
        defaultDoctorShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultDoctorShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the doctorList where firstName equals to UPDATED_FIRST_NAME
        defaultDoctorShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName is not null
        defaultDoctorShouldBeFound("firstName.specified=true");

        // Get all the doctorList where firstName is null
        defaultDoctorShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllDoctorsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName contains DEFAULT_FIRST_NAME
        defaultDoctorShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the doctorList where firstName contains UPDATED_FIRST_NAME
        defaultDoctorShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName does not contain DEFAULT_FIRST_NAME
        defaultDoctorShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the doctorList where firstName does not contain UPDATED_FIRST_NAME
        defaultDoctorShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllDoctorsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName equals to DEFAULT_LAST_NAME
        defaultDoctorShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the doctorList where lastName equals to UPDATED_LAST_NAME
        defaultDoctorShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName not equals to DEFAULT_LAST_NAME
        defaultDoctorShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the doctorList where lastName not equals to UPDATED_LAST_NAME
        defaultDoctorShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultDoctorShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the doctorList where lastName equals to UPDATED_LAST_NAME
        defaultDoctorShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName is not null
        defaultDoctorShouldBeFound("lastName.specified=true");

        // Get all the doctorList where lastName is null
        defaultDoctorShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllDoctorsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName contains DEFAULT_LAST_NAME
        defaultDoctorShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the doctorList where lastName contains UPDATED_LAST_NAME
        defaultDoctorShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName does not contain DEFAULT_LAST_NAME
        defaultDoctorShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the doctorList where lastName does not contain UPDATED_LAST_NAME
        defaultDoctorShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllDoctorsByBirthdayIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where birthday equals to DEFAULT_BIRTHDAY
        defaultDoctorShouldBeFound("birthday.equals=" + DEFAULT_BIRTHDAY);

        // Get all the doctorList where birthday equals to UPDATED_BIRTHDAY
        defaultDoctorShouldNotBeFound("birthday.equals=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllDoctorsByBirthdayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where birthday not equals to DEFAULT_BIRTHDAY
        defaultDoctorShouldNotBeFound("birthday.notEquals=" + DEFAULT_BIRTHDAY);

        // Get all the doctorList where birthday not equals to UPDATED_BIRTHDAY
        defaultDoctorShouldBeFound("birthday.notEquals=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllDoctorsByBirthdayIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where birthday in DEFAULT_BIRTHDAY or UPDATED_BIRTHDAY
        defaultDoctorShouldBeFound("birthday.in=" + DEFAULT_BIRTHDAY + "," + UPDATED_BIRTHDAY);

        // Get all the doctorList where birthday equals to UPDATED_BIRTHDAY
        defaultDoctorShouldNotBeFound("birthday.in=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllDoctorsByBirthdayIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where birthday is not null
        defaultDoctorShouldBeFound("birthday.specified=true");

        // Get all the doctorList where birthday is null
        defaultDoctorShouldNotBeFound("birthday.specified=false");
    }
                @Test
    @Transactional
    public void getAllDoctorsByBirthdayContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where birthday contains DEFAULT_BIRTHDAY
        defaultDoctorShouldBeFound("birthday.contains=" + DEFAULT_BIRTHDAY);

        // Get all the doctorList where birthday contains UPDATED_BIRTHDAY
        defaultDoctorShouldNotBeFound("birthday.contains=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllDoctorsByBirthdayNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where birthday does not contain DEFAULT_BIRTHDAY
        defaultDoctorShouldNotBeFound("birthday.doesNotContain=" + DEFAULT_BIRTHDAY);

        // Get all the doctorList where birthday does not contain UPDATED_BIRTHDAY
        defaultDoctorShouldBeFound("birthday.doesNotContain=" + UPDATED_BIRTHDAY);
    }


    @Test
    @Transactional
    public void getAllDoctorsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where address equals to DEFAULT_ADDRESS
        defaultDoctorShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the doctorList where address equals to UPDATED_ADDRESS
        defaultDoctorShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDoctorsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where address not equals to DEFAULT_ADDRESS
        defaultDoctorShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the doctorList where address not equals to UPDATED_ADDRESS
        defaultDoctorShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDoctorsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultDoctorShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the doctorList where address equals to UPDATED_ADDRESS
        defaultDoctorShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDoctorsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where address is not null
        defaultDoctorShouldBeFound("address.specified=true");

        // Get all the doctorList where address is null
        defaultDoctorShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllDoctorsByAddressContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where address contains DEFAULT_ADDRESS
        defaultDoctorShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the doctorList where address contains UPDATED_ADDRESS
        defaultDoctorShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDoctorsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where address does not contain DEFAULT_ADDRESS
        defaultDoctorShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the doctorList where address does not contain UPDATED_ADDRESS
        defaultDoctorShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllDoctorsByUrlPhotoIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where urlPhoto equals to DEFAULT_URL_PHOTO
        defaultDoctorShouldBeFound("urlPhoto.equals=" + DEFAULT_URL_PHOTO);

        // Get all the doctorList where urlPhoto equals to UPDATED_URL_PHOTO
        defaultDoctorShouldNotBeFound("urlPhoto.equals=" + UPDATED_URL_PHOTO);
    }

    @Test
    @Transactional
    public void getAllDoctorsByUrlPhotoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where urlPhoto not equals to DEFAULT_URL_PHOTO
        defaultDoctorShouldNotBeFound("urlPhoto.notEquals=" + DEFAULT_URL_PHOTO);

        // Get all the doctorList where urlPhoto not equals to UPDATED_URL_PHOTO
        defaultDoctorShouldBeFound("urlPhoto.notEquals=" + UPDATED_URL_PHOTO);
    }

    @Test
    @Transactional
    public void getAllDoctorsByUrlPhotoIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where urlPhoto in DEFAULT_URL_PHOTO or UPDATED_URL_PHOTO
        defaultDoctorShouldBeFound("urlPhoto.in=" + DEFAULT_URL_PHOTO + "," + UPDATED_URL_PHOTO);

        // Get all the doctorList where urlPhoto equals to UPDATED_URL_PHOTO
        defaultDoctorShouldNotBeFound("urlPhoto.in=" + UPDATED_URL_PHOTO);
    }

    @Test
    @Transactional
    public void getAllDoctorsByUrlPhotoIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where urlPhoto is not null
        defaultDoctorShouldBeFound("urlPhoto.specified=true");

        // Get all the doctorList where urlPhoto is null
        defaultDoctorShouldNotBeFound("urlPhoto.specified=false");
    }
                @Test
    @Transactional
    public void getAllDoctorsByUrlPhotoContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where urlPhoto contains DEFAULT_URL_PHOTO
        defaultDoctorShouldBeFound("urlPhoto.contains=" + DEFAULT_URL_PHOTO);

        // Get all the doctorList where urlPhoto contains UPDATED_URL_PHOTO
        defaultDoctorShouldNotBeFound("urlPhoto.contains=" + UPDATED_URL_PHOTO);
    }

    @Test
    @Transactional
    public void getAllDoctorsByUrlPhotoNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where urlPhoto does not contain DEFAULT_URL_PHOTO
        defaultDoctorShouldNotBeFound("urlPhoto.doesNotContain=" + DEFAULT_URL_PHOTO);

        // Get all the doctorList where urlPhoto does not contain UPDATED_URL_PHOTO
        defaultDoctorShouldBeFound("urlPhoto.doesNotContain=" + UPDATED_URL_PHOTO);
    }


    @Test
    @Transactional
    public void getAllDoctorsByHospitalIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);
        Hospital hospital = HospitalResourceIT.createEntity(em);
        em.persist(hospital);
        em.flush();
        doctor.setHospital(hospital);
        doctorRepository.saveAndFlush(doctor);
        Long hospitalId = hospital.getId();

        // Get all the doctorList where hospital equals to hospitalId
        defaultDoctorShouldBeFound("hospitalId.equals=" + hospitalId);

        // Get all the doctorList where hospital equals to hospitalId + 1
        defaultDoctorShouldNotBeFound("hospitalId.equals=" + (hospitalId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDoctorShouldBeFound(String filter) throws Exception {
        restDoctorMockMvc.perform(get("/api/doctors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].urlPhoto").value(hasItem(DEFAULT_URL_PHOTO)));

        // Check, that the count call also returns 1
        restDoctorMockMvc.perform(get("/api/doctors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDoctorShouldNotBeFound(String filter) throws Exception {
        restDoctorMockMvc.perform(get("/api/doctors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDoctorMockMvc.perform(get("/api/doctors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingDoctor() throws Exception {
        // Get the doctor
        restDoctorMockMvc.perform(get("/api/doctors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctor() throws Exception {
        // Initialize the database
        doctorService.save(doctor);

        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Update the doctor
        Doctor updatedDoctor = doctorRepository.findById(doctor.getId()).get();
        // Disconnect from session so that the updates on updatedDoctor are not directly saved in db
        em.detach(updatedDoctor);
        updatedDoctor
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthday(UPDATED_BIRTHDAY)
            .address(UPDATED_ADDRESS)
            .urlPhoto(UPDATED_URL_PHOTO);

        restDoctorMockMvc.perform(put("/api/doctors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDoctor)))
            .andExpect(status().isOk());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testDoctor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testDoctor.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testDoctor.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testDoctor.getUrlPhoto()).isEqualTo(UPDATED_URL_PHOTO);
    }

    @Test
    @Transactional
    public void updateNonExistingDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorMockMvc.perform(put("/api/doctors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(doctor)))
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDoctor() throws Exception {
        // Initialize the database
        doctorService.save(doctor);

        int databaseSizeBeforeDelete = doctorRepository.findAll().size();

        // Delete the doctor
        restDoctorMockMvc.perform(delete("/api/doctors/{id}", doctor.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
