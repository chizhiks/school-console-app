package ua.foxminded.chyzhov.schoolconsoleapp.dao.students;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.exception.DaoException;
import ua.foxminded.chyzhov.schoolconsoleapp.entity.Student;

@Repository
public class StudentDaoImpl implements StudentDao {

	@PersistenceContext
	private EntityManager em;

	private static final int DEFAULT_STUDENT_COUNT = 200;

	private static final Logger logger = LoggerFactory.getLogger(StudentDaoImpl.class);

	@Override
	public void generateStudents() {

		String[] firstNames = { "John", "Emma", "Michael", "Olivia", "Daniel", "Sophia", "David", "Ava", "James",
				"Isabella", "William", "Mia", "Alexander", "Charlotte", "Ethan", "Amelia", "Benjamin", "Harper",
				"Henry", "Evelyn" };

		String[] lastNames = { "Smith", "Johnson", "Brown", "Taylor", "Anderson", "Thomas", "Jackson", "White",
				"Harris", "Martin", "Thompson", "Garcia", "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Lee",
				"Walker", "Hall" };

		Random random = new Random();

		for (int i = 0; i < DEFAULT_STUDENT_COUNT; i++) {

			String firstName = firstNames[random.nextInt(firstNames.length)];
			String lastName = lastNames[random.nextInt(lastNames.length)];

			Student student = new Student(0, firstName, lastName);
			em.persist(student);
		}

		logger.info("{} students were successfully generated", DEFAULT_STUDENT_COUNT);
	}

	@Override
	public void addStudent(int groupId, String firstName, String lastName) {

		Student student = new Student(groupId, firstName, lastName);

		em.persist(student);

		logger.info("Student added, GroupId = {}, FirstName = {}, LastName = {}", groupId, firstName, lastName);
	}

	@Override
	public void assignStudentsToGroups() {
		Random random = new Random();

		List<Integer> groupIDs = em.createQuery("SELECT g.groupId FROM Group g", Integer.class).getResultList();

		List<Student> unassignedStudents = em
				.createQuery("SELECT s FROM Student s WHERE s.groupId IS NULL OR s.groupId = 0", Student.class)
				.getResultList();

		for (int groupID : groupIDs) {
			int studentsInGroup = random.nextInt(21) + 10;

			for (int i = 0; i < studentsInGroup && !unassignedStudents.isEmpty(); i++) {
				int index = random.nextInt(unassignedStudents.size());
				Student student = unassignedStudents.remove(index);

				student.setGroupId(groupID);

				em.merge(student);
			}
		}

		logger.info("Students were successfully assigned to groups");

	}

	@Override
	public void assignStudentsToCourses() {
		Random random = new Random();

		List<Student> studentsQuery = em.createQuery("SELECT s FROM Student s", Student.class).getResultList();

		List<Integer> courseIDs = em.createQuery("SELECT c.courseId FROM Course c", Integer.class).getResultList();

		for (Student student : studentsQuery) {
			int numCourses = random.nextInt(3) + 1;
			List<Integer> selectedCourses = new ArrayList<>(courseIDs);

			for (int i = 0; i < numCourses && !selectedCourses.isEmpty(); i++) {
				int courseIndex = random.nextInt(selectedCourses.size());
				int courseID = selectedCourses.remove(courseIndex);

				Query insertQuery = em
						.createNativeQuery("INSERT INTO school.students_courses(student_id, course_id) VALUES (?, ?)");
				insertQuery.setParameter(1, student.getStudentId());
				insertQuery.setParameter(2, courseID);
				insertQuery.executeUpdate();
			}
		}

		logger.info("Students were successfully assigned to courses");

	}

	@Override
	public List<String> getStudents() {

		List<Student> students = em.createQuery("SELECT s FROM Student s", Student.class).getResultList();

		int maxFirstNameLength = 0;
		int maxLastNameLength = 0;

		for (Student student : students) {

			String firstName = student.getFirstName();
			String lastName = student.getLastName();

			if (firstName.length() > maxFirstNameLength) {
				maxFirstNameLength = firstName.length();
			}

			if (lastName.length() > maxLastNameLength) {
				maxLastNameLength = lastName.length();
			}
		}

		maxFirstNameLength += 2;
		maxLastNameLength += 2;

		List<String> result = new ArrayList<>();

		result.add("\nList of students in the database:\n");

		for (Student student : students) {

			String studentInfo = String.format(
					"ID: %-5d | First Name: %-" + maxFirstNameLength + "s | Last Name: %-" + maxLastNameLength + "s",
					student.getStudentId(), student.getFirstName(), student.getLastName());

			result.add(studentInfo);
		}

		logger.info("Received {} students from the database", students.size());

		return result;
	}

	@Override
	public List<String> getStudentsWithCourses() {

		Query query = em.createNativeQuery("SELECT student_id, course_id FROM school.students_courses");

		@SuppressWarnings("unchecked")
		List<Object[]> rows = query.getResultList();

		List<String> result = new ArrayList<>();

		for (Object[] row : rows) {
			String info = String.format("StudentID: %-5d | CourseID: %-5d", ((Number) row[0]).intValue(),
					((Number) row[1]).intValue());
			result.add(info);
		}

		logger.info("Received {} student-course assignments from the database", result.size());

		return result;

	}

	@Override
	public List<String> getStudentsByCourse(String courseName) {

		Query query = em.createNativeQuery("""
				SELECT s.student_id, s.first_name, s.last_name
				FROM school.students s
				JOIN school.students_courses sc ON s.student_id = sc.student_id
				JOIN school.courses c ON sc.course_id = c.course_id
				WHERE c.course_name = ?
				""");
		query.setParameter(1, courseName);

		@SuppressWarnings("unchecked")
		List<Object[]> rows = query.getResultList();

		int maxFirstNameLength = 0;
		int maxLastNameLength = 0;

		for (Object[] row : rows) {
			String firstName = (String) row[1];
			String lastName = (String) row[2];

			if (firstName.length() > maxFirstNameLength) {
				maxFirstNameLength = firstName.length();
			}

			if (lastName.length() > maxLastNameLength) {
				maxLastNameLength = lastName.length();
			}
		}

		maxFirstNameLength += 2;
		maxLastNameLength += 2;

		List<String> result = new ArrayList<>();

		result.add("\nList of students in the course '" + courseName + "':\n");

		for (Object[] row : rows) {

			String studentInfo = String.format(
					"ID: %-5d First Name: %-" + maxFirstNameLength + "s Last Name: %-" + maxLastNameLength + "s",
					((Number) row[0]).intValue(), row[1], row[2]);

			result.add(studentInfo);
		}

		logger.info("Received {} students in the course '{}' from the database", rows.size(), courseName);

		return result;
	}

	@Override
	public List<String> getStudentsByGroup(String groupName) {

		Query query = em.createNativeQuery("""
				SELECT * FROM school.students s
				JOIN school.groups g ON s.group_id = g.group_id
				WHERE g.group_name = ?
				""");
		query.setParameter(1, groupName);

		@SuppressWarnings("unchecked")
		List<Object[]> rows = query.getResultList();

		int maxFirstNameLength = 0;
		int maxLastNameLength = 0;

		for (Object[] row : rows) {

			String firstName = (String) row[2];
			String lastName = (String) row[3];

			if (firstName.length() > maxFirstNameLength) {
				maxFirstNameLength = firstName.length();
			}

			if (lastName.length() > maxLastNameLength) {
				maxLastNameLength = lastName.length();
			}
		}

		maxFirstNameLength += 2;
		maxLastNameLength += 2;

		List<String> result = new ArrayList<>();

		result.add("\nList of students in the group '" + groupName + "':\n");

		for (Object[] row : rows) {

			String studentInfo = String.format(
					"ID: %-5d First Name: %-" + maxFirstNameLength + "s Last Name: %-" + maxLastNameLength + "s",
					((Number) row[0]).intValue(), row[1], row[2]);

			result.add(studentInfo);
		}

		logger.info("Received {} students in the group '{}' from the database", rows.size(), groupName);

		return result;
	}

	@Override
	public void deleteStudent(int studentId) throws DaoException {

		try {
			Query deleteCoursesQuery = em.createNativeQuery("DELETE FROM school.students_courses WHERE student_id = ?");
			deleteCoursesQuery.setParameter(1, studentId);
			deleteCoursesQuery.executeUpdate();

			Student student = em.find(Student.class, studentId);

			if (student != null) {
				em.remove(student);
				logger.info("Student with ID: {} was successfully deleted", studentId);
			} else {
				logger.warn("No student found with ID: {}", studentId);
			}

		} catch (Exception e) {
			logger.error("Failed to delete student with ID: {}", studentId, e);
			throw new DaoException("Failed to delete student with ID: " + studentId, e);
		}

	}

	@Override
	public void addStudentToCourse(int studentId, int courseId) throws DaoException {

		try {
			Query insertQuery = em.createNativeQuery("INSERT INTO school.students_courses VALUES (?, ?)");
			insertQuery.setParameter(1, studentId);
			insertQuery.setParameter(2, courseId);
			insertQuery.executeUpdate();

			logger.info("Student with ID: {} was successfully added to course with ID: {}", studentId, courseId);

		} catch (Exception e) {
			logger.error("Failed to add student with ID: {} to course with ID: {}", studentId, courseId, e);
			throw new DaoException("Failed to add student with ID: " + studentId + " to course with ID: " + courseId,
					e);
		}

	}

	@Override
	public void removeStudentFromCourse(int studentId, int courseId) throws DaoException {

		try {
			Query removeQuery = em
					.createNativeQuery("DELETE FROM school.students_courses WHERE student_id = ? AND course_id = ?");
			removeQuery.setParameter(1, studentId);
			removeQuery.setParameter(2, courseId);
			int rows = removeQuery.executeUpdate();

			if (rows == 0) {
				logger.warn("No student found with ID " + studentId + " in course with ID" + courseId);
			}

			logger.info("Student with ID: {} was successfully removed from course with ID: {}", studentId, courseId);

		} catch (Exception e) {
			logger.error("Failed to remove student with ID: {} from course with ID: {}", studentId, courseId, e);
			throw new DaoException(
					"Failed to remove student with ID: " + studentId + " from course with ID: " + courseId, e);
		}

	}

	@Override
	public boolean isStudentsTableEmpty() {
		Integer count = em.createQuery("SELECT COUNT(s) FROM Student s", Integer.class).getSingleResult();

		boolean isEmpty = count == null || count == 0;
		logger.info("Checked if students table is empty: {}", isEmpty);
		return isEmpty;
	}
}
