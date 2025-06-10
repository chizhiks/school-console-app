package ua.foxminded.chyzhov.schoolconsoleapp.database.service.students;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.exception.DaoException;

@Service
public class StudentService {

	private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

	private final StudentServiceImpl studentServiceImpl;

	public StudentService(StudentServiceImpl studentServiceImpl) {
		this.studentServiceImpl = studentServiceImpl;
	}

	@Transactional
	public void addStudent(int groupId, String firstName, String lastName) {
		studentServiceImpl.addStudent(groupId, firstName, lastName);
	}

	public List<String> getStudents() {
		return studentServiceImpl.getStudents();
	}

	public List<String> getStudentsWithCourses() {
		return studentServiceImpl.getStudentsWithCourses();
	}

	public List<String> getStudentsByCourse(String courseName) {
		return studentServiceImpl.getStudentsByCourse(courseName);
	}
	
	public List<String> getStudentsByGroup(String groupName) {
		return studentServiceImpl.getStudentsByGroup(groupName);
	}

	@Transactional
	public void deleteStudent(int studentId) throws DaoException {
		try {
			studentServiceImpl.deleteStudent(studentId);
		} catch (DaoException e) {
			logger.error("Failed to delete student with ID: {}", studentId, e);
			throw new DaoException("Failed to delete student with ID: " + studentId, e);
		}
	}

	@Transactional
	public void addStudentToCourse(int studentId, int courseId) throws DaoException {
		try {
			studentServiceImpl.addStudentToCourse(studentId, courseId);
		} catch (DaoException e) {
			logger.error("Failed to add student with ID: {} to course with ID: {}", studentId, courseId, e);
			throw new DaoException("Failed to add student with ID: " + studentId + " to course with ID: " + courseId,
					e);
		}
	}

	@Transactional
	public void removeStudentFromCourse(int studentId, int courseId) throws DaoException {
		try {
			studentServiceImpl.removeStudentFromCourse(studentId, courseId);
		} catch (DaoException e) {
			logger.error("Failed to remove student with ID: {} from course with ID: {}", studentId, courseId, e);
			throw new DaoException(
					"Failed to remove student with ID: " + studentId + " from course with ID: " + courseId, e);
		}
	}

	public boolean isStudentsTableEmpty() {
		return studentServiceImpl.isStudentsTableEmpty();
	}

}
