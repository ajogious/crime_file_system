package crime_file_system_files;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jdbc.DatabaseConnection;

public class ViewCriminalRecord extends JFrame {

    public ViewCriminalRecord() {
        // Frame settings
        setTitle("Crime File System - View Criminal Record");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Components
        JLabel searchLabel = new JLabel("Enter Criminal Number:");
        JTextField searchText = new JTextField("Enter Criminal Number");
        JButton searchButton = new JButton("Search");
        JTextArea resultArea = new JTextArea();
        JButton backButton = new JButton("Back");

        // Placeholder text setup
        searchText.setForeground(Color.GRAY);
        searchText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchText.getText().equals("Enter Criminal Number")) {
                    searchText.setText("");
                    searchText.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchText.getText().isEmpty()) {
                    searchText.setForeground(Color.GRAY);
                    searchText.setText("Enter Criminal Number");
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
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(resultPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Button actions
        searchButton.addActionListener(e -> {
            String criminalNumber = searchText.getText();

            if (criminalNumber.equals("Enter Criminal Number") || criminalNumber.trim().isEmpty()) {
                resultArea.setText("Please enter a valid criminal number.");
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT * FROM criminals WHERE criminal_number = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, criminalNumber);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String age = resultSet.getString("age");
                    String occupation = resultSet.getString("occupation");
                    String typeOfCrime = resultSet.getString("type_of_crime");
                    resultArea.setText("Criminal Number: " + criminalNumber + "\nAge: " + age + "\nOccupation: " + occupation + "\nType of Crime: " + typeOfCrime);
                } else {
                    resultArea.setText("No criminal found with number: " + criminalNumber);
                }
            } catch (SQLException ex) {
                resultArea.setText("Error retrieving criminal record.");
                ex.printStackTrace();
            }
        });

        backButton.addActionListener(e -> {
            new CriminalRegisterManagement().setVisible(true);
            this.dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewCriminalRecord().setVisible(true));
    }
}
