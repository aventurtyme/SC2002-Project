package edu.ntu.ccds.sc2002.io;

import edu.ntu.ccds.sc2002.model.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Simple CSV-based loader/saver for initial data. No DB/JSON/XML per assignment rules.
 */
public class FileHandler {

    public List<Student> loadStudents(Path csv) throws IOException {
        List<Student> list = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(csv)) {
            String line; boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) { headerSkipped = true; continue; }
                if (line.isBlank()) continue;
                String[] t = splitCsv(line);
                // id,name,year,major,password(optional)
                String id = tAt(t,0), name = tAt(t,1), major = tAt(t,3);
                int year = Integer.parseInt(tAt(t,2));
                String pwd = (t.length > 4) ? t[4].trim() : "";
                list.add(new Student(id, name, year, major, pwd));
            }
        }
        return list;
    }

    public List<CareerCenterStaff> loadCareerCenterStaff(Path csv) throws IOException {
        List<CareerCenterStaff> list = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(csv)) {
            String line; boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) { headerSkipped = true; continue; }
                if (line.isBlank()) continue;
                String[] t = splitCsv(line);
                // id,name,department,password(optional)
                String id = tAt(t,0), name = tAt(t,1), dept = tAt(t,2);
                String pwd = (t.length > 3) ? t[3].trim() : "";
                list.add(new CareerCenterStaff(id, name, dept, pwd));
            }
        }
        return list;
    }

    public List<CompanyRepresentative> loadCompanyReps(Path csv) throws IOException {
        List<CompanyRepresentative> list = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(csv)) {
            String line; boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) { headerSkipped = true; continue; }
                if (line.isBlank()) continue;
                String[] t = splitCsv(line);
                // email,name,company,department,position,approved(true/false),password(optional)
                String email = tAt(t,0), name = tAt(t,1), company = tAt(t,2);
                String dept = tAt(t,3), pos = tAt(t,4);
                boolean approved = Boolean.parseBoolean(tAt(t,5));
                String pwd = (t.length > 6) ? t[6].trim() : "";
                list.add(new CompanyRepresentative(email, name, company, dept, pos, approved, pwd));
            }
        }
        return list;
    }

    public List<Internship> loadInternships(Path csv) throws IOException {
        List<Internship> list = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(csv)) {
            String line; boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) { headerSkipped = true; continue; }
                if (line.isBlank()) continue;
                String[] t = splitCsv(line);
                // id,title,desc,level,major,openDate,closeDate,status,company,repEmail,slots,visible
                String id = tAt(t,0), title = tAt(t,1), desc = tAt(t,2);
                InternshipLevel level = InternshipLevel.valueOf(tAt(t,3).toUpperCase());
                String major = tAt(t,4);
                LocalDate open = LocalDate.parse(tAt(t,5));
                LocalDate close = LocalDate.parse(tAt(t,6));
                InternshipStatus status = InternshipStatus.valueOf(tAt(t,7).toUpperCase());
                String company = tAt(t,8), rep = tAt(t,9);
                int slots = Integer.parseInt(tAt(t,10));
                boolean visible = Boolean.parseBoolean(tAt(t,11));
                list.add(new Internship(id, title, desc, level, major, open, close, status, company, rep, slots, visible));
            }
        }
        return list;
    }

    // --- helpers ---
    private static String[] splitCsv(String line) {
        // naive split; assumes no escaped commas. Good enough for assignment CSVs.
        return line.split(",");
    }
    private static String tAt(String[] arr, int i) { return (i < arr.length) ? arr[i].trim() : ""; }
}
