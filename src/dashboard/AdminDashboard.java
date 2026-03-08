package dashboard;

import auth.Login;
import java.awt.*;
import javax.swing.*;

public class AdminDashboard {

    public AdminDashboard(String username){

        JFrame f = new JFrame("Admin Dashboard");
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
        panel.setPreferredSize(new Dimension(500,420));
        panel.setBackground(new Color(255,255,255,220));

        Font h = new Font("Times New Roman",Font.BOLD,24);
        Font b = new Font("Times New Roman",Font.BOLD,16);

        JLabel title = new JLabel("Welcome Admin " + username);
        title.setFont(h);
        title.setBounds(110,30,350,30);

        JButton add = new JButton("Add Bus");
        JButton update = new JButton("Update Bus");
        JButton delete = new JButton("Delete Bus");
        JButton view = new JButton("View Bookings");
        JButton logout = new JButton("Logout");

        JButton[] buttons={add,update,delete,view,logout};

        int y=90;
        for(JButton btn:buttons){
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

        // ✅ ADD THESE ACTION LISTENERS
        add.addActionListener(e -> new AddBus());
        update.addActionListener(e -> new UpdateBus());
        delete.addActionListener(e -> new DeleteBus());
        view.addActionListener(e -> new ViewBookings());

        logout.addActionListener(e->{
            new Login();
            f.dispose();
        });

        f.setVisible(true);
    }
}