<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="com.soft.db.Product, 
				com.soft.db.Batch, 
				com.soft.db.Purchase,
				com.soft.db.Sale, 
				java.util.List, 
				java.util.Iterator" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Demo Test</title>
</head>
<body>

<%

/*----------------------------------------------------------------------------------*/
/*------------------------ PRODUCT -------------------------------------------------*/
/*----------------------------------------------------------------------------------*/
	
	//Product p = new Product();
	//p.setId(1);
	//p.setCode(code);
	//p.setName("Honey");
	//p.setStatus("deactive");
	
		/* List <Product> products = Product.read(); 
		
		
		
		Iterator<Product> it = products.iterator();
		while (it.hasNext()) {
			Product p = it.next();
			out.append("ID: ").append(""+p.getId()).append("</br>");
			out.append("CODE: ").append(""+p.getCode()).append("</br>");
			out.append("NAME: ").append(""+p.getName()).append("</br>");
			out.append("STATUS: ").append(""+p.getStatus()).append("</br></br>");
			
		}
		 */
		 

 /*----------------------------------------------------------------------------------*/
 /*------------------------ BATCH  --------------------------------------------------*/
 /*----------------------------------------------------------------------------------*/
		
		//Create Batch
		/* boolean isCreated  = new Batch ()
							.setId(1)
							.setCode("BT-002")
							.create();
		 
		out.append("Is Created: ")
			.append("" + isCreated); */
		
		
		//Read Batch
		/* List <Batch> batches = Batch.read();
		Iterator<Batch> it = batches.iterator();
		
		while (it.hasNext()) {
			Batch b = it.next();
			out.append("ID: ").append(""+b.getId()).append("</br>");
			out.append("CODE: ").append(""+b.getCode()).append("</br></br>");
		} */
		
		/*----------------------------------------------------------------------------------*/
		 /*------------------------ PURCHASE  --------------------------------------------------*/
		 /*----------------------------------------------------------------------------------*/
				/* try { 
					
					//Create Purchase
					boolean isCreated  = new Purchase ()
										.setRefNo("ref-0001-006")
										.setBatchId(1)
										.setProductId(3)
										.setDescription("Third Batch of Phones bought")
										.setCost(100000)
										.create();
					 
					out.append("Is Purchase Created: ")
						.append("" + (isCreated ? "Yes!" : "No!")).append("</br></br>");
					
					
					//Read Purchase
					List <Purchase> purchase = Purchase.read();
					Iterator<Purchase> it = purchase.iterator();
					
					while (it.hasNext()) {
						Purchase p = it.next();
						out.append("ID: ").append(""+p.getId()).append("</br>");
						out.append("REF No.: ").append(""+p.getRefNo()).append("</br>");
						out.append("DESCRIPTION: ").append(""+p.getDescription()).append("</br></br>");
					}
				
				} catch (Exception ex) {
					if (ex instanceof java.sql.SQLIntegrityConstraintViolationException) {
						out.append(
								"<span style='color:red;'>"
								+ "Error occured while creating record!</br>REASON: [ref-no] duplicate attempt!"
								+ "</span>"
								);
					}
				} */
				
		 /*----------------------------------------------------------------------------------*/
		 /*------------------------ SALES  --------------------------------------------------*/
		 /*----------------------------------------------------------------------------------*/
				try { 
					
					//Create Sale
					boolean isCreated  = new Sale ()
										.setRefNo("ref-0001-001")
										.setBatchId(1)
										.setProductId(3)
										.setDescription("First Batch of phones sold!")
										.setAmount(100000)
										.create();
					 
					out.append("Is Sale Created: " + (isCreated ? "Yes!" : "No!") + "</br></br>");
					
					
					//Read Sales
					List <Sale> sales = Sale.read();
					Iterator<Sale> it = sales.iterator();
					
					while (it.hasNext()) {
						Sale p = it.next();
						out.append("SALE ID: ").append(""+p.getId()).append("</br>");
						out.append("REF No.: ").append(""+p.getRefNo()).append("</br>");
						out.append("DESCRIPTION: ").append(""+p.getDescription()).append("</br></br>");
					}
				
				} catch (Exception ex) {
					if (ex instanceof java.sql.SQLIntegrityConstraintViolationException) {
						out.append(
								"<span style='color:red;'>"
								+ "Error occured while creating record!</br>REASON: [ref-no] duplicate attempt!"
								+ "</span>"
								);
					}
				}
				
		
	
%>

</body>
</html>