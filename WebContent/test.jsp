<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ page import="com.soft.db.Product, java.util.List, java.util.Iterator" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<%
	
	//Product p = new Product();
	//p.setId(1);
	//p.setCode(code);
	//p.setName("Honey");
	//p.setStatus("deactive");
	
		List <Product> products = Product.read(); 
		
		
		
		Iterator<Product> it = products.iterator();
		while (it.hasNext()) {
			Product p = it.next();
			out.append("ID: ").append(""+p.getId()).append("</br>");
			out.append("CODE: ").append(""+p.getCode()).append("</br>");
			out.append("NAME: ").append(""+p.getName()).append("</br>");
			out.append("STATUS: ").append(""+p.getStatus()).append("</br></br>");
			
		}
		
		
	
%>

</body>
</html>