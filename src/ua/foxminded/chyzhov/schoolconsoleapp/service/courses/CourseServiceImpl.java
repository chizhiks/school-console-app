package ua.foxminded.chyzhov.schoolconsoleapp.service.courses;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.chyzhov.schoolconsoleapp.dao.courses.CourseRepository;
import ua.foxminded.chyzhov.schoolconsoleapp.entity.Course;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	CourseRepository courseRepository;

	private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

	public void generateCourses() {
		String[] courses = { "Mathematics", "Biology", "Chemistry", "Physics", "Computer Science", "History",
				"Geography", "Literature", "Philosophy", "Art" };

		for (String course : courses) {
			addCourse(course, course + " description");
		}

		logger.info("{} courses were generated successfully", courses.length);

	}

	@Override
	public void addCourse(String courseName, String courseDescription) {

		Course course = new Course(courseName, courseDescription);

		courseRepository.save(course);

		logger.info("Course added, CourseName: {}, CourseDescription: {}", courseName, courseDescription);

	}

	@Override
	public List<String> getCourses() {

		List<Course> courses = courseRepository.findAll();

		int maxCourseNameLength = 0;
		int maxCourseDescLength = 0;

		for (Course course : courses) {
			String courseName = (String) course.getCourseName();
			String courseDesc = (String) course.getCourseDescription();

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

		for (Course course : courses) {
			String courseInfo = String.format("ID: %-5d Course Name: %-" + maxCourseNameLength
					+ "s Course Description: %-" + maxCourseDescLength + "s", course.getCourseId(),
					course.getCourseName(), course.getCourseDescription());

			result.add(courseInfo);
		}

		logger.info("Received {} courses from the database", courses.size());

		return result;
	}

	@Override
	public boolean isCoursesTableEmpty() {
		Long count = courseRepository.count();

		boolean isEmpty = count == null || count == 0;
		logger.info("Checked if courses table is empty: {}", isEmpty);
		return isEmpty;
	}

}
