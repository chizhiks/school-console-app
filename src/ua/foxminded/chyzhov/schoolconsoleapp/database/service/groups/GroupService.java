package ua.foxminded.chyzhov.schoolconsoleapp.database.service.groups;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ua.foxminded.chyzhov.schoolconsoleapp.dao.groups.GroupDao;

@Service
public class GroupService {

	private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

	private final GroupDao groupDao;

	public GroupService(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public void addGroup(String groupName) {
		groupDao.addGroup(groupName);
		logger.info("Group added: GroupName: {}", groupName);
	}

	public List<String> getGroups() {
		List<String> groups = groupDao.getGroups();
		logger.info("Received {} groups from the database", groups.size());
		return groups;
	}

	public List<String> getGroupsWithLimitStudents(int limit) {
		List<String> groups = groupDao.getGroupsWithLimitStudents(limit);
		logger.info("Received {} groups with the number of students less than or equal to {}", groups.size(), limit);
		return groups;
	}

	public boolean isGroupsTableEmpty() {
		boolean isEmpty = groupDao.isGroupsTableEmpty();
		logger.info("Checked if groups table is empty: {}", isEmpty);
		return isEmpty;
	}

}
