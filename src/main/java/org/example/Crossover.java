package org.example;

import java.util.ArrayList;
import java.util.Random;

public class Crossover {
    private Random random;

    public Crossover() {
        this.random = new Random();
    }

    /**
     * Performs cycle crossover between two parent chromosomes to produce an offspring.
     *
     * @param parent1 The first parent chromosome.
     * @param parent2 The second parent chromosome.
     * @return The offspring chromosome created from the cycle crossover.
     */
    public Chromosome cycleCrossover(Chromosome parent1, Chromosome parent2) {
        int geneSize = parent1.getTeachers().size();
        ArrayList<Teacher> childTeachers = new ArrayList<>(geneSize);

        for (int i = 0; i < geneSize; i++) {
            childTeachers.add(null);
        }

        boolean[] visited = new boolean[geneSize];

        int start = random.nextInt(geneSize);
        int initialStart = start;

        while (start < geneSize) {
            if (visited[start]) {
                start++;
                continue;
            }

            int current = start;
            do {
                Teacher teacherFromParent1 = parent1.getTeachers().get(current);
                childTeachers.set(current, teacherFromParent1);
                visited[current] = true;

                Teacher correspondingTeacher = parent2.getTeachers().get(current);
                current = parent1.getTeachers().indexOf(correspondingTeacher);

            } while (current != start && !visited[current]);

            start++;
        }

        for (int i = 0; i < geneSize; i++) {
            if (childTeachers.get(i) == null) {
                childTeachers.set(i, parent2.getTeachers().get(i));
            }
        }

        Chromosome child = new Chromosome(childTeachers);
        child.calculateFitness();
        return child;
    }

    /**
     * Creates a mating pool of offspring chromosomes using cycle crossover on pairs from the mating pool.
     *
     * @param matingPool The list of selected chromosomes to be crossed over.
     * @return A new list of chromosomes representing the offspring.
     */
    public ArrayList<Chromosome> createOffspring(ArrayList<Chromosome> matingPool) {
        ArrayList<Chromosome> offspring = new ArrayList<>();

        for (int i = 0; i < matingPool.size() - 1; i += 2) {
            Chromosome parent1 = matingPool.get(i);
            Chromosome parent2 = matingPool.get(i + 1);

            offspring.add(cycleCrossover(parent1, parent2));
        }

        if (matingPool.size() % 2 != 0) {
            offspring.add(matingPool.get(matingPool.size() - 1));
        }

        return offspring;
    }
}
