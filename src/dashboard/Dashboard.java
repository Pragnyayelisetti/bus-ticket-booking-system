package dashboard;

import auth.Login;
import bus.CancelTicket;
import bus.MyBookings;
import bus.SearchBusFinal;
import java.awt.*;
import javax.swing.*;

public class Dashboard {

    public Dashboard(String username){

        JFrame f = new JFrame("User Dashboard");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ===== Background =====
        JPanel bg = new JPanel(){
            Image img = new ImageIcon("Images/Bg.jpeg").getImage();

            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(img,0,0,getWidth(),getHeight(),this);
            }
        };

        bg.setLayout(new GridBagLayout());
        f.setContentPane(bg);

        // ===== Center Card =====
        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(500,420));
        panel.setBackground(new Color(255,255,255,220));

        Font h = new Font("Times New Roman",Font.BOLD,24);
        Font b = new Font("Times New Roman",Font.BOLD,16);

        JLabel title = new JLabel("Welcome " + username);
        title.setFont(h);
        title.setBounds(150,30,300,30);

        JButton search = new JButton("Search Bus");
        JButton my = new JButton("My Bookings");
        JButton cancel = new JButton("Cancel Ticket");
        JButton logout = new JButton("Logout");

        JButton[] btns={search,my,cancel,logout};

        int y=100;
        for(JButton btn:btns){
            btn.setFont(b);
            btn.setBackground(new Color(30,144,255));
            btn.setForeground(Color.WHITE);
            btn.setBounds(150,y,200,40);
            panel.add(btn);
            y+=60;
        }

        logout.setBackground(new Color(178,34,34));

        panel.add(title);
        bg.add(panel);

        // ✅ CONNECT BUTTONS PROPERLY
        search.addActionListener(e -> new SearchBusFinal(username));

        my.addActionListener(e -> new MyBookings(username));

        cancel.addActionListener(e -> new CancelTicket(username));

        logout.addActionListener(e -> {
            new Login();
            f.dispose();
        });

        f.setVisible(true);
    }
}