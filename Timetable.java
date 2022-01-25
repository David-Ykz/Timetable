package timetableProgram;
import java.util.HashMap;
import java.util.HashSet;

/**
 * [Timetable.java]
 * Object of comprehensive timetable for the school
 * @author Brian Zhang, Blair Wang
 * ICS4UE
 * @version 1.0, January 25 2022
 */
public class Timetable {
	private final HashMap<Integer, Room> rooms;
	private final HashMap<Integer, Teacher> teachers;
	private final HashMap<Integer, Student> students;
	private final HashMap<String, Course> courses;
	private HashSet<String> doNotAutofill;
	private Class classes[];
	private int numClasses = 0;

	Timetable(HashMap<Integer, Room> rooms, HashMap<Integer, Teacher> teachers, HashMap<Integer, Student> students,
			HashMap<String, Course> courses, Class[] classList) {
		this.rooms = rooms;
		this.teachers = teachers;
		this.students = students;
		this.courses = courses;
		this.classes = classList;
		this.numClasses = classList.length;
		this.doNotAutofill = new HashSet<>();
		this.doNotAutofill.add("ESL");
		this.doNotAutofill.add("PPL");
		this.doNotAutofill.add("ZRE");
		this.doNotAutofill.add("AMR");
		this.doNotAutofill.add("COP");
		this.doNotAutofill.add("YYY");
		this.doNotAutofill.add("GLE");
		this.doNotAutofill.add("PAI");
	}

	// Makes a replica timetable
	Timetable(Timetable copy) {
		this.rooms = copy.getRooms();
		this.teachers = copy.getTeachers();
		this.students = copy.getStudents();
		this.courses = copy.getCourses();
		this.classes = copy.getClasses();
		this.numClasses = copy.getNumClasses();
	}

	// Getters
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

	public int getNumClasses() {
		return numClasses;
	}

	public Room getRandomRoom() {
		Object[] roomsArray = this.rooms.values().toArray();
		Room room = (Room) roomsArray[(int) (roomsArray.length * Math.random())];
		return room;
	}

	// Creating classes from a chromosome
	public void createClasses(Individual individual) {

		// Get student's chromosome
		int chromosome[] = individual.getChromosome();
		int chromosomePos = 0;
		int classIndex = 0;

		for (int i = 0; i < numClasses; i++) {

			// Assigning period
			if (this.classes[classIndex].getCourseId().contains("AMR")) {	// Repertoire happens after school
				this.classes[classIndex].setPeriod(5);
			} 
			else {
				this.classes[classIndex].setPeriod(chromosome[chromosomePos]);
			}
			chromosomePos++;

			// Assigning room
			if (this.classes[classIndex].getCourseId().contains("AMR")) {
				this.classes[classIndex].setRoomId(27);
			} 
			this.classes[classIndex].setRoomId(chromosome[chromosomePos]);
			chromosomePos++;

			// Assigning teacher
			this.classes[classIndex].setTeacherId(chromosome[chromosomePos]);
			chromosomePos++;

			// Assigning semester
			if (this.classes[classIndex].getCourseId().contains("AMR")) {		
				this.classes[classIndex].setSemester(1);
			} else if (this.classes[classIndex].getCourseId().contains("MHF")) {		
				this.classes[classIndex].setSemester(1);
			} else if (this.classes[classIndex].getCourseId().contains("MCV")) {
				this.classes[classIndex].setSemester(2);
			}
			this.classes[classIndex].setSemester(chromosome[chromosomePos]);
			chromosomePos++;

			classIndex++;
		}
	}

	// Assigning students to classes
	public void giveStudentsClasses() {
		// First give students the requested courses, if possible
		for (Student student : this.students.values()) {
			for (String courseCode : student.getCourseRequests()) {
				for (Class cl : this.classes) {
					// Ensures that there are no conflicts with same class time, class capacity, or
					// student classes
					if (cl.getCourseId().equals(courseCode)
							&& student.getCourseRequests()[0].charAt(0) != 'K'
							&& !student.getClasses().containsKey(cl.getPeriod() + (cl.getSemester() - 1) * 5)
							&& student.getClasses().size() < 10
							&& cl.getStudents().size() + 2 < this.courses.get(cl.getCourseId()).getCap()) {
						boolean addedStudent = student.addClass(cl.getPeriod() + (cl.getSemester() - 1) * 5, cl);
						if (addedStudent == true) {
							cl.addStudent(student);
						}
					}
					else if (student.getCourseRequests()[0].charAt(0) == 'K' 		// Special education
							&& cl.getCourseId().contains(courseCode.substring(0,3))
							&& !student.getClasses().containsKey(cl.getPeriod() + (cl.getSemester() - 1) * 5)) {
						
						boolean addedStudent = student.addClass(cl.getPeriod() + (cl.getSemester() - 1) * 5, cl);
						if (addedStudent == true) {
							cl.addStudent(student);
						}
					}
				}
			}
		}
		
		// Distribute leftover classes to students with empty time slots
		// Similar courses
		for (Student student : this.students.values()) {
			// Search for similar courses if possible (same letters different level, e.g. CGC1D2 -> CGC1DG)
			if ((student.getClasses().size() < 9 && student.getGrade() < 12)
					|| (student.getClasses().size() < 6 && student.getGrade() == 12)) { // If not full courseload
				for (Class cl : this.classes) {
					// There are some courses we don't want to fill randomly into
					if (!doNotAutofill.contains(cl.getCourseId().substring(0,3))) {
						if (cl.getCourseId().contains(cl.getCourseId().substring(0, 2))
								&& student.getClasses().size() < 9
								&& (!student.getClasses().containsKey(cl.getPeriod() + (cl.getSemester() - 1) * 5)
								&& cl.getStudents().size() < this.courses.get(cl.getCourseId()).getCap() + 2)
								&& (student.getGrade() <= ((Character.getNumericValue(cl.getCourseId().charAt(3))) + 9))
								&& (student.getGrade() >= ((Character.getNumericValue(cl.getCourseId().charAt(3))) + 8))) {
							if (student.getGrade() == 12 && student.getClasses().size() > student.getCourseRequests().length) {
								break;
							}
							
							boolean addedStudent = student.addClass(cl.getPeriod() + (cl.getSemester() - 1) * 5, cl);
							if (addedStudent == true) {
								cl.addStudent(student);
							}
						}
					}
					else if (student.getCourseRequests()[0].charAt(0) == 'K' 
							&& cl.getCourseId().charAt(0) == 'K'
							&& !student.getClasses().containsKey(cl.getPeriod() + (cl.getSemester() - 1) * 5)) {	// Special education
						boolean addedStudent = student.addClass(cl.getPeriod() + (cl.getSemester() - 1) * 5, cl);
						if (addedStudent == true) {
							cl.addStudent(student);
						}
					}
				}
			}
		}
		
		// Alternates
		for (Student student : this.students.values()) {
			// Search through student's alternates
			if ((student.getClasses().size() < 9 && student.getGrade() < 12)
					|| (student.getClasses().size() < 6 && student.getGrade() == 12)) { // If not full courseload
				for (Class cl : this.classes) {
					for (String alternate : student.getAlternates()) {
						if (!doNotAutofill.contains(cl.getCourseId().substring(0,3))) {
							if (cl.getCourseId().equals(alternate) && student.getClasses().size() < 9
									&& (!student.getClasses().containsKey(cl.getPeriod() + (cl.getSemester() - 1) * 5)
									&& cl.getStudents().size() < this.courses.get(cl.getCourseId()).getCap() + 2)
									&& (student.getGrade() <= ((Character.getNumericValue(cl.getCourseId().charAt(3))) + 9))
									&& (student.getGrade() >= ((Character.getNumericValue(cl.getCourseId().charAt(3))) + 8))) {
								if (student.getGrade() == 12 && student.getClasses().size() > student.getCourseRequests().length) {
									break; 
								}
								boolean addedStudent = student.addClass(cl.getPeriod() + (cl.getSemester() - 1) * 4,cl);
								if (addedStudent == true) {
									cl.addStudent(student);
								}
							}
						}
					}
				}
			}
		}
		// If all other options are unavailable, autofill
		for (Student student : this.students.values()) {
			if ((student.getClasses().size() < 9 && student.getGrade() < 12)
					|| (student.getClasses().size() < 6 && student.getGrade() == 12)) {
				for (Class cl : this.classes) {
					if (!doNotAutofill.contains(cl.getCourseId().substring(0,3)) && student.getCourseRequests()[0].charAt(0) != 'K') {
						if ((student.getClasses().size()) < 9 && (!student.getClasses().containsKey(cl.getPeriod() + (cl.getSemester() - 1) * 4)
								&& cl.getStudents().size() < this.courses.get(cl.getCourseId()).getCap() + 2
								&& (student.getGrade() <= ((Character.getNumericValue(cl.getCourseId().charAt(3))) + 9))
								&& (student.getGrade() >= ((Character.getNumericValue(cl.getCourseId().charAt(3))) + 8)))) {
							if (student.getGrade() == 12 && student.getClasses().size() > student.getCourseRequests().length) {
								break;
							}
							boolean addedStudent = student.addClass(cl.getPeriod() + (cl.getSemester() - 1) * 4, cl);
							if (addedStudent == true) {
								System.out.println("Added " + cl.getCourseId());
								cl.addStudent(student);
							}
						}
					}
				}
			}
		}
	}

	public int calculateConflicts() {
		int conflicts = 0;
		int semesterOneCount = 0;
		int semesterTwoCount = 0;
		for (Class classA : this.classes) {
			for (Class classB : this.classes) {
				// Check if room is taken
				if (classA.getRoomId() == classB.getRoomId() && classA.getPeriod() == classB.getPeriod()
						&& classA.getClassId() != classB.getClassId() && classA.getSemester() == classB.getSemester()) {
					conflicts++;
					break;
				}
			}

			// Room constraints
			if (classA.getCourseId().contains("TEJ")
					&& !this.rooms.get(classA.getRoomId()).getRoomName().equals("\"Technology room\"")) {
				conflicts+=3;
			} else if (classA.getCourseId().contains("PPL")
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("gym")) {
				conflicts+=3;
			} else if (classA.getCourseId().contains("ICS")
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("computer") 
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("library")) {
				conflicts+=3;
			} else if (classA.getCourseId().contains("AMI")
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("music")) {
				conflicts+=4;
			} else if ((classA.getCourseId().contains("ADA") || (classA.getCourseId().contains("ADD")))
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("drama")) {
				conflicts+=4;
			} else if ((classA.getCourseId().charAt(0) == 'K')
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("Special")) {
				conflicts+=20;
			} else if ((classA.getCourseId().contains("HIF"))
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("Family")) {
				conflicts+=4;
			} else if (!classA.getCourseId().contains("PPL") 
					&& this.rooms.get(classA.getRoomId()).getRoomName().contains("gym")) {
				conflicts+=2;
			} else if (!classA.getCourseId().contains("AMI") 
					&& this.rooms.get(classA.getRoomId()).getRoomName().contains("music")) {
				conflicts+=4;
			} else if (!classA.getCourseId().contains("ADA") 
					&& this.rooms.get(classA.getRoomId()).getRoomName().contains("drama")) {
				conflicts+=3;
			} else if (!classA.getCourseId().contains("ICS") 
					&& this.rooms.get(classA.getRoomId()).getRoomName().contains("computer")) {
				conflicts+=2;
			} else if (!classA.getCourseId().contains("HIF") 
					&& this.rooms.get(classA.getRoomId()).getRoomName().contains("Family")) {
				conflicts+=3;
			}  else if (classA.getCourseId().charAt(0) != 'K'
					&& this.rooms.get(classA.getRoomId()).getRoomName().contains("Special")) {
				conflicts+=20;
			}
			
			if (classA.getSemester() == 1) {
				semesterOneCount++;
			} else {
				semesterTwoCount++;
			}
		}

		// Make sure a similar number of courses in semester 1 and 2
		if (semesterOneCount > 0 && semesterTwoCount > 0) {
			double semesterRatio = (double) (semesterOneCount / semesterTwoCount);
			if (semesterRatio > 1.25 || semesterRatio < 0.87) {
				conflicts += 100;
			}
		}
		return conflicts;
	}
	

	public void printConflicts() {
		for (Class classA : this.classes) {
			for (Class classB : this.classes) {
				if (classA.getRoomId() == classB.getRoomId() && classA.getPeriod() == classB.getPeriod()
						&& classA.getClassId() != classB.getClassId() && classA.getSemester() == classB.getSemester()) {
					System.out.println(this.courses.get(classA.getCourseId()).getName() + " conflicts with "
							+ this.courses.get(classB.getCourseId()).getName());
					break;
				}
			}

			if (classA.getCourseId().contains("TEJ")
					&& !this.rooms.get(classA.getRoomId()).getRoomName().equals("\"Technology room\"")) {
				System.out.println(classA.getClassId() + " is being taught " + classA.getCourseId() + " in room "
						+ this.rooms.get(classA.getRoomId()).getRoomName());
			} else if (classA.getCourseId().contains("PPL")
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("gym")) {
				System.out.println(classA.getClassId() + " is being taught " + classA.getCourseId() + " in room "
						+ this.rooms.get(classA.getRoomId()).getRoomName());
			} else if (classA.getCourseId().contains("ICS")
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("computer") && !this.rooms.get(classA.getRoomId()).getRoomName().contains("library")) {
				System.out.println(classA.getClassId() + " is being taught " + classA.getCourseId() + " in room "
						+ this.rooms.get(classA.getRoomId()).getRoomName());
			} else if (classA.getCourseId().contains("AMI")
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("music")) {
				System.out.println(classA.getClassId() + " is being taught " + classA.getCourseId() + " in room "
						+ this.rooms.get(classA.getRoomId()).getRoomName());
			} else if ((classA.getCourseId().contains("ADA") || (classA.getCourseId().contains("ADD")))
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("drama")) {
				System.out.println(classA.getClassId() + " is being taught " + classA.getCourseId() + " in room "
						+ this.rooms.get(classA.getRoomId()).getRoomName());
			} else if ((classA.getCourseId().contains("HIF"))
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("Family")) {
				System.out.println(classA.getClassId() + " is being taught " + classA.getCourseId() + " in room "
						+ this.rooms.get(classA.getRoomId()).getRoomName());
			} else if (classA.getCourseId().charAt(0) == 'K'
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("Special")) {
				System.out.println(classA.getClassId() + " is being taught " + classA.getCourseId() + " in room "
						+ this.rooms.get(classA.getRoomId()).getRoomName());
			}
		}
	}
}