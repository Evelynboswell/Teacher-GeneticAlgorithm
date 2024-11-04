package org.example;

import java.util.ArrayList;
import java.util.Random;

public class Mutation {
    private Random random;

    public Mutation() {
        this.random = new Random();
    }

    /**
     * Applies insert mutation to a chromosome with a given mutation probability.
     *
     * @param chromosome The chromosome to mutate.
     * @param mutationProbability The probability of mutation occurring.
     */
    public void insertMutation(Chromosome chromosome, double mutationProbability) {
        if (random.nextDouble() < mutationProbability) {
            int geneSize = chromosome.getTeachers().size();

            int index1 = random.nextInt(geneSize);
            int index2 = random.nextInt(geneSize);

            while (index1 == index2) {
                index2 = random.nextInt(geneSize);
            }

            if (index1 > index2) {
                int temp = index1;
                index1 = index2;
                index2 = temp;
            }

            Teacher teacherToMove = chromosome.getTeachers().remove(index2);
            chromosome.getTeachers().add(index1 + 1, teacherToMove);

            chromosome.calculateFitness();
        }
    }

    /**
     * Applies insert mutation to all chromosomes in the population with a given probability.
     *
     * @param population The population of chromosomes to mutate.
     * @param mutationProbability The probability of mutation for each chromosome.
     */
    public void applyMutationToPopulation(Population population, double mutationProbability) {
        for (Chromosome chromosome : population.getChromosomes()) {
            insertMutation(chromosome, mutationProbability);
        }
    }
}
