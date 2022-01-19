import java.util.ArrayList;

public class Group {
    private final int groupId;
    private int groupSize;
    private final String courseCode;
    private ArrayList<Integer> studentIds = new ArrayList<>();
    private int cap;
    
    public Group(int groupId, String courseCode, int cap){
        this.groupId = groupId;
        this.courseCode = courseCode;
        this.groupSize = 0;
        this.cap = cap;
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
    
    public ArrayList<Integer> getStudentIds() {
        return this.studentIds;
    }
    
    public boolean isFull() {
        return groupSize >= cap;
    }
    
    public int getCap() {
        return this.cap;
    }

    
    
}