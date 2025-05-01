package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.courses;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.foxminded.chyzhov.schoolconsoleapp.database.DatabaseFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CourseServiceTest {

	@Autowired
	private CourseService courseService;
	@Autowired
	private DatabaseFacade databaseFacade;

	@BeforeEach
	void setup() {
		databaseFacade.clearAllTables();
		databaseFacade.generateAllData();
	}

	@Test
	void getCourses_shouldReturnNonEmptyList_whenDatabaseIsInitialized() {
		List<String> courses = courseService.getCourses();
		assertNotNull(courses);
		assertFalse(courses.isEmpty());
	}

}
