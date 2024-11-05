package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Population {
    private ArrayList<Chromosome> chromosomes;
    private int populationSize;
    private Random random;
    private static final double ELITISM_RATE = 0.1; // Retain the top 10% of chromosomes

    public Population(int populationSize, ArrayList<Teacher> allTeachers) {
        this.populationSize = populationSize;
        this.chromosomes = new ArrayList<>(populationSize);
        this.random = new Random();

        initializePopulation(allTeachers);
    }

    public Population(ArrayList<Chromosome> chromosomes) {
        this.chromosomes = new ArrayList<>(chromosomes);
        this.populationSize = chromosomes.size();
        this.random = new Random();
    }

    /**
     * Initializes the population with a diverse set of chromosomes.
     * Ensures that each chromosome starts with different school assignments.
     */
    private void initializePopulation(ArrayList<Teacher> allTeachers) {
        for (int i = 0; i < populationSize; i++) {
            Chromosome chromosome = new Chromosome(allTeachers);
            chromosome.randomizeAssignments();
            chromosome.calculateFitness();
            chromosomes.add(chromosome);
        }
    }

    public ArrayList<Chromosome> getChromosomes() {
        return chromosomes;
    }

    public Chromosome getFittest() {
        return Collections.max(chromosomes, Comparator.comparingDouble(Chromosome::getFitness));
    }

    public void sortChromosomesByFitness() {
        chromosomes.sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());
    }

    /**
     * Replaces the current population with a new population, keeping the top performers (elitism).
     *
     * @param newChromosomes The new population of chromosomes to be added.
     */
    public void replacePopulation(ArrayList<Chromosome> newChromosomes) {
        int eliteCount = (int) (ELITISM_RATE * populationSize);

        sortChromosomesByFitness();
        List<Chromosome> elites = new ArrayList<>(chromosomes.subList(0, eliteCount));

        int neededNewChromosomes = populationSize - eliteCount;
        List<Chromosome> newPopulationSegment;
        if (newChromosomes.size() >= neededNewChromosomes) {
            newPopulationSegment = newChromosomes.subList(0, neededNewChromosomes);
        } else {
            newPopulationSegment = newChromosomes;
        }

        chromosomes = new ArrayList<>(elites);
        chromosomes.addAll(newPopulationSegment);

        while (chromosomes.size() < populationSize) {
            chromosomes.add(newPopulationSegment.get(random.nextInt(newPopulationSegment.size())));
        }
    }


    /**
     * Calculates and updates fitness values for all chromosomes in the population.
     */
    public void calculateFitnessForPopulation() {
        for (Chromosome chromosome : chromosomes) {
            chromosome.calculateFitness();
        }
    }

    /**
     * Optional utility method to measure population diversity based on unique fitness values.
     * Higher diversity indicates a broader search space is being explored.
     *
     * @return The number of unique fitness values in the population.
     */
    public int measureDiversity() {
        Set<Double> uniqueFitnessValues = new HashSet<>();
        for (Chromosome chromosome : chromosomes) {
            uniqueFitnessValues.add(chromosome.getFitness());
        }
        return uniqueFitnessValues.size();
    }
}
