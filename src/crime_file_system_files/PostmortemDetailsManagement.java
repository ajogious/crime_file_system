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
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Components
        JLabel dateLabel = new JLabel("Date of Death:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(dateLabel, gbc);

        // Date Picker
        SqlDateModel model = new SqlDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(datePicker, gbc);

        JLabel causeLabel = new JLabel("Cause of Death:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(causeLabel, gbc);

        JTextField causeText = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(causeText, gbc);

        JLabel doctorLabel = new JLabel("Doctor Name:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(doctorLabel, gbc);

        JTextField doctorText = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(doctorText, gbc);

        JButton addButton = new JButton("Add Postmortem");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(addButton, gbc);

        JButton viewRecordsButton = new JButton("View Postmortem Records");
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(viewRecordsButton, gbc);

        JButton backButton = new JButton("Back");
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(backButton, gbc);

        // Add panel to frame
        add(panel);

        // Button actions
        addButton.addActionListener(e -> {
            java.sql.Date dateOfDeath = (java.sql.Date) datePicker.getModel().getValue();
            String causeOfDeath = causeText.getText();
            String doctorName = doctorText.getText();

            // Validate fields
            if (dateOfDeath == null || causeOfDeath.isEmpty() || doctorName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO postmortem (date_of_death, cause_of_death, doctor_name) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setDate(1, dateOfDeath);
                preparedStatement.setString(2, causeOfDeath);
                preparedStatement.setString(3, doctorName);

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Postmortem details added successfully");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "An error occurred while adding the postmortem details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        viewRecordsButton.addActionListener(e -> {
            new ViewPostmortemRecord().setVisible(true);
            this.dispose();
        });

        backButton.addActionListener(e -> {
            new AdminUserPage().setVisible(true);
            this.dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PostmortemDetailsManagement().setVisible(true));
    }
}
