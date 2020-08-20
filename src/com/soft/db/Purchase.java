package com.soft.db;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Purchase {
	
	private static Database db;
	
	private int id;
	private String ref_no;
	private int batch_id;
	private int product_id;
	private String description;
	private double cost;
	public static String table = "purchases";
	
	public Purchase () {
		super();
		
		db = new Database();
		System.out.println(db);
	}
	
	private Purchase setId (int id) {
		this.id = id;
		return this;
	}
	public int getId () {
		return this.id;
	}
	
	public Purchase setRefNo (String ref_no) {
		this.ref_no = ref_no;
		return this;
	}
	public String getRefNo () {
		return this.ref_no;
	}
	
	public Purchase setBatchId (int batch_id) {
		this.batch_id = batch_id;
		return this;
	}
	public int getBatchId () {
		return this.batch_id;
	}
	
	public Purchase setProductId (int product_id) {
		this.product_id = product_id;
		return this;
	}
	public int getProductId () {
		return this.product_id;
	}
	
	public Purchase setDescription (String description) {
		this.description = description;
		return this;
	}
	public String getDescription () {
		return this.description;
	}
	
	public Purchase setCost (double cost) {
		this.cost = cost;
		return this;
	}
	public double getCost () {
		return this.cost;
	}
	
	
	/**
	 * Creates a Product records in the database
	 * @return true if successfully created, false otherwise
	 */
	public boolean create() throws Exception {
		
		String sql = "INSERT INTO " + table + " (ref_no, batch_id, product_id, description, cost) VALUES (?,?,?,?,?)";
		
			
		PreparedStatement pStat = db.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		pStat.setString(1, getRefNo());
		pStat.setInt(2, getBatchId());
		pStat.setInt(3, getProductId());
		pStat.setString(4, getDescription());
		pStat.setDouble(5, getCost());
		
		int outCome = db.update(pStat);
		
		if (outCome >= 1) {
			setId(Database.getInsertedId(pStat));
			return true;
		}
		
		
		return false;
		
	}
	
	/**
	 * Reads all Product records from the database
	 * @return A <code>HashMap</code> Object containing a list of Products mapping them to their IDs
	 */
	public static List <Purchase> read () {
		return retrieveRecord("SELECT * FROM " + table);
	}
	
	//Untested
	public static Object readById (int purchaseId) {
		List <Purchase> purchase = retrieveRecord("SELECT * FROM " + table + " WHERE id="+purchaseId);
		return !purchase.isEmpty() ? purchase.get(purchaseId) : false;
	}
	
	//Untested
	public boolean update () {
		
		String sql = "UPDATE " + table + " SET "
				+ "ref_no='" + getRefNo() + "', "
				+ "batch_id='" + getBatchId() + "', "
				+ "product_id='" + getProductId() + "', "
				+ "description='" + getDescription() + "', "
				+ "cost='" + getCost() + "' "
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
		
		String sql = "DELETE FROM " + table + " WHERE id ="+ getId();
		int outCome = 0;
		
		try {
			PreparedStatement pStat = db.getConnection().prepareStatement(sql);
			outCome = db.update(pStat);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return outCome >= 1 ? true : false;
		
	}
	
	
	private static List <Purchase> retrieveRecord (String sql) {
		
		List <Purchase> purchases = null;
		
		try {
			
			PreparedStatement pStat = db.getConnection().prepareStatement(sql);
			ResultSet set = pStat.executeQuery();
			
			if (set.next()) {//Checks if ResultSet contains at least one record
				
				purchases = new ArrayList<>();
				
				/**
				 * Returns ResultSet row as a HashMap Object
				 * Auto instantiates Product Object with the row (HashMap Object)
				 * And finally adds Product Object to the list of products
				 */
				purchases.add(Purchase.instantiate(Purchase.toArrayRow (set)));
				
				while (set.next()) {//Continue while there are more than one records
					/**
					 * Returns ResultSet row as a HashMap Object
					 * Auto instantiates Product Object with the row (HashMap Object)
					 * And finally adds Product Object to the list of products
					 */
					purchases.add(Purchase.instantiate(Purchase.toArrayRow(set)));
					
				}
				
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return purchases;
	}
	
	private static HashMap<String, Object> toArrayRow (ResultSet set) throws SQLException {
		
		int count = set.getMetaData().getColumnCount();
		HashMap<String, Object> object = new HashMap<>();
		
		for (int i = 0; i < count; i++) {
			object.put(set.getMetaData().getColumnName(i+1), set.getObject(i+1));//Maps column name to value
		}
		
		return object;
		
	}
	
	public static Purchase instantiate (HashMap<String, Object> obj) throws Exception, IllegalAccessException, NoSuchFieldException {

		Purchase purchase = new Purchase ();
		
		for (String field : obj.keySet()) {
			
			if (hasField(purchase,field)) {
				
				Field theField = purchase.getClass().getDeclaredField(field);
				theField.set(purchase, obj.get(field));
			}
		}
		
		return purchase;
		
	}
	
	private static boolean hasField (Purchase batch, String field) {
		boolean isMatch = false;
		Field fields[] = batch.getClass().getDeclaredFields();
		
		for (Field f : fields) {
			if (f.getName().equals(field)) {
				return true;
			}
		}
		return isMatch;
	}
	
	
}
