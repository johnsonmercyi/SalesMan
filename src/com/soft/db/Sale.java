package com.soft.db;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sale {
	
private static Database db;
	
	private int id;
	private String ref_no;
	private int batch_id;
	private int product_id;
	private String description;
	private double amount;
	public static String table = "sales";
	
	public Sale () {
		super();
		
		db = new Database();
		System.out.println(db);
	}
	
	private Sale setId (int id) {
		this.id = id;
		return this;
	}
	public int getId () {
		return this.id;
	}
	
	public Sale setRefNo (String ref_no) {
		this.ref_no = ref_no;
		return this;
	}
	public String getRefNo () {
		return this.ref_no;
	}
	
	public Sale setBatchId (int batch_id) {
		this.batch_id = batch_id;
		return this;
	}
	public int getBatchId () {
		return this.batch_id;
	}
	
	public Sale setProductId (int product_id) {
		this.product_id = product_id;
		return this;
	}
	public int getProductId () {
		return this.product_id;
	}
	
	public Sale setDescription (String description) {
		this.description = description;
		return this;
	}
	public String getDescription () {
		return this.description;
	}
	
	public Sale setAmount (double cost) {
		this.amount = cost;
		return this;
	}
	public double getAmount () {
		return this.amount;
	}
	
	
	/**
	 * Creates a Sales record in the database
	 * @return true if successfully created, false otherwise
	 */
	public boolean create() throws Exception {
		
		String sql = "INSERT INTO " + table + " (ref_no, batch_id, product_id, description, amount) VALUES (?,?,?,?,?)";
		
			
		PreparedStatement pStat = db.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		pStat.setString(1, getRefNo());
		pStat.setInt(2, getBatchId());
		pStat.setInt(3, getProductId());
		pStat.setString(4, getDescription());
		pStat.setDouble(5, getAmount());
		
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
	public static List <Sale> read () {
		return retrieveRecord("SELECT * FROM " + table);
	}
	
	//Untested
	public static Object readById (int saleId) {
		List <Sale> sale = retrieveRecord("SELECT * FROM " + table + " WHERE id="+saleId);
		return !sale.isEmpty() ? sale.get(saleId) : false;
	}
	
	/**
	 * Updates this Sale record on the database
	 * @return <code>true | false</code> if update is successful or unsuccessful respectively
	 */
	public boolean update () {
		
		String sql = "UPDATE " + table + " SET "
				+ "ref_no='" + getRefNo() + "', "
				+ "batch_id='" + getBatchId() + "', "
				+ "product_id='" + getProductId() + "', "
				+ "description='" + getDescription() + "', "
				+ "cost='" + getAmount() + "' "
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
	
	/**
	 * Deletes this Sale record on the database
	 * @return <code>true | false</code> if update is successful or unsuccessful respectively
	 */
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
	
	
	private static List <Sale> retrieveRecord (String sql) {
		
		List <Sale> sales = null;
		
		try {
			
			PreparedStatement pStat = db.getConnection().prepareStatement(sql);
			ResultSet set = pStat.executeQuery();
			
			if (set.next()) {//Checks if ResultSet contains at least one record
				
				sales = new ArrayList<>();
				
				/**
				 * Returns ResultSet row as a HashMap Object
				 * Auto instantiates Product Object with the row (HashMap Object)
				 * And finally adds Product Object to the list of products
				 */
				sales.add(Sale.instantiate(Sale.toArrayRow (set)));
				
				while (set.next()) {//Continue while there are more than one records
					/**
					 * Returns ResultSet row as a HashMap Object
					 * Auto instantiates Product Object with the row (HashMap Object)
					 * And finally adds Product Object to the list of products
					 */
					sales.add(Sale.instantiate(Sale.toArrayRow(set)));
					
				}
				
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return sales;
	}
	
	private static HashMap<String, Object> toArrayRow (ResultSet set) throws SQLException {
		
		int count = set.getMetaData().getColumnCount();
		HashMap<String, Object> object = new HashMap<>();
		
		for (int i = 0; i < count; i++) {
			object.put(set.getMetaData().getColumnName(i+1), set.getObject(i+1));//Maps column name to value
		}
		
		return object;
		
	}
	
	public static Sale instantiate (HashMap<String, Object> obj) throws Exception, IllegalAccessException, NoSuchFieldException {

		Sale sale = new Sale ();
		
		for (String field : obj.keySet()) {
			
			if (hasField(sale,field)) {
				
				Field theField = sale.getClass().getDeclaredField(field);
				theField.set(sale, obj.get(field));
			}
		}
		
		return sale;
		
	}
	
	private static boolean hasField (Sale sale, String field) {
		boolean isMatch = false;
		Field fields[] = sale.getClass().getDeclaredFields();
		
		for (Field f : fields) {
			if (f.getName().equals(field)) {
				return true;
			}
		}
		return isMatch;
	}
	
}
