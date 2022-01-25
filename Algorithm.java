public class Algorithm {

	private int populationSize;
	private double mutationRate;
	private double crossoverRate;
	private int elitismCount;
	protected int tournamentSize;

	public Algorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount,
			int tournamentSize) {
		this.populationSize = populationSize;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.elitismCount = elitismCount;
		this.tournamentSize = tournamentSize;
	}

	// initializes a new population of possible timetables
	public Population initPopulation(Timetable timetable) {
		Population population = new Population(this.populationSize, timetable);
		return population;
	}

	// checks if fitness is as high as desired (as little conflict as possible)
	public boolean isMaxFit(Population population) {
		return population.getFittest(0).getFitness() == 1.0;
	}

	// calculates an individual's fitness level
	public double calculateFitness(Individual individual, Timetable timetable) {
		// creates new timetable object to use -- cloned from an existing timetable
		Timetable tempTimetable = new Timetable(timetable);
		// creates the timetable based off of individual chromosome
		tempTimetable.createClasses(individual);

		// calculate fitness
		int clashes = tempTimetable.calculateConflicts();
		// fitness is calculated as a fraction of 1 over the number of clashes
		double fitness = 1 / (double) (clashes + 1); 

		individual.setFitness(fitness);

		return fitness;
	}

	// evaluates the sum fitness of the population
	public void sumFitness(Population population, Timetable timetable) {
		double populationFitness = 0;

		// loop over population evaluating Individuals and summing populationfitness
		for (Individual Individual : population.getIndividuals()) {
			populationFitness += this.calculateFitness(Individual, timetable);
		}

		population.setPopulationFitness(populationFitness);
	}

	// using tournament selection, a parent is selected for crossover
	public Individual tournamentSelect(Population population) {
		// create tournament
		Population tournament = new Population(this.tournamentSize);

		// add random Individuals to the tournament
		population.shuffle();

		for (int i = 0; i < this.tournamentSize; i++) {
			Individual tournamentIndividual = population.getIndividual(i);
			tournament.setIndividual(i, tournamentIndividual);
		}

		// return the fittest individual for crossover
		return tournament.getFittest(0);
	}

	// randomly mutates genes in the population
	public Population mutatePopulation(Population population, Timetable timetable) {
		// initializing new population to store mutated population
		Population newPopulation = new Population(this.populationSize);
		// loop over current population by fitness
		for (int i = 0; i < population.getSize(); i++) {
			Individual individual = population.getFittest(i);

			// create random individual for random gene swapping (mutation)
			Individual randIndividual = new Individual(timetable);

			// only mutates individuals which are lower in fitness and only 75% of the time
			if (i > this.elitismCount && 0.75 > Math.random()) {
				// loop over genes in the individual's chromosome
				for (int j = 0; j < individual.getChromosomeLength(); j++) {
					// randomly decides if mutation should occur
					// mutation is more common for genes that refer to rooms
					if ((j + 3 % 4 == 0) && this.mutationRate * 200 > Math.random()) {
						individual.setGene(j, randIndividual.getGene(j));
					} else 
					if (this.mutationRate > Math.random()) {
						// swap for random gene
						individual.setGene(j, randIndividual.getGene(j));
					}
				}
			}
			newPopulation.setIndividual(i, individual);
		}
		return newPopulation;
	}

	// crosses over genes in the population to create new individuals
	public Population crossoverPopulation(Population population) {
		// initializing new population to store crossover population
		Population newPopulation = new Population(this.populationSize);

		// loop over current population by fitness
		for (int i = 0; i < population.getSize(); i++) {
			Individual parent1 = population.getFittest(i);
			
			// randomly decides if crossover should occur
			if (this.crossoverRate > Math.random() && i >= this.elitismCount) {
				Individual child = new Individual(parent1.getChromosomeLength());

				// parent 2 is selected randomly based on a tournament system
				Individual parent2 = tournamentSelect(population);
				
				// loop over genes in the individual's chromosome
				for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
					// takes half of parent 1's genes and half of parent 2's genes to create a child individual
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