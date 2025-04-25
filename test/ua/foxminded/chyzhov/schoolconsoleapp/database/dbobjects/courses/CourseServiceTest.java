package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.courses;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ua.foxminded.chyzhov.schoolconsoleapp.database.DatabaseFacade;

class CourseServiceTest {

	@BeforeAll
	static void setup() {
		DatabaseFacade.clearAllTables();
		DatabaseFacade.generateAllData();
	}

	@Test
	void getCourses_shouldReturnNonEmptyList_whenDatabaseIsInitialized() {
		List<String> courses = CourseService.getCourses();
		assertNotNull(courses);
		assertFalse(courses.isEmpty());
	}

}
