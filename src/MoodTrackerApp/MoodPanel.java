import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MoodPanel extends JPanel {
    private JTextArea catatan;
    private String currentUser;

    public MoodPanel(String currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());

        // Custom background gradient
        setOpaque(false);

        // Panel atas: logo, judul, tanggal
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);

        // Logo kiri atas
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        logoPanel.setOpaque(false);
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon("src/MoodTrackerApp/assets/logo.png");
        Image logoImage = logoIcon.getImage().getScaledInstance(250, 100, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(logoImage));
        logoPanel.add(logoLabel);
        topPanel.add(logoPanel);

        // Judul besar
        JLabel titleLabel = new JLabel("Pencatat Mood Harian");
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(titleLabel);

        // Tanggal
        JLabel dateLabel = new JLabel(new SimpleDateFormat("EEEE, d MMM yyyy").format(new Date()));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(dateLabel);

        add(topPanel, BorderLayout.NORTH);

        // Panel tengah: slider, input, tombol
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        // Slider mood
        JSlider moodSlider = new JSlider(0, 100, 50);
        moodSlider.setMajorTickSpacing(50);
        moodSlider.setMinorTickSpacing(10);
        moodSlider.setPaintTicks(true);
        moodSlider.setPaintLabels(false);
        moodSlider.setMaximumSize(new Dimension(400, 40));
        moodSlider.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label bawah slider
        JPanel sliderLabelPanel = new JPanel(new BorderLayout());
        sliderLabelPanel.setOpaque(false);
        JLabel badLabel = new JLabel("Bad");
        JLabel hmmLabel = new JLabel("Hmmmm", SwingConstants.CENTER);
        JLabel goodLabel = new JLabel("Good", SwingConstants.RIGHT);
        sliderLabelPanel.add(badLabel, BorderLayout.WEST);
        sliderLabelPanel.add(hmmLabel, BorderLayout.CENTER);
        sliderLabelPanel.add(goodLabel, BorderLayout.EAST);
        sliderLabelPanel.setMaximumSize(new Dimension(400, 20));

        // Input catatan
        catatan = new JTextArea(2, 20);
        catatan.setFont(new Font("Arial", Font.PLAIN, 16));
        catatan.setLineWrap(true);
        catatan.setWrapStyleWord(true);
        catatan.setMaximumSize(new Dimension(350, 40));
        catatan.setAlignmentX(Component.CENTER_ALIGNMENT);
        catatan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        catatan.setText("Tulis Perasaanmu hari ini.....");

        // Tombol simpan
        JButton saveButton = new JButton("Simpan");
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.setBackground(new Color(30, 60, 120));
        saveButton.setForeground(Color.WHITE);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setMaximumSize(new Dimension(200, 40));
        saveButton.addActionListener(e -> {
            int moodValue = moodSlider.getValue();
            String note = catatan.getText();
            saveMood(String.valueOf(moodValue));
            JOptionPane.showMessageDialog(this, "Mood: " + moodValue + "\nCatatan: " + note);
        });

        // Tambahkan komponen ke centerPanel
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(moodSlider);
        centerPanel.add(sliderLabelPanel);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(catatan);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(saveButton);

        add(centerPanel, BorderLayout.CENTER);
    }

    // Custom paintComponent untuk background gradient
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        Color color1 = new Color(120, 180, 255);
        Color color2 = new Color(200, 220, 255);
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }

    private void saveMood(String mood) {
        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
        DatabaseHelper.insertMood(currentUser, mood, catatan.getText(), date);
        catatan.setText("");
    }
}