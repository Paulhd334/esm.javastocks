package JavaStocks.models;

/**
 * Classe représentant une ligne dans une demande de réapprovisionnement
 */
public class LigneDemande {
    private Article article;
    private int quantite;
    
    public LigneDemande() {}
    
    public LigneDemande(Article article, int quantite) {
        this.article = article;
        this.quantite = quantite;
    }
    
    // Getters et setters
    public Article getArticle() { return article; }
    public void setArticle(Article article) { this.article = article; }
    
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    
    public double getMontant() {
        return quantite * 10.0; // Prix fictif
    }
    
    @Override
    public String toString() {
        return String.format("%s x %d", article.getLibelle(), quantite);
    }
}