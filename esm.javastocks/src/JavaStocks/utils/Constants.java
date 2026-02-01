package JavaStocks.utils;

public class Constants {
    // ========= CONFIGURATION BASE DE DONNÉES =========
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/javastocks";
    public static final String DB_USER = "postgres";      // ← MODIFIER ICI
    public static final String DB_PASSWORD = "password";  // ← MODIFIER ICI
    
    // ========= SEUILS DE RÉAPPROVISIONNEMENT =========
    public static final int SEUIL_TEXTILE = 10;
    public static final int SEUIL_BOISSON = 100;
    public static final int SEUIL_DENREE = 100;
    
    // ========= CATÉGORIES D'ARTICLES =========
    public static final String CATEGORIE_TEXTILE = "T";
    public static final String CATEGORIE_BOISSON = "B";
    public static final String CATEGORIE_DENREE = "DS";
    
    // ========= MOTIFS RÉAPPROVISIONNEMENT =========
    public static final String MOTIF_REAPPRO = "R";
    public static final String MOTIF_NOUVEAU_PRODUIT = "NP";
    public static final String MOTIF_URGENCE = "UR";
    
    // ========= MENUS =========
    public static final String[] MAIN_MENU = {
        "1. Gestion des articles",
        "2. Gestion des coureurs", 
        "3. Gestion des types d'épreuve",
        "4. Gestion des réservations",
        "5. Articles en rupture / Réservations en attente",
        "6. Consulter l'historique",
        "7. Gestion réapprovisionnement",
        "8. Quitter"
    };
}