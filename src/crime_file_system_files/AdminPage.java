/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crime_file_system_files;

import javax.swing.*;
import java.awt.*;

public class AdminPage extends JFrame {

    public AdminPage() {
        // Frame settings
        setTitle("Crime File System - Admin Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel and layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        // Components
        JButton criminalButton = new JButton("Criminal Management");
        JButton firButton = new JButton("FIR Management");
        JButton caseHistoryButton = new JButton("Case History Management");
        JButton postmortemButton = new JButton("Postmortem Management");
        JButton prisonerButton = new JButton("Prisoner Management");
        JButton mostWantedButton = new JButton("Most Wanted Criminals Management");
        JButton viewComplaintStatusButton = new JButton("View Complaint Status");
        JButton complaintRegistration = new JButton("Complain Registration");

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
            this.dispose(); // close the current window
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminPage().setVisible(true));
    }
}
