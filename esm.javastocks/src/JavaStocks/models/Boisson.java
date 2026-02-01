package JavaStocks.models;

import JavaStocks.utils.Constants;

public class Boisson extends Article {
    private double volume; // en centilitres
    
    public Boisson(int id, String libelle, int quantite, double volume) {
        super(id, libelle, Constants.CATEGORIE_BOISSON, quantite);
        this.volume = volume;
    }
    
    public double getVolume() { return volume; }
    public void setVolume(double volume) { this.volume = volume; }
    
    @Override
    public String getDetailsSpecifiques() {
        return String.format("Volume: %.1f cl", volume);
    }
    
    @Override
    public boolean besoinReapprovisionnement() {
        return quantite <= Constants.SEUIL_BOISSON;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" | %.1f cl", volume);
    }
}