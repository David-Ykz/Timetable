import java.util.ArrayList;

/**
 * [Class.java] This class represents the courses in the final timetable
 * 
 * @author Blair Wang
 * @author Brian Zhang
 * @author David Ye
 * @author Anthony Tecsa
 * @author Allen Liu
 * @version Jan 15 2022
 */
public class Class {
    private final int classId;
    private final String courseCode;
    private int teacherId;
    private int roomId;
    private int period;
    private int semester;
    private ArrayList<Student> studentList;
    
    public Class(int classId, String courseCode) {
        this.classId = classId;
        this.courseCode = courseCode;
        this.studentList = new ArrayList<>();
    }
    
    public int getClassId() {
        return this.classId;
    }
    public String getCourseId() {
        return this.courseCode;
    }
    public int getTeacherId() {
        return this.teacherId;
    }
    public int getRoomId() {
        return this.roomId;
    }
    public int getPeriod() {
        return this.period;
    }
    public int getSemester() {
        return this.semester;
    }
    public ArrayList<Student> getStudents() {
        return this.studentList;
    }
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    public void setPeriod(int period) {
        this.period = period;
    }
    public void setSemester(int semester) {
        this.semester = semester;
    }    
    public String infoAsString() {
        return courseCode + ";" + roomId;
    }
    public void addStudent(Student student) {
        this.studentList.add(student);
    }
}