package crime_file_system_files;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jdbc.DatabaseConnection;

public class CriminalRegisterManagement extends JFrame {

    public CriminalRegisterManagement() {
        // Frame settings
        setTitle("Crime File System - Criminal Register Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Criminal Register Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Criminal number
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Criminal Number:"), gbc);

        gbc.gridx = 1;
        JTextField criminalNumberText = new JTextField(20);
        formPanel.add(criminalNumberText, gbc);

        // Age
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Age:"), gbc);

        gbc.gridx = 1;
        JTextField ageText = new JTextField(20);
        formPanel.add(ageText, gbc);

        // Occupation
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Occupation:"), gbc);

        gbc.gridx = 1;
        JTextField occupationText = new JTextField(20);
        formPanel.add(occupationText, gbc);

        // Type of crime
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Type of Crime:"), gbc);

        gbc.gridx = 1;
        JTextField crimeTypeText = new JTextField(20);
        formPanel.add(crimeTypeText, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton addButton = new JButton("Add Criminal");
        JButton backButton = new JButton("Back");

        buttonPanel.add(addButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Button actions
        addButton.addActionListener(e -> {
            String criminalNumber = criminalNumberText.getText();
            String ageTextValue = ageText.getText();
            String occupation = occupationText.getText();
            String typeOfCrime = crimeTypeText.getText();

            // Check if any field is empty
            if (criminalNumber.isEmpty() || ageTextValue.isEmpty() || occupation.isEmpty() || typeOfCrime.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageTextValue);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Age must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO criminals (criminal_number, age, occupation, type_of_crime) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, criminalNumber);
                preparedStatement.setInt(2, age);
                preparedStatement.setString(3, occupation);
                preparedStatement.setString(4, typeOfCrime);

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Criminal added successfully");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "An error occurred while adding the criminal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        backButton.addActionListener(e -> {
            new AdminPage().setVisible(true);
            this.dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CriminalRegisterManagement().setVisible(true));
    }
}
