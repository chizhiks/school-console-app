package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.students;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.chyzhov.schoolconsoleapp.dao.exception.DaoException;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.groups.GroupDao;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.groups.GroupDaoImpl;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.students.StudentDao;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.students.StudentDaoImpl;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { StudentDaoImpl.class,
		GroupDaoImpl.class }))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/sql/clear_tables.sql",
		"/sql/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StudentDaoIntegrationTest {

	@Autowired
	private StudentDao studentDao;

	@Autowired
	private GroupDao groupDao;

	@Test
	void getStudents_shouldReturnNonEmptyList_whenDatabaseIsInitialized() {
		List<String> students = studentDao.getStudents();

		assertNotNull(students);

		assertTrue(students.size() > 1);

		boolean containsTestStudentA = students.stream().anyMatch(s -> s.contains("Test") && s.contains("Student A"));
		assertTrue(containsTestStudentA);
	}

	@Test
	void getStudentsByCourse_shouldReturnStudents_whenDatabaseIsInitialized() {
		List<String> students = studentDao.getStudentsByCourse("Mathematics");

		assertNotNull(students);
		assertTrue(students.size() > 1);

		boolean containsTestStudentA = students.stream().anyMatch(s -> s.contains("Test") && s.contains("Student A"));
		assertTrue(containsTestStudentA);
	}

	@Test
	void getStudentsByGroup_shouldReturnStudents_whenStudentsExistInGroup() {
		List<String> groups = groupDao.getGroups();
		assertFalse(groups.isEmpty());

		String firstGroupLine = groups.get(1);
		String groupName = extractGroupName(firstGroupLine);

		List<String> studentsInGroup = studentDao.getStudentsByGroup(groupName);

		assertNotNull(studentsInGroup);
		assertTrue(studentsInGroup.size() > 1);
	}

	@Test
	void getStudentsByCourse_shouldReturnCorrectStudents_whenCoursesExist() {
		List<String> mathStudents = studentDao.getStudentsByCourse("Mathematics");
		assertNotNull(mathStudents);
		assertTrue(mathStudents.size() > 1);

		List<String> testCourseStudents = studentDao.getStudentsByCourse("Test Course");
		assertNotNull(testCourseStudents);
		assertTrue(testCourseStudents.size() > 1);
	}

	@Test
	void addAndRemoveStudentFromCourse_shouldWorkCorrectly() throws DaoException {

		int studentId = 1; // Test Student A
		int courseId = 2; // Test Course

		studentDao.addStudentToCourse(studentId, courseId);

		List<String> studentsInCourse = studentDao.getStudentsByCourse("Test Course");
		boolean studentFound = studentsInCourse.stream().anyMatch(s -> s.contains("Test") && s.contains("Student A"));
		assertTrue(studentFound, "Student should be in the course after adding");

		studentDao.removeStudentFromCourse(studentId, courseId);

		List<String> studentsAfterRemoval = studentDao.getStudentsByCourse("Test Course");
		boolean studentStillExists = studentsAfterRemoval.stream()
				.anyMatch(s -> s.contains("Test") && s.contains("Student A"));
		assertFalse(studentStillExists, "Student should not be in the course after removal");
	}

	@Test
	void deleteStudent_shouldRemoveStudentAndRelatedRecords() throws DaoException {
		studentDao.addStudent(1, "ToDelete", "Student");

		List<String> studentsBefore = studentDao.getStudents();
		int countBefore = studentsBefore.size() - 1;

		studentDao.deleteStudent(3);

		List<String> studentsAfter = studentDao.getStudents();
		int countAfter = studentsAfter.size() - 1;

		assertEquals(countBefore - 1, countAfter, "Student count should decrease by 1");
	}

	private String extractGroupName(String groupLine) {

		if (groupLine.contains("AA-11"))
			return "AA-11";
		if (groupLine.contains("AB-12"))
			return "AB-12";

		String[] parts = groupLine.trim().split("\\s+");
		return parts[parts.length - 1];
	}
}