package src.main.java.com.controllers;

import src.main.java.com.Screen;
import src.main.java.com.models.Cell;
import src.main.java.com.models.CellType;
import src.main.java.com.models.Grid;
import src.main.java.com.models.Position;
import src.main.java.com.views.GridView;
import src.main.java.com.views.sandboxEditor.SandboxMod;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

/**
 * La classe Interaction gère les interactions de la souris avec la grille de jeu.
 * Elle permet de sélectionner, déplacer et interagir avec les cellules de la grille.
 * Elle est utilisée dans le mode éditeur et le mode jeu.
 *
 * @author Ethan dy
 */
public class Interaction extends MouseInputAdapter {
    // Décalage pour la souris
    public static int offsetX;
    public static int offsetY;

    // Information de la cellule qu'on déplace
    private Cell cell;
    private Position position;

    private GridView gridView;
    private Grid gameGrid;
    private Screen screen;

    // Boolean pour la gestion de l'éditeur
    private boolean canSelect;
    private boolean canDelete;
    private boolean canRotate;
    private boolean selectMode;
    protected boolean inEditor;
    private boolean changeInter;

    /**
     * Constructeur de la classe Interaction.
     *
     * @param gridView Affichage de la grille du jeu
     * @param screen Ecran pour afficher
     * @param gameGrid Grille du jeu
     * @param canSelect boolean condition si on peut sélectionner une cellule
     * @param inEditor boolean condition si on est dans l'éditeur
     */
    public Interaction(GridView gridView, Screen screen,Grid gameGrid,boolean canSelect, boolean inEditor) {
        this.gridView = gridView;
        this.screen = screen;
        this.gameGrid = gameGrid;
        this.canSelect = canSelect;
        this.canDelete = false;
        this.canRotate = false;
        this.changeInter = false;
        this.selectMode = true;
        this.inEditor = inEditor;

        // Calcul du décalage
        int width = gameGrid.getGrid()[0].length * GridView.cellSize;
        int height = gameGrid.getGrid().length * GridView.cellSize;
        offsetY = (screen.getHeight() - height) / 2;
        offsetX = (screen.getWidth() - width) / 2;
        this.cell = null;
    }

    /**
     * Permet de voir si la cellule qu'on sélectionne est une cellule qu'on a le droit de déplacer.
     *
     * @param c Cell cellule sélectionné
     * @return boolean True si on a le droit de la bouger, False sinon
     */
    public boolean isCellValid(Cell c){
        CellType type = c.getCellType();
        return !(type == CellType.WALL || type == CellType.ENEMY || type == CellType.WANDERER);
    }

    /**
     * Conversion des coordonnées X de la souris avec le décalage.
     *
     * @param e MouseEvent Information du clique de souris
     * @return int Coordonnée x
     */
    public int mouseConvertX(MouseEvent e) {
        return (e.getX()- offsetX) / GridView.cellSize;
    }

    /**
     * Conversion des coordonnées Y de la souris avec le décalage.
     *
     * @param e MouseEvent Information du clique de souris
     * @return int Coordonnée y
     */
    public int mouseConvertY(MouseEvent e) {
        return (e.getY() - offsetY) / GridView.cellSize;
    }

    public void mousePressed(MouseEvent e) {
        int x = mouseConvertX(e);
        int y = mouseConvertY(e);
        selectCell(e, x, y);
    }
    protected void selectCell(MouseEvent e, int x, int y){
        Position newposition = new Position(y,x);
        System.out.println("selectMode : " + selectMode);
        if (cell != null) {
            System.out.println("cell : " + cell.getCellType());
        }
        if (cell == null) {
            // On se place dans les interactions de l'éditeur
            if (inEditor) {
                // Cas de l'éditeur de niveau ou on veut supprimer une case
                if (canDelete && Grid.isValid(newposition, gameGrid.getGrid())){
                    cell = null;
                    gameGrid.getGrid()[newposition.getI()][newposition.getJ()] = cell;
                    return;
                }
                // Cas de l'éditeur ou on veut tourner une case
                if (canRotate && Grid.isValid(newposition,gameGrid.getGrid()) && newposition.getCell(gameGrid.getGrid()) != null){
                    cell = null;
                    gameGrid.getGrid()[newposition.getI()][newposition.getJ()].turn(1);
                    return;
                }
                // Cas de l'éditeur ou on change l'interaction d'une case
                if (changeInter && Grid.isValid(newposition,gameGrid.getGrid())){
                    cell = null;
                    gameGrid.getInteractableGrid()[newposition.getI()][newposition.getJ()] = !gameGrid.getInteractableGrid()[newposition.getI()][newposition.getJ()];
                    gridView.setbgSave(null);
                    return;
                }
            }

            if (selectMode){
                cell = null;
            }

            // On stocke la nouvelle position si on est pas dans l'éditeur
            if(position == null){
                position = newposition;
            }
            // On regarde si on est bien dans le jeu, sur la grille et sur une case qui autorise l'interaction
            if((!screen.isRunning() && canSelect && Grid.isValid(newposition, gameGrid.getGrid()) && gameGrid.getInteractableGrid()[y][x]) || inEditor){

                // On regarde si la case possède bien une cellule
                if(Grid.isValid(newposition, gameGrid.getGrid()) && gameGrid.getGrid()[y][x] != null){
                    cell = gameGrid.getGrid()[y][x];
                    // On regarde si la cellule est bien un cellule que le joueur peut déplace
                    // Le cas si le joueur est dans l'éditeur permet de déplacer les cellules interdites (WALL ou autre)
                    if(isCellValid(cell) || inEditor){
                        position = newposition;
                        Position deplacement = new Position(e.getX()-(GridView.cellSize/2), e.getY()-(GridView.cellSize/2));
                        gameGrid.getGrid()[y][x] = null;
                        gridView.startDrag(cell,deplacement); // On commence l'animation du drag and drop
                    }else{
                        // Cas ou le joueur n'a pas le droit donc on annule le déplacement
                        cell = null;
                    }
                }
            }
        }else{
            // Cas ou le joueur ne clique pas sur la grille
            if (!Grid.isValid(newposition, gameGrid.getGrid())) {
                if (!inEditor) {
                    cell = null;
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        int x = mouseConvertX(e);
        int y = mouseConvertY(e);
        deselectCell(x, y);
    }

    protected void deselectCell(int x, int y){
        // Si on ne stocke pas de position on stocke la position sur laquelle on relache la souris
        if(this.cell != null){
            // Si la position ou on relache est invalide on renvoie la cellule sur ça case de base
            if (!Grid.isValid(new Position(y, x), gameGrid.getGrid())){
                if (position != null){
                    gameGrid.getGrid()[position.getI()][position.getJ()] = cell.copy();
                }
            }
            if(position == null){
                position = new Position(y, x);
            }
            // Si les conditions sont valides pour poser on regarde la possibilité d'interaction avec la case
            if(!screen.isRunning() && canSelect && Grid.isValid(new Position(y, x), gameGrid.getGrid())){
                if (inEditor && selectMode) {
                    gameGrid.getGrid()[y][x] = cell.copy(); // Cas ou le joueur est dans l'éditeur
                    position = null;
                    cell = null;
                    gridView.stopDrag();
                    gridView.repaint();
                    return;
                }
                if (inEditor) {
                    gameGrid.getGrid()[y][x] = cell.copy(); // Cas ou le joueur est dans l'éditeur
                    position = null;
                    gridView.stopDrag();
                    gridView.repaint();
                    return;
                }
                // Si la case interdit d'interagir ou si la case est pleine on remet la cellule a sa place
                if(!gameGrid.getInteractableGrid()[y][x] || gameGrid.getGrid()[y][x] != null){
                    gameGrid.getGrid()[position.getI()][position.getJ()] = cell.copy();
                }else{
                    // On pose la cellule si tout est ok
                    gameGrid.getGrid()[y][x] = cell.copy();
                }
            }
        }
        // On reinitialise les informations de la cellule et on arrête la drag and drop
        cell = null;
        position = null;
        gridView.stopDrag();
        gridView.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        gridView.updateDrag(new Position(e.getX()-(GridView.cellSize/2), e.getY()-(GridView.cellSize/2)));
    }

    public void changeSelect(boolean canSelect){
        this.canSelect = canSelect;
    }

    /**
     * Change les informations envoyé par LevelEditorController pour que la gestion des interactions comprennent dans
     * quelle situation on se trouve.
     *
     * @param cell Cell cellule qu'on veut poser
     * @param deleteCell boolean condition si on veut supprimer une cellule
     * @param rotate boolean condition si on veut tourner une cellule
     * @param changeInter boolean condition pour changer l'interaction d'une case
     * @param inEditor boolean condition voir si le joueur est dans l'éditeur
     */
    public void changeCell(Cell cell,boolean deleteCell,boolean rotate,boolean changeInter,boolean selectMode, boolean inEditor){
        this.cell = cell;
        this.canDelete = deleteCell;
        this.canRotate = rotate;
        this.changeInter = changeInter;
        this.selectMode = selectMode;
        this.inEditor = inEditor;
    }

    public void setOffsetX(int decalageX) {
       offsetX = decalageX;
    }

    public void setOffsetY(int decalageY) {
        offsetY = decalageY;
    }

}
