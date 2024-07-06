package crime_file_system_files;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminUserPage extends JFrame {

    public AdminUserPage() {
        // Frame settings
        setTitle("Crime File System - Admin And User Page");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel and layout
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new GridLayout(8, 1, 10, 10));

        // Components
        JButton criminalButton = createStyledButton("Criminal Management");
        JButton firButton = createStyledButton("FIR Management");
        JButton caseHistoryButton = createStyledButton("Case History Management");
        JButton postmortemButton = createStyledButton("Postmortem Management");
        JButton prisonerButton = createStyledButton("Prisoner Management");
        JButton mostWantedButton = createStyledButton("Most Wanted Criminals Management");
        JButton viewComplaintStatusButton = createStyledButton("View Complaint Status");
        JButton complaintRegistration = createStyledButton("Complain Registration");

        // Adding components to panel
        panel.add(criminalButton);
        panel.add(firButton);
        panel.add(caseHistoryButton);
        panel.add(postmortemButton);
        panel.add(prisonerButton);
        panel.add(mostWantedButton);
        panel.add(viewComplaintStatusButton);
        panel.add(complaintRegistration);

        // Add panel to frame
        add(panel);

        // Button actions
        criminalButton.addActionListener(e -> {
            new CriminalRegisterManagement().setVisible(true);
            this.dispose();
        });

        firButton.addActionListener(e -> {
            new FIRManagement().setVisible(true);
            this.dispose();
        });

        caseHistoryButton.addActionListener(e -> {
            new CaseHistoryDetailsManagement().setVisible(true);
            this.dispose();
        });

        postmortemButton.addActionListener(e -> {
            new PostmortemDetailsManagement().setVisible(true);
            this.dispose();
        });

        prisonerButton.addActionListener(e -> {
            new PrisonerRegisterManagement().setVisible(true);
            this.dispose();
        });

        mostWantedButton.addActionListener(e -> {
            new MostWantedCriminalsManagement().setVisible(true);
            this.dispose();
        });

        viewComplaintStatusButton.addActionListener(e -> {
            new ViewComplaintStatus().setVisible(true);
            this.dispose();
        });

        complaintRegistration.addActionListener(e -> {
            new ComplaintRegistration().setVisible(true);
            this.dispose();
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(60, 63, 65));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        return button;
    }

}
