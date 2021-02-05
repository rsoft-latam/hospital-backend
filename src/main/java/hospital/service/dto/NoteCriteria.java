package hospital.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link hospital.domain.Note} entity. This class is used
 * in {@link hospital.web.rest.NoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NoteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter idDoctor;

    private LongFilter idPatient;

    private StringFilter description;

    private StringFilter date;

    public NoteCriteria() {
    }

    public NoteCriteria(NoteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idDoctor = other.idDoctor == null ? null : other.idDoctor.copy();
        this.idPatient = other.idPatient == null ? null : other.idPatient.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.date = other.date == null ? null : other.date.copy();
    }

    @Override
    public NoteCriteria copy() {
        return new NoteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(LongFilter idDoctor) {
        this.idDoctor = idDoctor;
    }

    public LongFilter getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(LongFilter idPatient) {
        this.idPatient = idPatient;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getDate() {
        return date;
    }

    public void setDate(StringFilter date) {
        this.date = date;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NoteCriteria that = (NoteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(idDoctor, that.idDoctor) &&
            Objects.equals(idPatient, that.idPatient) &&
            Objects.equals(description, that.description) &&
            Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        idDoctor,
        idPatient,
        description,
        date
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NoteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (idDoctor != null ? "idDoctor=" + idDoctor + ", " : "") +
                (idPatient != null ? "idPatient=" + idPatient + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
            "}";
    }

}
