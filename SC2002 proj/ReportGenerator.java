import java.util.List;
import java.util.function.Predicate;

public class ReportGenerator {
    public String generate(List<Internship> all,
                           InternshipStatus status, String major, InternshipLevel level, String company) {
        Predicate<Internship> p = i -> true;
        if (status != null) p = p.and(i -> i.getStatus()==status);
        if (major != null && !major.isBlank()) p = p.and(i -> i.getPreferredMajor().equalsIgnoreCase(major));
        if (level != null) p = p.and(i -> i.getLevel()==level);
        if (company != null && !company.isBlank()) p = p.and(i -> i.getCompanyName().equalsIgnoreCase(company));

        List<Internship> list = all.stream().filter(p).toList();

        StringBuilder sb = new StringBuilder();
        sb.append("Report: ").append(list.size()).append(" opportunities
");
        for (Internship i : list) {
            sb.append("- ").append(i.getTitle()).append(" | ").append(i.getCompanyName()).append("\n");
        }
        return sb.toString();
    }
}
