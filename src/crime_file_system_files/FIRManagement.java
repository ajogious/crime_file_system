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

public class FIRManagement extends JFrame {

    public FIRManagement() {
        // Frame settings
        setTitle("Crime File System - FIR Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel and layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        // Components
        JLabel crimeDetailsLabel = new JLabel("Crime Details:");
        JTextField crimeDetailsText = new JTextField();
        JLabel dateLabel = new JLabel("Date of Occurrence:");

        // Date Picker
        SqlDateModel model = new SqlDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        JLabel placeLabel = new JLabel("Place of Occurrence:");
        JTextField placeText = new JTextField();
        JButton addButton = new JButton("Add FIR");
        JButton backButton = new JButton("Back to Admin Page");

        // Adding components to panel
        panel.add(crimeDetailsLabel);
        panel.add(crimeDetailsText);
        panel.add(dateLabel);
        panel.add(datePicker);
        panel.add(placeLabel);
        panel.add(placeText);
        panel.add(new JLabel()); // empty cell
        panel.add(addButton);
        panel.add(new JLabel()); // empty cell
        panel.add(backButton);

        // Add panel to frame
        add(panel);

        // Button action
        addButton.addActionListener(e -> {
            String crimeDetails = crimeDetailsText.getText();
            java.sql.Date dateOfOccurrence = (java.sql.Date) datePicker.getModel().getValue();
            String placeOfOccurrence = placeText.getText();

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO firs (crime_details, date_of_occurrence, place_of_occurrence) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, crimeDetails);
                preparedStatement.setDate(2, dateOfOccurrence);
                preparedStatement.setString(3, placeOfOccurrence);

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "FIR added successfully");
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
        SwingUtilities.invokeLater(() -> new FIRManagement().setVisible(true));
    }
}