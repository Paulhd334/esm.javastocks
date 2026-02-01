package JavaStocks;

import JavaStocks.ui.MenuManager;
import JavaStocks.database.DatabaseConnection;
import JavaStocks.database.DatabaseInitializer;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("    APPLICATION JAVA STOCKS v3.0     ");
        System.out.println("======================================\n");
        
        try {
            // Initialisation de la base de données
            Connection connection = DatabaseConnection.getConnection();
            DatabaseInitializer.initializeDatabase(connection);
            
            // Lancement du menu principal
            MenuManager menuManager = new MenuManager(connection);
            menuManager.showMainMenu();
            
        } catch (Exception e) {
            System.err.println("Erreur lors du démarrage de l'application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}