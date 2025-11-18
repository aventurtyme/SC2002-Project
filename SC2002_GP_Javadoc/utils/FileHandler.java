package utils;

import enums.RequestStatus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import models.*;

/**
 * Utility for loading and writing domain objects to/from delimited text files under the data folder.
 * <p>Documented for API generation via the Javadoc tool.</p>
 */
public class FileHandler {
    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
/**
 * method utility method.
 *
 * @return result value
 * @throws IllegalArgumentException if arguments are invalid
 */

    public static List<Student> readStudents(String path) {
        List<Student> students = new ArrayList<>();
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("Student file not found at: " + path);
            return students;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                if (first) { 
                    first = false; 
                    continue; 
                }
                String norm = line.replace('\t', ',');
                String[] parts = norm.split(",");
                if (parts.length < 5) continue;
                String id = parts[0].trim();
                String name = parts[1].trim();
                String major = parts[2].trim();
                int year = Integer.parseInt(parts[3].trim());
                String email = parts[4].trim();
                students.add(new Student(id, name, major, year, email));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }
/**
 * method utility method.
 *
 * @return result value
 * @throws IllegalArgumentException if arguments are invalid
 */

    public static List<CareerCenterStaff> readStaff(String path) {
        List<CareerCenterStaff> staff = new ArrayList<>();
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("Staff file not found at: " + path);
            return staff;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                if (first) { 
                    first = false; 
                    continue; 
                }
                String norm = line.replace('\t', ',');
                String[] parts = norm.split(",");
                if (parts.length < 5) continue;
                String id = parts[0].trim();
                String name = parts[1].trim();
                String role = parts[2].trim();
                String dept = parts[3].trim();
                String email = parts[4].trim();
                staff.add(new CareerCenterStaff(id, name, dept, email));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return staff;
    }
/**
 * method utility method.
 *
 * @return result value
 * @throws IllegalArgumentException if arguments are invalid
 */

    public static List<CompanyRepresentative> readCompanyReps(String path) {
        List<CompanyRepresentative> reps = new ArrayList<>();
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("Company rep file not found at: " + path);
            return reps;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                if (first) { 
                    first = false; 
                    continue; 
                }
                String norm = line.replace('\t', ',');
                String[] parts = norm.split(",");
                if (parts.length < 7) continue; // must have at least 7 columns
                String id = parts[0].trim();
                String name = parts[1].trim();
                String companyName = parts[2].trim();
                String department = parts[3].trim();  
                String position = parts[4].trim();    
                String email = parts[5].trim();        
                String statusStr = parts[6].trim();
                String defaultPassword = "password";
                
                RequestStatus status = RequestStatus.PENDING;
                if (statusStr.equalsIgnoreCase("APPROVED")) status = RequestStatus.APPROVED;
                else if (statusStr.equalsIgnoreCase("REJECTED")) status = RequestStatus.REJECTED;

                CompanyRepresentative repObj = new CompanyRepresentative(
                id, name, defaultPassword, companyName, department, position, email
            );
            repObj.setRegistrationStatus(status);

            reps.add(repObj);
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return reps;
    }
}