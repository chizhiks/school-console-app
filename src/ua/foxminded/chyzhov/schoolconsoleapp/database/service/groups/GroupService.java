package ua.foxminded.chyzhov.schoolconsoleapp.database.service.groups;

import java.util.List;

import org.springframework.stereotype.Service;

import ua.foxminded.chyzhov.schoolconsoleapp.dao.groups.GroupDao;

@Service
public class GroupService {

	private final GroupDao groupDao;

	public GroupService(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public void generateGroups() {
		groupDao.generateGroups();
	}

	public void addGroup(String groupName) {
		groupDao.addGroup(groupName);
	}

	public List<String> getGroups() {
		return groupDao.getGroups();
	}

	public List<String> getGroupsWithLimitStudents(int limit) {
		return groupDao.getGroupsWithLimitStudents(limit);
	}
}
