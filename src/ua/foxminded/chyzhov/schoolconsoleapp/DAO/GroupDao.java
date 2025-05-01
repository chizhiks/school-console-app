package ua.foxminded.chyzhov.schoolconsoleapp.DAO;

import java.util.List;

public interface GroupDao {

	void generateGroups();

	void addGroup(String group_name);

	List<String> getGroups();

	List<String> getGroupsWithLimitStudents(int limit);
}
