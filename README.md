# School Console Application

A console-based educational management system for tracking students, groups, and courses — a practical exercise in building a data-driven application on top of Spring Boot and PostgreSQL without a web layer.

## Why this project

Before building web APIs, this project focuses purely on the data layer: modeling a real many-to-many domain (students enrolled in multiple courses, grouped into cohorts), writing meaningful queries against it, and validating the schema with Flyway migrations and integration tests — all driven through a simple console interface instead of HTTP.

## Features

The application exposes its functionality through an interactive console menu:

- Clear all records from all tables
- Generate randomized sample data for students, groups, and courses (`Randomizer`, `GeneratorService`) — useful for testing queries at scale
- View all tables / all students / all groups / all courses
- Find all groups with a student count less than or equal to a given number
- Find all students associated with a course by course title
- Add a new student
- Delete a student by ID
- Enroll a student in a course
- Remove a student from a course

## Tech Stack

| Category    | Technology                          |
|--------------|---------------------------------------|
| Language    | Java 17                             |
| Framework   | Spring Boot 3.4.5                   |
| Build Tool  | Maven                               |
| Database    | PostgreSQL                          |
| Migrations  | Flyway                              |
| Data Access | Spring Data JPA (`JpaRepository`)   |
| Testing     | JUnit 5, Spring Boot Testcontainers |

## Architecture

```
MainApplication (entry point)
     │
     ▼
UserInputService        ← console menu loop, reads and routes user commands
     │
     ▼
Service layer            ← StudentService, GroupService, CourseService
     │
     ▼
Repository layer          ← Spring Data JPA repositories
     │
     ▼
PostgreSQL
```

Schema changes are versioned with Flyway migrations, applied automatically on startup. `DataCheckRunner` validates the database state at boot.

## Project Structure

```
src/ua/foxminded/chyzhov/schoolconsoleapp/
├── MainApplication.java        # Entry point
├── UserInputService.java       # Console menu / user interaction
├── Randomizer.java             # Random test-data generator
├── DataCheckRunner.java        # Startup data validation
├── entity/                     # Student, Group, Course
├── service/
│   ├── students/                 # StudentService, StudentServiceImpl
│   ├── groups/                   # GroupService, GroupServiceImpl
│   └── courses/                  # CourseService, CourseServiceImpl
└── database/
    ├── JdbcConfig.java           # DataSource configuration
    └── service/                  # Data generation helpers
```

## Testing

- **Unit tests** — `RandomizerTest`
- **Integration tests** — `CourseServiceIntegrationTest`, `GroupServiceIntegrationTest`, `StudentServiceIntegrationTest`, run against a real PostgreSQL container via Testcontainers

## Running Locally

This is a **console application** — it needs an interactive terminal, so start the database first, then run the app with `docker compose run` (not `up`):

```bash
git clone https://github.com/chizhiks/school-console-app.git
cd school-console-app

docker compose up -d postgres
docker compose run --rm app
```

The first run builds the app image automatically. The interactive menu appears directly in your terminal — no local Java, Maven, or PostgreSQL installation needed.

Tests (`mvn test`) spin up their own PostgreSQL via Testcontainers — Docker must be running.

## Author

Andrii Chyzhov
