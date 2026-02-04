package JavaStocks.dao;

import JavaStocks.models.Coureur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des coureurs
 */
public class CoureurDAO {
    private Connection connection;
    
    public CoureurDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Crée un nouveau coureur
     */
    public boolean creer(Coureur coureur) {
        String sql = "INSERT INTO coureur (nom, prenom) VALUES (?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, coureur.getNom());
            stmt.setString(2, coureur.getPrenom());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    coureur.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur création coureur: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Récupère un coureur par son ID
     */
    public Coureur getById(int id) {
        String sql = "SELECT * FROM coureur WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Coureur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération coureur: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Récupère tous les coureurs
     */
    public List<Coureur> getAll() {
        List<Coureur> coureurs = new ArrayList<>();
        String sql = "SELECT * FROM coureur ORDER BY nom, prenom";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                coureurs.add(new Coureur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération coureurs: " + e.getMessage());
        }
        return coureurs;
    }
    
    /**
     * Met à jour un coureur
     */
    public boolean modifier(Coureur coureur) {
        String sql = "UPDATE coureur SET nom = ?, prenom = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, coureur.getNom());
            stmt.setString(2, coureur.getPrenom());
            stmt.setInt(3, coureur.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur modification coureur: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Supprime un coureur
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM coureur WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur suppression coureur: " + e.getMessage());
        }
        return false;
    }
}