package ua.foxminded.chyzhov.schoolconsoleapp.database.service.groups;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.chyzhov.schoolconsoleapp.Randomizer;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.groups.GroupRepository;
import ua.foxminded.chyzhov.schoolconsoleapp.entity.Group;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	GroupRepository groupRepository;

	private static final int DEFAULT_GROUP_COUNT = 10;

	private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

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

		groupRepository.save(group);

		logger.info("Group added, GroupName: {}", groupName);

	}

	@Override
	public List<String> getGroups() {

		List<Group> groups = groupRepository.findAll();

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

		List<Object[]> rows = groupRepository.findByLimitStudents(limit);

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
		Long count = groupRepository.count();

		boolean isEmpty = count == null || count == 0;
		logger.info("Checked if groups table is empty: {}", isEmpty);
		return isEmpty;
	}
}
