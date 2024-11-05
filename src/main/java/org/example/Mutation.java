package org.example;

import java.util.Random;

public class Mutation {
    private Random random;

    public Mutation() {
        this.random = new Random();
    }

    /**
     * Applies insert mutation to a chromosome with a given mutation probability.
     * This method will attempt to mutate each gene within the chromosome based on
     * the mutation probability, introducing more diversity.
     *
     * @param chromosome The chromosome to mutate.
     * @param mutationProbability The probability of mutation occurring for each gene.
     */
    public void insertMutation(Chromosome chromosome, double mutationProbability) {
        int geneSize = chromosome.getTeachers().size();

        if (geneSize < 2) return;

        for (int i = 0; i < geneSize; i++) {
            if (random.nextDouble() < mutationProbability) {
                int index1 = i;
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
            }
        }

        chromosome.calculateFitness();
    }

    /**
     * Applies insert mutation to all chromosomes in the population with a given probability.
     * Each chromosome may undergo mutations at the gene level based on the mutation probability.
     *
     * @param population The population of chromosomes to mutate.
     * @param mutationProbability The probability of mutation for each gene in each chromosome.
     */
    public void applyMutationToPopulation(Population population, double mutationProbability) {
        for (Chromosome chromosome : population.getChromosomes()) {
            insertMutation(chromosome, mutationProbability);
        }
    }
}
