package com.soft.db;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Product {
	
	private static Database db;
	
	private int id;
	private String code;
	private String name;
	private String status;
	
	public Product () {
		super();
		db = new Database();
		
		System.out.println(db.getConnection());
	}
	
	public void setId (int id) {
		this.id = id;
	}
	public int getId () {
		return this.id;
	}
	
	public void setCode (String code) {
		this.code = code;
	}
	public String getCode () {
		return this.code;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	public String getName () {
		return this.name;
	}
	
	public void setStatus (String status) {
		this.status = status;
	}
	public String getStatus () {
		return this.status;
	}
	
	/**
	 * Creates a Product records in the database
	 * @return true if successfully created, false otherwise
	 */
	public boolean create() {
		
		String sql = "INSERT INTO products (code, name, status) VALUES (?, ?, ?)";
		
		try {
			
			PreparedStatement pStat = db.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pStat.setString(1, this.code);
			pStat.setString(2, this.name);
			pStat.setString(3, this.status);
			
			int outCome = db.update(pStat);
			
			if (outCome >= 1) {
				this.id = getInsertedId(pStat);
				return true;
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
		
	}
	
	/**
	 * Reads all Product records from the database
	 * @return A <code>HashMap</code> Object containing a list of Products mapping them to their IDs
	 */
	public static List <Product> read () {
		return retrieveRecord("SELECT * FROM products");
	}
	
	//Untested
	public static Object readById (int productId) {
		List <Product> product = retrieveRecord("SELECT * FROM products WHERE id="+productId);
		return !product.isEmpty() ? product.get(productId) : false;
	}
	
	//Untested
	public boolean update () {
		
		String sql = "UPDATE products SET "
				+ "code='" + this.code + "', "
				+ "name='" + this.name + "', "
				+ "status='" + this.status +"' " 
				+ "WHERE id=" + this.id;
		int outCome = 0;
		
		try {
			PreparedStatement pStat = db.getConnection().prepareStatement(sql);
			outCome = db.update(pStat);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return outCome >= 1 ? true : false;
		
	}
	
	//Untested
	public boolean delete () {
		
		String sql = "DELETE FROM products WHERE id ="+ this.id;
		int outCome = 0;
		
		try {
			PreparedStatement pStat = db.getConnection().prepareStatement(sql);
			outCome = db.update(pStat);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return outCome >= 1 ? true : false;
		
	}
	
	
	private static List <Product> retrieveRecord (String sql) {
		
		List <Product> products = null;
		Product product = null;
		
		//Just in case db was not initialized
		if (db == null)
			db = new Database();
		
		try {
			
			PreparedStatement pStat = db.getConnection().prepareStatement(sql);
			ResultSet set = pStat.executeQuery();
			
			if (set.next()) {//Checks if ResultSet contains at least one record
				
				products = new ArrayList<>();
				
				/**
				 * Returns ResultSet row as a HashMap Object
				 * Auto instantiates Product Object with the row (HashMap Object)
				 * And finally adds Product Object to the list of products
				 */
				products.add(Product.instantiate(Product.toArrayRow (set)));
				
				while (set.next()) {//Continue while there are more than one records
					/**
					 * Returns ResultSet row as a HashMap Object
					 * Auto instantiates Product Object with the row (HashMap Object)
					 * And finally adds Product Object to the list of products
					 */
					products.add(Product.instantiate(Product.toArrayRow(set)));
					
				}
				
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return products;
	}
	
	private static HashMap<String, Object> toArrayRow (ResultSet set) throws SQLException {
		
		int count = set.getMetaData().getColumnCount();
		HashMap<String, Object> object = new HashMap<>();
		
		for (int i = 0; i < count; i++) {
			object.put(set.getMetaData().getColumnName(i+1), set.getObject(i+1));//Maps column name to value
		}
		
		return object;
		
	}
	
	
	private int getInsertedId (PreparedStatement pStat) throws SQLException {
		ResultSet set = pStat.getGeneratedKeys();
		
		if (set.next()) {
			return set.getInt(1);
		}
		return -1;
	}
	
	public static Product instantiate (HashMap<String, Object> obj) throws Exception, IllegalAccessException, NoSuchFieldException {

		Product product = new Product();
		
		for (String field : obj.keySet()) {
			
			if (hasField(product,field)) {
				
				Field theField = product.getClass().getDeclaredField(field);
				theField.set(product, obj.get(field));
			}
		}
		
		return product;
		
	}
	
	private static boolean hasField (Product product, String field) {
		boolean isMatch = false;
		Field fields[] = product.getClass().getDeclaredFields();
		
		for (Field f : fields) {
			if (f.getName().equals(field)) {
				return true;
			}
		}
		return isMatch;
	}
	
	
}
