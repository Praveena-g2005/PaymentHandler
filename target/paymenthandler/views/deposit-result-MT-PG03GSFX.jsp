<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.paymenthandler.model.DepositResponse" %>
<%
    DepositResponse depositResponse = (DepositResponse) request.getAttribute("response");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Deposit Result</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 600px; margin: 50px auto; padding: 20px; }
        .result { padding: 20px; border-radius: 5px; margin-bottom: 20px; }
        .success { background-color: #d4edda; border: 1px solid #c3e6cb; color: #155724; }
        .error { background-color: #f8d7da; border: 1px solid #f5c6cb; color: #721c24; }
        .info { margin: 10px 0; }
        .info strong { display: inline-block; width: 150px; }
        a { color: #007bff; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <h1>Deposit Result</h1>

    <% if (depositResponse != null && depositResponse.isSuccess()) { %>
        <div class="result success">
            <h2>✅ Deposit Successful!</h2>
            <div class="info">
                <strong>Message:</strong> <%= depositResponse.getMessage() %>
            </div>
            <div class="info">
                <strong>Transaction ID:</strong> <%= depositResponse.getTransactionId() %>
            </div>
            <div class="info">
                <strong>New Balance:</strong> ₹<%= String.format("%.2f", depositResponse.getNewBalance()) %>
            </div>
        </div>
    <% } else if (depositResponse != null) { %>
        <div class="result error">
            <h2>❌ Deposit Failed</h2>
            <div class="info">
                <strong>Error:</strong> <%= depositResponse.getMessage() %>
            </div>
            <div class="info">
                <strong>Current Balance:</strong> ₹<%= String.format("%.2f", depositResponse.getNewBalance()) %>
            </div>
            <p>Please try again with a different card.</p>
        </div>
    <% } else { %>
        <div class="result error">
            <h2>❌ Error</h2>
            <p>No response data available.</p>
        </div>
    <% } %>

    <p>
        <a href="${pageContext.request.contextPath}/wallet/deposit">Make Another Deposit</a> |
        <a href="${pageContext.request.contextPath}/payment/process">Make Payment</a>
    </p>
</body>
</html>
