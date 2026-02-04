package JavaStocks.models;

public abstract class Article {
    protected int id;
    protected String libelle;
    protected String categorie;
    protected int quantite;
    protected boolean suppressionLogique;
    
    // Constructeur
    public Article(int id, String libelle, String categorie, int quantite) {
        this.id = id;
        this.libelle = libelle;
        this.categorie = categorie;
        this.quantite = quantite;
        this.suppressionLogique = false;
    }
    
    // ========= GETTERS & SETTERS =========
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { 
        if (quantite < 0) {
            throw new IllegalArgumentException("Quantité ne peut pas être négative");
        }
        this.quantite = quantite; 
    }
    
    public boolean isSuppressionLogique() { return suppressionLogique; }
    public void setSuppressionLogique(boolean suppressionLogique) { 
        this.suppressionLogique = suppressionLogique; 
    }
    
    // ========= MÉTHODES ABSTRAITES =========
    public abstract String getDetailsSpecifiques();
    public abstract boolean besoinReapprovisionnement();
    
    // ========= MÉTHODES CONCRÈTES =========
    public void augmenterStock(int quantite) {
        this.quantite += quantite;
    }
    
    public void diminuerStock(int quantite) {
        if (this.quantite < quantite) {
            throw new IllegalStateException("Stock insuffisant");
        }
        this.quantite -= quantite;
    }
    
    public void afficher() {
        System.out.println("\n=== ARTICLE #" + id + " ===");
        System.out.println("Libellé: " + libelle);
        System.out.println("Catégorie: " + categorie);
        System.out.println("Quantité: " + quantite);
        System.out.println("Détails: " + getDetailsSpecifiques());
        System.out.println("Suppression logique: " + (suppressionLogique ? "OUI" : "NON"));
    }
    
    @Override
    public String toString() {
        return String.format("%4d | %-30s | %-5s | %4d | %s",
            id, 
            libelle.length() > 30 ? libelle.substring(0, 27) + "..." : libelle,
            categorie,
            quantite,
            suppressionLogique ? "[SUPPRIMÉ]" : ""
        );
    }
}