package ua.foxminded.chyzhov.schoolconsoleapp.database.service.groups;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class GroupService {

	private final GroupServiceImpl groupServiceImpl;

	public GroupService(GroupServiceImpl groupServiceImpl) {
		this.groupServiceImpl = groupServiceImpl;
	}

	@Transactional
	public void addGroup(String groupName) {
		groupServiceImpl.addGroup(groupName);
	}

	public List<String> getGroups() {
		return groupServiceImpl.getGroups();
	}

	public List<String> getGroupsWithLimitStudents(int limit) {
		return groupServiceImpl.getGroupsWithLimitStudents(limit);
	}

	public boolean isGroupsTableEmpty() {
		return groupServiceImpl.isGroupsTableEmpty();
	}

}
