package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.students;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import ua.foxminded.chyzhov.schoolconsoleapp.DAO.StudentDao;

@Service
public class StudentService implements StudentDao {

	private final JdbcTemplate jdbc;

	public StudentService(JdbcTemplate jdbc) {
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

		for (int i = 0; i < 200; i++) {

			String firstName = firstNames[random.nextInt(firstNames.length)];
			String lastName = lastNames[random.nextInt(lastNames.length)];

			jdbc.update(sql, firstName, lastName);
		}
	}

	@Override
	public void addStudent(int group_id, String first_name, String last_name) {

		String sql = "INSERT INTO school.students(group_id, first_name, last_name) values (?, ?, ?)";

		jdbc.update(sql, group_id, first_name, last_name);
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

		return result;
	}

	@Override
	public List<String> getStudentsWithCourses() {

		return jdbc.query("SELECT * FROM school.students_courses", (rs, rowNum) -> String
				.format("StudentID: %-5d | CourseID: %-5d", rs.getInt("student_id"), rs.getInt("course_id")));

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

		return result;
	}

	@Override
	public void deleteStudent(int student_id) {

		jdbc.update("DELETE FROM school.students_courses WHERE student_id = ?", student_id);
		jdbc.update("DELETE FROM school.students WHERE student_id = ?", student_id);

	}

	@Override
	public void addStudentToCourse(int student_id, int course_id) {

		jdbc.update("INSERT INTO school.students_courses VALUES (?, ?)", student_id, course_id);

	}

	@Override
	public void removeStudentFromCourse(int student_id, int course_id) {

		jdbc.update("DELETE FROM school.students_courses WHERE student_id = ? AND course_id = ?", student_id,
				course_id);

	}
}
