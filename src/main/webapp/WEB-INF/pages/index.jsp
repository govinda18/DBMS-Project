<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
 
<title>Narmada Sanitations</title>
 
<link href="<c:url value='/static/css/style.css' />" rel="stylesheet"></link>
<style>
body {
   background-image: url("http://www.technomix.com/uploads/home_image/Senso_AmbienteDisco_800x533.jpg");
  height: 100%; 

    /* Center and scale the image nicely */
    background-position: center;
    background-repeat: no-repeat;
    background-size: cover;
    color:white;
}
</style>
</head>
<body>
 
 
   <jsp:include page="_header.jsp" />
   <jsp:include page="_menu.jsp" />
 	
 	<center><h1>Welcome to Narmada Sanitations!!!</h1></center>
 	<center><h2>About us</h2></center>
  	<h4 style="color:white">We are a retail and whole sale sanitatory material provider. Do visit us at 1694, Sudama Nagar, Indore.</h4>
   <div style="color:white">
   <h3>Functionalities</h3>
  
   <ul>
      <li>Buy online</li>
      <li>Admin pages</li>
      <li>Reports</li>
      <li>Payment Gateway with PayUMoney</li>
   </ul>
   </div>
  
  
   <jsp:include page="_footer.jsp" />
 
</body>
</html>