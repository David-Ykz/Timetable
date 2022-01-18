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
    private HashMap<Integer, Student> studentList = new HashMap<>();
    private HashMap<String, Course> courseList = new HashMap<>();
    private HashMap<String, Room> roomList = new HashMap<>();
    private HashMap<Integer, Teacher> teacherList = new HashMap<>();
    
    CSVReader(String studentDataFileName, String courseDataFileName, String roomDataFileName, String teacherDataFileName) {
        try {
            // Reads student data
            File studentData = new File(studentDataFileName);
            Scanner studentDataScanner = new Scanner(studentData);
            studentDataScanner.nextLine();
            while (studentDataScanner.hasNext()) {
                Student student = createStudent(parseString(studentDataScanner.nextLine()));
                studentList.put(student.getId(), student);
            }
            // Reads course data
            File courseData = new File(courseDataFileName);
            Scanner courseDataScanner = new Scanner(courseData);
            courseDataScanner.nextLine();
            while (courseDataScanner.hasNext()) {
                Course course = createCourse(parseString(courseDataScanner.nextLine()));
                courseList.put(course.getCourseCode(), course);
            }
            // Reads room data
            File roomData = new File(roomDataFileName);
            Scanner roomDataScanner = new Scanner(roomData);
            while (roomDataScanner.hasNext()) {
                Room room = createRoom(parseString(roomDataScanner.nextLine()));
                roomList.put(room.getRoomNum(), room);
            }
            // Reads teacher data
            File teacherData = new File(teacherDataFileName);
            Scanner teacherDataScanner = new Scanner(teacherData);
            while (teacherDataScanner.hasNext()) {
                Teacher teacher = createTeacher(parseString(teacherDataScanner.nextLine()));
                teacherList.put(teacher.getId(), teacher);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
     
    public HashMap<Integer, Student> getStudentList() {
        return studentList;
    }
    public HashMap<String, Course> getCourseList() {
        return courseList;
    }
    public HashMap<String, Room> getRoomList() {
        return roomList;
    }
    public HashMap<Integer, Teacher> getTeacherList() {
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
        double credits = Double.parseDouble(message.get(2));
        String type = message.get(3);
        int cap = 30;
        Course course = new Course(courseList.size(), code, title, credits, type, cap);
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