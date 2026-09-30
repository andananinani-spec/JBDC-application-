# Student MVC + JDBC + MySQL Application

## Architecture

Frontend (HTML/CSS/JavaScript)
        |
        | HTTP
        v
StudentServlet (Controller)
        |
        v
StudentDAO (Model/Data Access)
        |
        | JDBC
        v
MySQL Database

## Requirements

- JDK 17+
- Maven
- MySQL Server
- Apache Tomcat 11
- A Java IDE such as IntelliJ IDEA, Eclipse, or VS Code

## Setup

1. Run `database.sql` in MySQL.
2. Open `src/main/java/com/studentapp/config/DatabaseConnection.java`.
3. Change `DB_USER` and `DB_PASSWORD` to your MySQL credentials.
4. Build:
   `mvn clean package`
5. Deploy `target/student-mvc-jdbc.war` to Tomcat 11.
6. Start Tomcat.
7. Open:
   `http://localhost:8080/student-mvc-jdbc/`

## Features

- Add student
- View all students
- Update student
- Delete student
- JDBC PreparedStatement
- MVC separation
- JSON API
- HTML/CSS/JavaScript frontend

## API

GET    /api/students
POST   /api/students
PUT    /api/students?id=1
DELETE /api/students?id=1

The frontend calls these endpoints using JavaScript fetch().
