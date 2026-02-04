package JavaStocks.dao;

import JavaStocks.models.Reservation;
import JavaStocks.models.Coureur;
import JavaStocks.models.TypeEpreuve;
import JavaStocks.models.LigneReservation;
import JavaStocks.models.Article;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des réservations
 */
public class ReservationDAO {
    private Connection connection;
    private CoureurDAO coureurDAO;
    private TypeEpreuveDAO typeEpreuveDAO;
    private ArticleDAO articleDAO;
    
    public ReservationDAO(Connection connection) {
        this.connection = connection;
        this.coureurDAO = new CoureurDAO(connection);
        this.typeEpreuveDAO = new TypeEpreuveDAO(connection);
        this.articleDAO = new ArticleDAO(connection);
    }
    
    /**
     * Crée une nouvelle réservation
     */
    public boolean creer(Reservation reservation) {
        String sql = "INSERT INTO reservation (date_reservation, coureur_id, type_epreuve_id) " +
                    "VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, new java.sql.Date(reservation.getDate().getTime()));
            stmt.setInt(2, reservation.getCoureur().getId());
            stmt.setInt(3, reservation.getTypeEpreuve().getId());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int reservationId = rs.getInt(1);
                    reservation.setId(reservationId);
                    
                    // Créer les lignes de la réservation
                    for (LigneReservation ligne : reservation.getLignes()) {
                        creerLigneReservation(reservationId, ligne);
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur création réservation: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Crée une ligne de réservation
     */
    private boolean creerLigneReservation(int reservationId, LigneReservation ligne) {
        String sql = "INSERT INTO ligne_reservation (reservation_id, article_id, quantite) " +
                    "VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            stmt.setInt(2, ligne.getArticle().getId());
            stmt.setInt(3, ligne.getQuantite());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur création ligne réservation: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Récupère une réservation par son ID
     */
    public Reservation getById(int id) {
        String sql = "SELECT * FROM reservation WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération réservation: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Récupère toutes les réservations
     */
    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation ORDER BY date_reservation DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Reservation reservation = mapResultSetToReservation(rs);
                if (reservation != null) {
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération réservations: " + e.getMessage());
        }
        return reservations;
    }
    
    /**
     * Supprime une réservation
     */
    public boolean supprimer(int id) {
        // D'abord supprimer les lignes
        String sqlLignes = "DELETE FROM ligne_reservation WHERE reservation_id = ?";
        String sqlReservation = "DELETE FROM reservation WHERE id = ?";
        
        try {
            // Supprimer les lignes
            try (PreparedStatement stmtLignes = connection.prepareStatement(sqlLignes)) {
                stmtLignes.setInt(1, id);
                stmtLignes.executeUpdate();
            }
            
            // Supprimer la réservation
            try (PreparedStatement stmtReservation = connection.prepareStatement(sqlReservation)) {
                stmtReservation.setInt(1, id);
                return stmtReservation.executeUpdate() > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur suppression réservation: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Mappe un ResultSet vers un objet Reservation
     */
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        Date date = rs.getDate("date_reservation");
        int coureurId = rs.getInt("coureur_id");
        int typeEpreuveId = rs.getInt("type_epreuve_id");
        
        Coureur coureur = coureurDAO.getById(coureurId);
        TypeEpreuve typeEpreuve = typeEpreuveDAO.getById(typeEpreuveId);
        
        if (coureur == null || typeEpreuve == null) {
            return null;
        }
        
        Reservation reservation = new Reservation(id, date, coureur, typeEpreuve, new ArrayList<>());
        
        // Récupérer les lignes de la réservation
        List<LigneReservation> lignes = getLignesReservation(id);
        reservation.setLignes(lignes);
        
        return reservation;
    }
    
    /**
     * Récupère les lignes d'une réservation
     */
    private List<LigneReservation> getLignesReservation(int reservationId) {
        List<LigneReservation> lignes = new ArrayList<>();
        String sql = "SELECT * FROM ligne_reservation WHERE reservation_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int articleId = rs.getInt("article_id");
                Article article = articleDAO.getById(articleId);
                if (article != null) {
                    LigneReservation ligne = new LigneReservation(article, rs.getInt("quantite"));
                    lignes.add(ligne);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération lignes réservation: " + e.getMessage());
        }
        return lignes;
    }
}