import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Population {
    private Individual population[];
    private double populationFitness = -1;

    //intializes an empty population
    public Population(int populationSize) {
        this.population = new Individual[populationSize];
    }
 
    //creates a population based off a timetable
    public Population(int populationSize, Timetable timetable) {
        this.population = new Individual[populationSize];
  
        for (int i = 0; i < populationSize; i++) {
            //create individual
            Individual individual = new Individual(timetable);
            //add individual to population
            this.population[i] = individual;
        }
    }
    
    public Population(int populationSize, int chromosomeLength) {
        this.population = new Individual[populationSize];
        
        for (int i = 0; i < populationSize; i++) {
            Individual individual = new Individual(chromosomeLength);
            this.population[i] = individual;
        }
    }
    
    public Individual[] getIndividuals() {
        return this.population;
    }
    
    public Individual getFittest(int offset) {
        //sort by fitness
        Arrays.sort(this.population, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                if (o1.getFitness() > o2.getFitness()) {
                    return -1;
                } else if (o1.getFitness() < o2.getFitness()) {
                    return 1;
                }
                return 0;
            }
        });
        
        return this.population[offset];
    }
    
    public double getPopulationFitness() {
        return this.populationFitness;
    }
    
    public Individual getIndividual(int offset) {
        return population[offset];
    }
    
    public int getSize() {
        return this.population.length;
    }
    
    public void setPopulationFitness(double fitness) {
        this.populationFitness = fitness;
    }
    
    public Individual setIndividual(int offset, Individual individual) {
        return population[offset] = individual;
    }
    
    public void shuffle() {
        Random rnd = new Random();
        for (int i = population.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Individual a = population[index];
            population[index] = population[i];
            population[i] = a;
        }
    }
    
}