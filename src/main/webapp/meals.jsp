<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.topjava.model.MealWithExceed" %><%--
  Created by IntelliJ IDEA.
  User: janna.silonosova
  Date: 04.07.18
  Time: 11:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals detail info</h2>
<table border="1">
    <tr>
        <th>Date&Time</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach var="meal" items="${requestScope.mlist}">
        <c:set var="color" value="green"/>
    <c:if test="${meal.exceed}">
        <c:set var="color" value="red"/>
    </c:if>
    <tr bgcolor="<c:out value="${color}"/>">
            <td>${meal.dateTimeString}</td>
            <%--I tried formatting also from jsp page but without success. Look please what is wrong.--%>
            <%--<td><fmt:formatDate value="${meal.dateTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>--%>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
