#!/bin/bash

#Lancement du jeu

rm -f sources.txt

mkdir -p dir

echo "CrÃ©ation de la liste des fichiers sources..."
find src/main/java/com -name "*java" > sources.txt

echo "Copie des ressources..."
cp -r src/resources bin/src/resources

echo "Compilation des fichiers Java..."
javac -d bin @sources.txt

echo "Execution du programme..."
java -cp bin src.main.java.com.Game