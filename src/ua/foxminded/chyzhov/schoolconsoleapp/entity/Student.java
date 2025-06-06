package ua.foxminded.chyzhov.schoolconsoleapp.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Formula;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "students", schema = "school")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_id")
	private long studentId;

	@Formula("group_id")
	private long groupId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@ManyToOne
	@JoinColumn(name = "group_id")
	private Group group;

	@ManyToMany
	@JoinTable(name = "students_courses", schema = "school", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
	private List<Course> courseList = new ArrayList<>();

	public Student() {

	}

	public List<Course> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<Course> courseList) {
		this.courseList = courseList;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Student(long groupId, String firstName, String lastName) {
		this.groupId = groupId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "Student{studentId=" + studentId + ", groupId=" + groupId + ", firstName='" + firstName + "', lastName='"
				+ lastName + "'}";
	}

	public void addCourse(Course course) {
		courseList.add(course);
		course.getStudentList().add(this);
	}

	public void removeCourse(Course course) {
		courseList.remove(course);
		course.getStudentList().remove(this);
	}

}
