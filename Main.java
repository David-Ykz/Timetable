import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
	private static HashMap<Integer, Student> studentList = new HashMap<>();
	private static HashMap<String, Course> courseList = new HashMap<>();
	private static HashMap<Integer, Room> roomList = new HashMap<>();
	private static HashMap<Integer, Teacher> teacherList = new HashMap<>();
	private static HashMap<Integer, Group> groupList = new HashMap<>();
	private static HashMap<Integer, Class> classList = new HashMap<>();
	private static ArrayList<Group> originalGroupList = new ArrayList<>();
	private static HashMap<String, ArrayList<Student>> coursePreferences = new HashMap<>();
	private static HashMap<String, ArrayList<Student>> alternatePreferences = new HashMap<>();

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		CSVReader csvReader = new CSVReader("StudentDataObfuscated.csv", "Course Master List.csv", "Room Utilization.csv", "FakeTeacherList.csv");
		studentList = csvReader.getStudentList();
		courseList = csvReader.getCourseList();
		roomList = csvReader.getRoomList();
		teacherList = csvReader.getTeacherList();

		//----- code above has been deleted in this version------\\
		//        System.out.println("removed: " + removeGroups.size());

		createClasses();
		//genetic algorithm 
		Timetable timetable = new Timetable(roomList, teacherList, studentList, courseList, groupList); 
		Algorithm alg = new Algorithm(200, 0.001, 0.9, 4, 5);
		Population population = alg.initPopulation(timetable);

		//evaluate population
		alg.sumFitness(population, timetable);

		int generation = 1;

		//evolution loop
		System.out.println(alg.isMaxFit(population));
		while (alg.isMaxFit(population) == false && generation < 1000) {
			//print fitness
			System.out.println("G" + generation + " Best fitness: " + population.getFittest(0).getFitness());

			//apply crossover
			population = alg.crossoverPopulation(population);

			//apply mutation
			population = alg.mutatePopulation(population, timetable);

			//reevaluate population
			alg.sumFitness(population, timetable);

			//increment the current generation
			generation++;
		}

		System.out.println();
		timetable.createClasses(population.getFittest(0));
		timetable.giveStudentsClasses();
		System.out.println("Solution found in " + generation + " generations");
		System.out.println("Final solution fitness: " + population.getFittest(0).getFitness());
		System.out.println("Clashes: " + timetable.calculateConflicts() + "\n");

		Class classes[] = timetable.getClasses();
		int classIndex = 1;

		for (Class bestClass : classes) {
			System.out.println("Class " + classIndex + ":");
			System.out.println("Course: " + 
					timetable.getCourse(bestClass.getCourseId()).getName() + " " + timetable.getCourse(bestClass.getCourseId()).getCourseCode());
			System.out.println("Class size: " + 
					timetable.getGroup(bestClass.getGroupId()).getGroupSize());
			System.out.println("Room: " + 
					timetable.getRoom(bestClass.getRoomId()).getRoomNum());
			System.out.println("Teacher: " + 
					timetable.getTeacher(bestClass.getTeacherId()).getTeacherName());
			System.out.println("Period: " + 
					bestClass.getPeriod());
			System.out.println("Semester: " + 
					bestClass.getSemester());
			System.out.println("-----");
			classIndex++;
		}

		timetable.printConflicts();

		//printing out student's timetable

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

			for (Class cl: selectedStudent.getClasses()) {
				System.out.println("Course: " + 
						timetable.getCourse(cl.getCourseId()).getName() + " " + timetable.getCourse(cl.getCourseId()).getCourseCode());
				System.out.println("Class size: " + 
						timetable.getGroup(cl.getGroupId()).getGroupSize());
				System.out.println("Room: " + 
						timetable.getRoom(cl.getRoomId()).getRoomNum());
				System.out.println("Teacher: " + 
						timetable.getTeacher(cl.getTeacherId()).getTeacherName());
				System.out.println("Period: " + 
						cl.getPeriod());
				System.out.println("Semester: " + 
						cl.getSemester());
				System.out.println("-----");
			}       
		}

	}

	public static void createClasses() {
		//Link course with group (Hashmap)
		//account for single section courses
		for(String courseName: courseList.keySet()) {
			coursePreferences.put(courseName, new ArrayList<>());
		}

		for(Student student: studentList.values()) {
			String[] preferences = student.getCourseRequests();
			for(String course: preferences) {
				coursePreferences.get(course).add(student);
			}
		}
		
		int classIndex = 1;
		int changes = 0; //use to calculate accuracy
		for(String course: coursePreferences.keySet()) {
			int capacity = courseList.get(course).getCapacity();
			int requests = coursePreferences.get(course).size();
			if(requests < capacity * Const.CUTOFF_THRESHOLD) {
				//createAltClasses(coursePreferences.get(course), changes, course);
			}else {
				//potentially create multiple classes
				if(requests > capacity) {
					//create multiple instances of the same class if feasable
					int remainder = requests % capacity;
					if(remainder < capacity * Const.CUTOFF_THRESHOLD) {
						if(capacity - (capacity * Const.CUTOFF_THRESHOLD - remainder) >= capacity * Const.CUTOFF_THRESHOLD) {
							for(int i = 0; i < requests/capacity + 1; i++) {
								classList.put(classIndex, new Class(classIndex, course));
								classIndex++;
							}
						}else {
							for(int i = 0; i < requests/capacity; i++) {
								classList.put(classIndex, new Class(classIndex, course));
								classIndex++;
							}
							//createAltClasses(coursePreferences.get(course), changes, course);
						}
					}
				}else {
					classList.put(classIndex, new Class(classIndex, course));
					classIndex++;
				}
			}
		}
	}

	public static void createAltClasses(ArrayList<Student> students, int changes, String course) {
		for(int i = coursePreferences.get(course).size()/courseList.get(course).getCapacity() * courseList.get(course).getCapacity() - 1; i < coursePreferences.get(course).size() % courseList.get(course).getCapacity(); i++) {
			Student student = coursePreferences.get(course).get(i);
			if(student.getAlternates().isEmpty()) {
				changes++;
				student.findNextBestCourse(courseList, student, alternatePreferences);
				//check by filling alternates and leftovers (separate method)
			}else {
				//check alternates
				ArrayList<String> alternates = student.getAlternates();
				for(String alternate: alternates) {
					alternatePreferences.get(alternate).add(student);
				}
				//check by filling alternates and leftovers(separate method)
			}
		}
	}

	public static void addToGroup(Student student) {
		for (String course : student.getCourses()) {
			if (!courseList.containsKey(course)) {
				return;
			}
			int groupIndex = availableGroupIndex(course);
			if (groupIndex < 0) {
				originalGroupList.add(new Group(originalGroupList.size() + 1, course, courseList.get(course).getCap()));
				originalGroupList.get(originalGroupList.size() - 1).addStudent(student.getId());
			} else {
				originalGroupList.get(groupIndex).addStudent(student.getId());
			}
		}
	}

	public static int countSections() {
		int total = 0;
		for (Group group: groupList.values()) {
			if (group.getCourseCode().charAt(4) == 'O') {
				System.out.println(group.getCourseCode());
				total += 1;
			}
		}
		return total;
	}

	public static int availableGroupIndex(String courseCode) {
		int maxGroupSize = courseList.get(courseCode).getCap();
		for (int i = 1; i < originalGroupList.size(); i++) {
			Group group = originalGroupList.get(i);
			if (group.getCourseCode().equals(courseCode) && group.getGroupSize() < maxGroupSize) {
				return i;
			}
		}
		return -1;
	}

	//fill in groups for each student that requested
	public static void fillGroups(String course, int groupIndex, int capacity, int remainder, int requests) {
		Group group = new Group(groupIndex, course, capacity);
		while(requests > 0) {
			if(group.isFull() || (group.getGroupSize() == remainder && remainder != 0)) { //create new group if current one is full, or we need to add group for leftover students
				groupList.put(groupIndex, group);
				groupIndex++;
				group = new Group(groupIndex, course, capacity);
			}else {
				group.addStudent(coursePreferences.get(course).get(0).getId());
				coursePreferences.remove(0);
			}
		}
	}
}
