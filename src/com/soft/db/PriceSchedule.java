package com.soft.db;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PriceSchedule {
	
	private static Database db;
	
	private int id;
	private int product_id;
	private int batch_id;
	private double cost_price;
	private double sale_price;
	
	public static String table = "price_schedules";
	
	private Product product;
	private Batch batch;
	
	public PriceSchedule () {
		super();
		
		db = new Database();
		System.out.println(db);
		
	}
	
	private PriceSchedule setId (int id) {
		this.id = id;
		return this;
	}
	public int getId () {
		return this.id;
	}
	
	public PriceSchedule setProduct (int product_id) {
		this.product = (Product) new Product().readById(product_id);
		if (this.product != null)this.setProductId(this.product.getId());		
		return this;
	}
	public Product getProduct () {
		return this.product;
	}
	
	private void setProductId (int product_id) {
		this.product_id = product_id;
	}
	public int getProductId () {
		return this.product_id;
	}
	
	public PriceSchedule setBatch (int batch_id) {
		this.batch = (Batch) new Batch().readById(batch_id);
		if ( this.batch != null ) this.setBatchId(this.batch.getId());
		return this;
	}
	public Batch getBatch () {
		return this.batch;
	}
	
	private void setBatchId (int batch_id) {
		this.batch_id = batch_id;
	}
	public int getBatchId () {
		return this.batch_id;
	}
	
	public PriceSchedule setCostPrice (double cost_price) {
		this.cost_price = cost_price;
		return this;
	}
	public double getCostPrice () {
		return this.cost_price;
	}
	
	public PriceSchedule setSalePrice (double sale_price) {
		this.sale_price = sale_price;
		return this;
	}
	public double getSalePrice () {
		return this.sale_price;
	}
	
	
	/**
	 * Creates a Product records in the database
	 * @return true if successfully created, false otherwise
	 */
	public boolean create() throws Exception {
		
		String sql = "INSERT INTO " + table + " (product_id, batch_id, cost_price, sale_price) VALUES (?,?,?,?)";
		
			
		PreparedStatement pStat = db.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		pStat.setInt(1, getProductId());
		pStat.setInt(2, getBatchId());
		pStat.setDouble(3, getCostPrice());
		pStat.setDouble(4, getSalePrice());
		
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
	public static List <PriceSchedule> read () {
		return retrieveRecord("SELECT * FROM " + table);
	}
	
	//Untested
	public static Object readById (int priceScheduleId) {
		List <PriceSchedule> priceSchedules = retrieveRecord("SELECT * FROM " + table + " WHERE id="+priceScheduleId);
		return !priceSchedules.isEmpty() ? priceSchedules.get(priceScheduleId) : false;
	}
	
	//Untested
	public boolean update () {
		
		String sql = "UPDATE " + table + " SET "
				+ "product_id='" + getProductId() + "', "
				+ "batch_id='" + getBatchId() + "', "
				+ "cost_price='" + getCostPrice() + "', "
				+ "sale_price='" + getSalePrice() + "' "
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
	
	
	private static List <PriceSchedule> retrieveRecord (String sql) {
		
		List <PriceSchedule> priceSchedules = null;
		
		try {
			
			PreparedStatement pStat = db.getConnection().prepareStatement(sql);
			ResultSet set = pStat.executeQuery();
			
			if (set.next()) {//Checks if ResultSet contains at least one record
				
				priceSchedules = new ArrayList<>();
				
				/**
				 * Returns ResultSet row as a HashMap Object
				 * Auto instantiates Product Object with the row (HashMap Object)
				 * And finally adds Product Object to the list of products
				 */
				priceSchedules.add(PriceSchedule.instantiate(PriceSchedule.toArrayRow (set)));
				
				while (set.next()) {//Continue while there are more than one records
					/**
					 * Returns ResultSet row as a HashMap Object
					 * Auto instantiates Product Object with the row (HashMap Object)
					 * And finally adds Product Object to the list of products
					 */
					priceSchedules.add(PriceSchedule.instantiate(PriceSchedule.toArrayRow(set)));
					
				}
				
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return priceSchedules;
	}
	
	private static HashMap<String, Object> toArrayRow (ResultSet set) throws SQLException {
		
		int count = set.getMetaData().getColumnCount();
		HashMap<String, Object> object = new HashMap<>();
		
		for (int i = 0; i < count; i++) {
			object.put(set.getMetaData().getColumnName(i+1), set.getObject(i+1));//Maps column name to value
		}
		
		return object;
		
	}
	
	public static PriceSchedule instantiate (HashMap<String, Object> obj) throws Exception, IllegalAccessException, NoSuchFieldException {

		PriceSchedule priceSchedule = new PriceSchedule ();
		
		for (String field : obj.keySet()) {
			
			if (hasField(priceSchedule,field)) {
				
				Field theField = priceSchedule.getClass().getDeclaredField(field);
				theField.set(priceSchedule, obj.get(field));
			}
		}
		
		return priceSchedule;
		
	}
	
	private static boolean hasField (PriceSchedule batch, String field) {
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
