package ua.foxminded.chyzhov.schoolconsoleapp.database.service.courses;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ua.foxminded.chyzhov.schoolconsoleapp.dao.courses.CourseDao;

@Service
public class CourseService {

	private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

	private final CourseDao courseDao;

	public CourseService(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public void addCourse(String courseName, String courseDescription) {
		courseDao.addCourse(courseName, courseDescription);
		logger.info("Course added: CourseName: {}, CourseDescription: {}", courseName, courseDescription);
	}

	public List<String> getCourses() {
		List<String> courses = courseDao.getCourses();
		logger.info("Received {} courses from the database", courses.size());
		return courses;
	}

	public boolean isCoursesTableEmpty() {
		boolean isEmpty = courseDao.isCoursesTableEmpty();
		logger.info("Checked if courses table is empty: {}", isEmpty);
		return isEmpty;
	}

}
