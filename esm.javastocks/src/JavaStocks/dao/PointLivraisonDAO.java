package JavaStocks.dao;

import JavaStocks.models.PointLivraison;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des points de livraison
 */
public class PointLivraisonDAO {
    private Connection connection;
    
    public PointLivraisonDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Crée un nouveau point de livraison
     */
    public boolean creer(PointLivraison point) {
        String sql = "INSERT INTO point_livraison (nom, rue, code_postal, ville, telephone, email) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, point.getNom());
            stmt.setString(2, point.getRue());
            stmt.setString(3, point.getCodePostal());
            stmt.setString(4, point.getVille());
            stmt.setString(5, point.getTelephone());
            stmt.setString(6, point.getEmail());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    point.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur création point livraison: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Récupère tous les points de livraison
     */
    public List<PointLivraison> getAll() {
        List<PointLivraison> points = new ArrayList<>();
        String sql = "SELECT * FROM point_livraison ORDER BY nom";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                points.add(mapResultSetToPoint(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération points livraison: " + e.getMessage());
        }
        return points;
    }
    
    /**
     * Récupère un point par son ID
     */
    public PointLivraison getById(int id) {
        String sql = "SELECT * FROM point_livraison WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPoint(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération point livraison: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Mappe un ResultSet vers un objet PointLivraison
     */
    private PointLivraison mapResultSetToPoint(ResultSet rs) throws SQLException {
        return new PointLivraison(
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