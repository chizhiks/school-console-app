package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.courses;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.chyzhov.schoolconsoleapp.dao.courses.CourseDao;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.courses.CourseDaoImpl;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		CourseDaoImpl.class }))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/sql/clear_tables.sql",
		"/sql/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CourseDaoIntegrationTest {

	@Autowired
	CourseDao courseDao;

	@Test
	void getCourses_shouldReturnNonEmptyList_whenDatabaseIsInitialized() {

		List<String> courses = courseDao.getCourses();

		assertNotNull(courses);
		assertEquals(3, courses.size());

		boolean containsMathematics = courses.stream()
				.anyMatch(s -> s.contains("Mathematics") && s.contains("Basic Mathematics Course"));
		boolean containsTestCourse = courses.stream()
				.anyMatch(s -> s.contains("Test Course") && s.contains("Test Course Description"));
		assertTrue(containsMathematics && containsTestCourse);
	}

}
