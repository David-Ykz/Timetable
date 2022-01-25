/**
 * [Teacher.Java]
 * Empty container for a teacher in the school
 * @author Blair Wang, David Ye
 * ICS4UE
 * @version 1.0, January 25 2022
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