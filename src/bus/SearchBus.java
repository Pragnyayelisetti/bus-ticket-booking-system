package bus;

import db.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class SearchBus {

    public SearchBus(String username){

        JFrame f = new JFrame("Search Bus");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);

        ImageIcon bg = new ImageIcon("images/busbg.jpg");
        JLabel background = new JLabel(bg);
        background.setLayout(null);
        f.setContentPane(background);

        JPanel overlay = new JPanel();
        overlay.setLayout(null);
        overlay.setBackground(new Color(0,0,0,170));
        overlay.setBounds(350,120,750,600);
        background.add(overlay);

        JLabel title = new JLabel("Search Buses");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI",Font.BOLD,30));
        title.setBounds(250,40,300,40);
        overlay.add(title);

        // FROM
        JLabel fromLabel = new JLabel("From");
        fromLabel.setForeground(Color.WHITE);
        fromLabel.setBounds(150,140,100,30);
        overlay.add(fromLabel);

        JComboBox<String> fromBox = new JComboBox<>();
        fromBox.setBounds(150,180,200,40);
        overlay.add(fromBox);

        // TO
        JLabel toLabel = new JLabel("To");
        toLabel.setForeground(Color.WHITE);
        toLabel.setBounds(400,140,100,30);
        overlay.add(toLabel);

        JComboBox<String> toBox = new JComboBox<>();
        toBox.setBounds(400,180,200,40);
        overlay.add(toBox);

        JComboBox<String> busResult = new JComboBox<>();
        busResult.setBounds(200,320,350,40);
        overlay.add(busResult);

        JButton searchBtn = new JButton("Search Bus");
        searchBtn.setBounds(280,240,180,45);
        searchBtn.setBackground(new Color(0,120,215));
        searchBtn.setForeground(Color.WHITE);
        overlay.add(searchBtn);

        JButton bookBtn = new JButton("Book Bus");
        bookBtn.setBounds(280,380,200,45);
        bookBtn.setBackground(new Color(0,180,100));
        bookBtn.setForeground(Color.WHITE);
        overlay.add(bookBtn);

        f.setVisible(true);

        // ===== LOAD ALL CITIES FROM DB =====
        try{

            Connection con = DBConnection.getConnection();

            ResultSet rs1 = con.createStatement()
            .executeQuery("SELECT DISTINCT source FROM bus");

            while(rs1.next()){
                fromBox.addItem(rs1.getString(1));
            }

            ResultSet rs2 = con.createStatement()
            .executeQuery("SELECT DISTINCT destination FROM bus");

            while(rs2.next()){
                toBox.addItem(rs2.getString(1));
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        // ===== SEARCH =====
        searchBtn.addActionListener(e -> {

            try{

                busResult.removeAllItems();

                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM bus WHERE source=? AND destination=?"
                );

                ps.setString(1, fromBox.getSelectedItem().toString());
                ps.setString(2, toBox.getSelectedItem().toString());

                ResultSet rs = ps.executeQuery();

                while(rs.next()){

                    busResult.addItem(
                    rs.getInt("bus_id")+" | "+
                    rs.getString("bus_name")+" | "+
                    rs.getInt("journey_hours")+" hrs | "+
                    rs.getInt("seats")+" seats"
                    );
                }

            }catch(Exception ex){
                ex.printStackTrace();
            }

        });

        // ===== BOOK =====
        bookBtn.addActionListener(e -> {

            if(busResult.getSelectedItem()==null) return;

            String selected = busResult.getSelectedItem().toString();
            String busId = selected.split("\\|")[0].trim();

            new BookTicket(username,busId);
            f.dispose();
        });

    }
}
