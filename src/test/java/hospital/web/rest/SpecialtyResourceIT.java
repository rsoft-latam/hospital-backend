package hospital.web.rest;

import hospital.HospitalApp;
import hospital.domain.Specialty;
import hospital.domain.Doctor;
import hospital.repository.SpecialtyRepository;
import hospital.service.SpecialtyService;
import hospital.service.dto.SpecialtyCriteria;
import hospital.service.SpecialtyQueryService;

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
 * Integration tests for the {@link SpecialtyResource} REST controller.
 */
@SpringBootTest(classes = HospitalApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SpecialtyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private SpecialtyService specialtyService;

    @Autowired
    private SpecialtyQueryService specialtyQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialtyMockMvc;

    private Specialty specialty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialty createEntity(EntityManager em) {
        Specialty specialty = new Specialty()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .icon(DEFAULT_ICON);
        return specialty;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialty createUpdatedEntity(EntityManager em) {
        Specialty specialty = new Specialty()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .icon(UPDATED_ICON);
        return specialty;
    }

    @BeforeEach
    public void initTest() {
        specialty = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpecialty() throws Exception {
        int databaseSizeBeforeCreate = specialtyRepository.findAll().size();
        // Create the Specialty
        restSpecialtyMockMvc.perform(post("/api/specialties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialty)))
            .andExpect(status().isCreated());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeCreate + 1);
        Specialty testSpecialty = specialtyList.get(specialtyList.size() - 1);
        assertThat(testSpecialty.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpecialty.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSpecialty.getIcon()).isEqualTo(DEFAULT_ICON);
    }

    @Test
    @Transactional
    public void createSpecialtyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = specialtyRepository.findAll().size();

        // Create the Specialty with an existing ID
        specialty.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialtyMockMvc.perform(post("/api/specialties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialty)))
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialtyRepository.findAll().size();
        // set the field null
        specialty.setName(null);

        // Create the Specialty, which fails.


        restSpecialtyMockMvc.perform(post("/api/specialties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialty)))
            .andExpect(status().isBadRequest());

        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSpecialties() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList
        restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)));
    }
    
    @Test
    @Transactional
    public void getSpecialty() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get the specialty
        restSpecialtyMockMvc.perform(get("/api/specialties/{id}", specialty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialty.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON));
    }


    @Test
    @Transactional
    public void getSpecialtiesByIdFiltering() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        Long id = specialty.getId();

        defaultSpecialtyShouldBeFound("id.equals=" + id);
        defaultSpecialtyShouldNotBeFound("id.notEquals=" + id);

        defaultSpecialtyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSpecialtyShouldNotBeFound("id.greaterThan=" + id);

        defaultSpecialtyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSpecialtyShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSpecialtiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where name equals to DEFAULT_NAME
        defaultSpecialtyShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the specialtyList where name equals to UPDATED_NAME
        defaultSpecialtyShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where name not equals to DEFAULT_NAME
        defaultSpecialtyShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the specialtyList where name not equals to UPDATED_NAME
        defaultSpecialtyShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSpecialtyShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the specialtyList where name equals to UPDATED_NAME
        defaultSpecialtyShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where name is not null
        defaultSpecialtyShouldBeFound("name.specified=true");

        // Get all the specialtyList where name is null
        defaultSpecialtyShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllSpecialtiesByNameContainsSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where name contains DEFAULT_NAME
        defaultSpecialtyShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the specialtyList where name contains UPDATED_NAME
        defaultSpecialtyShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where name does not contain DEFAULT_NAME
        defaultSpecialtyShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the specialtyList where name does not contain UPDATED_NAME
        defaultSpecialtyShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllSpecialtiesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where description equals to DEFAULT_DESCRIPTION
        defaultSpecialtyShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the specialtyList where description equals to UPDATED_DESCRIPTION
        defaultSpecialtyShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where description not equals to DEFAULT_DESCRIPTION
        defaultSpecialtyShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the specialtyList where description not equals to UPDATED_DESCRIPTION
        defaultSpecialtyShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSpecialtyShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the specialtyList where description equals to UPDATED_DESCRIPTION
        defaultSpecialtyShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where description is not null
        defaultSpecialtyShouldBeFound("description.specified=true");

        // Get all the specialtyList where description is null
        defaultSpecialtyShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllSpecialtiesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where description contains DEFAULT_DESCRIPTION
        defaultSpecialtyShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the specialtyList where description contains UPDATED_DESCRIPTION
        defaultSpecialtyShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where description does not contain DEFAULT_DESCRIPTION
        defaultSpecialtyShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the specialtyList where description does not contain UPDATED_DESCRIPTION
        defaultSpecialtyShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllSpecialtiesByIconIsEqualToSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where icon equals to DEFAULT_ICON
        defaultSpecialtyShouldBeFound("icon.equals=" + DEFAULT_ICON);

        // Get all the specialtyList where icon equals to UPDATED_ICON
        defaultSpecialtyShouldNotBeFound("icon.equals=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByIconIsNotEqualToSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where icon not equals to DEFAULT_ICON
        defaultSpecialtyShouldNotBeFound("icon.notEquals=" + DEFAULT_ICON);

        // Get all the specialtyList where icon not equals to UPDATED_ICON
        defaultSpecialtyShouldBeFound("icon.notEquals=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByIconIsInShouldWork() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where icon in DEFAULT_ICON or UPDATED_ICON
        defaultSpecialtyShouldBeFound("icon.in=" + DEFAULT_ICON + "," + UPDATED_ICON);

        // Get all the specialtyList where icon equals to UPDATED_ICON
        defaultSpecialtyShouldNotBeFound("icon.in=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByIconIsNullOrNotNull() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where icon is not null
        defaultSpecialtyShouldBeFound("icon.specified=true");

        // Get all the specialtyList where icon is null
        defaultSpecialtyShouldNotBeFound("icon.specified=false");
    }
                @Test
    @Transactional
    public void getAllSpecialtiesByIconContainsSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where icon contains DEFAULT_ICON
        defaultSpecialtyShouldBeFound("icon.contains=" + DEFAULT_ICON);

        // Get all the specialtyList where icon contains UPDATED_ICON
        defaultSpecialtyShouldNotBeFound("icon.contains=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByIconNotContainsSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where icon does not contain DEFAULT_ICON
        defaultSpecialtyShouldNotBeFound("icon.doesNotContain=" + DEFAULT_ICON);

        // Get all the specialtyList where icon does not contain UPDATED_ICON
        defaultSpecialtyShouldBeFound("icon.doesNotContain=" + UPDATED_ICON);
    }


    @Test
    @Transactional
    public void getAllSpecialtiesByDoctorIsEqualToSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);
        Doctor doctor = DoctorResourceIT.createEntity(em);
        em.persist(doctor);
        em.flush();
        specialty.setDoctor(doctor);
        specialtyRepository.saveAndFlush(specialty);
        Long doctorId = doctor.getId();

        // Get all the specialtyList where doctor equals to doctorId
        defaultSpecialtyShouldBeFound("doctorId.equals=" + doctorId);

        // Get all the specialtyList where doctor equals to doctorId + 1
        defaultSpecialtyShouldNotBeFound("doctorId.equals=" + (doctorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSpecialtyShouldBeFound(String filter) throws Exception {
        restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)));

        // Check, that the count call also returns 1
        restSpecialtyMockMvc.perform(get("/api/specialties/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSpecialtyShouldNotBeFound(String filter) throws Exception {
        restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSpecialtyMockMvc.perform(get("/api/specialties/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSpecialty() throws Exception {
        // Get the specialty
        restSpecialtyMockMvc.perform(get("/api/specialties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpecialty() throws Exception {
        // Initialize the database
        specialtyService.save(specialty);

        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();

        // Update the specialty
        Specialty updatedSpecialty = specialtyRepository.findById(specialty.getId()).get();
        // Disconnect from session so that the updates on updatedSpecialty are not directly saved in db
        em.detach(updatedSpecialty);
        updatedSpecialty
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .icon(UPDATED_ICON);

        restSpecialtyMockMvc.perform(put("/api/specialties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSpecialty)))
            .andExpect(status().isOk());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
        Specialty testSpecialty = specialtyList.get(specialtyList.size() - 1);
        assertThat(testSpecialty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpecialty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpecialty.getIcon()).isEqualTo(UPDATED_ICON);
    }

    @Test
    @Transactional
    public void updateNonExistingSpecialty() throws Exception {
        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc.perform(put("/api/specialties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialty)))
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSpecialty() throws Exception {
        // Initialize the database
        specialtyService.save(specialty);

        int databaseSizeBeforeDelete = specialtyRepository.findAll().size();

        // Delete the specialty
        restSpecialtyMockMvc.perform(delete("/api/specialties/{id}", specialty.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
