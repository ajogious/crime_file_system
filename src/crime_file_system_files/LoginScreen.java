package crime_file_system_files;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// Import your JDBC package here
import jdbc.DatabaseConnection;

public class LoginScreen extends JFrame {

    private JTextField usernameText;
    private JPasswordField passwordText;

    public LoginScreen() {
        // Frame settings
        setTitle("Crime File System - Login");
        setSize(400, 250);
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
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameText = new JTextField();
        usernameText.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordText = new JPasswordField();
        passwordText.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(40, 167, 69));
        registerButton.setForeground(Color.WHITE);

        // Adding components to panel with GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(usernameText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordText, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(loginButton, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(registerButton, gbc);

        // Add panel to frame
        add(panel);

        // Button actions
        loginButton.addActionListener(e -> {
            String username = usernameText.getText();
            String password = new String(passwordText.getPassword());

            if (authenticate(username, password)) {
                JOptionPane.showMessageDialog(null, "Login successful");
                new AdminUserPage().setVisible(true);
                this.dispose(); // close the login window
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password");
            }
        });
        
        registerButton.addActionListener(e -> {
            new RegistrationForm().setVisible(true);
            this.dispose(); // close the login window
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
    
}