import java.util.HashMap;

public class Timetable {
    private final HashMap<Integer, Room> rooms;
    private final HashMap<Integer, Teacher> teachers;
    private final HashMap<Integer, Student> students;
    private final HashMap<String, Course> courses;
    private final HashMap<Integer, Group> groups;
    
    private Class classes[];
    
    private int numClasses = 0;

    Timetable(HashMap<Integer, Room> rooms, HashMap<Integer, Teacher> teachers, HashMap<Integer, Student> students, HashMap<String, Course> courses, HashMap<Integer, Group> groups) {
        
        this.rooms = rooms;
        this.teachers = teachers;
        this.students = students;
        this.courses = courses;
        this.groups = groups;
        this.numClasses = groups.size();
    }

    //makes a replica timetable
    Timetable(Timetable copy) {
        this.rooms = copy.getRooms();
        this.teachers = copy.getTeachers();
        this.students = copy.getStudents();
        this.courses = copy.getCourses();
        this.groups = copy.getGroups();
        this.numClasses = copy.getNumClasses();
    }
    
    //getters
    private HashMap<Integer, Group> getGroups() {
        return this.groups;
    }
    
    public HashMap<String, Course> getCourses() {
        return this.courses;
    }
    
    public HashMap<Integer, Teacher> getTeachers() {
        return this.teachers;
    }
    
    public HashMap<Integer, Room> getRooms() {
        return this.rooms;
    }
    
    public HashMap<Integer, Student> getStudents() {
        return this.students;
    }
    
    public Class[] getClasses() {
        return this.classes;
    }
    
    public Teacher getTeacher(int teacherId) {
        return (Teacher) this.teachers.get(teacherId);
    }
    
    public Room getRoom(int roomId) {
        return (Room) this.rooms.get(roomId);
    }
    
    public Course getCourse(String string) {
        return (Course) this.courses.get(string);
    }
    
    public String getGroupCourse(int groupId) {
        Group group = (Group) this.groups.get(groupId);
        return group.getCourseCode();
    }

    public Group getGroup(int groupId) {
        return (Group) this.groups.get(groupId);
    }

    public Group[] getGroupsAsArray() {
        return (Group[]) this.groups.values().toArray(new Group[this.groups.size()]);
    }
    
  
    public int getNumClasses() {
        return numClasses;
    }
    
    public Room getRandomRoom() {
     Object[] roomsArray = this.rooms.values().toArray();
        Room room = (Room) roomsArray[(int) (roomsArray.length * Math.random())];
        return room;
    }
    
//    public void createGroups() {
//        for (int i = 0; i < students.size(); i++) {
//            Student student = students.get(i);
//            String[] courseRequests = student.getCourseRequests();
//            
//            for (String course: courseRequests) {
//                for (Group availableGroup: this.getGroupsAsArray()) {
//                    if (course.equals(availableGroup.getCourseCode()) == true) {
//                        availableGroup.addStudent(student.getStudentId());
//                    }
//                    //adds a new group if the course did not already have an associated group
//                    //or if the group is at max capacity
//                    else {
//                        this.addGroup(groups.size() + 1, course);
//                    }
//                }
//            }
//        }     
//    }
    
    //creating classes from a chromosome
    public void createClasses(Individual individual) {
        //initializes classes for the timetable
        Class classes[] = new Class[this.numClasses];
        
        // Get student's chromosome
        int chromosome[] = individual.getChromosome();
        int chromosomePos = 0;
        int classIndex = 0;
        
        for (Group group : this.getGroupsAsArray()) {
            String courseCode = group.getCourseCode();
            
            classes[classIndex] = new Class(classIndex, group.getGroupId(), courseCode);
            
            //period
            classes[classIndex].setPeriod(chromosome[chromosomePos]);
            chromosomePos++;
            
            //room
            classes[classIndex].setRoomId(chromosome[chromosomePos]);
            chromosomePos++;
            
            //teacher
            classes[classIndex].setTeacherId(chromosome[chromosomePos]);
            chromosomePos++;
            
            //semester
            classes[classIndex].setSemester(chromosome[chromosomePos]);
            chromosomePos++;
            
            classIndex++;
        }
        this.classes = classes;
    }
    
    //-----------------------------------------ENTIRE METHOD IS SUBJECT TO CHANGE--------------------------------------------//
    /*
     * 
     * 
     * 
     */
    //-----------------------------------------------------------------------------------------------------------------------//
    public int calculateConflicts() {
        int conflicts = 0;
        
        for (Class classA : this.classes) {
//            // Check course capacity
//            int roomCapacity = this.getCourse(classA.getCourseId()).getCapacity();
//            int groupSize = this.getGroup(classA.getGroupId()).getGroupSize();
//            
//            if (roomCapacity < groupSize) {
//             conflicts++;
//            }
            
            // Check if room is taken
            for (Class classB : this.classes) {
                if (classA.getRoomId() == classB.getRoomId() && classA.getPeriod() == classB.getPeriod()
                        && classA.getClassId() != classB.getClassId() && classA.getSemester() == classB.getSemester()) {
                	conflicts ++;
                    break;
                }
            }
            
            // Check if teacher is available
            for (Class classB : this.classes) {
                if (classA.getTeacherId() == classB.getTeacherId() && classA.getPeriod() == classB.getPeriod()
                        && classA.getClassId() != classB.getClassId() && classA.getSemester() == classB.getSemester()) {
                	conflicts++;
                    break;
                }
            }
        }
        
        return conflicts;
    }
    
    public void printConflicts() {
    	for (Class classA : this.classes) {
    		for (Class classB : this.classes) {
                if (classA.getRoomId() == classB.getRoomId() && classA.getPeriod() == classB.getPeriod()
                        && classA.getClassId() != classB.getClassId() && classA.getSemester() == classB.getSemester()) {
                	System.out.println(this.courses.get(classA.getCourseId()).getCourseName() + " conflicts with " + this.courses.get(classB.getCourseId()).getCourseName());
                    break;
                }
            }
            
            // Check if teacher is available
            for (Class classB : this.classes) {
                if (classA.getTeacherId() == classB.getTeacherId() && classA.getPeriod() == classB.getPeriod()
                        && classA.getClassId() != classB.getClassId() && classA.getSemester() == classB.getSemester()) {
                	System.out.println(this.teachers.get(classA.getTeacherId()).getTeacherName() + " conflicts with " + this.teachers.get(classB.getTeacherId()).getTeacherName());
                    break;
                }
            }
    	}
    }
    
    public void printTimetable() {
        System.out.println("Room Size: " + rooms.size());
        System.out.println("Teacher Size: " + teachers.size());
        System.out.println("Student Size: " + students.size());
        System.out.println("Course Size: " + courses.size());
        System.out.println("Group Size: " + groups.size());
        
        System.out.println ("Number of classes: " + numClasses);
    }
    

}
