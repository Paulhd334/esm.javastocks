package JavaStocks.database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {
    
    public static void initializeDatabase(Connection connection) throws SQLException {
        System.out.println("\n=== INITIALISATION BASE DE DONNÉES ===");
        
        try (Statement stmt = connection.createStatement()) {
            
            // 1. Table ARTICLE
            String createArticle = """
                CREATE TABLE IF NOT EXISTS article (
                    id SERIAL PRIMARY KEY,
                    libelle VARCHAR(100) NOT NULL,
                    categorie CHAR(2) NOT NULL CHECK (categorie IN ('T', 'B', 'DS')),
                    quantite INTEGER NOT NULL DEFAULT 0,
                    taille VARCHAR(50),
                    couleur VARCHAR(30),
                    volume DECIMAL(10,2),
                    poids DECIMAL(10,2),
                    suppression_logique BOOLEAN DEFAULT FALSE,
                    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
            stmt.executeUpdate(createArticle);
            System.out.println("✅ Table 'article' créée");
            
            // 2. Table COUREUR
            String createCoureur = """
                CREATE TABLE IF NOT EXISTS coureur (
                    id SERIAL PRIMARY KEY,
                    nom VARCHAR(50) NOT NULL,
                    prenom VARCHAR(50) NOT NULL,
                    date_inscription DATE DEFAULT CURRENT_DATE
                )
                """;
            stmt.executeUpdate(createCoureur);
            System.out.println("✅ Table 'coureur' créée");
            
            // 3. Table TYPE_EPREUVE
            String createTypeEpreuve = """
                CREATE TABLE IF NOT EXISTS type_epreuve (
                    id SERIAL PRIMARY KEY,
                    libelle VARCHAR(100) NOT NULL UNIQUE
                )
                """;
            stmt.executeUpdate(createTypeEpreuve);
            System.out.println("✅ Table 'type_epreuve' créée");
            
            // 4. Table RESERVATION
            String createReservation = """
                CREATE TABLE IF NOT EXISTS reservation (
                    id SERIAL PRIMARY KEY,
                    date_reservation DATE NOT NULL,
                    id_coureur INTEGER REFERENCES coureur(id),
                    id_type_epreuve INTEGER REFERENCES type_epreuve(id),
                    validee BOOLEAN DEFAULT FALSE,
                    annulee BOOLEAN DEFAULT FALSE,
                    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
            stmt.executeUpdate(createReservation);
            System.out.println("✅ Table 'reservation' créée");
            
            // 5. Table LIGNE_RESERVATION
            String createLigneReservation = """
                CREATE TABLE IF NOT EXISTS ligne_reservation (
                    id_reservation INTEGER REFERENCES reservation(id),
                    id_article INTEGER REFERENCES article(id),
                    quantite INTEGER NOT NULL CHECK (quantite > 0),
                    PRIMARY KEY (id_reservation, id_article)
                )
                """;
            stmt.executeUpdate(createLigneReservation);
            System.out.println("✅ Table 'ligne_reservation' créée");
            
            // 6. Table FOURNISSEUR
            String createFournisseur = """
                CREATE TABLE IF NOT EXISTS fournisseur (
                    id SERIAL PRIMARY KEY,
                    nom VARCHAR(100) NOT NULL,
                    rue VARCHAR(200),
                    code_postal VARCHAR(10),
                    ville VARCHAR(100),
                    telephone VARCHAR(20),
                    email VARCHAR(100)
                )
                """;
            stmt.executeUpdate(createFournisseur);
            System.out.println("✅ Table 'fournisseur' créée");
            
            // 7. Table POINT_LIVRAISON
            String createPointLivraison = """
                CREATE TABLE IF NOT EXISTS point_livraison (
                    id SERIAL PRIMARY KEY,
                    nom VARCHAR(100) NOT NULL,
                    rue VARCHAR(200),
                    code_postal VARCHAR(10),
                    ville VARCHAR(100),
                    telephone VARCHAR(20),
                    email VARCHAR(100)
                )
                """;
            stmt.executeUpdate(createPointLivraison);
            System.out.println("✅ Table 'point_livraison' créée");
            
            // 8. Table RÉAPPROVISIONNEMENT
            String createReapprovisionnement = """
                CREATE TABLE IF NOT EXISTS reapprovisionnement (
                    id SERIAL PRIMARY KEY,
                    numero_commande VARCHAR(50) UNIQUE NOT NULL,
                    date_commande DATE NOT NULL,
                    id_fournisseur INTEGER REFERENCES fournisseur(id),
                    id_point_livraison INTEGER REFERENCES point_livraison(id),
                    motif CHAR(2) CHECK (motif IN ('R', 'NP', 'UR')),
                    statut VARCHAR(20) DEFAULT 'EN_COURS'
                )
                """;
            stmt.executeUpdate(createReapprovisionnement);
            System.out.println("✅ Table 'reapprovisionnement' créée");
            
            // 9. Table LIGNE_RÉAPPROVISIONNEMENT
            String createLigneReapprovisionnement = """
                CREATE TABLE IF NOT EXISTS ligne_reapprovisionnement (
                    id_reappro INTEGER REFERENCES reapprovisionnement(id),
                    id_article INTEGER REFERENCES article(id),
                    quantite INTEGER NOT NULL,
                    PRIMARY KEY (id_reappro, id_article)
                )
                """;
            stmt.executeUpdate(createLigneReapprovisionnement);
            System.out.println("✅ Table 'ligne_reapprovisionnement' créée");
            
            System.out.println("\n✅ Base de données initialisée avec succès!");
            
        } catch (SQLException e) {
            System.err.println("❌ Erreur initialisation BDD: " + e.getMessage());
            throw e;
        }
    }
    
    // Méthode pour insérer des données de test
    public static void insertSampleData(Connection connection) throws SQLException {
        System.out.println("\n=== INSERTION DONNÉES DE TEST ===");
        
        try (Statement stmt = connection.createStatement()) {
            // Données de test pour coureurs
            String insertCoureurs = """
                INSERT INTO coureur (nom, prenom) VALUES
                ('Berthollo', 'Pierre'),
                ('Brillat', 'Savarin'),
                ('Dupont', 'Jean'),
                ('Martin', 'Sophie')
                ON CONFLICT DO NOTHING
                """;
            stmt.executeUpdate(insertCoureurs);
            System.out.println("✅ Données coureurs insérées");
            
            // Données de test pour types d'épreuve
            String insertTypesEpreuve = """
                INSERT INTO type_epreuve (libelle) VALUES
                ('Course folle junior'),
                ('Marathon de Paris'),
                ('Trail des montagnes'),
                ('10 km urbain')
                ON CONFLICT DO NOTHING
                """;
            stmt.executeUpdate(insertTypesEpreuve);
            System.out.println("✅ Données types épreuve insérées");
            
            // Données de test pour articles
            String insertArticles = """
                INSERT INTO article (libelle, categorie, quantite, taille, couleur) VALUES
                ('T-shirt sport', 'T', 50, 'L', 'Bleu'),
                ('Short running', 'T', 30, 'M', 'Noir'),
                ('Bouteille 50cl', 'B', 200, NULL, NULL),
                ('Bouteille 1L', 'B', 150, NULL, NULL),
                ('Barre énergétique 50g', 'DS', 300, NULL, NULL),
                ('Gel énergétique', 'DS', 250, NULL, NULL)
                ON CONFLICT DO NOTHING
                """;
            stmt.executeUpdate(insertArticles);
            System.out.println("✅ Données articles insérées");
            
            System.out.println("✅ Données de test ajoutées avec succès!");
        }
    }
}