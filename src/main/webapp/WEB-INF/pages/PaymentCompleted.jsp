<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
 
<title>Shopping Cart Finalize</title>
 
<link href="<c:url value='/static/css/style.css' />" rel="stylesheet"></link>
 
</head>
<body>
   <jsp:include page="_header.jsp" />
 
   <jsp:include page="_menu.jsp" />
 
   <div class="page-title">Finalize</div>
  
   <div class="container">
       <h3>Congrats. Your order has been placed successfully.</h3>
       <br><br> Our support will contact you soon.
   </div>
 	
 
</body>
</html>