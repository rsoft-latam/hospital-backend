package hospital.service.dto;

import hospital.domain.Hospital;

import java.io.Serializable;
import java.time.Instant;

public class PatientDTO implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String birthday;
    private String address;
    private String urlPhoto;
    private Hospital hospital;

    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getBirthday() { return birthday; }

    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getUrlPhoto() { return urlPhoto; }

    public void setUrlPhoto(String urlPhoto) { this.urlPhoto = urlPhoto; }

    public Hospital getHospital () { return hospital; }

    public void setHospital(Hospital hospital) { this.hospital = hospital; }



    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }


}
