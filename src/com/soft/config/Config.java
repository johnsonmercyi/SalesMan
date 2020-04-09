package com.soft.config;

public class Config {
	
	public static class Database {
		
		public static String DRIVER = "com.mysql.cj.jdbc.Driver";
		public static String DB_NAME = "salesman";
		public static String DB_URL = "jdbc:mysql://localhost:3306/"+DB_NAME+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		public static String DB_USERNAME = "root";
		public static String DB_PASSWORD = "";
		
	}
	
}