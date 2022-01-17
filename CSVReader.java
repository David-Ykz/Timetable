import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * [CSVReader.java]
 * This class reads and stores student and course information
 * 
 * @author Blair Wang
 * @author Brian Zhang
 * @author David Ye
 * @author Anthony Tecsa
 * @author Allen Liu
 * @version Jan 15 2022
 */

class CSVReader {
    private ArrayList<Student> studentList = new ArrayList<>();
    private HashMap<String, Course> courseList = new HashMap<>();
    private HashMap<String, Room> roomList = new HashMap<>();
    private ArrayList<Teacher> teacherList = new ArrayList<>();
    
    CSVReader(String studentDataFileName, String courseDataFileName, String roomDataFileName, String teacherDataFileName) {
        try {
            // Reads student data
            File studentData = new File(studentDataFileName);
            Scanner studentDataScanner = new Scanner(studentData);
            studentDataScanner.nextLine();
            while (studentDataScanner.hasNext()) {
                studentList.add(createStudent(parseString(studentDataScanner.nextLine())));
            }
            // Reads course data
            File courseData = new File(courseDataFileName);
            Scanner courseDataScanner = new Scanner(courseData);
            courseDataScanner.nextLine();
            while (courseDataScanner.hasNext()) {
                String message = courseDataScanner.nextLine();
                String courseCode = message.substring(0, message.indexOf(","));
                if (numOccurrences("]", message) <= 3 && !courseCode.contains("PV")) {
                    courseList.put(courseCode, createCourse(parseString(message)));
                }
            }
            // Reads room data
            File roomData = new File(roomDataFileName);
            Scanner roomDataScanner = new Scanner(roomData);
            while (roomDataScanner.hasNext()) {
                ArrayList<String> message = parseString(roomDataScanner.nextLine());
                roomList.put(message.get(0), createRoom(message));
            }
            // Reads teacher data
            File teacherData = new File(teacherDataFileName);
            Scanner teacherDataScanner = new Scanner(teacherData);
            while (teacherDataScanner.hasNext()) {
                teacherList.add(createTeacher(parseString(teacherDataScanner.nextLine())));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
     
    public ArrayList<Student> getStudentList() {
        return studentList;
    }
    public HashMap<String, Course> getCourseList() {
        return courseList;
    }
    public HashMap<String, Room> getRoomList() {
        return roomList;
    }
    public ArrayList<Teacher> getTeacherList() {
        return teacherList;
    }
    
    // Creates a student based on the given information
    public Student createStudent(ArrayList<String> message) {
        String name = message.get(0);
        // Removes the quotation marks around the name
        name = name.substring(0, name.indexOf(",")) + " " + name.substring(name.indexOf(",") + 1);
        String gender = message.get(1);
        int studentNum = Integer.parseInt(message.get(2));
        String gappsAccount = message.get(3);
        String guardianEmail_1 = message.get(4);
        String guardianEmail_2 = message.get(5);
        int grade = Integer.parseInt(message.get(6));
        // Adds the course codes for each course
        ArrayList<String> courses = new ArrayList<>();
        List<String> courseInfo = message.subList(7, 40);
        for (int i = 0; i < courseInfo.size(); i++) {
            if (i % 3 == 0 && !courseInfo.get(i).equals("")) {
                courses.add(courseInfo.get(i));
            }
        }
        int totalCourses = Integer.parseInt(message.get(40));
        // Adds the course codes for each alternate
        ArrayList<String> alternates = new ArrayList<>();
        List<String> alternateInfo = message.subList(41, message.size());
        for (int i = 0; i < alternateInfo.size(); i++) {
            if (i % 3 == 0 && !alternateInfo.get(i).equals("")) {
                alternates.add(alternateInfo.get(i));
            }
        }
        Student student = new Student(studentList.size(), name, gender, studentNum, gappsAccount, guardianEmail_1, guardianEmail_2, grade, courses, totalCourses, alternates);
        return student;
    }
    
    // Creates a course based on the given information
    public Course createCourse(ArrayList<String> message) {
        String code = message.get(0);
        String title = message.get(1);
        int grade = Integer.parseInt(message.get(2));
        String type = message.get(3);
        int credits = Integer.parseInt(message.get(4));
        ArrayList<String> prerequisites = new ArrayList<>();
        // Removes the quotation marks around the message
        message.set(5, message.get(5).substring(1, message.get(5).length() - 1));
        String[] prerequisiteInfo = message.get(5).split(",", -1);
        for (String prerequisite : prerequisiteInfo) {
            prerequisites.add(prerequisite);
        }
        ArrayList<String> corequisites = new ArrayList<>();
        message.set(6, message.get(6).substring(1, message.get(6).length() - 1));
        String[] corequisiteInfo = message.get(6).split(",", -1);
        for (String corequisite : corequisiteInfo) {
            corequisites.add(corequisite);
        }
        String special = message.get(7);
        // If there are no special requirements, the field will be set to empty
        if (special.equals("[]")) {
            special = "";
        }
        int cap = Integer.parseInt(message.get(8));
        Course course = new Course(courseList.size(), code, title, grade, type, credits, prerequisites, corequisites, special, cap);
        return course;
    }
    
    public Room createRoom(ArrayList<String> message) {
        String roomNum = message.get(0);
        String roomName = message.get(1).replace("\t", "");
        return new Room(roomList.size(), roomNum, roomName);
    }
    
    public Teacher createTeacher(ArrayList<String> message) {
        String teacherName = message.get(0);
        String qualifications = message.get(1);
        return new Teacher(teacherList.size(), teacherName, qualifications);
    }
    // Finds the number of occurrences of a certain expression in a string
    public static int numOccurrences(String expression, String string) {
        if (!string.contains(expression)) {
            return -1;
        }
        String[] strings = string.split(expression, -1);
        return strings.length - 1;
    }

    
    // Takes a string and converts it into an arraylist of strings
    public ArrayList<String> parseString(String inputMessage) {
        ArrayList<String> outputMessage = new ArrayList<>();
        while (inputMessage.contains(",")) {
            // Checks if the next section to be read contains multiple values (which will be denoted with quotation marks)
            if (inputMessage.charAt(0) == '"') {
                // Removes the first quotation mark
                inputMessage = inputMessage.substring(1);
                outputMessage.add(inputMessage.substring(0, inputMessage.indexOf('"')));
                // Removes the next quotation mark along with the comma
                inputMessage = inputMessage.substring(inputMessage.indexOf('"') + 2);
            } else {
                // Otherwise adds the value between the commas to the arraylist
                outputMessage.add(inputMessage.substring(0, inputMessage.indexOf(',')));
                inputMessage = inputMessage.substring(inputMessage.indexOf(',') + 1);
            }
        }
        outputMessage.add(inputMessage);
        return outputMessage;
    }
}