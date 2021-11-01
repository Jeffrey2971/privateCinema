<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core_1_1" %>
<%--
  Created by IntelliJ IDEA.
  User: jeffrey
  Date: 2021/9/10
  Time: 2:30 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>播放列表</title>
    <style>
        div {
            text-align: center;
        }
        table {
            margin: auto;
        }

    </style>
</head>
<body>
<div>
    <table border="1">
        <c:forEach items="${requestScope.ver}" var="item">
            <tr>
                <td><a href="${pageContext.request.contextPath}/lists?item=${item}">${item}</a></td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
