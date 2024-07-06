package crime_file_system_files;

import javax.swing.SwingUtilities;

public class MyApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}
