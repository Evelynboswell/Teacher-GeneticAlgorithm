package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/java/org/example/data_jarak.csv";
        int sampleSize = 100;
        int populationSize = 50;
        double crossoverProbability = 0.8;
        double mutationProbability = 0.2;
        int numGenerations = 300;

        ArrayList<Teacher> allTeachers;
        try {
            allTeachers = Utilities.loadTeachers(filePath);
            allTeachers = sampleData(allTeachers, sampleSize);
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
        Random random = new Random();

        population.calculateFitnessForPopulation();

        for (int generation = 1; generation <= numGenerations; generation++) {
            System.out.printf("Generation %d:\n", generation);

            ArrayList<Chromosome> matingPool = selection.selectMatingPool(population, populationSize);

            ArrayList<Chromosome> offspring = new ArrayList<>();
            for (int i = 0; i < matingPool.size() - 1; i += 2) {
                Chromosome parent1 = matingPool.get(i);
                Chromosome parent2 = matingPool.get(i + 1);

                if (random.nextDouble() < crossoverProbability) {
                    offspring.add(crossover.cycleCrossover(parent1, parent2));
                } else {
                    offspring.add(new Chromosome(new ArrayList<>(parent1.getTeachers())));
                    offspring.add(new Chromosome(new ArrayList<>(parent2.getTeachers())));
                }
            }

            Population offspringPopulation = new Population(offspring);
            mutation.applyMutationToPopulation(offspringPopulation, mutationProbability);

            offspringPopulation.calculateFitnessForPopulation();

            population.replacePopulation(offspringPopulation.getChromosomes());

            Chromosome fittestChromosome = population.getFittest();
            System.out.printf("  Best Fitness: %.8f\n", fittestChromosome.getFitness());
            for (int i = 0; i < Math.min(5, population.getChromosomes().size()); i++) {
                System.out.printf("Chromosome %d Fitness: %.8f\n", i, population.getChromosomes().get(i).getFitness());
            }
            System.out.printf("Generation %d Diversity: %d unique fitness values\n", generation, population.measureDiversity());
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
