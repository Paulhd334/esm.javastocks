package JavaStocks.models;

/**
 * Classe représentant un fournisseur dans le système Java Stocks
 */
public class Fournisseur {
    private int id;
    private String nom;
    private String rue;
    private String codePostal;
    private String ville;
    private String telephone;
    private String email;
    
    public Fournisseur() {}
    
    public Fournisseur(int id, String nom, String rue, String codePostal, 
                      String ville, String telephone, String email) {
        this.id = id;
        this.nom = nom;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.telephone = telephone;
        this.email = email;
    }
    
    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getRue() { return rue; }
    public void setRue(String rue) { this.rue = rue; }
    
    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    @Override
    public String toString() {
        return String.format("Fournisseur [ID: %d] %s - %s, %s %s", 
            id, nom, rue, codePostal, ville);
    }
}