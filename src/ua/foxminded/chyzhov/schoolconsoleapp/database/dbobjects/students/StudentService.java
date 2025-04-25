package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.students;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ua.foxminded.chyzhov.schoolconsoleapp.database.DbConnection;

public class StudentService {

	public static void generateStudents() {
		String[] firstNames = { "John", "Emma", "Michael", "Olivia", "Daniel", "Sophia", "David", "Ava", "James",
				"Isabella", "William", "Mia", "Alexander", "Charlotte", "Ethan", "Amelia", "Benjamin", "Harper",
				"Henry", "Evelyn" };

		String[] lastNames = { "Smith", "Johnson", "Brown", "Taylor", "Anderson", "Thomas", "Jackson", "White",
				"Harris", "Martin", "Thompson", "Garcia", "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Lee",
				"Walker", "Hall" };

		String sql = "INSERT INTO school.students(first_name, last_name) VALUES (?, ?)";

		Random random = new Random();

		try (Connection connection = DbConnection.getInstance()) {

			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				for (int i = 0; i < 200; i++) {

					String firstName = firstNames[random.nextInt(firstNames.length)];
					String lastName = lastNames[random.nextInt(lastNames.length)];

					statement.setString(1, firstName);
					statement.setString(2, lastName);
					statement.executeUpdate();
				}
			}

		} catch (SQLException e) {
			System.out.println("Failed to generate 200 students.");
			e.printStackTrace();
		}
	}

	public static void addStudent(int group_id, String first_name, String last_name) {

		String sql = "INSERT INTO school.students(group_id, first_name, last_name) values (?, ?, ?)";

		try (Connection connection = DbConnection.getInstance();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, group_id);
			statement.setString(2, first_name);
			statement.setString(3, last_name);

			statement.executeUpdate();

			System.out.println("Student has been added successfully.");

		} catch (SQLException e) {
			System.out.println("Failed to add student.");
			e.printStackTrace();
		}
	}

	public static void assignStudentsToGroups() {
		Random random = new Random();

		try (Connection connection = DbConnection.getInstance(); Statement statement = connection.createStatement()) {

			ResultSet groupResultSet = statement.executeQuery("SELECT group_id FROM school.groups");
			List<Integer> groupIDs = new ArrayList<>();
			while (groupResultSet.next()) {
				groupIDs.add(groupResultSet.getInt("group_id"));
			}

			ResultSet studentResultSet = statement
					.executeQuery("SELECT student_id FROM school.students WHERE group_id IS NULL");
			List<Integer> studentIDs = new ArrayList<>();
			while (studentResultSet.next()) {
				studentIDs.add(studentResultSet.getInt("student_id"));
			}

			PreparedStatement update = connection
					.prepareStatement("UPDATE school.students SET group_id = ? WHERE student_id = ?");

			for (int groupID : groupIDs) {
				int studentsInGroup = random.nextInt(21) + 10;

				for (int i = 0; i < studentsInGroup && !studentIDs.isEmpty(); i++) {
					int index = random.nextInt(studentIDs.size());
					int studentID = studentIDs.remove(index);

					update.setInt(1, groupID);
					update.setInt(2, studentID);
					update.executeUpdate();
				}
			}

		} catch (SQLException e) {
			System.out.println("Failed to assign students to groups.");
			e.printStackTrace();
		}

	}

	public static void assignStudentsToCourses() {
		Random random = new Random();

		try (Connection connection = DbConnection.getInstance(); Statement statement = connection.createStatement()) {

			ResultSet studentResultSet = statement.executeQuery("SELECT student_id FROM school.students");
			List<Integer> studentIDs = new ArrayList<>();
			while (studentResultSet.next()) {
				studentIDs.add(studentResultSet.getInt("student_id"));
			}

			ResultSet courseResultSet = statement.executeQuery("SELECT course_id FROM school.courses");
			List<Integer> courseIDs = new ArrayList<>();
			while (courseResultSet.next()) {
				courseIDs.add(courseResultSet.getInt("course_id"));
			}

			PreparedStatement insert = connection
					.prepareStatement("INSERT INTO school.students_courses(student_id, course_id) VALUES (?, ?)");

			for (int studentID : studentIDs) {
				int numCourses = random.nextInt(3) + 1;
				List<Integer> selectedCourses = new ArrayList<>(courseIDs);

				for (int i = 0; i < numCourses && !selectedCourses.isEmpty(); i++) {
					int courseIndex = random.nextInt(selectedCourses.size());
					int courseID = selectedCourses.remove(courseIndex);

					insert.setInt(1, studentID);
					insert.setInt(2, courseID);
					insert.executeUpdate();
				}
			}

		} catch (SQLException e) {
			System.out.println("Failed to assign students to courses.");
			e.printStackTrace();
		}

	}

	public static List<String> getStudents() {

		List<String> result = new ArrayList<String>();

		int maxFirstNameLength = 0;
		int maxLastNameLength = 0;

		String query = "SELECT * FROM school.students";

		try (Connection connection = DbConnection.getInstance();
				Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet resultSet = statement.executeQuery(query);) {

			while (resultSet.next()) {
				maxFirstNameLength = Math.max(maxFirstNameLength, resultSet.getString("first_name").length());
				maxLastNameLength = Math.max(maxLastNameLength, resultSet.getString("last_name").length());
			}

			maxFirstNameLength += 2;
			maxLastNameLength += 2;

			resultSet.beforeFirst();

			result.add("\nList of students in the database:\n");

			while (resultSet.next()) {

				String studentInfo = String.format(
						"ID: %-5d Group: %-5d First Name: %-" + maxFirstNameLength + "s Last Name: %-"
								+ maxLastNameLength + "s",
						resultSet.getInt("student_id"), resultSet.getInt("group_id"), resultSet.getString("first_name"),
						resultSet.getString("last_name"));

				result.add(studentInfo);
			}

		} catch (SQLException e) {
			System.out.println("Failed to get students data.");
			e.printStackTrace();
		}

		return result;
	}

	public static List<String> getStudentsWithCourses() {

		List<String> result = new ArrayList<String>();

		String query = "SELECT * FROM school.students_courses";

		try (Connection connection = DbConnection.getInstance();
				Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet resultSet = statement.executeQuery(query);) {

			resultSet.beforeFirst();

			result.add("\nList of students with theirs courses in the database:\n");

			while (resultSet.next()) {

				String studentCourseInfo = String.format("StudentID: %-5d CourseID: %-5d",
						resultSet.getInt("student_id"), resultSet.getInt("course_id"));

				result.add(studentCourseInfo);
			}

		} catch (SQLException e) {
			System.out.println("Failed to get student-course data.");
			e.printStackTrace();
		}

		return result;
	}

	public static List<String> getStudentsByCourse(String courseName) {

		List<String> result = new ArrayList<String>();

		String query = "SELECT s.student_id, s.first_name, s.last_name " + "FROM school.students s "
				+ "JOIN school.students_courses sc ON s.student_id = sc.student_id "
				+ "JOIN school.courses c ON sc.course_id = c.course_id " + "WHERE c.course_name = ?";

		int maxFirstNameLength = 0;
		int maxLastNameLength = 0;

		try (Connection connection = DbConnection.getInstance();
				PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY)) {

			statement.setString(1, courseName);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				maxFirstNameLength = Math.max(maxFirstNameLength, resultSet.getString("first_name").length());
				maxLastNameLength = Math.max(maxLastNameLength, resultSet.getString("last_name").length());
			}

			maxFirstNameLength += 2;
			maxLastNameLength += 2;

			resultSet.beforeFirst();

			result.add("\nList of students in the course '" + courseName + "':\n");

			while (resultSet.next()) {

				String studentInfo = String.format(
						"ID: %-5d First Name: %-" + maxFirstNameLength + "s Last Name: %-" + maxLastNameLength + "s",
						resultSet.getInt("student_id"), resultSet.getString("first_name"),
						resultSet.getString("last_name"));

				result.add(studentInfo);
			}

		} catch (SQLException e) {
			System.out.println("Failed to get students by course's name.");
			e.printStackTrace();
		}

		return result;
	}

	public static void deleteStudent(int student_id) {

		String query = "DELETE FROM school.students WHERE student_id = ?";

		try (Connection connection = DbConnection.getInstance();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setInt(1, student_id);
			statement.executeUpdate();

			System.out.println("Student has been deleted successfully.");

		} catch (SQLException e) {
			System.out.println("Failed to delete student by student_id.");
			e.printStackTrace();
		}
	}

	public static void addStudentToCourse(int student_id, int course_id) {

		String query = "INSERT INTO school.students_courses VALUES (?, ?)";

		try (Connection connection = DbConnection.getInstance();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setInt(1, student_id);
			statement.setInt(2, course_id);
			statement.executeUpdate();

			System.out.println("Student has been added to course successfully.");

		} catch (SQLException e) {
			System.out.println("Failed to add student to course.");
			e.printStackTrace();
		}
	}

	public static void removeStudentFromCourse(int student_id, int course_id) {

		String query = "DELETE FROM school.students_courses WHERE student_id = ? AND course_id = ?";

		try (Connection connection = DbConnection.getInstance();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setInt(1, student_id);
			statement.setInt(2, course_id);
			statement.executeUpdate();

			System.out.println("Student has been removed from course successfully.");

		} catch (SQLException e) {
			System.out.println("Failed to remove student from course.");
			e.printStackTrace();
		}
	}

}
