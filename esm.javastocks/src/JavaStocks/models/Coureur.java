package JavaStocks.models;

public class Coureur {
    private int id;
    private String nom;
    private String prenom;
    
    public Coureur(int id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }
    
    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    @Override
    public String toString() {
        return String.format("%4d | %-20s | %-15s", id, nom, prenom);
    }
    
    public void afficher() {
        System.out.println("\n=== COUREUR #" + id + " ===");
        System.out.println("Nom: " + nom);
        System.out.println("Prénom: " + prenom);
    }
}