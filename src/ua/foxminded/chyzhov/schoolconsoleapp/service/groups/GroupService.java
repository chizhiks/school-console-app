package ua.foxminded.chyzhov.schoolconsoleapp.service.groups;

import java.util.List;

public interface GroupService {

	public void addGroup(String groupName);

	public List<String> getGroups();

	public List<String> getGroupsWithLimitStudents(int limit);

	public boolean isGroupsTableEmpty();

}
