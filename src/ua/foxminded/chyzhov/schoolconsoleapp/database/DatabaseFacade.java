package ua.foxminded.chyzhov.schoolconsoleapp.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.courses.CourseService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.groups.GroupService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.students.StudentService;

public class DatabaseFacade {

	public static void clearAllTables() {
		try (Connection connection = DbConnection.getInstance(); Statement statement = connection.createStatement()) {

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

		List<String> results = StudentService.getStudents();

		for (String result : results) {
			System.out.println(result);
		}

		results = GroupService.getGroups();

		for (String result : results) {
			System.out.println(result);
		}

		results = CourseService.getCourses();

		for (String result : results) {
			System.out.println(result);
		}
	}

	public static void generateAllData() {
		GroupService.generateGroups();
		CourseService.generateCourses();
		StudentService.generateStudents();
		StudentService.assignStudentsToGroups();
		StudentService.assignStudentsToCourses();
		System.out.println("All data in tables have been generated successfully.");
	}

	public static int getMaxRowsInTableAmount(String tableName) {

		String query = "SELECT COUNT(*) FROM school." + tableName;
		int result = 0;

		try (Connection connection = DbConnection.getInstance();
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
