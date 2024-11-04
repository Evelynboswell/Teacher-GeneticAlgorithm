package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utilities {

    /**
     * Loads teachers and their school assignments from a CSV file.
     *
     * @param filePath The path to the CSV file containing teacher and school data.
     * @return An ArrayList of Teacher objects loaded from the CSV file.
     * @throws IOException if an error occurs while reading the file.
     */
    public static ArrayList<Teacher> loadTeachers(String filePath) throws IOException {
        ArrayList<Teacher> teachers = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] values;
            boolean isFirstLine = true;

            // Read each line from the CSV
            while ((values = csvReader.readNext()) != null) {
                // Skip the header line if present
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Parse line to Teacher object
                Teacher teacher = parseLineToTeacher(values);
                if (teacher != null) {
                    teachers.add(teacher);
                }
            }
        } catch (CsvValidationException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw e;
        }

        return teachers;
    }

    /**
     * Parses an array of CSV values into a Teacher object with potential school assignments.
     *
     * @param values The array of strings representing columns for a teacher and school assignments.
     * @return A Teacher object if parsing is successful, otherwise null.
     */
    private static Teacher parseLineToTeacher(String[] values) {
        try {
            // Assuming CSV columns: NO, NAMA SEKOLAH, NAMA GURU, JARAK
            String schoolName = values[1].trim();
            String teacherName = values[2].trim();

            // Parse distance to create a school assignment
            String distanceStr = values[3].trim().replaceAll("[^\\d]", ""); // Remove non-numeric characters
            int distance = Integer.parseInt(distanceStr);

            // Create a list for school assignments (one assignment per line in this simplified example)
            List<Teacher.SchoolAssignment> schoolAssignments = new ArrayList<>();
            schoolAssignments.add(new Teacher.SchoolAssignment(schoolName, distance));

            // Create and return a Teacher object with this assignment
            return new Teacher(teacherName, schoolAssignments);
        } catch (Exception e) {
            System.err.println("Error parsing line: " + String.join(",", values) + " - " + e.getMessage());
            return null; // Return null if parsing fails
        }
    }
}
