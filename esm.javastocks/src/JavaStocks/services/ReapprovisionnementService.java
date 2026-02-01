package JavaStocks.services;

import JavaStocks.utils.Constants;
import java.sql.*;
import java.util.Scanner;

public class ReapprovisionnementService {
    private Connection connection;
    private Scanner scanner;
    
    public ReapprovisionnementService(Connection connection) {
        this.connection = connection;
        this.scanner = new Scanner(System.in);
    }
    
    // ========= CRÉER UNE DEMANDE DE RÉAPPROVISIONNEMENT =========
    public void creerDemande() {
        System.out.println("\n=== NOUVELLE DEMANDE DE RÉAPPROVISIONNEMENT ===");
        
        try {
            // Informations de base
            System.out.print("Numéro de commande: ");
            String numeroCommande = scanner.nextLine();
            
            System.out.print("Date de commande (jj/mm/aaaa): ");
            String dateStr = scanner.nextLine();
            java.sql.Date dateCommande = java.sql.Date.valueOf(dateStr.replace("/", "-"));
            
            // Sélection fournisseur
            System.out.println("\nListe des fournisseurs:");
            listerFournisseurs();
            System.out.print("\nID du fournisseur: ");
            int idFournisseur = Integer.parseInt(scanner.nextLine());
            
            // Sélection point de livraison
            System.out.println("\nListe des points de livraison:");
            listerPointsLivraison();
            System.out.print("\nID du point de livraison: ");
            int idPointLivraison = Integer.parseInt(scanner.nextLine());
            
            // Motif
            System.out.print("Motif (R=Réappro, NP=Nouveau produit, UR=Urgence): ");
            String motif = scanner.nextLine().toUpperCase();
            
            // Créer la demande
            String sql = "INSERT INTO reapprovisionnement (numero_commande, date_commande, id_fournisseur, id_point_livraison, motif) " +
                        "VALUES (?, ?, ?, ?, ?) RETURNING id";
            
            int idReappro = -1;
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, numeroCommande);
                pstmt.setDate(2, dateCommande);
                pstmt.setInt(3, idFournisseur);
                pstmt.setInt(4, idPointLivraison);
                pstmt.setString(5, motif);
                
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    idReappro = rs.getInt(1);
                }
            }
            
            if (idReappro != -1) {
                // Ajouter les articles
                ajouterArticlesReapprovisionnement(idReappro);
                System.out.println("\n>> Demande de réapprovisionnement créée avec ID: " + idReappro);
            }
            
        } catch (Exception e) {
            System.err.println(">> Erreur création: " + e.getMessage());
        }
    }
    
    private void ajouterArticlesReapprovisionnement(int idReappro) {
        boolean continuer = true;
        
        while (continuer) {
            System.out.println("\n=== AJOUT D'ARTICLES À LA COMMANDE ===");
            
            // Lister les articles qui ont besoin de réapprovisionnement
            String sqlRuptures = """
                SELECT a.* FROM article a 
                WHERE a.suppression_logique = false 
                AND ((a.categorie = 'T' AND a.quantite <= ?) 
                     OR (a.categorie = 'B' AND a.quantite <= ?) 
                     OR (a.categorie = 'DS' AND a.quantite <= ?))
                ORDER BY a.quantite ASC
                """;
                
            try (PreparedStatement pstmt = connection.prepareStatement(sqlRuptures)) {
                pstmt.setInt(1, Constants.SEUIL_TEXTILE);
                pstmt.setInt(2, Constants.SEUIL_BOISSON);
                pstmt.setInt(3, Constants.SEUIL_DENREE);
                
                ResultSet rs = pstmt.executeQuery();
                
                System.out.println("\nArticles en rupture ou faible stock:");
                System.out.println("ID  | Libellé                       | Cat  | Stock | Seuil");
                System.out.println("----+-------------------------------+------+-------+-------");
                
                int count = 0;
                while (rs.next()) {
                    count++;
                    System.out.printf("%3d | %-30s | %-4s | %5d | %s\n",
                        rs.getInt("id"),
                        rs.getString("libelle"),
                        rs.getString("categorie"),
                        rs.getInt("quantite"),
                        getSeuilPourCategorie(rs.getString("categorie")));
                }
                
                if (count == 0) {
                    System.out.println(">> Aucun article en rupture de stock.");
                }
            } catch (SQLException e) {
                System.err.println(">> Erreur liste ruptures: " + e.getMessage());
            }
            
            // Demander article et quantité
            System.out.print("\nID de l'article à commander (0 pour terminer): ");
            int idArticle = Integer.parseInt(scanner.nextLine());
            
            if (idArticle == 0) {
                continuer = false;
                continue;
            }
            
            System.out.print("Quantité à commander: ");
            int quantite = Integer.parseInt(scanner.nextLine());
            
            // Ajouter à la commande
            try {
                String sql = "INSERT INTO ligne_reapprovisionnement (id_reappro, id_article, quantite) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, idReappro);
                    pstmt.setInt(2, idArticle);
                    pstmt.setInt(3, quantite);
                    pstmt.executeUpdate();
                    System.out.println(">> Article ajouté à la commande.");
                }
            } catch (SQLException e) {
                System.err.println(">> Erreur ajout article: " + e.getMessage());
            }
            
            System.out.print("Ajouter un autre article? (o/n): ");
            String reponse = scanner.nextLine().toLowerCase();
            if (!reponse.equals("o")) {
                continuer = false;
            }
        }
    }
    
    private int getSeuilPourCategorie(String categorie) {
        switch (categorie) {
            case "T": return Constants.SEUIL_TEXTILE;
            case "B": return Constants.SEUIL_BOISSON;
            case "DS": return Constants.SEUIL_DENREE;
            default: return 0;
        }
    }
    
    // ========= GESTION FOURNISSEURS =========
    public void creerFournisseur() {
        System.out.println("\n=== NOUVEAU FOURNISSEUR ===");
        
        try {
            System.out.print("Nom: ");
            String nom = scanner.nextLine();
            
            System.out.print("Rue: ");
            String rue = scanner.nextLine();
            
            System.out.print("Code postal: ");
            String codePostal = scanner.nextLine();
            
            System.out.print("Ville: ");
            String ville = scanner.nextLine();
            
            System.out.print("Téléphone: ");
            String telephone = scanner.nextLine();
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            String sql = "INSERT INTO fournisseur (nom, rue, code_postal, ville, telephone, email) " +
                        "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, nom);
                pstmt.setString(2, rue);
                pstmt.setString(3, codePostal);
                pstmt.setString(4, ville);
                pstmt.setString(5, telephone);
                pstmt.setString(6, email);
                
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    System.out.println(">> Fournisseur créé avec ID: " + rs.getInt(1));
                }
            }
        } catch (Exception e) {
            System.err.println(">> Erreur création: " + e.getMessage());
        }
    }
    
    public void modifierFournisseur() {
        System.out.println("\n=== MODIFICATION FOURNISSEUR ===");
        System.out.println("Fonctionnalité à implémenter...");
    }
    
    public void consulterFournisseur() {
        System.out.println("\n=== CONSULTATION FOURNISSEUR ===");
        System.out.println("Fonctionnalité à implémenter...");
    }
    
    public void listerFournisseurs() {
        System.out.println("\n=== LISTE DES FOURNISSEURS ===");
        
        try {
            String sql = "SELECT * FROM fournisseur ORDER BY nom";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                int count = 0;
                System.out.println("ID  | Nom                 | Ville           | Téléphone");
                System.out.println("----+---------------------+-----------------+----------------");
                
                while (rs.next()) {
                    count++;
                    System.out.printf("%3d | %-20s | %-15s | %s\n",
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("ville"),
                        rs.getString("telephone"));
                }
                
                if (count == 0) {
                    System.out.println(">> Aucun fournisseur enregistré.");
                } else {
                    System.out.println("\nTotal: " + count + " fournisseur(s)");
                }
            }
        } catch (Exception e) {
            System.err.println(">> Erreur liste: " + e.getMessage());
        }
    }
    
    // ========= GESTION POINTS DE LIVRAISON =========
    public void creerPointLivraison() {
        System.out.println("\n=== NOUVEAU POINT DE LIVRAISON ===");
        
        try {
            System.out.print("Nom: ");
            String nom = scanner.nextLine();
            
            System.out.print("Rue: ");
            String rue = scanner.nextLine();
            
            System.out.print("Code postal: ");
            String codePostal = scanner.nextLine();
            
            System.out.print("Ville: ");
            String ville = scanner.nextLine();
            
            System.out.print("Téléphone: ");
            String telephone = scanner.nextLine();
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            String sql = "INSERT INTO point_livraison (nom, rue, code_postal, ville, telephone, email) " +
                        "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, nom);
                pstmt.setString(2, rue);
                pstmt.setString(3, codePostal);
                pstmt.setString(4, ville);
                pstmt.setString(5, telephone);
                pstmt.setString(6, email);
                
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    System.out.println(">> Point de livraison créé avec ID: " + rs.getInt(1));
                }
            }
        } catch (Exception e) {
            System.err.println(">> Erreur création: " + e.getMessage());
        }
    }
    
    public void modifierPointLivraison() {
        System.out.println("\n=== MODIFICATION POINT DE LIVRAISON ===");
        System.out.println("Fonctionnalité à implémenter...");
    }
    
    public void consulterPointLivraison() {
        System.out.println("\n=== CONSULTATION POINT DE LIVRAISON ===");
        System.out.println("Fonctionnalité à implémenter...");
    }
    
    public void listerPointsLivraison() {
        System.out.println("\n=== LISTE DES POINTS DE LIVRAISON ===");
        
        try {
            String sql = "SELECT * FROM point_livraison ORDER BY nom";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                int count = 0;
                System.out.println("ID  | Nom                 | Ville           | Téléphone");
                System.out.println("----+---------------------+-----------------+----------------");
                
                while (rs.next()) {
                    count++;
                    System.out.printf("%3d | %-20s | %-15s | %s\n",
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("ville"),
                        rs.getString("telephone"));
                }
                
                if (count == 0) {
                    System.out.println(">> Aucun point de livraison enregistré.");
                } else {
                    System.out.println("\nTotal: " + count + " point(s) de livraison");
                }
            }
        } catch (Exception e) {
            System.err.println(">> Erreur liste: " + e.getMessage());
        }
    }
    
    // ========= LISTER LES DEMANDES DE RÉAPPROVISIONNEMENT =========
    public void listerDemandes() {
        System.out.println("\n=== DEMANDES DE RÉAPPROVISIONNEMENT ===");
        
        try {
            String sql = """
                SELECT r.*, f.nom as fournisseur, pl.nom as point_livraison
                FROM reapprovisionnement r
                LEFT JOIN fournisseur f ON r.id_fournisseur = f.id
                LEFT JOIN point_livraison pl ON r.id_point_livraison = pl.id
                ORDER BY r.date_commande DESC
                """;
                
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                int count = 0;
                System.out.println("ID  | N° Commande    | Date       | Fournisseur       | Statut");
                System.out.println("----+----------------+------------+-------------------+--------");
                
                while (rs.next()) {
                    count++;
                    System.out.printf("%3d | %-14s | %s | %-17s | %s\n",
                        rs.getInt("id"),
                        rs.getString("numero_commande"),
                        rs.getDate("date_commande"),
                        rs.getString("fournisseur"),
                        rs.getString("statut"));
                }
                
                if (count == 0) {
                    System.out.println(">> Aucune demande de réapprovisionnement.");
                } else {
                    System.out.println("\nTotal: " + count + " demande(s)");
                }
            }
        } catch (Exception e) {
            System.err.println(">> Erreur liste: " + e.getMessage());
        }
    }
}