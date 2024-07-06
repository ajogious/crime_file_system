package crime_file_system_files;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jdbc.DatabaseConnection;

public class ViewCasesPage extends JFrame {
    private static final int CASES_PER_PAGE = 10; // Number of cases per page
    private int currentPage = 1; // Current page number
    private JPanel casesPanel;
    private JTextField searchField;
    private String searchQuery = ""; // Search query string

    public ViewCasesPage() {
        // Frame settings
        setTitle("Crime File System - View Cases");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel and layout
        casesPanel = new JPanel();
        casesPanel.setLayout(new BoxLayout(casesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(casesPanel);

        searchField = new JTextField(20);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchQuery = searchField.getText();
                currentPage = 1; // Reset to first page on new search
                loadCases();
            }
        });

        JButton previousButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        JButton backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(backButton);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        // Add components to frame
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        previousButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadCases();
            }
        });

        nextButton.addActionListener(e -> {
            currentPage++;
            loadCases();
        });

        backButton.addActionListener(e -> {
            new CaseHistoryDetailsManagement().setVisible(true);
            this.dispose();
        });

        // Load initial cases
        loadCases();
    }

    private void loadCases() {
        casesPanel.removeAll();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM cases WHERE case_details LIKE ? OR type_of_crime LIKE ? OR place_of_occurrence LIKE ? LIMIT ? OFFSET ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + searchQuery + "%");
            preparedStatement.setString(2, "%" + searchQuery + "%");
            preparedStatement.setString(3, "%" + searchQuery + "%");
            preparedStatement.setInt(4, CASES_PER_PAGE);
            preparedStatement.setInt(5, (currentPage - 1) * CASES_PER_PAGE);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String caseDetails = resultSet.getString("case_details");
                java.sql.Date dateOfOccurrence = resultSet.getDate("date_of_occurrence");
                String typeOfCrime = resultSet.getString("type_of_crime");
                String placeOfOccurrence = resultSet.getString("place_of_occurrence");

                JPanel casePanel = new JPanel(new GridLayout(4, 1));
                casePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                casePanel.add(new JLabel("Case Details: " + caseDetails));
                casePanel.add(new JLabel("Date of Occurrence: " + dateOfOccurrence));
                casePanel.add(new JLabel("Type of Crime: " + typeOfCrime));
                casePanel.add(new JLabel("Place of Occurrence: " + placeOfOccurrence));

                casesPanel.add(casePanel);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "An error occurred while retrieving case details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        casesPanel.revalidate();
        casesPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewCasesPage().setVisible(true));
    }
}
