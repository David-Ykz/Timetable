import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    private static HashMap<Integer, Student> studentList = new HashMap<>();
    private static HashMap<String, Course> courseList = new HashMap<>();
    private static HashMap<Integer, Room> roomList = new HashMap<>();
    private static HashMap<Integer, Teacher> teacherList = new HashMap<>();
    private static HashMap<Integer, Group> groupList = new HashMap<>();
    private static ArrayList<Group> originalGroupList = new ArrayList<>();

    public static void main(String[] args) {
     Scanner input = new Scanner(System.in);
     CSVReader csvReader = new CSVReader("StudentDataObfuscated.csv", "Course Master List.csv", "Room Utilization.csv", "FakeTeacherList.csv");
        studentList = csvReader.getStudentList();
        courseList = csvReader.getCourseList();
        roomList = csvReader.getRoomList();
        teacherList = csvReader.getTeacherList();

        for (Student student : studentList.values()) {
            addToGroup(student);
        }
        
        int total = 0;
        
        for (Group group : originalGroupList) {
            total += group.getGroupSize();
        }
        
        System.out.println("Before: " + total + " Size: " + originalGroupList.size());
        
        
        int changes = 0;
        HashSet<Group> removeGroups = new HashSet<>();
        for (Group group : originalGroupList) {
            String code = group.getCourseCode();
            if (group.getGroupSize() < group.getCap() * Const.CUTOFF_THRESHOLD) {
                for (int i = 0; i < group.getStudentIds().size(); i++) {
                 changes++;
                    int studentId = group.getStudentIds().get(i);
                    Student student = studentList.get(studentId);
                    if (!student.getAlternates().isEmpty()) {
                        if (!student.moveIntoGroup(originalGroupList, removeGroups)) {
                            student.findNextBestCourse(originalGroupList, studentList, removeGroups);
                        }                            
                    } else {
                        student.findNextBestCourse(originalGroupList, studentList, removeGroups);
                    }
                }
                removeGroups.add(group);
            }
        }
        
        originalGroupList.removeAll(removeGroups);
        
        //make new groupList
        int groupIndex = 1;
        
        for (Group group : originalGroupList) {
         //give group a new id
         group.setId(groupIndex);
         groupList.put(groupIndex, group);
         groupIndex++;
        }
        
        System.out.println("Percent Success Rate of Class Assignments: " + Math.round((double)(total-changes)/total * 100 * 100.0)/100.00 + "%");
//        System.out.println("sections: " + countSections());
        total = 0;
        
        for (Group group : groupList.values()) {
            total += group.getGroupSize();
        }
        
        System.out.println("After: " + total + " Size: " + groupList.size());

        System.out.println("removed: " + removeGroups.size());
        
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
        
        CSVWriter csvWriter = new CSVWriter();
        csvWriter.saveStudentData(studentList);
        
        
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
    
}