package auth;

import db.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Signup {

    public Signup(){

        JFrame f = new JFrame("Sign Up");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel bgPanel = new JPanel(){
            Image bg = new ImageIcon("Images/Bg.jpeg").getImage();
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(bg,0,0,getWidth(),getHeight(),this);
            }
        };

        bgPanel.setLayout(new GridBagLayout());
        f.setContentPane(bgPanel);

        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(420,380));
        panel.setBackground(new Color(255,255,255,220));

        Font h = new Font("Times New Roman",Font.BOLD,24);
        Font l = new Font("Times New Roman",Font.PLAIN,18);
        Font fnt = new Font("Times New Roman",Font.PLAIN,16);
        Font b = new Font("Times New Roman",Font.BOLD,16);

        JLabel title = new JLabel("Create Account");
        title.setFont(h);
        title.setBounds(130,20,250,30);

        JLabel u = new JLabel("Username");
        u.setFont(l); u.setBounds(40,80,120,30);

        JTextField user = new JTextField();
        user.setFont(fnt); user.setBounds(160,80,200,30);

        JLabel p = new JLabel("Password");
        p.setFont(l); p.setBounds(40,130,120,30);

        JPasswordField pass = new JPasswordField();
        pass.setFont(fnt); pass.setBounds(160,130,200,30);

        JLabel r = new JLabel("Role");
        r.setFont(l); r.setBounds(40,180,120,30);

        JComboBox<String> role =
                new JComboBox<>(new String[]{"user","admin"});
        role.setFont(fnt); role.setBounds(160,180,200,30);

        JButton register = new JButton("Register");
        register.setFont(b);
        register.setBackground(new Color(34,139,34));
        register.setForeground(Color.WHITE);
        register.setBounds(140,250,150,40);

        panel.add(title);
        panel.add(u); panel.add(user);
        panel.add(p); panel.add(pass);
        panel.add(r); panel.add(role);
        panel.add(register);

        bgPanel.add(panel);

        register.addActionListener(e->{
            try{
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO users(username,password,role) VALUES(?,?,?)"
                );
                ps.setString(1,user.getText());
                ps.setString(2,new String(pass.getPassword()));
                ps.setString(3,role.getSelectedItem().toString());
                ps.executeUpdate();

                JOptionPane.showMessageDialog(f,"Registration Successful");
                new Login();
                f.dispose();

            }catch(Exception ex){
                ex.printStackTrace();
            }
        });

        f.setVisible(true);
    }
}