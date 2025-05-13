package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.groups;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import ua.foxminded.chyzhov.schoolconsoleapp.dao.groups.GroupDao;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.groups.GroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { GroupService.class })
class GroupDaoImplIntegrationTest {

	@MockBean
	GroupDao groupDao;

	@Autowired
	private GroupService groupService;

	@Test
	void getGroups_shouldReturnNonEmptyList_whenDatabaseIsInitialized() {

		when(groupDao.getGroups()).thenReturn(List.of("KI-33", "CS-50", "FM-77"));

		List<String> groups = groupService.getGroups();

		assertNotNull(groups);
		assertEquals(3, groups.size());
		assertTrue(groups.contains("KI-33"));

		verify(groupDao, times(1)).getGroups();
	}

}
