package ua.foxminded.chyzhov.schoolconsoleapp.database.service.students;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.chyzhov.schoolconsoleapp.dao.courses.CourseRepository;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.exception.DaoException;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.groups.GroupRepository;
import ua.foxminded.chyzhov.schoolconsoleapp.dao.students.StudentRepository;
import ua.foxminded.chyzhov.schoolconsoleapp.entity.Course;
import ua.foxminded.chyzhov.schoolconsoleapp.entity.Group;
import ua.foxminded.chyzhov.schoolconsoleapp.entity.Student;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	StudentRepository studentRepository;
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	CourseRepository courseRepository;

	private static final int DEFAULT_STUDENT_COUNT = 200;

	private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

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
			studentRepository.save(student);
		}

		logger.info("{} students were successfully generated", DEFAULT_STUDENT_COUNT);
	}

	@Override
	public void addStudent(int groupId, String firstName, String lastName) {

		Student student = new Student(groupId, firstName, lastName);

		studentRepository.save(student);

		logger.info("Student added, GroupId = {}, FirstName = {}, LastName = {}", groupId, firstName, lastName);
	}

	public void assignStudentsToGroups() {
		Random random = new Random();

		List<Group> groups = groupRepository.findAll();

		List<Student> unassignedStudents = studentRepository.findUnassignedStudents();

		for (Group group : groups) {
			int studentsInGroup = random.nextInt(21) + 10;

			for (int i = 0; i < studentsInGroup && !unassignedStudents.isEmpty(); i++) {
				int index = random.nextInt(unassignedStudents.size());
				Student student = unassignedStudents.remove(index);

				student.setGroup(group);

				studentRepository.save(student);
			}
		}

		logger.info("Students were successfully assigned to groups");

	}

	public void assignStudentsToCourses() {
		Random random = new Random();

		List<Student> students = studentRepository.findAll();

		List<Course> courses = courseRepository.findAll();

		for (Student student : students) {
			int numCourses = random.nextInt(3) + 1;
			List<Course> availableCourses = new ArrayList<>(courses);

			for (int i = 0; i < numCourses && !availableCourses.isEmpty(); i++) {
				int courseIndex = random.nextInt(availableCourses.size());
				Course course = availableCourses.remove(courseIndex);

				student.addCourse(course);
			}

			studentRepository.save(student);
		}

		logger.info("Students were successfully assigned to courses");

	}

	@Override
	public List<String> getStudents() {

		List<Student> students = studentRepository.findAll();

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

		List<Student> students = studentRepository.findWithCourses();

		List<String> result = new ArrayList<>();

		for (Student student : students) {
			for (Course course : student.getCourseList()) {
				String info = String.format("StudentID: %-5d | CourseID: %-5d", student.getStudentId(),
						course.getCourseId());
				result.add(info);
			}
		}

		logger.info("Received {} student-course assignments from the database", result.size());

		return result;

	}

	@Override
	public List<String> getStudentsByCourse(String courseName) {

		List<Student> students = studentRepository.findByCourseName(courseName);

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

		result.add("\nList of students in the course '" + courseName + "':\n");

		for (Student student : students) {

			String studentInfo = String.format(
					"ID: %-5d First Name: %-" + maxFirstNameLength + "s Last Name: %-" + maxLastNameLength + "s",
					student.getStudentId(), student.getFirstName(), student.getLastName());

			result.add(studentInfo);
		}

		logger.info("Received {} students in the course '{}' from the database", students.size(), courseName);

		return result;
	}

	@Override
	public List<String> getStudentsByGroup(String groupName) {

		List<Student> students = studentRepository.findByGroupName(groupName);

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

		result.add("\nList of students in the group '" + groupName + "':\n");

		for (Student student : students) {

			String studentInfo = String.format(
					"ID: %-5d First Name: %-" + maxFirstNameLength + "s Last Name: %-" + maxLastNameLength + "s",
					student.getStudentId(), student.getFirstName(), student.getLastName());

			result.add(studentInfo);
		}

		logger.info("Received {} students in the group '{}' from the database", students.size(), groupName);

		return result;
	}

	@Override
	public void deleteStudent(int studentId) throws DaoException {

		try {
			Student student = studentRepository.findById((long) studentId).get();

			if (student != null) {
				student.getCourseList().clear();
				studentRepository.delete(student);

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
			Student student = studentRepository.findById((long) studentId).get();
			Course course = courseRepository.findById((long) courseId).get();

			if (student == null) {
				throw new DaoException("Student with ID " + studentId + " not found");
			}

			if (course == null) {
				throw new DaoException("Course with ID " + courseId + " not found");
			}

			student.getCourseList().add(course);
			course.getStudentList().add(student);

			studentRepository.save(student);

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
			Student student = studentRepository.findById((long) studentId).get();
			Course course = courseRepository.findById((long) courseId).get();

			if (student == null) {
				throw new DaoException("Student with ID " + studentId + " not found");
			}

			if (course == null) {
				throw new DaoException("Course with ID " + courseId + " not found");
			}

			student.getCourseList().remove(course);
			course.getStudentList().remove(student);

			studentRepository.save(student);

			logger.info("Student with ID: {} was successfully removed from course with ID: {}", studentId, courseId);

		} catch (Exception e) {
			logger.error("Failed to remove student with ID: {} from course with ID: {}", studentId, courseId, e);
			throw new DaoException(
					"Failed to remove student with ID: " + studentId + " from course with ID: " + courseId, e);
		}

	}

	@Override
	public boolean isStudentsTableEmpty() {
		Long count = studentRepository.count();

		boolean isEmpty = count == null || count == 0;
		logger.info("Checked if students table is empty: {}", isEmpty);
		return isEmpty;
	}
}
