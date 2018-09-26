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
       <h3>Thank you for Order</h3>
       Your order number is: ${lastOrderedCart.orderNum}
   </div>
   
   Pay to continue.
   
   <div class="pm-button"><a href="https://www.payumoney.com/paybypayumoney/#/31CA3FCB962B3D9465E22B3B98249A67"><img src="https://www.payumoney.com/media/images/payby_payumoney/new_buttons/21.png"></a></div>
 
   <jsp:include page="_footer.jsp" />
 
</body>
</html>