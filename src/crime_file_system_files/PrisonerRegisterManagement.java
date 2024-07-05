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

public class PrisonerRegisterManagement extends JFrame {

    public PrisonerRegisterManagement() {
        // Frame settings
        setTitle("Crime File System - Prisoner Register Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel and layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        // Components
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameText = new JTextField();
        JLabel imprisonmentDateLabel = new JLabel("Date of Imprisonment:");

        // Date Picker for Date of Imprisonment
        SqlDateModel imprisonmentModel = new SqlDateModel();
        Properties imprisonmentProps = new Properties();
        imprisonmentProps.put("text.today", "Today");
        imprisonmentProps.put("text.month", "Month");
        imprisonmentProps.put("text.year", "Year");
        JDatePanelImpl imprisonmentDatePanel = new JDatePanelImpl(imprisonmentModel, imprisonmentProps);
        JDatePickerImpl imprisonmentDatePicker = new JDatePickerImpl(imprisonmentDatePanel, new DateLabelFormatter());

        JLabel releaseDateLabel = new JLabel("Release Date:");

        // Date Picker for Release Date
        SqlDateModel releaseModel = new SqlDateModel();
        Properties releaseProps = new Properties();
        releaseProps.put("text.today", "Today");
        releaseProps.put("text.month", "Month");
        releaseProps.put("text.year", "Year");
        JDatePanelImpl releaseDatePanel = new JDatePanelImpl(releaseModel, releaseProps);
        JDatePickerImpl releaseDatePicker = new JDatePickerImpl(releaseDatePanel, new DateLabelFormatter());

        JButton addButton = new JButton("Add Prisoner");
        JButton backButton = new JButton("Back to Admin Page");

        // Adding components to panel
        panel.add(nameLabel);
        panel.add(nameText);
        panel.add(imprisonmentDateLabel);
        panel.add(imprisonmentDatePicker);
        panel.add(releaseDateLabel);
        panel.add(releaseDatePicker);
        panel.add(new JLabel()); // empty cell
        panel.add(addButton);
        panel.add(new JLabel()); // empty cell
        panel.add(backButton);

        // Add panel to frame
        add(panel);

        // Button action
        addButton.addActionListener(e -> {
            String name = nameText.getText();
            java.sql.Date dateOfImprisonment = (java.sql.Date) imprisonmentDatePicker.getModel().getValue();
            java.sql.Date releaseDate = (java.sql.Date) releaseDatePicker.getModel().getValue();

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO prisoners (name, date_of_imprisonment, release_date) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, name);
                preparedStatement.setDate(2, dateOfImprisonment);
                preparedStatement.setDate(3, releaseDate);

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Prisoner added successfully");
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
        SwingUtilities.invokeLater(() -> new PrisonerRegisterManagement().setVisible(true));
    }
}
