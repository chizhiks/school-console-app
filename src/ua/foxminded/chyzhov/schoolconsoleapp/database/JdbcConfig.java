package ua.foxminded.chyzhov.schoolconsoleapp.database;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class JdbcConfig {

	private static final Logger logger = LoggerFactory.getLogger(JdbcConfig.class);

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/school_database");
		dataSource.setUsername("postgres");
		dataSource.setPassword("1234");

		logger.info("DataSource bean for PostgreSQL connection created successfully");

		return dataSource;
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		logger.info("Creating JdbcTemplate bean");
		return new JdbcTemplate(dataSource);
	}
}
