package JavaStocks.services;

import java.sql.*;
import java.util.Scanner;

public class TypeEpreuveService {
    private Connection connection;
    private Scanner scanner;
    
    public TypeEpreuveService(Connection connection) {
        this.connection = connection;
        this.scanner = new Scanner(System.in);
    }
    
    // ========= CRÉER UN TYPE D'ÉPREUVE =========
    public void creer() {
        System.out.println("\n=== NOUVEAU TYPE D'ÉPREUVE ===");
        
        try {
            System.out.print("Libellé: ");
            String libelle = scanner.nextLine();
            
            String sql = "INSERT INTO type_epreuve (libelle) VALUES (?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, libelle);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    System.out.println(">> Type d'épreuve créé avec ID: " + rs.getInt(1));
                }
            }
            
            pstmt.close();
            
        } catch (Exception e) {
            System.err.println(">> Erreur création: " + e.getMessage());
        }
    }
    
    // ========= MODIFIER UN TYPE D'ÉPREUVE =========
    public void modifier() {
        System.out.println("\n=== MODIFICATION TYPE D'ÉPREUVE ===");
        
        try {
            listerTous();
            System.out.print("\nID du type à modifier: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Nouveau libellé: ");
            String nouveauLibelle = scanner.nextLine();
            
            String sql = "UPDATE type_epreuve SET libelle = ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, nouveauLibelle);
            pstmt.setInt(2, id);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println(">> Type d'épreuve modifié!");
            } else {
                System.out.println(">> Type non trouvé!");
            }
            
            pstmt.close();
            
        } catch (Exception e) {
            System.err.println(">> Erreur modification: " + e.getMessage());
        }
    }
    
    // ========= CONSULTER UN TYPE D'ÉPREUVE =========
    public void consulter() {
        System.out.println("\n=== CONSULTATION TYPE D'ÉPREUVE ===");
        
        try {
            listerTous();
            System.out.print("\nID du type à consulter: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            String sql = "SELECT * FROM type_epreuve WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("\n=== DÉTAILS TYPE #" + id + " ===");
                System.out.println("Libellé: " + rs.getString("libelle"));
            } else {
                System.out.println(">> Type non trouvé!");
            }
            
            pstmt.close();
            
        } catch (Exception e) {
            System.err.println(">> Erreur consultation: " + e.getMessage());
        }
    }
    
    // ========= LISTER TOUS LES TYPES =========
    public void listerTous() {
        System.out.println("\n=== LISTE DES TYPES D'ÉPREUVE ===");
        
        try {
            String sql = "SELECT * FROM type_epreuve ORDER BY libelle";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("ID  | Libellé");
            System.out.println("----+--------------------------------");
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("%3d | %-30s\n",
                    rs.getInt("id"),
                    rs.getString("libelle"));
            }
            
            System.out.println("\nTotal: " + count + " type(s)");
            
            stmt.close();
            
        } catch (Exception e) {
            System.err.println(">> Erreur liste: " + e.getMessage());
        }
    }
    
    // ========= LISTER POUR MENU DÉROULANT =========
    public void listerTypesEpreuve() {
        try {
            String sql = "SELECT id, libelle FROM type_epreuve ORDER BY libelle";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                System.out.printf("%3d - %s\n",
                    rs.getInt("id"),
                    rs.getString("libelle"));
            }
            
            stmt.close();
            
        } catch (Exception e) {
            System.err.println(">> Erreur liste: " + e.getMessage());
        }
    }
}