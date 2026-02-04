package JavaStocks.ui;

import JavaStocks.services.ReapprovisionnementService;
import JavaStocks.dao.*;
import JavaStocks.models.*;
import java.sql.Connection;
import java.util.Scanner;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

/**
 * Gestionnaire des menus de l'application
 */
public class MenuManager {
    private Connection connection;
    private Scanner scanner;
    private ArticleDAO articleDAO;
    private CoureurDAO coureurDAO;
    private TypeEpreuveDAO typeEpreuveDAO;
    private ReservationDAO reservationDAO;
    private ReservationEnAttenteDAO reservationEnAttenteDAO;
    private ReapprovisionnementService reapproService;
    
    public MenuManager(Connection connection, Scanner scanner,
                      ArticleDAO articleDAO, CoureurDAO coureurDAO,
                      TypeEpreuveDAO typeEpreuveDAO, ReservationDAO reservationDAO,
                      ReservationEnAttenteDAO reservationEnAttenteDAO,
                      ReapprovisionnementService reapproService) {
        this.connection = connection;
        this.scanner = scanner;
        this.articleDAO = articleDAO;
        this.coureurDAO = coureurDAO;
        this.typeEpreuveDAO = typeEpreuveDAO;
        this.reservationDAO = reservationDAO;
        this.reservationEnAttenteDAO = reservationEnAttenteDAO;
        this.reapproService = reapproService;
    }
    
    /**
     * Affiche le menu principal
     */
    public void showMainMenu() {
        boolean continuer = true;
        
        do {
            System.out.println("\n=== JAVA STOCKS - GESTION DE STOCK ===");
            System.out.println("1. Gestion des articles");
            System.out.println("2. Gestion des coureurs");
            System.out.println("3. Gestion des types d'épreuve");
            System.out.println("4. Gestion des réservations");
            System.out.println("5. Articles en rupture / Réservations en attente");
            System.out.println("6. Gestion du réapprovisionnement");
            System.out.println("7. Consulter l'historique");
            System.out.println("8. Quitter");
            System.out.print("Choix: ");
            
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    menuArticles();
                    break;
                case "2":
                    menuCoureurs();
                    break;
                case "3":
                    menuTypesEpreuve();
                    break;
                case "4":
                    menuReservations();
                    break;
                case "5":
                    menuRupturesAttente();
                    break;
                case "6":
                    reapproService.menuReapprovisionnement();
                    break;
                case "7":
                    menuHistorique();
                    break;
                case "8":
                    System.out.println("Au revoir!");
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        } while (continuer);
    }
    
    /**
     * Menu de gestion des réservations
     */
    private void menuReservations() {
        boolean continuer = true;
        
        do {
            System.out.println("\n=== GESTION DES RÉSERVATIONS ===");
            System.out.println("1. Créer une réservation");
            System.out.println("2. Modifier une réservation");
            System.out.println("3. Consulter une réservation");
            System.out.println("4. Annuler une réservation");
            System.out.println("5. Revenir au menu");
            System.out.print("Choix: ");
            
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    creerReservation();
                    break;
                case "2":
                    modifierReservation();
                    break;
                case "3":
                    consulterReservation();
                    break;
                case "4":
                    annulerReservation();
                    break;
                case "5":
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        } while (continuer);
    }
    
    /**
     * Crée une nouvelle réservation
     */
    private void creerReservation() {
        System.out.println("\n--- Création d'une réservation ---");
        
        try {
            // Afficher les coureurs
            System.out.println("\nListe des coureurs:");
            List<Coureur> coureurs = coureurDAO.getAll();
            for (int i = 0; i < coureurs.size(); i++) {
                System.out.println((i + 1) + ". " + coureurs.get(i));
            }
            
            System.out.print("Numéro du coureur: ");
            int coureurIndex = Integer.parseInt(scanner.nextLine()) - 1;
            if (coureurIndex < 0 || coureurIndex >= coureurs.size()) {
                System.out.println("Numéro invalide!");
                return;
            }
            Coureur coureur = coureurs.get(coureurIndex);
            
            // Afficher les types d'épreuve
            System.out.println("\nListe des types d'épreuve:");
            List<TypeEpreuve> types = typeEpreuveDAO.getAll();
            for (int i = 0; i < types.size(); i++) {
                System.out.println((i + 1) + ". " + types.get(i).getLibelle());
            }
            
            System.out.print("Numéro du type d'épreuve: ");
            int typeIndex = Integer.parseInt(scanner.nextLine()) - 1;
            if (typeIndex < 0 || typeIndex >= types.size()) {
                System.out.println("Numéro invalide!");
                return;
            }
            TypeEpreuve typeEpreuve = types.get(typeIndex);
            
            // Saisir la date
            System.out.print("Date de la réservation (AAAA-MM-JJ): ");
            String dateStr = scanner.nextLine();
            Date date = java.sql.Date.valueOf(dateStr);
            
            // Créer la réservation
            Reservation reservation = new Reservation(0, date, coureur, typeEpreuve, new ArrayList<>());
            
            // Ajouter des articles
            boolean ajouterArticles = true;
            while (ajouterArticles) {
                System.out.println("\nListe des articles disponibles:");
                List<Article> articles = articleDAO.getAll();
                for (int i = 0; i < articles.size(); i++) {
                    Article a = articles.get(i);
                    System.out.println((i + 1) + ". " + a.getLibelle() + " (Stock: " + a.getQuantite() + ")");
                }
                
                System.out.print("Numéro de l'article (0 pour terminer): ");
                int articleIndex = Integer.parseInt(scanner.nextLine()) - 1;
                
                if (articleIndex == -1) {
                    ajouterArticles = false;
                    continue;
                }
                
                if (articleIndex < 0 || articleIndex >= articles.size()) {
                    System.out.println("Numéro invalide!");
                    continue;
                }
                
                Article article = articles.get(articleIndex);
                
                System.out.print("Quantité: ");
                int quantite = Integer.parseInt(scanner.nextLine());
                
                // Vérifier le stock
                if (article.getQuantite() < quantite) {
                    System.out.println("Stock insuffisant! Disponible: " + article.getQuantite());
                    System.out.println("Voulez-vous mettre cette réservation en attente? (O/N)");
                    String choix = scanner.nextLine().toUpperCase();
                    
                    if (choix.equals("O")) {
                        // Créer une réservation en attente
                        ReservationEnAttente reservationAttente = new ReservationEnAttente(
                            0, date, coureur, typeEpreuve, new ArrayList<>()
                        );
                        reservationAttente.ajouterLigne(article, quantite);
                        
                        if (reservationEnAttenteDAO.creer(reservationAttente)) {
                            System.out.println("Réservation mise en attente. ID: " + reservationAttente.getId());
                        }
                    }
                    continue;
                }
                
                // Ajouter la ligne à la réservation
                LigneReservation ligne = new LigneReservation(article, quantite);
                reservation.getLignes().add(ligne);
                
                System.out.println("Article ajouté à la réservation.");
            }
            
            // Sauvegarder la réservation si elle a des articles
            if (!reservation.getLignes().isEmpty()) {
                if (reservationDAO.creer(reservation)) {
                    // Mettre à jour les stocks
                    for (LigneReservation ligne : reservation.getLignes()) {
                        Article article = ligne.getArticle();
                        article.setQuantite(article.getQuantite() - ligne.getQuantite());
                        articleDAO.modifier(article);
                    }
                    System.out.println("Réservation créée avec succès! ID: " + reservation.getId());
                } else {
                    System.out.println("Erreur lors de la création de la réservation.");
                }
            } else {
                System.out.println("Aucun article ajouté. Réservation annulée.");
            }
            
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    /**
     * Modifie une réservation existante
     */
    private void modifierReservation() {
        System.out.println("\n--- Modification d'une réservation ---");
        
        try {
            // Afficher les réservations
            List<Reservation> reservations = reservationDAO.getAll();
            if (reservations.isEmpty()) {
                System.out.println("Aucune réservation existante.");
                return;
            }
            
            for (Reservation r : reservations) {
                System.out.println("ID: " + r.getId() + " - " + r.getCoureur().getNom() + 
                                 " - " + r.getDate());
            }
            
            System.out.print("ID de la réservation à modifier: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            Reservation reservation = reservationDAO.getById(id);
            if (reservation == null) {
                System.out.println("Réservation non trouvée!");
                return;
            }
            
            System.out.println("\nRéservation actuelle:");
            System.out.println("Date: " + reservation.getDate());
            System.out.println("Coureur: " + reservation.getCoureur().getNom() + " " + reservation.getCoureur().getPrenom());
            System.out.println("Épreuve: " + reservation.getTypeEpreuve().getLibelle());
            System.out.println("Articles:");
            for (LigneReservation ligne : reservation.getLignes()) {
                System.out.println("  - " + ligne.getArticle().getLibelle() + " x " + ligne.getQuantite());
            }
            
            // TODO: Implémenter la modification complète
            System.out.println("\nFonctionnalité de modification à implémenter complètement.");
            
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    /**
     * Consulte une réservation
     */
    private void consulterReservation() {
        System.out.println("\n--- Consultation d'une réservation ---");
        
        try {
            // Afficher les réservations
            List<Reservation> reservations = reservationDAO.getAll();
            if (reservations.isEmpty()) {
                System.out.println("Aucune réservation existante.");
                return;
            }
            
            for (Reservation r : reservations) {
                System.out.println("ID: " + r.getId() + " - " + r.getCoureur().getNom() + 
                                 " - " + r.getDate());
            }
            
            System.out.print("ID de la réservation à consulter: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            Reservation reservation = reservationDAO.getById(id);
            if (reservation == null) {
                System.out.println("Réservation non trouvée!");
                return;
            }
            
            System.out.println("\n=== DÉTAILS DE LA RÉSERVATION ===");
            System.out.println("ID: " + reservation.getId());
            System.out.println("Date: " + reservation.getDate());
            System.out.println("Coureur: " + reservation.getCoureur().getNom() + " " + reservation.getCoureur().getPrenom());
            System.out.println("Type d'épreuve: " + reservation.getTypeEpreuve().getLibelle());
            System.out.println("\nArticles réservés:");
            
            double total = 0;
            for (LigneReservation ligne : reservation.getLignes()) {
                Article article = ligne.getArticle();
                System.out.println("  - " + article.getLibelle() + " x " + ligne.getQuantite());
                if (article instanceof Textile) {
                    Textile textile = (Textile) article;
                    System.out.println("    Taille: " + textile.getTaille() + ", Couleur: " + textile.getCouleur());
                } else if (article instanceof Boisson) {
                    Boisson boisson = (Boisson) article;
                    System.out.println("    Volume: " + boisson.getVolume() + "cl");
                } else if (article instanceof DenreeSeche) {
                    DenreeSeche denree = (DenreeSeche) article;
                    System.out.println("    Poids: " + denree.getPoids() + "g");
                }
            }
            
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    /**
     * Annule une réservation
     */
    private void annulerReservation() {
        System.out.println("\n--- Annulation d'une réservation ---");
        
        try {
            // Afficher les réservations
            List<Reservation> reservations = reservationDAO.getAll();
            if (reservations.isEmpty()) {
                System.out.println("Aucune réservation existante.");
                return;
            }
            
            for (Reservation r : reservations) {
                System.out.println("ID: " + r.getId() + " - " + r.getCoureur().getNom() + 
                                 " - " + r.getDate());
            }
            
            System.out.print("ID de la réservation à annuler: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            Reservation reservation = reservationDAO.getById(id);
            if (reservation == null) {
                System.out.println("Réservation non trouvée!");
                return;
            }
            
            System.out.println("\nConfirmez-vous l'annulation de la réservation ID " + id + "? (O/N)");
            String confirmation = scanner.nextLine().toUpperCase();
            
            if (confirmation.equals("O")) {
                // Rembourser les stocks
                for (LigneReservation ligne : reservation.getLignes()) {
                    Article article = ligne.getArticle();
                    article.setQuantite(article.getQuantite() + ligne.getQuantite());
                    articleDAO.modifier(article);
                }
                
                // Supprimer la réservation
                if (reservationDAO.supprimer(id)) {
                    System.out.println("Réservation annulée avec succès. Stocks réapprovisionnés.");
                } else {
                    System.out.println("Erreur lors de l'annulation.");
                }
            } else {
                System.out.println("Annulation annulée.");
            }
            
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    /**
     * Menu de gestion des articles en rupture et réservations en attente
     */
    private void menuRupturesAttente() {
        boolean continuer = true;
        
        do {
            System.out.println("\n=== ARTICLES EN RUPTURE / RÉSERVATIONS EN ATTENTE ===");
            System.out.println("1. Consulter les produits en rupture");
            System.out.println("2. Consulter les réservations en attente de validation");
            System.out.println("3. Valider une réservation en attente");
            System.out.println("4. Revenir au menu");
            System.out.print("Choix: ");
            
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    afficherArticlesEnRupture();
                    break;
                case "2":
                    afficherReservationsEnAttente();
                    break;
                case "3":
                    validerReservationEnAttente();
                    break;
                case "4":
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        } while (continuer);
    }
    
    /**
     * Affiche les articles en rupture de stock
     */
    private void afficherArticlesEnRupture() {
        System.out.println("\n--- Articles en rupture de stock ---");
        List<Article> articles = articleDAO.getAll();
        boolean ruptureTrouvee = false;
        
        for (Article article : articles) {
            int seuil = getSeuilReapprovisionnement(article);
            if (article.getQuantite() <= seuil) {
                System.out.println(article);
                System.out.println("  Quantité: " + article.getQuantite() + " (seuil: " + seuil + ")");
                ruptureTrouvee = true;
            }
        }
        
        if (!ruptureTrouvee) {
            System.out.println("Aucun article en rupture de stock.");
        }
    }
    
    /**
     * Détermine le seuil de réapprovisionnement selon la catégorie
     */
    private int getSeuilReapprovisionnement(Article article) {
        if (article instanceof Textile) {
            return 10; // Seuil pour textile
        } else if (article instanceof Boisson) {
            return 100; // Seuil pour boisson
        } else if (article instanceof DenreeSeche) {
            return 100; // Seuil pour denrée sèche
        }
        return 50; // Seuil par défaut
    }
    
    /**
     * Affiche les réservations en attente
     */
    private void afficherReservationsEnAttente() {
        System.out.println("\n--- Réservations en attente de validation ---");
        List<ReservationEnAttente> reservations = reservationEnAttenteDAO.getAll();
        
        if (reservations.isEmpty()) {
            System.out.println("Aucune réservation en attente.");
        } else {
            for (ReservationEnAttente res : reservations) {
                System.out.println("\n" + res);
                System.out.println("Date: " + res.getDate());
                System.out.println("Coureur: " + res.getCoureur().getNom() + " " + res.getCoureur().getPrenom());
                System.out.println("Épreuve: " + res.getTypeEpreuve().getLibelle());
                System.out.println("Articles:");
                for (LigneReservation ligne : res.getLignes()) {
                    System.out.println("  - " + ligne.getArticle().getLibelle() + " x " + ligne.getQuantite());
                }
            }
        }
    }
    
    /**
     * Valide une réservation en attente
     */
    private void validerReservationEnAttente() {
        afficherReservationsEnAttente();
        System.out.print("\nID de la réservation à valider: ");
        
        try {
            int id = Integer.parseInt(scanner.nextLine());
            
            // Vérifier si la réservation existe
            List<ReservationEnAttente> reservations = reservationEnAttenteDAO.getAll();
            ReservationEnAttente reservationAValider = null;
            
            for (ReservationEnAttente res : reservations) {
                if (res.getId() == id) {
                    reservationAValider = res;
                    break;
                }
            }
            
            if (reservationAValider == null) {
                System.out.println("Réservation non trouvée!");
                return;
            }
            
            // Vérifier les stocks avant validation
            boolean stocksDisponibles = true;
            for (LigneReservation ligne : reservationAValider.getLignes()) {
                Article article = ligne.getArticle();
                if (article.getQuantite() < ligne.getQuantite()) {
                    System.out.println("Stock insuffisant pour: " + article.getLibelle());
                    System.out.println("  Disponible: " + article.getQuantite() + ", Demandé: " + ligne.getQuantite());
                    stocksDisponibles = false;
                }
            }
            
            if (!stocksDisponibles) {
                System.out.println("Impossible de valider: stocks insuffisants!");
                return;
            }
            
            // Valider la réservation
            if (reservationEnAttenteDAO.valider(id)) {
                // Mettre à jour les stocks
                for (LigneReservation ligne : reservationAValider.getLignes()) {
                    Article article = ligne.getArticle();
                    article.setQuantite(article.getQuantite() - ligne.getQuantite());
                    articleDAO.modifier(article);
                }
                System.out.println("Réservation validée avec succès! Stocks mis à jour.");
            } else {
                System.out.println("Erreur lors de la validation.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("ID invalide!");
        }
    }
    
    /**
     * Menu de consultation de l'historique
     */
    private void menuHistorique() {
        boolean continuer = true;
        
        do {
            System.out.println("\n=== CONSULTATION HISTORIQUE ===");
            System.out.println("1. Nombre de réservations à une date donnée");
            System.out.println("2. Nombre de réservations pour un coureur");
            System.out.println("3. Nombre de réservations pour un type d'épreuve");
            System.out.println("4. Quantités réservées par article à une date");
            System.out.println("5. Retour");
            System.out.print("Choix: ");
            
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    // À implémenter
                    System.out.println("Fonctionnalité à implémenter");
                    break;
                case "2":
                    // À implémenter
                    System.out.println("Fonctionnalité à implémenter");
                    break;
                case "3":
                    // À implémenter
                    System.out.println("Fonctionnalité à implémenter");
                    break;
                case "4":
                    // À implémenter
                    System.out.println("Fonctionnalité à implémenter");
                    break;
                case "5":
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        } while (continuer);
    }
    
    /**
     * Menu de gestion des articles
     */
    private void menuArticles() {
        boolean continuer = true;
        
        do {
            System.out.println("\n=== GESTION DES ARTICLES ===");
            System.out.println("1. Ajouter un article");
            System.out.println("2. Modifier un article");
            System.out.println("3. Consulter un article");
            System.out.println("4. Suppression logique");
            System.out.println("5. Revenir au menu");
            System.out.print("Choix: ");
            
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    creerArticle();
                    break;
                case "2":
                    modifierArticle();
                    break;
                case "3":
                    consulterArticle();
                    break;
                case "4":
                    supprimerArticleLogique();
                    break;
                case "5":
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        } while (continuer);
    }
    
    /**
     * Consulte un article - CORRIGÉ : n'affiche plus les messages d'erreur
     */
    private void consulterArticle() {
        System.out.println("\n--- Consultation d'un article ---");
        
        try {
            // CORRECTION : Récupère seulement les ID et noms sans mapper tous les objets
            System.out.println("\n=== LISTE DES ARTICLES ===");
            List<Object[]> articlesList = getArticlesSimpleList();
            
            if (articlesList.isEmpty()) {
                System.out.println("Aucun article enregistré.");
                return;
            }
            
            // Afficher seulement les ID et noms
            for (int i = 0; i < articlesList.size(); i++) {
                Object[] articleInfo = articlesList.get(i);
                System.out.println((i + 1) + ". " + articleInfo[1] + " (ID: " + articleInfo[0] + ")");
            }
            
            System.out.print("\nNuméro de l'article: ");
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            
            if (index < 0 || index >= articlesList.size()) {
                System.out.println("Numéro invalide!");
                return;
            }
            
            // Récupérer l'article complet par son ID
            int articleId = (int) articlesList.get(index)[0];
            Article article = articleDAO.getById(articleId);
            
            if (article == null) {
                System.out.println("Article non trouvé!");
                return;
            }
            
            System.out.println("\n=== DÉTAILS DE L'ARTICLE ===");
            System.out.println("ID: " + article.getId());
            System.out.println("Libellé: " + article.getLibelle());
            System.out.println("Quantité: " + article.getQuantite());
            
            if (article instanceof Textile) {
                Textile textile = (Textile) article;
                System.out.println("Catégorie: Textile");
                System.out.println("Taille: " + textile.getTaille());
                System.out.println("Couleur: " + textile.getCouleur());
            } else if (article instanceof Boisson) {
                Boisson boisson = (Boisson) article;
                System.out.println("Catégorie: Boisson");
                System.out.println("Volume: " + boisson.getVolume() + "cl");
            } else if (article instanceof DenreeSeche) {
                DenreeSeche denree = (DenreeSeche) article;
                System.out.println("Catégorie: Denrée Sèche");
                System.out.println("Poids: " + denree.getPoids() + "g");
            } else {
                System.out.println("Catégorie: Inconnue");
            }
            
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Méthode helper pour obtenir une liste simple d'articles (ID + nom)
     * sans mapper complètement les objets
     */
    private List<Object[]> getArticlesSimpleList() {
        List<Object[]> result = new ArrayList<>();
        
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT id, libelle FROM article WHERE suppression_logique = false ORDER BY id")) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String libelle = rs.getString("libelle");
                result.add(new Object[]{id, libelle});
            }
        } catch (Exception e) {
            System.err.println("Erreur récupération liste articles: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Crée un nouvel article
     */
    private void creerArticle() {
        System.out.println("\n--- Création d'un nouvel article ---");
        
        try {
            System.out.print("Libellé: ");
            String libelle = scanner.nextLine();
            
            System.out.print("Quantité initiale: ");
            int quantite = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Catégorie (T=Textile, B=Boisson, DS=Denrée Sèche): ");
            String categorie = scanner.nextLine().toUpperCase();
            
            Article article = null;
            
            switch (categorie) {
                case "T":
                    System.out.print("Taille (XS, S, M, L, XL): ");
                    String taille = scanner.nextLine();
                    
                    System.out.print("Couleur: ");
                    String couleur = scanner.nextLine();
                    
                    article = new Textile(0, libelle, quantite, taille, couleur);
                    break;
                    
                case "B":
                    System.out.print("Volume (en cl): ");
                    int volume = Integer.parseInt(scanner.nextLine());
                    
                    article = new Boisson(0, libelle, quantite, volume);
                    break;
                    
                case "DS":
                    System.out.print("Poids (en g): ");
                    int poids = Integer.parseInt(scanner.nextLine());
                    
                    article = new DenreeSeche(0, libelle, quantite, poids);
                    break;
                    
                default:
                    System.out.println("Catégorie invalide!");
                    return;
            }
            
            if (articleDAO.creer(article)) {
                System.out.println("Article créé avec succès! ID: " + article.getId());
            } else {
                System.out.println("Erreur lors de la création de l'article.");
            }
            
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    /**
     * Modifie un article
     */
    private void modifierArticle() {
        System.out.println("\n--- Modification d'un article ---");
        
        try {
            List<Object[]> articlesList = getArticlesSimpleList();
            if (articlesList.isEmpty()) {
                System.out.println("Aucun article enregistré.");
                return;
            }
            
            for (int i = 0; i < articlesList.size(); i++) {
                Object[] articleInfo = articlesList.get(i);
                System.out.println((i + 1) + ". " + articleInfo[1] + " (ID: " + articleInfo[0] + ")");
            }
            
            System.out.print("Numéro de l'article: ");
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            
            if (index < 0 || index >= articlesList.size()) {
                System.out.println("Numéro invalide!");
                return;
            }
            
            int articleId = (int) articlesList.get(index)[0];
            Article article = articleDAO.getById(articleId);
            
            if (article == null) {
                System.out.println("Article non trouvé!");
                return;
            }
            
            System.out.println("\nArticle actuel: " + article.getLibelle());
            System.out.print("Nouveau libellé [" + article.getLibelle() + "]: ");
            String nouveauLibelle = scanner.nextLine();
            if (!nouveauLibelle.isEmpty()) {
                article.setLibelle(nouveauLibelle);
            }
            
            System.out.print("Nouvelle quantité [" + article.getQuantite() + "]: ");
            String quantiteStr = scanner.nextLine();
            if (!quantiteStr.isEmpty()) {
                article.setQuantite(Integer.parseInt(quantiteStr));
            }
            
            if (articleDAO.modifier(article)) {
                System.out.println("Article modifié avec succès!");
            } else {
                System.out.println("Erreur lors de la modification.");
            }
            
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    /**
     * Suppression logique d'un article
     */
    private void supprimerArticleLogique() {
        System.out.println("\n--- Suppression logique d'un article ---");
        
        try {
            List<Object[]> articlesList = getArticlesSimpleList();
            if (articlesList.isEmpty()) {
                System.out.println("Aucun article enregistré.");
                return;
            }
            
            for (int i = 0; i < articlesList.size(); i++) {
                Object[] articleInfo = articlesList.get(i);
                System.out.println((i + 1) + ". " + articleInfo[1] + " (ID: " + articleInfo[0] + ")");
            }
            
            System.out.print("Numéro de l'article: ");
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            
            if (index < 0 || index >= articlesList.size()) {
                System.out.println("Numéro invalide!");
                return;
            }
            
            int articleId = (int) articlesList.get(index)[0];
            Article article = articleDAO.getById(articleId);
            
            if (article == null) {
                System.out.println("Article non trouvé!");
                return;
            }
            
            System.out.println("\nConfirmez-vous la suppression logique de: " + article.getLibelle() + "? (O/N)");
            String confirmation = scanner.nextLine().toUpperCase();
            
            if (confirmation.equals("O")) {
                if (articleDAO.supprimerLogique(article.getId())) {
                    System.out.println("Article supprimé logiquement.");
                } else {
                    System.out.println("Erreur lors de la suppression.");
                }
            } else {
                System.out.println("Suppression annulée.");
            }
            
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    /**
     * Menu de gestion des coureurs (avec création)
     */
    private void menuCoureurs() {
        boolean continuer = true;
        
        do {
            System.out.println("\n=== GESTION DES COUREURS ===");
            System.out.println("1. Ajouter un coureur");
            System.out.println("2. Afficher les coureurs");
            System.out.println("3. Revenir au menu");
            System.out.print("Choix: ");
            
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    creerCoureur();
                    break;
                case "2":
                    afficherCoureurs();
                    break;
                case "3":
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        } while (continuer);
    }
    
    /**
     * Crée un nouveau coureur
     */
    private void creerCoureur() {
        System.out.println("\n--- Création d'un nouveau coureur ---");
        
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        
        System.out.print("Prénom: ");
        String prenom = scanner.nextLine();
        
        // Créer le coureur
        Coureur coureur = new Coureur(0, nom, prenom);
        
        if (coureurDAO.creer(coureur)) {
            System.out.println("Coureur créé avec succès! ID: " + coureur.getId());
        } else {
            System.out.println("Erreur lors de la création du coureur.");
        }
    }
    
    /**
     * Affiche tous les coureurs
     */
    private void afficherCoureurs() {
        System.out.println("\n--- Liste des coureurs ---");
        List<Coureur> coureurs = coureurDAO.getAll();
        
        if (coureurs.isEmpty()) {
            System.out.println("Aucun coureur enregistré.");
        } else {
            for (Coureur c : coureurs) {
                System.out.println(c);
            }
        }
    }
    
    /**
     * Menu de gestion des types d'épreuve (avec création)
     */
    private void menuTypesEpreuve() {
        boolean continuer = true;
        
        do {
            System.out.println("\n=== GESTION DES TYPES D'ÉPREUVE ===");
            System.out.println("1. Ajouter un type d'épreuve");
            System.out.println("2. Afficher les types d'épreuve");
            System.out.println("3. Revenir au menu");
            System.out.print("Choix: ");
            
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    creerTypeEpreuve();
                    break;
                case "2":
                    afficherTypesEpreuve();
                    break;
                case "3":
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        } while (continuer);
    }
    
    /**
     * Crée un nouveau type d'épreuve
     */
    private void creerTypeEpreuve() {
        System.out.println("\n--- Création d'un nouveau type d'épreuve ---");
        
        System.out.print("Libellé: ");
        String libelle = scanner.nextLine();
        
        // Créer le type d'épreuve
        TypeEpreuve typeEpreuve = new TypeEpreuve(0, libelle);
        
        if (typeEpreuveDAO.creer(typeEpreuve)) {
            System.out.println("Type d'épreuve créé avec succès! ID: " + typeEpreuve.getId());
        } else {
            System.out.println("Erreur lors de la création du type d'épreuve.");
        }
    }
    
    /**
     * Affiche tous les types d'épreuve
     */
    private void afficherTypesEpreuve() {
        System.out.println("\n--- Liste des types d'épreuve ---");
        List<TypeEpreuve> types = typeEpreuveDAO.getAll();
        
        if (types.isEmpty()) {
            System.out.println("Aucun type d'épreuve enregistré.");
        } else {
            for (TypeEpreuve t : types) {
                System.out.println(t);
            }
        }
    }
}