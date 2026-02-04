package JavaStocks.services;

import JavaStocks.dao.*;
import JavaStocks.models.*;
import java.util.List;
import java.util.Scanner;

/**
 * Service pour la gestion du réapprovisionnement
 */
public class ReapprovisionnementService {
    private Scanner scanner;
    private FournisseurDAO fournisseurDAO;
    private PointLivraisonDAO pointLivraisonDAO;
    private DemandeReapprovisionnementDAO demandeDAO;
    private ArticleDAO articleDAO;
    
    public ReapprovisionnementService(Scanner scanner, FournisseurDAO fournisseurDAO,
                                     PointLivraisonDAO pointLivraisonDAO,
                                     DemandeReapprovisionnementDAO demandeDAO,
                                     ArticleDAO articleDAO) {
        this.scanner = scanner;
        this.fournisseurDAO = fournisseurDAO;
        this.pointLivraisonDAO = pointLivraisonDAO;
        this.demandeDAO = demandeDAO;
        this.articleDAO = articleDAO;
    }
    
    /**
     * Affiche le menu de gestion du réapprovisionnement
     */
    public void menuReapprovisionnement() {
        boolean continuer = true;
        
        do {
            System.out.println("\n=== GESTION RÉAPPROVISIONNEMENT ===");
            System.out.println("1. Gestion Fournisseurs");
            System.out.println("2. Gestion Points de Livraison");
            System.out.println("3. Gestion Demandes de Réapprovisionnement");
            System.out.println("4. Retour au menu principal");
            System.out.print("Choix: ");
            
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    menuFournisseurs();
                    break;
                case "2":
                    menuPointsLivraison();
                    break;
                case "3":
                    menuDemandesReappro();
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
     * Menu de gestion des fournisseurs
     */
    private void menuFournisseurs() {
        boolean continuer = true;
        
        do {
            System.out.println("\n=== GESTION FOURNISSEURS ===");
            System.out.println("1. Créer un nouveau fournisseur");
            System.out.println("2. Modifier un fournisseur");
            System.out.println("3. Afficher les fournisseurs");
            System.out.println("4. Retour");
            System.out.print("Choix: ");
            
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    creerFournisseur();
                    break;
                case "2":
                    modifierFournisseur();
                    break;
                case "3":
                    afficherFournisseurs();
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
     * Crée un nouveau fournisseur
     */
    private void creerFournisseur() {
        System.out.println("\n--- Création d'un nouveau fournisseur ---");
        
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        
        System.out.print("Rue: ");
        String rue = scanner.nextLine();
        
        System.out.print("Code Postal: ");
        String cp = scanner.nextLine();
        
        System.out.print("Ville: ");
        String ville = scanner.nextLine();
        
        System.out.print("Téléphone: ");
        String tel = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        Fournisseur fournisseur = new Fournisseur(0, nom, rue, cp, ville, tel, email);
        
        if (fournisseurDAO.creer(fournisseur)) {
            System.out.println("Fournisseur créé avec succès! ID: " + fournisseur.getId());
        } else {
            System.out.println("Erreur lors de la création du fournisseur.");
        }
    }
    
    /**
     * Affiche tous les fournisseurs
     */
    private void afficherFournisseurs() {
        System.out.println("\n--- Liste des fournisseurs ---");
        List<Fournisseur> fournisseurs = fournisseurDAO.getAll();
        
        if (fournisseurs.isEmpty()) {
            System.out.println("Aucun fournisseur enregistré.");
        } else {
            for (Fournisseur f : fournisseurs) {
                System.out.println(f);
            }
        }
    }
    
    /**
     * Menu de gestion des points de livraison
     */
    private void menuPointsLivraison() {
        boolean continuer = true;
        
        do {
            System.out.println("\n=== GESTION POINTS DE LIVRAISON ===");
            System.out.println("1. Créer un nouveau point de livraison");
            System.out.println("2. Afficher les points de livraison");
            System.out.println("3. Retour");
            System.out.print("Choix: ");
            
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    creerPointLivraison();
                    break;
                case "2":
                    afficherPointsLivraison();
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
     * Crée un nouveau point de livraison
     */
    private void creerPointLivraison() {
        System.out.println("\n--- Création d'un nouveau point de livraison ---");
        
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        
        System.out.print("Rue: ");
        String rue = scanner.nextLine();
        
        System.out.print("Code Postal: ");
        String cp = scanner.nextLine();
        
        System.out.print("Ville: ");
        String ville = scanner.nextLine();
        
        System.out.print("Téléphone: ");
        String tel = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        PointLivraison point = new PointLivraison(0, nom, rue, cp, ville, tel, email);
        
        if (pointLivraisonDAO.creer(point)) {
            System.out.println("Point de livraison créé avec succès! ID: " + point.getId());
        } else {
            System.out.println("Erreur lors de la création du point de livraison.");
        }
    }
    
    /**
     * Affiche tous les points de livraison
     */
    private void afficherPointsLivraison() {
        System.out.println("\n--- Liste des points de livraison ---");
        List<PointLivraison> points = pointLivraisonDAO.getAll();
        
        if (points.isEmpty()) {
            System.out.println("Aucun point de livraison enregistré.");
        } else {
            for (PointLivraison p : points) {
                System.out.println(p);
            }
        }
    }
    
    /**
     * Modifie un fournisseur existant
     */
    private void modifierFournisseur() {
        afficherFournisseurs();
        System.out.print("\nID du fournisseur à modifier: ");
        
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Fournisseur fournisseur = fournisseurDAO.getById(id);
            
            if (fournisseur == null) {
                System.out.println("Fournisseur non trouvé!");
                return;
            }
            
            System.out.println("\nModification du fournisseur: " + fournisseur);
            
            System.out.print("Nouveau nom [" + fournisseur.getNom() + "]: ");
            String nom = scanner.nextLine();
            if (!nom.isEmpty()) fournisseur.setNom(nom);
            
            System.out.print("Nouvelle rue [" + fournisseur.getRue() + "]: ");
            String rue = scanner.nextLine();
            if (!rue.isEmpty()) fournisseur.setRue(rue);
            
            System.out.print("Nouveau code postal [" + fournisseur.getCodePostal() + "]: ");
            String cp = scanner.nextLine();
            if (!cp.isEmpty()) fournisseur.setCodePostal(cp);
            
            System.out.print("Nouvelle ville [" + fournisseur.getVille() + "]: ");
            String ville = scanner.nextLine();
            if (!ville.isEmpty()) fournisseur.setVille(ville);
            
            System.out.print("Nouveau téléphone [" + fournisseur.getTelephone() + "]: ");
            String tel = scanner.nextLine();
            if (!tel.isEmpty()) fournisseur.setTelephone(tel);
            
            System.out.print("Nouvel email [" + fournisseur.getEmail() + "]: ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) fournisseur.setEmail(email);
            
            if (fournisseurDAO.modifier(fournisseur)) {
                System.out.println("Fournisseur modifié avec succès!");
            } else {
                System.out.println("Erreur lors de la modification.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("ID invalide!");
        }
    }
    
    /**
     * Menu de gestion des demandes de réapprovisionnement
     */
    private void menuDemandesReappro() {
        boolean continuer = true;
        
        do {
            System.out.println("\n=== GESTION DEMANDES RÉAPPROVISIONNEMENT ===");
            System.out.println("1. Créer une nouvelle demande");
            System.out.println("2. Afficher les demandes");
            System.out.println("3. Retour");
            System.out.print("Choix: ");
            
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    creerDemandeReappro();
                    break;
                case "2":
                    afficherDemandes();
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
     * Crée une nouvelle demande de réapprovisionnement
     */
    private void creerDemandeReappro() {
        System.out.println("\n--- Création d'une demande de réapprovisionnement ---");
        
        // Sélection du fournisseur
        afficherFournisseurs();
        System.out.print("ID du fournisseur: ");
        int fournisseurId = Integer.parseInt(scanner.nextLine());
        Fournisseur fournisseur = fournisseurDAO.getById(fournisseurId);
        
        if (fournisseur == null) {
            System.out.println("Fournisseur non trouvé!");
            return;
        }
        
        // Sélection du point de livraison
        afficherPointsLivraison();
        System.out.print("ID du point de livraison: ");
        int pointId = Integer.parseInt(scanner.nextLine());
        PointLivraison point = pointLivraisonDAO.getById(pointId);
        
        if (point == null) {
            System.out.println("Point de livraison non trouvé!");
            return;
        }
        
        // Choix du motif
        System.out.println("\nMotifs disponibles:");
        System.out.println("R - Réapprovisionnement normal");
        System.out.println("NP - Nouveaux produits");
        System.out.println("UR - Urgence réapprovisionnement");
        System.out.print("Motif: ");
        String motif = scanner.nextLine().toUpperCase();
        
        // Création de la demande
        DemandeReapprovisionnement demande = new DemandeReapprovisionnement(
            0, new java.util.Date(), motif, fournisseur, point
        );
        
        // Ajout des articles
        boolean ajouterArticles = true;
        while (ajouterArticles) {
            System.out.println("\n--- Ajout d'articles à la demande ---");
            
            // Afficher les articles disponibles
            List<Article> articles = articleDAO.getAll();
            for (Article a : articles) {
                System.out.println(a);
            }
            
            System.out.print("ID de l'article (0 pour terminer): ");
            int articleId = Integer.parseInt(scanner.nextLine());
            
            if (articleId == 0) {
                ajouterArticles = false;
                continue;
            }
            
            Article article = articleDAO.getById(articleId);
            if (article == null) {
                System.out.println("Article non trouvé!");
                continue;
            }
            
            System.out.print("Quantité: ");
            int quantite = Integer.parseInt(scanner.nextLine());
            
            demande.ajouterLigne(article, quantite);
            System.out.println("Article ajouté à la demande.");
        }
        
        // Sauvegarde de la demande
        if (demandeDAO.creer(demande)) {
            System.out.println("Demande de réapprovisionnement créée avec succès! ID: " + demande.getId());
            System.out.println("Montant total: " + demande.calculerMontantTotal() + " €");
        } else {
            System.out.println("Erreur lors de la création de la demande.");
        }
    }
    
    /**
     * Affiche toutes les demandes
     */
    private void afficherDemandes() {
        System.out.println("\n--- Liste des demandes de réapprovisionnement ---");
        List<DemandeReapprovisionnement> demandes = demandeDAO.getAll();
        
        if (demandes.isEmpty()) {
            System.out.println("Aucune demande enregistrée.");
        } else {
            for (DemandeReapprovisionnement d : demandes) {
                System.out.println("\n" + d);
                System.out.println("Fournisseur: " + d.getFournisseur().getNom());
                System.out.println("Point de livraison: " + d.getPointLivraison().getNom());
                System.out.println("Articles:");
                for (LigneDemande ligne : d.getLignes()) {
                    System.out.println("  - " + ligne);
                }
                System.out.println("Montant total: " + d.calculerMontantTotal() + " €");
            }
        }
    }
}