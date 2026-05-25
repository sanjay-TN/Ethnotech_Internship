# Internship Projects

This repository contains my internship practice work and mini projects, covering core Java, object-oriented programming, JDBC/MySQL, MongoDB, HTML/CSS/JavaScript, servlet basics, and full-stack Spring Boot applications.

The folder works as a learning portfolio: it includes small programming exercises, frontend UI tasks, database assignments, and complete web applications with backend APIs.

## Tech Stack

- Java
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Maven
- MySQL
- MongoDB
- HTML5
- CSS3
- JavaScript
- WebSocket
- JWT Authentication

## Project Highlights

| Project | Description |
|---|---|
| `ConceptClarity` | Full-stack education platform with a local AI-style explanation engine, chat UI, saved history, favorites, and MySQL backend. |
| `Expense_Tracker` | AI-powered expense tracker with authentication, dashboard, expenses, reports, category analysis, and Spring Boot backend. |
| `Hospital_appointment_project` | Hospital appointment management system with admin, doctor, and patient workflows. |
| `Chat-app_project` | Real-time chat application using Spring Boot, WebSocket, JWT authentication, and frontend pages. |
| `coding platform project` | Coding platform with authentication, problems, submissions, test cases, leaderboard, and admin pages. |
| `Simple_Calculator` | Java servlet-based calculator web application. |
| `Student_DB` | Java CRUD-style student database practice project. |
| `Taxi_app` | Java console-based taxi booking application. |
| `Contact_book` | Java contact book program. |
| `Food_Delivery` | Java-based food delivery/customer practice module. |

## Mini Frontend Projects

| Folder | Description |
|---|---|
| `BMI Calculator` | BMI calculator using HTML, CSS, and JavaScript. |
| `Counter_app_task` | Simple counter application. |
| `Gallery_task` | Image gallery frontend task. |
| `LiveClock` | Live digital clock using JavaScript. |
| `feedback form` / `Feedback_form.html` | Feedback form UI. |
| `product` | Product card/page UI practice. |
| `minitask` | Small HTML/CSS frontend task. |

## Java Practice Programs

The repository also includes standalone Java practice files and folders for:

- Classes and objects
- Inheritance
- Basic calculations
- Number programs
- Arrays
- Streams
- Annotations
- Regular expressions
- Decorators
- Bank account/payment examples
- Login validation
- Electricity bill calculation

Examples include:

```text
Prime.java
Rev_num.java
Positive_negative.java
Simple_calculator.java
Account.java
Bank.java
Payment.java
Payments.java
SquareCheck.java
```

## Repository Structure

```text
Internship/
|-- ConceptClarity/
|-- Expense_Tracker/
|-- Hospital_appointment_project/
|-- Chat-app_project/
|-- coding platform project/
|-- BMI Calculator/
|-- Counter_app_task/
|-- Gallery_task/
|-- LiveClock/
|-- Simple_Calculator/
|-- Student_DB/
|-- Taxi_app/
|-- Contact_book/
|-- Food_Delivery/
|-- Mongodb/
|-- mongodb_project/
|-- Mysql/
|-- Arrays/
|-- Streams/
|-- Annotations/
|-- RegEx/
|-- Decorators/
|-- *.java
```

## How to Run

### Run a Standalone Java Program

Open a terminal in the repository root or inside the required folder.

```bash
javac FileName.java
java FileName
```

Example:

```bash
javac Prime.java
java Prime
```

### Run a Frontend Project

Open the project folder and launch the `index.html` file in a browser.

For a local server:

```bash
cd "BMI Calculator"
python -m http.server 3000
```

Then open:

```text
http://localhost:3000
```

### Run a Spring Boot Backend

Go to the backend folder that contains `pom.xml`.

```bash
cd "Expense_Tracker/backend"
mvn spring-boot:run
```

Or, if the project includes Maven Wrapper:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Most Spring Boot APIs run on:

```text
http://localhost:8080
```

### Run a Full-Stack Project

For projects with both `frontend` and `backend` folders:

1. Start MySQL if the backend requires it.
2. Create or update the database mentioned in `application.properties` or `schema.sql`.
3. Start the Spring Boot backend.
4. Open the frontend `index.html`, or serve the frontend with a static server.

Example:

```bash
cd "ConceptClarity/backend"
mvn spring-boot:run
```

In another terminal:

```bash
cd "ConceptClarity/frontend"
python -m http.server 3000
```

## Database Notes

Some projects use MySQL or MongoDB. Check each project's backend resources before running:

```text
src/main/resources/application.properties
schema.sql
data.sql
```

Update database username, password, and database name according to your local setup.

## Suggested GitHub Cleanup

Before pushing to GitHub, it is recommended to avoid committing generated files such as:

```text
target/
bin/
*.class
.vscode/
```

These can be added to a `.gitignore` file.

## Author

Created as part of internship learning and project practice.

