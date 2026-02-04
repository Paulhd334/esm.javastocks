📋 DOCUMENTATION TECHNIQUE - JavaStocks 

esm.javastocks/
├── src/
│   └── JavaStocks/
│       ├── Main.java                          # Point d'entrée + CAPTCHA
│       ├── Captcha.java                       # Module de vérification humaine
│       ├── database/
│       │   ├── DatabaseConnection.java        # Connexion PostgreSQL
│       │   ├── DatabaseInitializer.java       # Création des tables
│       │   └── DonneesTest.java               # Données de démonstration
│       ├── dao/ (Data Access Objects)
│       │   ├── ArticleDAO.java
│       │   ├── CoureurDAO.java
│       │   ├── TypeEpreuveDAO.java
│       │   ├── ReservationDAO.java
│       │   ├── ReservationEnAttenteDAO.java
│       │   ├── FournisseurDAO.java
│       │   ├── PointLivraisonDAO.java
│       │   └── DemandeReapprovisionnementDAO.java
│       ├── services/
│       │   ├── ArticleService.java
│       │   ├── CoureurService.java
│       │   ├── TypeEpreuveService.java
│       │   ├── ReservationService.java
│       │   └── ReapprovisionnementService.java
│       └── ui/
│           └── MenuManager.java               # Gestionnaire des menus
├── sql/
│   ├── creation_tables.sql                    # Script SQL des tables
│   └── donnees_test.sql                       # Données d'exemple
├── docs/                                      # Documentation
├── compile_and_run.bat                        # Script Windows
└── README.md


JavaStocks DB





+----------------+       +----------------+       +----------------+
|    Main        |       |  MenuManager   |       |   *Service     |
|----------------|       |----------------|       |----------------|
| + main()       |------>| + showMainMenu()|----->| + creer()      |
| + validerCaptcha()|   | + showMenuX()   |      | + modifier()   |
+----------------+       +----------------+       | + consulter()  |
                                                  | + supprimer()  |
                                                  +--------+-------+
                                                           |
                                                  +--------v-------+
                                                  |      *DAO      |
                                                  |----------------|
                                                  | + create()     |
                                                  | + read()       |
                                                  | + update()     |
                                                  | + delete()     |
                                                  +----------------+

