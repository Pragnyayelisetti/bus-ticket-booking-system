package dashboard;

import db.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class DeleteBus {

    public DeleteBus(){

        JFrame f=new JFrame("Delete Bus");
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
        panel.setPreferredSize(new Dimension(400,250));
        panel.setBackground(new Color(255,255,255,220));

        Font h=new Font("Times New Roman",Font.BOLD,24);
        Font l=new Font("Times New Roman",Font.PLAIN,18);
        Font b=new Font("Times New Roman",Font.BOLD,16);

        JLabel title=new JLabel("Delete Bus");
        title.setFont(h);
        title.setBounds(120,20,200,30);

        JLabel idL=new JLabel("Bus ID");
        idL.setFont(l);
        idL.setBounds(50,80,100,30);

        JTextField id=new JTextField();
        id.setBounds(150,80,180,30);

        JButton del=new JButton("Delete");
        del.setFont(b);
        del.setBackground(new Color(178,34,34));
        del.setForeground(Color.WHITE);
        del.setBounds(120,140,150,40);

        panel.add(title);
        panel.add(idL);
        panel.add(id);
        panel.add(del);

        bg.add(panel);

        del.addActionListener(e->{
            try{
                Connection con=DBConnection.getConnection();
                PreparedStatement ps=con.prepareStatement(
                        "DELETE FROM bus WHERE bus_id=?"
                );
                ps.setInt(1,Integer.parseInt(id.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(f,"Deleted Successfully");
                f.dispose();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        });

        f.setVisible(true);
    }
}