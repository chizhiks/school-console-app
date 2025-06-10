DELETE FROM school.students_courses;
DELETE FROM school.students;
DELETE FROM school.courses;
DELETE FROM school.groups;

ALTER SEQUENCE school.groups_group_id_seq RESTART WITH 1;
ALTER SEQUENCE school.courses_course_id_seq RESTART WITH 1;
ALTER SEQUENCE school.students_student_id_seq RESTART WITH 1;