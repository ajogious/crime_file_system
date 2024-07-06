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
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("FIR Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Components
        JLabel crimeDetailsLabel = new JLabel("Crime Details:");
        JTextField crimeDetailsText = new JTextField(20);

        JLabel dateLabel = new JLabel("Date of Occurrence:");
        SqlDateModel model = new SqlDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        JLabel placeLabel = new JLabel("Place of Occurrence:");
        JTextField placeText = new JTextField(20);

        JButton addButton = new JButton("Add FIR");
        JButton backButton = new JButton("Back");

        // Adding components to panel with layout constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(crimeDetailsLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(crimeDetailsText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(datePicker, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(placeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(placeText, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(addButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(backButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Add panel to frame
        add(mainPanel);

        // Button action
        addButton.addActionListener(e -> {
            String crimeDetails = crimeDetailsText.getText();
            java.sql.Date dateOfOccurrence = (java.sql.Date) datePicker.getModel().getValue();
            String placeOfOccurrence = placeText.getText();

            // Validate fields
            if (crimeDetails.isEmpty() || dateOfOccurrence == null || placeOfOccurrence.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO firs (crime_details, date_of_occurrence, place_of_occurrence) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, crimeDetails);
                preparedStatement.setDate(2, dateOfOccurrence);
                preparedStatement.setString(3, placeOfOccurrence);

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "FIR added successfully");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "An error occurred while adding the FIR: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        backButton.addActionListener(e -> {
            new AdminUserPage().setVisible(true);
            this.dispose();
        });
    }

    public static void main(String[] args) {
        // Use a more modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new FIRManagement().setVisible(true));
    }
}
