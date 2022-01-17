import java.util.ArrayList;

class Student {
    private String lastName;
    private String firstName;
    private String gender;
    private int studentNum;
    private String gappsAccount;
    private String guardianEmail_1;
    private String guardianEmail_2;
    private int grade;
    private ArrayList<String> courses;
    private int crs;
    private ArrayList<String> alternates;

    Student(String lastName, String firstName, String gender, int studentNum, String gappsAccount, String guardianEmail_1, String guardianEmail_2, int grade, ArrayList<String> courses, int crs, ArrayList<String> alternates) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.gender = gender;
        this.studentNum = studentNum;
        this.gappsAccount = gappsAccount;
        this.guardianEmail_1 = guardianEmail_1;
        this.guardianEmail_2 = guardianEmail_2;
        this.grade = grade;
        this.courses = courses;
        this.crs = crs;
        this.alternates = alternates;
    }

    public void printStudent() {
        System.out.println("Last Name: " + lastName);
        System.out.println("First Name: " + firstName);
        System.out.println("Gender: " + gender);
        System.out.println("Student Number: " + studentNum);
        System.out.println("Gapps Email: " + gappsAccount);
        System.out.println("Guardian Email #1: " + guardianEmail_1);
        System.out.println("Guardian Email #2: " + guardianEmail_2);
        System.out.println("Grade: " + grade);
        System.out.println("Courses: " + courses.toString());
        System.out.println("Crs: " + crs);
        System.out.println("Alternates: " + alternates.toString());
    }



}