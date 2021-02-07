package hospital.domain.extras;

public class NotePatientLite {

    private Long id;
    private String firstNamePatient;
    private String lastNamePatient;
    private String firstNameDoctor;
    private String lastNameDoctor;
    private String description;
    private String date;

    public NotePatientLite() {

    }

    public NotePatientLite(Long id,
        String firstNamePatient,
        String lastNamePatient,
        String firstNameDoctor,
        String lastNameDoctor,
        String description,
        String date) {
        this.id = id;
        this.firstNamePatient = firstNamePatient;
        this.lastNamePatient = lastNamePatient;
        this.firstNameDoctor = firstNameDoctor;
        this.lastNameDoctor = lastNameDoctor;
        this.description = description;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstNamePatient() {
        return firstNamePatient;
    }

    public void setFirstNamePatient(String firstNamePatient) {
        this.firstNamePatient = firstNamePatient;
    }

    public String getLastNamePatient() {
        return lastNamePatient;
    }

    public void setLastNamePatient(String lastNamePatient) {
        this.lastNamePatient = lastNamePatient;
    }

    public String getFirstNameDoctor() {
        return firstNameDoctor;
    }

    public void setFirstNameDoctor(String firstNameDoctor) {
        this.firstNameDoctor = firstNameDoctor;
    }

    public String getLastNameDoctor() {
        return lastNameDoctor;
    }

    public void setLastNameDoctor(String lastNameDoctor) {
        this.lastNameDoctor = lastNameDoctor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
