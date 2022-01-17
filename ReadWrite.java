import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadWrite {
    private static ArrayList<Student> studentList = new ArrayList<>();

        public static void main(String[] args) {
            createStudent("Waldo ,Mendoza,175003272,10,BBI2O1,CHC2D1,CIVCAR,ENG2D1,ESLEO1,FSF2D1,MPM2D1,PPL2O8,SNC2D1,ZLUN21,11");
            Course course = createCourse("ADA1O1,Dramatic Arts,9,Open,1,[],[],[],28");
            course.printInfo();

        }

        public static void readFile() throws Exception {
            File studentData = new File("StudentCourseRequests.csv");
            Scanner in = new Scanner(studentData);
            while (in.hasNext()) {
                studentList.add(createStudent(in.nextLine()));
            }
        }

        public static Student createStudent(String studentInfo) {
            String[] message = studentInfo.split(",", -1);

            System.out.println(message.toString());
            String lastName = studentInfo.substring(0, studentInfo.indexOf(","));
            studentInfo = studentInfo.substring(studentInfo.indexOf(",") + 1);
            String firstName = studentInfo.substring(0, studentInfo.indexOf(","));
            studentInfo = studentInfo.substring(studentInfo.indexOf(",") + 1);
            String gender = studentInfo.substring(studentInfo.indexOf(",") + 1);
            studentInfo = studentInfo.substring(studentInfo.indexOf(",") + 1);
            int studentNum = Integer.parseInt(studentInfo.substring(0, studentInfo.indexOf(",")));
            studentInfo = studentInfo.substring(studentInfo.indexOf(",") + 1);
            String gappsAccount = studentInfo.substring(studentInfo.indexOf(",") + 1);
            studentInfo = studentInfo.substring(studentInfo.indexOf(",") + 1);
            String guardianEmail_1 = studentInfo.substring(studentInfo.indexOf(",") + 1);
            studentInfo = studentInfo.substring(studentInfo.indexOf(",") + 1);
            String guardianEmail_2 = studentInfo.substring(studentInfo.indexOf(",") + 1);
            studentInfo = studentInfo.substring(studentInfo.indexOf(",") + 1);
            int grade = Integer.parseInt(studentInfo.substring(0, studentInfo.indexOf(",")));
            studentInfo = studentInfo.substring(studentInfo.indexOf(",") + 1);
            ArrayList<String> courses = new ArrayList<>();
            for (int i = 0; i < 10; i++) { // Hardcoded
                String course = studentInfo.substring(0, studentInfo.indexOf(","));
                courses.add(course);
                studentInfo = studentInfo.substring(studentInfo.indexOf(",") + 1);
                studentInfo = studentInfo.substring(studentInfo.indexOf(",") + 1);
                studentInfo = studentInfo.substring(studentInfo.indexOf(",") + 1);
            }
            int crs = Integer.parseInt(studentInfo);
            studentInfo = studentInfo.substring(studentInfo.indexOf(",") + 1);
            ArrayList<String> alternates = new ArrayList<>();
            for (int i = 0; i < 2; i++) { // Hardcoded
                String course = studentInfo.substring(0, studentInfo.indexOf(","));
                alternates.add(course);
                studentInfo = studentInfo.substring(studentInfo.indexOf(",") + 1);
                studentInfo = studentInfo.substring(studentInfo.indexOf(",") + 1);
                studentInfo = studentInfo.substring(studentInfo.indexOf(",") + 1);
            }
            Student student = new Student(lastName, firstName, gender, studentNum, gappsAccount, guardianEmail_1, guardianEmail_2, grade, courses, crs, alternates);
            return student;
        }

        public static Course createCourse(String courseInfo) {
            //Course Code,Course Title,Grade,Type,Credits,Prequisites,Corequisites,Special,Caps (Per Class)
            String code = courseInfo.substring(0, courseInfo.indexOf(","));
            courseInfo = courseInfo.substring(courseInfo.indexOf(",") + 1);
            String title = courseInfo.substring(0, courseInfo.indexOf(","));
            courseInfo = courseInfo.substring(courseInfo.indexOf(",") + 1);
            int grade = Integer.parseInt(courseInfo.substring(0, courseInfo.indexOf(",")));
            courseInfo = courseInfo.substring(courseInfo.indexOf(",") + 1);
            String type = courseInfo.substring(0, courseInfo.indexOf(","));
            courseInfo = courseInfo.substring(courseInfo.indexOf(",") + 1);
            int credits = Integer.parseInt(courseInfo.substring(0, courseInfo.indexOf(",")));
            courseInfo = courseInfo.substring(courseInfo.indexOf(",") + 1);
            String prerequisiteString = courseInfo.substring(0, courseInfo.indexOf("]") + 1);
            ArrayList<String> prerequisites = new ArrayList<>();
            if (prerequisiteString != "[]") {
                prerequisiteString = prerequisiteString.substring(1, prerequisiteString.length() - 1);
                while (prerequisiteString.contains(",")) {
                    prerequisites.add(prerequisiteString.substring(0, prerequisiteString.indexOf(",")));
                    prerequisiteString = prerequisiteString.substring(prerequisiteString.indexOf(",") + 1);
                }
                prerequisites.add(prerequisiteString);
            }
            courseInfo = courseInfo.substring(courseInfo.indexOf("]") + 2);

            // Probably doesn't work
            String corequisiteString = courseInfo.substring(0, courseInfo.indexOf("]") + 1);
            ArrayList<String> corequisites = new ArrayList<>();
            if (corequisiteString != "[]") {
                corequisiteString = corequisiteString.substring(1, corequisiteString.length() - 1);
                while (corequisiteString.contains(",")) {
                    corequisites.add(corequisiteString.substring(0, corequisiteString.indexOf(",")));
                    corequisiteString = corequisiteString.substring(corequisiteString.indexOf(",") + 1);
                }
                corequisites.add(corequisiteString);
            }
            courseInfo = courseInfo.substring(courseInfo.indexOf("]") + 2);

            String special = courseInfo.substring(0, courseInfo.indexOf(","));
            courseInfo = courseInfo.substring(courseInfo.indexOf(",") + 1);
            if (special.equals("[]")) {
                special = "";
            }

            int cap = Integer.parseInt(courseInfo);

            Course course = new Course(code, title, grade, type, credits, prerequisites, corequisites, special, cap);
            return course;
        }
    }





}
