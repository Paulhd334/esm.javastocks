package JavaStocks.models;

public class Coureur {
    private int id;
    private String nom;
    private String prenom;
    
    public Coureur() {}
    
    public Coureur(int id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }
    
    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    @Override
    public String toString() {
        return String.format("[%d] %s %s", id, prenom, nom);
    }
}