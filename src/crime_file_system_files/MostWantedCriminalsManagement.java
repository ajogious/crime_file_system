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
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel settings
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setLayout(new BorderLayout(10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Most Wanted Criminals Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));
        
        JLabel criminalIdLabel = new JLabel("Criminal ID:");
        JTextField criminalIdText = new JTextField();
        JLabel reasonLabel = new JLabel("Reason for Wanted:");
        JTextField reasonText = new JTextField();
        
        formPanel.add(criminalIdLabel);
        formPanel.add(criminalIdText);
        formPanel.add(reasonLabel);
        formPanel.add(reasonText);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        JButton addButton = new JButton("Add Most Wanted");
        JButton backButton = new JButton("Back to Admin Page");
        
        buttonsPanel.add(addButton);
        buttonsPanel.add(backButton);
        
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        // Add panel to frame
        add(mainPanel);
        
        // Button action
        addButton.addActionListener(e -> {
            String criminalId = criminalIdText.getText();
            String reasonForWanted = reasonText.getText();
            
            if (criminalId.isEmpty() || reasonForWanted.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO most_wanted (criminal_id, reason_for_wanted) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, criminalId);
                preparedStatement.setString(2, reasonForWanted);

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Most wanted criminal added successfully");
                criminalIdText.setText("");
                reasonText.setText("");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to add most wanted criminal", "Error", JOptionPane.ERROR_MESSAGE);
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
