package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.students;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import ua.foxminded.chyzhov.schoolconsoleapp.database.DatabaseFacade;
import ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.courses.CourseService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.groups.GroupService;

@SpringBootTest
class StudentServiceIntegrationTest {

	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private StudentService studentService;
	@Autowired
	private CourseService courseService;
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
	void getStudents_shouldReturnNonEmptyList_whenDatabaseIsInitialized() {
		List<String> students = studentService.getStudents();
		assertNotNull(students);
		assertFalse(students.isEmpty());
	}

	@Test
	void getStudentsByCourse_shouldReturnStudents_whenDatabaseIsInitialized() {
		List<String> students = studentService.getStudentsByCourse("Mathematics");
		assertNotNull(students);
		assertFalse(students.isEmpty());
	}

	@Test
	void assignStudentsToGroups_shouldAssignGroups_whenStudentsExist() {

		List<String> groups = groupService.getGroups();
		assertFalse(groups.isEmpty());

		String firstGroup = groups.get(1);

		String groupId = firstGroup.split(" ")[1];

		List<String> studentsInGroup = jdbc.queryForList("SELECT first_name FROM school.students WHERE group_id = ?",
				String.class, Integer.parseInt(groupId));

		assertFalse(studentsInGroup.isEmpty());
	}

	@Test
	void assignStudentsToCourses_shouldAssignStudents_whenCoursesExist() {
		List<String> students = studentService.getStudentsByCourse("Mathematics");
		students.addAll(studentService.getStudentsByCourse("Biology"));
		assertNotNull(students);
		assertFalse(students.isEmpty());
	}

	@Test
	void removeStudentFromCourse_shouldRemoveStudent_thenStudentIsAssignToCourse() {
		studentService.addStudent(1, "Test Student", "Test Student");
		int studentId = 201;
		courseService.addCourse("Test Course", "Test Course Description");
		int courseId = 11;
		studentService.addStudentToCourse(studentId, courseId);

		List<String> studentsBefore = studentService.getStudentsByCourse("Test Course");
		assertTrue(studentsBefore.stream().anyMatch(s -> s.contains("" + studentId)));

		studentService.removeStudentFromCourse(studentId, courseId);

		List<String> studentsAfter = studentService.getStudentsByCourse("Test Course");
		boolean isStillExists = studentsAfter.stream().anyMatch(s -> s.contains("" + studentId));
		assertFalse(isStillExists);
	}

}
