import java.util.ArrayList;

class Course {
 private int courseId;
 private int teacherIds[]; //teachers who can teach this class
 private String code;
 private String name;
 private int grade;
 private String type;
 private int credits;
 private ArrayList<String> prerequisites;
 private ArrayList<String> corequisites;
 private String special;
 private int cap;

 Course(int courseId, String code, String name, int grade, String type, int credits, ArrayList<String> prerequisites, ArrayList<String> corequisites, String special, int cap) {
     this.courseId = courseId;
     this.code = code;
     this.name = name;
     this.grade = grade;
     this.type = type;
     this.credits = credits;
     this.prerequisites = prerequisites;
     this.corequisites = corequisites;
     this.special = special;
     this.cap = cap;
 }

 public void printInfo() {
     System.out.println("Code: " + code);
     System.out.println("Name: " + name);
     System.out.println("Grade: " + grade);
     System.out.println("Type: " + type);
     System.out.println("Credits: " + credits);
     System.out.println("Prerequisites: " + prerequisites.toString());
     System.out.println("Corequisites: " + corequisites.toString());
     System.out.println("Special: " + special);
     System.out.println("Cap: " + cap);
     
 }
 
    public int getCourseId(){
        return this.courseId;
    }
    
    public int getCapacity() {
     return this.cap;
    }
    
    public String getCourseCode(){
        return this.code;
    }
    
    public String getCourseName(){
        return this.name;
    }
    
    
    public int getCap() {
        return this.cap;
    }
    

}