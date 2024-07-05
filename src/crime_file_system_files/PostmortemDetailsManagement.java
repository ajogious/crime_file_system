package crime_file_system_files;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jdbc.DatabaseConnection;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import java.util.Properties;

public class PostmortemDetailsManagement extends JFrame {

    public PostmortemDetailsManagement() {
        // Frame settings
        setTitle("Crime File System - Postmortem Details Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel and layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        // Components
        JLabel dateLabel = new JLabel("Date of Death:");
        
        // Date Picker
        SqlDateModel model = new SqlDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        JLabel causeLabel = new JLabel("Cause of Death:");
        JTextField causeText = new JTextField();
        JLabel doctorLabel = new JLabel("Doctor Name:");
        JTextField doctorText = new JTextField();
        JButton addButton = new JButton("Add Postmortem");
        JButton backButton = new JButton("Back to Admin Page");

        // Adding components to panel
        panel.add(dateLabel);
        panel.add(datePicker);
        panel.add(causeLabel);
        panel.add(causeText);
        panel.add(doctorLabel);
        panel.add(doctorText);
        panel.add(new JLabel()); // empty cell
        panel.add(addButton);
        panel.add(new JLabel()); // empty cell
        panel.add(backButton);

        // Add panel to frame
        add(panel);

        // Button action
        addButton.addActionListener(e -> {
            java.sql.Date dateOfDeath = (java.sql.Date) datePicker.getModel().getValue();
            String causeOfDeath = causeText.getText();
            String doctorName = doctorText.getText();

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO postmortem (date_of_death, cause_of_death, doctor_name) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setDate(1, dateOfDeath);
                preparedStatement.setString(2, causeOfDeath);
                preparedStatement.setString(3, doctorName);

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Postmortem details added successfully");
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
        SwingUtilities.invokeLater(() -> new PostmortemDetailsManagement().setVisible(true));
    }
}