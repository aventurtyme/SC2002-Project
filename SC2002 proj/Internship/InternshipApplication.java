import java.time.Instant;

public class InternshipApplication {
    private final long id;
    private final String applicantId; // student ID
    private final long internshipId;
    private ApplicationStatus status = ApplicationStatus.PENDING;
    private final Instant createdAt;
    private Instant updatedAt;
    private String resumePath;
    private String coverLetter;

    public InternshipApplication(long id, String applicantId, long internshipId) {
        this.id = id;
        this.applicantId = applicantId;
        this.internshipId = internshipId;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public long getId() {
        return id;
    }
    public String getApplicatioId() {
        return applicantId;
    }
    public long getInternshipId() {
        return internshipId;
    }
    public ApplicationStatus getStatus() {
        return status;
    }
    public void setStatus(ApplicationStatus status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public String getResumePath() {
        return resumePath;
    }
    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }
    public String getCoverLetter() {
        return coverLetter;
    }
    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    @Override
    public String toString() {
        return String.format("App[id=%d, applicant=%s, internship=%d, status=%s]", id, applicantId, internshipId, status);
    }
}
