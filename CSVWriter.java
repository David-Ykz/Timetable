import java.io.File;
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
}