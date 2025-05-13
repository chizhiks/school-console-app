package ua.foxminded.chyzhov.schoolconsoleapp.dao.courses;

import java.util.List;

public interface CourseDao {

	void generateCourses();

	void addCourse(String courseName, String courseDescription);

	List<String> getCourses();

	boolean isCoursesTableEmpty();
}
