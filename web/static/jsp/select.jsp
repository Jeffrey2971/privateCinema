<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core_1_1" %>
<%--
  Created by IntelliJ IDEA.
  User: jeffrey
  Date: 2021/9/10
  Time: 2:30 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <h1>选择要看哪一季</h1>
    <span>观看顺序</span>
    <p>1、刀剑神域第一季（SAO+ALO）</p>
    <p>2、刀剑神域第一季（番外篇）</p>
    <p>3、刀剑神域第二季（幽灵子弹+圣剑+圣母圣咏）</p>
    <p>4、刀剑神域序列之争</p>
    <p>5、刀剑神域第三季（Alicization）</p>
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
