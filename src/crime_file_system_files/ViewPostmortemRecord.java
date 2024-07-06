package crime_file_system_files;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jdbc.DatabaseConnection;
import java.util.Vector;

public class ViewPostmortemRecord extends JFrame {

    private JTable postmortemTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton nextButton;
    private JButton prevButton;
    private int currentPage = 1;
    private int totalPages = 1;
    private final int recordsPerPage = 10;

    public ViewPostmortemRecord() {
        // Frame settings
        setTitle("Crime File System - View Postmortem Records");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel and layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Table
        tableModel = new DefaultTableModel(new String[]{"Date of Death", "Cause of Death", "Doctor Name"}, 0);
        postmortemTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(postmortemTable);

        // Pagination panel
        JPanel paginationPanel = new JPanel(new FlowLayout());
        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");
        paginationPanel.add(prevButton);
        paginationPanel.add(nextButton);

        // Back button
        JButton backButton = new JButton("Back");

        // Add components to main panel
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(paginationPanel, BorderLayout.SOUTH);
        mainPanel.add(backButton, BorderLayout.PAGE_END);

        // Add main panel to frame
        add(mainPanel);

        // Load initial data
        loadPostmortemRecords();

        // Button actions
        searchButton.addActionListener(e -> searchPostmortemRecords());
        nextButton.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadPostmortemRecords();
            }
        });
        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadPostmortemRecords();
            }
        });
        backButton.addActionListener(e -> {
            new PostmortemDetailsManagement().setVisible(true);
            this.dispose();
        });
    }

    private void loadPostmortemRecords() {
        tableModel.setRowCount(0);  // Clear existing data
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM postmortem LIMIT ? OFFSET ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, recordsPerPage);
            preparedStatement.setInt(2, (currentPage - 1) * recordsPerPage);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Vector<String> row = new Vector<>();
                row.add(resultSet.getString("date_of_death"));
                row.add(resultSet.getString("cause_of_death"));
                row.add(resultSet.getString("doctor_name"));
                tableModel.addRow(row);
            }

            // Calculate total pages
            String countQuery = "SELECT COUNT(*) AS total FROM postmortem";
            ResultSet countResultSet = connection.createStatement().executeQuery(countQuery);
            if (countResultSet.next()) {
                int totalRecords = countResultSet.getInt("total");
                totalPages = (int) Math.ceil(totalRecords / (double) recordsPerPage);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading postmortem records: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        updatePaginationButtons();
    }

    private void searchPostmortemRecords() {
        String searchText = searchField.getText();
        tableModel.setRowCount(0);  // Clear existing data
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM postmortem WHERE cause_of_death LIKE ? OR doctor_name LIKE ? LIMIT ? OFFSET ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + searchText + "%");
            preparedStatement.setString(2, "%" + searchText + "%");
            preparedStatement.setInt(3, recordsPerPage);
            preparedStatement.setInt(4, (currentPage - 1) * recordsPerPage);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Vector<String> row = new Vector<>();
                row.add(resultSet.getString("date_of_death"));
                row.add(resultSet.getString("cause_of_death"));
                row.add(resultSet.getString("doctor_name"));
                tableModel.addRow(row);
            }

            // Calculate total pages
            String countQuery = "SELECT COUNT(*) AS total FROM postmortem WHERE cause_of_death LIKE ? OR doctor_name LIKE ?";
            PreparedStatement countPreparedStatement = connection.prepareStatement(countQuery);
            countPreparedStatement.setString(1, "%" + searchText + "%");
            countPreparedStatement.setString(2, "%" + searchText + "%");
            ResultSet countResultSet = countPreparedStatement.executeQuery();
            if (countResultSet.next()) {
                int totalRecords = countResultSet.getInt("total");
                totalPages = (int) Math.ceil(totalRecords / (double) recordsPerPage);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error searching postmortem records: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        updatePaginationButtons();
    }

    private void updatePaginationButtons() {
        prevButton.setEnabled(currentPage > 1);
        nextButton.setEnabled(currentPage < totalPages);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewPostmortemRecord().setVisible(true));
    }
}
