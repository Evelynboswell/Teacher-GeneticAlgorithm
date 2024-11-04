package org.example;

import java.util.ArrayList;
import java.util.Random;

public class Selection {
    private Random random;

    public Selection() {
        this.random = new Random();
    }

    /**
     * Performs roulette wheel selection on a population to choose a chromosome.
     *
     * @param population The population from which to select a chromosome.
     * @return A selected Chromosome based on fitness-proportional probability.
     */
    public Chromosome rouletteWheelSelection(Population population) {
        double totalFitness = 0.0;
        for (Chromosome chromosome : population.getChromosomes()) {
            totalFitness += chromosome.getFitness();
        }

        double randomThreshold = random.nextDouble() * totalFitness;

        double cumulativeFitness = 0.0;
        for (Chromosome chromosome : population.getChromosomes()) {
            cumulativeFitness += chromosome.getFitness();
            if (cumulativeFitness >= randomThreshold) {
                return chromosome;
            }
        }

        return population.getChromosomes().get(population.getChromosomes().size() - 1);
    }

    /**
     * Selects a set of chromosomes from the population to form a mating pool.
     *
     * @param population The population from which to select chromosomes.
     * @param poolSize The number of chromosomes to select for the mating pool.
     * @return An ArrayList of selected Chromosomes for crossover.
     */
    public ArrayList<Chromosome> selectMatingPool(Population population, int poolSize) {
        ArrayList<Chromosome> matingPool = new ArrayList<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            matingPool.add(rouletteWheelSelection(population));
        }
        return matingPool;
    }
}
