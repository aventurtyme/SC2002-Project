package edu.ntu.ccds.sc2002.main;

import edu.ntu.ccds.sc2002.io.FileHandler;
import edu.ntu.ccds.sc2002.model.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class TestDataGenerator {

    public static void main(String[] args) throws Exception {
        Path base = Paths.get("data");
        if (!Files.exists(base)) Files.createDirectories(base);

        createSampleFiles(base);

        System.out.println("[Data Setup Complete] Files created under ./data\n--- students.csv / companyreps.csv / companystaff.csv / internships.csv ---\n");

        FileHandler fh = new FileHandler();
        System.out.println("Students loaded: " + fh.loadStudents(base.resolve("students.csv")).size());
        System.out.println("Company reps loaded: " + fh.loadCompanyReps(base.resolve("companyreps.csv")).size());
        System.out.println("CCS staff loaded: " + fh.loadCareerCenterStaff(base.resolve("companystaff.csv")).size());
        System.out.println("Internships loaded: " + fh.loadInternships(base.resolve("internships.csv")).size());

        System.out.println("\nNow run InternManagementSystem to start the CLI.");
    }

    private static void createSampleFiles(Path base) throws IOException {
        createIfMissing(base.resolve("students.csv"), List.of(
                "id,name,year,major,password",
                "U2345123F,Alice,3,CSC,",
                "U1234567B,Bob,2,EEE,password",
                "U7654321A,Charlie,4,MAE,"
        ));

        createIfMissing(base.resolve("companystaff.csv"), List.of(
                "id,name,department,password",
                "ccs001,Dr. Tan,CCDS,",
                "ccs002,Ms. Lee,CCDS,"
        ));

        createIfMissing(base.resolve("companyreps.csv"), List.of(
                "email,name,company,department,position,approved,password",
                "rep@acme.com,John,ACME,AI,Lead,true,",
                "rep@beta.com,Sarah,BetaTech,Data,Manager,false,"
        ));

        createIfMissing(base.resolve("internships.csv"), List.of(
                "id,title,desc,level,major,openDate,closeDate,status,company,repEmail,slots,visible",
                "INT001,Data Analyst,Analyze datasets,BASIC,CSC,2025-01-01,2025-12-31,APPROVED,ACME,rep@acme.com,5,true",
                "INT002,Hardware Tester,Test circuits,BASIC,EEE,2025-01-01,2025-05-31,APPROVED,BetaTech,rep@beta.com,3,true",
                "INT003,AI Developer,Implement ML models,ADVANCED,CSC,2025-03-01,2025-11-30,PENDING,ACME,rep@acme.com,2,false"
        ));
    }

    private static void createIfMissing(Path file, List<String> lines) throws IOException {
        if (!Files.exists(file)) {
            Files.write(file, lines);
            System.out.println("[Created] " + file.getFileName());
        } else {
            System.out.println("[Exists]  " + file.getFileName());
        }
    }
}