import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class Student {
	private int studentId;
	private String name;
	private String gender;
	private int studentNum;
	private String gappsAccount;
	private String guardianEmail_1;
	private String guardianEmail_2;
	private int grade;
	private ArrayList<String> courseRequests;
	private int totalCourses;
	private ArrayList<String> alternates;
	private HashMap<Integer, Class> classes;
	private HashSet<String> takenClasses;

	Student(int studentId, String name, String gender, int studentNum, String gappsAccount, String guardianEmail_1,
			String guardianEmail_2, int grade, ArrayList<String> courseRequests, int totalCourses,
			ArrayList<String> alternates) {
		this.studentId = studentId;
		this.name = name;
		this.gender = gender;
		this.studentNum = studentNum;
		this.gappsAccount = gappsAccount;
		this.guardianEmail_1 = guardianEmail_1;
		this.guardianEmail_2 = guardianEmail_2;
		this.grade = grade;
		this.courseRequests = courseRequests;
		this.totalCourses = totalCourses;
		this.alternates = alternates;
		this.classes = new HashMap<>();
		this.takenClasses = new HashSet<>();
	}

	public void printInfo() {
		System.out.println("Name: " + name);
		System.out.println("Gender: " + gender);
		System.out.println("Student Number: " + studentNum);
		System.out.println("Gapps Email: " + gappsAccount);
		System.out.println("Guardian Email #1: " + guardianEmail_1);
		System.out.println("Guardian Email #2: " + guardianEmail_2);
		System.out.println("Grade: " + grade);
		System.out.println("Courses: " + courseRequests.toString());
		System.out.println("Total Courses: " + totalCourses);
		System.out.println("Alternates: " + alternates.toString());
	}

	// Returns true if adding student was successful
	public boolean addClass(int period, Class newClass) {
		// Won't add course if a similar course is already being taken
		if (!takenClasses.contains(newClass.getCourseId().substring(0, 3))) {
			this.classes.put(period, newClass);
			this.takenClasses.add(newClass.getCourseId().substring(0, 3));
			return true;
		}
		return false;
	}

	public int getId() {
		return this.studentId;
	}

	public String getName() {
		return this.name;
	}

	public String[] getCourseRequests() {
		return (String[]) courseRequests.toArray(new String[courseRequests.size()]);
	}

	public ArrayList<String> getCourses() {
		return this.courseRequests;
	}

	public ArrayList<String> getAlternates() {
		return this.alternates;
	}

	public HashMap<Integer, Class> getClasses() {
		return this.classes;
	}

	public int getStudentNum() {
		return this.studentNum;
	}

	public int getGrade() {
		return this.grade;
	}

	public boolean moveIntoGroup(ArrayList<Group> groupList, HashSet<Group> removeGroups) {
		for (Group group : groupList) {
			if (alternates.contains(group.getCourseCode()) && !group.isFull() && !removeGroups.contains(group)) {
				group.addStudent(studentId);
				alternates.remove(group.getCourseCode());
				return true;
			}
		}
		return false;
	}

	public void findNextBestCourse(HashMap<String, Course> courseList, Student student,
			HashMap<String, ArrayList<Student>> alternateList) {
		for (String course : alternateList.keySet()) {
			if (alternateList.get(course).size() < courseList.get(course).getCapacity()) {
				alternateList.get(course).add(student);
			}
		}
	}

	public String infoAsString() {
		String[] sortedClasses = new String[10];
		String message = Integer.toString(studentNum);
		for (Class c1 : this.classes.values()) {
			int index = c1.getPeriod() + 5 * (c1.getSemester() - 1) - 1;
			sortedClasses[index] = c1.infoAsString();
		}
		for (int i = 0; i < sortedClasses.length; i++) {
			message += ",";
			if (sortedClasses[i] != null) {
				message += sortedClasses[i];
			}
		}
		return message;
	}

}