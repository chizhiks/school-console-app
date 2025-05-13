package ua.foxminded.chyzhov.schoolconsoleapp;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import ua.foxminded.chyzhov.schoolconsoleapp.database.DatabaseFacade;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.GeneratorService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.courses.CourseService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.groups.GroupService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.students.StudentService;

@Component
public class UserInputService {

	private static final int CLEAR_TABLES = 1;
	private static final int GENERATE_DATA = 2;
	private static final int VIEW_ALL_TABLES = 3;
	private static final int VIEW_ALL_STUDENTS = 4;
	private static final int VIEW_ALL_GROUPS = 5;
	private static final int VIEW_ALL_COURSES = 6;
	private static final int FIND_GROUPS_BY_MAX_STUDENTS = 7;
	private static final int FIND_STUDENTS_BY_COURSE = 8;
	private static final int ADD_STUDENT = 9;
	private static final int DELETE_STUDENT = 10;
	private static final int ADD_STUDENT_TO_COURSE = 11;
	private static final int REMOVE_STUDENT_FROM_COURSE = 12;
	private static final int EXIT = 13;

	private final GroupService groupService;
	private final CourseService courseService;
	private final StudentService studentService;
	private final GeneratorService generatorService;
	private final DatabaseFacade databaseFacade;

	public UserInputService(GroupService groupService, CourseService courseService, StudentService studentService,
			GeneratorService generatorService, DatabaseFacade databaseFacade) {
		this.groupService = groupService;
		this.courseService = courseService;
		this.studentService = studentService;
		this.generatorService = generatorService;
		this.databaseFacade = databaseFacade;
	}

	public void start() {

		Scanner sc = new Scanner(System.in);

		while (true) {

			try {
				System.out.println("\n~~~~~~~~~~~~~~~~~~~ SCHOOL APP MENU ~~~~~~~~~~~~~~~~~~~");
				System.out.println("1 - Clear all records of all tables");
				System.out.println("2 - Generate records for tables");
				System.out.println("3 - View all tables");
				System.out.println("4 - View all students");
				System.out.println("5 - View all groups");
				System.out.println("6 - View all courses");
				System.out.println("7 - Find all groups with less than (or equal) number of students");
				System.out.println("8 - Find all students associated with the course with the given title");
				System.out.println("9 - Add a new student");
				System.out.println("10 - Delete student by student_id");
				System.out.println("11 - Add a student to the course (from the list)");
				System.out.println("12 - Remove a student from one of the courses");
				System.out.println("13 - Exit");
				System.out.printf("\nChoose an option: ");

				int menuChoice = sc.nextInt();
				sc.nextLine();

				switch (menuChoice) {
				case CLEAR_TABLES:
					databaseFacade.clearAllTables();
					break;
				case GENERATE_DATA:
					generatorService.generateAllData();
					break;
				case VIEW_ALL_TABLES:
					databaseFacade.getAllTables();
					break;
				case VIEW_ALL_STUDENTS:
					List<String> results = studentService.getStudents();

					for (String result : results) {
						System.out.println(result);
					}
					break;
				case VIEW_ALL_GROUPS:
					results = groupService.getGroups();

					for (String result : results) {
						System.out.println(result);
					}

					break;
				case VIEW_ALL_COURSES:
					results = courseService.getCourses();

					for (String result : results) {
						System.out.println(result);
					}

					break;
				case FIND_GROUPS_BY_MAX_STUDENTS:
					System.out.printf("Enter max student count: ");
					int maxCount = sc.nextInt();
					sc.nextLine();
					results = groupService.getGroupsWithLimitStudents(maxCount);

					for (String result : results) {
						System.out.println(result);
					}

					break;
				case FIND_STUDENTS_BY_COURSE:
					System.out.printf("\nTo search for students, enter the name of the course: ");
					String courseName = sc.nextLine();
					results = studentService.getStudentsByCourse(courseName);

					for (String result : results) {
						System.out.println(result);
					}

					break;
				case ADD_STUDENT:
					int maxGroupID = databaseFacade.getMaxRowsInTableAmount("groups");
					int group_id = 0;

					while (true) {
						System.out
								.printf("\nEnter the group_id (1 - " + maxGroupID + ") to which to add the student: ");
						group_id = sc.nextInt();
						sc.nextLine();
						if (group_id > maxGroupID || group_id < 1) {
							System.out.println("group_id must be from 1 to " + maxGroupID);
							continue;
						} else {
							break;
						}
					}

					System.out.printf("Enter the first name: ");
					String firstName = sc.nextLine();

					System.out.printf("Enter the last name: ");
					String lastName = sc.nextLine();

					studentService.addStudent(group_id, firstName, lastName);
					break;
				case DELETE_STUDENT:
					int student_id;
					int maxStudentID = databaseFacade.getMaxRowsInTableAmount("students");

					while (true) {
						System.out.printf("\nEnter the student_id (1 - " + maxStudentID + ") to delete the student: ");
						student_id = sc.nextInt();
						sc.nextLine();
						if (student_id > maxStudentID || student_id < 1) {
							System.out.println("student_id must be from 1 to " + maxStudentID);
							continue;
						} else {
							break;
						}
					}

					studentService.deleteStudent(student_id);
					break;
				case ADD_STUDENT_TO_COURSE:
					int course_id;
					maxStudentID = databaseFacade.getMaxRowsInTableAmount("students");
					int maxCourseID = databaseFacade.getMaxRowsInTableAmount("courses");

					while (true) {
						System.out.printf(
								"\nEnter the student_id (1 - " + maxStudentID + ") to add the student to the course: ");
						student_id = sc.nextInt();
						sc.nextLine();

						System.out.printf(
								"\nEnter the course_id (1 - " + maxCourseID + ") to add the student to the course: ");
						course_id = sc.nextInt();
						sc.nextLine();

						if (student_id > maxStudentID || student_id < 1 || course_id > maxCourseID || course_id < 1) {
							System.out.println("student_id must be from 1 to " + maxStudentID);
							System.out.println("course_id must be from 1 to " + maxCourseID);
							continue;
						} else {
							break;
						}
					}

					studentService.addStudentToCourse(student_id, course_id);
					break;
				case REMOVE_STUDENT_FROM_COURSE:
					maxStudentID = databaseFacade.getMaxRowsInTableAmount("students");
					maxCourseID = databaseFacade.getMaxRowsInTableAmount("courses");

					while (true) {
						System.out.printf("\nEnter the student_id (1 - " + maxStudentID
								+ ") to remove the student from the course: ");
						student_id = sc.nextInt();
						sc.nextLine();

						System.out.printf("\nEnter the course_id (1 - " + maxCourseID
								+ ") to remove the student from the course: ");
						course_id = sc.nextInt();
						sc.nextLine();

						if (student_id > maxStudentID || student_id < 1 || course_id > maxCourseID || course_id < 1) {
							System.out.println("student_id must be from 1 to " + maxStudentID);
							System.out.println("course_id must be from 1 to " + maxCourseID);
							continue;
						} else {
							break;
						}
					}

					studentService.removeStudentFromCourse(student_id, course_id);
					break;
				case EXIT:
					System.out.println("\nExiting...");
					sc.close();
					return;
				default:
					System.out.println("Invalid choice. Try again.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please try again.");
				sc.nextLine();
			} catch (Exception e) {
				System.out.println("An error occurred: " + e.getMessage());
			}
		}

	}

}