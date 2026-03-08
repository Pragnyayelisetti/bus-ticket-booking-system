package dashboard;

import db.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class AddBus {

    public AddBus(){

        JFrame f = new JFrame("Add Bus");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);

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
        panel.setPreferredSize(new Dimension(500,600));
        panel.setBackground(new Color(255,255,255,220));

        Font h = new Font("Times New Roman",Font.BOLD,24);
        Font l = new Font("Times New Roman",Font.PLAIN,18);
        Font fnt = new Font("Times New Roman",Font.PLAIN,16);
        Font b = new Font("Times New Roman",Font.BOLD,16);

        JLabel title = new JLabel("Add New Bus");
        title.setFont(h);
        title.setBounds(160,20,250,30);

        String labels[]={
                "Bus Name",
                "Source",
                "Destination",
                "Departure Time (HH:MM:SS)",
                "Journey Hours",
                "Total Seats",
                "Price"
        };

        JTextField fields[]=new JTextField[7];

        int y=80;

        for(int i=0;i<labels.length;i++){
            JLabel lab=new JLabel(labels[i]);
            lab.setFont(l);
            lab.setBounds(60,y,220,30);

            fields[i]=new JTextField();
            fields[i].setFont(fnt);
            fields[i].setBounds(280,y,180,30);

            panel.add(lab);
            panel.add(fields[i]);
            y+=60;
        }

        JButton add=new JButton("Add Bus");
        add.setFont(b);
        add.setBackground(new Color(30,144,255));
        add.setForeground(Color.WHITE);
        add.setBounds(180,y+20,150,40);

        panel.add(title);
        panel.add(add);
        bg.add(panel);

        add.addActionListener(e->{
            try{
                Connection con=DBConnection.getConnection();

                PreparedStatement ps=con.prepareStatement(
                        "INSERT INTO bus(bus_name,source,destination,departure_time,journey_hours,total_seats,booked_seats,price) VALUES(?,?,?,?,?,?,0,?)"
                );

                ps.setString(1,fields[0].getText());
                ps.setString(2,fields[1].getText());
                ps.setString(3,fields[2].getText());

                // Departure Time
                ps.setTime(4, Time.valueOf(fields[3].getText()));

                ps.setInt(5,Integer.parseInt(fields[4].getText()));
                ps.setInt(6,Integer.parseInt(fields[5].getText()));
                ps.setDouble(7,Double.parseDouble(fields[6].getText()));

                ps.executeUpdate();

                JOptionPane.showMessageDialog(f,"Bus Added Successfully");
                f.dispose();

            }catch(Exception ex){
                JOptionPane.showMessageDialog(f,"Invalid Input! Please check time format (HH:MM:SS)");
                ex.printStackTrace();
            }
        });

        f.setVisible(true);
    }
}