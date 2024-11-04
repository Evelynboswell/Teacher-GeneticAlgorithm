package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Population {
    private ArrayList<Chromosome> chromosomes;
    private int populationSize;
    private Random random;

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

    private void initializePopulation(ArrayList<Teacher> allTeachers) {
        for (int i = 0; i < populationSize; i++) {
            Chromosome chromosome = new Chromosome(allTeachers);
            chromosome.randomizeAssignments();
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

    public void replacePopulation(ArrayList<Chromosome> newChromosomes) {
        this.chromosomes = new ArrayList<>(newChromosomes);
        this.populationSize = newChromosomes.size();
    }

    public void calculateFitnessForPopulation() {
        for (Chromosome chromosome : chromosomes) {
            chromosome.calculateFitness();
        }
    }
}
