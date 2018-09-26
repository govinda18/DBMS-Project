<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Product List</title>
 
<link href="<c:url value='/static/css/style.css' />" rel="stylesheet"></link>
 
</head>
<body>
 
   <jsp:include page="_header.jsp" />
   <jsp:include page="_menu.jsp" />
    
   <fmt:setLocale value="en_US" scope="session"/>
 	<br>
   <div class="page-title">Change Status</div>
 	<br><br>
 	<form method="POST" action="#">
	  <select name="productId">
	    <option value="1">Pending</option>
	    <option value="2">Dispatched</option>
	    <option value="3">Delivered</option>
	    <option value="4">Cancelled</option>
	  </select>
	  <input type="submit" value="Submit">
	</form>
	<br>
	
 
 
 
 
   <jsp:include page="_footer.jsp" />
 
</body>
</html>