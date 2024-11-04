package org.example;

import java.util.*;

public class Chromosome {
    private List<Teacher> teachers;
    private double fitness;
    private Random random;

    public Chromosome(List<Teacher> allTeachers) {
        this.teachers = new ArrayList<>(allTeachers);
        this.random = new Random();
        this.fitness = -1;
    }

    public void randomizeAssignments() {
        Map<String, Teacher.SchoolAssignment> uniqueAssignments = new HashMap<>();

        for (Teacher teacher : teachers) {
            if (!uniqueAssignments.containsKey(teacher.getTeacherName())) {
                List<Teacher.SchoolAssignment> assignments = teacher.getSchoolAssignments();
                if (!assignments.isEmpty()) {
                    int randomIndex = random.nextInt(assignments.size());
                    Teacher.SchoolAssignment chosenAssignment = assignments.get(randomIndex);
                    uniqueAssignments.put(teacher.getTeacherName(), chosenAssignment);
                }
            }
        }

        for (Teacher teacher : teachers) {
            Teacher.SchoolAssignment assignment = uniqueAssignments.get(teacher.getTeacherName());
            if (assignment != null) {
                teacher.setBestSchoolName(assignment.getSchoolName());
                teacher.setBestSchoolDistance(assignment.getDistance());
            }
        }
    }

    public void calculateFitness() {
        double totalDistance = 0;

        for (Teacher teacher : teachers) {
            totalDistance += teacher.getBestSchoolDistance();
        }

        this.fitness = 1 / (1 + totalDistance);
    }

    public double getFitness() {
        if (fitness == -1) {
            calculateFitness();
        }
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public Chromosome cycleCrossover(Chromosome other) {
        ArrayList<Teacher> childTeachers = new ArrayList<>(Collections.nCopies(teachers.size(), null));
        boolean[] cycleFlags = new boolean[teachers.size()];

        int start = 0;
        while (start < teachers.size()) {
            if (cycleFlags[start]) {
                start++;
                continue;
            }

            int current = start;
            while (!cycleFlags[current]) {
                childTeachers.set(current, this.teachers.get(current));
                cycleFlags[current] = true;
                Teacher correspondingTeacher = other.teachers.get(current);
                current = teachers.indexOf(correspondingTeacher);
            }

            for (int i = 0; i < childTeachers.size(); i++) {
                if (childTeachers.get(i) == null) {
                    childTeachers.set(i, other.teachers.get(i));
                }
            }
        }

        Chromosome child = new Chromosome(childTeachers);
        child.calculateFitness();
        return child;
    }

    public void insertMutation(double mutationProbability) {
        if (random.nextDouble() < mutationProbability) {
            int index1 = random.nextInt(teachers.size());
            int index2 = random.nextInt(teachers.size());

            if (index1 > index2) {
                int temp = index1;
                index1 = index2;
                index2 = temp;
            }

            Teacher teacherToMove = teachers.remove(index2);
            teachers.add(index1 + 1, teacherToMove);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Chromosome{fitness=").append(fitness).append(", teachers=[");
        for (Teacher teacher : teachers) {
            sb.append(teacher.getTeacherName()).append(" -> ").append(teacher.getBestSchoolName())
                    .append(" (Distance: ").append(teacher.getBestSchoolDistance()).append("), ");
        }
        sb.append("]}");
        return sb.toString();
    }
}
