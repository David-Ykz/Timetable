import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * [CSVWriter.java] 
 * Outputs timetable information into .csv files
 * @author David Ye, Brian Zhang
 * ICS4UE
 * @version Jan 25 2022
 */

public class CSVWriter {
	public static final String MASTER_TIMETABLE_FILE = "MasterTimetable.csv";
	public static final String TIMETABLE_FILE = "TimetableData.csv";
	public static final String STUDENT_DATA_FILE = "StudentDataSorted.csv";
	
	public CSVWriter() {
	}
	
	// Saves all student timetables in a single file
	public void saveStudentData(HashMap<Integer, Student> studentList) {
		try {
			File studentDataFile = new File(STUDENT_DATA_FILE);
			PrintWriter printer = new PrintWriter(studentDataFile);
			for (Student student : studentList.values()) {
				printer.println(student.infoAsString());
			}
			printer.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	// Saves a master timetable showing which classes are running during each period
	public void saveMasterTimetable(Timetable timetable) {
        String[] periods = {Const.BLANK, Const.BLANK, Const.BLANK, Const.BLANK, Const.BLANK, Const.BLANK, Const.BLANK, Const.BLANK, Const.BLANK, Const.BLANK,};
        
        
        for (Class bestClass : timetable.getClasses()) {
            int index = bestClass.getPeriod() + 5 * (bestClass.getSemester() - 1) - 1;
            String classInfo = timetable.getRoom(bestClass.getRoomId()).getRoomNum() + Const.SEMI_COLON + 
                timetable.getCourse(bestClass.getCourseId()).getCourseCode();
            periods[index] += classInfo + Const.COMMA;
        }
        try {
            File masterTimetableFile = new File(MASTER_TIMETABLE_FILE);
            PrintWriter printer = new PrintWriter(masterTimetableFile);
            for (String period : periods) {
                printer.println(period.substring(0, period.length()));
            }
            printer.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
	
	// Saves an individual student's timetable
	public void createStudentTimetable(Student student) {
		HashMap<Integer, Class> studentClasses = student.getClasses();
		
		File studentDataFile = new File(student.getStudentNum() + TIMETABLE_FILE);
		try {
			PrintWriter printer = new PrintWriter(studentDataFile);
			printer.println("Course:, Period:, Semester:");
			for (Class cl : studentClasses.values()) {
				String fileInput = Const.BLANK;
				if (cl != null) {
					fileInput += cl.getCourseId() + Const.SPACE + Const.COMMA + Const.SPACE;
					fileInput += cl.getPeriod() + Const.COMMA + Const.SPACE;
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
