<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Create New User</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 { color: #333; }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #555;
        }
        input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            font-size: 14px;
        }
        input:focus {
            outline: none;
            border-color: #007bff;
        }
        .btn {
            padding: 12px 30px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            margin-right: 10px;
        }
        .btn:hover { background: #0056b3; }
        .btn-secondary {
            background: #6c757d;
        }
        .btn-secondary:hover { background: #545b62; }
        .info {
            background: #d1ecf1;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
            border-left: 4px solid #17a2b8;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Create New User</h1>

        <div class="info">
            <strong>Note:</strong> After creating a user, a default wallet balance of $0.00 will be initialized.
        </div>

        <form method="post" action="${pageContext.request.contextPath}/users">
            <input type="hidden" name="action" value="create">

            <div class="form-group">
                <label for="name">Username *</label>
                <input type="text" id="name" name="name" required
                       placeholder="Enter username (e.g., John Doe)"
                       minlength="2" maxlength="100">
            </div>

            <div class="form-group">
                <label for="email">Email Address *</label>
                <input type="email" id="email" name="email" required
                       placeholder="Enter email (e.g., john@example.com)"
                       maxlength="255">
                <small style="color: #666;">Email must be unique</small>
            </div>

            <button type="submit" class="btn">Create User</button>
            <a href="${pageContext.request.contextPath}/users" class="btn btn-secondary"
               style="text-decoration: none; display: inline-block;">Cancel</a>
        </form>
    </div>
</body>
</html>
