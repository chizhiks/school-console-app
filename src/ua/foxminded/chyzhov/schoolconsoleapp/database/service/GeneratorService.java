package ua.foxminded.chyzhov.schoolconsoleapp.database.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ua.foxminded.chyzhov.schoolconsoleapp.dao.courses.CourseDao;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.groups.GroupDao;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.students.StudentDao;

@Service
public class GeneratorService {

	private static final Logger logger = LoggerFactory.getLogger(GeneratorService.class);

	private final StudentDao studentDao;
	private final GroupDao groupDao;
	private final CourseDao courseDao;

	public GeneratorService(StudentDao studentDao, GroupDao groupDao, CourseDao courseDao) {
		this.studentDao = studentDao;
		this.groupDao = groupDao;
		this.courseDao = courseDao;
	}

	public void generateAllData() {
		generateGroups();
		generateCourses();
		generateStudents();
		assignStudentsToGroups();
		assignStudentsToCourses();
		logger.info("All data in tables have been generated successfully.");
	}

	public void generateGroups() {
		groupDao.generateGroups();
	}

	public void generateCourses() {
		courseDao.generateCourses();
	}

	public void generateStudents() {
		studentDao.generateStudents();
	}

	public void assignStudentsToGroups() {
		studentDao.assignStudentsToGroups();
	}

	public void assignStudentsToCourses() {
		studentDao.assignStudentsToCourses();
	}

}
