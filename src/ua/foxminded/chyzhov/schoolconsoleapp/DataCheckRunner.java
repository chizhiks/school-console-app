package ua.foxminded.chyzhov.schoolconsoleapp;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import ua.foxminded.chyzhov.schoolconsoleapp.database.service.GeneratorService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.courses.CourseService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.groups.GroupService;
import ua.foxminded.chyzhov.schoolconsoleapp.database.service.students.StudentService;

@Component
public class DataCheckRunner implements ApplicationRunner {

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
		return studentService.isStudentsTableEmpty() && groupService.isGroupsTableEmpty()
				&& courseService.isCoursesTableEmpty();
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (isTablesEmpty()) {
			System.out.println("The database is empty. Starting the generator...");
			generatorService.generateAllData();
		} else {
			System.out.println("The database already contains data.");
		}

		userInputService.start();
	}

}
