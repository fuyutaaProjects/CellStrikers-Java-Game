@echo off
title Lancement du jeu

del sources.txt 2>nul
if not exist bin (
    mkdir bin
)

echo Création de la liste des fichiers sources...
for /R src\main\java\com %%f in (*.java) do (
    echo %%f >> sources.txt
)

echo Copie des fichiers de ressources...
xcopy /E /Y /I src\resources\ bin\src\resources

echo Compilation des fichiers Java...
javac -d bin @sources.txt

echo Exécution du programme...
java -cp bin src.main.java.com.Game

pause