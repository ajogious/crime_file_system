package crime_file_system_files;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import jdbc.DatabaseConnection;

public class ViewCasesPage extends JFrame {

    private JTextField searchField;
    private JButton searchButton;
    private JButton backButton;
    private JTable casesTable;
    private DefaultTableModel tableModel;
    private int currentPage = 1;
    private int totalRows = 0;
    private int pageSize = 10;
    private JLabel pageLabel;
    private JButton prevButton;
    private JButton nextButton;

    public ViewCasesPage() {
        // Frame settings
        setTitle("View Cases");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel and layout
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        backButton = new JButton("Back");

        casesTable = new JTable();
        tableModel = new DefaultTableModel(new Object[]{"Case ID", "Case Details", "Date of Occurrence", "Crime Type", "Place of Occurrence"}, 0);
        casesTable.setModel(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(casesTable);

        topPanel.add(searchField);
        topPanel.add(searchButton);

        paginationPanel.add(backButton);
        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");
        pageLabel = new JLabel("Page: " + currentPage);

        paginationPanel.add(prevButton);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextButton);

        bottomPanel.add(paginationPanel, BorderLayout.NORTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Add panel to frame
        add(panel);

        // Load cases
        loadCases("");

        // Button actions
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            currentPage = 1;
            loadCases(searchTerm);
        });

        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadCases(searchField.getText().trim());
            }
        });

        nextButton.addActionListener(e -> {
            if (currentPage * pageSize < totalRows) {
                currentPage++;
                loadCases(searchField.getText().trim());
            }
        });

        backButton.addActionListener(e -> {
            new CaseHistoryDetailsManagement().setVisible(true);
            this.dispose();
        });
    }

    private void loadCases(String searchTerm) {
        tableModel.setRowCount(0);
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT SQL_CALC_FOUND_ROWS * FROM cases WHERE case_details LIKE '%" + searchTerm + "%' "
                    + "OR type_of_crime LIKE '%" + searchTerm + "%' "
                    + "OR place_of_occurrence LIKE '%" + searchTerm + "%' "
                    + "LIMIT " + (currentPage - 1) * pageSize + ", " + pageSize;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                    resultSet.getInt("case_id"),
                    resultSet.getString("case_details"),
                    resultSet.getDate("date_of_occurrence"),
                    resultSet.getString("type_of_crime"),
                    resultSet.getString("place_of_occurrence")
                });
            }

            ResultSet countResultSet = statement.executeQuery("SELECT FOUND_ROWS()");
            if (countResultSet.next()) {
                totalRows = countResultSet.getInt(1);
            }
            updatePageLabel();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "An error occurred while retrieving cases: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updatePageLabel() {
        pageLabel.setText("Page: " + currentPage + " / " + ((totalRows / pageSize) + 1));
    }

}
