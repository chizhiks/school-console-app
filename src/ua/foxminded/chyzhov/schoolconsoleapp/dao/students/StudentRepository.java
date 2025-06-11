package ua.foxminded.chyzhov.schoolconsoleapp.dao.students;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.foxminded.chyzhov.schoolconsoleapp.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	@Query("SELECT s FROM Student s WHERE s.groupId IS NULL OR s.groupId = 0")
	List<Student> findUnassignedStudents();

	@Query("""
			SELECT DISTINCT s FROM Student s
			JOIN FETCH s.courseList c
			ORDER BY s.studentId, c.courseId
			""")
	List<Student> findWithCourses();

	@Query("""
			SELECT s FROM Student s
			JOIN s.courseList c
			WHERE c.courseName = :courseName
			""")
	List<Student> findByCourseName(@Param("courseName") String courseName);

	@Query("""
			SELECT s FROM Student s
			JOIN s.group g
			WHERE g.groupName = :groupName
			""")
	List<Student> findByGroupName(@Param("groupName") String groupName);

}
