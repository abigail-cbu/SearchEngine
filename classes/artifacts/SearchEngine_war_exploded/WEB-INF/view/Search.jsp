<%--
  Created by IntelliJ IDEA.
  User: turtle
  Date: 10/27/18
  Time: 8:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
  <title>Search Page</title>
</head>
<body>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<form method="POST" action="${pageContext.request.contextPath}/Search">
  <div style="text-align: center"><input type="text" style="width:300px" name="searchKey" />
    <input type="submit" value="Search" /></div>
</form>
<div>
  <table>
    <%--
  <c:if test="${links_list ne ''}">
    <c:foreach items="${links_list}" var="links">
      <tr>
        ${links.title}<br />
        ${links.link_page}<br />
        ${links.description}
      <tr>
    </c:foreach>
  </c:if> --%>
  </table>
</div>
</body>
</html>
