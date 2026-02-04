package JavaStocks.services;

import java.sql.*;
import java.util.Scanner;

public class CoureurService {
    private Connection connection;
    private Scanner scanner;
    
    public CoureurService(Connection connection) {
        this.connection = connection;
        this.scanner = new Scanner(System.in);
    }
    
    // ========= CRÉER UN COUREUR =========
    public void creer() {
        System.out.println("\n=== NOUVEAU COUREUR ===");
        
        try {
            System.out.print("Nom: ");
            String nom = scanner.nextLine();
            
            System.out.print("Prénom: ");
            String prenom = scanner.nextLine();
            
            String sql = "INSERT INTO coureur (nom, prenom) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, nom);
            pstmt.setString(2, prenom);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    System.out.println("✅ Coureur créé avec ID: " + rs.getInt(1));
                }
            }
            
            pstmt.close();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur création: " + e.getMessage());
        }
    }
    
    // ========= MODIFIER UN COUREUR =========
    public void modifier() {
        System.out.println("\n=== MODIFICATION COUREUR ===");
        
        try {
            listerTous();
            System.out.print("\nID du coureur à modifier: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Nouveau nom (enter pour garder): ");
            String nouveauNom = scanner.nextLine();
            
            System.out.print("Nouveau prénom (enter pour garder): ");
            String nouveauPrenom = scanner.nextLine();
            
            String sql = "UPDATE coureur SET nom = COALESCE(?, nom), prenom = COALESCE(?, prenom) WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            
            if (!nouveauNom.isEmpty()) {
                pstmt.setString(1, nouveauNom);
            } else {
                pstmt.setNull(1, Types.VARCHAR);
            }
            
            if (!nouveauPrenom.isEmpty()) {
                pstmt.setString(2, nouveauPrenom);
            } else {
                pstmt.setNull(2, Types.VARCHAR);
            }
            
            pstmt.setInt(3, id);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Coureur modifié avec succès!");
            } else {
                System.out.println("❌ Coureur non trouvé!");
            }
            
            pstmt.close();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur modification: " + e.getMessage());
        }
    }
    
    // ========= CONSULTER UN COUREUR =========
    public void consulter() {
        System.out.println("\n=== CONSULTATION COUREUR ===");
        
        try {
            listerTous();
            System.out.print("\nID du coureur à consulter: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            String sql = "SELECT * FROM coureur WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("\n=== DÉTAILS COUREUR #" + id + " ===");
                System.out.println("Nom: " + rs.getString("nom"));
                System.out.println("Prénom: " + rs.getString("prenom"));
                System.out.println("Date inscription: " + rs.getDate("date_inscription"));
            } else {
                System.out.println("❌ Coureur non trouvé!");
            }
            
            pstmt.close();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur consultation: " + e.getMessage());
        }
    }
    
    // ========= LISTER TOUS LES COUREURS =========
    public void listerTous() {
        System.out.println("\n=== LISTE DES COUREURS ===");
        
        try {
            String sql = "SELECT * FROM coureur ORDER BY nom, prenom";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("ID  | Nom                 | Prénom");
            System.out.println("----+---------------------+-----------------");
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("%3d | %-20s | %-15s\n",
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"));
            }
            
            System.out.println("\nTotal: " + count + " coureur(s)");
            
            stmt.close();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur liste: " + e.getMessage());
        }
    }
    
    // ========= LISTER POUR MENU DÉROULANT =========
    public void listerCoureurs() {
        try {
            String sql = "SELECT id, nom, prenom FROM coureur ORDER BY nom";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                System.out.printf("%3d - %s %s\n",
                    rs.getInt("id"),
                    rs.getString("prenom"),
                    rs.getString("nom"));
            }
            
            stmt.close();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur liste: " + e.getMessage());
        }
    }
}