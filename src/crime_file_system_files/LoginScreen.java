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

public class LoginScreen extends JFrame {

     private JTextField usernameText;
    private JPasswordField passwordText;

    public LoginScreen() {
        // Frame settings
        setTitle("Crime File System - Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel and layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        // Components
        JLabel usernameLabel = new JLabel("Username:");
        usernameText = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordText = new JPasswordField();
        JButton loginButton = new JButton("Login");

        // Adding components to panel
        panel.add(usernameLabel);
        panel.add(usernameText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(new JLabel()); // empty cell
        panel.add(loginButton);

        // Add panel to frame
        add(panel);

        // Button action
        loginButton.addActionListener(e -> {
            String username = usernameText.getText();
            String password = new String(passwordText.getPassword());

            if (authenticate(username, password)) {
                JOptionPane.showMessageDialog(null, "Login successful");
                new AdminPage().setVisible(true);
                this.dispose(); // close the login window
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password");
            }
        });
    }

    private boolean authenticate(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT password FROM users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                return password.equals(storedPassword); // Replace with hash verification in production
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}