package ua.foxminded.chyzhov.schoolconsoleapp.database.service.students;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ua.foxminded.chyzhov.schoolconsoleapp.dao.exception.DaoException;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.students.StudentDao;

@Service
public class StudentService {

	private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

	private final StudentDao studentDao;

	public StudentService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public void addStudent(int groupId, String firstName, String lastName) {
		studentDao.addStudent(groupId, firstName, lastName);
		logger.info("Student added: GroupId = {}, FirstName = {}, LastName = {}", groupId, firstName, lastName);
	}

	public List<String> getStudents() {
		List<String> students = studentDao.getStudents();
		logger.info("Received {} students from the database", students.size());
		return students;

	}

	public List<String> getStudentsWithCourses() {
		List<String> students = studentDao.getStudentsWithCourses();
		logger.info("Received {} student-course assignments from the database", students.size());
		return students;
	}

	public List<String> getStudentsByCourse(String courseName) {
		List<String> students = studentDao.getStudentsByCourse(courseName);
		logger.info("Received {} students in the course '{}' from the database", students.size(), courseName);
		return students;
	}

	public void deleteStudent(int studentId) throws DaoException {
		try {
			studentDao.deleteStudent(studentId);
			logger.info("Student with ID: {} was successfully deleted", studentId);
		} catch (DaoException e) {
			logger.error("Failed to delete student with ID: {}", studentId, e);
			throw new DaoException("Failed to delete student with ID: " + studentId, e);
		}
	}

	public void addStudentToCourse(int studentId, int courseId) {
		studentDao.addStudentToCourse(studentId, courseId);
		logger.info("Student with ID: {} was successfully added to course with ID: {}", studentId, courseId);
	}

	public void removeStudentFromCourse(int studentId, int courseId) throws DaoException {
		try {
			studentDao.removeStudentFromCourse(studentId, courseId);
			logger.info("Student with ID: {} was successfully removed from course with ID: {}", studentId, courseId);
		} catch (DaoException e) {
			logger.error("Failed to remove student with ID: {} from course with ID: {}", studentId, courseId, e);
			throw new DaoException(
					"Failed to remove student with ID: " + studentId + " from course with ID: " + courseId, e);
		}
	}

	public boolean isStudentsTableEmpty() {
		boolean isEmpty = studentDao.isStudentsTableEmpty();
		logger.info("Checked if students table is empty: {}", isEmpty);
		return isEmpty;
	}

}
