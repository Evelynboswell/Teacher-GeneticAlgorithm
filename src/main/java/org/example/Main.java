package org.example;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // Parameters for the genetic algorithm
        String filePath = "src/main/java/org/example/data_jarak.csv"; // Path to the CSV file with teacher data
        int sampleSize = 1000;            // Number of teachers to sample (reduce data size for testing)
        int populationSize = 50;          // Reduced population size for faster runtime
        double mutationProbability = 0.01; // Lower mutation probability (1%)
        int numGenerations = 300;         // Number of generations

        // Load and sample teacher data
        ArrayList<Teacher> allTeachers = null;
        try {
            allTeachers = Utilities.loadTeachers(filePath);
            allTeachers = sampleData(allTeachers, sampleSize); // Randomly sample a subset of data
            System.out.println("Loaded and sampled " + allTeachers.size() + " teachers from CSV.");
        } catch (IOException e) {
            System.err.println("Failed to load teachers: " + e.getMessage());
            return;
        }

        // Initialize population, selection, crossover, mutation, and fitness calculator
        Population population = new Population(populationSize, allTeachers);
        FitnessCalculator fitnessCalculator = new FitnessCalculator();
        Selection selection = new Selection();
        Crossover crossover = new Crossover();
        Mutation mutation = new Mutation();

        // Initial fitness calculation for the population
        fitnessCalculator.calculateFitnessForPopulation(population);

        // Main genetic algorithm loop
        for (int generation = 0; generation < numGenerations; generation++) {
            System.out.println("Generation " + generation);

            // Selection: Create a mating pool
            ArrayList<Chromosome> matingPool = selection.selectMatingPool(population, populationSize);

            // Crossover: Generate offspring
            ArrayList<Chromosome> offspring = crossover.createOffspring(matingPool);

            // Mutation: Apply mutation to offspring
            mutation.applyMutationToPopulation(new Population(offspring), mutationProbability);

            // Calculate fitness for the new population
            fitnessCalculator.calculateFitnessForPopulation(new Population(offspring));

            // Replace the old population with the new generation
            population.replacePopulation(offspring);

            // Track and output the best chromosome of the generation
            Chromosome fittestChromosome = population.getFittest();
            System.out.println("Best fitness of generation " + generation + ": " + fittestChromosome.getFitness());

            // Optional: Early stopping if an optimal solution is found
            if (fittestChromosome.getFitness() > 0.99) {
                System.out.println("Early stopping as an optimal solution is found.");
                break;
            }
        }

        // Output the best solution found after all generations
        Chromosome bestChromosome = population.getFittest();
        System.out.println("Best solution found:");
        System.out.println(bestChromosome);
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

