package JavaStocks;

import JavaStocks.ui.MenuManager;
import JavaStocks.database.DatabaseConnection;
import JavaStocks.database.DatabaseInitializer;
import JavaStocks.services.ReapprovisionnementService;
import JavaStocks.dao.FournisseurDAO;
import JavaStocks.dao.PointLivraisonDAO;
import JavaStocks.dao.DemandeReapprovisionnementDAO;
import JavaStocks.dao.ArticleDAO;
import JavaStocks.dao.CoureurDAO;
import JavaStocks.dao.TypeEpreuveDAO;
import JavaStocks.dao.ReservationDAO;
import JavaStocks.dao.ReservationEnAttenteDAO;

import java.sql.Connection;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("    APPLICATION JAVA STOCKS v3.0     ");
        System.out.println("======================================\n");
        
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;
        
        try {
            // Initialisation de la base de données
            connection = DatabaseConnection.getConnection();
            DatabaseInitializer.initializeDatabase(connection);
            
            // Initialisation des DAO
            ArticleDAO articleDAO = new ArticleDAO(connection);
            CoureurDAO coureurDAO = new CoureurDAO(connection);
            TypeEpreuveDAO typeEpreuveDAO = new TypeEpreuveDAO(connection);
            ReservationDAO reservationDAO = new ReservationDAO(connection);
            ReservationEnAttenteDAO reservationEnAttenteDAO = new ReservationEnAttenteDAO(connection);
            FournisseurDAO fournisseurDAO = new FournisseurDAO(connection);
            PointLivraisonDAO pointLivraisonDAO = new PointLivraisonDAO(connection);
            DemandeReapprovisionnementDAO demandeReapproDAO = new DemandeReapprovisionnementDAO(connection);
            
            // Initialisation des services
            ReapprovisionnementService reapproService = new ReapprovisionnementService(
                scanner, fournisseurDAO, pointLivraisonDAO, demandeReapproDAO, articleDAO
            );
            
            // Lancement du menu principal avec tous les services
            MenuManager menuManager = new MenuManager(
                connection, scanner, 
                articleDAO, coureurDAO, typeEpreuveDAO, reservationDAO, reservationEnAttenteDAO,
                reapproService
            );
            
            menuManager.showMainMenu();
            
        } catch (Exception e) {
            System.err.println("Erreur lors du démarrage de l'application: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            // Fermer la connexion si nécessaire
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
                }
            }
        }
    }
}