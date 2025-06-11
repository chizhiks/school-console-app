package ua.foxminded.chyzhov.schoolconsoleapp.dao.courses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.chyzhov.schoolconsoleapp.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}
