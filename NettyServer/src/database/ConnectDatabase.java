package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDatabase {

	public static ConnectDatabase instance;	
	
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_CONNECTION = "jdbc:mysql://"+DatabaseConfig.IP_SERVER_MYSQL+":"+
	DatabaseConfig.PORT_SERVER_MYSQL+"/"+DatabaseConfig.DB_NAME;

	public ConnectDatabase() {
		// TODO Auto-generated constructor stub
	}
	
	public static ConnectDatabase getInstance(){
		if(instance==null)
			instance=new ConnectDatabase();
		return instance;
	}
	
	/*
	 * Connection database
	 */
	public Connection getDBConnection() {
		Connection dbConnection = null;
		System.out.println("Connecting Database:");
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		try {
		dbConnection = DriverManager.getConnection(DB_CONNECTION, DatabaseConfig.DB_USER,DatabaseConfig.DB_PASSWORD);
			System.out.println("Connected!");
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return dbConnection;

	}
	
	

}
