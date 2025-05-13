CREATE TABLE IF NOT EXISTS school.groups (
    group_id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    group_name text NOT NULL
);

CREATE TABLE IF NOT EXISTS school.courses (
    course_id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    course_name text NOT NULL,
    course_description text NOT NULL
);

CREATE TABLE IF NOT EXISTS school.students (
    student_id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    group_id integer,
    first_name text NOT NULL,
    last_name text NOT NULL,
    CONSTRAINT fk_group FOREIGN KEY (group_id) REFERENCES school.groups(group_id)
);

CREATE TABLE IF NOT EXISTS school.students_courses (
    student_id integer NOT NULL,
    course_id integer NOT NULL,
    PRIMARY KEY (student_id, course_id),
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES school.students(student_id) ON DELETE CASCADE,
    CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES school.courses(course_id) ON DELETE CASCADE
);
