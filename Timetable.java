import java.util.HashMap;

public class Timetable {
	private final HashMap<Integer, Room> rooms;
	private final HashMap<Integer, Teacher> teachers;
	private final HashMap<Integer, Student> students;
	private final HashMap<String, Course> courses;
//    private final HashMap<Integer, Group> groups;

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

	// makes a replica timetable
	Timetable(Timetable copy) {
		this.rooms = copy.getRooms();
		this.teachers = copy.getTeachers();
		this.students = copy.getStudents();
		this.courses = copy.getCourses();
		this.classes = copy.getClasses();
		this.numClasses = copy.getNumClasses();
	}

	// getters

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

	// creating classes from a chromosome
	public void createClasses(Individual individual) {

		// Get student's chromosome
		int chromosome[] = individual.getChromosome();
		int chromosomePos = 0;
		int classIndex = 0;

		for (int i = 0; i < numClasses; i++) {
			String courseCode = classes[i].getCourseId();

			// period
			this.classes[classIndex].setPeriod(chromosome[chromosomePos]);
			chromosomePos++;

			// room
			this.classes[classIndex].setRoomId(chromosome[chromosomePos]);
			chromosomePos++;

			// teacher
			this.classes[classIndex].setTeacherId(chromosome[chromosomePos]);
			chromosomePos++;

			// semester
			this.classes[classIndex].setSemester(chromosome[chromosomePos]);
			chromosomePos++;

			classIndex++;
		}
//        giveStudentsClasses();
	}

	public void giveStudentsClasses() {
		// all accomodated courses
		for (Student student : this.students.values()) {
			for (String courseCode : student.getCourseRequests()) {
				for (Class cl : this.classes) {
					//ensures that there are no conflicts with same class time, class capacity, etc.
					if (cl.getCourseId().equals(courseCode) && !student.getClasses().containsKey(cl.getPeriod() + (cl.getSemester() - 1) * 4)	
							&& student.getClasses().size() < 10
							&& cl.getStudents().size() < this.courses.get(cl.getCourseId()).getCap()) {
						student.addClass(cl.getPeriod() + (cl.getSemester() - 1) * 4, cl);
						cl.addStudent(student);
					}
				}
			}
			//alternates
			if ((student.getClasses().size() < 8 && student.getGrade() < 12) || (student.getClasses().size() < 6 && student.getGrade() == 12)) {		//if not full courseload
				for (Class cl : this.classes) {
					for (String alternate: student.getAlternates()){
						if (cl.getCourseId().equals(alternate) && student.getClasses().size() < 9 && (!student.getClasses().containsKey(cl.getPeriod() + (cl.getSemester() - 1) * 4)
								&& cl.getStudents().size() < this.courses.get(cl.getCourseId()).getCap())) {
							if (student.getGrade() == 12 && student.getClasses().size() > student.getCourseRequests().length) {
								break;
							}
							student.addClass(cl.getPeriod() + (cl.getSemester() - 1) * 4, cl);
							cl.addStudent(student);
						}
					}
				}
			}
			//autofill
			if ((student.getClasses().size() < 8 && student.getGrade() < 12) || (student.getClasses().size() < 6 && student.getGrade() == 12)) {
				for (Class cl : this.classes) {
					if ((student.getClasses().size()) < 9 && (!student.getClasses().containsKey(cl.getPeriod() + (cl.getSemester() - 1) * 4)
							&& cl.getStudents().size() < this.courses.get(cl.getCourseId()).getCap())) {
						if (student.getGrade() == 12 && student.getClasses().size() > student.getCourseRequests().length) {
							break;
						}
						student.addClass(cl.getPeriod() + (cl.getSemester() - 1) * 4, cl);
						cl.addStudent(student);
					}
				}
			}
		}
	}

	public int calculateConflicts() {
		int conflicts = 0;
		for (Class classA : this.classes) {
			for (Class classB : this.classes) {
				// Check if room is taken
				if (classA.getRoomId() == classB.getRoomId() && classA.getPeriod() == classB.getPeriod()
						&& classA.getClassId() != classB.getClassId() && classA.getSemester() == classB.getSemester()) {
					conflicts++;
					break;
				}
//                // Check if teacher is available
//                if (classA.getTeacherId() == classB.getTeacherId() && classA.getPeriod() == classB.getPeriod()
//                        && classA.getClassId() != classB.getClassId() && classA.getSemester() == classB.getSemester()) {
//                	conflicts++;
//                	break;
//                }
			}

			// room constraints
			if (classA.getCourseId().contains("TEJ")
					&& !this.rooms.get(classA.getRoomId()).getRoomName().equals("\"Technology room\"")) {
//            	System.out.println(classA.getClassId() + " is being taught " + classA.getCourseId() + " in room " + this.rooms.get(classA.getRoomId()).getRoomName());
				conflicts += 2;
			} else if (classA.getCourseId().contains("PPL")
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("gym")) {
				conflicts += 3;
			} else if (classA.getCourseId().contains("ICS")
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("computer")) {
				conflicts += 3;
			} else if (classA.getCourseId().contains("AMI")
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("music")) {
				conflicts += 3;
			} else if ((classA.getCourseId().contains("ADA") || (classA.getCourseId().contains("ADD")))
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("drama")) {
				conflicts += 3;
			}
//            else if ((classA.getCourseId().contains("HIF") || classA.getCourseId().contains("IDC")) && !this.rooms.get(classA.getRoomId()).getRoomName().contains("Family")) {
//	        	conflicts+=3;
//	        }
//            
			// semester balancing
			// similar classes in each semester

		}

		// student clashes

		// students should not have 2 classes in the same period and should have same #
		// classes per semester
//        for (Student student: this.students.values()) {
//        	for (Class classA: student.getClasses()) {
//        		for (Class classB: student.getClasses()) {
//        			if (classA.getPeriod() == classB.getPeriod() && classA.getClassId() != classB.getClassId() && classA.getSemester() == classB.getSemester()) {
//                    	conflicts++;
//                    	break;
//                    }
//        		}
//        	}
//        }
//        System.out.println(conflicts);

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

//			// Check if teacher is available
//			for (Class classB : this.classes) {
//				if (classA.getTeacherId() == classB.getTeacherId() && classA.getPeriod() == classB.getPeriod()
//						&& classA.getClassId() != classB.getClassId() && classA.getSemester() == classB.getSemester()) {
//					System.out.println(this.teachers.get(classA.getTeacherId()).getTeacherName() + " conflicts with "
//							+ this.teachers.get(classB.getTeacherId()).getTeacherName());
//						break;
//				}
//			}
			if (classA.getCourseId().contains("TEJ")
					&& !this.rooms.get(classA.getRoomId()).getRoomName().equals("\"Technology room\"")) {
				System.out.println(classA.getClassId() + " is being taught " + classA.getCourseId() + " in room "
						+ this.rooms.get(classA.getRoomId()).getRoomName());
			} else if (classA.getCourseId().contains("PPL")
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("gym")) {
				System.out.println(classA.getClassId() + " is being taught " + classA.getCourseId() + " in room "
						+ this.rooms.get(classA.getRoomId()).getRoomName());
			} else if (classA.getCourseId().contains("ICS")
					&& !this.rooms.get(classA.getRoomId()).getRoomName().contains("computer")) {
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
			}
//            else if ((classA.getCourseId().contains("HIF") || classA.getCourseId().contains("IDC")) && !this.rooms.get(classA.getRoomId()).getRoomName().contains("Family")) {
//            	System.out.println(classA.getClassId() + " is being taught " + classA.getCourseId() + " in room " + this.rooms.get(classA.getRoomId()).getRoomName());
//	        }
		}
	}

	public void printTimetable() {
		System.out.println("Room Size: " + rooms.size());
		System.out.println("Teacher Size: " + teachers.size());
		System.out.println("Student Size: " + students.size());
		System.out.println("Course Size: " + courses.size());

		System.out.println("Number of classes: " + numClasses);
	}

}