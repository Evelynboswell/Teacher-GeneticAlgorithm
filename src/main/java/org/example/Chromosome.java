package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Chromosome {
    private List<Teacher> teachers; // List of teachers with assigned schools
    private double fitness;         // Fitness score of the chromosome
    private Random random;          // Random instance for mutation and initialization

    // Constructor to initialize a chromosome with a list of teachers
    public Chromosome(ArrayList<Teacher> allTeachers) {
        this.teachers = new ArrayList<>(allTeachers);
        this.random = new Random();
        this.fitness = -1; // Fitness will be calculated lazily
    }

    // Randomly assigns schools to each teacher for initial population setup
    public void randomizeAssignments() {
        for (Teacher teacher : teachers) {
            List<Teacher.SchoolAssignment> assignments = teacher.getSchoolAssignments();
            if (!assignments.isEmpty()) {
                int randomIndex = random.nextInt(assignments.size());
                Teacher.SchoolAssignment assignment = assignments.get(randomIndex);
                teacher.setBestSchoolName(assignment.getSchoolName());
                teacher.setBestSchoolDistance(assignment.getDistance());
            }
        }
    }

    // Calculates the fitness score for the chromosome based on total distance to assigned schools
    public void calculateFitness() {
        double totalDistance = 0;

        // Sum the best school distance for each teacher in this chromosome
        for (Teacher teacher : teachers) {
            totalDistance += teacher.getBestSchoolDistance();
        }

        // Fitness is the inverse of the total distance (lower distance means higher fitness)
        this.fitness = 1 / (1 + totalDistance); // Adding 1 to prevent division by zero
    }

    // Getter for fitness
    public double getFitness() {
        if (fitness == -1) { // Lazy calculation
            calculateFitness();
        }
        return fitness;
    }

    // Setter for fitness (needed in FitnessCalculator)
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    // Getter for the list of teachers (needed in Crossover and Mutation)
    public List<Teacher> getTeachers() {
        return teachers;
    }

    // Method to perform crossover with another parent chromosome
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

            // Swap the cycles for the remaining teachers
            for (int i = 0; i < childTeachers.size(); i++) {
                if (childTeachers.get(i) == null) {
                    childTeachers.set(i, other.teachers.get(i));
                }
            }
        }

        Chromosome child = new Chromosome(childTeachers);
        child.calculateFitness(); // Calculate fitness for the new child
        return child;
    }

    // Method to perform insert mutation on the chromosome
    public void insertMutation(double mutationProbability) {
        if (random.nextDouble() < mutationProbability) {
            int index1 = random.nextInt(teachers.size());
            int index2 = random.nextInt(teachers.size());

            // Ensure index1 is less than index2 for orderly insertion
            if (index1 > index2) {
                int temp = index1;
                index1 = index2;
                index2 = temp;
            }

            Teacher teacherToMove = teachers.remove(index2);
            teachers.add(index1 + 1, teacherToMove);
        }
    }

    // Override toString to display chromosome details
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
