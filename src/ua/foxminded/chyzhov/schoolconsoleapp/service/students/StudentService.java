package ua.foxminded.chyzhov.schoolconsoleapp.service.students;

import java.util.List;

import ua.foxminded.chyzhov.schoolconsoleapp.dao.exception.DaoException;

public interface StudentService {

	public void addStudent(int groupId, String firstName, String lastName);

	public List<String> getStudents();

	public List<String> getStudentsWithCourses();

	public List<String> getStudentsByCourse(String courseName);

	public List<String> getStudentsByGroup(String groupName);

	public void deleteStudent(int studentId) throws DaoException;

	public void addStudentToCourse(int studentId, int courseId) throws DaoException;

	public void removeStudentFromCourse(int studentId, int courseId) throws DaoException;

	public boolean isStudentsTableEmpty();

}
