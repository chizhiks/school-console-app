package ua.foxminded.chyzhov.schoolconsoleapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbConnection {

	private static final Logger logger = LoggerFactory.getLogger(DbConnection.class);

	private static DbConnection instance = null;
	private Connection connection = null;

	private DbConnection() {
	}

	private void init() throws SQLException {
		final String JDBC_URL = "jdbc:postgresql://localhost:5432/school_database";
		final String JDBC_USER = "postgres";
		final String JDBC_PASSWORD = "1234";

		try {
			connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
			logger.info("Database connection established successfully");
		} catch (SQLException e) {
			logger.error("Failed to establish database connection", e);
			throw e;
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public static Connection getInstance() throws SQLException {
		try {
			if (instance == null || instance.getConnection().isClosed()) {
				instance = new DbConnection();
				instance.init();
				logger.info("Database connection instance successfully initialized.");
			}
		} catch (SQLException e) {
			logger.error("Failed to initialize database connection instance", e);
			throw e;
		}

		return instance.getConnection();
	}
}