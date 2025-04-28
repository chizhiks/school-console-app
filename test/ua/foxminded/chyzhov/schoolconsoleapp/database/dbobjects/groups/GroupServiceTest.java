package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.groups;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ua.foxminded.chyzhov.schoolconsoleapp.database.DatabaseFacade;

class GroupServiceTest {

	@BeforeAll
	static void setup() {
		DatabaseFacade.clearAllTables();
		DatabaseFacade.generateAllData();
	}

	@Test
	void getGroups_shouldReturnNonEmptyList_whenDatabaseIsInitialized() {
		List<String> groups = GroupService.getGroups();
		assertNotNull(groups);
		assertFalse(groups.isEmpty());
	}

}
