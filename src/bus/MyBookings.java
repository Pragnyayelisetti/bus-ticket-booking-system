package bus;

import db.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class MyBookings {

    public MyBookings(String username){

        JFrame f = new JFrame("My Bookings");
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

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(900,500));
        panel.setBackground(new Color(255,255,255,220));

        Font heading = new Font("Times New Roman",Font.BOLD,24);

        JLabel title = new JLabel("My Bookings - " + username, SwingConstants.CENTER);
        title.setFont(heading);
        title.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));

        String cols[]={"Booking ID","Bus ID","Seat Number","Date"};
        DefaultTableModel model = new DefaultTableModel(cols,0);
        JTable table = new JTable(model);
        table.setRowHeight(28);

        JScrollPane sp = new JScrollPane(table);

        panel.add(title,BorderLayout.NORTH);
        panel.add(sp,BorderLayout.CENTER);

        bg.add(panel);

        // ===== LOAD DATA =====
        try{
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM booking WHERE username=?"
            );

            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while(rs.next()){
                found = true;
                model.addRow(new Object[]{
                    rs.getInt("booking_id"),
                    rs.getInt("bus_id"),
                    rs.getInt("seat_number"),
                    rs.getTimestamp("booking_date")
                });
            }

            if(!found){
                JOptionPane.showMessageDialog(f,"No Bookings Found");
            }

        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(f,"Error: "+e.getMessage());
        }

        f.setVisible(true);
    }
}