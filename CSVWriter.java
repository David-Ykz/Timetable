import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

public class CSVWriter {
	public CSVWriter() {
	}

	public void saveStudentData(HashMap<Integer, Student> studentList) {
		try {
			File studentDataFile = new File("StudentDataSorted.csv");
			PrintWriter printer = new PrintWriter(studentDataFile);
			for (Student student : studentList.values()) {
				printer.println(student.infoAsString());
			}
			printer.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void saveMasterTimetable() {
		
	}
	
	public void createStudentTimetable(Student student) {
		HashMap<Integer, Class> studentClasses = student.getClasses();
		File studentDataFile = new File(student.getStudentNum() + "TimetableData.csv");
		try {
			PrintWriter printer = new PrintWriter(studentDataFile);
			printer.println("Course:, Period:, Semester:");
			for (int i = 1; i <= studentClasses.size(); i++) {
				Class cl = studentClasses.get(i);
				String fileInput = "";
				if (cl != null) {
					fileInput += cl.getCourseId();
					fileInput += cl.getPeriod();
					fileInput += cl.getSemester();
				}
				printer.println(fileInput);
			}
			printer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
