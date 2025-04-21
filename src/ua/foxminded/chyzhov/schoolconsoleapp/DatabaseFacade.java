package ua.foxminded.chyzhov.schoolconsoleapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseFacade {

	static final String JDBC_URL = "jdbc:postgresql://localhost:5432/school_database";
	static final String JDBC_USER = "postgres";
	static final String JDBC_PASSWORD = "1234";

	public static void clearAllTables() {
		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
				Statement statement = connection.createStatement()) {

			statement.executeUpdate("TRUNCATE TABLE school.students_courses RESTART IDENTITY CASCADE");
			statement.executeUpdate("TRUNCATE TABLE school.students RESTART IDENTITY CASCADE");
			statement.executeUpdate("TRUNCATE TABLE school.courses RESTART IDENTITY CASCADE");
			statement.executeUpdate("TRUNCATE TABLE school.groups RESTART IDENTITY CASCADE");

			System.out.println("All tables have been deleted successfully.");

		} catch (SQLException e) {
			System.out.println("Failed to clear all tables data.");
			e.printStackTrace();
		}
	}

	public static void getAllTables() {

		List<String> results = getStudents();

		for (String result : results) {
			System.out.println(result);
		}

		results = getGroups();

		for (String result : results) {
			System.out.println(result);
		}

		results = getCourses();

		for (String result : results) {
			System.out.println(result);
		}
	}

	public static List<String> getStudents() {

		List<String> result = new ArrayList<String>();

		int maxFirstNameLength = 0;
		int maxLastNameLength = 0;

		String query = "SELECT * FROM school.students";

		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
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

	public static List<String> getGroups() {

		List<String> result = new ArrayList<String>();

		String query = "SELECT * FROM school.groups";

		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query);) {

			result.add("\nList of groups in the database:\n");

			while (resultSet.next()) {

				String groupInfo = String.format("ID: %-5d Group Name: %-7s", resultSet.getInt("group_id"),
						resultSet.getString("group_name"));

				result.add(groupInfo);
			}

		} catch (SQLException e) {
			System.out.println("Failed to get groups data.");
			e.printStackTrace();
		}

		return result;
	}

	public static List<String> getGroupsWithLimitStudents(int limit) {

		List<String> result = new ArrayList<String>();

		String query = "SELECT g.group_id, g.group_name, count(student_id) AS student_count FROM school.groups g "
				+ "LEFT JOIN school.students s ON g.group_id = s.group_id "
				+ "GROUP BY g.group_id, g.group_name HAVING count(student_id) <= ?";

		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setInt(1, limit);
			ResultSet resultSet = statement.executeQuery();

			result.add("\nList of groups in the database with students limit - " + limit + ":\n");

			while (resultSet.next()) {
				String groupsInfo = String.format("GroupID: %-5d Group name: %-8s Students amount: %-5d",
						resultSet.getInt("group_id"), resultSet.getString("group_name"),
						resultSet.getInt("student_count"));

				result.add(groupsInfo);
			}

		} catch (SQLException e) {
			System.out.println("Failed to get groups with a limited number of students.");
			e.printStackTrace();
		}

		return result;
	}

	public static List<String> getCourses() {

		List<String> result = new ArrayList<String>();

		String query = "SELECT * FROM school.courses";

		int maxCourseNameLength = 0;
		int maxCourseDescLength = 0;

		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
				Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet resultSet = statement.executeQuery(query);) {

			result.add("\nList of courses in the database:\n");

			while (resultSet.next()) {
				maxCourseNameLength = Math.max(maxCourseNameLength, resultSet.getString("course_name").length());
				maxCourseDescLength = Math.max(maxCourseDescLength, resultSet.getString("course_description").length());
			}

			maxCourseNameLength += 2;
			maxCourseDescLength += 2;

			resultSet.beforeFirst();

			while (resultSet.next()) {

				String courseInfo = String.format(
						"ID: %-5d Course Name: %-" + maxCourseNameLength + "s Course Description: %-"
								+ maxCourseDescLength + "s",
						resultSet.getInt("course_id"), resultSet.getString("course_name"),
						resultSet.getString("course_description"));

				result.add(courseInfo);
			}

		} catch (SQLException e) {
			System.out.println("Failed to get course data.");
			e.printStackTrace();
		}

		return result;
	}

	public static List<String> getStudentsWithCourses() {

		List<String> result = new ArrayList<String>();

		String query = "SELECT * FROM school.students_courses";

		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
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

		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
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

	public static void generateAllData() {
		generateGroups();
		generateCourses();
		generateStudents();
		assignStudentsToGroups();
		assignStudentsToCourses();
		System.out.println("All data in tables have been generated successfully.");
	}

	public static void generateGroups() {

		Randomizer randomizer = new Randomizer();

		for (int i = 0; i < 10; i++) {
			addGroup(randomizer.getRandomGroupName());
		}
	}

	public static void generateCourses() {
		String[] courses = { "Mathematics", "Biology", "Chemistry", "Physics", "Computer Science", "History",
				"Geography", "Literature", "Philosophy", "Art" };

		for (String course : courses) {
			addCourse(course, course + " description");
		}

	}

	public static void generateStudents() {
		String[] firstNames = { "John", "Emma", "Michael", "Olivia", "Daniel", "Sophia", "David", "Ava", "James",
				"Isabella", "William", "Mia", "Alexander", "Charlotte", "Ethan", "Amelia", "Benjamin", "Harper",
				"Henry", "Evelyn" };

		String[] lastNames = { "Smith", "Johnson", "Brown", "Taylor", "Anderson", "Thomas", "Jackson", "White",
				"Harris", "Martin", "Thompson", "Garcia", "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Lee",
				"Walker", "Hall" };

		String sql = "INSERT INTO school.students(first_name, last_name) VALUES (?, ?)";

		Random random = new Random();

		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {

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

	public static void assignStudentsToGroups() {
		Random random = new Random();

		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
				Statement statement = connection.createStatement()) {

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

		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
				Statement statement = connection.createStatement()) {

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

	public static void addStudent(int group_id, String first_name, String last_name) {

		String sql = "INSERT INTO school.students(group_id, first_name, last_name) values (?, ?, ?)";

		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
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

	public static void addGroup(String group_name) {
		String sql = "INSERT INTO school.groups(group_name) values (?)";

		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, group_name);

			statement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Failed to add group.");
			e.printStackTrace();
		}
	}

	public static void addCourse(String course_name, String course_description) {
		String sql = "INSERT INTO school.courses(course_name, course_description) values (?, ?)";

		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, course_name);
			statement.setString(2, course_description);

			statement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Failed to add course.");
			e.printStackTrace();
		}
	}

	public static void deleteStudent(int student_id) {

		String query = "DELETE FROM school.students WHERE student_id = ?";

		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
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

		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
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

		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
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

	public static int getMaxRowsInTableAmount(String tableName) {

		String query = "SELECT COUNT(*) FROM school." + tableName;
		int result = 0;

		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
				PreparedStatement statement = connection.prepareStatement(query)) {

			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				result = resultSet.getInt(1);
			}

		} catch (SQLException e) {
			System.out.println("Failed to get max rows amount in table " + tableName + ".");
			e.printStackTrace();
		}

		return result;
	}

	public static int getNextRowInTable(String tableName) {

		int resultId = getMaxRowsInTableAmount(tableName) + 1;

		return resultId;
	}

}
