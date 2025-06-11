package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.groups;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.chyzhov.schoolconsoleapp.service.groups.GroupService;
import ua.foxminded.chyzhov.schoolconsoleapp.service.groups.GroupServiceImpl;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { GroupService.class,
		GroupServiceImpl.class }))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/sql/clear_tables.sql",
		"/sql/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GroupServiceIntegrationTest {

	@Autowired
	GroupService groupService;

	@Test
	void getGroups_shouldReturnNonEmptyList_whenDatabaseIsInitialized() {

		List<String> groups = groupService.getGroups();

		assertNotNull(groups);
		assertEquals(3, groups.size());

		boolean containsAA11 = groups.stream().anyMatch(s -> s.contains("AA-11"));
		boolean containsAB12 = groups.stream().anyMatch(s -> s.contains("AB-12"));
		assertTrue(containsAA11 && containsAB12);
	}

}
