package org.example;

import java.util.*;

public class Chromosome {
    private List<Teacher> teachers;
    private double fitness;
    private Random random;
    private static final double SCALING_FACTOR = 1000.0; // Matches FitnessCalculator scaling

    public Chromosome(List<Teacher> allTeachers) {
        this.teachers = new ArrayList<>(allTeachers);
        this.random = new Random();
        this.fitness = -1;
    }

    /**
     * Randomly assigns teachers to schools from available assignments.
     * Ensures diversity in initial assignments to reduce population similarity.
     */
    public void randomizeAssignments() {
        for (Teacher teacher : teachers) {
            List<Teacher.SchoolAssignment> assignments = teacher.getSchoolAssignments();
            if (!assignments.isEmpty()) {
                int randomIndex = random.nextInt(assignments.size());
                Teacher.SchoolAssignment chosenAssignment = assignments.get(randomIndex);
                teacher.setBestSchoolName(chosenAssignment.getSchoolName());
                teacher.setBestSchoolDistance(chosenAssignment.getDistance());
            }
        }
    }

    /**
     * Calculates fitness with a scaling factor for improved differentiation.
     */
    public void calculateFitness() {
        double totalDistance = 0;
        for (Teacher teacher : teachers) {
            totalDistance += teacher.getBestSchoolDistance();
        }
        this.fitness = SCALING_FACTOR / (1 + totalDistance); // Consistent scaling
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

    /**
     * Performs cycle crossover with a random starting point to produce a child chromosome.
     *
     * @param other The second parent chromosome.
     * @return The child chromosome created from the cycle crossover.
     */
    public Chromosome cycleCrossover(Chromosome other) {
        ArrayList<Teacher> childTeachers = new ArrayList<>(Collections.nCopies(teachers.size(), null));
        boolean[] cycleFlags = new boolean[teachers.size()];

        int start = random.nextInt(teachers.size());

        while (start < teachers.size()) {
            if (cycleFlags[start]) {
                start++;
                continue;
            }

            int current = start;
            do {
                childTeachers.set(current, this.teachers.get(current));
                cycleFlags[current] = true;

                Teacher correspondingTeacher = other.teachers.get(current);
                current = teachers.indexOf(correspondingTeacher);
            } while (current != start && !cycleFlags[current]);

            start++;
        }

        for (int i = 0; i < childTeachers.size(); i++) {
            if (childTeachers.get(i) == null) {
                childTeachers.set(i, other.teachers.get(i));
            }
        }

        Chromosome child = new Chromosome(childTeachers);
        child.calculateFitness();
        return child;
    }

    /**
     * Applies insert mutation at the gene level, providing each gene a chance to mutate.
     *
     * @param mutationProbability The probability of mutation for each gene.
     */
    public void insertMutation(double mutationProbability) {
        for (int i = 0; i < teachers.size(); i++) {
            if (random.nextDouble() < mutationProbability) {
                int index1 = i;
                int index2 = random.nextInt(teachers.size());

                while (index1 == index2) {
                    index2 = random.nextInt(teachers.size());
                }

                if (index1 > index2) {
                    int temp = index1;
                    index1 = index2;
                    index2 = temp;
                }

                Teacher teacherToMove = teachers.remove(index2);
                teachers.add(index1 + 1, teacherToMove);
            }
        }
        calculateFitness();
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
