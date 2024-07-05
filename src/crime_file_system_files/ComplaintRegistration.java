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

public class ComplaintRegistration extends JFrame {

    public ComplaintRegistration() {
        // Frame settings
        setTitle("Crime File System - Register Complaint");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel and layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        // Components
        JLabel incidentLabel = new JLabel("Incident Details:");
        JTextField incidentText = new JTextField();
        JLabel victimLabel = new JLabel("Victim Details:");
        JTextField victimText = new JTextField();
        JLabel crimeLabel = new JLabel("Crime Details:");
        JTextField crimeText = new JTextField();
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Admin Page");

        // Adding components to panel
        panel.add(incidentLabel);
        panel.add(incidentText);
        panel.add(victimLabel);
        panel.add(victimText);
        panel.add(crimeLabel);
        panel.add(crimeText);
        panel.add(new JLabel()); // empty cell
        panel.add(registerButton);
        panel.add(new JLabel()); // empty cell
        panel.add(backButton);

        // Add panel to frame
        add(panel);

        // Button action
        registerButton.addActionListener(e -> {
            String incidentDetails = incidentText.getText();
            String victimDetails = victimText.getText();
            String crimeDetails = crimeText.getText();

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO complaints (incident_details, victim_details, crime_details) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, incidentDetails);
                preparedStatement.setString(2, victimDetails);
                preparedStatement.setString(3, crimeDetails);

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Complaint registered successfully");
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
        SwingUtilities.invokeLater(() -> new ComplaintRegistration().setVisible(true));
    }
}
