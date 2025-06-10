package ua.foxminded.chyzhov.schoolconsoleapp.database;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import ua.foxminded.chyzhov.schoolconsoleapp.dao.exception.DaoException;
import ua.foxminded.chyzhov.schoolconsoleapp.service.courses.CourseService;
import ua.foxminded.chyzhov.schoolconsoleapp.service.groups.GroupService;
import ua.foxminded.chyzhov.schoolconsoleapp.service.students.StudentService;

@Service
public class DatabaseFacade {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseFacade.class);

	private final JdbcTemplate jdbc;
	private final GroupService groupService;
	private final CourseService courseService;
	private final StudentService studentService;

	public DatabaseFacade(JdbcTemplate jdbc, GroupService groupService, CourseService courseService,
			StudentService studentService) {
		this.jdbc = jdbc;
		this.groupService = groupService;
		this.courseService = courseService;
		this.studentService = studentService;
	}

	public void clearAllTables() throws DaoException {

		try {

			jdbc.update("TRUNCATE TABLE school.students_courses RESTART IDENTITY CASCADE");
			jdbc.update("TRUNCATE TABLE school.students RESTART IDENTITY CASCADE");
			jdbc.update("TRUNCATE TABLE school.courses RESTART IDENTITY CASCADE");
			jdbc.update("TRUNCATE TABLE school.groups RESTART IDENTITY CASCADE");
			logger.info("All tables have been deleted successfully.");

		} catch (Exception e) {
			logger.error("Failed to clear all tables data.", e);
			throw new DaoException("Failed to clear all tables data.", e);
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

		logger.info("All data received successfully from the database");
	}

	public int getMaxRowsInTableAmount(String tableName) {

		String sql = "SELECT COUNT(*) FROM school." + tableName;

		int maxRows = jdbc.queryForObject(sql, Integer.class);

		logger.info("Table '{}' contains {} rows.", tableName, maxRows);

		return maxRows;

	}

	public int getNextRowInTable(String tableName) {

		int resultId = getMaxRowsInTableAmount(tableName) + 1;

		logger.info("Next row ID for table '{}' is {}", tableName, resultId);

		return resultId;
	}

}
