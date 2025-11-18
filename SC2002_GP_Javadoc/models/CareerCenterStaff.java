package models;

/**
 * Represents a Career Center staff member who can review and approve requests.
 * <p>This class is documented for API generation via the Javadoc tool.</p>
 */
public class CareerCenterStaff extends User {
    private String department;

    public CareerCenterStaff(String userID, String name, String department, String email) {
        super(userID, name, email);
        this.department = department;
    }

    public String getDepartment() { 
        return department; 
    }

    @Override
    public String toString() { 
        return String.format("Staff: %s (%s)", name, department); 
    }
}