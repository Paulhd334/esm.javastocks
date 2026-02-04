package JavaStocks.models;

public class TypeEpreuve {
    private int id;
    private String libelle;
    
    public TypeEpreuve(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }
    
    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    
    @Override
    public String toString() {
        return String.format("%4d | %-30s", id, libelle);
    }
    
    public void afficher() {
        System.out.println("\n=== TYPE ÉPREUVE #" + id + " ===");
        System.out.println("Libellé: " + libelle);
    }
}