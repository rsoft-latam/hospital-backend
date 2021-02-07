package hospital.service.mapper;

import hospital.domain.Note;
import hospital.service.dto.NoteDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {

    default Note fromId(Long id) {
        if (id == null) {
            return null;
        }
        Note note = new Note();
        note.setId(id);
        return note;
    }
}
