<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Payment Result</title>
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
        .result-card {
            border: 2px solid #ddd;
            padding: 20px;
            margin: 20px 0;
            border-radius: 8px;
        }
        .result-card.success {
            background: #d4edda;
            border-color: #28a745;
        }
        .result-card.error {
            background: #f8d7da;
            border-color: #dc3545;
        }
        .result-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }
        .result-row:last-child {
            border-bottom: none;
        }
        .label {
            font-weight: bold;
            color: #555;
        }
        .value {
            color: #333;
        }
        .status-badge {
            display: inline-block;
            padding: 5px 15px;
            border-radius: 20px;
            font-weight: bold;
            font-size: 14px;
        }
        .status-badge.success {
            background: #28a745;
            color: white;
        }
        .status-badge.failed {
            background: #dc3545;
            color: white;
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
            text-decoration: none;
            display: inline-block;
        }
        .btn:hover { background: #0056b3; }
        .btn-success {
            background: #28a745;
        }
        .btn-success:hover { background: #218838; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Payment Result</h1>

        <c:choose>
            <c:when test="${response.success}">
                <div class="result-card success">
                    <h2 style="margin-top: 0; color: #155724;">
                        Payment Successful
                        <span class="status-badge success">SUCCESS</span>
                    </h2>

                    <div class="result-row">
                        <span class="label">Transaction ID:</span>
                        <span class="value">${response.transactionId}</span>
                    </div>

                    <div class="result-row">
                        <span class="label">Payer ID:</span>
                        <span class="value">${response.payerId}</span>
                    </div>

                    <c:if test="${response.payeeId != null}">
                        <div class="result-row">
                            <span class="label">Payee ID:</span>
                            <span class="value">${response.payeeId}</span>
                        </div>
                    </c:if>

                    <div class="result-row">
                        <span class="label">Amount:</span>
                        <span class="value">$${response.amount}</span>
                    </div>

                    <div class="result-row">
                        <span class="label">Payment Method:</span>
                        <span class="value" style="text-transform: uppercase;">${response.method}</span>
                    </div>

                    <div class="result-row">
                        <span class="label">Message:</span>
                        <span class="value">${response.message}</span>
                    </div>
                </div>
            </c:when>

            <c:otherwise>
                <div class="result-card error">
                    <h2 style="margin-top: 0; color: #721c24;">
                        Payment Failed
                        <span class="status-badge failed">FAILED</span>
                    </h2>

                    <div class="result-row">
                        <span class="label">Payer ID:</span>
                        <span class="value">${response.payerId}</span>
                    </div>

                    <c:if test="${response.payeeId != null}">
                        <div class="result-row">
                            <span class="label">Payee ID:</span>
                            <span class="value">${response.payeeId}</span>
                        </div>
                    </c:if>

                    <div class="result-row">
                        <span class="label">Amount:</span>
                        <span class="value">$${response.amount}</span>
                    </div>

                    <div class="result-row">
                        <span class="label">Payment Method:</span>
                        <span class="value" style="text-transform: uppercase;">${response.method}</span>
                    </div>

                    <div class="result-row">
                        <span class="label">Error:</span>
                        <span class="value" style="color: #721c24; font-weight: bold;">${response.message}</span>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

        <div style="margin-top: 30px;">
            <a href="${pageContext.request.contextPath}/payment/process" class="btn btn-success">Make Another Payment</a>
            <a href="${pageContext.request.contextPath}/" class="btn">Back to Home</a>
        </div>
    </div>
</body>
</html>
