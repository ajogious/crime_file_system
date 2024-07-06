package crime_file_system_files;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import jdbc.DatabaseConnection;

public class CaseHistoryDetailsManagement extends JFrame {

    public CaseHistoryDetailsManagement() {
        // Frame settings
        setTitle("Crime File System - Case History Details Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel and layout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Components
        JLabel caseDetailsLabel = new JLabel("Case Details:");
        JTextField caseDetailsText = new JTextField(20);

        JLabel dateLabel = new JLabel("Date of Occurrence:");

        // Date picker components
        SqlDateModel model = new SqlDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        JLabel crimeTypeLabel = new JLabel("Type of Crime:");
        JTextField crimeTypeText = new JTextField(20);
        JLabel placeLabel = new JLabel("Place of Occurrence:");
        JTextField placeText = new JTextField(20);
        JButton addButton = new JButton("Add Case");
        JButton viewCasesButton = new JButton("View Cases");
        JButton backButton = new JButton("Back");

        // Adding components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(caseDetailsLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(caseDetailsText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(dateLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(datePicker, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(crimeTypeLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(crimeTypeText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(placeLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(placeText, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(addButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(viewCasesButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(backButton, gbc);

        // Add panel to frame
        add(panel);

        // Button action
        addButton.addActionListener(e -> {
            String caseDetails = caseDetailsText.getText();
            java.sql.Date dateOfOccurrence = (java.sql.Date) datePicker.getModel().getValue();
            String typeOfCrime = crimeTypeText.getText();
            String placeOfOccurrence = placeText.getText();

            // Check if any field is empty
            if (caseDetails.isEmpty() || dateOfOccurrence == null || typeOfCrime.isEmpty() || placeOfOccurrence.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO cases (case_details, date_of_occurrence, type_of_crime, place_of_occurrence) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, caseDetails);
                preparedStatement.setDate(2, dateOfOccurrence);
                preparedStatement.setString(3, typeOfCrime);
                preparedStatement.setString(4, placeOfOccurrence);

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Case details added successfully");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "An error occurred while adding case details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        viewCasesButton.addActionListener(e -> {
            new ViewCasesPage().setVisible(true);
            this.dispose();
        });

        backButton.addActionListener(e -> {
            new AdminUserPage().setVisible(true);
            this.dispose();
        });
    }

}
