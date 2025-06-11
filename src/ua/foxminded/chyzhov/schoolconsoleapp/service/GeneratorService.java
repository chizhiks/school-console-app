package ua.foxminded.chyzhov.schoolconsoleapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ua.foxminded.chyzhov.schoolconsoleapp.service.courses.CourseServiceImpl;
import ua.foxminded.chyzhov.schoolconsoleapp.service.groups.GroupServiceImpl;
import ua.foxminded.chyzhov.schoolconsoleapp.service.students.StudentServiceImpl;

@Service
public class GeneratorService {

	private static final Logger logger = LoggerFactory.getLogger(GeneratorService.class);

	private final StudentServiceImpl studentService;
	private final GroupServiceImpl groupService;
	private final CourseServiceImpl courseService;

	public GeneratorService(StudentServiceImpl studentService, GroupServiceImpl groupService,
			CourseServiceImpl courseService) {
		this.studentService = studentService;
		this.groupService = groupService;
		this.courseService = courseService;
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
		groupService.generateGroups();
	}

	public void generateCourses() {
		courseService.generateCourses();
	}

	public void generateStudents() {
		studentService.generateStudents();
	}

	public void assignStudentsToGroups() {
		studentService.assignStudentsToGroups();
	}

	public void assignStudentsToCourses() {
		studentService.assignStudentsToCourses();
	}

}
