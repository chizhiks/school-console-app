package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.groups;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ua.foxminded.chyzhov.schoolconsoleapp.Randomizer;
import ua.foxminded.chyzhov.schoolconsoleapp.database.DbConnection;

public class GroupService {

	public static void generateGroups() {

		Randomizer randomizer = new Randomizer();

		for (int i = 0; i < 10; i++) {
			addGroup(randomizer.getRandomGroupName());
		}
	}

	public static void addGroup(String group_name) {
		String sql = "INSERT INTO school.groups(group_name) values (?)";

		try (Connection connection = DbConnection.getInstance();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, group_name);

			statement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Failed to add group.");
			e.printStackTrace();
		}
	}

	public static List<String> getGroups() {

		List<String> result = new ArrayList<String>();

		String query = "SELECT * FROM school.groups";

		try (Connection connection = DbConnection.getInstance();
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

		try (Connection connection = DbConnection.getInstance();
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
}
