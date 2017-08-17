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

    <title>Welcome</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
</head>
<body>

<div class="container">

    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <button class="form-logout" type = "submit" onclick="document.forms['logoutForm'].submit()">Logout</button>

        <table class = "table">
            <c:forEach items="${userList}" var="user">
                <c:if test="${pageContext.request.userPrincipal.name != user.username}">
                    <tr class="th">
                        <td class="th, text-center"><c:out value = "${user.username}"/> </td>
                        <td class="th, text-center"><c:out value = "${user.name}"/> </td>
                        <td class="th, text-center">
                            <form action="/user-list/${user.id}">
                                <c:choose>
                                    <c:when test="${user.blocked == true}">
                                        <button class="table-button unblock" type="submit" name="button" value="blockButton">
                                            Unblock
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="table-button block" type="submit" name="button" value="blockButton">
                                            Block
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </form>
                        </td>
                        <td class="th, text-center">
                            <form action = "/user-list/${user.id}">
                                <button class="table-button delete" type = "submit" name="button" value="deleteButton">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
    </c:if>

</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>