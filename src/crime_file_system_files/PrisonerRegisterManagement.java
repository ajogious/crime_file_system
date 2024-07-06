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
        setLayout(new BorderLayout());

        // Main panel with border
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title label
        JLabel titleLabel = new JLabel("Prisoner Register Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));

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

        // Adding components to form panel
        formPanel.add(nameLabel);
        formPanel.add(nameText);
        formPanel.add(imprisonmentDateLabel);
        formPanel.add(imprisonmentDatePicker);
        formPanel.add(releaseDateLabel);
        formPanel.add(releaseDatePicker);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Add Prisoner");
        JButton backButton = new JButton("Back");
        JButton viewButton = new JButton("View Prisoners Records");

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Button action
        addButton.addActionListener(e -> {
            String name = nameText.getText();
            java.sql.Date dateOfImprisonment = (java.sql.Date) imprisonmentDatePicker.getModel().getValue();
            java.sql.Date releaseDate = (java.sql.Date) releaseDatePicker.getModel().getValue();

            // Validate fields
            if (name.isEmpty() || dateOfImprisonment == null || releaseDate == null) {
                JOptionPane.showMessageDialog(null, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO prisoners (name, date_of_imprisonment, release_date) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, name);
                preparedStatement.setDate(2, dateOfImprisonment);
                preparedStatement.setDate(3, releaseDate);

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Prisoner added successfully");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "An error occurred while adding the prisoner: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        viewButton.addActionListener(e -> {
            new ViewPrisonersRecord().setVisible(true);
            this.dispose();
        });

        backButton.addActionListener(e -> {
            new AdminUserPage().setVisible(true);
            this.dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PrisonerRegisterManagement().setVisible(true));
    }
}