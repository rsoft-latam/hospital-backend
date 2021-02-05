package hospital.web.rest;

import hospital.domain.Specialty;
import hospital.service.SpecialtyService;
import hospital.web.rest.errors.BadRequestAlertException;
import hospital.service.dto.SpecialtyCriteria;
import hospital.service.SpecialtyQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link hospital.domain.Specialty}.
 */
@RestController
@RequestMapping("/api")
public class SpecialtyResource {

    private final Logger log = LoggerFactory.getLogger(SpecialtyResource.class);

    private static final String ENTITY_NAME = "specialty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialtyService specialtyService;

    private final SpecialtyQueryService specialtyQueryService;

    public SpecialtyResource(SpecialtyService specialtyService, SpecialtyQueryService specialtyQueryService) {
        this.specialtyService = specialtyService;
        this.specialtyQueryService = specialtyQueryService;
    }

    /**
     * {@code POST  /specialties} : Create a new specialty.
     *
     * @param specialty the specialty to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialty, or with status {@code 400 (Bad Request)} if the specialty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/specialties")
    public ResponseEntity<Specialty> createSpecialty(@Valid @RequestBody Specialty specialty) throws URISyntaxException {
        log.debug("REST request to save Specialty : {}", specialty);
        if (specialty.getId() != null) {
            throw new BadRequestAlertException("A new specialty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Specialty result = specialtyService.save(specialty);
        return ResponseEntity.created(new URI("/api/specialties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /specialties} : Updates an existing specialty.
     *
     * @param specialty the specialty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialty,
     * or with status {@code 400 (Bad Request)} if the specialty is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/specialties")
    public ResponseEntity<Specialty> updateSpecialty(@Valid @RequestBody Specialty specialty) throws URISyntaxException {
        log.debug("REST request to update Specialty : {}", specialty);
        if (specialty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Specialty result = specialtyService.save(specialty);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, specialty.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /specialties} : get all the specialties.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialties in body.
     */
    @GetMapping("/specialties")
    public ResponseEntity<List<Specialty>> getAllSpecialties(SpecialtyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Specialties by criteria: {}", criteria);
        Page<Specialty> page = specialtyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /specialties/count} : count all the specialties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/specialties/count")
    public ResponseEntity<Long> countSpecialties(SpecialtyCriteria criteria) {
        log.debug("REST request to count Specialties by criteria: {}", criteria);
        return ResponseEntity.ok().body(specialtyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /specialties/:id} : get the "id" specialty.
     *
     * @param id the id of the specialty to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialty, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/specialties/{id}")
    public ResponseEntity<Specialty> getSpecialty(@PathVariable Long id) {
        log.debug("REST request to get Specialty : {}", id);
        Optional<Specialty> specialty = specialtyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specialty);
    }

    /**
     * {@code DELETE  /specialties/:id} : delete the "id" specialty.
     *
     * @param id the id of the specialty to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/specialties/{id}")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
        log.debug("REST request to delete Specialty : {}", id);
        specialtyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
