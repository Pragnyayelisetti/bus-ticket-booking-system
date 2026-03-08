package dashboard;

import db.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class UpdateBus {

    public UpdateBus(){

        JFrame f=new JFrame("Update Bus");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel bg=new JPanel(){
            Image img=new ImageIcon("Images/Bg.jpeg").getImage();
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(img,0,0,getWidth(),getHeight(),this);
            }
        };

        bg.setLayout(new GridBagLayout());
        f.setContentPane(bg);

        JPanel panel=new JPanel(null);
        panel.setPreferredSize(new Dimension(450,350));
        panel.setBackground(new Color(255,255,255,220));

        Font h=new Font("Times New Roman",Font.BOLD,24);
        Font l=new Font("Times New Roman",Font.PLAIN,18);
        Font fnt=new Font("Times New Roman",Font.PLAIN,16);
        Font b=new Font("Times New Roman",Font.BOLD,16);

        JLabel title=new JLabel("Update Bus");
        title.setFont(h);
        title.setBounds(140,20,200,30);

        JLabel idL=new JLabel("Bus ID");
        idL.setFont(l);
        idL.setBounds(50,80,120,30);

        JTextField id=new JTextField();
        id.setFont(fnt);
        id.setBounds(180,80,200,30);

        JLabel priceL=new JLabel("New Price");
        priceL.setFont(l);
        priceL.setBounds(50,130,120,30);

        JTextField price=new JTextField();
        price.setFont(fnt);
        price.setBounds(180,130,200,30);

        JLabel seatL=new JLabel("Total Seats");
        seatL.setFont(l);
        seatL.setBounds(50,180,120,30);

        JTextField seats=new JTextField();
        seats.setFont(fnt);
        seats.setBounds(180,180,200,30);

        JButton update=new JButton("Update");
        update.setFont(b);
        update.setBackground(new Color(30,144,255));
        update.setForeground(Color.WHITE);
        update.setBounds(150,240,150,40);

        panel.add(title);
        panel.add(idL); panel.add(id);
        panel.add(priceL); panel.add(price);
        panel.add(seatL); panel.add(seats);
        panel.add(update);

        bg.add(panel);

        update.addActionListener(e->{
            try{
                Connection con=DBConnection.getConnection();

                PreparedStatement ps=con.prepareStatement(
                        "UPDATE bus SET price=?,total_seats=? WHERE bus_id=?"
                );

                ps.setDouble(1,Double.parseDouble(price.getText()));
                ps.setInt(2,Integer.parseInt(seats.getText()));
                ps.setInt(3,Integer.parseInt(id.getText()));

                ps.executeUpdate();
                JOptionPane.showMessageDialog(f,"Updated Successfully");
                f.dispose();

            }catch(Exception ex){
                ex.printStackTrace();
            }
        });

        f.setVisible(true);
    }
}