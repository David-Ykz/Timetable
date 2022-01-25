import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
	private static HashMap<Integer, Student> studentList = new HashMap<>();
	private static HashMap<String, Course> courseList = new HashMap<>();
	private static HashMap<Integer, Room> roomList = new HashMap<>();
	private static HashMap<Integer, Teacher> teacherList = new HashMap<>();
	private static HashMap<Integer, Class> classList = new HashMap<>();
	private static HashMap<String, ArrayList<Student>> coursePreferences = new HashMap<>();
	private static HashMap<String, ArrayList<Student>> alternatePreferences = new HashMap<>();
	private static HashSet<String> specialEd = new HashSet<>();
	private static Timetable timetable;
	private static CSVWriter csvWriter = new CSVWriter();
	private static boolean isAlgorithmFinished = false;
	private static boolean runAlgorithm = false;
	private static String dataFileName;

	public static void main(String[] args) {
		MenuFrame frame = new MenuFrame();
		
		while (runAlgorithm == false) {
			try {
				TimeUnit.MILLISECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		dataFileName = frame.getFileName();
		
		Scanner input = new Scanner(System.in);
		CSVReader csvReader = new CSVReader(dataFileName, "Course Master List.csv",
				"Room Utilization.csv", "FakeTeacherList.csv");
		studentList = csvReader.getStudentList();
		courseList = csvReader.getCourseList();
		roomList = csvReader.getRoomList();
		teacherList = csvReader.getTeacherList();

		for (String courseName : courseList.keySet()) {
			coursePreferences.put(courseName, new ArrayList<Student>());
		}
		
		int count = 0;
		for (Student s: studentList.values()) {
			String[] preferences = s.getCourseRequests();
			for (String course: preferences) {
				if (!course.equals("ZREMOT") && !course.contains("GLC"))
				count++;
			}
		}
		
		System.out.println(count);

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
		

		System.out.println(startingClasses.length);

		//--------------Genetic algorithm----------------//
		
		timetable = new Timetable(roomList, teacherList, studentList, courseList, startingClasses);
		Algorithm alg = new Algorithm(200, 0.001, 0.90, 1, 5);
		Population population = alg.initPopulation(timetable);

		// Evaluate population
		alg.sumFitness(population, timetable);

		int generation = 1;

		// Evolution loop
		System.out.println(alg.isMaxFit(population));
		while (!alg.isMaxFit(population) && generation < 5000) {
			// Print fitness
			System.out.println("G" + generation + " Best fitness: " + population.getFittest(0).getFitness());

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

		// Printing out student's timetable
		while (true) {
			System.out.print("Enter a student identification number ('2000' to Exit): ");
			int inputInt = input.nextInt();

			if (inputInt >= 2000) {
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
	
	//add all special edd courses into the special ed courses set
	private static void addSpecialEdCourses() {
		for(Course course: courseList.values()) {
			if((course.getType().equals("Special Education") || course.getType().equals("English as a Second Language")) && coursePreferences.get(course.getCode()).size() != 0) {
				specialEd.add(course.getCode().substring(0,3));    		
			}
		}
	}

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
				} else if (requests < capacity * Const.CUTOFF_THRESHOLD && !course.contains("AMR")) {
				} else {
					// Potentially create multiple classes
					if (requests > capacity) {
						// Create multiple instances of the same class if feasable
						int averageClassSize = requests / (requests/capacity);
//						System.out.println(course + " " + requests + " : " + capacity + " : " + averageClassSize);
						if (averageClassSize >= capacity * Const.CUTOFF_THRESHOLD) {
							if (capacity - (capacity * Const.CUTOFF_THRESHOLD - requests % capacity) >= capacity * Const.CUTOFF_THRESHOLD) {
								for (int i = 0; i < requests / capacity + 1; i++) {
									classList.put(classIndex, new Class(classIndex, course));
									classIndex++;
								}
							} else {
								for (int i = 0; i < requests / capacity; i++) {
									classList.put(classIndex, new Class(classIndex, course));
									classIndex++;
								}
								// CreateAltClasses(coursePreferences.get(course), changes, course);
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
	public static void saveStudentTimetables() {
		if (isAlgorithmFinished) {
			csvWriter.saveStudentData(studentList);
			System.out.println("All student timetables saved");
		}
		else {
			System.out.println("The algorithm isn't done running!");
		}
	}
	
	public static void saveMasterTimetable() {
		if (isAlgorithmFinished) {
			csvWriter.saveMasterTimetable(timetable);
		}
		else {
			System.out.println("The algorithm isn't done running!");
		}
	}
	
	public static void saveTimetableFromNumber(int studentNumber) {
		if (isAlgorithmFinished) {
			Student correctStudent = null;
			for (Student student: studentList.values()) {
				if (student.getStudentNum() == studentNumber) {
					correctStudent = student;
					break;
				}
			}
			csvWriter.createStudentTimetable(correctStudent);
			System.out.println(studentNumber + "'s timetable was saved!");
		}
		else {
			System.out.println("The algorithm isn't done running!");
		}
	}

}