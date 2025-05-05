package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.courses;

import java.util.List;

import org.springframework.stereotype.Service;

import ua.foxminded.chyzhov.schoolconsoleapp.DAO.CourseDao;

@Service
public class CourseService {

	private final CourseDao courseDao;

	public CourseService(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public void generateCourses() {
		courseDao.generateCourses();
	}

	public void addCourse(String courseName, String courseDescription) {
		courseDao.addCourse(courseName, courseDescription);
	}

	public List<String> getCourses() {
		return courseDao.getCourses();
	}

}
