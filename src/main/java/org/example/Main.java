package org.example;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        String filePath = "src/main/java/org/example/data_jarak.csv";
        int sampleSize = 50;
        int populationSize = 50;
        double mutationProbability = 0.01;
        int numGenerations = 300;

        ArrayList<Teacher> allTeachers = null;
        try {
            allTeachers = Utilities.loadTeachers(filePath);
            allTeachers = sampleData(allTeachers, sampleSize); // Randomly sample a subset of data
            System.out.printf("Loaded and sampled %d teachers from CSV.\n", allTeachers.size());
        } catch (IOException e) {
            System.err.println("Failed to load teachers: " + e.getMessage());
            return;
        }

        Population population = new Population(populationSize, allTeachers);
        FitnessCalculator fitnessCalculator = new FitnessCalculator();
        Selection selection = new Selection();
        Crossover crossover = new Crossover();
        Mutation mutation = new Mutation();

        fitnessCalculator.calculateFitnessForPopulation(population);

        for (int generation = 1; generation <= numGenerations; generation++) {
            System.out.printf("Generation %d:\n", generation);

            ArrayList<Chromosome> matingPool = selection.selectMatingPool(population, populationSize);

            ArrayList<Chromosome> offspring = crossover.createOffspring(matingPool);

            mutation.applyMutationToPopulation(new Population(offspring), mutationProbability);

            fitnessCalculator.calculateFitnessForPopulation(new Population(offspring));

            population.replacePopulation(offspring);

            Chromosome fittestChromosome = population.getFittest();
            System.out.printf("  Best Fitness: %.8f\n", fittestChromosome.getFitness());

            if (fittestChromosome.getFitness() > 0.99) {
                System.out.println("  Optimal solution found. Stopping early.");
                break;
            }
        }

        Chromosome bestChromosome = population.getFittest();
        displayBestSolution(bestChromosome);
    }

    /**
     * Displays the best solution in a clear, readable format.
     *
     * @param bestChromosome The chromosome representing the best solution.
     */
    private static void displayBestSolution(Chromosome bestChromosome) {
        System.out.println("\nBest Solution Found:");
        System.out.printf("  Fitness: %.8f\n", bestChromosome.getFitness());
        System.out.println("  Teacher Assignments:");

        for (Teacher teacher : bestChromosome.getTeachers()) {
            System.out.printf("    - %s assigned to %s (Distance: %d)\n",
                    teacher.getTeacherName(),
                    teacher.getBestSchoolName(),
                    teacher.getBestSchoolDistance());
        }
    }

    /**
     * Randomly samples a subset of the teacher data for testing purposes.
     *
     * @param allTeachers List of all teachers loaded from the CSV.
     * @param sampleSize The number of teachers to include in the sample.
     * @return A sampled list of teachers.
     */
    public static ArrayList<Teacher> sampleData(ArrayList<Teacher> allTeachers, int sampleSize) {
        ArrayList<Teacher> sampledTeachers = new ArrayList<>(allTeachers);
        java.util.Collections.shuffle(sampledTeachers);
        return new ArrayList<>(sampledTeachers.subList(0, Math.min(sampleSize, sampledTeachers.size())));
    }
}
