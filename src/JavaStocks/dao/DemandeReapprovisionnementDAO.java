package JavaStocks.dao;

import JavaStocks.models.DemandeReapprovisionnement;
import JavaStocks.models.Fournisseur;
import JavaStocks.models.PointLivraison;
import JavaStocks.models.LigneDemande;
import JavaStocks.models.Article;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des demandes de réapprovisionnement
 */
public class DemandeReapprovisionnementDAO {
    private Connection connection;
    private FournisseurDAO fournisseurDAO;
    private PointLivraisonDAO pointLivraisonDAO;
    private ArticleDAO articleDAO;
    
    public DemandeReapprovisionnementDAO(Connection connection) {
        this.connection = connection;
        this.fournisseurDAO = new FournisseurDAO(connection);
        this.pointLivraisonDAO = new PointLivraisonDAO(connection);
        this.articleDAO = new ArticleDAO(connection);
    }
    
    /**
     * Crée une nouvelle demande de réapprovisionnement
     */
    public boolean creer(DemandeReapprovisionnement demande) {
        // Vérifier que la demande a des lignes
        if (demande.getLignes() == null || demande.getLignes().isEmpty()) {
            System.err.println("Erreur: La demande doit contenir au moins une ligne");
            return false;
        }
        
        String sql = "INSERT INTO demande_reappro (date_demande, motif, fournisseur_id, point_livraison_id) " +
                    "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, new java.sql.Date(demande.getDate().getTime()));
            stmt.setString(2, demande.getMotif());
            stmt.setInt(3, demande.getFournisseur().getId());
            stmt.setInt(4, demande.getPointLivraison().getId());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int demandeId = rs.getInt(1);
                    demande.setId(demandeId);
                    
                    // Créer les lignes de la demande
                    for (LigneDemande ligne : demande.getLignes()) {
                        if (!creerLigneDemande(demandeId, ligne)) {
                            System.err.println("Erreur lors de la création d'une ligne de demande");
                            return false;
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur création demande réappro: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Crée une ligne de demande
     */
    private boolean creerLigneDemande(int demandeId, LigneDemande ligne) {
        String sql = "INSERT INTO ligne_demande (demande_id, article_id, quantite) " +
                    "VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, demandeId);
            stmt.setInt(2, ligne.getArticle().getId());
            stmt.setInt(3, ligne.getQuantite());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur création ligne demande: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Récupère toutes les demandes
     */
    public List<DemandeReapprovisionnement> getAll() {
        List<DemandeReapprovisionnement> demandes = new ArrayList<>();
        String sql = "SELECT * FROM demande_reappro ORDER BY date_demande DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                DemandeReapprovisionnement demande = mapResultSetToDemande(rs);
                if (demande != null) {
                    List<LigneDemande> lignes = getLignesDemande(demande.getId());
                    if (lignes != null) {
                        demande.setLignes(lignes);
                    } else {
                        demande.setLignes(new ArrayList<>());
                    }
                    demandes.add(demande);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération demandes: " + e.getMessage());
            e.printStackTrace();
        }
        return demandes;
    }
    
    /**
     * Récupère une demande par son ID
     */
    public DemandeReapprovisionnement getById(int id) {
        String sql = "SELECT * FROM demande_reappro WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                DemandeReapprovisionnement demande = mapResultSetToDemande(rs);
                if (demande != null) {
                    List<LigneDemande> lignes = getLignesDemande(demande.getId());
                    if (lignes != null) {
                        demande.setLignes(lignes);
                    } else {
                        demande.setLignes(new ArrayList<>());
                    }
                }
                return demande;
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération demande: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Récupère les lignes d'une demande
     */
    private List<LigneDemande> getLignesDemande(int demandeId) {
        List<LigneDemande> lignes = new ArrayList<>();
        String sql = "SELECT * FROM ligne_demande WHERE demande_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, demandeId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int articleId = rs.getInt("article_id");
                Article article = articleDAO.getById(articleId);
                if (article != null) {
                    LigneDemande ligne = new LigneDemande(article, rs.getInt("quantite"));
                    lignes.add(ligne);
                } else {
                    System.err.println("Article non trouvé pour la ligne de demande: " + articleId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération lignes demande: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return lignes;
    }
    
    /**
     * Mappe un ResultSet vers un objet DemandeReapprovisionnement
     */
    private DemandeReapprovisionnement mapResultSetToDemande(ResultSet rs) throws SQLException {
        int fournisseurId = rs.getInt("fournisseur_id");
        int pointId = rs.getInt("point_livraison_id");
        
        Fournisseur fournisseur = fournisseurDAO.getById(fournisseurId);
        PointLivraison point = pointLivraisonDAO.getById(pointId);
        
        if (fournisseur == null) {
            System.err.println("Fournisseur non trouvé pour ID: " + fournisseurId);
            return null;
        }
        
        if (point == null) {
            System.err.println("Point de livraison non trouvé pour ID: " + pointId);
            return null;
        }
        
        DemandeReapprovisionnement demande = new DemandeReapprovisionnement(
            rs.getInt("id"),
            rs.getDate("date_demande"),
            rs.getString("motif"),
            fournisseur,
            point
        );
        
        // Initialiser la liste des lignes (elle sera remplie après)
        demande.setLignes(new ArrayList<>());
        
        return demande;
    }
    
    /**
     * Supprime une demande de réapprovisionnement
     */
    public boolean supprimer(int id) {
        // D'abord supprimer les lignes de la demande
        String sqlLignes = "DELETE FROM ligne_demande WHERE demande_id = ?";
        String sqlDemande = "DELETE FROM demande_reappro WHERE id = ?";
        
        try {
            // Supprimer les lignes
            try (PreparedStatement stmtLignes = connection.prepareStatement(sqlLignes)) {
                stmtLignes.setInt(1, id);
                stmtLignes.executeUpdate();
            }
            
            // Supprimer la demande
            try (PreparedStatement stmtDemande = connection.prepareStatement(sqlDemande)) {
                stmtDemande.setInt(1, id);
                return stmtDemande.executeUpdate() > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur suppression demande: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Met à jour une demande de réapprovisionnement
     */
    public boolean modifier(DemandeReapprovisionnement demande) {
        String sql = "UPDATE demande_reappro SET date_demande = ?, motif = ?, " +
                    "fournisseur_id = ?, point_livraison_id = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(demande.getDate().getTime()));
            stmt.setString(2, demande.getMotif());
            stmt.setInt(3, demande.getFournisseur().getId());
            stmt.setInt(4, demande.getPointLivraison().getId());
            stmt.setInt(5, demande.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur modification demande: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}