package utils;

import enums.*;
import java.util.Scanner;

/**
 * Utility class used across the application: FilterHelper.
 * <p>Documented for API generation via the Javadoc tool.</p>
 */
public class FilterHelper {
/**
 * method utility method.
 *
 * @return result value
 * @throws IllegalArgumentException if arguments are invalid
 */

    public static FilterCriteria promptFilters(Scanner scanner) {
        FilterCriteria criteria = new FilterCriteria();

        System.out.print("Filter by Status (or blank): ");
        String statusInput = scanner.nextLine().trim().toUpperCase();
        if (!statusInput.isEmpty()) {
            try {
                criteria.status = OpportunityStatus.valueOf(statusInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Ignored.");
            }
        }

        System.out.print("Filter by Level (BASIC/INTERMEDIATE/ADVANCED or blank): ");
        String levelInput = scanner.nextLine().trim().toUpperCase();
        if (!levelInput.isEmpty()) {
            try {
                criteria.level = InternshipLevel.valueOf(levelInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid level. Ignored.");
            }
        }

        System.out.print("Filter by Preferred Major (or blank): ");
        String majorInput = scanner.nextLine().trim();
        criteria.preferredMajor = majorInput.isEmpty() ? null : majorInput;

        System.out.print("Filter by Closing Date before (yyyy-MM-dd or blank): ");
        String dateInput = scanner.nextLine().trim();
        try {
            criteria.closingBefore = dateInput.isEmpty() ? null : java.time.LocalDate.parse(dateInput);
        } catch (Exception e) {
            System.out.println("Invalid date. Ignored.");
            criteria.closingBefore = null;
        }

        return criteria;
    }

    public static class FilterCriteria {
        public OpportunityStatus status = null;
        public InternshipLevel level = null;
        public String preferredMajor = null;
        public java.time.LocalDate closingBefore = null;
    }
}