<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Product</title>
 
<link href="<c:url value='/static/css/style.css' />" rel="stylesheet"></link>
 
</head>
<body>
 
   <jsp:include page="_header.jsp" />
   <jsp:include page="_menu.jsp" />
 
   <div class="page-title">Feedback</div>
  
   <c:if test="${not empty errorMessage }">
     <div class="error-message">
         ${errorMessage}
     </div>
   </c:if>
 
   <form:form modelAttribute="feedbackForm" method="POST" enctype="multipart/form-data">
       <table style="text-align:left;">
           <tr>
               <td>OrderID *</td>
               <td style="color:red;">
                  <c:if test="${not empty feedbackForm.orderid}">
                       <form:hidden path="orderid"/>
                       ${feedbackForm.orderid}
                  </c:if>
                  <c:if test="${empty feedbackForm.orderid}">
                       <form:input path="orderid" />
                       <form:hidden path="newFeedback" />
                  </c:if>
               </td>
               <td><form:errors path="orderid" class="error-message" /></td>
           </tr>

 
           <tr>
               <td>Review *</td>
               <td><form:input path="review" /></td>
               <td><form:errors path="review" class="error-message" /></td>
           </tr>
 
           <tr>
               <td>Rating *</td>
               <td><form:input path="rating" type="number" min="0" max="5" step="1"/></td>
               <td><form:errors path="rating" class="error-message" /></td>
           </tr>
 
 
           <tr>
               <td>&nbsp;</td>
               <td><input type="submit" value="Submit" /> <input type="reset"
                   value="Reset" /></td>
           </tr>
       </table>
   </form:form>
 
 
 
 
   <jsp:include page="_footer.jsp" />
 
</body>
</html>