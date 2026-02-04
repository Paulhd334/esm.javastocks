package JavaStocks.database;

import JavaStocks.utils.Constants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Chargement du driver PostgreSQL
                Class.forName("org.postgresql.Driver");
                
                // Établissement de la connexion
                connection = DriverManager.getConnection(
                    Constants.DB_URL,
                    Constants.DB_USER, 
                    Constants.DB_PASSWORD
                );
                
                // Configuration
                connection.setAutoCommit(true);
                System.out.println("✅ Connexion PostgreSQL établie: " + Constants.DB_URL);
                
            } catch (ClassNotFoundException e) {
                throw new SQLException("❌ Driver PostgreSQL non trouvé. Ajoutez postgresql.jar dans lib/");
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔌 Connexion fermée");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur fermeture connexion: " + e.getMessage());
        }
    }
    
    // Méthode de test de connexion
    public static boolean testConnection() {
        try {
            Connection testConn = getConnection();
            if (testConn != null && !testConn.isClosed()) {
                System.out.println("✅ Test connexion: OK");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Test connexion: ÉCHEC - " + e.getMessage());
        }
        return false;
    }
}