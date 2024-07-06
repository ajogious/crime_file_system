package crime_file_system_files;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jdbc.DatabaseConnection;

public class ViewMostWantedCriminalRecord extends JFrame {

    private JTextField searchField;
    private JButton backButton;
    private JButton previousButton;
    private JButton nextButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private int currentPage = 1;
    private int totalPages = 1;
    private int recordsPerPage = 10;

    public ViewMostWantedCriminalRecord() {
        setTitle("View Most Wanted Criminal Record");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        searchField = new JTextField(20);
        searchField.addActionListener(e -> loadRecords());

        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new MostWantedCriminalsManagement().setVisible(true);
            this.dispose();
        });

        previousButton = new JButton("Previous");
        previousButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadRecords();
            }
        });

        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadRecords();
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(previousButton);
        topPanel.add(nextButton);
        topPanel.add(backButton);
        
        tableModel = new DefaultTableModel(new Object[]{"Criminal ID", "Reason for Wanted"}, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        loadRecords();
    }

    private void loadRecords() {
        String searchQuery = searchField.getText();
        String queryCount = "SELECT COUNT(*) FROM most_wanted WHERE criminal_id LIKE ? OR reason_for_wanted LIKE ?";
        String query = "SELECT * FROM most_wanted WHERE criminal_id LIKE ? OR reason_for_wanted LIKE ? LIMIT ? OFFSET ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Fetch total count of records
            PreparedStatement countStmt = connection.prepareStatement(queryCount);
            countStmt.setString(1, "%" + searchQuery + "%");
            countStmt.setString(2, "%" + searchQuery + "%");
            ResultSet rsCount = countStmt.executeQuery();
            if (rsCount.next()) {
                int totalRecords = rsCount.getInt(1);
                totalPages = (totalRecords + recordsPerPage - 1) / recordsPerPage;
            }
            
            // Fetch filtered and paginated records
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, "%" + searchQuery + "%");
            stmt.setString(2, "%" + searchQuery + "%");
            stmt.setInt(3, recordsPerPage);
            stmt.setInt(4, (currentPage - 1) * recordsPerPage);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0); // Clear current table content
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getString("criminal_id"), rs.getString("reason_for_wanted")});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load records", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewMostWantedCriminalRecord().setVisible(true));
    }
}