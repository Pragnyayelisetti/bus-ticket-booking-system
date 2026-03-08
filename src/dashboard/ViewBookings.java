package dashboard;

import db.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class ViewBookings {

    public ViewBookings() {

        JFrame f = new JFrame("All Bookings");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ===== Background Panel =====
        JPanel bgPanel = new JPanel(){
            Image bg = new ImageIcon("Images/Bg.jpeg").getImage();

            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(bg,0,0,getWidth(),getHeight(),this);
            }
        };

        bgPanel.setLayout(new GridBagLayout());
        f.setContentPane(bgPanel);

        // ===== Center Card Panel =====
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(1000,550));
        panel.setBackground(new Color(255,255,255,220));

        // ===== Fonts =====
        Font headingFont = new Font("Times New Roman", Font.BOLD, 26);
        Font tableFont = new Font("Times New Roman", Font.PLAIN, 16);
        Font headerFont = new Font("Times New Roman", Font.BOLD, 18);

        // ===== Title =====
        JLabel title = new JLabel("All User Bookings", SwingConstants.CENTER);
        title.setFont(headingFont);
        title.setForeground(new Color(25,25,112));
        title.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));

        // ===== Table Columns =====
        String cols[]={
                "Booking ID","Username","Bus Name","Source",
                "Destination","Passenger","Seat","Date"
        };

        DefaultTableModel model = new DefaultTableModel(cols,0);
        JTable table = new JTable(model);

        // ===== Table Styling =====
        table.setFont(tableFont);
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(30,144,255));
        table.setGridColor(new Color(220,220,220));

        JTableHeader header = table.getTableHeader();
        header.setFont(headerFont);
        header.setBackground(new Color(30,144,255));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10,30,30,30));

        // ===== Fetch Data =====
        try {

            Connection con = DBConnection.getConnection();

            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT b.booking_id,b.username,bu.bus_name,bu.source,bu.destination," +
                            "b.passenger_name,b.seat_number,b.booking_date " +
                            "FROM booking b JOIN bus bu ON b.bus_id=bu.bus_id"
            );

            while(rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getInt(7),
                        rs.getTimestamp(8)
                });
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        bgPanel.add(panel);

        f.setVisible(true);
    }
}