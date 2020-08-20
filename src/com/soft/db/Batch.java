package com.soft.db;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Batch {
	
	private static Database db;
	private int id;
	private String code;
	public static String table = "batches";
	
	public Batch () {
		super();
		
		db = new Database();
		System.out.println(db);
	}
	
	private Batch setId (int id) {
		this.id = id;
		return this;
	}
	public int getId () {
		return this.id;
	}
	
	public Batch setCode (String code) {
		this.code = code;
		return this;
	}
	public String getCode () {
		return this.code;
	}
	
	/**
	 * Creates a Product records in the database
	 * @return true if successfully created, false otherwise
	 */
	public boolean create() {
		
		String sql = "INSERT INTO "  + table + " (code) VALUES (?)";
		
		try {
			
			PreparedStatement pStat = db.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pStat.setString(1, getCode());
			
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
	public static List <Batch> read () {
		return retrieveRecord("SELECT * FROM "  + table);
	}
	
	//Untested
	public static Object readById (int batchId) {
		List <Batch> batch = retrieveRecord("SELECT * FROM "  + table + " WHERE id="+batchId);
		return !batch.isEmpty() ? batch.get(batchId) : false;
	}
	
	//Untested
	public boolean update () {
		
		String sql = "UPDATE "  + table + " SET "
				+ "code='" + getCode() + "' "
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
	
	
	private static List <Batch> retrieveRecord (String sql) {
		
		List <Batch> batches = null;
		
		try {
			
			PreparedStatement pStat = db.getConnection().prepareStatement(sql);
			ResultSet set = pStat.executeQuery();
			
			if (set.next()) {//Checks if ResultSet contains at least one record
				
				batches = new ArrayList<>();
				
				/**
				 * Returns ResultSet row as a HashMap Object
				 * Auto instantiates Product Object with the row (HashMap Object)
				 * And finally adds Product Object to the list of products
				 */
				batches.add(Batch.instantiate(Batch.toArrayRow (set)));
				
				while (set.next()) {//Continue while there are more than one records
					/**
					 * Returns ResultSet row as a HashMap Object
					 * Auto instantiates Product Object with the row (HashMap Object)
					 * And finally adds Product Object to the list of products
					 */
					batches.add(Batch.instantiate(Batch.toArrayRow(set)));
					
				}
				
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return batches;
	}
	
	private static HashMap<String, Object> toArrayRow (ResultSet set) throws SQLException {
		
		int count = set.getMetaData().getColumnCount();
		HashMap<String, Object> object = new HashMap<>();
		
		for (int i = 0; i < count; i++) {
			object.put(set.getMetaData().getColumnName(i+1), set.getObject(i+1));//Maps column name to value
		}
		
		return object;
		
	}
	
	public static Batch instantiate (HashMap<String, Object> obj) throws Exception, IllegalAccessException, NoSuchFieldException {

		Batch batch = new Batch ();
		
		for (String field : obj.keySet()) {
			
			if (hasField(batch,field)) {
				
				Field theField = batch.getClass().getDeclaredField(field);
				theField.set(batch, obj.get(field));
			}
		}
		
		return batch;
		
	}
	
	private static boolean hasField (Batch batch, String field) {
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
