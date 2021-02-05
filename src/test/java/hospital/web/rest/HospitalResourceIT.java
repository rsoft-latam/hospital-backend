package hospital.web.rest;

import hospital.HospitalApp;
import hospital.domain.Hospital;
import hospital.repository.HospitalRepository;
import hospital.service.HospitalService;
import hospital.service.dto.HospitalCriteria;
import hospital.service.HospitalQueryService;

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
 * Integration tests for the {@link HospitalResource} REST controller.
 */
@SpringBootTest(classes = HospitalApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class HospitalResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CREATION_DATE = "AAAAAAAAAA";
    private static final String UPDATED_CREATION_DATE = "BBBBBBBBBB";

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalQueryService hospitalQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHospitalMockMvc;

    private Hospital hospital;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hospital createEntity(EntityManager em) {
        Hospital hospital = new Hospital()
            .name(DEFAULT_NAME)
            .creationDate(DEFAULT_CREATION_DATE);
        return hospital;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hospital createUpdatedEntity(EntityManager em) {
        Hospital hospital = new Hospital()
            .name(UPDATED_NAME)
            .creationDate(UPDATED_CREATION_DATE);
        return hospital;
    }

    @BeforeEach
    public void initTest() {
        hospital = createEntity(em);
    }

    @Test
    @Transactional
    public void createHospital() throws Exception {
        int databaseSizeBeforeCreate = hospitalRepository.findAll().size();
        // Create the Hospital
        restHospitalMockMvc.perform(post("/api/hospitals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hospital)))
            .andExpect(status().isCreated());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeCreate + 1);
        Hospital testHospital = hospitalList.get(hospitalList.size() - 1);
        assertThat(testHospital.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHospital.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void createHospitalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hospitalRepository.findAll().size();

        // Create the Hospital with an existing ID
        hospital.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHospitalMockMvc.perform(post("/api/hospitals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hospital)))
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hospitalRepository.findAll().size();
        // set the field null
        hospital.setName(null);

        // Create the Hospital, which fails.


        restHospitalMockMvc.perform(post("/api/hospitals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hospital)))
            .andExpect(status().isBadRequest());

        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHospitals() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList
        restHospitalMockMvc.perform(get("/api/hospitals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hospital.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE)));
    }
    
    @Test
    @Transactional
    public void getHospital() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get the hospital
        restHospitalMockMvc.perform(get("/api/hospitals/{id}", hospital.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hospital.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE));
    }


    @Test
    @Transactional
    public void getHospitalsByIdFiltering() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        Long id = hospital.getId();

        defaultHospitalShouldBeFound("id.equals=" + id);
        defaultHospitalShouldNotBeFound("id.notEquals=" + id);

        defaultHospitalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHospitalShouldNotBeFound("id.greaterThan=" + id);

        defaultHospitalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHospitalShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllHospitalsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name equals to DEFAULT_NAME
        defaultHospitalShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the hospitalList where name equals to UPDATED_NAME
        defaultHospitalShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHospitalsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name not equals to DEFAULT_NAME
        defaultHospitalShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the hospitalList where name not equals to UPDATED_NAME
        defaultHospitalShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHospitalsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name in DEFAULT_NAME or UPDATED_NAME
        defaultHospitalShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the hospitalList where name equals to UPDATED_NAME
        defaultHospitalShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHospitalsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name is not null
        defaultHospitalShouldBeFound("name.specified=true");

        // Get all the hospitalList where name is null
        defaultHospitalShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllHospitalsByNameContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name contains DEFAULT_NAME
        defaultHospitalShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the hospitalList where name contains UPDATED_NAME
        defaultHospitalShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHospitalsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name does not contain DEFAULT_NAME
        defaultHospitalShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the hospitalList where name does not contain UPDATED_NAME
        defaultHospitalShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllHospitalsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where creationDate equals to DEFAULT_CREATION_DATE
        defaultHospitalShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the hospitalList where creationDate equals to UPDATED_CREATION_DATE
        defaultHospitalShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllHospitalsByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultHospitalShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the hospitalList where creationDate not equals to UPDATED_CREATION_DATE
        defaultHospitalShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllHospitalsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultHospitalShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the hospitalList where creationDate equals to UPDATED_CREATION_DATE
        defaultHospitalShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllHospitalsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where creationDate is not null
        defaultHospitalShouldBeFound("creationDate.specified=true");

        // Get all the hospitalList where creationDate is null
        defaultHospitalShouldNotBeFound("creationDate.specified=false");
    }
                @Test
    @Transactional
    public void getAllHospitalsByCreationDateContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where creationDate contains DEFAULT_CREATION_DATE
        defaultHospitalShouldBeFound("creationDate.contains=" + DEFAULT_CREATION_DATE);

        // Get all the hospitalList where creationDate contains UPDATED_CREATION_DATE
        defaultHospitalShouldNotBeFound("creationDate.contains=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllHospitalsByCreationDateNotContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where creationDate does not contain DEFAULT_CREATION_DATE
        defaultHospitalShouldNotBeFound("creationDate.doesNotContain=" + DEFAULT_CREATION_DATE);

        // Get all the hospitalList where creationDate does not contain UPDATED_CREATION_DATE
        defaultHospitalShouldBeFound("creationDate.doesNotContain=" + UPDATED_CREATION_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHospitalShouldBeFound(String filter) throws Exception {
        restHospitalMockMvc.perform(get("/api/hospitals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hospital.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE)));

        // Check, that the count call also returns 1
        restHospitalMockMvc.perform(get("/api/hospitals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHospitalShouldNotBeFound(String filter) throws Exception {
        restHospitalMockMvc.perform(get("/api/hospitals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHospitalMockMvc.perform(get("/api/hospitals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingHospital() throws Exception {
        // Get the hospital
        restHospitalMockMvc.perform(get("/api/hospitals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHospital() throws Exception {
        // Initialize the database
        hospitalService.save(hospital);

        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();

        // Update the hospital
        Hospital updatedHospital = hospitalRepository.findById(hospital.getId()).get();
        // Disconnect from session so that the updates on updatedHospital are not directly saved in db
        em.detach(updatedHospital);
        updatedHospital
            .name(UPDATED_NAME)
            .creationDate(UPDATED_CREATION_DATE);

        restHospitalMockMvc.perform(put("/api/hospitals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedHospital)))
            .andExpect(status().isOk());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
        Hospital testHospital = hospitalList.get(hospitalList.size() - 1);
        assertThat(testHospital.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHospital.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHospitalMockMvc.perform(put("/api/hospitals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hospital)))
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHospital() throws Exception {
        // Initialize the database
        hospitalService.save(hospital);

        int databaseSizeBeforeDelete = hospitalRepository.findAll().size();

        // Delete the hospital
        restHospitalMockMvc.perform(delete("/api/hospitals/{id}", hospital.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
