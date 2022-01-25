package timetableProgram;
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
	
	public void saveMasterTimetable(Timetable timetable) {
        String[] periods = {"", "", "", "", "", "", "", "", "", "",};
        
        
        for (Class bestClass : timetable.getClasses()) {
            int index = bestClass.getPeriod() + 5 * (bestClass.getSemester() - 1) - 1;
            String classInfo = timetable.getRoom(bestClass.getRoomId()).getRoomNum() + ";" + 
                timetable.getCourse(bestClass.getCourseId()).getCourseCode();
            periods[index] += classInfo + ",";
        }
        try {
            File masterTimetableFile = new File("MasterTimetable.csv");
            PrintWriter printer = new PrintWriter(masterTimetableFile);
            for (String period : periods) {
                printer.println(period.substring(0, period.length() - 1));
            }
            printer.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
	
	public void createStudentTimetable(Student student) {
		HashMap<Integer, Class> studentClasses = student.getClasses();
		
		File studentDataFile = new File(student.getStudentNum() + "TimetableData.csv");
		try {
			PrintWriter printer = new PrintWriter(studentDataFile);
			printer.println("Course:, Period:, Semester:");
			for (Class cl : studentClasses.values()) {
				String fileInput = "";
				if (cl != null) {
					fileInput += cl.getCourseId() + " , ";
					fileInput += cl.getPeriod() + ", ";
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
