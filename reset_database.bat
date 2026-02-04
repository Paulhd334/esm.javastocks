@echo off
echo ========================================
echo   REINITIALISATION BASE DE DONNEES
echo ========================================
echo.
echo Attention : Cette action va supprimer et recreer la base javastocks
echo.
set /p choice=Voulez-vous continuer ? (o/n): 
if /i "%choice%" neq "o" (
    echo Operation annulee.
    pause
    exit /b 0
)

echo.
echo [1/3] Suppression de l'ancienne base...
"C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -c "DROP DATABASE IF EXISTS javastocks;" 2>nul

echo [2/3] Creation de la nouvelle base...
"C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -c "CREATE DATABASE javastocks;" 2>nul

echo [3/3] Base 'javastocks' recreer avec succes !
echo.
echo Lancez l'application Java pour creer les tables automatiquement.
echo.
pause