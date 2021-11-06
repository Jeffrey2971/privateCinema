<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core_1_1" %>
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
    <table border="1" style="text-align: center">
        <th><a href="${pageContext.request.contextPath}/upload?action=upload">视频上传</a></th>
        <c:forEach items="${requestScope.ver}" var="item">
            <tr>
                <td><a href="${pageContext.request.contextPath}/player?action=selectVideoList&item=${item}">${item}</a></td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
