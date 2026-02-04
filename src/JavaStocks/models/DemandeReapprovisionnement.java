package JavaStocks.models;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une demande de réapprovisionnement
 */
public class DemandeReapprovisionnement {
    private int id;
    private Date date;
    private String motif; // R, NP, UR
    private Fournisseur fournisseur;
    private PointLivraison pointLivraison;
    private List<LigneDemande> lignes;
    
    public DemandeReapprovisionnement() {
        this.lignes = new ArrayList<>();
    }
    
    public DemandeReapprovisionnement(int id, Date date, String motif,
                                     Fournisseur fournisseur, PointLivraison pointLivraison) {
        this();
        this.id = id;
        this.date = date;
        this.motif = motif;
        this.fournisseur = fournisseur;
        this.pointLivraison = pointLivraison;
    }
    
    /**
     * Ajoute une ligne à la demande de réapprovisionnement
     */
    public void ajouterLigne(Article article, int quantite) {
        LigneDemande ligne = new LigneDemande(article, quantite);
        lignes.add(ligne);
    }
    
    /**
     * Calcule le montant total de la demande
     */
    public double calculerMontantTotal() {
        double total = 0;
        for (LigneDemande ligne : lignes) {
            total += ligne.getQuantite() * 10.0; // Prix fictif à adapter
        }
        return total;
    }
    
    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    
    public Fournisseur getFournisseur() { return fournisseur; }
    public void setFournisseur(Fournisseur fournisseur) { this.fournisseur = fournisseur; }
    
    public PointLivraison getPointLivraison() { return pointLivraison; }
    public void setPointLivraison(PointLivraison pointLivraison) { this.pointLivraison = pointLivraison; }
    
    public List<LigneDemande> getLignes() { return lignes; }
    public void setLignes(List<LigneDemande> lignes) { this.lignes = lignes; }
    
    @Override
    public String toString() {
        return String.format("Demande Réappro [ID: %d] %s - %s", 
            id, motif, fournisseur.getNom());
    }
}