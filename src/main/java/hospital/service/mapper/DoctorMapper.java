package hospital.service.mapper;

import hospital.domain.Doctor;
import hospital.service.dto.DoctorDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {})
public interface DoctorMapper extends EntityMapper<DoctorDTO, Doctor> {

    default Doctor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Doctor doctor = new Doctor();
        doctor.setId(id);
        return doctor;
    }
}
