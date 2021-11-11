<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core_1_1" %>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
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
                <td><a href="${pageContext.request.contextPath}/player?action=videoPlayback&file=static/video/${ver}/${item}&name=${item}">${item}</a></td>
                <td><a download="${item}" href="${pageContext.request.contextPath}/static/video/${ver}/${item}">点击下载</a></td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
