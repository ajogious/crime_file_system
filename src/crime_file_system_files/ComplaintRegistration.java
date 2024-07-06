package crime_file_system_files;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

        // Main panel with padding
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Register Complaint", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Incident details
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Incident Details:"), gbc);

        gbc.gridx = 1;
        JTextField incidentText = new JTextField(20);
        formPanel.add(incidentText, gbc);

        // Victim details
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Victim Details:"), gbc);

        gbc.gridx = 1;
        JTextField victimText = new JTextField(20);
        formPanel.add(victimText, gbc);

        // Crime details
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Crime Details:"), gbc);

        gbc.gridx = 1;
        JTextField crimeText = new JTextField(20);
        formPanel.add(crimeText, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to frame
        add(panel);

        // Button action
        registerButton.addActionListener(e -> {
            String incidentDetails = incidentText.getText();
            String victimDetails = victimText.getText();
            String crimeDetails = crimeText.getText();

            // Check if any field is empty
            if (incidentDetails.isEmpty() || victimDetails.isEmpty() || crimeDetails.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO complaints (incident_details, victim_details, crime_details) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, incidentDetails);
                preparedStatement.setString(2, victimDetails);
                preparedStatement.setString(3, crimeDetails);

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Complaint registered successfully");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "An error occurred while registering the complaint: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        backButton.addActionListener(e -> {
            new AdminUserPage().setVisible(true);
            this.dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ComplaintRegistration().setVisible(true));
    }
}
