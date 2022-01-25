import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * [Main.java]
 * Driver Method of Program
 * Adds classes into timetable, fills students into classes, mutates timetable using genetic algorithm
 * @author Brian Zhang, Blair Wang, David Ye, Anthony Tecsa, Allen Liu
 * ICS4UE
 * @version 1.0, January 25 2022
 */

public class Main {
	public static final double CUTOFF_THRESHOLD = 0.55;
	public static final int POPULATION_SIZE = 200;
	public static final double MUTATION_RATE = 0.001;
	public static final double CROSSOVER_RATE = 0.90;
	public static final int ELITISM = 1;
	public static final int TOURNAMENT_SIZE = 5;
	public static final int GENERATION_CAP = 5000;
	
	public static final String COURSE_LIST_FILE= "Course Master List.csv";
	public static final String ROOM_LIST_FILE = "Room Utilization.csv";
	public static final String TEACHER_LIST_FILE = "FakeTeacherList.csv";
	
	private static HashMap<Integer, Student> studentList = new HashMap<>();
	private static HashMap<String, Course> courseList = new HashMap<>();
	private static HashMap<Integer, Room> roomList = new HashMap<>();
	private static HashMap<Integer, Teacher> teacherList = new HashMap<>();
	private static HashMap<Integer, Class> classList = new HashMap<>();
	private static HashMap<String, ArrayList<Student>> coursePreferences = new HashMap<>();
	private static HashSet<String> specialEd = new HashSet<>();
	private static CSVWriter csvWriter = new CSVWriter();
	
	private static Timetable timetable;
	private static boolean isAlgorithmFinished = false;
	private static boolean runAlgorithm = false;
	private static String dataFileName;

	public static void main(String[] args) {
		MenuFrame frame = new MenuFrame();
		
		// Waits for file input from the user
		while (runAlgorithm == false) {
			try {
				TimeUnit.MILLISECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		dataFileName = frame.getFileName();
		
		Scanner input = new Scanner(System.in);
		CSVReader csvReader = new CSVReader(dataFileName, COURSE_LIST_FILE, ROOM_LIST_FILE, TEACHER_LIST_FILE);
		studentList = csvReader.getStudentList();
		courseList = csvReader.getCourseList();
		roomList = csvReader.getRoomList();
		teacherList = csvReader.getTeacherList();

		for (String courseName : courseList.keySet()) {
			coursePreferences.put(courseName, new ArrayList<Student>());
		}

		for (Student student : studentList.values()) {
			String[] preferences = student.getCourseRequests();
			for (String course : preferences) {
				if (coursePreferences.containsKey(course)) {
					coursePreferences.get(course).add(student);
				}
			}
		}

		addSpecialEdCourses();
		createClasses();

		Class[] startingClasses = (Class[]) classList.values().toArray(new Class[classList.size()]);

		//--------------Genetic algorithm----------------//
		
		timetable = new Timetable(roomList, teacherList, studentList, courseList, startingClasses);
		Algorithm alg = new Algorithm(POPULATION_SIZE, MUTATION_RATE, CROSSOVER_RATE, ELITISM, TOURNAMENT_SIZE);
		Population population = alg.initPopulation(timetable);

		// Evaluate population
		alg.sumFitness(population, timetable);

		int generation = 1;

		// Evolution loop
		while (!alg.isMaxFit(population) && generation < GENERATION_CAP) {
			// Print fitness
			System.out.println("G: " + generation + " Best fitness: " + population.getFittest(0).getFitness());

			// Apply crossover
			population = alg.crossoverPopulation(population);

			// Apply mutation
			population = alg.mutatePopulation(population, timetable);

			// Reevaluate population
			alg.sumFitness(population, timetable);

			// Increment the current generation
			generation++;
		}
		
		isAlgorithmFinished = true;
		
		
		// Outputting class information to the console
		System.out.println();
		timetable.createClasses(population.getFittest(0));
		timetable.giveStudentsClasses();
		System.out.println("Solution found in " + generation + " generations");
		System.out.println("Final solution fitness: " + population.getFittest(0).getFitness());
		System.out.println("Clashes: " + timetable.calculateConflicts() + "\n");

		Class classes[] = timetable.getClasses();
		int classIndex = 1;
		int totalSlots = 0;

		for (Class bestClass : classes) {
			System.out.println("Class " + classIndex + ":");
			System.out.println("Course: " + timetable.getCourse(bestClass.getCourseId()).getName() + " "
					+ timetable.getCourse(bestClass.getCourseId()).getCourseCode());
			System.out.println("Class size: " + timetable.getClasses()[classIndex - 1].getStudents().size());
			System.out.println("Room: " + timetable.getRoom(bestClass.getRoomId()).getRoomNum());
			System.out.println("Teacher: " + timetable.getTeacher(bestClass.getTeacherId()).getTeacherName());
			System.out.println("Period: " + bestClass.getPeriod());
			System.out.println("Semester: " + bestClass.getSemester());
			System.out.println("-----");
			totalSlots += timetable.getClasses()[classIndex - 1].getStudents().size();
			classIndex++;
		}

		timetable.printConflicts();
		System.out.println("Total slots: " + totalSlots);

		// Printing out individual student timetables to the console
		final int QUIT_INPUT = 2000;
		
		while (true) {
			System.out.print("Enter a student identification number ('0' - '1587') ('2000' to Exit): ");
			int inputInt = input.nextInt();

			if (inputInt >= QUIT_INPUT) {
				break;
			}

			Student selectedStudent = studentList.get(inputInt);

			System.out.println("Name: " + selectedStudent.getName());
			System.out.println("Grade: " + selectedStudent.getGrade());
			System.out.println("Student Number: " + selectedStudent.getStudentNum());
			System.out.println("Gapps Email: " + selectedStudent.getStudentNum() + "@gapps.yrdsb.ca");
			System.out.println("Courses: " + selectedStudent.getCourses().toString());
			System.out.println("Alternates: " + selectedStudent.getAlternates().toString());
			System.out.println();

			HashMap<Integer, Class> studentClasses = selectedStudent.getClasses();

			for (Class cl : studentClasses.values()) {
				if (cl != null) {
					System.out.println("Course: " + timetable.getCourse(cl.getCourseId()).getName() + " "
							+ timetable.getCourse(cl.getCourseId()).getCourseCode());
					System.out
							.println("Class size: " + timetable.getClasses()[cl.getClassId() - 1].getStudents().size());
					System.out.println("Room: " + timetable.getRoom(cl.getRoomId()).getRoomNum());
					System.out.println("Teacher: " + timetable.getTeacher(cl.getTeacherId()).getTeacherName());
					System.out.println("Period: " + cl.getPeriod());
					System.out.println("Semester: " + cl.getSemester());
					System.out.println("-----");
				}
			}
		}
		
		input.close();
	}
	
	// Add all special edd courses into the special ed courses set
	private static void addSpecialEdCourses() {
		for(Course course: courseList.values()) {
			if((course.getType().equals("Special Education") || course.getType().equals("English as a Second Language")) && coursePreferences.get(course.getCode()).size() != 0) {
				specialEd.add(course.getCode().substring(0,3));    		
			}
		}
	}

	// Creates classes based on the amount of people who requested a course
	public static void createClasses() {
		int classIndex = 1;
		HashSet<String> combinedCourses = new HashSet<>();
		for (String course : coursePreferences.keySet()) {
			int capacity = courseList.get(course).getCapacity();
			int requests = coursePreferences.get(course).size();
			if (!course.equals("ZREMOT") && !course.contains("GLC")) {
				if(specialEd.contains(course.substring(0,3)) && !combinedCourses.contains(course.substring(0,3))) {
					classList.put(classIndex, new Class(classIndex, course));
					combinedCourses.add(course.substring(0,3));
					classIndex++;
				} else if (requests < capacity * CUTOFF_THRESHOLD && !course.contains("AMR")) {
				} else {
					// Potentially create multiple classes
					if (requests > capacity) {
						// Create multiple instances of the same class if feasable
						int averageClassSize = requests / (requests/capacity);
						if (averageClassSize >= capacity * CUTOFF_THRESHOLD) {
							// Check if we can create one extra class
							if (capacity - (capacity * CUTOFF_THRESHOLD - requests % capacity) >= capacity * CUTOFF_THRESHOLD) {
								for (int i = 0; i < requests / capacity + 1; i++) {
									classList.put(classIndex, new Class(classIndex, course));
									classIndex++;
								}
							} else {
								for (int i = 0; i < requests / capacity; i++) {
									classList.put(classIndex, new Class(classIndex, course));
									classIndex++;
								}
							}
						}
					} else {
						classList.put(classIndex, new Class(classIndex, course));
						classIndex++;
					}
				}
			}
		}
	}
	
	public static void startAlgorithm() {
		runAlgorithm = true;
	}
	
	// Writing information to .csv files
	public static boolean saveStudentTimetables() {
		if (isAlgorithmFinished) {
			csvWriter.saveStudentData(studentList);
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean saveMasterTimetable() {
		if (isAlgorithmFinished) {
			csvWriter.saveMasterTimetable(timetable);
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean saveTimetableFromNumber(int studentNumber) {
		if (isAlgorithmFinished) {
			Student correctStudent = null;
			for (Student student: studentList.values()) {
				if (student.getStudentNum() == studentNumber) {
					correctStudent = student;
					break;
				}
			}
			csvWriter.createStudentTimetable(correctStudent);
			return true;
		}
		else {
			return false;
		}
	}

}