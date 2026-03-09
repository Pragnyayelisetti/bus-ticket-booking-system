🚌 Bus Ticket Booking System (Java)

A Java-based Bus Ticket Booking System that allows users to search buses, book tickets, and manage bookings through a simple GUI. The system also includes an Admin module to manage buses and monitor the booking process.

This project is built using Java Swing for the UI and MySQL for database storage.

📌 Features 👤 User Module

User Registration and Login

Search available buses

View bus details

Select journey date (up to 7 days in advance)

Book bus tickets

Online payment simulation

View booking confirmation with seat numbers

🛠 Admin Module

Admin Signup and Login

Add new buses

View bus list

Delete buses from the system

Manage bus information

💾 Database Tables

The system uses a MySQL database with the following tables:

Users Table

Stores user login details.

Column Description id User ID username User name password Password Admin Table

Stores admin login credentials.

Column Description id Admin ID username Admin username password Admin password Bus Table

Stores bus details.

Column Description bus_id Bus ID source Source location destination Destination location departure_time Departure time journey_hours Travel duration total_seats Total number of seats price Ticket price Booking Table

Stores ticket booking information.

Column Description booking_id Booking ID username User who booked bus_id Bus ID passenger_name Passenger name seat_number Seat number booking_date Booking date journey_date Travel date payment_method Payment type ⚙️ Technologies Used

Java

Java Swing (GUI)

JDBC

▶️ How to Run the Project

Clone the repository https://github.com/Pragnyayelisetti/bus-ticket-booking-system

Import the project into VS Code / any Java IDE

Create the MySQL database

CREATE DATABASE tickets;

Create required tables (users, admin, bus, booking)

Update your database credentials in:

DBConnection.java

Run the project starting from the Login class.

MySQL

VS Code

🎓 Academic Purpose

This project was developed as part of a Java Mini Project to demonstrate:

GUI development

Database connectivity

CRUD operations

Booking system logic

👩‍💻 Author

Pragnya

Java Project – Bus Ticket Booking System
