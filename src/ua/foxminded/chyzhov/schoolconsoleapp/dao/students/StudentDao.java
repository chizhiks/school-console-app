package ua.foxminded.chyzhov.schoolconsoleapp.dao.students;

import java.util.List;

public interface StudentDao {

	void generateStudents();

	void addStudent(int groupId, String firstName, String lastName);

	void assignStudentsToGroups();

	void assignStudentsToCourses();

	List<String> getStudents();

	List<String> getStudentsWithCourses();

	List<String> getStudentsByCourse(String courseName);

	List<String> getStudentsByGroup(String groupName);

	void deleteStudent(int studentId);

	void addStudentToCourse(int studentId, int courseId);

	void removeStudentFromCourse(int studentId, int courseId);

	boolean isStudentsTableEmpty();
}
