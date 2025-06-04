package ua.foxminded.chyzhov.schoolconsoleapp.database.service.courses;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.courses.CourseDao;

@Service
@Transactional
public class CourseService {

	private final CourseDao courseDao;

	public CourseService(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public void addCourse(String courseName, String courseDescription) {
		courseDao.addCourse(courseName, courseDescription);
	}

	public List<String> getCourses() {
		return courseDao.getCourses();
	}

	public boolean isCoursesTableEmpty() {
		return courseDao.isCoursesTableEmpty();
	}

}
