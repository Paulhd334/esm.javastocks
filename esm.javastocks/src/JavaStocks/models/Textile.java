package JavaStocks.models;

import JavaStocks.utils.Constants;

public class Textile extends Article {
    private String taille;
    private String couleur;
    
    public Textile(int id, String libelle, int quantite, String taille, String couleur) {
        super(id, libelle, Constants.CATEGORIE_TEXTILE, quantite);
        this.taille = taille;
        this.couleur = couleur;
    }
    
    // Getters & Setters
    public String getTaille() { return taille; }
    public void setTaille(String taille) { this.taille = taille; }
    
    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
    
    @Override
    public String getDetailsSpecifiques() {
        return "Taille: " + taille + " | Couleur: " + couleur;
    }
    
    @Override
    public boolean besoinReapprovisionnement() {
        return quantite <= Constants.SEUIL_TEXTILE;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" | T:%s C:%s", taille, couleur);
    }
}