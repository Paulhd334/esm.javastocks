package JavaStocks.models;

public class Boisson extends Article {
    private int volume; // en cl
    
    // Constructeur vide
    public Boisson() {
        super(0, "", "B", 0); // "B" pour Boisson
    }
    
    // Constructeur complet
    public Boisson(int id, String libelle, int quantite, int volume) {
        super(id, libelle, "B", quantite);
        this.volume = volume;
    }
    
    // Getters et setters
    public int getVolume() { return volume; }
    public void setVolume(int volume) { this.volume = volume; }
    
    // Implémentation des méthodes abstraites
    @Override
    public String getDetailsSpecifiques() {
        return "Volume: " + volume + "cl";
    }
    
    @Override
    public boolean besoinReapprovisionnement() {
        return getQuantite() <= 100; // Seuil pour boisson: 100
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" | Volume: %dcl", volume);
    }
}