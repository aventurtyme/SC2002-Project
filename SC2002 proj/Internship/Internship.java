import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Internship {
    private final long id;
    private String title;
    private String description;
    private String companyName;
    private String preferredMajor; // assumes single preferred major
    private InternshipLevel level;
    private InternshipStatus status = InternshipStatus.PENDING;
    private boolean visible = false; // handled by InternshipController.java
    private LocalDate openDate;
    private LocalDate closeDate;
    private int capacity = 1; // number of internship slots (max 10)
    private final List<String> assignedRepIds = new ArrayList<>(); // company reps IDs/emails
    private final List<Long> applicationIds = new ArrayList<>();

    public Internship(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    private void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    private void setDescription(String discription) {
        this.description = discription;
    }
    public String getCompanyName() {
        return companyName;
    }
    private void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public InternshipLevel getLevel() {
        return level;
    }
    private void setLevel(InternshipLevel level) {
        this.level = level;
    }
    public InternshipStatus getStatus() {
        return status;
    }
    public void setStatus(InternshipStatus status) {
        this.status = status;
    }
    public boolean isVisible() {
        return visible;
    }
    private void setVisible(boolean visible) {
        this.visible = visible;
    }
    public LocalDate getOpenDate() {
        return openDate;
    }
    private void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }
    public LocalDate getCloseDate() {
        return closeDate;
    }
    private void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        if (capacity < 1) capacity = 1;
        if (capacity > 10) capacity = 10;
        this.capacity = capacity;
    }
    public List<Long> getApplicationIds() { 
        return Collections.unmodifiableList(applicationIds); 
    }
    public void addApplicationId(Long id) { 
        if (!applicationIds.contains(id)) applicationIds.add(id); 
    }
    public void removeApplicationId(Long id) { 
        applicationIds.remove(id); 
    }
    public List<String> getAssignedRepIds() { 
        return Collections.unmodifiableList(assignedRepIds); 
    }
    public void assignRep(String repId) { 
        if (!assignedRepIds.contains(repId)) assignedRepIds.add(repId); 
    }
    public void unassignRep(String repId) { 
        assignedRepIds.remove(repId); 
    }

    @Override
    public String toString() {
        return String.format("internship[id=%d, title=%s, company=%s, level=%s, status=%s, visible=%s, slots=%d]", id, title, companyName, level, status, visible, capacity);
    }
}
