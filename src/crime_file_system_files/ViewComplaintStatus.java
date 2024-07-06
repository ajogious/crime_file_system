package crime_file_system_files;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jdbc.DatabaseConnection;

public class ViewComplaintStatus extends JFrame {

    private JTextArea resultArea;
    private JButton treatButton;

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
        JTextField searchText = new JTextField("Enter Complaint ID");
        JButton searchButton = new JButton("Search");
        resultArea = new JTextArea();
        JButton backButton = new JButton("Back");
        treatButton = new JButton("Treat Case");
        treatButton.setVisible(false); // Initially hidden

        // Placeholder text setup
        searchText.setForeground(Color.GRAY);
        searchText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchText.getText().equals("Enter Complaint ID")) {
                    searchText.setText("");
                    searchText.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchText.getText().isEmpty()) {
                    searchText.setForeground(Color.GRAY);
                    searchText.setText("Enter Complaint ID");
                }
            }
        });

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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(treatButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to frame
        add(panel);

        // Button actions
        searchButton.addActionListener(e -> {
            String complaintID = searchText.getText();

            if (complaintID.equals("Enter Complaint ID") || complaintID.trim().isEmpty()) {
                resultArea.setText("Please enter a valid complaint ID.");
                treatButton.setVisible(false); // Hide the button
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT * FROM complaints WHERE complaint_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, complaintID);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String status = resultSet.getString("status");
                    String details = resultSet.getString("crime_details");
                    resultArea.setText("Complaint ID: " + complaintID + "\nStatus: " + status + "\nDetails: " + details);

                    if ("Pending".equalsIgnoreCase(status)) {
                        treatButton.setVisible(true); // Show the button
                        treatButton.setActionCommand(complaintID); // Store the complaint ID in the button's action command
                    } else {
                        treatButton.setVisible(false); // Hide the button
                    }
                } else {
                    resultArea.setText("No complaint found with ID: " + complaintID);
                    treatButton.setVisible(false); // Hide the button
                }
            } catch (SQLException ex) {
                resultArea.setText("Error retrieving complaint status.");
                treatButton.setVisible(false); // Hide the button
            }
        });

        treatButton.addActionListener(e -> {
            String complaintID = treatButton.getActionCommand();
            treatPendingCase(complaintID);
        });

        backButton.addActionListener(e -> {
            new ComplaintRegistration().setVisible(true);
            this.dispose();
        });
    }

    private void treatPendingCase(String complaintID) {
        String query = "UPDATE complaints SET status = 'Treated' WHERE complaint_id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, complaintID);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Complaint treated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshComplaintStatus(complaintID);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to treat complaint", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshComplaintStatus(String complaintID) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM complaints WHERE complaint_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, complaintID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String status = resultSet.getString("status");
                String details = resultSet.getString("crime_details");
                resultArea.setText("Complaint ID: " + complaintID + "\nStatus: " + status + "\nDetails: " + details);

                // Now the complaint is treated, so hide the treat button
                treatButton.setVisible(false);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            resultArea.setText("Error retrieving updated complaint status.");
        }
    }

}
