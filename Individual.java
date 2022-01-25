package timetableProgram;
/**
 * [Individual.Java]
 * Object of any individual attending a class
 * @author Brian Zhang, Blair Wang
 * ICS4UE
 * @version 1.0, January 25 2022
 */
public class Individual {
	private int[] chromosome;
	private double fitness = -1;

	public Individual(Timetable timetable) {
		int numClasses = timetable.getNumClasses();
		// The length of the chromosome is equal to the number of classes * 4 (number of genes referring to a single class)
		int chromosomeLength = numClasses * 4;

		int newChromosome[] = new int[chromosomeLength];
		int chromosomeIndex = 0;

		// Creating a random chromosome
		for (int i = 0; i < timetable.getClasses().length; i++) {
			// Add random period
			int period = (int) (Math.random() * (4) + 1);
			newChromosome[chromosomeIndex] = period;
			chromosomeIndex++;

			// Add random room
			int roomId = timetable.getRandomRoom().getRoomId();
			newChromosome[chromosomeIndex] = roomId;
			chromosomeIndex++;

			// Add random teacher
			int randomTeacherIndex = (int) (Math.random() * timetable.getTeachers().size());
			newChromosome[chromosomeIndex] = randomTeacherIndex;
			chromosomeIndex++;

			// add random semester
			int randomSemester = (int) (Math.random() * (2) + 1);
			newChromosome[chromosomeIndex] = randomSemester;
			chromosomeIndex++;
		}
		this.chromosome = newChromosome;
	}

	public Individual(int chromosomeLength) {
		int[] newChromosome = new int[chromosomeLength];
		this.chromosome = newChromosome;
	}

	public Individual(int[] chromosome) {
		this.chromosome = chromosome;
	}

	public int[] getChromosome() {
		return this.chromosome;
	}

	public int getChromosomeLength() {
		return this.chromosome.length;
	}

	public int getGene(int offset) {
		return this.chromosome[offset];
	}

	public void setGene(int offset, int gene) {
		this.chromosome[offset] = gene;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double getFitness() {
		return this.fitness;
	}

	public String toString() {
		String output = "";
		for (int gene = 0; gene < this.chromosome.length; gene++) {
			output += this.chromosome[gene] + ",";
		}
		return output;
	}
}
