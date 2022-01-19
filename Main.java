import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Main {
 private static HashMap<Integer, Student> studentList = new HashMap<>();
    private static HashMap<String, Course> courseList = new HashMap<>();
    private static HashMap<Integer, Room> roomList = new HashMap<>();
    private static HashMap<Integer, Teacher> teacherList = new HashMap<>();
    private static HashMap<Integer, Group> groupList = new HashMap<>();

    public static void main(String[] args) {
     CSVReader csvReader = new CSVReader("StudentDataObfuscated.csv", "Course Master List.csv", "Room Utilization.csv", "FakeTeacherList.csv");
        studentList = csvReader.getStudentList();
        courseList = csvReader.getCourseList();
        roomList = csvReader.getRoomList();
        teacherList = csvReader.getTeacherList();

        for (Student student : studentList.values()) {
            addToGroup(student);
        }
        
        
        int total = 0;
        
        for (Group group : groupList.values()) {
            total += group.getGroupSize();
        }
        
        System.out.println("Before: " + total + " Size: " + groupList.size());
        
        
        int movedStudents = 0;
        HashSet<Group> removeGroups = new HashSet<>();
        for (Group group : groupList.values()) {
            String code = group.getCourseCode();
            if (group.getGroupSize() < group.getCap() * Const.CUTOFF_THRESHOLD) {
        //        System.out.println("UNDER --- " + code + " " + group.getGroupSize());
                movedStudents += group.getGroupSize();
                for (int i = 0; i < group.getStudentIds().size(); i++) {
                    int studentId = group.getStudentIds().get(i);
                    Student student = studentList.get(studentId);
                    if (!student.getAlternates().isEmpty()) {
                        if (!student.moveIntoGroup(groupList, removeGroups)) {
                            student.findNextBestCourse(groupList, studentList, removeGroups);
                        }                            
                    } else {
                        student.findNextBestCourse(groupList, studentList, removeGroups);
                    }
                }
                removeGroups.add(group);
            }
        }
        
        groupList.values().removeAll(removeGroups);

        total = 0;
        
        for (Group group : groupList.values()) {
//            System.out.println("Code: " + group.getCourseCode() + " Size: " + group.getGroupSize());
            
            
            total += group.getGroupSize();
        }
        
        System.out.println("After: " + total + " Size: " + groupList.size());

        System.out.println("removed: " + removeGroups.size());


//genetic algorithm 
        Timetable timetable = new Timetable(roomList, teacherList, studentList, courseList, groupList); 
        timetable.printTimetable();
        Algorithm alg = new Algorithm(200, 0.0001, 0.9, 1, 5);
        Population population = alg.initPopulation(timetable);
        
        //evaluate population
        alg.sumFitness(population, timetable);
        
        int generation = 1;
        
        //evolution loop
        //THIS ENTIRE SECTION CURRENTLY DOES NOT WORK. WE FIRST HAVE TO WORK ON FITNESS ALG.
        System.out.println(alg.isMaxFit(population));
        while (alg.isMaxFit(population) == false && generation < 2000) {
            //print fitness
            if (generation % 100 == 0) {
                System.out.println("G" + generation + " Best fitness: " + population.getFittest(0).getFitness());
            }

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
        System.out.println("Clashes: " + timetable.calculateConflicts() + "\n");
        
        Class classes[] = timetable.getClasses();
        int classIndex = 1;
        
        for (Class bestClass : classes) {
            System.out.println("Class " + classIndex + ":");
            System.out.println("Course: " + 
                               timetable.getCourse(bestClass.getCourseId()).getName());
            System.out.println("Group: " + 
                               timetable.getGroup(bestClass.getGroupId()).getId());
            System.out.println("Room: " + 
                               timetable.getRoom(bestClass.getRoomId()).getRoomNum());
            System.out.println("Teacher: " + 
                               timetable.getTeacher(bestClass.getTeacherId()).getTeacherName());
            System.out.println("Class size: " + 
                    			timetable.getGroup(bestClass.getGroupId()).getGroupSize());
            System.out.println("Period: " + 
                               bestClass.getPeriod());
            System.out.println("Semester: " + 
                    bestClass.getSemester());
            System.out.println("-----");
            classIndex++;
        }
        
        timetable.printConflicts();
        
        System.out.println("NO ERRORS");
        
 }
//    public static Timetable initializeTimetable() {
//        Timetable timetable = new Timetable();
//     
//        //iniatialize rooms
//        for (String roomNum: roomList.keySet()) {
//            timetable.addRoom(roomList.get(roomNum).getRoomId(), roomNum, roomList.get(roomNum).getRoomName());
//        }
//        //intialize teachers
//        for (Teacher teacher : teacherList) {
//            timetable.addTeacher(teacher.getTeacherId(), teacher.getTeacherName(), teacher.getQualifications());
//        }        
//        //intialize students and courses
//        for (int i = 0; i < studentList.size(); i++) {
//            Student student = studentList.get(i);
//            addToGroup(student);
//            timetable.addStudent(i, studentList.get(i));            
//        }
//        for (String courseCode : courseList.keySet()) {
//            timetable.addCourse(courseCode, courseList.get(courseCode));
//        }
//        //initialize courses
//        for (Group group : groupList) {
//            timetable.addGroup(group.getGroupId(), group.getCourseCode());
//        }
//        return timetable;
//    }
    
    public static void addToGroup(Student student) {
        for (String course : student.getCourses()) {
            if (!courseList.containsKey(course)) {
                return;
            }
            int groupIndex = availableGroupIndex(course);
            if (groupIndex < 0) {
                groupList.put(groupList.size(), new Group(groupList.size() + 1, course, courseList.get(course).getCap()));
                groupList.get(groupList.size() - 1).addStudent(student.getId());
            } else {
                groupList.get(groupIndex).addStudent(student.getId());
            }
        }
    }

    public static int availableGroupIndex(String courseCode) {
        int maxGroupSize = courseList.get(courseCode).getCap();
        for (int i = 1; i < groupList.size(); i++) {
            Group group = groupList.get(i);
            if (group.getCourseCode().equals(courseCode) && group.getGroupSize() < maxGroupSize) {
                return i;
            }
        }
        return -1;
    }
    
}
