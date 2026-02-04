package JavaStocks;

import JavaStocks.ui.MenuManager;
import JavaStocks.database.DatabaseConnection;
import JavaStocks.database.DatabaseInitializer;
import JavaStocks.database.DonneesTest;
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
import java.util.Random; // <-- AJOUTE IMPORT POUR RANDOM

public class Main {
    
    // MÉTHODE CAPTCHA POUR LA CLASSE Main
    public static boolean validerCaptcha() {
        Random random = new Random();
        Scanner captchaScanner = new Scanner(System.in);
        
        int num1 = random.nextInt(10) + 1;
        int num2 = random.nextInt(10) + 1;
        int somme = num1 + num2;
        
        System.out.println("\n=== VÉRIFICATION HUMAINE ===");
        System.out.println("Pour accéder à JavaStocks, résolvez :");
        System.out.println(num1 + " + " + num2 + " = ?");
        System.out.print("> Réponse : ");
        
        try {
            int reponse = captchaScanner.nextInt();
            if (reponse == somme) {
                System.out.println(" CAPTCHA validé. Bienvenue dans JavaStocks.\n");
                return true;
            } else {
                System.out.println(" Réponse incorrecte. Accès refusé.\n");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Saisie invalide. CAPTCHA échoué.\n");
            return false;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("    APPLICATION JAVA STOCKS v3.0     ");
        System.out.println("======================================\n");
        
        // ============ AJOUT DU CAPTCHA ICI ============
        System.out.println("> Vérification de sécurité...");
        if (!validerCaptcha()) {
            System.out.println("Accès refusé. Fin du programme.");
            System.exit(0);
        }
        // ==============================================
        
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;
        
        try {
            // 1. CONNEXION A LA BASE DE DONNEES
            System.out.println("> Connexion a PostgreSQL...");
            connection = DatabaseConnection.getConnection();
            System.out.println("OK - Connexion etablie\n");
            
            // 2. CREATION DES TABLES
            System.out.println("> Initialisation de la base de donnees...");
            DatabaseInitializer.initializeDatabase(connection);
            System.out.println("OK - Base de donnees initialisee\n");
            
            // 3. INSERTION DES DONNEES DE TEST
            System.out.println("> Insertion des donnees de test...");
            DonneesTest.insererDonneesTest(connection);
            System.out.println("OK - Donnees de test inserees\n");
            
            // 4. INITIALISATION DES DAO
            System.out.println("> Initialisation des services...");
            ArticleDAO articleDAO = new ArticleDAO(connection);
            CoureurDAO coureurDAO = new CoureurDAO(connection);
            TypeEpreuveDAO typeEpreuveDAO = new TypeEpreuveDAO(connection);
            ReservationDAO reservationDAO = new ReservationDAO(connection);
            ReservationEnAttenteDAO reservationEnAttenteDAO = new ReservationEnAttenteDAO(connection);
            FournisseurDAO fournisseurDAO = new FournisseurDAO(connection);
            PointLivraisonDAO pointLivraisonDAO = new PointLivraisonDAO(connection);
            DemandeReapprovisionnementDAO demandeReapproDAO = new DemandeReapprovisionnementDAO(connection);
            
            // 5. INITIALISATION DES SERVICES
            ReapprovisionnementService reapproService = new ReapprovisionnementService(
                scanner, fournisseurDAO, pointLivraisonDAO, demandeReapproDAO, articleDAO
            );
            
            // 6. LANCEMENT DE L'APPLICATION
            System.out.println("> Lancement de l'application...\n");
            MenuManager menuManager = new MenuManager(
                connection, scanner, 
                articleDAO, coureurDAO, typeEpreuveDAO, reservationDAO, reservationEnAttenteDAO,
                reapproService
            );
            
            menuManager.showMainMenu();
            
        } catch (Exception e) {
            System.err.println("ERREUR lors du demarrage: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 7. NETTOYAGE
            if (scanner != null) {
                scanner.close();
            }
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("> Connexion fermee");
                } catch (Exception e) {
                    System.err.println("Erreur fermeture connexion: " + e.getMessage());
                }
            }
        }
    }
}

