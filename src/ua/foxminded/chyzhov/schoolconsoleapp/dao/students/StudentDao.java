package ua.foxminded.chyzhov.schoolconsoleapp.dao.students;

import java.util.List;

import ua.foxminded.chyzhov.schoolconsoleapp.dao.exception.DaoException;

public interface StudentDao {

	void generateStudents();

	void addStudent(int groupId, String firstName, String lastName);

	void assignStudentsToGroups();

	void assignStudentsToCourses();

	List<String> getStudents();

	List<String> getStudentsWithCourses();

	List<String> getStudentsByCourse(String courseName);

	List<String> getStudentsByGroup(String groupName);

	void deleteStudent(int studentId) throws DaoException;

	void addStudentToCourse(int studentId, int courseId) throws DaoException;

	void removeStudentFromCourse(int studentId, int courseId) throws DaoException;

	boolean isStudentsTableEmpty();
}
