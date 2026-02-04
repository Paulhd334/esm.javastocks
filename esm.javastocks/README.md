# PROJET JAVA STOCKS - Association Web Courses

## Configuration initiale
1. Installer PostgreSQL
2. Créer la base de données : javastocks
3. Ajouter le driver PostgreSQL dans le dossier lib/

## Compilation et exécution
```bash
javac -cp "lib/*" -d bin src/JavaStocks/**/*.java
java -cp "bin:lib/*" JavaStocks.Main