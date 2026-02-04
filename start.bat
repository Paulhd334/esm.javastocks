@echo off
title Java Stocks - PostgreSQL Manager
color 0A
echo ========================================
echo   JAVA STOCKS - GESTION DES STOCKS
echo ========================================
echo.

echo [1/3] Demarrage de PostgreSQL 18...
net start "postgresql-x64-18" >nul 2>&1
if %errorlevel% equ 0 (
    echo   ✅ PostgreSQL 18 demarre
) else (
    echo   ℹ️  PostgreSQL deja en cours d'execution
)

echo [2/3] Test de connexion a la base de donnees...
"C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -d javastocks -c "SELECT 'Connexion OK' as status;" >nul 2>&1
if %errorlevel% equ 0 (
    echo   ✅ Base 'javastocks' accessible
) else (
    echo   ❌ Probleme de connexion
    echo   Verifiez le mot de passe dans Constants.java
)

echo [3/3] Lancement de l'application Java Stocks...
echo.
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo   ❌ Java non installe ou non dans le PATH
    echo   Telechargez JDK 17+ depuis : https://adoptium.net/
    pause
    exit /b 1
)

echo   ✅ Java disponible
echo.
echo ========================================
echo   LANCEMENT DE L'APPLICATION...
echo ========================================
echo.
pause