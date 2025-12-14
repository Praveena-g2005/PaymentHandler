<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Process Payment</title>
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
        input, select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            font-size: 14px;
        }
        .btn {
            padding: 12px 30px;
            background: #28a745;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            margin-right: 10px;
        }
        .btn:hover { background: #218838; }
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
        <h1>Process Payment</h1>

        <div class="info">
            <strong>Payment Methods Available:</strong>
            <ul style="margin: 10px 0; padding-left: 20px;">
                <li><strong>card</strong> - Credit/Debit Card (No balance validation)</li>
                <li><strong>upi</strong> - UPI Transfer (No balance validation)</li>
                <li><strong>wallet</strong> - Wallet Transfer (Validates balance)</li>
            </ul>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/payment/process">
            <div class="form-group">
                <label for="payerId">Payer ID *</label>
                <input type="number" id="payerId" name="payerId" required
                       placeholder="Enter payer user ID (e.g., 1)" min="1">
            </div>

            <div class="form-group">
                <label for="payeeId">Payee ID </label>
                <input type="number" id="payeeId" name="payeeId"
                       placeholder="Enter payee user ID " min="1">
                <small style="color: #666;">Leave empty for one-way transactions</small>
            </div>

            <div class="form-group">
                <label for="amount">Amount *</label>
                <input type="number" id="amount" name="amount" required
                       placeholder="Enter amount (e.g., 100.50)" min="0.01" step="0.01">
            </div>

            <div class="form-group">
                <label for="method">Payment Method *</label>
                <select id="method" name="method" required>
                    <option value="">-- Select Payment Method --</option>
                    <option value="card">Card Payment</option>
                    <option value="upi">UPI Transfer</option>
                    <option value="wallet">Wallet Transfer</option>
                </select>
            </div>

            <button type="submit" class="btn">Process Payment</button>
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary"
               style="text-decoration: none; display: inline-block;">Cancel</a>
        </form>
    </div>
</body>
</html>
