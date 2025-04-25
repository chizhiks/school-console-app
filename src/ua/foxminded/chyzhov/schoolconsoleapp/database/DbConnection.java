package ua.foxminded.chyzhov.schoolconsoleapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

	private static DbConnection instance = null;
	private Connection connection = null;

	private DbConnection() {
	}

	private void init() throws SQLException {
		final String JDBC_URL = "jdbc:postgresql://localhost:5432/school_database";
		final String JDBC_USER = "postgres";
		final String JDBC_PASSWORD = "1234";

		connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
	}

	public Connection getConnection() {
		return connection;
	}

	public static Connection getInstance() throws SQLException {
		if (instance == null || instance.getConnection().isClosed()) {
			instance = new DbConnection();
			instance.init();
		}

		return instance.getConnection();
	}
}