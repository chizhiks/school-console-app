package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.courses;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import ua.foxminded.chyzhov.schoolconsoleapp.dao.courses.CourseDao;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.courses.CourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { CourseService.class })
class CourseDaoImplIntegrationTest {

	@MockBean
	CourseDao courseDao;

	@Autowired
	private CourseService courseService;

	@Test
	void getCourses_shouldReturnNonEmptyList_whenDatabaseIsInitialized() {

		when(courseDao.getCourses()).thenReturn(List.of("Math", "Physics", "Biology"));

		List<String> courses = courseService.getCourses();

		assertNotNull(courses);
		assertEquals(3, courses.size());
		assertTrue(courses.contains("Math"));

		verify(courseDao, times(1)).getCourses();
	}

}
