package hospital.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import hospital.service.dto.SpecialtyDTO;
import hospital.service.mapper.SpecialtyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import hospital.domain.Specialty;
import hospital.domain.*; // for static metamodels
import hospital.repository.SpecialtyRepository;
import hospital.service.dto.SpecialtyCriteria;

/**
 * Service for executing complex queries for {@link Specialty} entities in the database.
 * The main input is a {@link SpecialtyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Specialty} or a {@link Page} of {@link Specialty} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SpecialtyQueryService extends QueryService<Specialty> {

    private final Logger log = LoggerFactory.getLogger(SpecialtyQueryService.class);

    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;

    public SpecialtyQueryService(SpecialtyRepository specialtyRepository, SpecialtyMapper specialtyMapper) {
        this.specialtyRepository = specialtyRepository;
        this.specialtyMapper = specialtyMapper;
    }

    /**
     * Return a {@link List} of {@link Specialty} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Specialty> findByCriteria(SpecialtyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Specialty> specification = createSpecification(criteria);
        return specialtyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Specialty} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Specialty> findByCriteria(SpecialtyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Specialty> specification = createSpecification(criteria);
        return specialtyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SpecialtyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Specialty> specification = createSpecification(criteria);
        return specialtyRepository.count(specification);
    }

    /**
     * Function to convert {@link SpecialtyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Specialty> createSpecification(SpecialtyCriteria criteria) {
        Specification<Specialty> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Specialty_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Specialty_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Specialty_.description));
            }
            if (criteria.getIcon() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIcon(), Specialty_.icon));
            }
            if (criteria.getDoctorId() != null) {
                specification = specification.and(buildSpecification(criteria.getDoctorId(),
                    root -> root.join(Specialty_.doctor, JoinType.LEFT).get(Doctor_.id)));
            }
        }
        return specification;
    }








    /**
     * Return a {@link Page} of {@link Specialty} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SpecialtyDTO> findByCriteriaAuditory(SpecialtyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Specialty> specification = createSpecification(criteria);
        return specialtyRepository.findAll(specification, page)
            .map(specialtyMapper::toDto);
    }




}
