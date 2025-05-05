package ua.foxminded.chyzhov.schoolconsoleapp.database;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.courses.CourseDaoImpl;
import ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.groups.GroupDaoImpl;
import ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.students.StudentDaoImpl;

@Service
public class DatabaseFacade {

	private final JdbcTemplate jdbc;
	private final GroupDaoImpl groupService;
	private final CourseDaoImpl courseService;
	private final StudentDaoImpl studentService;

	public DatabaseFacade(JdbcTemplate jdbc, GroupDaoImpl groupService, CourseDaoImpl courseService,
			StudentDaoImpl studentService) {
		this.jdbc = jdbc;
		this.groupService = groupService;
		this.courseService = courseService;
		this.studentService = studentService;
	}

	public void clearAllTables() {

		try {

			jdbc.update("TRUNCATE TABLE school.students_courses RESTART IDENTITY CASCADE");
			jdbc.update("TRUNCATE TABLE school.students RESTART IDENTITY CASCADE");
			jdbc.update("TRUNCATE TABLE school.courses RESTART IDENTITY CASCADE");
			jdbc.update("TRUNCATE TABLE school.groups RESTART IDENTITY CASCADE");
			System.out.println("All tables have been deleted successfully.");

		} catch (Exception e) {
			System.out.println("Failed to clear all tables data.");
			e.printStackTrace();
		}
	}

	public void getAllTables() {

		List<String> results = studentService.getStudents();

		for (String result : results) {
			System.out.println(result);
		}

		results = groupService.getGroups();

		for (String result : results) {
			System.out.println(result);
		}

		results = courseService.getCourses();

		for (String result : results) {
			System.out.println(result);
		}
	}

	public void generateAllData() {
		groupService.generateGroups();
		courseService.generateCourses();
		studentService.generateStudents();
		studentService.assignStudentsToGroups();
		studentService.assignStudentsToCourses();
		System.out.println("All data in tables have been generated successfully.");
	}

	public int getMaxRowsInTableAmount(String tableName) {

		String sql = "SELECT COUNT(*) FROM school." + tableName;

		return jdbc.queryForObject(sql, Integer.class);

	}

	public int getNextRowInTable(String tableName) {

		int resultId = getMaxRowsInTableAmount(tableName) + 1;

		return resultId;
	}

}
