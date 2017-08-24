<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=utf-8" %>
<script src="https://code.jquery.com/jquery-3.0.0.min.js"></script>


<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Look for goods</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">

    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>

</head>

<body>

<form id="logoutForm" method="POST" action="${contextPath}/logout">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <form action="${contextPath}/login">
                    <button class="form-logout" type="submit" data-target=".navbar-collapse">
                        Back
                    </button>
                </form>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
    <form action="/user-list/search">
        <ul class="nav navbar-nav margin-to-table">
            <li>
                <input name="value" type="text" class="form-control" autofocus="true"/>
            </li>
            <li>
                <button class="table-button block" style="margin-left: 5px" type="submit">Search</button>
            </li>
        </ul>
    </form>
    <c:if test="${products != null}">
        <table class="table">
            <form id="searchs">
                <c:forEach items="${products}" var="product">
                    <tr class="th">
                        <td class="th" style="width: 160px">
                            <a href="${product.url}">
                                <img height="120" width="120" src="${product.imageUrl}"/>
                            </a>
                        </td>
                        <td class="th, text-left">
                            Name:
                            <a href="${product.url}">
                                <c:out value="${product.name}"></c:out>
                            </a>
                            <br/>
                            Price:
                            <c:out value="${product.price}"></c:out>
                        </td>
                    </tr>
                </c:forEach>
            </form>
        </table>
    </c:if>
</div>
<!-- /container -->
<script src="${contextPath}/resources/js/scroll.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>