package JavaStocks.dao;

import JavaStocks.models.Fournisseur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des fournisseurs dans la base de données
 */
public class FournisseurDAO {
    private Connection connection;
    
    public FournisseurDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Crée un nouveau fournisseur
     */
    public boolean creer(Fournisseur fournisseur) {
        String sql = "INSERT INTO fournisseur (nom, rue, code_postal, ville, telephone, email) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, fournisseur.getNom());
            stmt.setString(2, fournisseur.getRue());
            stmt.setString(3, fournisseur.getCodePostal());
            stmt.setString(4, fournisseur.getVille());
            stmt.setString(5, fournisseur.getTelephone());
            stmt.setString(6, fournisseur.getEmail());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    fournisseur.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur création fournisseur: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Met à jour un fournisseur existant
     */
    public boolean modifier(Fournisseur fournisseur) {
        String sql = "UPDATE fournisseur SET nom = ?, rue = ?, code_postal = ?, " +
                    "ville = ?, telephone = ?, email = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fournisseur.getNom());
            stmt.setString(2, fournisseur.getRue());
            stmt.setString(3, fournisseur.getCodePostal());
            stmt.setString(4, fournisseur.getVille());
            stmt.setString(5, fournisseur.getTelephone());
            stmt.setString(6, fournisseur.getEmail());
            stmt.setInt(7, fournisseur.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur modification fournisseur: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Récupère un fournisseur par son ID
     */
    public Fournisseur getById(int id) {
        String sql = "SELECT * FROM fournisseur WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToFournisseur(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération fournisseur: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Récupère tous les fournisseurs
     */
    public List<Fournisseur> getAll() {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM fournisseur ORDER BY nom";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                fournisseurs.add(mapResultSetToFournisseur(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération fournisseurs: " + e.getMessage());
        }
        return fournisseurs;
    }
    
    /**
     * Supprime un fournisseur
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM fournisseur WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur suppression fournisseur: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Mappe un ResultSet vers un objet Fournisseur
     */
    private Fournisseur mapResultSetToFournisseur(ResultSet rs) throws SQLException {
        return new Fournisseur(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("rue"),
            rs.getString("code_postal"),
            rs.getString("ville"),
            rs.getString("telephone"),
            rs.getString("email")
        );
    }
}