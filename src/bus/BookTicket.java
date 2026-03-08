package bus;

import db.DBConnection;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class BookTicket {

    public BookTicket(String username, String busId, String passenger, int ticketCount){

        Connection con = null;

        try{

            if(passenger == null || passenger.trim().isEmpty()){
                JOptionPane.showMessageDialog(null,"Passenger name required");
                return;
            }

            if(ticketCount <= 0){
                JOptionPane.showMessageDialog(null,"Invalid ticket count");
                return;
            }

            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // ===== DATE SELECTION =====
            String[] dates = new String[7];
            LocalDate today = LocalDate.now();

            for(int i=0;i<7;i++){
                dates[i] = today.plusDays(i).toString();
            }

            String selectedDateStr = (String) JOptionPane.showInputDialog(
                    null,
                    "Select Journey Date:",
                    "Select Date",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    dates,
                    dates[0]
            );

            if(selectedDateStr == null) return;

            Date journeyDate = Date.valueOf(selectedDateStr);

            // ===== GET BUS DETAILS =====
            PreparedStatement busStmt = con.prepareStatement(
                    "SELECT total_seats,departure_time,journey_hours,price FROM bus WHERE bus_id=?"
            );

            busStmt.setInt(1,Integer.parseInt(busId));

            ResultSet rs = busStmt.executeQuery();

            int totalSeats = 0;
            Time departureTime = null;
            int hours = 0;
            double price = 0;

            if(rs.next()){
                totalSeats = rs.getInt("total_seats");
                departureTime = rs.getTime("departure_time");
                hours = rs.getInt("journey_hours");
                price = rs.getDouble("price");
            }

            // ===== DATE WISE BOOKING CHECK =====
            PreparedStatement checkStmt = con.prepareStatement(
                    "SELECT COUNT(*) FROM booking WHERE bus_id=? AND journey_date=?"
            );

            checkStmt.setInt(1,Integer.parseInt(busId));
            checkStmt.setDate(2,journeyDate);

            ResultSet rs2 = checkStmt.executeQuery();
            int bookedSeats = 0;

            if(rs2.next()){
                bookedSeats = rs2.getInt(1);
            }

            int availableSeats = totalSeats - bookedSeats;

            if(ticketCount > availableSeats){

                JOptionPane.showMessageDialog(null,
                        "Seats are not available for " + selectedDateStr +
                        "\nAvailable seats: " + availableSeats);

                return;
            }

            if(availableSeats <= 0){

                JOptionPane.showMessageDialog(null,
                        "Bus is fully booked for " + selectedDateStr);

                return;
            }

            // ===== PAYMENT OPTION =====
            String options[] = {"UPI","Credit Card","Debit Card"};

            String paymentMethod = (String) JOptionPane.showInputDialog(
                    null,
                    "Select Payment Method",
                    "Payment",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if(paymentMethod == null) return;

            double totalAmount = price * ticketCount;

            boolean paymentSuccess = processPayment(paymentMethod,totalAmount);

            if(!paymentSuccess){
                JOptionPane.showMessageDialog(null,"Payment Failed");
                return;
            }

            // ===== BOOK SEATS =====
            // ===== BOOK SEATS =====
List<Integer> seatList = new ArrayList<>();

PreparedStatement seatStmt = con.prepareStatement(
"SELECT seat_number FROM booking WHERE bus_id=? AND journey_date=?"
);

seatStmt.setInt(1, Integer.parseInt(busId));
seatStmt.setDate(2, journeyDate);

ResultSet seatRs = seatStmt.executeQuery();

List<Integer> bookedSeatNumbers = new ArrayList<>();

while(seatRs.next()){
    bookedSeatNumbers.add(seatRs.getInt("seat_number"));
}

int seat = 1;

while(seatList.size() < ticketCount){

    if(!bookedSeatNumbers.contains(seat)){

        seatList.add(seat);

        PreparedStatement insert = con.prepareStatement(
        "INSERT INTO booking(username,bus_id,passenger_name,seat_number,journey_date,payment_method) VALUES(?,?,?,?,?,?)"
        );

        insert.setString(1, username);
        insert.setInt(2, Integer.parseInt(busId));
        insert.setString(3, passenger);
        insert.setInt(4, seat);
        insert.setDate(5, journeyDate);
        insert.setString(6, paymentMethod);

        insert.executeUpdate();
    }

    seat++;
}

            con.commit();

            // ===== ARRIVAL TIME =====
            Time arrivalTime = null;

            if(departureTime != null){

                long depMillis = departureTime.getTime();
                long arrMillis = depMillis + (hours*60*60*1000);

                arrivalTime = new Time(arrMillis);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

            String depStr = (departureTime!=null)?sdf.format(departureTime):"Not Set";
            String arrStr = (arrivalTime!=null)?sdf.format(arrivalTime):"Not Set";

            // ===== CONFIRMATION WINDOW =====
            JFrame confirm = new JFrame("Booking Confirmation");

            confirm.setSize(520,520);
            confirm.setLocationRelativeTo(null);

            JPanel panel = new JPanel(null);
            panel.setBackground(Color.WHITE);

            JLabel title = new JLabel("Booking Confirmation");

            title.setFont(new Font("Times New Roman",Font.BOLD,24));
            title.setBounds(140,20,300,30);

            JLabel l1 = new JLabel("Bus ID : "+busId);
            JLabel l2 = new JLabel("Passenger : "+passenger);
            JLabel l3 = new JLabel("Seats : "+seatList);
            JLabel l4 = new JLabel("Journey Date : "+selectedDateStr);
            JLabel l5 = new JLabel("Departure : "+depStr);
            JLabel l6 = new JLabel("Arrival : "+arrStr);
            JLabel l7 = new JLabel("Payment : "+paymentMethod);
            JLabel l8 = new JLabel("Total Amount : ₹ "+totalAmount);

            JLabel labels[] = {l1,l2,l3,l4,l5,l6,l7,l8};

            int y = 80;

            for(JLabel lbl : labels){
                lbl.setFont(new Font("Times New Roman",Font.PLAIN,18));
                lbl.setBounds(90,y,350,30);
                panel.add(lbl);
                y += 40;
            }

            JButton close = new JButton("Close");

            close.setBounds(200,y+20,120,40);
            close.setBackground(new Color(30,144,255));
            close.setForeground(Color.WHITE);

            close.addActionListener(e -> confirm.dispose());

            panel.add(title);
            panel.add(close);

            confirm.add(panel);
            confirm.setVisible(true);

        }
        catch(Exception e){

            try{
                if(con!=null) con.rollback();
            }catch(Exception ex){}

            e.printStackTrace();

            JOptionPane.showMessageDialog(null,"Booking Error : "+e.getMessage());
        }
    }

    // ===== PAYMENT WINDOW =====

    private boolean processPayment(String method,double amount){

        JDialog payDialog = new JDialog((Frame)null,"Online Payment - "+method,true);

        payDialog.setSize(400,380);
        payDialog.setLocationRelativeTo(null);
        payDialog.setLayout(null);

        JLabel title = new JLabel(method+" Payment");

        title.setFont(new Font("Times New Roman",Font.BOLD,22));
        title.setBounds(120,20,200,30);

        JLabel amountLabel = new JLabel("Amount : ₹ "+amount);

        amountLabel.setFont(new Font("Times New Roman",Font.PLAIN,18));
        amountLabel.setBounds(120,60,200,30);

        payDialog.add(title);
        payDialog.add(amountLabel);

        JTextField field1 = new JTextField();

        JButton payBtn = new JButton("Pay Now");

        payBtn.setBounds(140,200,120,40);
        payBtn.setBackground(new Color(30,144,255));
        payBtn.setForeground(Color.WHITE);

        JLabel label = new JLabel(method.equals("UPI")?"UPI ID":"Card Number");

        label.setBounds(60,120,100,30);
        field1.setBounds(160,120,160,30);

        payDialog.add(label);
        payDialog.add(field1);

        final boolean success[] = {false};

        payBtn.addActionListener(e -> {

            if(field1.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(payDialog,"Enter payment details");
                return;
            }

            JOptionPane.showMessageDialog(payDialog,"Payment Successful");

            success[0] = true;

            payDialog.dispose();
        });

        payDialog.add(payBtn);

        payDialog.setVisible(true);

        return success[0];
    }
}