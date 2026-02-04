package JavaStocks.dao;

import JavaStocks.models.ReservationEnAttente;
import JavaStocks.models.Coureur;
import JavaStocks.models.TypeEpreuve;
import JavaStocks.models.LigneReservation;
import JavaStocks.models.Article;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationEnAttenteDAO {
    private Connection connection;
    private CoureurDAO coureurDAO;
    private TypeEpreuveDAO typeEpreuveDAO;
    private ArticleDAO articleDAO;
    
    public ReservationEnAttenteDAO(Connection connection) {
        this.connection = connection;
        this.coureurDAO = new CoureurDAO(connection);
        this.typeEpreuveDAO = new TypeEpreuveDAO(connection);
        this.articleDAO = new ArticleDAO(connection);
    }
    
    public boolean creer(ReservationEnAttente reservation) {
        String sql = "INSERT INTO reservation_en_attente (date_reservation, coureur_id, " +
                    "type_epreuve_id, date_creation, statut) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, new java.sql.Date(reservation.getDate().getTime()));
            stmt.setInt(2, reservation.getCoureur().getId());
            stmt.setInt(3, reservation.getTypeEpreuve().getId());
            stmt.setDate(4, new java.sql.Date(reservation.getDateCreation().getTime()));
            stmt.setString(5, reservation.getStatut());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int reservationId = rs.getInt(1);
                    reservation.setId(reservationId);
                    
                    for (LigneReservation ligne : reservation.getLignes()) {
                        creerLigneReservation(reservationId, ligne);
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur création réservation en attente: " + e.getMessage());
        }
        return false;
    }
    
    private boolean creerLigneReservation(int reservationId, LigneReservation ligne) {
        String sql = "INSERT INTO ligne_reservation_attente (reservation_id, article_id, quantite) " +
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
    
    public List<ReservationEnAttente> getAll() {
        List<ReservationEnAttente> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation_en_attente WHERE statut = 'EN_ATTENTE' " +
                    "ORDER BY date_creation";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ReservationEnAttente reservation = mapResultSetToReservation(rs);
                if (reservation != null) {
                    reservation.setLignes(getLignesReservation(reservation.getId()));
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération réservations en attente: " + e.getMessage());
        }
        return reservations;
    }
    
    public boolean valider(int reservationId) {
        String sql = "UPDATE reservation_en_attente SET statut = 'VALIDE' WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur validation réservation: " + e.getMessage());
        }
        return false;
    }
    
    private List<LigneReservation> getLignesReservation(int reservationId) {
        List<LigneReservation> lignes = new ArrayList<>();
        String sql = "SELECT * FROM ligne_reservation_attente WHERE reservation_id = ?";
        
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
    
    private ReservationEnAttente mapResultSetToReservation(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        Date date = rs.getDate("date_reservation");
        int coureurId = rs.getInt("coureur_id");
        int typeEpreuveId = rs.getInt("type_epreuve_id");
        Date dateCreation = rs.getDate("date_creation");
        String statut = rs.getString("statut");
        
        Coureur coureur = coureurDAO.getById(coureurId);
        TypeEpreuve typeEpreuve = typeEpreuveDAO.getById(typeEpreuveId);
        
        if (coureur == null || typeEpreuve == null) {
            return null;
        }
        
        ReservationEnAttente reservation = new ReservationEnAttente(id, date, coureur, typeEpreuve, new ArrayList<>());
        reservation.setDateCreation(dateCreation);
        reservation.setStatut(statut);
        
        return reservation;
    }
}