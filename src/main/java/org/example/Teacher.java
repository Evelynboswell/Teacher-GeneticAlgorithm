package org.example;

import java.util.List;

public class Teacher {
    private String teacherName;
    private int bestSchoolDistance; // Distance to the best school for this teacher
    private String bestSchoolName;  // Name of the best school for this teacher
    private List<SchoolAssignment> schoolAssignments; // List of possible school assignments

    // Constructor
    public Teacher(String teacherName, List<SchoolAssignment> schoolAssignments) {
        this.teacherName = teacherName;
        this.schoolAssignments = schoolAssignments;
        this.bestSchoolDistance = Integer.MAX_VALUE; // Initialize with a large distance
        this.bestSchoolName = "";
        findBestSchool();
    }

    // Method to find the best school assignment based on distance
    private void findBestSchool() {
        for (SchoolAssignment assignment : schoolAssignments) {
            if (assignment.getDistance() < bestSchoolDistance) {
                bestSchoolDistance = assignment.getDistance();
                bestSchoolName = assignment.getSchoolName();
            }
        }
    }

    // Getters
    public String getTeacherName() {
        return teacherName;
    }

    public int getBestSchoolDistance() {
        return bestSchoolDistance;
    }

    public String getBestSchoolName() {
        return bestSchoolName;
    }

    public List<SchoolAssignment> getSchoolAssignments() {
        return schoolAssignments;
    }

    // Setters for best school name and distance (needed in Chromosome and FitnessCalculator)
    public void setBestSchoolName(String schoolName) {
        this.bestSchoolName = schoolName;
    }

    public void setBestSchoolDistance(int distance) {
        this.bestSchoolDistance = distance;
    }

    public void setSchoolAssignments(List<SchoolAssignment> schoolAssignments) {
        this.schoolAssignments = schoolAssignments;
        findBestSchool(); // Recalculate the best school when assignments are updated
    }

    // Override toString to display teacher details and the best school assignment
    @Override
    public String toString() {
        return "Teacher{" +
                "teacherName='" + teacherName + '\'' +
                ", bestSchoolName='" + bestSchoolName + '\'' +
                ", bestSchoolDistance=" + bestSchoolDistance +
                '}';
    }

    // Inner class to represent possible school assignments with distance
    public static class SchoolAssignment {
        private String schoolName;
        private int distance;

        public SchoolAssignment(String schoolName, int distance) {
            this.schoolName = schoolName;
            this.distance = distance;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public int getDistance() {
            return distance;
        }

        @Override
        public String toString() {
            return "SchoolAssignment{" +
                    "schoolName='" + schoolName + '\'' +
                    ", distance=" + distance +
                    '}';
        }
    }
}
