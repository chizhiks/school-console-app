package ua.foxminded.chyzhov.schoolconsoleapp;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import ua.foxminded.chyzhov.schoolconsoleapp.database.DatabaseFacade;
import ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.courses.CourseService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.groups.GroupService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.students.StudentService;

public class UserInputService {

	public static void start() {

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
				case 1:
					DatabaseFacade.clearAllTables();
					break;
				case 2:
					DatabaseFacade.generateAllData();
					break;
				case 3:
					DatabaseFacade.getAllTables();
					break;
				case 4:
					List<String> results = StudentService.getStudents();

					for (String result : results) {
						System.out.println(result);
					}
					break;
				case 5:
					results = GroupService.getGroups();

					for (String result : results) {
						System.out.println(result);
					}

					break;
				case 6:
					results = CourseService.getCourses();

					for (String result : results) {
						System.out.println(result);
					}

					break;
				case 7:
					System.out.printf("Enter max student count: ");
					int maxCount = sc.nextInt();
					sc.nextLine();
					results = GroupService.getGroupsWithLimitStudents(maxCount);

					for (String result : results) {
						System.out.println(result);
					}

					break;
				case 8:
					System.out.printf("\nTo search for students, enter the name of the course: ");
					String courseName = sc.nextLine();
					results = StudentService.getStudentsByCourse(courseName);

					for (String result : results) {
						System.out.println(result);
					}

					break;
				case 9:
					int maxGroupID = DatabaseFacade.getMaxRowsInTableAmount("groups");
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

					StudentService.addStudent(group_id, firstName, lastName);
					break;
				case 10:
					int student_id;
					int maxStudentID = DatabaseFacade.getMaxRowsInTableAmount("students");

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

					StudentService.deleteStudent(student_id);
					break;
				case 11:
					int course_id;
					maxStudentID = DatabaseFacade.getMaxRowsInTableAmount("students");
					int maxCourseID = DatabaseFacade.getMaxRowsInTableAmount("courses");

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

					StudentService.addStudentToCourse(student_id, course_id);
					break;
				case 12:
					maxStudentID = DatabaseFacade.getMaxRowsInTableAmount("students");
					maxCourseID = DatabaseFacade.getMaxRowsInTableAmount("courses");

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

					StudentService.removeStudentFromCourse(student_id, course_id);
					break;
				case 13:
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