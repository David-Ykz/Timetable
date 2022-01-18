public class Teacher {
    private final int teacherId;
    private final String teacherName;
    private final String qualifications;
    
    public Teacher(int teacherId, String teacherName, String qualifications){
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.qualifications = qualifications;
    }
    
    public int getId(){
        return this.teacherId;
    }
    
    public String getName(){
        return this.teacherName;
    }
    
    public String getQualifications() {
        return this.qualifications;
    }
}
