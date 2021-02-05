package hospital.web.rest;

import hospital.HospitalApp;
import hospital.domain.Note;
import hospital.repository.NoteRepository;
import hospital.service.NoteService;
import hospital.service.dto.NoteCriteria;
import hospital.service.NoteQueryService;

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
 * Integration tests for the {@link NoteResource} REST controller.
 */
@SpringBootTest(classes = HospitalApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class NoteResourceIT {

    private static final Long DEFAULT_ID_DOCTOR = 1L;
    private static final Long UPDATED_ID_DOCTOR = 2L;
    private static final Long SMALLER_ID_DOCTOR = 1L - 1L;

    private static final Long DEFAULT_ID_PATIENT = 1L;
    private static final Long UPDATED_ID_PATIENT = 2L;
    private static final Long SMALLER_ID_PATIENT = 1L - 1L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DATE = "BBBBBBBBBB";

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteQueryService noteQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNoteMockMvc;

    private Note note;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Note createEntity(EntityManager em) {
        Note note = new Note()
            .idDoctor(DEFAULT_ID_DOCTOR)
            .idPatient(DEFAULT_ID_PATIENT)
            .description(DEFAULT_DESCRIPTION)
            .date(DEFAULT_DATE);
        return note;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Note createUpdatedEntity(EntityManager em) {
        Note note = new Note()
            .idDoctor(UPDATED_ID_DOCTOR)
            .idPatient(UPDATED_ID_PATIENT)
            .description(UPDATED_DESCRIPTION)
            .date(UPDATED_DATE);
        return note;
    }

    @BeforeEach
    public void initTest() {
        note = createEntity(em);
    }

    @Test
    @Transactional
    public void createNote() throws Exception {
        int databaseSizeBeforeCreate = noteRepository.findAll().size();
        // Create the Note
        restNoteMockMvc.perform(post("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(note)))
            .andExpect(status().isCreated());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeCreate + 1);
        Note testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getIdDoctor()).isEqualTo(DEFAULT_ID_DOCTOR);
        assertThat(testNote.getIdPatient()).isEqualTo(DEFAULT_ID_PATIENT);
        assertThat(testNote.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testNote.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createNoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = noteRepository.findAll().size();

        // Create the Note with an existing ID
        note.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNoteMockMvc.perform(post("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(note)))
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkIdDoctorIsRequired() throws Exception {
        int databaseSizeBeforeTest = noteRepository.findAll().size();
        // set the field null
        note.setIdDoctor(null);

        // Create the Note, which fails.


        restNoteMockMvc.perform(post("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(note)))
            .andExpect(status().isBadRequest());

        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdPatientIsRequired() throws Exception {
        int databaseSizeBeforeTest = noteRepository.findAll().size();
        // set the field null
        note.setIdPatient(null);

        // Create the Note, which fails.


        restNoteMockMvc.perform(post("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(note)))
            .andExpect(status().isBadRequest());

        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = noteRepository.findAll().size();
        // set the field null
        note.setDescription(null);

        // Create the Note, which fails.


        restNoteMockMvc.perform(post("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(note)))
            .andExpect(status().isBadRequest());

        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = noteRepository.findAll().size();
        // set the field null
        note.setDate(null);

        // Create the Note, which fails.


        restNoteMockMvc.perform(post("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(note)))
            .andExpect(status().isBadRequest());

        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotes() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList
        restNoteMockMvc.perform(get("/api/notes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(note.getId().intValue())))
            .andExpect(jsonPath("$.[*].idDoctor").value(hasItem(DEFAULT_ID_DOCTOR.intValue())))
            .andExpect(jsonPath("$.[*].idPatient").value(hasItem(DEFAULT_ID_PATIENT.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)));
    }
    
    @Test
    @Transactional
    public void getNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get the note
        restNoteMockMvc.perform(get("/api/notes/{id}", note.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(note.getId().intValue()))
            .andExpect(jsonPath("$.idDoctor").value(DEFAULT_ID_DOCTOR.intValue()))
            .andExpect(jsonPath("$.idPatient").value(DEFAULT_ID_PATIENT.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE));
    }


    @Test
    @Transactional
    public void getNotesByIdFiltering() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        Long id = note.getId();

        defaultNoteShouldBeFound("id.equals=" + id);
        defaultNoteShouldNotBeFound("id.notEquals=" + id);

        defaultNoteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNoteShouldNotBeFound("id.greaterThan=" + id);

        defaultNoteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNoteShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllNotesByIdDoctorIsEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where idDoctor equals to DEFAULT_ID_DOCTOR
        defaultNoteShouldBeFound("idDoctor.equals=" + DEFAULT_ID_DOCTOR);

        // Get all the noteList where idDoctor equals to UPDATED_ID_DOCTOR
        defaultNoteShouldNotBeFound("idDoctor.equals=" + UPDATED_ID_DOCTOR);
    }

    @Test
    @Transactional
    public void getAllNotesByIdDoctorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where idDoctor not equals to DEFAULT_ID_DOCTOR
        defaultNoteShouldNotBeFound("idDoctor.notEquals=" + DEFAULT_ID_DOCTOR);

        // Get all the noteList where idDoctor not equals to UPDATED_ID_DOCTOR
        defaultNoteShouldBeFound("idDoctor.notEquals=" + UPDATED_ID_DOCTOR);
    }

    @Test
    @Transactional
    public void getAllNotesByIdDoctorIsInShouldWork() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where idDoctor in DEFAULT_ID_DOCTOR or UPDATED_ID_DOCTOR
        defaultNoteShouldBeFound("idDoctor.in=" + DEFAULT_ID_DOCTOR + "," + UPDATED_ID_DOCTOR);

        // Get all the noteList where idDoctor equals to UPDATED_ID_DOCTOR
        defaultNoteShouldNotBeFound("idDoctor.in=" + UPDATED_ID_DOCTOR);
    }

    @Test
    @Transactional
    public void getAllNotesByIdDoctorIsNullOrNotNull() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where idDoctor is not null
        defaultNoteShouldBeFound("idDoctor.specified=true");

        // Get all the noteList where idDoctor is null
        defaultNoteShouldNotBeFound("idDoctor.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotesByIdDoctorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where idDoctor is greater than or equal to DEFAULT_ID_DOCTOR
        defaultNoteShouldBeFound("idDoctor.greaterThanOrEqual=" + DEFAULT_ID_DOCTOR);

        // Get all the noteList where idDoctor is greater than or equal to UPDATED_ID_DOCTOR
        defaultNoteShouldNotBeFound("idDoctor.greaterThanOrEqual=" + UPDATED_ID_DOCTOR);
    }

    @Test
    @Transactional
    public void getAllNotesByIdDoctorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where idDoctor is less than or equal to DEFAULT_ID_DOCTOR
        defaultNoteShouldBeFound("idDoctor.lessThanOrEqual=" + DEFAULT_ID_DOCTOR);

        // Get all the noteList where idDoctor is less than or equal to SMALLER_ID_DOCTOR
        defaultNoteShouldNotBeFound("idDoctor.lessThanOrEqual=" + SMALLER_ID_DOCTOR);
    }

    @Test
    @Transactional
    public void getAllNotesByIdDoctorIsLessThanSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where idDoctor is less than DEFAULT_ID_DOCTOR
        defaultNoteShouldNotBeFound("idDoctor.lessThan=" + DEFAULT_ID_DOCTOR);

        // Get all the noteList where idDoctor is less than UPDATED_ID_DOCTOR
        defaultNoteShouldBeFound("idDoctor.lessThan=" + UPDATED_ID_DOCTOR);
    }

    @Test
    @Transactional
    public void getAllNotesByIdDoctorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where idDoctor is greater than DEFAULT_ID_DOCTOR
        defaultNoteShouldNotBeFound("idDoctor.greaterThan=" + DEFAULT_ID_DOCTOR);

        // Get all the noteList where idDoctor is greater than SMALLER_ID_DOCTOR
        defaultNoteShouldBeFound("idDoctor.greaterThan=" + SMALLER_ID_DOCTOR);
    }


    @Test
    @Transactional
    public void getAllNotesByIdPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where idPatient equals to DEFAULT_ID_PATIENT
        defaultNoteShouldBeFound("idPatient.equals=" + DEFAULT_ID_PATIENT);

        // Get all the noteList where idPatient equals to UPDATED_ID_PATIENT
        defaultNoteShouldNotBeFound("idPatient.equals=" + UPDATED_ID_PATIENT);
    }

    @Test
    @Transactional
    public void getAllNotesByIdPatientIsNotEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where idPatient not equals to DEFAULT_ID_PATIENT
        defaultNoteShouldNotBeFound("idPatient.notEquals=" + DEFAULT_ID_PATIENT);

        // Get all the noteList where idPatient not equals to UPDATED_ID_PATIENT
        defaultNoteShouldBeFound("idPatient.notEquals=" + UPDATED_ID_PATIENT);
    }

    @Test
    @Transactional
    public void getAllNotesByIdPatientIsInShouldWork() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where idPatient in DEFAULT_ID_PATIENT or UPDATED_ID_PATIENT
        defaultNoteShouldBeFound("idPatient.in=" + DEFAULT_ID_PATIENT + "," + UPDATED_ID_PATIENT);

        // Get all the noteList where idPatient equals to UPDATED_ID_PATIENT
        defaultNoteShouldNotBeFound("idPatient.in=" + UPDATED_ID_PATIENT);
    }

    @Test
    @Transactional
    public void getAllNotesByIdPatientIsNullOrNotNull() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where idPatient is not null
        defaultNoteShouldBeFound("idPatient.specified=true");

        // Get all the noteList where idPatient is null
        defaultNoteShouldNotBeFound("idPatient.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotesByIdPatientIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where idPatient is greater than or equal to DEFAULT_ID_PATIENT
        defaultNoteShouldBeFound("idPatient.greaterThanOrEqual=" + DEFAULT_ID_PATIENT);

        // Get all the noteList where idPatient is greater than or equal to UPDATED_ID_PATIENT
        defaultNoteShouldNotBeFound("idPatient.greaterThanOrEqual=" + UPDATED_ID_PATIENT);
    }

    @Test
    @Transactional
    public void getAllNotesByIdPatientIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where idPatient is less than or equal to DEFAULT_ID_PATIENT
        defaultNoteShouldBeFound("idPatient.lessThanOrEqual=" + DEFAULT_ID_PATIENT);

        // Get all the noteList where idPatient is less than or equal to SMALLER_ID_PATIENT
        defaultNoteShouldNotBeFound("idPatient.lessThanOrEqual=" + SMALLER_ID_PATIENT);
    }

    @Test
    @Transactional
    public void getAllNotesByIdPatientIsLessThanSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where idPatient is less than DEFAULT_ID_PATIENT
        defaultNoteShouldNotBeFound("idPatient.lessThan=" + DEFAULT_ID_PATIENT);

        // Get all the noteList where idPatient is less than UPDATED_ID_PATIENT
        defaultNoteShouldBeFound("idPatient.lessThan=" + UPDATED_ID_PATIENT);
    }

    @Test
    @Transactional
    public void getAllNotesByIdPatientIsGreaterThanSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where idPatient is greater than DEFAULT_ID_PATIENT
        defaultNoteShouldNotBeFound("idPatient.greaterThan=" + DEFAULT_ID_PATIENT);

        // Get all the noteList where idPatient is greater than SMALLER_ID_PATIENT
        defaultNoteShouldBeFound("idPatient.greaterThan=" + SMALLER_ID_PATIENT);
    }


    @Test
    @Transactional
    public void getAllNotesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where description equals to DEFAULT_DESCRIPTION
        defaultNoteShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the noteList where description equals to UPDATED_DESCRIPTION
        defaultNoteShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllNotesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where description not equals to DEFAULT_DESCRIPTION
        defaultNoteShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the noteList where description not equals to UPDATED_DESCRIPTION
        defaultNoteShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllNotesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultNoteShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the noteList where description equals to UPDATED_DESCRIPTION
        defaultNoteShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllNotesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where description is not null
        defaultNoteShouldBeFound("description.specified=true");

        // Get all the noteList where description is null
        defaultNoteShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where description contains DEFAULT_DESCRIPTION
        defaultNoteShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the noteList where description contains UPDATED_DESCRIPTION
        defaultNoteShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllNotesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where description does not contain DEFAULT_DESCRIPTION
        defaultNoteShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the noteList where description does not contain UPDATED_DESCRIPTION
        defaultNoteShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllNotesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where date equals to DEFAULT_DATE
        defaultNoteShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the noteList where date equals to UPDATED_DATE
        defaultNoteShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllNotesByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where date not equals to DEFAULT_DATE
        defaultNoteShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the noteList where date not equals to UPDATED_DATE
        defaultNoteShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllNotesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where date in DEFAULT_DATE or UPDATED_DATE
        defaultNoteShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the noteList where date equals to UPDATED_DATE
        defaultNoteShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllNotesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where date is not null
        defaultNoteShouldBeFound("date.specified=true");

        // Get all the noteList where date is null
        defaultNoteShouldNotBeFound("date.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotesByDateContainsSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where date contains DEFAULT_DATE
        defaultNoteShouldBeFound("date.contains=" + DEFAULT_DATE);

        // Get all the noteList where date contains UPDATED_DATE
        defaultNoteShouldNotBeFound("date.contains=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllNotesByDateNotContainsSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where date does not contain DEFAULT_DATE
        defaultNoteShouldNotBeFound("date.doesNotContain=" + DEFAULT_DATE);

        // Get all the noteList where date does not contain UPDATED_DATE
        defaultNoteShouldBeFound("date.doesNotContain=" + UPDATED_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNoteShouldBeFound(String filter) throws Exception {
        restNoteMockMvc.perform(get("/api/notes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(note.getId().intValue())))
            .andExpect(jsonPath("$.[*].idDoctor").value(hasItem(DEFAULT_ID_DOCTOR.intValue())))
            .andExpect(jsonPath("$.[*].idPatient").value(hasItem(DEFAULT_ID_PATIENT.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)));

        // Check, that the count call also returns 1
        restNoteMockMvc.perform(get("/api/notes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNoteShouldNotBeFound(String filter) throws Exception {
        restNoteMockMvc.perform(get("/api/notes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNoteMockMvc.perform(get("/api/notes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingNote() throws Exception {
        // Get the note
        restNoteMockMvc.perform(get("/api/notes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNote() throws Exception {
        // Initialize the database
        noteService.save(note);

        int databaseSizeBeforeUpdate = noteRepository.findAll().size();

        // Update the note
        Note updatedNote = noteRepository.findById(note.getId()).get();
        // Disconnect from session so that the updates on updatedNote are not directly saved in db
        em.detach(updatedNote);
        updatedNote
            .idDoctor(UPDATED_ID_DOCTOR)
            .idPatient(UPDATED_ID_PATIENT)
            .description(UPDATED_DESCRIPTION)
            .date(UPDATED_DATE);

        restNoteMockMvc.perform(put("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedNote)))
            .andExpect(status().isOk());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
        Note testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getIdDoctor()).isEqualTo(UPDATED_ID_DOCTOR);
        assertThat(testNote.getIdPatient()).isEqualTo(UPDATED_ID_PATIENT);
        assertThat(testNote.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNote.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNoteMockMvc.perform(put("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(note)))
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNote() throws Exception {
        // Initialize the database
        noteService.save(note);

        int databaseSizeBeforeDelete = noteRepository.findAll().size();

        // Delete the note
        restNoteMockMvc.perform(delete("/api/notes/{id}", note.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
