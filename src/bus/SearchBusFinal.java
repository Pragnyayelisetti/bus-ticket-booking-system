package bus;

import db.DBConnection;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.*;

public class SearchBusFinal {

    public SearchBusFinal(String username){

        JFrame f = new JFrame("Search Bus");
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
        panel.setPreferredSize(new Dimension(1100,600));
        panel.setBackground(new Color(255,255,255,220));

        Font heading = new Font("Times New Roman",Font.BOLD,26);
        Font text = new Font("Times New Roman",Font.PLAIN,16);

        JLabel title = new JLabel("Search Available Buses",SwingConstants.CENTER);
        title.setFont(heading);

        JPanel top = new JPanel();

        JTextField sourceField = new JTextField(10);
        JTextField destField = new JTextField(10);

        JButton searchBtn = new JButton("Search");

        top.add(new JLabel("Source:"));
        top.add(sourceField);
        top.add(new JLabel("Destination:"));
        top.add(destField);
        top.add(searchBtn);

        String cols[]={
                "Bus ID","Bus Name","Source","Destination",
                "Departure","Arrival","Seats Available","Fare"
        };

        DefaultTableModel model = new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r,int c){
                return false;
            }
        };

        JTable table = new JTable(model);

        JScrollPane sp = new JScrollPane(table);

        JButton bookBtn = new JButton("Book Selected Bus");

        JPanel bottom = new JPanel();
        bottom.add(bookBtn);

        panel.add(title,BorderLayout.NORTH);
        panel.add(top,BorderLayout.BEFORE_FIRST_LINE);
        panel.add(sp,BorderLayout.CENTER);
        panel.add(bottom,BorderLayout.SOUTH);

        bg.add(panel);

        // ===== SEARCH =====

        searchBtn.addActionListener(e -> {

            model.setRowCount(0);

            try{

                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM bus WHERE LOWER(TRIM(source))=? AND LOWER(TRIM(destination))=?"
                );

                ps.setString(1,sourceField.getText().trim().toLowerCase());
                ps.setString(2,destField.getText().trim().toLowerCase());

                ResultSet rs = ps.executeQuery();

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

                while(rs.next()){

                    int total = rs.getInt("total_seats");
                    int booked = rs.getInt("booked_seats");

                    int available = total - booked;

                    Time dep = rs.getTime("departure_time");
                    int hours = rs.getInt("journey_hours");

                    String depStr="Not Set";
                    String arrStr="Not Set";

                    if(dep!=null){

                        long arrMillis = dep.getTime() + (hours*60*60*1000);

                        Time arr = new Time(arrMillis);

                        depStr = sdf.format(dep);
                        arrStr = sdf.format(arr);
                    }

                    model.addRow(new Object[]{
                            rs.getInt("bus_id"),
                            rs.getString("bus_name"),
                            rs.getString("source"),
                            rs.getString("destination"),
                            depStr,
                            arrStr,
                            available,
                            rs.getDouble("price")
                    });
                }

            }catch(Exception ex){
                ex.printStackTrace();
            }

        });

        // ===== BOOK =====

        bookBtn.addActionListener(e -> {

            int row = table.getSelectedRow();

            if(row==-1){
                JOptionPane.showMessageDialog(f,"Select bus first");
                return;
            }

            String busId = model.getValueAt(row,0).toString();

            String passenger = JOptionPane.showInputDialog("Enter Passenger Name");

            if(passenger==null || passenger.isEmpty()) return;

            String ticketStr = JOptionPane.showInputDialog("Enter Ticket Count");

            int tickets = Integer.parseInt(ticketStr);

            new BookTicket(username,busId,passenger,tickets);
        });

        f.setVisible(true);
    }
}