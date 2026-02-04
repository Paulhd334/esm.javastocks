package JavaStocks.models;

import java.util.Date;
import java.util.List;

/**
 * Classe représentant une réservation
 */
public class Reservation {
    private int id;
    private Date date;
    private Coureur coureur;
    private TypeEpreuve typeEpreuve;
    private List<LigneReservation> lignes;
    
    public Reservation() {}
    
    public Reservation(int id, Date date, Coureur coureur, 
                      TypeEpreuve typeEpreuve, List<LigneReservation> lignes) {
        this.id = id;
        this.date = date;
        this.coureur = coureur;
        this.typeEpreuve = typeEpreuve;
        this.lignes = lignes;
    }
    
    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    
    public Coureur getCoureur() { return coureur; }
    public void setCoureur(Coureur coureur) { this.coureur = coureur; }
    
    public TypeEpreuve getTypeEpreuve() { return typeEpreuve; }
    public void setTypeEpreuve(TypeEpreuve typeEpreuve) { this.typeEpreuve = typeEpreuve; }
    
    public List<LigneReservation> getLignes() { return lignes; }
    public void setLignes(List<LigneReservation> lignes) { this.lignes = lignes; }
    
    @Override
    public String toString() {
        return String.format("Réservation [ID: %d] %s - %s", 
            id, coureur.getNom(), date);
    }
}