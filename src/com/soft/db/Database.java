package com.soft.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.soft.config.Config;

public class Database {

	//Fields
	private Connection con;
	private String driver;
	private String db_name;
	private String db_url;
	private String db_username;
	private String db_password;
	
	//Constructor
	public Database () {
		super ();
		
		this.driver = Config.Database.DRIVER;
		this.db_name = Config.Database.DB_NAME;
		this.db_url = Config.Database.DB_URL;
		this.db_username = Config.Database.DB_USERNAME;
		this.db_password = Config.Database.DB_PASSWORD;
		
		//Try connecting if not connected
	    connect();
	}
	
	
	/*
	 * Setters and Getters: Use setter and getter methods
	 * only if you need to use another DBMS connection setup.
	 * The default connection setup is for MySQL Database.
	 */
	
	public void setDriver (String driver) {
		this.driver = driver;
	}
	public String getDriver () {
		return this.driver;
	}
	
	public void setDbName (String db_name) {
		this.db_name = db_name;
	}
	public String getDbName () {
		return this.db_name;
	}
	
	public void setDbUrl (String db_url) {
		this.db_url = db_url;
	}
	public String getDbUrl () {
		return this.db_url;
	}
	
	public void setDbUsername (String db_username) {
		this.db_username = db_username;
	}
	public String getDbUsername () {
		return this.db_username;
	}
	
	public void setDbPassword (String db_password) {
		this.db_password = db_password;
	}
	public String getDbPassword () {
		return this.db_password;
	}
	
	public Connection getConnection () {
		return con;
	}
	
	
	
	//Utility Functions
	
	/**
	 * Tries to establish a connection to the database
	 */
	public void connect() {
		try {
			if (!isConnected()) {
				
				Class.forName(this.driver);
				this.con = DriverManager.getConnection(this.db_url, this.db_username, this.db_password);
				
			}
		} catch (Exception ex) {
			if (ex instanceof ClassNotFoundException) {
				ex.printStackTrace();
			}
			if (ex instanceof SQLException) {
				ex.printStackTrace();
			}
		}
		
	}
	
	public boolean isConnected(){
        return this.con != null;
    }
	
	/**
	 * Queries the Database for a record or more. 
	 * @param sql SQL string
	 * @return <code>ResultSet</code> Object
	 */
	public ResultSet query(String sql) throws SQLException {
		PreparedStatement pStat = this.con.prepareStatement(sql);
		return pStat.executeQuery();
	}
	
	/**
	 * <code>INSERT, UPDATE, DELETE</code> records on the Database. 
	 * @param sql SQL string
	 * @return <code>int</code> value
	 */
	public int update (PreparedStatement pStat) throws SQLException {
		return pStat.executeUpdate();
	}
	
	public static int getInsertedId (PreparedStatement pStat) throws SQLException {
		ResultSet set = pStat.getGeneratedKeys();
		
		if (set.next()) {
			return set.getInt(1);
		}
		return -1;
	}
	
	@Override
	public String toString() {
		if (this.isConnected()) {
			return "Connection Successfull!";
		}
		
		return "Connection failed!";
	}
	
}
