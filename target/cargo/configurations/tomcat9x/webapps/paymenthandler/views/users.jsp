<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Users - JSP Demo</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #007bff; color: white; }
    </style>
</head>
<body>
    <h1>User List (JSP Demo)</h1>

    <!-- JSP EL Expression for session bean -->
    <p>Logged in as: <strong>${userSession.userDisplayName}</strong></p>
    <p>Page views: ${userSession.pageViews}</p>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
            </tr>
        </thead>
        <tbody>
            <!-- JSTL forEach loop -->
            <c:forEach items="${users}" var="user">
                <tr>
                    <!-- EL expressions -->
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                </tr>
            </c:forEach>

            <!-- JSTL conditional -->
            <c:if test="${empty users}">
                <tr>
                    <td colspan="3">No users found</td>
                </tr>
            </c:if>
        </tbody>
    </table>

    <br>
    <a href="${pageContext.request.contextPath}/">Back to Home</a>
</body>
</html>
