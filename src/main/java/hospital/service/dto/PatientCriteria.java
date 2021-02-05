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
 * Criteria class for the {@link hospital.domain.Patient} entity. This class is used
 * in {@link hospital.web.rest.PatientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /patients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PatientCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter birthday;

    private StringFilter address;

    private StringFilter urlPhoto;

    private LongFilter hospitalId;

    public PatientCriteria() {
    }

    public PatientCriteria(PatientCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.birthday = other.birthday == null ? null : other.birthday.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.urlPhoto = other.urlPhoto == null ? null : other.urlPhoto.copy();
        this.hospitalId = other.hospitalId == null ? null : other.hospitalId.copy();
    }

    @Override
    public PatientCriteria copy() {
        return new PatientCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getBirthday() {
        return birthday;
    }

    public void setBirthday(StringFilter birthday) {
        this.birthday = birthday;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(StringFilter urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public LongFilter getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(LongFilter hospitalId) {
        this.hospitalId = hospitalId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PatientCriteria that = (PatientCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(birthday, that.birthday) &&
            Objects.equals(address, that.address) &&
            Objects.equals(urlPhoto, that.urlPhoto) &&
            Objects.equals(hospitalId, that.hospitalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstName,
        lastName,
        birthday,
        address,
        urlPhoto,
        hospitalId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (birthday != null ? "birthday=" + birthday + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (urlPhoto != null ? "urlPhoto=" + urlPhoto + ", " : "") +
                (hospitalId != null ? "hospitalId=" + hospitalId + ", " : "") +
            "}";
    }

}
