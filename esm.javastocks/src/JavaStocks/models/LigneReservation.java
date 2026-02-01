package JavaStocks.models;

/**
 * Classe représentant une ligne dans une réservation
 */
public class LigneReservation {
    private Article article;
    private int quantite;
    
    public LigneReservation() {}
    
    public LigneReservation(Article article, int quantite) {
        this.article = article;
        this.quantite = quantite;
    }
    
    // Getters et setters
    public Article getArticle() { return article; }
    public void setArticle(Article article) { this.article = article; }
    
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    
    @Override
    public String toString() {
        return String.format("%s x %d", article.getLibelle(), quantite);
    }
}