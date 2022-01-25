/**
 * [Algorithm.java]
 * Genetic Algorithm to solve timetabling problem
 * @author Brian Zhang
 * ICS4UE
 * @version 1.0, January 25 2022
 */

public class Algorithm {
	private final double MAX_FITNESS = 1.0;
	private final double OVERALL_MUTATION_RATE = 0.75;
	private final int ROOM_GENE_LOCATION_OFFSET = 3;
	private final int ROOM_MUTATION_MULTIPLIER = 70;
	
	private int populationSize;
	private double mutationRate;
	private double crossoverRate;
	private int elitismCount;
	protected int tournamentSize;

	public Algorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount, int tournamentSize) {
		this.populationSize = populationSize;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.elitismCount = elitismCount;
		this.tournamentSize = tournamentSize;
	}

	// Initializes a new population of possible timetables
	public Population initPopulation(Timetable timetable) {
		Population population = new Population(this.populationSize, timetable);
		return population;
	}

	// Checks if fitness is as high as desired (as little conflict as possible)
	public boolean isMaxFit(Population population) {
		return population.getFittest(0).getFitness() == MAX_FITNESS;
	}

	// Calculates an individual's fitness level
	public double calculateFitness(Individual individual, Timetable timetable) {
		// Creates new timetable object to use -- cloned from an existing timetable
		Timetable tempTimetable = new Timetable(timetable);
		// creates the timetable based off of individual chromosome
		tempTimetable.createClasses(individual);

		// Calculate fitness
		int clashes = tempTimetable.calculateConflicts();
		// Fitness is calculated as a fraction of 1 over the number of clashes + 1
		double fitness = 1 / (double) (clashes + 1); 

		individual.setFitness(fitness);

		return fitness;
	}

	// Evaluates the sum fitness of the population
	public void sumFitness(Population population, Timetable timetable) {
		double populationFitness = 0;

		// Loop over population evaluating Individuals and summing populationfitness
		for (Individual Individual : population.getIndividuals()) {
			populationFitness += this.calculateFitness(Individual, timetable);
		}

		population.setPopulationFitness(populationFitness);
	}

	// Using tournament selection, a parent is selected for crossover
	public Individual tournamentSelect(Population population) {
		// Create tournament
		Population tournament = new Population(this.tournamentSize);

		// Add random Individuals to the tournament
		population.shuffle();

		for (int i = 0; i < this.tournamentSize; i++) {
			Individual tournamentIndividual = population.getIndividual(i);
			tournament.setIndividual(i, tournamentIndividual);
		}

		// Return the fittest individual for crossover
		return tournament.getFittest(0);
	}

	// Randomly mutates genes in the population
	public Population mutatePopulation(Population population, Timetable timetable) {
		// Initializing new population to store mutated population
		Population newPopulation = new Population(this.populationSize);
		// Loop over current population by fitness
		for (int i = 0; i < population.getSize(); i++) {
			Individual individual = population.getFittest(i);

			// Create random individual for random gene swapping (mutation)
			Individual randIndividual = new Individual(timetable);

			// Only mutates individuals which are lower in fitness and only 80% of the time
			if (i > this.elitismCount && OVERALL_MUTATION_RATE > Math.random()) {
				// Loop over genes in the individual's chromosome
				for (int j = 0; j < individual.getChromosomeLength(); j++) {
					// Randomly decides if mutation should occur
					// Mutation is more common for genes that refer to rooms
					if ((j + ROOM_GENE_LOCATION_OFFSET % Const.GENE_LENGTH == 0) && this.mutationRate * ROOM_MUTATION_MULTIPLIER  > Math.random()) {
						individual.setGene(j, randIndividual.getGene(j));
					} else if (this.mutationRate > Math.random()) {
						// Swap for random gene
						individual.setGene(j, randIndividual.getGene(j));
					}
				}
			}
			newPopulation.setIndividual(i, individual);
		}
		return newPopulation;
	}

	// Crosses over genes in the population to create new individuals
	public Population crossoverPopulation(Population population) {
		// Initializing new population to store crossover population
		Population newPopulation = new Population(this.populationSize);

		// Loop over current population by fitness
		for (int i = 0; i < population.getSize(); i++) {
			Individual parent1 = population.getFittest(i);
			
			// Randomly decides if crossover should occur
			if (this.crossoverRate > Math.random() && i >= this.elitismCount) {
				Individual child = new Individual(parent1.getChromosomeLength());

				// Parent 2 is selected randomly based on a tournament system
				Individual parent2 = tournamentSelect(population);
				
				// Loop over genes in the individual's chromosome
				for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
					if (0.5 > Math.random()) {
						child.setGene(geneIndex, parent1.getGene(geneIndex));
					} else {
						child.setGene(geneIndex, parent2.getGene(geneIndex));
					}
				}
				newPopulation.setIndividual(i, child);
			} else {
				newPopulation.setIndividual(i, parent1);
			}
		}
		return newPopulation;
	}
}