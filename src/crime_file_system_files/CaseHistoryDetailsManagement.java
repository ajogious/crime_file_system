package crime_file_system_files;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import java.util.Properties;
import jdbc.DatabaseConnection;

public class CaseHistoryDetailsManagement extends JFrame {

    public CaseHistoryDetailsManagement() {
        // Frame settings
        setTitle("Crime File System - Case History Details Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel and layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        // Components
        JLabel caseDetailsLabel = new JLabel("Case Details:");
        JTextField caseDetailsText = new JTextField("Enter details of case(s): ");
        
        JLabel dateLabel = new JLabel("Date of Occurrence:");

        // Date picker components
        SqlDateModel model = new SqlDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        
        JLabel crimeTypeLabel = new JLabel("Type of Crime: ");
        JTextField crimeTypeText = new JTextField("Enter type(s) of crime:");
        JLabel placeLabel = new JLabel("Place of Occurrence:");
        JTextField placeText = new JTextField("Enter place of occurrence: ");
        JButton addButton = new JButton("Add Case");
        JButton backButton = new JButton("Back to Admin Page");

        // Adding components to panel
        panel.add(caseDetailsLabel);
        panel.add(caseDetailsText);
        panel.add(dateLabel);
        panel.add(datePicker);
        panel.add(crimeTypeLabel);
        panel.add(crimeTypeText);
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
            String caseDetails = caseDetailsText.getText();
            java.sql.Date dateOfOccurrence = (java.sql.Date) datePicker.getModel().getValue();
            String typeOfCrime = crimeTypeText.getText();
            String placeOfOccurrence = placeText.getText();

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
                ex.printStackTrace();
            }
        });
        
        backButton.addActionListener(e -> {
            new AdminPage().setVisible(true);
            this.dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CaseHistoryDetailsManagement().setVisible(true));
    }
}