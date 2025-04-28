package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.courses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ua.foxminded.chyzhov.schoolconsoleapp.database.DbConnection;

public class CourseService {

	public static void generateCourses() {
		String[] courses = { "Mathematics", "Biology", "Chemistry", "Physics", "Computer Science", "History",
				"Geography", "Literature", "Philosophy", "Art" };

		for (String course : courses) {
			addCourse(course, course + " description");
		}

	}

	public static void addCourse(String course_name, String course_description) {
		String sql = "INSERT INTO school.courses(course_name, course_description) values (?, ?)";

		try (Connection connection = DbConnection.getInstance();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, course_name);
			statement.setString(2, course_description);

			statement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Failed to add course.");
			e.printStackTrace();
		}
	}

	public static List<String> getCourses() {

		List<String> result = new ArrayList<String>();

		String query = "SELECT * FROM school.courses";

		int maxCourseNameLength = 0;
		int maxCourseDescLength = 0;

		try (Connection connection = DbConnection.getInstance();
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

}
