import java.util.HashMap;

public class Timetable {
	private final HashMap<Integer, Room> rooms;
	private final HashMap<Integer, Teacher> teachers;
	private final HashMap<Integer, Student> students;
	private final HashMap<String, Course> courses;

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
			this.classes[classIndex].setPeriod(chromosome[chromosomePos]);
			chromosomePos++;

			// Assigning room
			this.classes[classIndex].setRoomId(chromosome[chromosomePos]);
			chromosomePos++;

			// Assigning teacher
			this.classes[classIndex].setTeacherId(chromosome[chromosomePos]);
			chromosomePos++;

			// Assigning semester
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
							&& !student.getClasses().containsKey(cl.getPeriod() + (cl.getSemester() - 1) * 4)
							&& student.getClasses().size() < 10
							&& cl.getStudents().size() < this.courses.get(cl.getCourseId()).getCap()) {
						boolean addedStudent = student.addClass(cl.getPeriod() + (cl.getSemester() - 1) * 4, cl);
						if (addedStudent == true) {
							cl.addStudent(student);
						}
					}
				}
			}
		}
		// Distribute leftover classes to students with empty time slots
		for (Student student : this.students.values()) {
			// Search for similar courses if possible (same letters different level, e.g. CGC1D2 -> CGC1DG)
			if ((student.getClasses().size() < 8 && student.getGrade() < 12)
					|| (student.getClasses().size() < 6 && student.getGrade() == 12)) { // If not full courseload
				for (Class cl : this.classes) {
					// There are some courses we don't want to fill randomly into
					if (!cl.getCourseId().contains("ESL") && !cl.getCourseId().contains("PPL")
							&& !cl.getCourseId().equals("ZREMOT") && !cl.getCourseId().contains("AMR")) {
						if (cl.getCourseId().contains(cl.getCourseId().substring(0, 3))
								&& student.getClasses().size() < 9
								&& (!student.getClasses().containsKey(cl.getPeriod() + (cl.getSemester() - 1) * 4)
										&& cl.getStudents().size() < this.courses.get(cl.getCourseId()).getCap())
								&& (student.getGrade() <= Integer.parseInt(String.valueOf(cl.getCourseId().charAt(3)))
										+ 10
										&& (student.getGrade() >= Integer
												.parseInt(String.valueOf(cl.getCourseId().charAt(3))) + 8))) {
							if (student.getGrade() == 12
									&& student.getClasses().size() > student.getCourseRequests().length) {
								break;
							}
							boolean addedStudent = student.addClass(cl.getPeriod() + (cl.getSemester() - 1) * 4, cl);
							if (addedStudent == true) {
								cl.addStudent(student);
							}
						}
					}
				}
			}
			// Search through student's alternates
			if ((student.getClasses().size() < 8 && student.getGrade() < 12)
					|| (student.getClasses().size() < 6 && student.getGrade() == 12)) { // If not full courseload
				for (Class cl : this.classes) {
					for (String alternate : student.getAlternates()) {
						if (!cl.getCourseId().contains("ESL") && !cl.getCourseId().contains("PPL")
								&& !cl.getCourseId().equals("ZREMOT") && !cl.getCourseId().contains("AMR")) {
							if (cl.getCourseId().equals(alternate) && student.getClasses().size() < 9
									&& (!student.getClasses().containsKey(cl.getPeriod() + (cl.getSemester() - 1) * 4)
											&& cl.getStudents().size() < this.courses.get(cl.getCourseId()).getCap())
									&& (student.getGrade() < Integer
											.parseInt(String.valueOf(cl.getCourseId().charAt(3))) + 10
											&& (student.getGrade() >= Integer
													.parseInt(String.valueOf(cl.getCourseId().charAt(3))) + 8))) {
								if (student.getGrade() == 12
										&& student.getClasses().size() > student.getCourseRequests().length) {
									break;
								}
								boolean addedStudent = student.addClass(cl.getPeriod() + (cl.getSemester() - 1) * 4,
										cl);
								if (addedStudent == true) {
									cl.addStudent(student);
								}
							}
						}
					}
				}
			}
			// If student still does not have full course load, autofill them into an available class
			if ((student.getClasses().size() < 8 && student.getGrade() < 12)
					|| (student.getClasses().size() < 6 && student.getGrade() == 12)) {
				for (Class cl : this.classes) {
					if (!cl.getCourseId().contains("ESL") && !cl.getCourseId().contains("PPL")
							&& !cl.getCourseId().equals("ZREMOT") && !cl.getCourseId().contains("AMR")) {
						if ((student.getClasses().size()) < 9 && (!student.getClasses()
								.containsKey(cl.getPeriod() + (cl.getSemester() - 1) * 4)
								&& cl.getStudents().size() < this.courses.get(cl.getCourseId()).getCap()
								&& (student.getGrade() < Integer.parseInt(String.valueOf(cl.getCourseId().charAt(3)))
										+ 10
										&& (student.getGrade() >= Integer
												.parseInt(String.valueOf(cl.getCourseId().charAt(3))) + 8)))) {
							if (student.getGrade() == 12
									&& student.getClasses().size() > student.getCourseRequests().length) {
								break;
							}
							boolean addedStudent = student.addClass(cl.getPeriod() + (cl.getSemester() - 1) * 4, cl);
							if (addedStudent == true) {
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
				// Check if teacher is available
				if (classA.getTeacherId() == classB.getTeacherId() && classA.getPeriod() == classB.getPeriod()
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
			} else if ((classA.getCourseId().contains("HIF"))
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("Family")) {
				conflicts+=4;
			} else if (!classA.getCourseId().contains("PPL") 
					&& this.rooms.get(classA.getRoomId()).getRoomName().contains("gym")) {
				conflicts+=2;
			} else if (!classA.getCourseId().contains("AMI") 
					&& this.rooms.get(classA.getRoomId()).getRoomName().contains("music")) {
				conflicts+=3;
			} else if (!classA.getCourseId().contains("ADA") 
					&& this.rooms.get(classA.getRoomId()).getRoomName().contains("drama")) {
				conflicts+=3;
			} else if (!classA.getCourseId().contains("ICS") 
					&& this.rooms.get(classA.getRoomId()).getRoomName().contains("computer")) {
				conflicts+=2;
			} else if (!classA.getCourseId().contains("HIF") 
					&& this.rooms.get(classA.getRoomId()).getRoomName().contains("Family")) {
				conflicts+=3;
			}
			
			if (classA.getSemester() == 1) {
				semesterOneCount++;
			} else {
				semesterTwoCount++;
			}
		}

		// Make sure a similar number of courses in semester 1 and 2
		double semesterRatio = (double) (semesterOneCount / semesterTwoCount);
		if (semesterRatio > 1.5 || semesterRatio < 0.66666666666666) {
			conflicts += 100;
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

			// Check if teacher is available
			for (Class classB : this.classes) {
				if (classA.getTeacherId() == classB.getTeacherId() && classA.getPeriod() == classB.getPeriod()
						&& classA.getClassId() != classB.getClassId() && classA.getSemester() == classB.getSemester()) {
					System.out.println(this.teachers.get(classA.getTeacherId()).getTeacherName() + " conflicts with "
							+ this.teachers.get(classB.getTeacherId()).getTeacherName());
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
			}
		}
	}
}