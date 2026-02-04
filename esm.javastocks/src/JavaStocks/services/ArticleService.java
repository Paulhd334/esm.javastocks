package JavaStocks.services;

import JavaStocks.utils.Constants;
import java.sql.*;
import java.util.Scanner;

public class ArticleService {
    private Connection connection;
    private Scanner scanner;
    
    public ArticleService(Connection connection) {
        this.connection = connection;
        this.scanner = new Scanner(System.in);
    }
    
    // ========= CRÉER UN ARTICLE =========
    public void creer() {
        System.out.println("\n=== CRÉATION D'UN ARTICLE ===");
        
        try {
            System.out.print("Libellé: ");
            String libelle = scanner.nextLine();
            
            System.out.print("Catégorie (T=Textile, B=Boisson, DS=Denrée): ");
            String categorie = scanner.nextLine().toUpperCase();
            
            if (!categorie.matches("[TBD]S?")) {
                System.out.println("❌ Catégorie invalide!");
                return;
            }
            
            System.out.print("Quantité initiale: ");
            int quantite = Integer.parseInt(scanner.nextLine());
            
            String sql = "";
            PreparedStatement pstmt = null;
            
            switch (categorie) {
                case Constants.CATEGORIE_TEXTILE:
                    System.out.print("Taille (XS,S,M,L,XL): ");
                    String taille = scanner.nextLine();
                    System.out.print("Couleur: ");
                    String couleur = scanner.nextLine();
                    
                    sql = "INSERT INTO article (libelle, categorie, quantite, taille, couleur) VALUES (?, ?, ?, ?, ?)";
                    pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    pstmt.setString(1, libelle);
                    pstmt.setString(2, categorie);
                    pstmt.setInt(3, quantite);
                    pstmt.setString(4, taille);
                    pstmt.setString(5, couleur);
                    break;
                    
                case Constants.CATEGORIE_BOISSON:
                    System.out.print("Volume (cl): ");
                    double volume = Double.parseDouble(scanner.nextLine());
                    
                    sql = "INSERT INTO article (libelle, categorie, quantite, volume) VALUES (?, ?, ?, ?)";
                    pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    pstmt.setString(1, libelle);
                    pstmt.setString(2, categorie);
                    pstmt.setInt(3, quantite);
                    pstmt.setDouble(4, volume);
                    break;
                    
                case Constants.CATEGORIE_DENREE:
                    System.out.print("Poids (g): ");
                    double poids = Double.parseDouble(scanner.nextLine());
                    
                    sql = "INSERT INTO article (libelle, categorie, quantite, poids) VALUES (?, ?, ?, ?)";
                    pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    pstmt.setString(1, libelle);
                    pstmt.setString(2, categorie);
                    pstmt.setInt(3, quantite);
                    pstmt.setDouble(4, poids);
                    break;
            }
            
            if (pstmt != null) {
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    ResultSet rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        System.out.println("✅ Article créé avec ID: " + rs.getInt(1));
                    }
                }
                pstmt.close();
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erreur création: " + e.getMessage());
        }
    }
    
    // ========= MODIFIER UN ARTICLE =========
    public void modifier() {
        System.out.println("\n=== MODIFICATION D'UN ARTICLE ===");
        
        try {
            listerTous();
            System.out.print("\nID de l'article à modifier: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            String sqlSelect = "SELECT * FROM article WHERE id = ? AND suppression_logique = false";
            PreparedStatement pstmtSelect = connection.prepareStatement(sqlSelect);
            pstmtSelect.setInt(1, id);
            ResultSet rs = pstmtSelect.executeQuery();
            
            if (rs.next()) {
                afficherDetails(rs);
                
                System.out.println("\n--- NOUVELLES VALEURS ---");
                System.out.print("Nouveau libellé (enter pour garder): ");
                String nouveauLibelle = scanner.nextLine();
                
                System.out.print("Nouvelle quantité (enter pour garder): ");
                String nouvelleQteStr = scanner.nextLine();
                
                String sqlUpdate = "UPDATE article SET libelle = COALESCE(?, libelle), quantite = COALESCE(?, quantite) WHERE id = ?";
                PreparedStatement pstmtUpdate = connection.prepareStatement(sqlUpdate);
                
                if (!nouveauLibelle.isEmpty()) {
                    pstmtUpdate.setString(1, nouveauLibelle);
                } else {
                    pstmtUpdate.setNull(1, Types.VARCHAR);
                }
                
                if (!nouvelleQteStr.isEmpty()) {
                    pstmtUpdate.setInt(2, Integer.parseInt(nouvelleQteStr));
                } else {
                    pstmtUpdate.setNull(2, Types.INTEGER);
                }
                
                pstmtUpdate.setInt(3, id);
                
                int rows = pstmtUpdate.executeUpdate();
                if (rows > 0) {
                    System.out.println("✅ Article modifié avec succès!");
                }
                
                pstmtUpdate.close();
            } else {
                System.out.println("❌ Article non trouvé ou supprimé!");
            }
            
            pstmtSelect.close();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur modification: " + e.getMessage());
        }
    }
    
    // ========= CONSULTER UN ARTICLE =========
    public void consulter() {
        System.out.println("\n=== CONSULTATION D'UN ARTICLE ===");
        
        try {
            listerTous();
            System.out.print("\nID de l'article à consulter: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            String sql = "SELECT * FROM article WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                afficherDetails(rs);
            } else {
                System.out.println("❌ Article non trouvé!");
            }
            
            pstmt.close();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur consultation: " + e.getMessage());
        }
    }
    
    // ========= SUPPRESSION LOGIQUE =========
    public void supprimerLogique() {
        System.out.println("\n=== SUPPRESSION LOGIQUE ===");
        
        try {
            listerTous();
            System.out.print("\nID de l'article à supprimer: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            String sql = "UPDATE article SET suppression_logique = true WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Article supprimé logiquement!");
            } else {
                System.out.println("❌ Article non trouvé!");
            }
            
            pstmt.close();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur suppression: " + e.getMessage());
        }
    }
    
    // ========= LISTER TOUS LES ARTICLES =========
    public void listerTous() {
        System.out.println("\n=== LISTE DES ARTICLES ===");
        System.out.println("ID  | Libellé                       | Cat  | Qte | Détails");
        System.out.println("----+-------------------------------+------+-----+----------------");
        
        try {
            String sql = "SELECT * FROM article WHERE suppression_logique = false ORDER BY id";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("%3d | %-30s | %-4s | %3d | ",
                    rs.getInt("id"),
                    rs.getString("libelle"),
                    rs.getString("categorie"),
                    rs.getInt("quantite"));
                
                String categorie = rs.getString("categorie");
                switch (categorie) {
                    case "T":
                        System.out.printf("T:%s C:%s", rs.getString("taille"), rs.getString("couleur"));
                        break;
                    case "B":
                        System.out.printf("%.1f cl", rs.getDouble("volume"));
                        break;
                    case "DS":
                        System.out.printf("%.1f g", rs.getDouble("poids"));
                        break;
                }
                System.out.println();
            }
            
            System.out.println("\nTotal: " + count + " article(s)");
            
            stmt.close();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur liste: " + e.getMessage());
        }
    }
    
    // ========= LISTER ARTICLES EN RUPTURE =========
    public void listerRuptures() {
        System.out.println("\n=== ARTICLES EN RUPTURE ===");
        
        try {
            String sql = "SELECT * FROM article WHERE suppression_logique = false AND " +
                        "((categorie = 'T' AND quantite <= ?) OR " +
                        "(categorie = 'B' AND quantite <= ?) OR " +
                        "(categorie = 'DS' AND quantite <= ?))";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, Constants.SEUIL_TEXTILE);
            pstmt.setInt(2, Constants.SEUIL_BOISSON);
            pstmt.setInt(3, Constants.SEUIL_DENREE);
            
            ResultSet rs = pstmt.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("⚠️  ID:%3d | %-30s | %-4s | Stock:%3d | ",
                    rs.getInt("id"),
                    rs.getString("libelle"),
                    rs.getString("categorie"),
                    rs.getInt("quantite"));
                
                if (rs.getString("categorie").equals("T")) {
                    System.out.printf("Seuil:%d", Constants.SEUIL_TEXTILE);
                } else {
                    System.out.printf("Seuil:%d", Constants.SEUIL_BOISSON);
                }
                System.out.println();
            }
            
            if (count == 0) {
                System.out.println("✅ Aucun article en rupture de stock!");
            } else {
                System.out.println("\n⚠️  " + count + " article(s) nécessite(nt) réapprovisionnement!");
            }
            
            pstmt.close();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur liste ruptures: " + e.getMessage());
        }
    }
    
    // ========= AFFICHER DÉTAILS ARTICLE =========
    private void afficherDetails(ResultSet rs) throws SQLException {
        System.out.println("\n=== DÉTAILS ARTICLE #" + rs.getInt("id") + " ===");
        System.out.println("Libellé: " + rs.getString("libelle"));
        System.out.println("Catégorie: " + rs.getString("categorie"));
        System.out.println("Quantité: " + rs.getInt("quantite"));
        
        String categorie = rs.getString("categorie");
        switch (categorie) {
            case "T":
                System.out.println("Taille: " + rs.getString("taille"));
                System.out.println("Couleur: " + rs.getString("couleur"));
                break;
            case "B":
                System.out.println("Volume: " + rs.getDouble("volume") + " cl");
                break;
            case "DS":
                System.out.println("Poids: " + rs.getDouble("poids") + " g");
                break;
        }
        
        System.out.println("Suppression logique: " + (rs.getBoolean("suppression_logique") ? "OUI" : "NON"));
        System.out.println("Date création: " + rs.getTimestamp("date_creation"));
        
        // Vérifier seuil
        int seuil = 0;
        switch (categorie) {
            case "T": seuil = Constants.SEUIL_TEXTILE; break;
            case "B": 
            case "DS": seuil = Constants.SEUIL_BOISSON; break;
        }
        
        if (rs.getInt("quantite") <= seuil) {
            System.out.println("⚠️  ALERTE: Stock faible (" + rs.getInt("quantite") + " ≤ " + seuil + ")");
        }
    }
    
    // ========= VÉRIFIER DISPONIBILITÉ =========
    public boolean verifierDisponibilite(int idArticle, int quantiteDemandee) {
        try {
            String sql = "SELECT quantite FROM article WHERE id = ? AND suppression_logique = false";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, idArticle);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int quantiteStock = rs.getInt("quantite");
                return quantiteStock >= quantiteDemandee;
            }
            
            pstmt.close();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur vérification: " + e.getMessage());
        }
        return false;
    }
    
    // ========= MISE À JOUR STOCK =========
    public void mettreAJourStock(int idArticle, int delta) {
        try {
            String sql = "UPDATE article SET quantite = quantite + ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, delta);
            pstmt.setInt(2, idArticle);
            
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                System.out.println("⚠️  Article non trouvé pour mise à jour stock");
            }
            
            pstmt.close();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur mise à jour stock: " + e.getMessage());
        }
    }
}