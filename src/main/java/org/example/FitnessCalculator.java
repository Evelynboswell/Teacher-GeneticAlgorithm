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

        // Sum the distances of all teacher-school assignments in the chromosome
        for (Teacher teacher : chromosome.getTeachers()) {
            totalDistance += teacher.getBestSchoolDistance();
        }

        // Fitness is the inverse of the total distance (lower distance means higher fitness)
        double fitness = 1 / (1 + totalDistance); // Adding 1 to prevent division by zero
        chromosome.setFitness(fitness);           // Save the fitness value in the chromosome
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
