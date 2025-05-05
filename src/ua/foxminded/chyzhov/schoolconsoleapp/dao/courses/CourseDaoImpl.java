package ua.foxminded.chyzhov.schoolconsoleapp.dao.courses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CourseDaoImpl implements CourseDao {

	private final JdbcTemplate jdbc;

	public CourseDaoImpl(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public void generateCourses() {
		String[] courses = { "Mathematics", "Biology", "Chemistry", "Physics", "Computer Science", "History",
				"Geography", "Literature", "Philosophy", "Art" };

		for (String course : courses) {
			addCourse(course, course + " description");
		}

	}

	@Override
	public void addCourse(String courseName, String courseDescription) {

		jdbc.update("INSERT INTO school.courses(course_name, course_description) VALUES (?, ?)", courseName,
				courseDescription);

	}

	@Override
	public List<String> getCourses() {

		String sql = "SELECT * FROM school.courses";

		List<Map<String, Object>> rows = jdbc.queryForList(sql);

		int maxCourseNameLength = 0;
		int maxCourseDescLength = 0;

		for (Map<String, Object> row : rows) {
			String courseName = (String) row.get("course_name");
			String courseDesc = (String) row.get("course_description");

			if (courseName.length() > maxCourseNameLength) {
				maxCourseNameLength = courseName.length();
			}

			if (courseDesc.length() > maxCourseDescLength) {
				maxCourseDescLength = courseDesc.length();
			}

		}

		maxCourseNameLength += 2;
		maxCourseDescLength += 2;

		List<String> result = new ArrayList<String>();

		result.add("\nList of courses in the database:\n");

		for (Map<String, Object> row : rows) {
			String courseInfo = String.format("ID: %-5d Course Name: %-" + maxCourseNameLength
					+ "s Course Description: %-" + maxCourseDescLength + "s", row.get("course_id"),
					row.get("course_name"), row.get("course_description"));

			result.add(courseInfo);
		}

		return result;
	}

}
