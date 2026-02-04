package JavaStocks.services;

import java.sql.*;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservationService {
    private Connection connection;
    private Scanner scanner;
    private ArticleService articleService;
    private CoureurService coureurService;
    private TypeEpreuveService typeEpreuveService;
    
    public ReservationService(Connection connection, ArticleService articleService, 
                            CoureurService coureurService, TypeEpreuveService typeEpreuveService) {
        this.connection = connection;
        this.scanner = new Scanner(System.in);
        this.articleService = articleService;
        this.coureurService = coureurService;
        this.typeEpreuveService = typeEpreuveService;
    }
    
    // ========= CRÉER UNE RÉSERVATION =========
    public void creer() {
        System.out.println("\n=== CRÉATION D'UNE RÉSERVATION ===");
        
        try {
            // Sélection de la date
            System.out.print("Date (jj/mm/aaaa): ");
            String dateStr = scanner.nextLine();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(dateStr);
            
            // Sélection du coureur
            System.out.println("\nListe des coureurs:");
            coureurService.listerCoureurs();
            System.out.print("\nID du coureur: ");
            int idCoureur = Integer.parseInt(scanner.nextLine());
            
            // Sélection du type d'épreuve
            System.out.println("\nListe des types d'épreuve:");
            typeEpreuveService.listerTypesEpreuve();
            System.out.print("\nID du type d'épreuve: ");
            int idTypeEpreuve = Integer.parseInt(scanner.nextLine());
            
            // Création de la réservation en base
            String sql = "INSERT INTO reservation (date_reservation, id_coureur, id_type_epreuve, validee) " +
                        "VALUES (?, ?, ?, false) RETURNING id";
            int idReservation = -1;
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setDate(1, new java.sql.Date(date.getTime()));
                pstmt.setInt(2, idCoureur);
                pstmt.setInt(3, idTypeEpreuve);
                
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    idReservation = rs.getInt(1);
                }
            }
            
            if (idReservation != -1) {
                // Ajout des articles
                ajouterArticlesReservation(idReservation);
                System.out.println("\n>> Réservation créée avec succès! ID: " + idReservation);
            }
            
        } catch (Exception e) {
            System.err.println(">> Erreur lors de la création: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void ajouterArticlesReservation(int idReservation) throws SQLException {
        boolean continuer = true;
        
        while (continuer) {
            System.out.println("\n=== AJOUT D'ARTICLES À LA RÉSERVATION ===");
            articleService.listerTous();
            
            System.out.print("\nID de l'article à ajouter (0 pour terminer): ");
            int idArticle = Integer.parseInt(scanner.nextLine());
            
            if (idArticle == 0) {
                continuer = false;
                continue;
            }
            
            System.out.print("Quantité: ");
            int quantite = Integer.parseInt(scanner.nextLine());
            
            // Vérifier la disponibilité
            if (articleService.verifierDisponibilite(idArticle, quantite)) {
                // Ajouter à la réservation
                String sql = "INSERT INTO ligne_reservation (id_reservation, id_article, quantite) " +
                            "VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, idReservation);
                    pstmt.setInt(2, idArticle);
                    pstmt.setInt(3, quantite);
                    pstmt.executeUpdate();
                }
                
                // Mettre à jour le stock
                articleService.mettreAJourStock(idArticle, -quantite);
                System.out.println(">> Article ajouté à la réservation.");
            } else {
                System.out.println(">> Quantité insuffisante en stock!");
                System.out.println(">> La réservation sera mise en attente de validation.");
                mettreReservationEnAttente(idReservation);
                break;
            }
            
            System.out.print("Ajouter un autre article? (o/n): ");
            String reponse = scanner.nextLine().toLowerCase();
            if (!reponse.equals("o")) {
                continuer = false;
            }
        }
    }
    
    private void mettreReservationEnAttente(int idReservation) throws SQLException {
        // Marquer la réservation comme non validée
        String sql = "UPDATE reservation SET validee = false WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idReservation);
            pstmt.executeUpdate();
        }
    }
    
    // ========= MODIFIER UNE RÉSERVATION =========
    public void modifier() {
        System.out.println("\n=== MODIFICATION RÉSERVATION ===");
        
        try {
            listerReservations();
            System.out.print("\nID de la réservation à modifier: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            String sql = "SELECT * FROM reservation WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    afficherDetailsReservation(rs);
                    
                    System.out.println("\n--- NOUVELLES INFORMATIONS ---");
                    System.out.print("Nouvelle date (jj/mm/aaaa, enter pour garder): ");
                    String nouvelleDateStr = scanner.nextLine();
                    
                    if (!nouvelleDateStr.isEmpty()) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date nouvelleDate = sdf.parse(nouvelleDateStr);
                        
                        String updateSql = "UPDATE reservation SET date_reservation = ? WHERE id = ?";
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                            updateStmt.setDate(1, new java.sql.Date(nouvelleDate.getTime()));
                            updateStmt.setInt(2, id);
                            updateStmt.executeUpdate();
                            System.out.println(">> Date modifiée avec succès!");
                        }
                    }
                } else {
                    System.out.println(">> Réservation non trouvée!");
                }
            }
        } catch (Exception e) {
            System.err.println(">> Erreur modification: " + e.getMessage());
        }
    }
    
    // ========= CONSULTER UNE RÉSERVATION =========
    public void consulter() {
        System.out.println("\n=== CONSULTATION RÉSERVATION ===");
        
        try {
            listerReservations();
            System.out.print("\nID de la réservation à consulter: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            String sql = """
                SELECT r.*, c.nom, c.prenom, te.libelle as type_epreuve
                FROM reservation r
                JOIN coureur c ON r.id_coureur = c.id
                JOIN type_epreuve te ON r.id_type_epreuve = te.id
                WHERE r.id = ?
                """;
                
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    System.out.println("\n=== DÉTAILS RÉSERVATION #" + id + " ===");
                    System.out.println("Date: " + rs.getDate("date_reservation"));
                    System.out.println("Coureur: " + rs.getString("prenom") + " " + rs.getString("nom"));
                    System.out.println("Type d'épreuve: " + rs.getString("type_epreuve"));
                    System.out.println("Statut: " + (rs.getBoolean("validee") ? "Validée" : "En attente"));
                    
                    // Afficher les articles de la réservation
                    System.out.println("\nArticles réservés:");
                    String sqlArticles = """
                        SELECT a.libelle, lr.quantite
                        FROM ligne_reservation lr
                        JOIN article a ON lr.id_article = a.id
                        WHERE lr.id_reservation = ?
                        """;
                        
                    try (PreparedStatement pstmtArticles = connection.prepareStatement(sqlArticles)) {
                        pstmtArticles.setInt(1, id);
                        ResultSet rsArticles = pstmtArticles.executeQuery();
                        
                        int totalArticles = 0;
                        while (rsArticles.next()) {
                            totalArticles++;
                            System.out.printf("  - %s : %d unité(s)\n",
                                rsArticles.getString("libelle"),
                                rsArticles.getInt("quantite"));
                        }
                        
                        if (totalArticles == 0) {
                            System.out.println("  Aucun article dans cette réservation");
                        }
                    }
                } else {
                    System.out.println(">> Réservation non trouvée!");
                }
            }
        } catch (Exception e) {
            System.err.println(">> Erreur consultation: " + e.getMessage());
        }
    }
    
    // ========= ANNULER UNE RÉSERVATION =========
    public void annuler() {
        System.out.println("\n=== ANNULATION RÉSERVATION ===");
        
        try {
            listerReservations();
            System.out.print("\nID de la réservation à annuler: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            // Demander confirmation
            System.out.print("Êtes-vous sûr de vouloir annuler cette réservation? (o/n): ");
            String confirmation = scanner.nextLine().toLowerCase();
            
            if (confirmation.equals("o")) {
                // Récupérer les articles de la réservation pour restocker
                String sqlSelect = "SELECT id_article, quantite FROM ligne_reservation WHERE id_reservation = ?";
                try (PreparedStatement pstmtSelect = connection.prepareStatement(sqlSelect)) {
                    pstmtSelect.setInt(1, id);
                    ResultSet rs = pstmtSelect.executeQuery();
                    
                    // Restocker chaque article
                    while (rs.next()) {
                        int idArticle = rs.getInt("id_article");
                        int quantite = rs.getInt("quantite");
                        articleService.mettreAJourStock(idArticle, quantite);
                    }
                }
                
                // Supprimer les lignes de réservation
                String sqlDeleteLignes = "DELETE FROM ligne_reservation WHERE id_reservation = ?";
                try (PreparedStatement pstmtDelete = connection.prepareStatement(sqlDeleteLignes)) {
                    pstmtDelete.setInt(1, id);
                    pstmtDelete.executeUpdate();
                }
                
                // Supprimer la réservation
                String sqlDeleteReservation = "DELETE FROM reservation WHERE id = ?";
                try (PreparedStatement pstmtDeleteRes = connection.prepareStatement(sqlDeleteReservation)) {
                    pstmtDeleteRes.setInt(1, id);
                    int rows = pstmtDeleteRes.executeUpdate();
                    
                    if (rows > 0) {
                        System.out.println(">> Réservation annulée avec succès!");
                        System.out.println(">> Les articles ont été restockés.");
                    } else {
                        System.out.println(">> Réservation non trouvée!");
                    }
                }
            } else {
                System.out.println(">> Annulation abandonnée.");
            }
        } catch (Exception e) {
            System.err.println(">> Erreur annulation: " + e.getMessage());
        }
    }
    
    // ========= VALIDER RÉSERVATION EN ATTENTE =========
    public void validerEnAttente() {
        System.out.println("\n=== VALIDATION RÉSERVATIONS EN ATTENTE ===");
        
        try {
            // Lister les réservations en attente
            String sql = """
                SELECT r.id, r.date_reservation, c.nom, c.prenom, te.libelle
                FROM reservation r
                JOIN coureur c ON r.id_coureur = c.id
                JOIN type_epreuve te ON r.id_type_epreuve = te.id
                WHERE r.validee = false AND r.annulee = false
                ORDER BY r.date_reservation
                """;
                
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                int count = 0;
                System.out.println("\nRéservations en attente de validation:");
                System.out.println("ID  | Date       | Coureur           | Épreuve");
                System.out.println("----+------------+-------------------+-------------------");
                
                while (rs.next()) {
                    count++;
                    System.out.printf("%3d | %s | %-15s | %s\n",
                        rs.getInt("id"),
                        rs.getDate("date_reservation"),
                        rs.getString("prenom") + " " + rs.getString("nom"),
                        rs.getString("libelle"));
                }
                
                if (count == 0) {
                    System.out.println(">> Aucune réservation en attente.");
                    return;
                }
                
                System.out.print("\nID de la réservation à valider: ");
                int id = Integer.parseInt(scanner.nextLine());
                
                // Valider la réservation
                String sqlUpdate = "UPDATE reservation SET validee = true WHERE id = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
                    pstmt.setInt(1, id);
                    int rows = pstmt.executeUpdate();
                    
                    if (rows > 0) {
                        System.out.println(">> Réservation validée avec succès!");
                    } else {
                        System.out.println(">> Réservation non trouvée!");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(">> Erreur validation: " + e.getMessage());
        }
    }
    
    // ========= LISTER RÉSERVATIONS EN ATTENTE =========
    public void listerEnAttente() {
        System.out.println("\n=== RÉSERVATIONS EN ATTENTE ===");
        
        try {
            String sql = """
                SELECT r.id, r.date_reservation, c.nom, c.prenom, te.libelle,
                       (SELECT COUNT(*) FROM ligne_reservation lr WHERE lr.id_reservation = r.id) as nb_articles
                FROM reservation r
                JOIN coureur c ON r.id_coureur = c.id
                JOIN type_epreuve te ON r.id_type_epreuve = te.id
                WHERE r.validee = false AND r.annulee = false
                ORDER BY r.date_reservation
                """;
                
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                int count = 0;
                System.out.println("ID  | Date       | Coureur           | Épreuve              | Articles");
                System.out.println("----+------------+-------------------+----------------------+---------");
                
                while (rs.next()) {
                    count++;
                    System.out.printf("%3d | %s | %-15s | %-20s | %d\n",
                        rs.getInt("id"),
                        rs.getDate("date_reservation"),
                        rs.getString("prenom") + " " + rs.getString("nom"),
                        rs.getString("libelle"),
                        rs.getInt("nb_articles"));
                }
                
                if (count == 0) {
                    System.out.println(">> Aucune réservation en attente.");
                } else {
                    System.out.println("\nTotal: " + count + " réservation(s) en attente");
                }
            }
        } catch (Exception e) {
            System.err.println(">> Erreur liste: " + e.getMessage());
        }
    }
    
    // ========= HISTORIQUE PAR DATE =========
    public void historiqueParDate() {
        System.out.println("\n=== HISTORIQUE PAR DATE ===");
        
        try {
            System.out.print("Date (jj/mm/aaaa): ");
            String dateStr = scanner.nextLine();
            
            String sql = """
                SELECT COUNT(*) as nb_reservations, 
                       SUM((SELECT COUNT(*) FROM ligne_reservation lr WHERE lr.id_reservation = r.id)) as nb_articles
                FROM reservation r
                WHERE r.date_reservation = ? AND r.annulee = false
                """;
                
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = sdf.parse(dateStr);
                pstmt.setDate(1, new java.sql.Date(date.getTime()));
                
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int nbReservations = rs.getInt("nb_reservations");
                    int nbArticles = rs.getInt("nb_articles");
                    
                    System.out.println("\nStatistiques pour le " + dateStr + ":");
                    System.out.println("Nombre de réservations: " + nbReservations);
                    System.out.println("Nombre total d'articles: " + nbArticles);
                } else {
                    System.out.println(">> Aucune donnée pour cette date.");
                }
            }
        } catch (Exception e) {
            System.err.println(">> Erreur historique: " + e.getMessage());
        }
    }
    
    // ========= AUTRES MÉTHODES D'HISTORIQUE =========
    public void historiqueParCoureur() {
        System.out.println("\n=== HISTORIQUE PAR COUREUR ===");
        System.out.println("Fonctionnalité à implémenter...");
    }
    
    public void historiqueParTypeEpreuve() {
        System.out.println("\n=== HISTORIQUE PAR TYPE D'ÉPREUVE ===");
        System.out.println("Fonctionnalité à implémenter...");
    }
    
    public void historiqueQuantites() {
        System.out.println("\n=== QUANTITÉS RÉSERVÉES PAR ARTICLE ===");
        System.out.println("Fonctionnalité à implémenter...");
    }
    
    // ========= MÉTHODES PRIVÉES =========
    private void listerReservations() throws SQLException {
        String sql = """
            SELECT r.id, r.date_reservation, c.nom, c.prenom, te.libelle, r.validee
            FROM reservation r
            JOIN coureur c ON r.id_coureur = c.id
            JOIN type_epreuve te ON r.id_type_epreuve = te.id
            WHERE r.annulee = false
            ORDER BY r.date_reservation DESC
            LIMIT 20
            """;
            
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n=== DERNIÈRES RÉSERVATIONS ===");
            System.out.println("ID  | Date       | Coureur           | Épreuve              | Statut");
            System.out.println("----+------------+-------------------+----------------------+---------");
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("%3d | %s | %-15s | %-20s | %s\n",
                    rs.getInt("id"),
                    rs.getDate("date_reservation"),
                    rs.getString("prenom") + " " + rs.getString("nom"),
                    rs.getString("libelle"),
                    rs.getBoolean("validee") ? "Validée" : "En attente");
            }
            
            System.out.println("\nTotal affiché: " + count + " réservation(s)");
        }
    }
    
    private void afficherDetailsReservation(ResultSet rs) throws SQLException {
        System.out.println("\n=== DÉTAILS RÉSERVATION #" + rs.getInt("id") + " ===");
        System.out.println("Date: " + rs.getDate("date_reservation"));
        System.out.println("ID Coureur: " + rs.getInt("id_coureur"));
        System.out.println("ID Type Épreuve: " + rs.getInt("id_type_epreuve"));
        System.out.println("Validée: " + (rs.getBoolean("validee") ? "Oui" : "Non"));
        System.out.println("Annulée: " + (rs.getBoolean("annulee") ? "Oui" : "Non"));
    }
}