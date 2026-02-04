@echo off
echo ====================================
echo    COMPILATION JAVASTOCKS v3.0
echo ====================================

REM Créer le dossier bin s'il n'existe pas
if not exist bin mkdir bin

REM Se placer dans src
cd src

echo Compilation des fichiers sources...
javac -encoding UTF-8 -d ../bin JavaStocks/Main.java
javac -encoding UTF-8 -d ../bin JavaStocks/database/*.java
javac -encoding UTF-8 -d ../bin JavaStocks/dao/*.java
javac -encoding UTF-8 -d ../bin JavaStocks/services/*.java
javac -encoding UTF-8 -d ../bin JavaStocks/ui/*.java

REM Revenir à la racine
cd ..

echo.
echo ====================================
echo    EXECUTION DU PROGRAMME
echo ====================================
java -cp bin JavaStocks.Main

pause