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
                <li><strong>card</strong> - Credit/Debit Card </li>
                <li><strong>upi</strong> - UPI Transfer </li>
                <li><strong>wallet</strong> - Wallet Transfer </li>
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

            <!-- Fee Breakdown Display -->
            <div id="feeBreakdown" style="display: none; background: #fff3cd; padding: 15px; border-radius: 4px; margin-bottom: 20px; border-left: 4px solid #ffc107;">
                <h3 style="margin-top: 0; color: #856404;">Transaction Summary</h3>
                <table style="width: 100%; border-collapse: collapse;">
                    <tr style="border-bottom: 1px solid #ddd;">
                        <td style="padding: 8px 0; color: #555;">Base Amount:</td>
                        <td style="padding: 8px 0; text-align: right; font-weight: bold;">₹<span id="baseAmount">0.00</span></td>
                    </tr>
                    <tr style="border-bottom: 1px solid #ddd;">
                        <td style="padding: 8px 0; color: #555;">Service Fee (<span id="feePercentage">0</span>):</td>
                        <td style="padding: 8px 0; text-align: right; font-weight: bold; color: #d9534f;">₹<span id="serviceFee">0.00</span></td>
                    </tr>
                    <tr style="border-bottom: 2px solid #856404;">
                        <td style="padding: 12px 0; color: #333; font-size: 16px; font-weight: bold;">Total Amount:</td>
                        <td style="padding: 12px 0; text-align: right; font-size: 18px; font-weight: bold; color: #28a745;">₹<span id="totalAmount">0.00</span></td>
                    </tr>
                </table>
                <p style="margin: 10px 0 0 0; font-size: 12px; color: #856404;">
                    <strong>Note:</strong> The total amount (including service fee) will be deducted from the payer's wallet.
                </p>
            </div>

            <button type="submit" class="btn">Process Payment</button>
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary"
               style="text-decoration: none; display: inline-block;">Cancel</a>
        </form>
    </div>

    <script>
        const cardFee = parseFloat('${empty cardFeePercentage ? 0.1 : cardFeePercentage}');
        const upiFee = parseFloat('${empty upiFeePercentage ? 0.02 : upiFeePercentage}');
        const walletFee = parseFloat('${empty walletFeePercentage ? 0 : walletFeePercentage}');

        const FEE_PERCENTAGES = {
            'card': cardFee,
            'upi': upiFee,
            'wallet': walletFee
        };

        // Getting form elements
        const amountInput = document.getElementById('amount');
        const methodSelect = document.getElementById('method');
        const feeBreakdown = document.getElementById('feeBreakdown');

        // Geting display elements
        const baseAmountSpan = document.getElementById('baseAmount');
        const serviceFeeSpan = document.getElementById('serviceFee');
        const totalAmountSpan = document.getElementById('totalAmount');
        const feePercentageSpan = document.getElementById('feePercentage');

        // Calculate fee based on amount and payment method
        function calculateFee(baseAmount, paymentMethod) {
            if (!baseAmount || baseAmount <= 0 || !paymentMethod) {
                return 0;
            }

            const feePercentage = FEE_PERCENTAGES[paymentMethod] || 0;
            // Converting percentage to decimal
            let fee = baseAmount * (feePercentage / 100);

            // Rounding to 2 decimal places
            fee = Math.round(fee * 100) / 100;

            return fee;
        }

        function updateFeeBreakdown() {
            const amount = parseFloat(amountInput.value) || 0;
            const method = methodSelect.value;

            if (amount > 0 && method) {
                const fee = calculateFee(amount, method);
                const total = amount + fee;

                const feePercentage = FEE_PERCENTAGES[method] || 0;
                const feePercentageText = feePercentage + '%';

                baseAmountSpan.textContent = amount.toFixed(2);
                serviceFeeSpan.textContent = fee.toFixed(2);
                totalAmountSpan.textContent = total.toFixed(2);
                feePercentageSpan.textContent = feePercentageText;

                feeBreakdown.style.display = 'block';
            } else {
                feeBreakdown.style.display = 'none';
            }
        }

        amountInput.addEventListener('input', updateFeeBreakdown);
        amountInput.addEventListener('change', updateFeeBreakdown);
        methodSelect.addEventListener('change', updateFeeBreakdown);
        updateFeeBreakdown();
    </script>
</body>
</html>
