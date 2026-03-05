package src.main.java.com.controllers;

import src.main.java.com.Screen;
import src.main.java.com.models.Grid;
import src.main.java.com.views.GridView;
import src.main.java.com.views.sandboxEditor.SandboxMod;

import java.awt.event.MouseEvent;
/**
 * Classe qui hérite de la classe Interaction pour changer son comportement
 * afin de l'adapter pour la grille de cellule du mode sandbox
 * 
 * @author dy Elias
 * @version 1.1
 */
public class InteractionSandbox extends Interaction{
    SandboxMod sandboxView;

    /**
     * Constructeur de la classe qui permet d'initialiser les champs de sa super class @see Interaction
     * @param gridView GridView la classe qui gère l'affichage de la grille
     * @param screen Screen l'écran de jeu
     * @param gameGrid gameGrid la grille de cellule du jeu
     * @param canSelect boolean informe de si on peut sélectionner les cellules ou non
     */
    public InteractionSandbox(GridView gridView, Screen screen, Grid gameGrid, boolean canSelect) {
        super(gridView, screen, gameGrid, canSelect, true);
        this.inEditor = true;
        this.sandboxView = (SandboxMod) gridView;

        // Calcul du décalage
        int width = (sandboxView.getDisplayRange()[3] - sandboxView.getDisplayRange()[1]) * GridView.cellSize;
        int height = (sandboxView.getDisplayRange()[2] - sandboxView.getDisplayRange()[0]) * GridView.cellSize;
        offsetY = (screen.getHeight() - height) / 2;
        offsetX = (screen.getWidth() - width) / 2;
    }

    @Override
    public int mouseConvertX(MouseEvent e){
        return super.mouseConvertX(e) + sandboxView.getDisplayRange()[1];
    }

    @Override
    public int mouseConvertY(MouseEvent e){
        return super.mouseConvertY(e) + sandboxView.getDisplayRange()[0];
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = mouseConvertX(e);
        int y = mouseConvertY(e);
        selectCell(e,x,y);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = mouseConvertX(e);
        int y = mouseConvertY(e);
        deselectCell(x,y);
    }
}
