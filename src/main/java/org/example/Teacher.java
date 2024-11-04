package org.example;

import java.util.List;

public class Teacher {
    private String teacherName;
    private int bestSchoolDistance;
    private String bestSchoolName;
    private List<SchoolAssignment> schoolAssignments;

    public Teacher(String teacherName, List<SchoolAssignment> schoolAssignments) {
        this.teacherName = teacherName;
        this.schoolAssignments = schoolAssignments;
        this.bestSchoolDistance = Integer.MAX_VALUE;
        this.bestSchoolName = "";
        findBestSchool();
    }

    private void findBestSchool() {
        for (SchoolAssignment assignment : schoolAssignments) {
            if (assignment.getDistance() < bestSchoolDistance) {
                bestSchoolDistance = assignment.getDistance();
                bestSchoolName = assignment.getSchoolName();
            }
        }
    }

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

    public void setBestSchoolName(String schoolName) {
        this.bestSchoolName = schoolName;
    }

    public void setBestSchoolDistance(int distance) {
        this.bestSchoolDistance = distance;
    }

    public void setSchoolAssignments(List<SchoolAssignment> schoolAssignments) {
        this.schoolAssignments = schoolAssignments;
        findBestSchool();
    }

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
