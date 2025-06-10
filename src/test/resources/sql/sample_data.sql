INSERT INTO school.groups (group_name) VALUES ('AA-11');
INSERT INTO school.groups (group_name) VALUES ('AB-12');

INSERT INTO school.courses (course_name, course_description) VALUES ('Mathematics', 'Basic Mathematics Course');
INSERT INTO school.courses (course_name, course_description) VALUES ('Test Course', 'Test Course Description');

INSERT INTO school.students (group_id, first_name, last_name) VALUES (1, 'Test', 'Student A');
INSERT INTO school.students (group_id, first_name, last_name) VALUES (2, 'Test', 'Student B');
INSERT INTO school.students (group_id, first_name, last_name) VALUES (1, 'Student', '201');

INSERT INTO school.students_courses (student_id, course_id) VALUES (1, 1); -- Test Student A -> Mathematics
INSERT INTO school.students_courses (student_id, course_id) VALUES (2, 1); -- Test Student B -> Mathematics  
INSERT INTO school.students_courses (student_id, course_id) VALUES (3, 2); -- Student 201 -> Test Course