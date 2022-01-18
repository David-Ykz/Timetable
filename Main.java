import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    private static HashMap<Integer, Student> studentList = new HashMap<>();
    private static HashMap<String, Course> courseList = new HashMap<>();
    private static HashMap<String, Room> roomList = new HashMap<>();
    private static HashMap<Integer, Teacher> teacherList = new HashMap<>();
    private static HashMap<Integer, Group> groupList = new HashMap<>();
    private static int studentCount = 0;
    private static int courseCount = 0;

    public static void main(String[] args) {
        CSVReader csvReader = new CSVReader("StudentDataObfuscated.csv", "Course Master List.csv", "Room Utilization.csv", "FakeTeacherList.csv");
        studentList = csvReader.getStudentList();
        courseList = csvReader.getCourseList();
        System.out.println(courseList.size());
        roomList = csvReader.getRoomList();
        teacherList = csvReader.getTeacherList();

        for (Student student : studentList.values()) {
            addToGroup(student);
        }
        
        for (Group group : groupList.values()) {
            if (group.getGroupSize() < group.getCap() * Const.CUTOFF_THRESHOLD) {
                for (int i = 0; i < group.getStudentIds().size(); i++) {
                    int studentId = group.getStudentIds().get(i);
                    Student student = studentList.get(studentId);
                    if (!student.getAlternates().isEmpty()) {
                        student.moveIntoGroup(groupList);
                    }
                }
                groupList.remove(group);
            }
        }
        
        
        
        //genetic algorithm 
        Timetable timetable = new Timetable(roomList, teacherList, studentList, courseList, groupList); 
        timetable.printTimetable();
        Algorithm alg = new Algorithm(100, 0.01, 0.9, 2, 5);
        Population population = alg.initPopulation(timetable);
        
        //evaluate population
        alg.sumFitness(population, timetable);
        
        int generation = 1;
        
        //evolution loop
        //THIS ENTIRE SECTION CURRENTLY DOES NOT WORK. WE FIRST HAVE TO WORK ON FITNESS ALG.
        System.out.println(alg.isMaxFit(population));
        while (alg.isMaxFit(population) == false && generation < 20) {
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
        System.out.println("Solution found in " + generation + " generations");
        System.out.println("Final solution fitness: " + population.getFittest(0).getFitness());
        System.out.println("Clashes: " + timetable.calculateConflicts());
        
        Class classes[] = timetable.getClasses();
        int classIndex = 1;
        
//        for (Class bestClass : classes) {
//            System.out.println("Class " + classIndex + ":");
//            System.out.println("Course: " + 
//                               timetable.getCourse(bestClass.getCourseId()).getCourseName());
//            System.out.println("Group: " + 
//                               timetable.getGroup(bestClass.getGroupId()).getGroupId());
//            System.out.println("Room: " + 
//                               timetable.getRoom(bestClass.getRoomId()).getRoomNum());
//            System.out.println("Teacher: " + 
//                               timetable.getTeacher(bestClass.getTeacherId()).getTeacherName());
//            System.out.println("Period: " + 
//                               bestClass.getPeriod());
//            System.out.println("-----");
//            classIndex++;
//        }
//        
        System.out.println("NO ERRORS");
        
    }
    
    
    
    public static void addToGroup(Student student) {
        for (String course : student.getCourses()) {
            if (!courseList.containsKey(course)) {
                return;
            }
            int groupIndex = availableGroupIndex(course);
            if (groupIndex < 0) {
                groupList.put(groupList.size(), new Group(groupList.size() + 1, course, courseList.get(course).getCap()));
            }
            groupList.get(groupIndex).addStudent(student.getId());
        }
    }

    public static int availableGroupIndex(String courseCode) {
        int maxGroupSize = courseList.get(courseCode).getCap();
        for (int i = 0; i < groupList.size(); i++) {
            Group group = groupList.get(i);
            if (group.getCourseCode().equals(courseCode) && group.getGroupSize() < maxGroupSize) {
                return i;
            }
        }
        return -1;
    }
    
}
