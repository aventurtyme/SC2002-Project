package edu.ntu.ccds.sc2002.util;

import edu.ntu.ccds.sc2002.model.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InternshipFilter {

    /**
     * Filters internships based on a combination of optional criteria.
     */
    public static List<Internship> filter(
            List<Internship> source,
            Optional<String> status,
            Optional<String> preferredMajor,
            Optional<InternshipLevel> level,
            Optional<LocalDate> closingBefore,
            Optional<Boolean> onlyVisible,
            Optional<User> viewer
    ) {
        return source.stream()
                .filter(i -> status.map(s -> i.getStatus().name().equalsIgnoreCase(s)).orElse(true))
                .filter(i -> preferredMajor.map(m -> i.getPreferredMajor().equalsIgnoreCase(m)).orElse(true))
                .filter(i -> level.map(l -> i.getLevel() == l).orElse(true))
                .filter(i -> closingBefore.map(date -> !i.getClosingDate().isAfter(date)).orElse(true))
                .filter(i -> onlyVisible.map(v -> !v || i.isVisible()).orElse(true))
                .filter(i -> viewer.map(u -> eligibleFor(u, i)).orElse(true))
                .sorted(Comparator.comparing(Internship::getTitle, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    /**
     * Determines if the viewer is eligible to view/apply for a specific internship.
     * Applies the following rules (from assignment spec):
     *  - Only visible internships can be seen.
     *  - Year 1-2 students can apply only for BASIC level.
     *  - Year 3-4 students can apply for any level.
     *  - Must match preferred major.
     */
    public static boolean eligibleFor(User user, Internship internship) {
        if (!(user instanceof Student s)) return true; // Non-students (e.g. CCS/Rep) can see all

        // Visibility
        if (!internship.isVisible()) return false;

        // Major check
        if (!internship.getPreferredMajor().equalsIgnoreCase(s.getMajor())) return false;

        // Year/level restriction
        if (s.getYearOfStudy() <= 2 && internship.getLevel() != InternshipLevel.BASIC) return false;

        // Closing date check (must not be expired)
        if (LocalDate.now().isAfter(internship.getClosingDate())) return false;

        return true;
    }

    /**
     * Returns a Predicate version for reusability (e.g., in streams or other modules)
     */
    public static Predicate<Internship> eligibilityFor(Student s) {
        return i -> eligibleFor(s, i);
    }

    /**
     * Saves user-specific filter preferences to a simple map.
     * (Optional: integrate with persistent storage later if needed.)
     */
    public static class UserFilterPrefs {
        private String status;
        private String preferredMajor;
        private InternshipLevel level;
        private LocalDate closingBefore;
        private boolean onlyVisible = true;

        public UserFilterPrefs() {}

        public Predicate<Internship> toPredicate(User user) {
            return i -> filter(List.of(i),
                    Optional.ofNullable(status),
                    Optional.ofNullable(preferredMajor),
                    Optional.ofNullable(level),
                    Optional.ofNullable(closingBefore),
                    Optional.of(onlyVisible),
                    Optional.ofNullable(user)
            ).size() == 1;
        }

        public void setStatus(String status) { this.status = status; }
        public void setPreferredMajor(String preferredMajor) { this.preferredMajor = preferredMajor; }
        public void setLevel(InternshipLevel level) { this.level = level; }
        public void setClosingBefore(LocalDate closingBefore) { this.closingBefore = closingBefore; }
        public void setOnlyVisible(boolean onlyVisible) { this.onlyVisible = onlyVisible; }
    }
}