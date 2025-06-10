package ua.foxminded.chyzhov.schoolconsoleapp.database.service.courses;

import java.util.List;

public interface CourseService {

	public void addCourse(String courseName, String courseDescription);

	public List<String> getCourses();

	public boolean isCoursesTableEmpty();

}
