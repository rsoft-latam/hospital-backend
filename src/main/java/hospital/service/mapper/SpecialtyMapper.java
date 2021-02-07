package hospital.service.mapper;

import hospital.domain.Specialty;
import hospital.service.dto.SpecialtyDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface SpecialtyMapper extends EntityMapper<SpecialtyDTO, Specialty> {

    default Specialty fromId(Long id) {
        if (id == null) {
            return null;
        }
        Specialty specialty = new Specialty();
        specialty.setId(id);
        return specialty;
    }
}
