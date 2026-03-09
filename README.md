## **Bus Ticket Booking System (Java)**

**Project Description**

A Java-based Bus Ticket Booking System that allows users to search buses, book tickets, and manage bookings through a graphical user interface.  
The system also includes an Admin module to manage buses and monitor the booking process.

This project is developed using Java Swing for the user interface and MySQL for database storage.

--------------------------------------------------

**Features**

**User Module**
- User registration and login
- Search available buses
- View bus details
- Select journey date (up to 7 days in advance)
- Book bus tickets
- Online payment simulation
- View booking confirmation with seat numbers

**Admin Module**
- Admin signup and login
- Add new buses
- View bus list
- Delete buses from the system
- Manage bus information

--------------------------------------------------

**Database Tables**

The system uses a MySQL database with the following tables:

**Users Table**  
Stores user login details.  
Columns:  
id – User ID  
username – User name  
password – Password  

**Admin Table**  
Stores admin login credentials.  
Columns:  
id – Admin ID  
username – Admin username  
password – Admin password  

**Bus Table**  
Stores bus details.  
Columns:  
bus_id – Bus ID  
source – Source location  
destination – Destination location  
departure_time – Departure time  
journey_hours – Travel duration  
total_seats – Total number of seats  
price – Ticket price  

**Booking Table**  
Stores ticket booking information.  
Columns:  
booking_id – Booking ID  
username – User who booked  
bus_id – Bus ID  
passenger_name – Passenger name  
seat_number – Seat number  
booking_date – Booking date  
journey_date – Travel date  
payment_method – Payment type  

--------------------------------------------------

**Technologies Used**
- Java
- Java Swing (GUI)
- JDBC
- MySQL
- VS Code

--------------------------------------------------

**How to Run the Project**

1. Clone the repository  
   https://github.com/Pragnyayelisetti/bus-ticket-booking-system  

2. Import the project into VS Code or any Java IDE  

3. Create the MySQL database  

   CREATE DATABASE tickets;

4. Create required tables (users, admin, bus, booking)

5. Update database credentials in  
   DBConnection.java

6. Run the project starting from the Login class.

--------------------------------------------------

**Academic Purpose**

This project was developed as part of a Java Mini Project to demonstrate:
- GUI development
- Database connectivity using JDBC
- CRUD operations
- Booking system logic
- Object-Oriented Programming concepts

--------------------------------------------------

**Author**

Pragnya  
Java Project – Bus Ticket Booking System
