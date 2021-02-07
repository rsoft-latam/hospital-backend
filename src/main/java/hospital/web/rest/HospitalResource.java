package hospital.web.rest;

import hospital.domain.Hospital;
import hospital.service.HospitalService;
import hospital.service.dto.HospitalDTO;
import hospital.web.rest.errors.BadRequestAlertException;
import hospital.service.dto.HospitalCriteria;
import hospital.service.HospitalQueryService;

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
 * REST controller for managing {@link hospital.domain.Hospital}.
 */
@RestController
@RequestMapping("/api")
public class HospitalResource {

    private final Logger log = LoggerFactory.getLogger(HospitalResource.class);

    private static final String ENTITY_NAME = "hospital";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HospitalService hospitalService;

    private final HospitalQueryService hospitalQueryService;

    public HospitalResource(HospitalService hospitalService, HospitalQueryService hospitalQueryService) {
        this.hospitalService = hospitalService;
        this.hospitalQueryService = hospitalQueryService;
    }

    /**
     * {@code POST  /hospitals} : Create a new hospital.
     *
     * @param hospital the hospital to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hospital, or with status {@code 400 (Bad Request)} if the hospital has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hospitals")
    public ResponseEntity<Hospital> createHospital(@Valid @RequestBody Hospital hospital) throws URISyntaxException {
        log.debug("REST request to save Hospital : {}", hospital);
        if (hospital.getId() != null) {
            throw new BadRequestAlertException("A new hospital cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hospital result = hospitalService.save(hospital);
        return ResponseEntity.created(new URI("/api/hospitals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hospitals} : Updates an existing hospital.
     *
     * @param hospital the hospital to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hospital,
     * or with status {@code 400 (Bad Request)} if the hospital is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hospital couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hospitals")
    public ResponseEntity<Hospital> updateHospital(@Valid @RequestBody Hospital hospital) throws URISyntaxException {
        log.debug("REST request to update Hospital : {}", hospital);
        if (hospital.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Hospital result = hospitalService.save(hospital);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hospital.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /hospitals} : get all the hospitals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hospitals in body.
     */
    @GetMapping("/hospitals")
    public ResponseEntity<List<Hospital>> getAllHospitals(HospitalCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Hospitals by criteria: {}", criteria);
        Page<Hospital> page = hospitalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hospitals/count} : count all the hospitals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/hospitals/count")
    public ResponseEntity<Long> countHospitals(HospitalCriteria criteria) {
        log.debug("REST request to count Hospitals by criteria: {}", criteria);
        return ResponseEntity.ok().body(hospitalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hospitals/:id} : get the "id" hospital.
     *
     * @param id the id of the hospital to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hospital, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hospitals/{id}")
    public ResponseEntity<Hospital> getHospital(@PathVariable Long id) {
        log.debug("REST request to get Hospital : {}", id);
        Optional<Hospital> hospital = hospitalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hospital);
    }

    /**
     * {@code DELETE  /hospitals/:id} : delete the "id" hospital.
     *
     * @param id the id of the hospital to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hospitals/{id}")
    public ResponseEntity<Void> deleteHospital(@PathVariable Long id) {
        log.debug("REST request to delete Hospital : {}", id);
        hospitalService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }







    /**
     * {@code GET  /hospitals} : get all the hospitals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hospitals in body.
     */
    @GetMapping("/hospitals-auditory")
    public ResponseEntity<List<HospitalDTO>> getAllHospitalsAuditory(HospitalCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Hospitals by criteria: {}", criteria);
        Page<HospitalDTO> page = hospitalQueryService.findByCriteriaAuditory(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }



}
