package ua.foxminded.chyzhov.schoolconsoleapp.dao.groups;

import java.util.List;

public interface GroupDao {

	void generateGroups();

	void addGroup(String groupName);

	List<String> getGroups();

	List<String> getGroupsWithLimitStudents(int limit);
}
