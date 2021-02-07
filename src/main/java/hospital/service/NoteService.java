package hospital.service;

import hospital.domain.Note;
import hospital.domain.extras.NoteDoctorLite;
import hospital.domain.extras.NotePatientLite;
import hospital.repository.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Note}.
 */
@Service
@Transactional
public class NoteService {

    private final Logger log = LoggerFactory.getLogger(NoteService.class);

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Save a note.
     *
     * @param note the entity to save.
     * @return the persisted entity.
     */
    public Note save(Note note) {
        log.debug("Request to save Note : {}", note);
        return noteRepository.save(note);
    }

    /**
     * Get all the notes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Note> findAll(Pageable pageable) {
        log.debug("Request to get all Notes");
        return noteRepository.findAll(pageable);
    }


    /**
     * Get one note by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Note> findOne(Long id) {
        log.debug("Request to get Note : {}", id);
        return noteRepository.findById(id);
    }

    /**
     * Delete the note by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Note : {}", id);
        noteRepository.deleteById(id);
    }




    /**
     * Get all the notes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NotePatientLite> findAllByIdPatient(Pageable pageable, Long idPatient) {
        log.debug("Request to get all Notes");
        return noteRepository.findAllByIdPatient(pageable, idPatient);
    }


    /**
     * Get all the notes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NoteDoctorLite> findAllByIdDoctor(Pageable pageable, Long idDoctor) {
        log.debug("Request to get all Notes");
        return noteRepository.findAllByIdDoctor(pageable, idDoctor);
    }


}
