<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=utf-8" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>User list</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
</head>
<body>

<form id="logoutForm" method="POST" action="${contextPath}/logout">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li>
                    <button class="form-logout" type = "submit" onclick="document.forms['logoutForm'].submit()">
                        Logout
                    </button>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form action="/user-list/change">
            <button class="table-button delete margin-to-table" type = "submit" name="button" value="deleteButton">
                Delete
            </button>
            <button class="table-button block margin-to-tablex" type="submit" name="button" value="blockButton">
                Block/Unblock
            </button>
            <table class = "table">
                <c:forEach items="${userList}" var="user">
                    <c:if test="${pageContext.request.userPrincipal.name != user.username}">
                        <tr class="th">
                            <td class="th, text-left"><c:out value = "${user.username}"/> </td>
                            <td class="th, text-left"><c:out value = "${user.name}"/> </td>
                            <td class="th, text-center">
                                <c:choose>
                                    <c:when test="${user.blocked == true}">
                                        Blocked
                                    </c:when>
                                    <c:otherwise>
                                        Unblocked
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="th, text-center">
                                <input type="checkbox" name="personId" value="${user.id}"/>
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>
            </table>
        <form/>
    </c:if>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>