package ua.foxminded.chyzhov.schoolconsoleapp.dao.students;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.foxminded.chyzhov.schoolconsoleapp.dao.exception.DaoException;

@Repository
public class StudentDaoImpl implements StudentDao {

	private static final int DEFAULT_STUDENT_COUNT = 200;

	private static final Logger logger = LoggerFactory.getLogger(StudentDaoImpl.class);

	private final JdbcTemplate jdbc;

	public StudentDaoImpl(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public void generateStudents() {

		String[] firstNames = { "John", "Emma", "Michael", "Olivia", "Daniel", "Sophia", "David", "Ava", "James",
				"Isabella", "William", "Mia", "Alexander", "Charlotte", "Ethan", "Amelia", "Benjamin", "Harper",
				"Henry", "Evelyn" };

		String[] lastNames = { "Smith", "Johnson", "Brown", "Taylor", "Anderson", "Thomas", "Jackson", "White",
				"Harris", "Martin", "Thompson", "Garcia", "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Lee",
				"Walker", "Hall" };

		String sql = "INSERT INTO school.students(first_name, last_name) VALUES (?, ?)";

		Random random = new Random();

		for (int i = 0; i < DEFAULT_STUDENT_COUNT; i++) {

			String firstName = firstNames[random.nextInt(firstNames.length)];
			String lastName = lastNames[random.nextInt(lastNames.length)];

			jdbc.update(sql, firstName, lastName);
		}

		logger.info("{} students were successfully generated", DEFAULT_STUDENT_COUNT);
	}

	@Override
	public void addStudent(int groupId, String firstName, String lastName) {

		String sql = "INSERT INTO school.students(group_id, first_name, last_name) values (?, ?, ?)";

		jdbc.update(sql, groupId, firstName, lastName);

		logger.info("Student added, GroupId = {}, FirstName = {}, LastName = {}", groupId, firstName, lastName);
	}

	@Override
	public void assignStudentsToGroups() {
		Random random = new Random();

		List<Integer> groupIDs = jdbc.queryForList("SELECT group_id FROM school.groups", Integer.class);

		List<Integer> studentIDs = jdbc.queryForList("SELECT student_id FROM school.students WHERE group_id IS NULL",
				Integer.class);

		for (int groupID : groupIDs) {
			int studentsInGroup = random.nextInt(21) + 10;

			for (int i = 0; i < studentsInGroup && !studentIDs.isEmpty(); i++) {
				int index = random.nextInt(studentIDs.size());
				int studentID = studentIDs.remove(index);

				jdbc.update("UPDATE school.students SET group_id = ? WHERE student_id = ?", groupID, studentID);
			}
		}

		logger.info("Students were successfully assigned to groups");

	}

	@Override
	public void assignStudentsToCourses() {
		Random random = new Random();

		List<Integer> studentIDs = jdbc.queryForList("SELECT student_id FROM school.students", Integer.class);

		List<Integer> courseIDs = jdbc.queryForList("SELECT course_id FROM school.courses", Integer.class);

		for (int studentID : studentIDs) {
			int numCourses = random.nextInt(3) + 1;
			List<Integer> selectedCourses = new ArrayList<>(courseIDs);

			for (int i = 0; i < numCourses && !selectedCourses.isEmpty(); i++) {
				int courseIndex = random.nextInt(selectedCourses.size());
				int courseID = selectedCourses.remove(courseIndex);

				jdbc.update("INSERT INTO school.students_courses(student_id, course_id) VALUES (?, ?)", studentID,
						courseID);
			}
		}

		logger.info("Students were successfully assigned to courses");

	}

	@Override
	public List<String> getStudents() {

		List<Map<String, Object>> rows = jdbc.queryForList("SELECT * FROM school.students");

		int maxFirstNameLength = 0;
		int maxLastNameLength = 0;

		for (Map<String, Object> row : rows) {
			String firstName = (String) row.get("first_name");
			String lastName = (String) row.get("last_name");

			if (firstName.length() > maxFirstNameLength) {
				maxFirstNameLength = firstName.length();
			}

			if (lastName.length() > maxLastNameLength) {
				maxLastNameLength = lastName.length();
			}
		}

		maxFirstNameLength += 2;
		maxLastNameLength += 2;

		List<String> result = new ArrayList<>();

		result.add("\nList of students in the database:\n");

		for (Map<String, Object> row : rows) {

			String studentInfo = String.format(
					"ID: %-5d | First Name: %-" + maxFirstNameLength + "s | Last Name: %-" + maxLastNameLength + "s",
					row.get("student_id"), row.get("first_name"), row.get("last_name"));

			result.add(studentInfo);
		}

		logger.info("Received {} students from the database", rows.size());

		return result;
	}

	@Override
	public List<String> getStudentsWithCourses() {

		List<String> result = jdbc.query("SELECT * FROM school.students_courses", (rs, rowNum) -> String
				.format("StudentID: %-5d | CourseID: %-5d", rs.getInt("student_id"), rs.getInt("course_id")));

		logger.info("Received {} student-course assignments from the database", result.size());

		return result;

	}

	@Override
	public List<String> getStudentsByCourse(String courseName) {

		String sql = """
				SELECT s.student_id, s.first_name, s.last_name
				FROM school.students s
				JOIN school.students_courses sc ON s.student_id = sc.student_id
				JOIN school.courses c ON sc.course_id = c.course_id
				WHERE c.course_name = ?
				""";

		List<Map<String, Object>> rows = jdbc.queryForList(sql, courseName);

		int maxFirstNameLength = 0;
		int maxLastNameLength = 0;

		for (Map<String, Object> row : rows) {
			String firstName = (String) row.get("first_name");
			String lastName = (String) row.get("last_name");

			if (firstName.length() > maxFirstNameLength) {
				maxFirstNameLength = firstName.length();
			}

			if (lastName.length() > maxLastNameLength) {
				maxLastNameLength = lastName.length();
			}
		}

		maxFirstNameLength += 2;
		maxLastNameLength += 2;

		List<String> result = new ArrayList<>();

		result.add("\nList of students in the course '" + courseName + "':\n");

		for (Map<String, Object> row : rows) {

			String studentInfo = String.format(
					"ID: %-5d First Name: %-" + maxFirstNameLength + "s Last Name: %-" + maxLastNameLength + "s",
					row.get("student_id"), row.get("first_name"), row.get("last_name"));

			result.add(studentInfo);
		}

		logger.info("Received {} students in the course '{}' from the database", rows.size(), courseName);

		return result;
	}

	@Override
	public List<String> getStudentsByGroup(String groupName) {

		String sql = """
				SELECT * FROM school.students s
				JOIN school.groups g ON s.group_id = g.group_id
				WHERE g.group_name = ?
				""";

		List<Map<String, Object>> rows = jdbc.queryForList(sql, groupName);

		int maxFirstNameLength = 0;
		int maxLastNameLength = 0;

		for (Map<String, Object> row : rows) {
			String firstName = (String) row.get("first_name");
			String lastName = (String) row.get("last_name");

			if (firstName.length() > maxFirstNameLength) {
				maxFirstNameLength = firstName.length();
			}

			if (lastName.length() > maxLastNameLength) {
				maxLastNameLength = lastName.length();
			}
		}

		maxFirstNameLength += 2;
		maxLastNameLength += 2;

		List<String> result = new ArrayList<>();

		result.add("\nList of students in the group '" + groupName + "':\n");

		for (Map<String, Object> row : rows) {

			String studentInfo = String.format(
					"ID: %-5d First Name: %-" + maxFirstNameLength + "s Last Name: %-" + maxLastNameLength + "s",
					row.get("student_id"), row.get("first_name"), row.get("last_name"));

			result.add(studentInfo);
		}

		logger.info("Received {} students in the group '{}' from the database", rows.size(), groupName);

		return result;
	}

	@Override
	public void deleteStudent(int studentId) throws DaoException {

		try {
			jdbc.update("DELETE FROM school.students_courses WHERE student_id = ?", studentId);
			int studentRows = jdbc.update("DELETE FROM school.students WHERE student_id = ?", studentId);

			if (studentRows == 0) {
				logger.warn("No student found with ID: {}", studentId);
			}

			logger.info("Student with ID: {} was successfully deleted", studentId);

		} catch (DataAccessException e) {
			logger.error("Failed to delete student with ID: {}", studentId, e);
			throw new DaoException("Failed to delete student with ID: " + studentId, e);
		}

	}

	@Override
	public void addStudentToCourse(int studentId, int courseId) throws DaoException {

		try {
			jdbc.update("INSERT INTO school.students_courses VALUES (?, ?)", studentId, courseId);
			logger.info("Student with ID: {} was successfully added to course with ID: {}", studentId, courseId);
		} catch (DataAccessException e) {
			logger.error("Failed to add student with ID: {} to course with ID: {}", studentId, courseId, e);
			throw new DaoException("Failed to add student with ID: " + studentId + " to course with ID: " + courseId,
					e);
		}

	}

	@Override
	public void removeStudentFromCourse(int studentId, int courseId) throws DaoException {

		try {
			int rows = jdbc.update("DELETE FROM school.students_courses WHERE student_id = ? AND course_id = ?",
					studentId, courseId);

			if (rows == 0) {
				logger.warn("No student found with ID " + studentId + " in course with ID" + courseId);
			}

			logger.info("Student with ID: {} was successfully removed from course with ID: {}", studentId, courseId);

		} catch (DataAccessException e) {
			logger.error("Failed to remove student with ID: {} from course with ID: {}", studentId, courseId, e);
			throw new DaoException(
					"Failed to remove student with ID: " + studentId + " from course with ID: " + courseId, e);
		}

	}

	@Override
	public boolean isStudentsTableEmpty() {
		String sql = "SELECT COUNT(*) FROM school.students";
		Integer count = jdbc.queryForObject(sql, Integer.class);
		boolean isEmpty = count == null || count == 0;
		logger.info("Checked if students table is empty: {}", isEmpty);
		return isEmpty;
	}
}
