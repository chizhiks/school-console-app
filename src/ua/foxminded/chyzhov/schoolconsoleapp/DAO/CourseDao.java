package ua.foxminded.chyzhov.schoolconsoleapp.DAO;

import java.util.List;

public interface CourseDao {

	void generateCourses();

	void addCourse(String course_name, String course_description);

	List<String> getCourses();
}
