package JavaStocks.dao;

import JavaStocks.models.Article;
import JavaStocks.models.Textile;
import JavaStocks.models.Boisson;
import JavaStocks.models.DenreeSeche;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des articles
 */
public class ArticleDAO {
    private Connection connection;
    
    public ArticleDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Crée un nouvel article
     */
    public boolean creer(Article article) {
        String sql = "INSERT INTO article (libelle, quantite, categorie, taille, couleur, volume, poids, suppression_logique) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, article.getLibelle());
            stmt.setInt(2, article.getQuantite());
            
            // Déterminer la catégorie
            String categorie = "";
            String taille = null;
            String couleur = null;
            Integer volume = null;
            Integer poids = null;
            
            if (article instanceof Textile) {
                categorie = "T";
                Textile textile = (Textile) article;
                taille = textile.getTaille();
                couleur = textile.getCouleur();
            } else if (article instanceof Boisson) {
                categorie = "B";
                Boisson boisson = (Boisson) article;
                volume = boisson.getVolume();
            } else if (article instanceof DenreeSeche) {
                categorie = "DS";
                DenreeSeche denree = (DenreeSeche) article;
                poids = denree.getPoids();
            }
            
            stmt.setString(3, categorie);
            stmt.setString(4, taille);
            stmt.setString(5, couleur);
            
            if (volume != null) {
                stmt.setInt(6, volume);
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            
            if (poids != null) {
                stmt.setInt(7, poids);
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            
            stmt.setBoolean(8, false); // suppression_logique = false par défaut
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    article.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur création article: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Récupère un article par son ID
     */
    public Article getById(int id) {
        String sql = "SELECT * FROM article WHERE id = ? AND suppression_logique = false";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToArticle(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération article: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Récupère tous les articles
     */
    public List<Article> getAll() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM article WHERE suppression_logique = false ORDER BY libelle";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Article article = mapResultSetToArticle(rs);
                if (article != null) {
                    articles.add(article);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération articles: " + e.getMessage());
        }
        return articles;
    }
    
    /**
     * Met à jour un article
     */
    public boolean modifier(Article article) {
        String sql = "UPDATE article SET libelle = ?, quantite = ?, taille = ?, " +
                    "couleur = ?, volume = ?, poids = ? WHERE id = ? AND suppression_logique = false";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, article.getLibelle());
            stmt.setInt(2, article.getQuantite());
            
            // Déterminer les attributs spécifiques
            String taille = null;
            String couleur = null;
            Integer volume = null;
            Integer poids = null;
            
            if (article instanceof Textile) {
                Textile textile = (Textile) article;
                taille = textile.getTaille();
                couleur = textile.getCouleur();
            } else if (article instanceof Boisson) {
                Boisson boisson = (Boisson) article;
                volume = boisson.getVolume();
            } else if (article instanceof DenreeSeche) {
                DenreeSeche denree = (DenreeSeche) article;
                poids = denree.getPoids();
            }
            
            stmt.setString(3, taille);
            stmt.setString(4, couleur);
            
            if (volume != null) {
                stmt.setInt(5, volume);
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            
            if (poids != null) {
                stmt.setInt(6, poids);
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            
            stmt.setInt(7, article.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur modification article: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Suppression logique d'un article
     */
    public boolean supprimerLogique(int id) {
        String sql = "UPDATE article SET suppression_logique = true WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur suppression logique: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Récupère les articles dont le stock est inférieur au seuil
     */
    public List<Article> getArticlesEnRupture(int seuil) {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM article WHERE quantite <= ? AND suppression_logique = false ORDER BY quantite ASC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, seuil);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Article article = mapResultSetToArticle(rs);
                if (article != null) {
                    articles.add(article);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération articles en rupture: " + e.getMessage());
        }
        return articles;
    }
    
    /**
     * Met à jour la quantité d'un article
     */
    public boolean mettreAJourQuantite(int idArticle, int nouvelleQuantite) {
        String sql = "UPDATE article SET quantite = ? WHERE id = ? AND suppression_logique = false";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, nouvelleQuantite);
            stmt.setInt(2, idArticle);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour quantité: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Récupère la quantité actuelle d'un article
     */
    public int getQuantite(int idArticle) {
        String sql = "SELECT quantite FROM article WHERE id = ? AND suppression_logique = false";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idArticle);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("quantite");
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération quantité: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Mappe un ResultSet vers un objet Article
     */
    private Article mapResultSetToArticle(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String libelle = rs.getString("libelle");
        int quantite = rs.getInt("quantite");
        String categorie = rs.getString("categorie");
        
        // Nettoyer et normaliser la catégorie
        if (categorie != null) {
            categorie = categorie.trim().toUpperCase();
        }
        
        switch (categorie) {
            case "T":
                String taille = rs.getString("taille");
                String couleur = rs.getString("couleur");
                if (taille != null) taille = taille.trim();
                if (couleur != null) couleur = couleur.trim();
                return new Textile(id, libelle, quantite, taille, couleur);
                
            case "B":
                Integer volume = rs.getInt("volume");
                if (rs.wasNull()) volume = 0;
                return new Boisson(id, libelle, quantite, volume);
                
            case "DS":
                Integer poids = rs.getInt("poids");
                if (rs.wasNull()) poids = 0;
                return new DenreeSeche(id, libelle, quantite, poids);
                
            default:
                System.err.println("Catégorie inconnue: '" + categorie + "' pour l'article " + libelle);
                return null;
        }
    }
}