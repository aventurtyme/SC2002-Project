package services;

import enums.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import models.*;

/**
 * Service layer for business logic related to ReportService.
 * <p>This class is documented for API generation via the Javadoc tool.</p>
 */
public class ReportService {
/**
 * printOpportunitiesByStatus operation.
 *
 * @param list parameter
 * @return result of the operation
 * @throws IllegalArgumentException if invalid arguments
 */
    public static void printOpportunitiesByStatus(List<InternshipOpportunity> list) {
        Map<OpportunityStatus, List<InternshipOpportunity>> map = list.stream().collect(Collectors.groupingBy(InternshipOpportunity::getStatus));
        for (OpportunityStatus s : OpportunityStatus.values()) {
            System.out.println("== " + s + " ==");
            List<InternshipOpportunity> l = map.get(s);
            if (l == null || l.isEmpty()) System.out.println(" (none)");
            else l.forEach(i -> System.out.println(" " + i));
        }
    }

    public static List<InternshipOpportunity> filterByLevel(List<InternshipOpportunity> list, InternshipLevel level) {
        return list.stream().filter(i -> i.getLevel() == level).collect(Collectors.toList());
    }

    public static List<InternshipOpportunity> filterByMajor(List<InternshipOpportunity> list, String major) {
        return list.stream().filter(i -> i.getPreferredMajor() == null || i.getPreferredMajor().isEmpty() || i.getPreferredMajor().equalsIgnoreCase(major)).collect(Collectors.toList());
    }
}