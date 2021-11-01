<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core_1_1" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: jeffrey
  Date: 2021/9/9
  Time: 8:44 下午
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
        <c:forEach items="${list}" var="item">
            <tr>
                <td><a href="${pageContext.request.contextPath}/set?file=static/video/${ver}/${item}">${item}</a></td>
                <td><a download="${item}" href="${pageContext.request.contextPath}/static/video/${ver}/${item}">点击下载</a></td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
