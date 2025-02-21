package finanztracker.main;

import java.sql.*;

public class DatabaseManager {

    private static Connection conn;

    // Statische Methode, um die Datenbankverbindung zu erhalten und initialisieren
    public static void connectToDatabase() {
        if (conn == null) {
            try {
                // SQLite-Datenbankverbindung herstellen
                conn = DriverManager.getConnection("jdbc:sqlite:finanztracker");

                // Erstelle Tabellen, falls sie noch nicht existieren
                String createEinnahmenTable = "CREATE TABLE IF NOT EXISTS Einnahmen (id INTEGER PRIMARY KEY, betrag REAL, kategorie TEXT, datum TEXT)";
                String createAusgabenTable = "CREATE TABLE IF NOT EXISTS Ausgaben (id INTEGER PRIMARY KEY, betrag REAL, kategorie TEXT, datum TEXT)";
                Statement stmt = conn.createStatement();
                stmt.execute(createEinnahmenTable);
                stmt.execute(createAusgabenTable);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Getter-Methode für die Verbindung
    public static Connection getConnection() {
        return conn;
    }


    public static void saveToDatabase(double amount, String purpose, String date, String category) {
        String tableName = category.equals("Einnahmen") ? "Einnahmen" : "Ausgaben";

        String insertSQL = "INSERT INTO " + tableName + " (betrag, kategorie, datum) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(insertSQL)) {
            pstmt.setDouble(1, amount);
            pstmt.setString(2, purpose);
            pstmt.setString(3, date);
            DatabaseManager.getConnection().setAutoCommit(false);
            pstmt.executeUpdate();
            DatabaseManager.getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
