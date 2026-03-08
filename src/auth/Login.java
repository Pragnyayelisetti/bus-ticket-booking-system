package auth;

import dashboard.AdminDashboard;
import dashboard.Dashboard;
import db.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Login {

    public Login(){

        JFrame f = new JFrame("Bus Booking System");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ===== Background Panel =====
        JPanel backgroundPanel = new JPanel() {
            Image bg = new ImageIcon("Images/Bg.jpeg").getImage();

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(new GridBagLayout()); // CENTER PERFECTLY
        f.setContentPane(backgroundPanel);

        // ===== Login Card Panel =====
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(400, 350));
        panel.setBackground(new Color(255, 255, 255, 220)); // Transparent white
        panel.setLayout(null);

        Font headingFont = new Font("Times New Roman", Font.BOLD, 24);
        Font labelFont = new Font("Times New Roman", Font.PLAIN, 18);
        Font fieldFont = new Font("Times New Roman", Font.PLAIN, 16);
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 16);

        JLabel title = new JLabel("User Login");
        title.setFont(headingFont);
        title.setBounds(140,20,200,30);

        JLabel u = new JLabel("Username");
        u.setFont(labelFont);
        u.setBounds(40,80,120,30);

        JTextField user = new JTextField();
        user.setFont(fieldFont);
        user.setBounds(160,80,180,30);

        JLabel p = new JLabel("Password");
        p.setFont(labelFont);
        p.setBounds(40,130,120,30);

        JPasswordField pass = new JPasswordField();
        pass.setFont(fieldFont);
        pass.setBounds(160,130,180,30);

        JLabel r = new JLabel("Role");
        r.setFont(labelFont);
        r.setBounds(40,180,120,30);

        String roles[]={"user","admin"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        roleBox.setFont(fieldFont);
        roleBox.setBounds(160,180,180,30);

        JButton login = new JButton("Login");
        login.setFont(buttonFont);
        login.setBackground(new Color(30,144,255));
        login.setForeground(Color.WHITE);
        login.setBounds(60,240,120,40);

        JButton signup = new JButton("Sign Up");
        signup.setFont(buttonFont);
        signup.setBackground(new Color(34,139,34));
        signup.setForeground(Color.WHITE);
        signup.setBounds(210,240,120,40);

        panel.add(title);
        panel.add(u); panel.add(user);
        panel.add(p); panel.add(pass);
        panel.add(r); panel.add(roleBox);
        panel.add(login); panel.add(signup);

        backgroundPanel.add(panel); // Automatically centered

        // ===== Login Action =====
        login.addActionListener(e->{
            try{
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM users WHERE username=? AND password=? AND role=?"
                );

                ps.setString(1,user.getText());
                ps.setString(2,new String(pass.getPassword()));
                ps.setString(3,roleBox.getSelectedItem().toString());

                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    if(roleBox.getSelectedItem().toString().equals("admin")){
                        new AdminDashboard(user.getText());
                    } else {
                        new Dashboard(user.getText());
                    }
                    f.dispose();
                } else {
                    JOptionPane.showMessageDialog(f,"Invalid Login");
                }

            }catch(Exception ex){
                ex.printStackTrace();
            }
        });

        signup.addActionListener(e->{
            new Signup();
            f.dispose();
        });

        f.setVisible(true);
    }
}