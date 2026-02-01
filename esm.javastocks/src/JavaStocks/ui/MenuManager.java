package JavaStocks.ui;

import JavaStocks.utils.Constants;
import JavaStocks.services.*;
import JavaStocks.database.DatabaseInitializer;
import java.sql.Connection;
import java.util.Scanner;

public class MenuManager {
    private Connection connection;
    private Scanner scanner;
    private ArticleService articleService;
    private CoureurService coureurService;
    private TypeEpreuveService typeEpreuveService;
    private ReservationService reservationService;
    private ReapprovisionnementService reapproService;
    
    public MenuManager(Connection connection) {
        this.connection = connection;
        this.scanner = new Scanner(System.in);
        this.articleService = new ArticleService(connection);
        this.coureurService = new CoureurService(connection);
        this.typeEpreuveService = new TypeEpreuveService(connection);
        this.reservationService = new ReservationService(connection, articleService, coureurService, typeEpreuveService);
        this.reapproService = new ReapprovisionnementService(connection);
    }
    
    public void showMainMenu() {
        boolean continuer = true;
        
        while (continuer) {
            afficherEntete("MENU PRINCIPAL");
            
            for (String option : Constants.MAIN_MENU) {
                System.out.println("  " + option);
            }
            
            System.out.print("\nVotre choix (1-8): ");
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    gererArticles();
                    break;
                case "2":
                    gererCoureurs();
                    break;
                case "3":
                    gererTypesEpreuve();
                    break;
                case "4":
                    gererReservations();
                    break;
                case "5":
                    gererRupturesEtAttentes();
                    break;
                case "6":
                    consulterHistorique();
                    break;
                case "7":
                    gererReapprovisionnement();
                    break;
                case "8":
                    System.out.println("\nAu revoir !");
                    continuer = false;
                    break;
                default:
                    System.out.println(">> Choix invalide!");
                    pause();
            }
        }
    }
    
    private void gererArticles() {
        boolean retour = false;
        
        while (!retour) {
            afficherEntete("GESTION DES ARTICLES");
            System.out.println("1. Ajouter un article");
            System.out.println("2. Modifier un article");
            System.out.println("3. Consulter un article");
            System.out.println("4. Suppression logique");
            System.out.println("5. Lister tous les articles");
            System.out.println("6. Retour menu principal");
            
            System.out.print("\nVotre choix (1-6): ");
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    articleService.creer();
                    pause();
                    break;
                case "2":
                    articleService.modifier();
                    pause();
                    break;
                case "3":
                    articleService.consulter();
                    pause();
                    break;
                case "4":
                    articleService.supprimerLogique();
                    pause();
                    break;
                case "5":
                    articleService.listerTous();
                    pause();
                    break;
                case "6":
                    retour = true;
                    break;
                default:
                    System.out.println(">> Choix invalide!");
                    pause();
            }
        }
    }
    
    private void gererCoureurs() {
        boolean retour = false;
        
        while (!retour) {
            afficherEntete("GESTION DES COUREURS");
            System.out.println("1. Ajouter un coureur");
            System.out.println("2. Modifier un coureur");
            System.out.println("3. Consulter un coureur");
            System.out.println("4. Lister tous les coureurs");
            System.out.println("5. Retour menu principal");
            
            System.out.print("\nVotre choix (1-5): ");
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    coureurService.creer();
                    pause();
                    break;
                case "2":
                    coureurService.modifier();
                    pause();
                    break;
                case "3":
                    coureurService.consulter();
                    pause();
                    break;
                case "4":
                    coureurService.listerTous();
                    pause();
                    break;
                case "5":
                    retour = true;
                    break;
                default:
                    System.out.println(">> Choix invalide!");
                    pause();
            }
        }
    }
    
    private void gererTypesEpreuve() {
        boolean retour = false;
        
        while (!retour) {
            afficherEntete("GESTION DES TYPES D'ÉPREUVE");
            System.out.println("1. Ajouter un type d'épreuve");
            System.out.println("2. Modifier un type d'épreuve");
            System.out.println("3. Consulter un type d'épreuve");
            System.out.println("4. Lister tous les types");
            System.out.println("5. Retour menu principal");
            
            System.out.print("\nVotre choix (1-5): ");
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    typeEpreuveService.creer();
                    pause();
                    break;
                case "2":
                    typeEpreuveService.modifier();
                    pause();
                    break;
                case "3":
                    typeEpreuveService.consulter();
                    pause();
                    break;
                case "4":
                    typeEpreuveService.listerTous();
                    pause();
                    break;
                case "5":
                    retour = true;
                    break;
                default:
                    System.out.println(">> Choix invalide!");
                    pause();
            }
        }
    }
    
    private void gererReservations() {
        boolean retour = false;
        
        while (!retour) {
            afficherEntete("GESTION DES RÉSERVATIONS");
            System.out.println("1. Créer une réservation");
            System.out.println("2. Modifier une réservation");
            System.out.println("3. Consulter une réservation");
            System.out.println("4. Annuler une réservation");
            System.out.println("5. Valider réservation en attente");
            System.out.println("6. Retour menu principal");
            
            System.out.print("\nVotre choix (1-6): ");
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    reservationService.creer();
                    pause();
                    break;
                case "2":
                    reservationService.modifier();
                    pause();
                    break;
                case "3":
                    reservationService.consulter();
                    pause();
                    break;
                case "4":
                    reservationService.annuler();
                    pause();
                    break;
                case "5":
                    reservationService.validerEnAttente();
                    pause();
                    break;
                case "6":
                    retour = true;
                    break;
                default:
                    System.out.println(">> Choix invalide!");
                    pause();
            }
        }
    }
    
    private void gererRupturesEtAttentes() {
        boolean retour = false;
        
        while (!retour) {
            afficherEntete("RUPTURES & RÉSERVATIONS EN ATTENTE");
            System.out.println("1. Consulter les produits en rupture");
            System.out.println("2. Consulter les réservations en attente");
            System.out.println("3. Retour menu principal");
            
            System.out.print("\nVotre choix (1-3): ");
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    articleService.listerRuptures();
                    pause();
                    break;
                case "2":
                    reservationService.listerEnAttente();
                    pause();
                    break;
                case "3":
                    retour = true;
                    break;
                default:
                    System.out.println(">> Choix invalide!");
                    pause();
            }
        }
    }
    
    private void consulterHistorique() {
        boolean retour = false;
        
        while (!retour) {
            afficherEntete("HISTORIQUE");
            System.out.println("1. Réservations par date");
            System.out.println("2. Réservations par coureur");
            System.out.println("3. Réservations par type d'épreuve");
            System.out.println("4. Quantités réservées par article");
            System.out.println("5. Retour menu principal");
            
            System.out.print("\nVotre choix (1-5): ");
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    reservationService.historiqueParDate();
                    pause();
                    break;
                case "2":
                    reservationService.historiqueParCoureur();
                    pause();
                    break;
                case "3":
                    reservationService.historiqueParTypeEpreuve();
                    pause();
                    break;
                case "4":
                    reservationService.historiqueQuantites();
                    pause();
                    break;
                case "5":
                    retour = true;
                    break;
                default:
                    System.out.println(">> Choix invalide!");
                    pause();
            }
        }
    }
    
    private void gererReapprovisionnement() {
        boolean retour = false;
        
        while (!retour) {
            afficherEntete("GESTION RÉAPPROVISIONNEMENT");
            System.out.println("1. Créer une demande");
            System.out.println("2. Gérer les fournisseurs");
            System.out.println("3. Gérer les points de livraison");
            System.out.println("4. Consulter les demandes");
            System.out.println("5. Retour menu principal");
            
            System.out.print("\nVotre choix (1-5): ");
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    reapproService.creerDemande();
                    pause();
                    break;
                case "2":
                    gererFournisseurs();
                    pause();
                    break;
                case "3":
                    gererPointsLivraison();
                    pause();
                    break;
                case "4":
                    reapproService.listerDemandes();
                    pause();
                    break;
                case "5":
                    retour = true;
                    break;
                default:
                    System.out.println(">> Choix invalide!");
                    pause();
            }
        }
    }
    
    private void gererFournisseurs() {
        boolean retour = false;
        
        while (!retour) {
            afficherEntete("GESTION FOURNISSEURS");
            System.out.println("1. Ajouter un fournisseur");
            System.out.println("2. Modifier un fournisseur");
            System.out.println("3. Consulter un fournisseur");
            System.out.println("4. Lister tous les fournisseurs");
            System.out.println("5. Retour");
            
            System.out.print("\nVotre choix (1-5): ");
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    reapproService.creerFournisseur();
                    pause();
                    break;
                case "2":
                    reapproService.modifierFournisseur();
                    pause();
                    break;
                case "3":
                    reapproService.consulterFournisseur();
                    pause();
                    break;
                case "4":
                    reapproService.listerFournisseurs();
                    pause();
                    break;
                case "5":
                    retour = true;
                    break;
                default:
                    System.out.println(">> Choix invalide!");
                    pause();
            }
        }
    }
    
    private void gererPointsLivraison() {
        boolean retour = false;
        
        while (!retour) {
            afficherEntete("GESTION POINTS DE LIVRAISON");
            System.out.println("1. Ajouter un point de livraison");
            System.out.println("2. Modifier un point de livraison");
            System.out.println("3. Consulter un point de livraison");
            System.out.println("4. Lister tous les points");
            System.out.println("5. Retour");
            
            System.out.print("\nVotre choix (1-5): ");
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    reapproService.creerPointLivraison();
                    pause();
                    break;
                case "2":
                    reapproService.modifierPointLivraison();
                    pause();
                    break;
                case "3":
                    reapproService.consulterPointLivraison();
                    pause();
                    break;
                case "4":
                    reapproService.listerPointsLivraison();
                    pause();
                    break;
                case "5":
                    retour = true;
                    break;
                default:
                    System.out.println(">> Choix invalide!");
                    pause();
            }
        }
    }
    
    private void afficherEntete(String titre) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  " + titre);
        System.out.println("=".repeat(50));
    }
    
    private void pause() {
        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }
}