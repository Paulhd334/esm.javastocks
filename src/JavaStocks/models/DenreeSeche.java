package JavaStocks.models;

public class DenreeSeche extends Article {
    private int poids; // en grammes
    
    // Constructeur vide
    public DenreeSeche() {
        super(0, "", "DS", 0); // Appelle le constructeur parent
    }
    
    // Constructeur complet
    public DenreeSeche(int id, String libelle, int quantite, int poids) {
        super(id, libelle, "DS", quantite); // "DS" pour Denrée Sèche
        this.poids = poids;
    }
    
    // Getters et setters
    public int getPoids() { return poids; }
    public void setPoids(int poids) { this.poids = poids; }
    
    // Implémentation des méthodes abstraites
    @Override
    public String getDetailsSpecifiques() {
        return "Poids: " + poids + "g";
    }
    
    @Override
    public boolean besoinReapprovisionnement() {
        return getQuantite() <= 100; // Seuil pour denrée sèche: 100
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" | Poids: %dg", poids);
    }
}