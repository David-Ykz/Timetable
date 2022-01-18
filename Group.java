import java.util.ArrayList;

public class Group {
    private final int groupId;
    private int groupSize;
    private final String courseCode;
    private ArrayList<Integer> studentIds = new ArrayList<>();
    
    public Group(int groupId, String courseCode){
        this.groupId = groupId;
        this.courseCode = courseCode;
    }

    public int getGroupId(){
        return this.groupId;
    }
    
    public int getGroupSize(){
        return this.groupSize;
    }
    
    public String getCourseCode(){
        return this.courseCode;
    }
    
    public void addStudent(int studentId){
        studentIds.add(studentId);
        groupSize++;
    }
    

    
    
}