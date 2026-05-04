# AI_Expense_Tracker

A complete full-stack AI-powered Expense Tracker Web Application with a vanilla HTML/CSS/JavaScript frontend and a Java Spring Boot + MySQL backend.

## Folder Structure

```text
AI_Expense_Tracker/
|-- frontend/
|   |-- login.html
|   |-- register.html
|   |-- dashboard.html
|   |-- expenses.html
|   |-- reports.html
|   |-- style.css
|   |-- script.js
|
|-- backend/
|   |-- pom.xml
|   |-- src/
|       |-- main/
|       |   |-- java/com/expensetracker/
|       |   |   |-- AIExpenseTrackerApplication.java
|       |   |   |-- ai/ExpenseAnalyzer.java
|       |   |   |-- config/CorsConfig.java
|       |   |   |-- config/DataInitializer.java
|       |   |   |-- controller/AuthController.java
|       |   |   |-- controller/ExpenseController.java
|       |   |   |-- controller/ReportController.java
|       |   |   |-- controller/AIController.java
|       |   |   |-- dto/*.java
|       |   |   |-- exception/*.java
|       |   |   |-- model/*.java
|       |   |   |-- repository/*.java
|       |   |   |-- service/*.java
|       |   |-- resources/
|       |       |-- application.properties
|       |       |-- schema.sql
|       |       |-- data.sql
|       |-- test/java/com/expensetracker/AIExpenseTrackerApplicationTests.java
|
|-- README.md
|-- .gitignore
```

## Requirements

- Java 17 or later
- Maven 3.9+
- MySQL 8+
- A static web server for the frontend, such as VS Code Live Server or Python `http.server`

## MySQL Setup

1. Start MySQL.
2. Create the database:

```sql
CREATE DATABASE IF NOT EXISTS expense_tracker;
```

3. Update credentials in `backend/src/main/resources/application.properties` if your MySQL username or password is different:

```properties
spring.datasource.username=root
spring.datasource.password=root
```

4. Optional sample data:

```bash
mysql -u root -p expense_tracker < backend/src/main/resources/schema.sql
mysql -u root -p expense_tracker < backend/src/main/resources/data.sql
```

Demo login after importing sample data:

```text
Email: demo@example.com
Password: password123
```

## Run Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The REST API runs at:

```text
http://localhost:8080
```

Main endpoints:

```text
POST   /register
POST   /login
POST   /logout
GET    /me
GET    /dashboard
GET    /categories
GET    /expenses
POST   /expenses
PUT    /expenses/{id}
DELETE /expenses/{id}
GET    /reports/daily
GET    /reports/weekly
GET    /reports/monthly
GET    /ai/analyze
```

## Run Frontend

From the project root, run:

```bash
cd frontend
python -m http.server 5500
```

Open:

```text
http://localhost:5500/login.html
```

The frontend expects the backend at `http://localhost:8080`. Change `API_BASE` in `frontend/script.js` if needed.

## AI Logic

The backend AI logic lives in `backend/src/main/java/com/expensetracker/ai/ExpenseAnalyzer.java` and includes:

- `detectOverspending()`
- `predictMonthlyExpense()`
- `suggestSavings()`
- `analyzeCategoryTrends()`
- unusual expense detection
- behavior analysis

It uses monthly pace prediction, historical weighted averages, category trend percentages, spend-to-income ratios, and basic statistical outlier detection.
