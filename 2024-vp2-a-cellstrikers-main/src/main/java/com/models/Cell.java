package src.main.java.com.models;

import src.main.java.com.views.*;

import java.util.ArrayList;

/**
 * La classe Cell représente une cellule dans la grille du jeu.
 * Elle contient des informations sur le type de cellule, sa direction, son état traité et si elle a changé.
 * Elle permet également de mettre à jour la cellule en fonction de son type et de sa direction.
 *
 * @author Ethan dy
 */
public class Cell {
    private CellType cellType;
    private Direction direction;
    private boolean treated;
    private boolean changed;

    /**
     * Constructeur de la classe Cell.
     *
     * @param cellType Type de la cellule (CellType).
     * @param direction Direction de la cellule (Direction).
     */
    public Cell(CellType cellType, Direction direction){
        this.cellType = cellType;
        this.direction = direction;
        this.treated = false;
        this.changed = false;
    }

    /**
     * Constructeur de la classe Cell.
     *
     * @param cellType Type de la cellule (CellType).
     */
    public Cell(CellType cellType){
        this.cellType = cellType;
        this.direction = Direction.N;
        this.treated = false;
    }

////////////////////////////////////////////////GETTER//////////////////////////////////////////////////////////////////

    public CellType getCellType() {
        return cellType;
    }
    public boolean isChanged() {
        return changed;
    }
    public Direction getOpposite(){
        return this.direction.getOpposite();
    }
    public int getI(){
        return this.direction.getI();
    }
    public int getJ(){
        return this.direction.getJ();
    }
    public Direction getDirection() {
        return direction;
    }
    public boolean isTreated() {
        return treated;
    }
///////////////////////////////////////////////////SETTER///////////////////////////////////////////////////////////////

    public void setTreated(boolean treated) {
        this.treated = treated;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
     * Permet de faire tourner la direction de la cellule.
     *
     * @param k int
     */
    public void turn(int k){
        this.direction = this.direction.turn(k);
    }

    /**
     * Permet de réaliser la copie d'une cellule pour ne pas mélanger les adresses mémoires.
     *
     * @return Cell
     */
    public Cell copy(){
        Cell cell = new Cell(this.cellType,this.direction);
        cell.setTreated(this.treated);
        return cell;
    }

    /**
     * Utile pour pouvoir générer le comportement futur de la cellule en fonction de son type.
     *
     * @param pos Position
     * @param grid Cell[][]
     * @param newGrid Cell[][]
     */
    public void update(Position pos, Cell[][] grid, Cell[][] newGrid, ArrayList<AnimaCell> animaGrid){
        cellType.update(pos,grid,newGrid, animaGrid);
    }
}