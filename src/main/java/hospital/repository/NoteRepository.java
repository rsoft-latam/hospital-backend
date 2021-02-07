package hospital.repository;

import hospital.domain.Note;

import hospital.domain.extras.NoteDoctorLite;
import hospital.domain.extras.NotePatientLite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Note entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends JpaRepository<Note, Long>, JpaSpecificationExecutor<Note> {

    /**
     * Get All By Id Patient
     */
    @Query(value = "SELECT new hospital.domain.extras.NotePatientLite(t1.id, t1.firstName, t1.lastName, t3.firstName, t3.lastName, t2.description, t2.date) FROM Patient t1 INNER JOIN Note t2 ON t1.id = t2.idPatient INNER JOIN Doctor t3 ON t2.idDoctor=t3.id WHERE t1.id = ?1")
    Page<NotePatientLite> findAllByIdPatient(Pageable pageable, Long idPatient);

    /**
     * Get All By Id Doctor
     */
    @Query("SELECT new hospital.domain.extras.NoteDoctorLite(t1.id, t1.firstName, t1.lastName,t3.firstName, t3.lastName, t2.description, t2.date) FROM Doctor t1 INNER JOIN Note t2 ON t1.id = t2.idDoctor INNER JOIN Patient t3 ON t2.idPatient=t3.id WHERE t1.id = ?1")
    Page<NoteDoctorLite> findAllByIdDoctor(Pageable pageable, Long idDoctor);

}
