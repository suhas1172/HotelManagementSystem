# 🏨 Smart Hotel Management System

A robust, console-based Java application designed for efficient hotel operations. This system manages room inventory across three categories (Standard, Deluxe, Suite) and features an automated billing engine with MySQL persistence.

## 🚀 Key Features

* **Smart Auto Check-In:** Automatically assigns the lowest available room number based on user-selected category.
* **Grouped Status View:** Clean dashboard that displays available rooms in ranges (e.g., `Rooms 102-110: Available`) for better readability.
* **Business Logic Enforcement:** Strictly enforces a 10-room limit per category and validates stay duration.
* **ANSI Color Coding:** Professional console feedback using **Green** for success/availability and **Red** for occupied rooms/errors.
* **Real-Time Analytics:** Instantly calculates total hotel revenue and tracks active occupancy counts.
* **Data Integrity:** Automatic name formatting (Title Case) and robust error handling for invalid user inputs.

---

## 🛠️ Technical Stack

* **Language:** Java (JDK 17+)
* **Database:** MySQL 8.0+
* **Driver:** JDBC (Connector/J)
* **Pattern:** Layered Architecture (DTO -> DAO -> Service -> UI)

---

## 📂 Project Structure

| File | Responsibility |
| :--- | :--- |
| **Main.java** | Presentation Layer: Handles user interaction and the main menu loop. |
| **Service.java** | Business Logic Layer: Processes room grouping, bill calculation, and validation. |
| **DAO.java** | Interface: Defines the contract for database interactions. |
| **DAOIMPL.java** | Data Access Layer: Executes JDBC queries and updates the MySQL database. |
| **DTO.java** | Data Transfer Object: A simple object representing a reservation record. |



---

## ⚙️ Setup Instructions

### 1. Database Initialization
Run the following SQL script in your MySQL Workbench to create the schema and populate the room inventory:

```sql
CREATE DATABASE hotel_db;
USE hotel_db;

CREATE TABLE rooms (
    room_no INT PRIMARY KEY,
    room_type VARCHAR(20),
    price DOUBLE,
    status VARCHAR(20) DEFAULT 'Available'
);

CREATE TABLE reservations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    guest_name VARCHAR(50),
    room_no INT,
    days INT,
    bill DOUBLE,
    FOREIGN KEY (room_no) REFERENCES rooms(room_no)
);

### 2.Configure Credentials
Open DAOIMPL.java and update your database password:
private final String PASS = "Your_MySQL_Password_Here";

Compile all files and run Main.java.

-- Insert 10 Rooms per category
-- Standard (101-110), Deluxe (201-210), Suite (301-310)
-- (See full SQL commands provided in project documentation)
