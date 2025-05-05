package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.groups;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.foxminded.chyzhov.schoolconsoleapp.database.DatabaseFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GroupServiceIntegrationTest {

	@Autowired
	private GroupService groupService;
	@Autowired
	private DatabaseFacade databaseFacade;

	@BeforeEach
	void setup() {
		databaseFacade.clearAllTables();
		databaseFacade.generateAllData();
	}

	@Test
	void getGroups_shouldReturnNonEmptyList_whenDatabaseIsInitialized() {
		List<String> groups = groupService.getGroups();
		assertNotNull(groups);
		assertFalse(groups.isEmpty());
	}

}
