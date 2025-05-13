package ua.foxminded.chyzhov.schoolconsoleapp.DAO;

import java.util.List;

public interface CourseDao {

	void generateCourses();

	void addCourse(String courseName, String courseDescription);

	List<String> getCourses();
}
