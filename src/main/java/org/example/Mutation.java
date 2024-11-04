package org.example;

import java.util.ArrayList;
import java.util.Random;

public class Mutation {
    private Random random;

    // Constructor
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
        // Perform mutation only if a random value is below the mutation probability
        if (random.nextDouble() < mutationProbability) {
            int geneSize = chromosome.getTeachers().size();

            // Randomly select two distinct positions
            int index1 = random.nextInt(geneSize);
            int index2 = random.nextInt(geneSize);

            // Ensure indices are distinct and ordered for easier insertion
            while (index1 == index2) {
                index2 = random.nextInt(geneSize);
            }

            // Ensure index1 is the smaller index for orderly insertion
            if (index1 > index2) {
                int temp = index1;
                index1 = index2;
                index2 = temp;
            }

            // Perform the insertion: remove teacher at index2 and insert after index1
            Teacher teacherToMove = chromosome.getTeachers().remove(index2);
            chromosome.getTeachers().add(index1 + 1, teacherToMove);

            // Recalculate fitness after mutation
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
