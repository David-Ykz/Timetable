/**
 * [Course.java] This class stores information about the different courses students can take
 * 
 * @author Blair Wang
 * @author Brian Zhang
 * @author David Ye
 * @author Anthony Tecsa
 * @author Allen Liu
 * @version Jan 15 2022
 */
class Course {
    private int courseId;
    private String code;
    private String name;
    private String type;
    private double credits;
    private String special;
    private int cap;
        
    Course(int courseId, String code, String name, double credits, String type, int cap) {
        this.courseId = courseId;
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.type = type;
        this.cap = cap;
    }
    
    public int getCourseId() {
        return this.courseId;
    }
    public int getCapacity() {
        return this.cap;
    }
    public String getCourseCode() {
        return this.code;
    }
    public String getName() {
        return this.name;
    }
    public int getCap() {
        return this.cap;
    }
    
    public String getType() {
    	return this.type;
    }
    public String getCode() {
    	return this.code;
    }
}