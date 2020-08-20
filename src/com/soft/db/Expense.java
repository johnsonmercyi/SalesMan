package com.soft.db;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Expense {

private static Database db;
	
	private int id;
	private int batch_id;
	private String description;
	private double amount;
	public static String table = "expenses";
	
	public Expense () {
		super();
		
		db = new Database();
		System.out.println(db);
	}
	
	private Expense setId (int id) {
		this.id = id;
		return this;
	}
	public int getId () {
		return this.id;
	}
	
	public Expense setBatchId (int batch_id) {
		this.batch_id = batch_id;
		return this;
	}
	public int getBatchId () {
		return this.batch_id;
	}
	
	public Expense setDescription (String description) {
		this.description = description;
		return this;
	}
	public String getDescription () {
		return this.description;
	}
	
	public Expense setAmount (double amount) {
		this.amount = amount;
		return this;
	}
	public double getAmount () {
		return this.amount;
	}
	
	
	/**
	 * Creates a Product records in the database
	 * @return true if successfully created, false otherwise
	 */
	public boolean create() throws Exception {
		
		String sql = "INSERT INTO " + table + " (batch_id, description, amount) VALUES (?,?,?)";
		
			
		PreparedStatement pStat = db.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		pStat.setInt(1, getBatchId());
		pStat.setString(2, getDescription());
		pStat.setDouble(3, getAmount());
		
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
	public static List <Expense> read () {
		return retrieveRecord("SELECT * FROM " + table);
	}
	
	//Untested
	public static Object readById (int expenseId) {
		List <Expense> expense = retrieveRecord("SELECT * FROM " + table + " WHERE id="+expenseId);
		return !expense.isEmpty() ? expense.get(expenseId) : false;
	}
	
	//Untested
	public boolean update () {
		
		String sql = "UPDATE " + table + " SET "
				+ "batch_id='" + getBatchId() + "', "
				+ "description='" + getDescription() + "', "
				+ "amount='" + getAmount() + "' "
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
	
	
	private static List <Expense> retrieveRecord (String sql) {
		
		List <Expense> expenses = null;
		
		try {
			
			PreparedStatement pStat = db.getConnection().prepareStatement(sql);
			ResultSet set = pStat.executeQuery();
			
			if (set.next()) {//Checks if ResultSet contains at least one record
				
				expenses = new ArrayList<>();
				
				/**
				 * Returns ResultSet row as a HashMap Object
				 * Auto instantiates Product Object with the row (HashMap Object)
				 * And finally adds Product Object to the list of products
				 */
				expenses.add(Expense.instantiate(Expense.toArrayRow (set)));
				
				while (set.next()) {//Continue while there are more than one records
					/**
					 * Returns ResultSet row as a HashMap Object
					 * Auto instantiates Product Object with the row (HashMap Object)
					 * And finally adds Product Object to the list of products
					 */
					expenses.add(Expense.instantiate(Expense.toArrayRow(set)));
					
				}
				
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return expenses;
	}
	
	private static HashMap<String, Object> toArrayRow (ResultSet set) throws SQLException {
		
		int count = set.getMetaData().getColumnCount();
		HashMap<String, Object> object = new HashMap<>();
		
		for (int i = 0; i < count; i++) {
			object.put(set.getMetaData().getColumnName(i+1), set.getObject(i+1));//Maps column name to value
		}
		
		return object;
		
	}
	
	public static Expense instantiate (HashMap<String, Object> obj) throws Exception, IllegalAccessException, NoSuchFieldException {

		Expense expense = new Expense ();
		
		for (String field : obj.keySet()) {
			
			if (hasField(expense,field)) {
				
				Field theField = expense.getClass().getDeclaredField(field);
				theField.set(expense, obj.get(field));
			}
		}
		
		return expense;
		
	}
	
	private static boolean hasField (Expense batch, String field) {
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
