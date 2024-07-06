package crime_file_system_files;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jdbc.DatabaseConnection;

public class RegistrationForm extends JFrame {

    private JTextField fullNameText;
    private JTextField rankText;
    private JTextField usernameText;
    private JTextField phoneNumberText;
    private JPasswordField passwordText;

    public RegistrationForm() {
        // Frame settings
        setTitle("Crime File System - User Registration");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel and layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Components
        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        fullNameText = new JTextField();
        fullNameText.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel rankLabel = new JLabel("Rank:");
        rankLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rankText = new JTextField();
        rankText.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameText = new JTextField();
        usernameText.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneNumberText = new JTextField();
        phoneNumberText.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordText = new JPasswordField();
        passwordText.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(0, 123, 255));
        registerButton.setForeground(Color.WHITE);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(40, 167, 69));
        loginButton.setForeground(Color.WHITE);

        // Adding components to panel with GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(fullNameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(fullNameText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(rankLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(rankText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(usernameText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(phoneNumberLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(phoneNumberText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(passwordText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(registerButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(loginButton, gbc);

        // Add panel to frame
        add(panel);

        // Button actions
        registerButton.addActionListener(e -> {
            String fullName = fullNameText.getText();
            String rank = rankText.getText();
            String username = usernameText.getText();
            String phoneNumber = phoneNumberText.getText();
            String password = new String(passwordText.getPassword());

            if (fullName.isEmpty() || username.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (usernameExists(username)) {
                JOptionPane.showMessageDialog(null, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            registerUser(fullName, rank, username, phoneNumber, password);
        });

        loginButton.addActionListener(e -> {
            new LoginScreen().setVisible(true);
            this.dispose();
        });
    }

    private boolean usernameExists(String username) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void registerUser(String fullName, String rank, String username, String phoneNumber, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String insertQuery = "INSERT INTO users (full_name, `rank`, username, phone_number, password, role) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, rank);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, password); 
            preparedStatement.setString(6, "Normal User");

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                new LoginScreen().setVisible(true);
                this.dispose(); 
            } else {
                JOptionPane.showMessageDialog(null, "Failed to register user", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred during registration", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
