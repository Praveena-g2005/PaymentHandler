#### Payment Handler API
A Java EE web application for managing users, processing payments, and handling transactions with multiple payment methods (Card, UPI, Wallet).
It demonstrates enterprise Java patterns including CDI, Servlets, JSP, JOOQ, and comprehensive design patterns for the AeroParker learning requirements.

---

## 📚 Table of Contents

- Architecture
- Endpoints
- Example Payloads
- Tech Stack
- Prerequisites
- Installation & Setup
- Design Patterns
- Notes
- Postman Testing

---

# 🏗️ Architecture

The project follows a clean layered architecture:

**Controllers (Servlets)** → Handle HTTP requests & responses

**Services** → Contain business logic (users, payments, balances)

**Repositories (DAOs)** → Handle database interactions with JOOQ

**Models** → Represent data entities

**Payment Handlers** → Strategy pattern for different payment methods

**Persistence Layer** → Database connection management with try-with-resources

**Web Layer** → JSP views and session management


## Database Schema

```
users
├── id (PK, AUTO_INCREMENT)
├── username
├── email (UNIQUE)
└── created_at

balances
├── user_id (PK, FK → users.id)
├── amount
└── updated_at

transactions
├── id (PK, AUTO_INCREMENT)
├── payer_id (FK → users.id)
├── payee_id (FK → users.id)
├── amount
├── payment_method
└── created_at
```

---

## 📦 Endpoints

## 👥 User Management APIs

| Method | Endpoint         | Description                    |
| ------ | ---------------- | ------------------------------ |
| GET    | `/users`         | Get all users (JSP view)       |
| GET    | `/users/:id`     | Get user details by ID         |
| GET    | `/users/create`  | Show user creation form        |
| POST   | `/users`         | Create a new user              |
| POST   | `/users`         | Update user (action=update)    |
| POST   | `/users`         | Delete user (action=delete)    |
| POST   | `/users`         | Login as user (action=login)   |

## 💰 Payment APIs

| Method | Endpoint              | Description                          |
| ------ | --------------------- | ------------------------------------ |
| GET    | `/payment/process`    | Show payment form                    |
| POST   | `/payment/process`    | Process payment transaction          |

---

## 🧪 Example Payloads

## User Management

### ➕ Create User
```
POST /users
Content-Type: application/x-www-form-urlencoded

action=create
name=John Doe
email=john@example.com
```

### ✏️ Update User
```
POST /users
Content-Type: application/x-www-form-urlencoded

action=update
userId=1
name=Updated Name
```

### 🗑️ Delete User
```
POST /users
Content-Type: application/x-www-form-urlencoded

action=delete
userId=1
```

### 🔑 Login as User
```
POST /users
Content-Type: application/x-www-form-urlencoded

action=login
userId=1
```

## Payment Processing

### 💳 Process Card Payment
```
POST /payment/process
Content-Type: application/x-www-form-urlencoded

payerId=1
payeeId=2
amount=100.50
method=card
```

### 💵 Process UPI Payment
```
POST /payment/process
Content-Type: application/x-www-form-urlencoded

payerId=1
payeeId=2
amount=50.00
method=upi
```

### 💰 Process Wallet Payment
```
POST /payment/process
Content-Type: application/x-www-form-urlencoded

payerId=1
payeeId=2
amount=200.00
method=wallet
```

---

## 🛠️ Tech Stack

## Backend

**Language:** Java 8+

**Framework:** Java EE / Jakarta EE

**CDI Implementation:** Weld 3.1.9

**Build Tool:** Maven 3.6+

## Database

**Database:** H2 (In-memory)

**ORM:** JOOQ 3.14.16

**Connection Management:** Try-with-resources pattern

## Web Layer

**Servlets:** Java Servlet API 4.0

**View Technology:** JSP 2.3 + JSTL 1.2

**Server:** Jetty 9.4

## Testing

**Testing Framework:** JUnit 4.13.2

**Mocking:** Mockito 5.11.0

**Test Pattern:** Arrange-Act-Assert

## Design Patterns

**Strategy Pattern:** Payment handlers

**Builder Pattern:** Payment object construction

**DAO Pattern:** Data access abstraction

**Factory Pattern:** CDI @Produces

**Dependency Injection:** CDI @Inject

---

## ⚙️ Prerequisites

- Java JDK 8+ (Recommended: JDK 11)
- Maven 3.6+
- Git

---

## 🚀 Installation & Setup

**1. Clone the repository:**
```bash
git clone <repository-url>
cd paymenthandler
```

**2. Configure the database:**
Database auto-initializes with H2 in-memory. Schema location:
```
src/main/resources/schema.sql
```

**3. Build the project:**
```bash
# Compile
mvn clean compile

# Package WAR file
mvn clean package -DskipTests
```

**4. Run the project:**
```bash
# Run with Jetty
mvn jetty:run

# Access application
http://localhost:8080/paymenthandler/
```

**5. Default Test Users:**
```
Sample users are auto-created:
- John Doe (john@example.com)
- Jane Smith (jane@example.com)
- Bob Wilson (bob@example.com)

Initial balances:
- User 1: $1000.00
- User 2: $500.00
- User 3: $750.00
```

---

## 🎨 Design Patterns

### 1. Strategy Pattern
**Location:** `com.paymenthandler.payment`

Different payment methods as strategies:
- `CardPaymentHandler` - Processes card payments
- `UpiPaymentHandler` - Processes UPI payments
- `WalletPaymentHandler` - Processes wallet transfers

```java
// Dynamic selection at runtime
for (PaymentHandler h : handlers) {
    if (h.getMethod().equals(request.getMethod())) {
        selected = h;
    }
}
```

### 2. Builder Pattern
**Location:** `Payment.java`

Fluent API for object construction:
```java
Payment payment = Payment.builder()
    .payerUserId(1L)
    .amount(100.50)
    .method("card")
    .build();
```

### 3. DAO Pattern
**Location:** `com.paymenthandler.dao`

Abstracts data access:
- Interface: `UserDao`
- JOOQ Implementation: `JooqUserDao`
- In-Memory Implementation: `InMemoryBalanceDao`

### 4. Factory Pattern (CDI)
**Location:** `DatabaseConnectionFactory.java`

```java
@Produces
public DataSource getDataSource() {
    return dataSource;
}
```

### 5. Dependency Injection
**Location:** All services

```java
@ApplicationScoped
public class UserService {
    @Inject
    @Named("jooqUserDao")
    private UserDao dao;
}
```

---

## 🚧 Notes

✅ All user data stored in H2 database

✅ JOOQ provides type-safe SQL queries

✅ Connection management with try-with-resources

✅ CDI handles dependency injection

✅ Session tracking with @SessionScoped beans

✅ Multiple payment methods supported

✅ Transaction records maintained

✅ Wallet balance validation implemented

⚠️ H2 is in-memory - data resets on restart

⚠️ For production, switch to PostgreSQL/MySQL

---

## 🧪 Postman Testing

### Step-by-Step Testing Guide:

**1. Start the application**
```bash
mvn jetty:run
```

**2. GET `/users`**
→ View all users (returns JSP page)
```
GET http://localhost:8080/paymenthandler/users
```

**3. GET `/users/1`**
→ View specific user details
```
GET http://localhost:8080/paymenthandler/users/1
```

**4. POST `/users` (Create)**
→ Create a new user
```
POST http://localhost:8080/paymenthandler/users
Body (x-www-form-urlencoded):
  action: create
  name: Test User
  email: test@example.com
```

**5. POST `/users` (Update)**
→ Update existing user
```
POST http://localhost:8080/paymenthandler/users
Body (x-www-form-urlencoded):
  action: update
  userId: 1
  name: Updated Name
```

**6. POST `/users` (Login)**
→ Set user in session
```
POST http://localhost:8080/paymenthandler/users
Body (x-www-form-urlencoded):
  action: login
  userId: 1
```

**7. GET `/payment/process`**
→ View payment form
```
GET http://localhost:8080/paymenthandler/payment/process
```

**8. POST `/payment/process` (Card)**
→ Process card payment
```
POST http://localhost:8080/paymenthandler/payment/process
Body (x-www-form-urlencoded):
  payerId: 1
  payeeId: 2
  amount: 100.50
  method: card
```

**9. POST `/payment/process` (UPI)**
→ Process UPI payment
```
POST http://localhost:8080/paymenthandler/payment/process
Body (x-www-form-urlencoded):
  payerId: 1
  payeeId: 2
  amount: 50.00
  method: upi
```

**10. POST `/payment/process` (Wallet)**
→ Process wallet payment (validates balance)
```
POST http://localhost:8080/paymenthandler/payment/process
Body (x-www-form-urlencoded):
  payerId: 1
  payeeId: 2
  amount: 200.00
  method: wallet
```

**11. POST `/users` (Delete)**
→ Delete user
```
POST http://localhost:8080/paymenthandler/users
Body (x-www-form-urlencoded):
  action: delete
  userId: 3
```

**12. GET `/` (Homepage)**
→ View application homepage with session info
```
GET http://localhost:8080/paymenthandler/
```

---

## 📋 Learning Checklist Coverage

### ✅ Java 8+ Features
- Lambda Expressions (`UserService.java:36`)
- Stream API (`.filter()`, `.map()`, `.collect()`)
- Optional (`.map()`, `.flatMap()`, `.ifPresent()`)
- Method References (`User::getName`, `dao::updateUser`)

### ✅ Testing
- JUnit 4 (`@Test`, `@Before`, `@After`)
- Mockito (`mock()`, `when().thenReturn()`, `verify()`)
- Arrange-Act-Assert pattern

### ✅ CDI (Dependency Injection)
- `@Inject`, `@Named` annotations
- `@ApplicationScoped` for services
- `@SessionScoped` for session beans
- Factory pattern with `@Produces`

### ✅ Database
- JOOQ for type-safe queries
- DAO pattern for abstraction
- Try-with-resources for connection management

### ✅ Design Patterns
- Strategy Pattern (payment handlers)
- Builder Pattern (Payment object)
- Factory Pattern (CDI producers)

### ✅ Web Development
- Servlets (`@WebServlet`, `doGet()`, `doPost()`)
- JSP with JSTL tags

---
