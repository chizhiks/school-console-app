package ua.foxminded.chyzhov.schoolconsoleapp.DAO;

import java.util.List;

public interface StudentDao {

	void generateStudents();

	void addStudent(int group_id, String first_name, String last_name);

	void assignStudentsToGroups();

	void assignStudentsToCourses();

	List<String> getStudents();

	List<String> getStudentsWithCourses();

	List<String> getStudentsByCourse(String courseName);

	void deleteStudent(int student_id);

	void addStudentToCourse(int student_id, int course_id);

	void removeStudentFromCourse(int student_id, int course_id);
}
