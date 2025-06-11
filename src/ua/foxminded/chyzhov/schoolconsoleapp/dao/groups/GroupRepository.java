package ua.foxminded.chyzhov.schoolconsoleapp.dao.groups;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.foxminded.chyzhov.schoolconsoleapp.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

	@Query(value = """
			SELECT g.group_id, g.group_name, count(s.student_id) AS student_count
			FROM school.groups g
			LEFT JOIN school.students s ON g.group_id = s.group_id
			GROUP BY g.group_id, g.group_name
			HAVING count(s.student_id) <= :limit
			""", nativeQuery = true)
	List<Object[]> findByLimitStudents(@Param("limit") int limit);

}
