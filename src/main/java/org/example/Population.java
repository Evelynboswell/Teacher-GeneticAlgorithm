package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Population {
    private ArrayList<Chromosome> chromosomes; // List of potential solutions
    private int populationSize; // Number of chromosomes in the population
    private Random random; // Random instance for operations

    // Constructor to initialize the population with a specified list of teachers
    public Population(int populationSize, ArrayList<Teacher> allTeachers) {
        this.populationSize = populationSize;
        this.chromosomes = new ArrayList<>(populationSize);
        this.random = new Random();

        // Initialize the population with random chromosomes
        initializePopulation(allTeachers);
    }

    // Constructor to initialize a population with an existing list of chromosomes (e.g., offspring)
    public Population(ArrayList<Chromosome> chromosomes) {
        this.chromosomes = new ArrayList<>(chromosomes);
        this.populationSize = chromosomes.size();
        this.random = new Random();
    }

    // Initializes the population with random chromosomes
    private void initializePopulation(ArrayList<Teacher> allTeachers) {
        for (int i = 0; i < populationSize; i++) {
            Chromosome chromosome = new Chromosome(allTeachers);
            chromosome.randomizeAssignments(); // Assign random schools to each teacher
            chromosomes.add(chromosome);
        }
    }

    // Getter for the list of chromosomes
    public ArrayList<Chromosome> getChromosomes() {
        return chromosomes;
    }

    // Returns the chromosome with the highest fitness in the population
    public Chromosome getFittest() {
        return Collections.max(chromosomes, Comparator.comparingDouble(Chromosome::getFitness));
    }

    // Sorts chromosomes by fitness in descending order
    public void sortChromosomesByFitness() {
        chromosomes.sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());
    }

    // Replaces the population with a new list of chromosomes (e.g., next generation)
    public void replacePopulation(ArrayList<Chromosome> newChromosomes) {
        this.chromosomes = new ArrayList<>(newChromosomes);
        this.populationSize = newChromosomes.size();
    }

    // Method to calculate fitness for the entire population
    public void calculateFitnessForPopulation() {
        for (Chromosome chromosome : chromosomes) {
            chromosome.calculateFitness();
        }
    }
}
