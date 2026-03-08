package bus;

import java.awt.*;
import java.util.List;
import javax.swing.*;

public class TicketDetails {

    public TicketDetails(String busId,String passenger,List<Integer> seats){

        JFrame f=new JFrame("Ticket Details");
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
        panel.setPreferredSize(new Dimension(500,400));
        panel.setBackground(new Color(255,255,255,220));

        Font h=new Font("Times New Roman",Font.BOLD,24);
        Font l=new Font("Times New Roman",Font.PLAIN,18);

        JLabel title=new JLabel("Booking Confirmation");
        title.setFont(h);
        title.setBounds(120,20,300,30);

        JLabel bus=new JLabel("Bus ID: "+busId);
        bus.setFont(l);
        bus.setBounds(60,80,300,30);

        JLabel pass=new JLabel("Passenger: "+passenger);
        pass.setFont(l);
        pass.setBounds(60,120,300,30);

        int y=170;
        for(Integer s:seats){
            JLabel seat=new JLabel("Seat: "+s);
            seat.setFont(l);
            seat.setBounds(60,y,200,30);
            panel.add(seat);
            y+=35;
        }

        JButton close=new JButton("Close");
        close.setBackground(new Color(30,144,255));
        close.setForeground(Color.WHITE);
        close.setBounds(180,y+20,130,40);

        panel.add(title);
        panel.add(bus);
        panel.add(pass);
        panel.add(close);

        bg.add(panel);

        close.addActionListener(e->f.dispose());

        f.setVisible(true);
    }
}