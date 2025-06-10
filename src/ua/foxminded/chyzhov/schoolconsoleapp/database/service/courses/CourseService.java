package ua.foxminded.chyzhov.schoolconsoleapp.database.service.courses;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class CourseService {

	
	private final CourseServiceImpl courseServiceImpl;

	public CourseService(CourseServiceImpl courseServiceImpl) {
		this.courseServiceImpl = courseServiceImpl;
	}

	@Transactional
	public void addCourse(String courseName, String courseDescription) {
		courseServiceImpl.addCourse(courseName, courseDescription);
	}

	public List<String> getCourses() {
		return courseServiceImpl.getCourses();
	}

	public boolean isCoursesTableEmpty() {
		return courseServiceImpl.isCoursesTableEmpty();
	}

}
