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

public class CriminalRegisterManagement extends JFrame {

    public CriminalRegisterManagement() {
        // Frame settings
        setTitle("Crime File System - Criminal Register Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel and layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        // Components
        JLabel criminalNumberLabel = new JLabel("Criminal Number:");
        JTextField criminalNumberText = new JTextField();
        JLabel ageLabel = new JLabel("Age:");
        JTextField ageText = new JTextField();
        JLabel occupationLabel = new JLabel("Occupation:");
        JTextField occupationText = new JTextField();
        JLabel crimeTypeLabel = new JLabel("Type of Crime:");
        JTextField crimeTypeText = new JTextField();
        JButton addButton = new JButton("Add Criminal");
        JButton backButton = new JButton("Back to Admin Page");

        // Adding components to panel
        panel.add(criminalNumberLabel);
        panel.add(criminalNumberText);
        panel.add(ageLabel);
        panel.add(ageText);
        panel.add(occupationLabel);
        panel.add(occupationText);
        panel.add(crimeTypeLabel);
        panel.add(crimeTypeText);
        panel.add(new JLabel()); // empty cell
        panel.add(addButton);
        panel.add(new JLabel()); // empty cell
        panel.add(backButton);

        // Add panel to frame
        add(panel);

        // Button action
        addButton.addActionListener(e -> {
            String criminalNumber = criminalNumberText.getText();
            int age = Integer.parseInt(ageText.getText());
            String occupation = occupationText.getText();
            String typeOfCrime = crimeTypeText.getText();

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
