import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableModel;

public class MoodPanel extends JPanel {
    private JTextArea catatan;
    private String currentUser;

    public MoodPanel(String currentUser) {
        this.currentUser = currentUser; // Simpan username pengguna
        setLayout(new BorderLayout());

        // Tambahkan logo
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon logoIcon = new ImageIcon("src/MoodTrackerApp/assets/logo.png"); // Path ke logo
        if (logoIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.err.println("Logo tidak ditemukan di path: src/MoodTrackerApp/assets/logo.png");
        }
        Image logoImage = logoIcon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(logoImage));
        add(logoLabel, BorderLayout.NORTH);

        // Tambahkan tab untuk input mood dan riwayat mood
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Catat Mood", createInputPanel());
        tabbedPane.addTab("Riwayat Mood", createHistoryPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JLabel moodLabel = new JLabel("Bagaimana perasaanmu hari ini?");
        moodLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moodLabel.setAlignmentX(CENTER_ALIGNMENT);

        JSlider moodSlider = new JSlider(0, 100, 50);
        moodSlider.setPaintTicks(true);
        moodSlider.setPaintLabels(false);
        moodSlider.setMajorTickSpacing(50);
        moodSlider.setMinorTickSpacing(10);

        catatan = new JTextArea(); // Inisialisasi variabel catatan
        catatan.setLineWrap(true);
        catatan.setWrapStyleWord(true);
        catatan.setMaximumSize(new Dimension(400, 100));
        catatan.setAlignmentX(CENTER_ALIGNMENT);

        JButton saveButton = new JButton("Simpan");
        saveButton.setAlignmentX(CENTER_ALIGNMENT);
        saveButton.addActionListener(e -> {
            int moodValue = moodSlider.getValue();
            String note = catatan.getText();
            saveMood(String.valueOf(moodValue));
            JOptionPane.showMessageDialog(this, "Mood: " + moodValue + "\nCatatan: " + note);
        });

        inputPanel.add(Box.createVerticalStrut(20)); // Spasi
        inputPanel.add(moodLabel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(moodSlider);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(catatan);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(saveButton);

        return inputPanel;
    }

    private JPanel createHistoryPanel() {
        JPanel historyPanel = new JPanel(new BorderLayout());

        JLabel historyLabel = new JLabel("Riwayat Mood");
        historyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        historyLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTable historyTable = new JTable(); // Tabel untuk menampilkan riwayat mood
        JScrollPane scrollPane = new JScrollPane(historyTable);

        historyPanel.add(historyLabel, BorderLayout.NORTH);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        return historyPanel;
    }

    private void saveMood(String mood) {
        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
        System.out.println("Menyimpan mood: " + mood + ", catatan: " + catatan.getText() + ", tanggal: " + date);
        DatabaseHelper.insertMood(currentUser, mood, catatan.getText(), date);
        JOptionPane.showMessageDialog(this, "Mood disimpan: " + mood);
        catatan.setText("");
    }

    private void refreshTable(DefaultTableModel model) {
        refreshTable(model, "Semua");
    }

    private void refreshTable(DefaultTableModel model, String filter) {
        model.setRowCount(0); // Hapus data lama dari tabel
        String sql = "SELECT date, mood, note FROM moods WHERE user = ?";

        if (!filter.equals("Semua")) {
            sql += " AND mood = ?";
        }
        sql += " ORDER BY date DESC";

        System.out.println("Query SQL: " + sql);
        System.out.println("Parameter user: " + currentUser);
        if (!filter.equals("Semua")) {
            System.out.println("Parameter filter: " + filter);
        }

        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, currentUser);
            if (!filter.equals("Semua")) {
                stmt.setString(2, filter);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Data dari database:");
                while (rs.next()) {
                    System.out.println(
                            rs.getString("date") + " - " + rs.getString("mood") + " - " + rs.getString("note"));
                    model.addRow(new Object[] {
                            rs.getString("date"),
                            rs.getString("mood"),
                            rs.getString("note")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}