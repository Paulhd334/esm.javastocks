package JavaStocks;

import java.util.Random;
import java.util.Scanner;

public class Captcha {
    
    public static boolean validerCaptcha() {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        int num1 = random.nextInt(10) + 1;
        int num2 = random.nextInt(10) + 1;
        int somme = num1 + num2;

        System.out.println("\n=== VÉRIFICATION HUMAINE ===");
        System.out.println("Pour accéder à JavaStocks, résolvez :");
        System.out.println(num1 + " + " + num2 + " = ?");
        System.out.print("> Réponse : ");

        try {
            int reponse = scanner.nextInt();
            if (reponse == somme) {
                System.out.println(" CAPTCHA validé. Bienvenue dans JavaStocks.\n");
                return true;
            } else {
                System.out.println(" Réponse incorrecte. Accès refusé.\n");
                return false;
            }
        } catch (Exception e) {
            System.out.println(" Saisie invalide. CAPTCHA échoué.\n");
            return false;
        }
    }
}