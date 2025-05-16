package ua.foxminded.chyzhov.schoolconsoleapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import ua.foxminded.chyzhov.schoolconsoleapp.database.service.GeneratorService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.courses.CourseService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.groups.GroupService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.students.StudentService;

@Component
public class DataCheckRunner implements ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger(DataCheckRunner.class);

	private final GeneratorService generatorService;
	private final StudentService studentService;
	private final GroupService groupService;
	private final CourseService courseService;
	private final UserInputService userInputService;

	public DataCheckRunner(GeneratorService generatorService, StudentService studentService, GroupService groupService,
			CourseService courseService, UserInputService userInputService) {
		this.generatorService = generatorService;
		this.studentService = studentService;
		this.groupService = groupService;
		this.courseService = courseService;
		this.userInputService = userInputService;
	}

	private boolean isTablesEmpty() {
		boolean isTablesEmpty = studentService.isStudentsTableEmpty() && groupService.isGroupsTableEmpty()
				&& courseService.isCoursesTableEmpty();
		logger.info("Checked if all tables are empty: {}", isTablesEmpty);
		return isTablesEmpty;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		logger.info("Application started, beginning database check...");

		if (isTablesEmpty()) {
			logger.info("The database is empty. Starting the data generator...");
			generatorService.generateAllData();
			logger.info("Data generation completed successfully");
		} else {
			logger.info("Database already contains data. Skipping data generation.");

		}

		logger.info("Starting user input service.");
		userInputService.start();
		logger.info("User input service started.");
	}

}
