import java.util.ArrayList;

/**
 * [Class.java]
 * A group of students occupying a classroom at any given period
 * @author Brian Zhang, Blair Wang, David Ye
 * ICS4UE
 * @version 1.0, January 25 2022
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

	public String infoAsString() {
		return courseCode + Const.SEMI_COLON + roomId;
	}

	public void addStudent(Student student) {
		this.studentList.add(student);
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

}