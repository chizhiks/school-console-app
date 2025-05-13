package ua.foxminded.chyzhov.schoolconsoleapp.dao.groups;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.foxminded.chyzhov.schoolconsoleapp.Randomizer;

@Repository
public class GroupDaoImpl implements GroupDao {

	private final JdbcTemplate jdbc;

	public GroupDaoImpl(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public void generateGroups() {

		Randomizer randomizer = new Randomizer();

		for (int i = 0; i < 10; i++) {
			addGroup(randomizer.getRandomGroupName());
		}
	}

	@Override
	public void addGroup(String groupName) {
		String sql = "INSERT INTO school.groups(group_name) values (?)";

		jdbc.update(sql, groupName);

	}

	@Override
	public List<String> getGroups() {

		List<Map<String, Object>> rows = jdbc.queryForList("SELECT * FROM school.groups");

		List<String> result = new ArrayList<String>();

		result.add("\nList of groups in the database:\n");

		for (Map<String, Object> row : rows) {

			String groupInfo = String.format("ID: %-5d | Group Name: %-7s", row.get("group_id"), row.get("group_name"));

			result.add(groupInfo);

		}

		return result;

	}

	@Override
	public List<String> getGroupsWithLimitStudents(int limit) {

		String sql = """
				SELECT g.group_id, g.group_name, count(student_id) AS student_count FROM school.groups g
				LEFT JOIN school.students s ON g.group_id = s.group_id
				GROUP BY g.group_id, g.group_name HAVING count(student_id) <= ?
				""";

		return jdbc.query(sql, new Object[] { limit },
				(rs, rowNum) -> String.format("GroupID: %-5d Group name: %-8s Students amount: %-5d",
						rs.getInt("group_id"), rs.getString("group_name"), rs.getInt("student_count")));
	}

	@Override
	public boolean isGroupsTableEmpty() {
		String sql = "SELECT COUNT(*) FROM school.groups";
		Integer count = jdbc.queryForObject(sql, Integer.class);
		return count == null || count == 0;
	}

}
