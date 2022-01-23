import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Main {
	private static HashMap<Integer, Student> studentList = new HashMap<>();
    private static HashMap<String, Course> courseList = new HashMap<>();
    private static HashMap<Integer, Room> roomList = new HashMap<>();
    private static HashMap<Integer, Teacher> teacherList = new HashMap<>();
    private static HashMap<Integer, Class> classList = new HashMap<>();
    private static HashMap<String, ArrayList<Student>> coursePreferences = new HashMap<>();
    private static HashMap<String, ArrayList<Student>> alternatePreferences = new HashMap<>();


    public static void main(String[] args) {
    	Scanner input = new Scanner(System.in);
    	CSVReader csvReader = new CSVReader("StudentDataObfuscated.csv", "Course Master List.csv", "Room Utilization.csv", "FakeTeacherList.csv");
        studentList = csvReader.getStudentList();
        courseList = csvReader.getCourseList();
        roomList = csvReader.getRoomList();
        teacherList = csvReader.getTeacherList();
        
        for(String courseName: courseList.keySet()) {
			coursePreferences.put(courseName, new ArrayList<Student>());
		}

		for(Student student: studentList.values()) {
			String[] preferences = student.getCourseRequests();
			for(String course: preferences) {
				if (coursePreferences.containsKey(course)) {
					coursePreferences.get(course).add(student);
				}
			}
		}
        
        createClasses();

        
        Class[] startingClasses = (Class[]) classList.values().toArray(new Class[classList.size()]);
        
        System.out.println(startingClasses.length);
//        
//        System.out.println(startingClasses[0].getClassId());
        
        //genetic algorithm 
        Timetable timetable = new Timetable(roomList, teacherList, studentList, courseList, startingClasses); 
        Algorithm alg = new Algorithm(200, 0.0005, 0.90, 1, 5);
        Population population = alg.initPopulation(timetable);
        
        //evaluate population
        alg.sumFitness(population, timetable);
        
        int generation = 1;
        
        //evolution loop
        System.out.println(alg.isMaxFit(population));
        while (!alg.isMaxFit(population) && generation < 3000) {
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
        int totalSlots = 0;
        
        for (Class bestClass : classes) {
            System.out.println("Class " + classIndex + ":");
            System.out.println("Course: " + 
                               timetable.getCourse(bestClass.getCourseId()).getName() + " " + timetable.getCourse(bestClass.getCourseId()).getCourseCode());
            System.out.println("Class size: " +
                               timetable.getClasses()[classIndex - 1].getStudents().size());
            System.out.println("Room: " + 
                               timetable.getRoom(bestClass.getRoomId()).getRoomNum());
            System.out.println("Teacher: " + 
                               timetable.getTeacher(bestClass.getTeacherId()).getTeacherName());
            System.out.println("Period: " + 
                               bestClass.getPeriod());
            System.out.println("Semester: " + 
                    bestClass.getSemester());
            System.out.println("-----");
            totalSlots += timetable.getClasses()[classIndex - 1].getStudents().size();
            classIndex++;
        }
        
        timetable.printConflicts();
        System.out.println("Total slots: " + totalSlots);
        
        
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
	        
	        HashMap<Integer, Class> studentClasses = selectedStudent.getClasses();
	        
	        for (int i = 1; i <= studentClasses.size(); i++) {
	        	Class cl = studentClasses.get(i);
	        	
	        	if (cl != null) {
		            System.out.println("Course: " + 
		                               timetable.getCourse(cl.getCourseId()).getName() + " " + timetable.getCourse(cl.getCourseId()).getCourseCode());
		            System.out.println("Class size: " + 
		            					timetable.getClasses()[cl.getClassId() - 1].getStudents().size());
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
        
        CSVWriter csvWriter = new CSVWriter();
        csvWriter.saveStudentData(studentList);
 }

    public static void createClasses() {
		int classIndex = 1;
		int changes = 0; //use to calculate accuracy
		for(String course: coursePreferences.keySet()) {
			int capacity = courseList.get(course).getCapacity();
			int requests = coursePreferences.get(course).size();
			if(requests < capacity * Const.CUTOFF_THRESHOLD) {
				//createAltClasses(coursePreferences.get(course), changes, course);
			} 
			else {
				//potentially create multiple classes
				if(requests > capacity) {
//					System.out.println(course + " " + requests + " : " + capacity);
					//create multiple instances of the same class if feasable
					System.out.println(course + " " + requests + " : " + capacity);
					int averageClassSize = requests/(requests/capacity);
					if(averageClassSize >= capacity * Const.CUTOFF_THRESHOLD) {
						if(capacity - (capacity * Const.CUTOFF_THRESHOLD - requests%capacity) >= capacity * Const.CUTOFF_THRESHOLD) {
							for(int i = 0; i < requests/capacity + 1; i++) {
//								System.out.println("haa");
								classList.put(classIndex, new Class(classIndex, course));
								classIndex++;
							}
						}else {
							for(int i = 0; i < requests/capacity; i++) {
//								System.out.println("aha");
								classList.put(classIndex, new Class(classIndex, course));
								classIndex++;
							}
							//createAltClasses(coursePreferences.get(course), changes, course);
						}
					}
				}else {
//					System.out.println("aah");
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
    
    
}