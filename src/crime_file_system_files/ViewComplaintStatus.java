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
import java.sql.ResultSet;
import java.sql.SQLException;
import jdbc.DatabaseConnection;

public class ViewComplaintStatus extends JFrame {

    public ViewComplaintStatus() {
        // Frame settings
        setTitle("Crime File System - View Complaint Status");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel and layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Components
        JLabel searchLabel = new JLabel("Enter Complaint ID:");
        JTextField searchText = new JTextField();
        JButton searchButton = new JButton("Search");
        JTextArea resultArea = new JTextArea();
        JButton backButton = new JButton("Back to Admin Page");

        // Search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.add(searchLabel);
        searchPanel.add(searchText);
        searchPanel.add(searchButton);

        // Result panel
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Add components to main panel
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(resultPanel, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        // Add panel to frame
        add(panel);

        // Button actions
        searchButton.addActionListener(e -> {
            String complaintID = searchText.getText();
            System.out.println(complaintID);

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT * FROM complaints WHERE complaint_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, complaintID);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String status = resultSet.getString("status");
                    String details = resultSet.getString("crime_details");
                    resultArea.setText("Complaint ID: " + complaintID + "\nStatus: " + status + "\nDetails: " + details);

                } else {
                    resultArea.setText("No complaint found with ID: " + complaintID);
                    System.out.println(complaintID + 100);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                resultArea.setText("Error retrieving complaint status.");
            }
        });

        backButton.addActionListener(e -> {
            new AdminPage().setVisible(true);
            this.dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewComplaintStatus().setVisible(true));
    }
}
