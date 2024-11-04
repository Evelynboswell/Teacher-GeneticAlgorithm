package org.example;

public class FitnessCalculator {

    /**
     * Calculates the fitness of a chromosome based on the cumulative distance to assigned schools.
     * The fitness is the inverse of the total distance to prioritize closer assignments.
     *
     * @param chromosome The chromosome whose fitness is to be calculated.
     * @return The fitness value of the chromosome.
     */
    public double calculateFitness(Chromosome chromosome) {
        double totalDistance = 0;

        for (Teacher teacher : chromosome.getTeachers()) {
            totalDistance += teacher.getBestSchoolDistance();
        }

        double fitness = 1 / (1 + totalDistance);
        chromosome.setFitness(fitness);
        return fitness;
    }

    /**
     * Calculates and assigns fitness values for all chromosomes in a population.
     *
     * @param population The population of chromosomes whose fitnesses are to be calculated.
     */
    public void calculateFitnessForPopulation(Population population) {
        for (Chromosome chromosome : population.getChromosomes()) {
            calculateFitness(chromosome);
        }
    }
}
