package org.example;

import java.util.ArrayList;

public class Crossover {

    /**
     * Performs cycle crossover between two parent chromosomes to produce an offspring.
     *
     * @param parent1 The first parent chromosome.
     * @param parent2 The second parent chromosome.
     * @return The offspring chromosome created from the cycle crossover.
     */
    public Chromosome cycleCrossover(Chromosome parent1, Chromosome parent2) {
        ArrayList<Teacher> childTeachers = new ArrayList<>(parent1.getTeachers().size());

        // Initialize the child's teachers list with null values
        for (int i = 0; i < parent1.getTeachers().size(); i++) {
            childTeachers.add(null);
        }

        boolean[] visited = new boolean[parent1.getTeachers().size()]; // Track visited indices

        // Start cycle crossover from the first unvisited index
        int start = 0;
        while (start < parent1.getTeachers().size()) {
            if (visited[start]) {
                start++;
                continue;
            }

            int current = start;
            do {
                // Assign the teacher and school from parent1 to the child in this cycle
                Teacher teacherFromParent1 = parent1.getTeachers().get(current);
                childTeachers.set(current, teacherFromParent1);
                visited[current] = true;

                // Get the corresponding teacher in parent2 and find its index in parent1
                Teacher correspondingTeacher = parent2.getTeachers().get(current);
                current = parent1.getTeachers().indexOf(correspondingTeacher);

            } while (current != start && !visited[current]); // Continue the cycle

            // Fill in the remaining genes from parent2 for positions not set in the cycle
            for (int i = 0; i < childTeachers.size(); i++) {
                if (childTeachers.get(i) == null) {
                    childTeachers.set(i, parent2.getTeachers().get(i));
                }
            }
        }

        Chromosome child = new Chromosome(childTeachers);
        child.calculateFitness(); // Calculate fitness for the new child
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

        // Perform cycle crossover on each pair of chromosomes in the mating pool
        for (int i = 0; i < matingPool.size() - 1; i += 2) {
            Chromosome parent1 = matingPool.get(i);
            Chromosome parent2 = matingPool.get(i + 1);

            // Generate offspring using cycle crossover and add to offspring list
            offspring.add(cycleCrossover(parent1, parent2));
        }

        // Handle odd-sized mating pool by cloning the last chromosome if necessary
        if (matingPool.size() % 2 != 0) {
            offspring.add(matingPool.get(matingPool.size() - 1));
        }

        return offspring;
    }
}
