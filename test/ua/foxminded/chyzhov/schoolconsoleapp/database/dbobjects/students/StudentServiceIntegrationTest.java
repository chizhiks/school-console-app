package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.students;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.foxminded.chyzhov.schoolconsoleapp.dao.courses.CourseDao;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.groups.GroupDao;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.students.StudentDao;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.students.StudentService;

@SpringBootTest(classes = { StudentService.class })
class StudentServiceIntegrationTest {

	@MockBean
	private StudentDao studentDao;

	@MockBean
	private CourseDao courseDao;

	@MockBean
	private GroupDao groupDao;

	@Autowired
	private StudentService studentService;

	@Test
	void getStudents_shouldReturnNonEmptyList_whenDatabaseIsInitialized() {

		when(studentDao.getStudents()).thenReturn(List.of("Test Student A", "Test Student B"));

		List<String> students = studentService.getStudents();

		assertNotNull(students);
		assertEquals(2, students.size());
		assertTrue(students.contains("Test Student A"));
	}

	@Test
	void getStudentsByCourse_shouldReturnStudents_whenDatabaseIsInitialized() {

		when(studentDao.getStudentsByCourse("Mathematics")).thenReturn(List.of("Test Student A"));

		List<String> students = studentService.getStudentsByCourse("Mathematics");

		assertNotNull(students);
		assertFalse(students.isEmpty());
		assertTrue(students.contains("Test Student A"));
	}

	@Test
	void assignStudentsToGroups_shouldAssignGroups_whenStudentsExist() {

		when(groupDao.getGroups()).thenReturn(List.of("AA-11", "AB-12"));

		List<String> groups = groupDao.getGroups();
		assertFalse(groups.isEmpty());

		String firstGroup = groups.get(0);

		String groupName = firstGroup;

		when(studentDao.getStudentsByGroup(groupName)).thenReturn(List.of("Test Student A", "Test Student B"));

		List<String> studentsInGroup = studentDao.getStudentsByGroup(groupName);

		assertFalse(studentsInGroup.isEmpty());

		assertTrue(studentsInGroup.contains("Test Student A"));
		assertTrue(studentsInGroup.contains("Test Student B"));
	}

	@Test
	void assignStudentsToCourses_shouldAssignStudents_whenCoursesExist() {

		when(studentDao.getStudentsByCourse("Mathematics")).thenReturn(List.of("Test Student A", "Test Student B"));

		List<String> students = studentService.getStudentsByCourse("Mathematics");

		assertNotNull(students);
		assertFalse(students.isEmpty());
		assertTrue(students.contains("Test Student A"));
		assertTrue(students.contains("Test Student B"));
	}

	@Test
	void removeStudentFromCourse_shouldRemoveStudent_thenStudentIsAssignToCourse() {

		when(studentDao.getStudentsByCourse("Test Course")).thenReturn(List.of("Student 201"));

		int studentId = 201;
		int courseId = 11;

		studentService.addStudentToCourse(studentId, courseId);

		List<String> studentsBefore = studentService.getStudentsByCourse("Test Course");
		assertTrue(studentsBefore.contains("Student 201"));

		studentService.removeStudentFromCourse(studentId, courseId);

		when(studentDao.getStudentsByCourse("Test Course")).thenReturn(List.of());

		List<String> studentsAfter = studentService.getStudentsByCourse("Test Course");
		boolean isStillExists = studentsAfter.contains("Student 201");
		assertFalse(isStillExists);
	}

}
