import java.sql.*;

public class DatabaseHelper {

    public static void initDatabase() {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            // Tabel users
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE," +
                    "password TEXT)");

            // Tabel moods
            stmt.execute("CREATE TABLE IF NOT EXISTS moods (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user TEXT," +
                    "mood TEXT," +
                    "note TEXT," +
                    "date TEXT)");
        } catch (SQLException e) {
            System.err.println("Error saat menginisialisasi database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlite:moodtracker.db"; // Path ke database SQLite
        return DriverManager.getConnection(url);
    }

    public static void insertMood(String user, String mood, String note, String date) {
        String sql = "INSERT INTO moods (user, mood, note, date) VALUES (?, ?, ?, ?)";

        System.out.println("Query INSERT: " + sql);
        System.out.println("Parameter: user=" + user + ", mood=" + mood + ", note=" + note + ", date=" + date);

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user);
            stmt.setString(2, mood);
            stmt.setString(3, note);
            stmt.setString(4, date);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saat menyimpan mood: " + e.getMessage());
        }
    }
}