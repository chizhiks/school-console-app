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
	}

	public List<String> getStudents() {
		return studentDao.getStudents();
	}

	public List<String> getStudentsWithCourses() {
		return studentDao.getStudentsWithCourses();
	}

	public List<String> getStudentsByCourse(String courseName) {
		return studentDao.getStudentsByCourse(courseName);
	}

	public void deleteStudent(int studentId) throws DaoException {
		try {
			studentDao.deleteStudent(studentId);
		} catch (DaoException e) {
			logger.error("Failed to delete student with ID: {}", studentId, e);
			throw new DaoException("Failed to delete student with ID: " + studentId, e);
		}
	}

	public void addStudentToCourse(int studentId, int courseId) throws DaoException {
		try {
			studentDao.addStudentToCourse(studentId, courseId);
		} catch (DaoException e) {
			logger.error("Failed to add student with ID: {} to course with ID: {}", studentId, courseId, e);
			throw new DaoException("Failed to add student with ID: " + studentId + " to course with ID: " + courseId,
					e);
		}
	}

	public void removeStudentFromCourse(int studentId, int courseId) throws DaoException {
		try {
			studentDao.removeStudentFromCourse(studentId, courseId);
		} catch (DaoException e) {
			logger.error("Failed to remove student with ID: {} from course with ID: {}", studentId, courseId, e);
			throw new DaoException(
					"Failed to remove student with ID: " + studentId + " from course with ID: " + courseId, e);
		}
	}

	public boolean isStudentsTableEmpty() {
		return studentDao.isStudentsTableEmpty();
	}

}
