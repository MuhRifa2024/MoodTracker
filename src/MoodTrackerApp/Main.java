import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        DatabaseHelper.initDatabase();
        showLoginScreen();
    }

    private static void showLoginScreen() {
        JFrame loginFrame = new JFrame("Login");

        JPanel panel = new JPanel(new BorderLayout());

        // Tambahkan logo
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon logoIcon = new ImageIcon("src/MoodTrackerApp/assets/logo.png");
        Image logoImage = logoIcon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(logoImage));
        panel.add(logoLabel, BorderLayout.NORTH);

        // Form login
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        formPanel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Username dan password tidak boleh kosong!");
                return;
            }

            if (AuthService.login(username, password)) {
                loginFrame.dispose();
                launchMainApp(username);
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Login gagal! Periksa username dan password Anda.");
            }
        });

        registerBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Username dan password tidak boleh kosong!");
                return;
            }

            if (AuthService.register(username, password)) {
                JOptionPane.showMessageDialog(loginFrame, "Registrasi berhasil!");
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Registrasi gagal! Username mungkin sudah digunakan.");
            }
        });

        formPanel.add(loginBtn);
        formPanel.add(registerBtn);

        panel.add(formPanel, BorderLayout.CENTER);

        loginFrame.add(panel);
        loginFrame.pack();
        loginFrame.setVisible(true);
    }

    private static void launchMainApp(String username) {
        JFrame frame = new JFrame("Mood Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        MoodPanel moodPanel = new MoodPanel(username); // Kirim username ke MoodPanel
        frame.add(moodPanel);

        frame.setVisible(true);
    }
}