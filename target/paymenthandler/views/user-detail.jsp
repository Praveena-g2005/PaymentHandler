<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 700px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 { color: #333; }
        .user-card {
            border: 1px solid #ddd;
            padding: 20px;
            margin: 20px 0;
            border-radius: 8px;
            background: #fafafa;
        }
        .detail-row {
            display: flex;
            justify-content: space-between;
            padding: 12px 0;
            border-bottom: 1px solid #eee;
        }
        .detail-row:last-child {
            border-bottom: none;
        }
        .label {
            font-weight: bold;
            color: #555;
            width: 40%;
        }
        .value {
            color: #333;
            width: 60%;
            text-align: right;
        }
        .btn {
            padding: 10px 20px;
            margin: 5px;
            text-decoration: none;
            color: white;
            border-radius: 4px;
            display: inline-block;
            border: none;
            cursor: pointer;
            font-size: 14px;
        }
        .btn-primary {
            background: #007bff;
        }
        .btn-primary:hover { background: #0056b3; }
        .btn-success {
            background: #28a745;
        }
        .btn-success:hover { background: #218838; }
        .btn-danger {
            background: #dc3545;
        }
        .btn-danger:hover { background: #c82333; }
        .btn-secondary {
            background: #6c757d;
        }
        .btn-secondary:hover { background: #545b62; }
        .actions {
            margin-top: 30px;
            padding-top: 20px;
            border-top: 2px solid #eee;
        }
        .user-id {
            background: #17a2b8;
            color: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-weight: bold;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>User Details <span class="user-id">ID: ${user.id}</span></h1>

        <div class="user-card">
            <div class="detail-row">
                <span class="label">User ID:</span>
                <span class="value">${user.id}</span>
            </div>

            <div class="detail-row">
                <span class="label">Username:</span>
                <span class="value">${user.name}</span>
            </div>

            <div class="detail-row">
                <span class="label">Email:</span>
                <span class="value">${user.email}</span>
            </div>
        </div>

        <div class="actions">
            <h3>Actions</h3>

            <form method="post" action="${pageContext.request.contextPath}/users" style="display: inline-block; margin-right: 10px;">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="userId" value="${user.id}">
                <input type="text" name="name" placeholder="New username"
                       style="padding: 10px; border: 1px solid #ddd; border-radius: 4px; margin-right: 5px;">
                <button type="submit" class="btn btn-primary">Update Name</button>
            </form>

            <form method="post" action="${pageContext.request.contextPath}/users" style="display: inline-block; margin-right: 10px;">
                <input type="hidden" name="action" value="login">
                <input type="hidden" name="userId" value="${user.id}">
                <button type="submit" class="btn btn-success">Login as User</button>
            </form>

            <form method="post" action="${pageContext.request.contextPath}/users"
                  style="display: inline-block; margin-right: 10px;"
                  onsubmit="return confirm('Are you sure you want to delete this user?');">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="userId" value="${user.id}">
                <button type="submit" class="btn btn-danger">Delete User</button>
            </form>

            <a href="${pageContext.request.contextPath}/users" class="btn btn-secondary">Back to Users List</a>
        </div>
    </div>
</body>
</html>
