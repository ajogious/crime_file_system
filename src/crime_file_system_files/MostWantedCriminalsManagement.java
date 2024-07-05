/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crime_file_system_files;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jdbc.DatabaseConnection;

public class MostWantedCriminalsManagement extends JFrame {

    public MostWantedCriminalsManagement() {
        // Frame settings
        setTitle("Crime File System - Most Wanted Criminals Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel and layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        // Components
        JLabel criminalIdLabel = new JLabel("Criminal ID:");
        JTextField criminalIdText = new JTextField();
        JLabel reasonLabel = new JLabel("Reason for Wanted:");
        JTextField reasonText = new JTextField();
        JButton addButton = new JButton("Add Most Wanted");
        JButton backButton = new JButton("Back to Admin Page");

        // Adding components to panel
        panel.add(criminalIdLabel);
        panel.add(criminalIdText);
        panel.add(reasonLabel);
        panel.add(reasonText);
        panel.add(new JLabel()); // empty cell
        panel.add(addButton);
        panel.add(new JLabel()); // empty cell
        panel.add(backButton);

        // Add panel to frame
        add(panel);

        // Button action
        addButton.addActionListener(e -> {
            String criminalId = criminalIdText.getText();
            String reasonForWanted = reasonText.getText();

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO most_wanted (criminal_id, reason_for_wanted) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, criminalId);
                preparedStatement.setString(2, reasonForWanted);

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Most wanted criminal added successfully");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        
         backButton.addActionListener(e -> {
            new AdminPage().setVisible(true);
            this.dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MostWantedCriminalsManagement().setVisible(true));
    }
}
