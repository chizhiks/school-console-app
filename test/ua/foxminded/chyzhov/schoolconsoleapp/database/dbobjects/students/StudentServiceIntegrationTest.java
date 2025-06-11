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
import ua.foxminded.chyzhov.schoolconsoleapp.service.groups.GroupService;
import ua.foxminded.chyzhov.schoolconsoleapp.service.groups.GroupServiceImpl;
import ua.foxminded.chyzhov.schoolconsoleapp.service.students.StudentService;
import ua.foxminded.chyzhov.schoolconsoleapp.service.students.StudentServiceImpl;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { StudentService.class,
		StudentServiceImpl.class, GroupService.class, GroupServiceImpl.class }))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/sql/clear_tables.sql",
		"/sql/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StudentServiceIntegrationTest {

	@Autowired
	private StudentService studentService;

	@Autowired
	private GroupService groupService;

	@Test
	void getStudents_shouldReturnNonEmptyList_whenDatabaseIsInitialized() {
		List<String> students = studentService.getStudents();

		assertNotNull(students);

		assertTrue(students.size() > 1);

		boolean containsTestStudentA = students.stream().anyMatch(s -> s.contains("Test") && s.contains("Student A"));
		assertTrue(containsTestStudentA);
	}

	@Test
	void getStudentsByCourse_shouldReturnStudents_whenDatabaseIsInitialized() {
		List<String> students = studentService.getStudentsByCourse("Mathematics");

		assertNotNull(students);
		assertTrue(students.size() > 1);

		boolean containsTestStudentA = students.stream().anyMatch(s -> s.contains("Test") && s.contains("Student A"));
		assertTrue(containsTestStudentA);
	}

	@Test
	void getStudentsByGroup_shouldReturnStudents_whenStudentsExistInGroup() {
		List<String> groups = groupService.getGroups();
		assertFalse(groups.isEmpty());

		String firstGroupLine = groups.get(1);
		String groupName = extractGroupName(firstGroupLine);

		List<String> studentsInGroup = studentService.getStudentsByGroup(groupName);

		assertNotNull(studentsInGroup);
		assertTrue(studentsInGroup.size() > 1);
	}

	@Test
	void getStudentsByCourse_shouldReturnCorrectStudents_whenCoursesExist() {
		List<String> mathStudents = studentService.getStudentsByCourse("Mathematics");
		assertNotNull(mathStudents);
		assertTrue(mathStudents.size() > 1);

		List<String> testCourseStudents = studentService.getStudentsByCourse("Test Course");
		assertNotNull(testCourseStudents);
		assertTrue(testCourseStudents.size() > 1);
	}

	@Test
	void addAndRemoveStudentFromCourse_shouldWorkCorrectly() throws DaoException {

		int studentId = 1; // Test Student A
		int courseId = 2; // Test Course

		studentService.addStudentToCourse(studentId, courseId);

		List<String> studentsInCourse = studentService.getStudentsByCourse("Test Course");
		boolean studentFound = studentsInCourse.stream().anyMatch(s -> s.contains("Test") && s.contains("Student A"));
		assertTrue(studentFound, "Student should be in the course after adding");

		studentService.removeStudentFromCourse(studentId, courseId);

		List<String> studentsAfterRemoval = studentService.getStudentsByCourse("Test Course");
		boolean studentStillExists = studentsAfterRemoval.stream()
				.anyMatch(s -> s.contains("Test") && s.contains("Student A"));
		assertFalse(studentStillExists, "Student should not be in the course after removal");
	}

	@Test
	void deleteStudent_shouldRemoveStudentAndRelatedRecords() throws DaoException {
		studentService.addStudent(1, "ToDelete", "Student");

		List<String> studentsBefore = studentService.getStudents();
		int countBefore = studentsBefore.size() - 1;

		studentService.deleteStudent(3);

		List<String> studentsAfter = studentService.getStudents();
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