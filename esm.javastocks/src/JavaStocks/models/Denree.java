package JavaStocks.models;

import JavaStocks.utils.Constants;

public class Denree extends Article {
    private double poids; // en grammes
    
    public Denree(int id, String libelle, int quantite, double poids) {
        super(id, libelle, Constants.CATEGORIE_DENREE, quantite);
        this.poids = poids;
    }
    
    public double getPoids() { return poids; }
    public void setPoids(double poids) { this.poids = poids; }
    
    @Override
    public String getDetailsSpecifiques() {
        return String.format("Poids: %.1f g", poids);
    }
    
    @Override
    public boolean besoinReapprovisionnement() {
        return quantite <= Constants.SEUIL_DENREE;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" | %.1f g", poids);
    }
}