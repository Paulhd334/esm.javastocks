package JavaStocks.models;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class ReservationEnAttente {
    private int id;
    private Date date;
    private Coureur coureur;
    private TypeEpreuve typeEpreuve;
    private List<LigneReservation> lignes;
    private Date dateCreation;
    private String statut; // "EN_ATTENTE", "VALIDE", "ANNULE"
    
    public ReservationEnAttente() {
        this.lignes = new ArrayList<>();
        this.dateCreation = new Date();
        this.statut = "EN_ATTENTE";
    }
    
    public ReservationEnAttente(int id, Date date, Coureur coureur, 
                               TypeEpreuve typeEpreuve, List<LigneReservation> lignes) {
        this();
        this.id = id;
        this.date = date;
        this.coureur = coureur;
        this.typeEpreuve = typeEpreuve;
        if (lignes != null) {
            this.lignes = lignes;
        }
    }
    
    public void ajouterLigne(Article article, int quantite) {
        LigneReservation ligne = new LigneReservation(article, quantite);
        this.lignes.add(ligne);
    }
    
    public void valider() {
        this.statut = "VALIDE";
    }
    
    public void annuler() {
        this.statut = "ANNULE";
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
    public void setLignes(List<LigneReservation> lignes) { 
        this.lignes = (lignes != null) ? lignes : new ArrayList<>(); 
    }
    
    public Date getDateCreation() { return dateCreation; }
    public void setDateCreation(Date dateCreation) { this.dateCreation = dateCreation; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    @Override
    public String toString() {
        return String.format("Réservation en attente [ID: %d] %s %s - %s", 
            id, coureur.getPrenom(), coureur.getNom(), statut);
    }
}