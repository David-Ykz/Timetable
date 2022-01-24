/**
 * [Teacher.java] This class represents each teacher and their corresponding information 
 * 
 * @author Blair Wang
 * @author Brian Zhang
 * @author David Ye
 * @author Anthony Tecsa
 * @author Allen Liu
 * @version Jan 15 2022
 */
public class Teacher {
    private final int teacherId;
    private final String teacherName;
    private final String qualifications;
    
    public Teacher(int teacherId, String teacherName, String qualifications) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.qualifications = qualifications;
    }
    
    public int getTeacherId() {
        return this.teacherId;
    }
    public String getTeacherName() {
        return this.teacherName;
    }
    public String getQualifications() {
        return this.qualifications;
    }
}