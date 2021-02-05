package hospital.service;

import hospital.domain.Specialty;
import hospital.repository.SpecialtyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Specialty}.
 */
@Service
@Transactional
public class SpecialtyService {

    private final Logger log = LoggerFactory.getLogger(SpecialtyService.class);

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyService(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    /**
     * Save a specialty.
     *
     * @param specialty the entity to save.
     * @return the persisted entity.
     */
    public Specialty save(Specialty specialty) {
        log.debug("Request to save Specialty : {}", specialty);
        return specialtyRepository.save(specialty);
    }

    /**
     * Get all the specialties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Specialty> findAll(Pageable pageable) {
        log.debug("Request to get all Specialties");
        return specialtyRepository.findAll(pageable);
    }


    /**
     * Get one specialty by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Specialty> findOne(Long id) {
        log.debug("Request to get Specialty : {}", id);
        return specialtyRepository.findById(id);
    }

    /**
     * Delete the specialty by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Specialty : {}", id);
        specialtyRepository.deleteById(id);
    }
}
