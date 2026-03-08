package bus;

import db.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class CancelTicket {

    public CancelTicket(String username){

        JFrame f = new JFrame("Cancel Ticket");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel bg = new JPanel(){
            Image img = new ImageIcon("Images/Bg.jpeg").getImage();
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(img,0,0,getWidth(),getHeight(),this);
            }
        };

        bg.setLayout(new GridBagLayout());
        f.setContentPane(bg);

        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(600,350));
        panel.setBackground(new Color(255,255,255,220));

        Font heading = new Font("Times New Roman",Font.BOLD,24);
        Font text = new Font("Times New Roman",Font.PLAIN,18);

        JLabel title = new JLabel("Cancel Your Booking");
        title.setFont(heading);
        title.setBounds(170,30,300,30);

        JComboBox<String> combo = new JComboBox<>();
        combo.setFont(text);
        combo.setBounds(100,100,400,35);

        JButton cancelBtn = new JButton("Cancel Ticket");
        cancelBtn.setBackground(new Color(178,34,34));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBounds(220,170,150,40);

        panel.add(title);
        panel.add(combo);
        panel.add(cancelBtn);
        bg.add(panel);

        // ===== LOAD USER BOOKINGS =====
        try{
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "SELECT booking_id,bus_id,passenger_name,seat_number FROM booking WHERE username=?"
            );

            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                combo.addItem(
                    rs.getInt("booking_id") + " | Bus " +
                    rs.getInt("bus_id") + " | " +
                    rs.getString("passenger_name") + " | Seat " +
                    rs.getInt("seat_number")
                );
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }

        // ===== CANCEL LOGIC =====
        cancelBtn.addActionListener(e->{
            try{

                if(combo.getSelectedItem()==null){
                    JOptionPane.showMessageDialog(f,"No Booking Selected");
                    return;
                }

                String data = combo.getSelectedItem().toString();
                int bookingId = Integer.parseInt(data.split("\\|")[0].trim());

                Connection con = DBConnection.getConnection();

                // ===== GET PAYMENT METHOD & PRICE =====
                PreparedStatement getDetails = con.prepareStatement(
                    "SELECT b.payment_method, bus.price " +
                    "FROM booking b JOIN bus ON b.bus_id = bus.bus_id " +
                    "WHERE b.booking_id=?"
                );

                getDetails.setInt(1,bookingId);
                ResultSet rs = getDetails.executeQuery();

                String paymentMethod = "Online";
                double price = 0;

                if(rs.next()){
                    paymentMethod = rs.getString("payment_method");
                    price = rs.getDouble("price");
                }

                if(paymentMethod == null){
                    paymentMethod = "Online";
                }

                // ===== 10% Cancellation Charge =====
                double cancellationCharge = price * 0.10;
                double refundAmount = price - cancellationCharge;

                // ===== DELETE BOOKING =====
                PreparedStatement del = con.prepareStatement(
                    "DELETE FROM booking WHERE booking_id=?"
                );
                del.setInt(1,bookingId);
                del.executeUpdate();

                // ===== PROCESS REFUND =====
                processRefund(username, paymentMethod, refundAmount, cancellationCharge);

                combo.removeItem(data);

            }catch(Exception ex){
                ex.printStackTrace();
            }
        });

        f.setVisible(true);
    }

    // ================= REFUND WINDOW =================
    private void processRefund(String username, String paymentMethod,
                               double refundAmount, double charge){

        JDialog refundDialog = new JDialog((Frame)null,"Refund Processing",true);
        refundDialog.setSize(450,350);
        refundDialog.setLocationRelativeTo(null);
        refundDialog.setLayout(null);

        JLabel title = new JLabel("Refund Details");
        title.setFont(new Font("Times New Roman",Font.BOLD,22));
        title.setBounds(140,30,200,30);

        JLabel method = new JLabel("Refund To: " + username + " (" + paymentMethod + ")");
        method.setFont(new Font("Times New Roman",Font.PLAIN,18));
        method.setBounds(80,80,300,30);

        JLabel chargeLbl = new JLabel("Cancellation Charge (10%): ₹ " + charge);
        chargeLbl.setBounds(80,120,300,30);

        JLabel refundLbl = new JLabel("Refund Amount: ₹ " + refundAmount);
        refundLbl.setFont(new Font("Times New Roman",Font.BOLD,18));
        refundLbl.setBounds(110,160,300,30);

        JButton confirm = new JButton("Confirm Refund");
        confirm.setBounds(150,220,150,40);
        confirm.setBackground(new Color(34,139,34));
        confirm.setForeground(Color.WHITE);

        confirm.addActionListener(e->{
            JOptionPane.showMessageDialog(refundDialog,
                    "Refund Successful ✅\nAmount will reflect in 2-3 working days.");
            refundDialog.dispose();
        });

        refundDialog.add(title);
        refundDialog.add(method);
        refundDialog.add(chargeLbl);
        refundDialog.add(refundLbl);
        refundDialog.add(confirm);

        refundDialog.setVisible(true);
    }
}