<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Deposit to Wallet</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 600px; margin: 50px auto; padding: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input { width: 100%; padding: 8px; box-sizing: border-box; }
        button { background-color: #4CAF50; color: white; padding: 10px 20px; border: none; cursor: pointer; font-size: 16px; }
        button:hover { background-color: #45a049; }
        .test-cards { background-color: #f0f0f0; padding: 15px; margin-top: 20px; border-radius: 5px; }
        .test-cards h3 { margin-top: 0; }
        .test-cards ul { margin: 5px 0; }
    </style>
</head>
<body>
    <h1>Deposit Money to Wallet</h1>

    <form action="${pageContext.request.contextPath}/wallet/deposit" method="POST">
        <div class="form-group">
            <label for="amount">Amount (₹):</label>
            <input type="number" id="amount" name="amount" step="0.01" min="1" max="100000" required />
        </div>

        <div class="form-group">
            <label for="cardNumber">Card Number:</label>
            <input type="text" id="cardNumber" name="cardNumber" placeholder="4242 4242 4242 4242" maxlength="19" required />
        </div>

        <div class="form-group">
            <label for="expiryMonth">Expiry Month (1-12):</label>
            <input type="number" id="expiryMonth" name="expiryMonth" min="1" max="12" placeholder="12" required />
        </div>

        <div class="form-group">
            <label for="expiryYear">Expiry Year:</label>
            <input type="number" id="expiryYear" name="expiryYear" min="2024" max="2050" placeholder="2025" required />
        </div>

        <div class="form-group">
            <label for="cvv">CVV:</label>
            <input type="text" id="cvv" name="cvv" maxlength="4" placeholder="123" required />
        </div>

        <div class="form-group">
            <label for="cardholderName">Cardholder Name:</label>
            <input type="text" id="cardholderName" name="cardholderName" placeholder="John Doe" required />
        </div>

        <button type="submit">Deposit Money</button>
    </form>

    <div class="test-cards">
        <h3>Test Cards </h3>
        <ul>
            <li><strong>Success:</strong> 4242 4242 4242 4242</li>
            <li><strong>Declined:</strong> 4000 0000 0000 0002</li>
        </ul>
    </div>

    <p><a href="${pageContext.request.contextPath}/payment/process">← Back to Payments</a></p>
</body>
</html>
