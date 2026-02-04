package JavaStocks.dao;

import JavaStocks.models.TypeEpreuve;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des types d'épreuve
 */
public class TypeEpreuveDAO {
    private Connection connection;
    
    public TypeEpreuveDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Crée un nouveau type d'épreuve
     */
    public boolean creer(TypeEpreuve typeEpreuve) {
        String sql = "INSERT INTO type_epreuve (libelle) VALUES (?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, typeEpreuve.getLibelle());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    typeEpreuve.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur création type épreuve: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Récupère un type d'épreuve par son ID
     */
    public TypeEpreuve getById(int id) {
        String sql = "SELECT * FROM type_epreuve WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new TypeEpreuve(
                    rs.getInt("id"),
                    rs.getString("libelle")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération type épreuve: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Récupère tous les types d'épreuve
     */
    public List<TypeEpreuve> getAll() {
        List<TypeEpreuve> types = new ArrayList<>();
        String sql = "SELECT * FROM type_epreuve ORDER BY libelle";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                types.add(new TypeEpreuve(
                    rs.getInt("id"),
                    rs.getString("libelle")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération types épreuve: " + e.getMessage());
        }
        return types;
    }
    
    /**
     * Met à jour un type d'épreuve
     */
    public boolean modifier(TypeEpreuve typeEpreuve) {
        String sql = "UPDATE type_epreuve SET libelle = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, typeEpreuve.getLibelle());
            stmt.setInt(2, typeEpreuve.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur modification type épreuve: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Supprime un type d'épreuve
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM type_epreuve WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur suppression type épreuve: " + e.getMessage());
        }
        return false;
    }
}