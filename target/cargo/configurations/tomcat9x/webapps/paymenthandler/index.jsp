<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Payment Handler - Home</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .card {
            border: 1px solid #ddd;
            padding: 20px;
            margin: 20px 0;
            border-radius: 5px;
            background: #fafafa;
        }
        .btn {
            padding: 10px 20px;
            margin: 5px;
            text-decoration: none;
            background: #007bff;
            color: white;
            border-radius: 3px;
            display: inline-block;
            border: none;
            cursor: pointer;
        }
        .btn:hover { background: #0056b3; }
        h1 { color: #333; }
        .session-info {
            background: #d4edda;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Payment Handler Application</h1>

        <div class="session-info">
            <strong>Session Info:</strong>
            Logged in as: <strong>${userSession.userDisplayName}</strong> |
            Page views this session: <strong>${userSession.pageViews}</strong>
        </div>

        <div class="card">
            <h2>User Management</h2>
            <p>Create, view, update, and delete users in the database</p>
            <a href="${pageContext.request.contextPath}/users" class="btn">Manage Users</a>
        </div>

        <div class="card">
            <h2>Process Payments</h2>
            <p>Handle card, UPI, and wallet payments using Strategy pattern</p>
            <a href="${pageContext.request.contextPath}/payment/process" class="btn">Make Payment</a>
        </div>
    </div>
</body>
</html>
