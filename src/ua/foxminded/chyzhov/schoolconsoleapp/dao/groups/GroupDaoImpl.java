package ua.foxminded.chyzhov.schoolconsoleapp.dao.groups;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import ua.foxminded.chyzhov.schoolconsoleapp.Randomizer;
import ua.foxminded.chyzhov.schoolconsoleapp.entity.Group;

@Repository
public class GroupDaoImpl implements GroupDao {

	@PersistenceContext
	EntityManager em;

	private static final int DEFAULT_GROUP_COUNT = 10;

	private static final Logger logger = LoggerFactory.getLogger(GroupDaoImpl.class);

	@Override
	public void generateGroups() {

		Randomizer randomizer = new Randomizer();

		for (int i = 0; i < DEFAULT_GROUP_COUNT; i++) {
			addGroup(randomizer.getRandomGroupName());
		}

		logger.info("{} groups were generated successfully", DEFAULT_GROUP_COUNT);
	}

	@Override
	public void addGroup(String groupName) {

		Group group = new Group(groupName);

		em.persist(group);

		logger.info("Group added, GroupName: {}", groupName);

	}

	@Override
	public List<String> getGroups() {

		List<Group> groups = em.createQuery("SELECT g FROM Group g", Group.class).getResultList();

		List<String> result = new ArrayList<String>();

		result.add("\nList of groups in the database:\n");

		for (Group group : groups) {

			String groupInfo = String.format("ID: %-5d | Group Name: %-7s", group.getGroupId(), group.getGroupName());

			result.add(groupInfo);

		}

		logger.info("Received {} groups from the database", groups.size());

		return result;

	}

	@Override
	public List<String> getGroupsWithLimitStudents(int limit) {

		String sql = """
				SELECT g.group_id, g.group_name, count(student_id) AS student_count FROM school.groups g
				LEFT JOIN school.students s ON g.group_id = s.group_id
				GROUP BY g.group_id, g.group_name HAVING count(student_id) <= ?
				""";

		Query query = em.createNativeQuery(sql);
		query.setParameter(1, limit);

		List<Object[]> rows = query.getResultList();

		List<String> result = new ArrayList<>();

		for (Object[] row : rows) {
			String groupInfo = String.format("GroupID: %-5d Group name: %-8s Students amount: %-5d", row[0], row[1],
					row[2]);
			result.add(groupInfo);
		}

		logger.info("Received {} groups with the number of students less than or equal to {}", result.size(), limit);
		return result;
	}

	@Override
	public boolean isGroupsTableEmpty() {
		Integer count = em.createQuery("SELECT COUNT(g) FROM Group g", Integer.class).getSingleResult();
		boolean isEmpty = count == null || count == 0;
		logger.info("Checked if groups table is empty: {}", isEmpty);
		return isEmpty;
	}

}
