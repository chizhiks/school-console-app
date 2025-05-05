package ua.foxminded.chyzhov.schoolconsoleapp.database.dbobjects.students;

import java.util.List;

import org.springframework.stereotype.Service;

import ua.foxminded.chyzhov.schoolconsoleapp.DAO.StudentDao;

@Service
public class StudentService {

	private final StudentDao studentDao;

	public StudentService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public void generateStudents() {
		studentDao.generateStudents();
	}

	public void addStudent(int groupId, String firstName, String lastName) {
		studentDao.addStudent(groupId, firstName, lastName);
	}

	public void assignStudentsToGroups() {
		studentDao.assignStudentsToGroups();

	}

	public void assignStudentsToCourses() {
		studentDao.assignStudentsToCourses();
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

	public void deleteStudent(int studentId) {
		studentDao.deleteStudent(studentId);
	}

	public void addStudentToCourse(int studentId, int courseId) {
		studentDao.addStudentToCourse(studentId, courseId);
	}

	public void removeStudentFromCourse(int studentId, int courseId) {
		studentDao.removeStudentFromCourse(studentId, courseId);
	}

}
