// Removed package declaration

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.Color;
import java.sql.*;

public class ChartUtils {
    public static JFreeChart createMoodChart(String username) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        String sql = "SELECT mood, COUNT(*) as count FROM moods WHERE user = ? GROUP BY mood";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dataset.setValue(rs.getString("mood"), rs.getInt("count"));
            }

            if (dataset.getItemCount() == 0) {
                System.err.println("Tidak ada data mood untuk pengguna: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil data untuk chart: " + e.getMessage());
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Distribusi Mood Anda", dataset, true, true, false);

        @SuppressWarnings("rawtypes")
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Senang", Color.GREEN);
        plot.setSectionPaint("Biasa", Color.YELLOW);
        plot.setSectionPaint("Marah", Color.RED);

        return chart;
    }
}