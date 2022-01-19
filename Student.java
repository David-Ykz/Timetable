import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class Student {
    private int studentId;
    private String name;
    private String gender;
    private int studentNum;
    private String gappsAccount;
    private String guardianEmail_1;
    private String guardianEmail_2;
    private int grade;
    private ArrayList<String> courses;
    private int totalCourses;
    private ArrayList<String> alternates;

    Student(int studentId, String name, String gender, int studentNum, String gappsAccount, String guardianEmail_1, String guardianEmail_2, int grade, ArrayList<String> courses, int totalCourses, ArrayList<String> alternates) {
        this.studentId = studentId;
        this.name = name;
        this.gender = gender;
        this.studentNum = studentNum;
        this.gappsAccount = gappsAccount;
        this.guardianEmail_1 = guardianEmail_1;
        this.guardianEmail_2 = guardianEmail_2;
        this.grade = grade;
        this.courses = courses;
        this.totalCourses = totalCourses;
        this.alternates = alternates;
    }
    
    public void printInfo() {
        System.out.println("Name: " + name);
        System.out.println("Gender: " + gender);
        System.out.println("Student Number: " + studentNum);
        System.out.println("Gapps Email: " + gappsAccount);
        System.out.println("Guardian Email #1: " + guardianEmail_1);
        System.out.println("Guardian Email #2: " + guardianEmail_2);
        System.out.println("Grade: " + grade);
        System.out.println("Courses: " + courses.toString());
        System.out.println("Total Courses: " + totalCourses);
        System.out.println("Alternates: " + alternates.toString());
    }

    public void addTimetableInfo(){
     //complete once timetable class has been figured out
     //loop through timetable and module, and place student in a random class
     //calculate clashes and fit score through timetable class
    }

    
    
    public int getId() {
        return this.studentId;
    }
    
    public String[] getCourseRequests() {
        return (String[]) courses.toArray();
    }
    
    public ArrayList<String> getCourses() {
        return this.courses;
    }
    
    public ArrayList<String> getAlternates() {
        return this.alternates;
    }
    
    public int getStudentId() {
        return this.studentId;
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    public boolean moveIntoGroup(HashMap<Integer, Group> groupList, HashSet<Group> removeGroups) {
        for (Group group : groupList.values()) {
            if (alternates.contains(group.getCourseCode()) && !group.isFull() && !removeGroups.contains(group)) {
                group.addStudent(studentId);
                alternates.remove(group.getCourseCode());
                return true;
            }
        }
        return false;
    }
    
    public void findNextBestCourse(HashMap<Integer, Group> groupList, HashMap<Integer, Student> studentList, HashSet<Group> removeGroups) {
        for (Group group : groupList.values()) {
            if (group.findGroupGrade(studentList) == this.grade && !group.isFull() && !removeGroups.contains(group)) {
                group.addStudent(studentId);
                return;
            }
        }
    }
}