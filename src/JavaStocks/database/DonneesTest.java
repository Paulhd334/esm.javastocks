package JavaStocks.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DonneesTest {
    
    public static void insererDonneesTest(Connection connection) throws SQLException {
        System.out.println("=== INSERTION DONNEES DE TEST ===");
        
        // 1. COUREURS
        insererCoureurs(connection);
        
        // 2. TYPES D'EPREUVE
        insererTypesEpreuve(connection);
        
        // 3. ARTICLES
        insererArticles(connection);
        
        // 4. FOURNISSEURS
        insererFournisseurs(connection);
        
        // 5. POINTS DE LIVRAISON
        insererPointsLivraison(connection);
    }
    
    private static void insererCoureurs(Connection connection) throws SQLException {
        String sql = "INSERT INTO coureur (nom, prenom) VALUES (?, ?) " +
                    "ON CONFLICT DO NOTHING";
        
        String[][] coureurs = {
            {"Dupont", "Jean"},
            {"Martin", "Marie"},
            {"Bernard", "Pierre"},
            {"Thomas", "Sophie"},
            {"Petit", "Luc"}
        };
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (String[] coureur : coureurs) {
                stmt.setString(1, coureur[0]);
                stmt.setString(2, coureur[1]);
                stmt.addBatch();
            }
            stmt.executeBatch();
            System.out.println("- Coureurs inseres");
        }
    }
    
    private static void insererTypesEpreuve(Connection connection) throws SQLException {
        String sql = "INSERT INTO type_epreuve (libelle) VALUES (?) " +
                    "ON CONFLICT DO NOTHING";
        
        String[] types = {
            "Marathon de Paris",
            "Semi-marathon de Lyon",
            "10km de Marseille",
            "Trail des Alpes",
            "Course de la Saint-Sylvestre"
        };
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (String type : types) {
                stmt.setString(1, type);
                stmt.addBatch();
            }
            stmt.executeBatch();
            System.out.println("- Types d'epreuve inseres");
        }
    }
    
    private static void insererArticles(Connection connection) throws SQLException {
        // Textiles
        String sqlTextile = "INSERT INTO article (libelle, quantite, categorie, taille, couleur) VALUES (?, ?, 'T', ?, ?) " +
                           "ON CONFLICT DO NOTHING";
        
String[][] textiles = {
    {"T-shirt Running", "100", "M", "Bleu"},
    {"Short de sport", "50", "L", "Noir"},
    {"Casquette", "200", "Unique", "Blanc"}  // <-- "Taille unique" → "Unique"
};
        
        try (PreparedStatement stmt = connection.prepareStatement(sqlTextile)) {
            for (String[] textile : textiles) {
                stmt.setString(1, textile[0]);
                stmt.setInt(2, Integer.parseInt(textile[1]));
                stmt.setString(3, textile[2]);
                stmt.setString(4, textile[3]);
                stmt.addBatch();
            }
            stmt.executeBatch();
            System.out.println("- Textiles inseres");
        }
        
        // Boissons
        String sqlBoisson = "INSERT INTO article (libelle, quantite, categorie, volume) VALUES (?, ?, 'B', ?) " +
                          "ON CONFLICT DO NOTHING";
        
        String[][] boissons = {
            {"Eau minerale 50cl", "500", "50"},
            {"Boisson energetique", "300", "50"},
            {"Jus d'orange", "200", "33"}
        };
        
        try (PreparedStatement stmt = connection.prepareStatement(sqlBoisson)) {
            for (String[] boisson : boissons) {
                stmt.setString(1, boisson[0]);
                stmt.setInt(2, Integer.parseInt(boisson[1]));
                stmt.setInt(3, Integer.parseInt(boisson[2]));
                stmt.addBatch();
            }
            stmt.executeBatch();
            System.out.println("- Boissons inserees");
        }
        
        // Denrees seches
        String sqlDenree = "INSERT INTO article (libelle, quantite, categorie, poids) VALUES (?, ?, 'DS', ?) " +
                         "ON CONFLICT DO NOTHING";
        
        String[][] denrees = {
            {"Barre energetique", "400", "50"},
            {"Gel energetique", "250", "30"},
            {"Fruits secs", "180", "200"}
        };
        
        try (PreparedStatement stmt = connection.prepareStatement(sqlDenree)) {
            for (String[] denree : denrees) {
                stmt.setString(1, denree[0]);
                stmt.setInt(2, Integer.parseInt(denree[1]));
                stmt.setInt(3, Integer.parseInt(denree[2]));
                stmt.addBatch();
            }
            stmt.executeBatch();
            System.out.println("- Denrees seches inserees");
        }
    }
    
    private static void insererFournisseurs(Connection connection) throws SQLException {
        String sql = "INSERT INTO fournisseur (nom, rue, code_postal, ville, telephone, email) VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT DO NOTHING";
        
        String[][] fournisseurs = {
            {"Sport Diffusion", "12 rue des Sports", "75015", "Paris", "01-45-78-12-34", "contact@sportdiffusion.fr"},
            {"Textile Pro", "45 avenue de l'Industrie", "69000", "Lyon", "04-78-12-34-56", "info@textilepro.fr"}
        };
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (String[] fournisseur : fournisseurs) {
                stmt.setString(1, fournisseur[0]);
                stmt.setString(2, fournisseur[1]);
                stmt.setString(3, fournisseur[2]);
                stmt.setString(4, fournisseur[3]);
                stmt.setString(5, fournisseur[4]);
                stmt.setString(6, fournisseur[5]);
                stmt.addBatch();
            }
            stmt.executeBatch();
            System.out.println("- Fournisseurs inseres");
        }
    }
    
    private static void insererPointsLivraison(Connection connection) throws SQLException {
        String sql = "INSERT INTO point_livraison (nom, rue, code_postal, ville, telephone, email) VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT DO NOTHING";
        
        String[][] points = {
            {"Entrepot Central", "Zone Industrielle Nord", "75019", "Paris", "01-40-12-34-56", "entrepot@javastocks.fr"},
            {"Depot Lyon", "15 rue de l'Entrepot", "69007", "Lyon", "04-72-34-56-78", "lyon@javastocks.fr"}
        };
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (String[] point : points) {
                stmt.setString(1, point[0]);
                stmt.setString(2, point[1]);
                stmt.setString(3, point[2]);
                stmt.setString(4, point[3]);
                stmt.setString(5, point[4]);
                stmt.setString(6, point[5]);
                stmt.addBatch();
            }
            stmt.executeBatch();
            System.out.println("- Points de livraison inseres");
        }
    }
}