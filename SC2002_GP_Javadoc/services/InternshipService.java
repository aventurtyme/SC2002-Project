package services;

import enums.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import models.*;

/**
 * Read-only queries and filtering for internship opportunities.
 * <p>This class is documented for API generation via the Javadoc tool.</p>
 */
public class InternshipService {
    private List<InternshipOpportunity> internships = new ArrayList<>();
/**
 * method operation.
 *
 * @return result of the operation
 * @throws IllegalArgumentException if invalid arguments
 */

    public void addOpportunity(InternshipOpportunity opp) { 
        long count = internships.stream().filter(o -> o.getRepInCharge().equals(opp.getRepInCharge())).count();        
        if (count >= 5) {
            System.out.println("You have reached the maximum of 5 internship opportunities.");
            return;
        }
        internships.add(opp);
        System.out.println("Internship opportunity created successfully.");
    }

    public List<InternshipOpportunity> getAll() { 
        return internships; 
    }

    public List<InternshipOpportunity> getByRep(CompanyRepresentative rep) {
        return internships.stream().filter(o -> o.getRepInCharge().equals(rep)).collect(Collectors.toList());
    }

    public InternshipOpportunity findById(String id) {
        for (InternshipOpportunity i : internships) if (i.getId().equals(id)) return i;
        return null;
    }

    public void removeOpportunity(InternshipOpportunity opp) {
        internships.remove(opp);
    }
    
    // Return list available to the student based on profile + visibility + opportunity status
    public List<InternshipOpportunity> getVisibleForStudent(Student s) {
        return getFilteredInternships(
                s,
                OpportunityStatus.APPROVED,
                null,
                null,
                null,
                true
        );
    }

    // filter method for all users
    public List<InternshipOpportunity> getFilteredInternships(
            Student student,
            OpportunityStatus status,
            InternshipLevel level,
            String preferredMajor,
            LocalDate closingBefore,
            boolean onlyVisible
    ) {
        return internships.stream()
                .filter(i -> student == null 
                        || i.getPreferredMajor() == null 
                        || i.getPreferredMajor().isEmpty() 
                        || i.getPreferredMajor().equalsIgnoreCase(student.getMajor()))
                .filter(i -> status == null || i.getStatus() == status)
                .filter(i -> level == null || i.getLevel() == level)
                .filter(i -> preferredMajor == null 
                        || i.getPreferredMajor() == null 
                        || i.getPreferredMajor().isEmpty() 
                        || i.getPreferredMajor().equalsIgnoreCase(preferredMajor))
                .filter(i -> closingBefore == null || i.getClosingDate().isBefore(closingBefore))
                .filter(i -> !onlyVisible || i.isVisible())
                .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()))
                .collect(Collectors.toList());
    }
}