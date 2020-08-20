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
	public static String table = "products";
	
	public Product () {
		super();
		db = new Database();
		
		System.out.println(db);
	}
	
	private void setId (int id) {
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
		
		String sql = "INSERT INTO " + table + " (code, name, status) VALUES (?, ?, ?)";
		
		try {
			
			PreparedStatement pStat = db.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pStat.setString(1, getCode());
			pStat.setString(2, getStatus());
			
			int outCome = db.update(pStat);
			
			if (outCome >= 1) {
				setId(Database.getInsertedId(pStat));
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
		return retrieveRecord("SELECT * FROM " + table);
	}
	
	/**
	 * Reads a Product record from the database where the <code>productId</code> matches.
	 * @return An Object wrapping a <code>Product</code>.
	 */
	public Object readById (int productId) {
		List <Product> product = retrieveRecord("SELECT * FROM " + table + " WHERE id="+productId);
		return !product.isEmpty() ? product.get(0) : false;
	}
	
	//Untested
	public boolean update () {
		
		String sql = "UPDATE "  + table +  " SET "
				+ "code='" + getCode() + "', "
				+ "name='" + getName() + "', "
				+ "status='" + getStatus() +"' " 
				+ "WHERE id=" + getId();
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
		
		String sql = "DELETE FROM "  + table + " WHERE id ="+ getId();
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
