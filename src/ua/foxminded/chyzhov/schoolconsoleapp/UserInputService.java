package ua.foxminded.chyzhov.schoolconsoleapp;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ua.foxminded.chyzhov.schoolconsoleapp.database.DatabaseFacade;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.GeneratorService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.courses.CourseService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.groups.GroupService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.students.StudentService;

@Component
public class UserInputService {

	private static final Logger logger = LoggerFactory.getLogger(UserInputService.class);

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

				logger.info("User chose menu option: {}", menuChoice);

				switch (menuChoice) {
				case CLEAR_TABLES:
					databaseFacade.clearAllTables();
					logger.info("All tables cleared successfully");
					break;
				case GENERATE_DATA:
					generatorService.generateAllData();
					logger.info("All data generated successfully");
					break;
				case VIEW_ALL_TABLES:
					databaseFacade.getAllTables();
					logger.info("All tables got successfully");
					break;
				case VIEW_ALL_STUDENTS:
					List<String> results = studentService.getStudents();

					logger.info("Received {} students from the database", results.size());

					for (String result : results) {
						System.out.println(result);
					}
					break;
				case VIEW_ALL_GROUPS:
					results = groupService.getGroups();

					logger.info("Received {} groups from the database", results.size());

					for (String result : results) {
						System.out.println(result);
					}

					break;
				case VIEW_ALL_COURSES:
					results = courseService.getCourses();

					logger.info("Received {} courses from the database", results.size());

					for (String result : results) {
						System.out.println(result);
					}

					break;
				case FIND_GROUPS_BY_MAX_STUDENTS:
					System.out.printf("Enter max student count: ");
					int maxCount = sc.nextInt();
					sc.nextLine();
					results = groupService.getGroupsWithLimitStudents(maxCount);

					logger.info("Received {} groups with the number of students less than or equal to {}",
							results.size(), maxCount);

					for (String result : results) {
						System.out.println(result);
					}

					break;
				case FIND_STUDENTS_BY_COURSE:
					System.out.printf("\nTo search for students, enter the name of the course: ");
					String courseName = sc.nextLine();
					results = studentService.getStudentsByCourse(courseName);

					logger.info("Received {} students in the course '{}' from the database", results.size(),
							courseName);

					for (String result : results) {
						System.out.println(result);
					}

					break;
				case ADD_STUDENT:
					int maxGroupID = databaseFacade.getMaxRowsInTableAmount("groups");
					int groupId = 0;

					while (true) {
						System.out
								.printf("\nEnter the group_id (1 - " + maxGroupID + ") to which to add the student: ");
						groupId = sc.nextInt();
						sc.nextLine();
						if (groupId > maxGroupID || groupId < 1) {
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

					studentService.addStudent(groupId, firstName, lastName);
					logger.info("Student added: GroupId = {}, FirstName = {}, LastName = {}", groupId, firstName,
							lastName);
					break;
				case DELETE_STUDENT:
					int studentId;
					int maxStudentID = databaseFacade.getMaxRowsInTableAmount("students");

					while (true) {
						System.out.printf("\nEnter the student_id (1 - " + maxStudentID + ") to delete the student: ");
						studentId = sc.nextInt();
						sc.nextLine();
						if (studentId > maxStudentID || studentId < 1) {
							System.out.println("student_id must be from 1 to " + maxStudentID);
							continue;
						} else {
							break;
						}
					}

					studentService.deleteStudent(studentId);
					logger.info("Student with ID: {} was successfully deleted", studentId);
					break;
				case ADD_STUDENT_TO_COURSE:
					int courseId;
					maxStudentID = databaseFacade.getMaxRowsInTableAmount("students");
					int maxCourseID = databaseFacade.getMaxRowsInTableAmount("courses");

					while (true) {
						System.out.printf(
								"\nEnter the student_id (1 - " + maxStudentID + ") to add the student to the course: ");
						studentId = sc.nextInt();
						sc.nextLine();

						System.out.printf(
								"\nEnter the course_id (1 - " + maxCourseID + ") to add the student to the course: ");
						courseId = sc.nextInt();
						sc.nextLine();

						if (studentId > maxStudentID || studentId < 1 || courseId > maxCourseID || courseId < 1) {
							System.out.println("student_id must be from 1 to " + maxStudentID);
							System.out.println("course_id must be from 1 to " + maxCourseID);
							continue;
						} else {
							break;
						}
					}

					studentService.addStudentToCourse(studentId, courseId);
					logger.info("Student with ID: {} was successfully added to course with ID: {}", studentId,
							courseId);

					break;
				case REMOVE_STUDENT_FROM_COURSE:
					maxStudentID = databaseFacade.getMaxRowsInTableAmount("students");
					maxCourseID = databaseFacade.getMaxRowsInTableAmount("courses");

					while (true) {
						System.out.printf("\nEnter the student_id (1 - " + maxStudentID
								+ ") to remove the student from the course: ");
						studentId = sc.nextInt();
						sc.nextLine();

						System.out.printf("\nEnter the course_id (1 - " + maxCourseID
								+ ") to remove the student from the course: ");
						courseId = sc.nextInt();
						sc.nextLine();

						if (studentId > maxStudentID || studentId < 1 || courseId > maxCourseID || courseId < 1) {
							System.out.println("student_id must be from 1 to " + maxStudentID);
							System.out.println("course_id must be from 1 to " + maxCourseID);
							continue;
						} else {
							break;
						}
					}

					studentService.removeStudentFromCourse(studentId, courseId);
					logger.info("Student with ID: {} was successfully removed from course with ID: {}", studentId,
							courseId);
					break;
				case EXIT:
					logger.info("User exited the application.");
					System.out.println("\nExiting...");
					sc.close();
					return;
				default:
					logger.warn("User selected invalid menu option: {}", menuChoice);
					System.out.println("Invalid choice. Try again.");
				}
			} catch (InputMismatchException e) {
				logger.warn("User input mismatch exception: {}", e.getMessage());
				System.out.println("Invalid input. Please try again.");
				sc.nextLine();
			} catch (Exception e) {
				logger.error("Unexpected error occurred: ", e);
			}
		}

	}

}