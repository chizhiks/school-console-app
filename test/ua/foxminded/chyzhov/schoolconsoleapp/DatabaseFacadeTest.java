package ua.foxminded.chyzhov.schoolconsoleapp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DatabaseFacadeTest {

	@BeforeAll
	static void setup() {
		DatabaseFacade.clearAllTables();
		DatabaseFacade.generateAllData();
	}

	@Test
	void getStudents_shouldReturnNonEmptyList_whenDatabaseIsInitialized() {
		List<String> students = DatabaseFacade.getStudents();
		assertNotNull(students);
		assertFalse(students.isEmpty());
	}

	@Test
	void getGroups_shouldReturnNonEmptyList_whenDatabaseIsInitialized() {
		List<String> groups = DatabaseFacade.getGroups();
		assertNotNull(groups);
		assertFalse(groups.isEmpty());
	}

	@Test
	void getCourses_shouldReturnNonEmptyList_whenDatabaseIsInitialized() {
		List<String> courses = DatabaseFacade.getCourses();
		assertNotNull(courses);
		assertFalse(courses.isEmpty());
	}

	@Test
	void getStudentsByCourse_shouldReturnStudents_whenDatabaseIsInitialized() {
		List<String> students = DatabaseFacade.getStudentsByCourse("Mathematics");
		assertNotNull(students);
		assertFalse(students.isEmpty());
	}

	@Test
	void assignStudentsToGroups_shouldAssignGroups_whenStudentsExist() {
		List<String> students = DatabaseFacade.getStudents();
		assertTrue(students.stream().anyMatch(s -> s.contains("Group")));
	}

	@Test
	void assignStudentsToCourses_shouldAssignStudents_whenCoursesExist() {
		List<String> students = DatabaseFacade.getStudentsByCourse("Mathematics");
		assertNotNull(students);
		assertFalse(students.isEmpty());
	}

	@Test
	void removeStudentFromCourse_shouldRemoveStudent_thenStudentIsAssignToCourse() {
		List<String> students = DatabaseFacade.getStudentsByCourse("Mathematics");
		String studentInfo = students.get(1);

		String[] parts = studentInfo.split("\\s+");

		if (parts.length > 1) {
			try {
				int student_id = Integer.parseInt(parts[1].replaceAll("\n", "").trim());

				DatabaseFacade.removeStudentFromCourse(student_id, 1);

				List<String> updatedList = DatabaseFacade.getStudentsByCourse("Mathematics");

				boolean isStillExists = updatedList.stream().anyMatch(s -> s.contains("" + student_id));

				assertFalse(isStillExists);
			} catch (NumberFormatException e) {
				System.out.println("Error parsing student ID: " + e.getMessage());
			}
		} else {
			System.out.println("Invalid student info format: " + studentInfo);
		}
	}

}
