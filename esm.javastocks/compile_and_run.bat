@echo off
cd /d "C:\Users\Paul\Desktop\esm.javastocks"
title Compilation Java Stocks
color 0B

echo ========================================
echo   COMPILATION JAVA STOCKS
echo ========================================
echo.

echo [1/4] Preparation des dossiers...
if exist bin rmdir /s /q bin
mkdir bin >nul 2>&1

echo [2/4] Compilation des classes...
echo.
javac -cp "lib\postgresql.jar" -d bin ^
    src\JavaStocks\Main.java ^
    src\JavaStocks\models\*.java ^
    src\JavaStocks\database\*.java ^
    src\JavaStocks\services\*.java ^
    src\JavaStocks\ui\*.java ^
    src\JavaStocks\utils\*.java

if %errorlevel% neq 0 (
    echo.
    echo ❌ ERREUR DE COMPILATION
    echo Verifiez que :
    echo 1. Tous les fichiers Java sont presents
    echo 2. Le driver postgresql.jar est dans lib\
    echo 3. Java JDK est installe
    pause
    exit /b 1
)

echo.
echo [3/4] Compilation terminee avec succes !
echo.

echo [4/4] Lancement de l'application...
echo ========================================
echo   APPLICATION JAVA STOCKS
echo   Association Web Courses
echo ========================================
echo.
timeout /t 2 /nobreak >nul

java -cp "bin;lib\postgresql.jar" JavaStocks.Main

echo.
pause