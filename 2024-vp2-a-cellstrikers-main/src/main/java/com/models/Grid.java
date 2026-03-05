package src.main.java.com.models;

import java.util.ArrayList;
import java.util.List;
import src.main.java.com.views.*;

/**
 * Cette classe représente la grille des cellules du jeu
 * Elle contient la sous-classe @see GridCheckers
 * 
 * <h4>Voici les principales méthodes de la classe: </h4>
 * 
 * <ul>
 * <li><strong>updateAll()</strong> Méthode permettant de faire fonctionner un cycle du jeu
 * en appellant les méthodes update des 8 cellules ayant un impact direct sur
 * les autres (les 8 premières de l'enum CellType)</li>
 * <li><strong>newGrid()</strong> Méthode permettant de copier la grille de l'état actuel dans celle du 
 * prochain état</li>
 * <li><strong>static boolean isValid(Position pos, Cell[][] grid)</strong> Méthode permettant d'informer si la case est valide</li>
 * <li><strong>int getRemainingEnnemies()</strong> Méthode permettant d'obtenir le nombre d'ennemis restant sur la grille</li>
 * <li><strong>static boolean canMove(Position pos, Direction dir, Cell[][] grid)</strong> Méthode permettant d'informer 
 * si une cellule sur la grille à une position donnée peut bouger vers une direction donnée</li>
 * <li><strong>static void move(Position pos, Direction dir, Cell[][] grid ,Cell[][] newGrid)</strong> Fonction permettant
 * de faire avancer la cellule dans la grille de cellule qui gère le jeu</li>
 * </ul>
 * 
 * @author Elias dy
 * @version 2.1
 */
public class Grid {

    private Cell[][] grid;
    private Cell[][] newGrid;
    public static ArrayList<AnimaCell> animaList;
    private boolean[][] interactGrid;// Utile pour les cases qu'on a le droit de bouger
    public static final int cellNumber = 8; //Le nombre de type de cellules opératrices différentes

    private CellType[] order = {
        CellType.REPULSE, CellType.SWITCHER, CellType.GENERATOR, CellType.SPINNERLEFT, CellType.SPINNERRIGHT,
        CellType.MOVER, CellType.NUDGE, CellType.WANDERER, CellType.HITMAN
    };// Ordre de priorité d'appel des cellules

    /**
     * Constructeur de la classe Grid qui permet d'initialiser les attributs de la classe
     * @param grid Cell[][] la grille de cellule
     * @param interactableSpace boolean[][] les cases de la grille où l'on peut déplacer les cellules
     */
    public Grid(Cell[][] grid, boolean[][] interactableSpace){
        this.grid = grid;
        this.newGrid();
        this.interactGrid = interactableSpace;
    }

///////////////////////////////////////////////////GETTER///////////////////////////////////////////////////////////////

    public Cell[][] getGrid() {
        return grid;
    }
    public boolean[][] getInteractableGrid(){
        return interactGrid;
    }
    public ArrayList<AnimaCell> getAnimaList(){
        return animaList;
    }


    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }
    public void setInteractableGrid(boolean[][] interactableSpace){
        this.interactGrid = interactableSpace;
    }
    public void setOrder(CellType[] order) {
        this.order = order;
    }
    public static void resetAnimaList(){
        animaList = null;
    }

    /**
     * Méthode permettant de faire fonctionner un cycle du jeu
     * en appellant les méthodes update des cellules ayant un impact direct sur
     * les autres (elles se trouvent dans order)
     */
    public void updateAll(){
        animaList = new ArrayList<AnimaCell>();
        for (int i = 0; i < cellNumber; i++) {
            for (int j = 0; j < grid.length; j++) {
                for (int k = 0; k < grid[j].length; k++) {
                    Cell cell = this.grid[j][k];

                    if(cell != null && cell.getCellType() == order[i] && !cell.isTreated()){
                        cell.setTreated(true);
                        cell.update(new Position(j, k), grid, newGrid, animaList);
                        this.swapGrid();
                    }
                }
            }
        }
        this.resetTreated();
    }

    /**
     * Méthode permettant de passer de l'état actuel au prochain état
     */
    private void swapGrid(){
        this.grid = this.newGrid;
        this.newGrid();
    }

    /**
     * Méthode permettant de copier la grille de l'état actuel dans celle du 
     * prochain état
     */
    public void newGrid(){
        this.newGrid = new Cell[this.grid.length][];
        for (int i = 0; i < this.grid.length; i++) {
            this.newGrid[i] = new Cell[this.grid[i].length];
            for (int j = 0; j < this.grid[i].length; j++) {
                this.newGrid[i][j] = this.grid[i][j];
            }
        }
    }

    /**
     * Méthode qui met le champ treated des cellules à false
     */
    private void resetTreated(){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Cell actual = grid[i][j];
                if( actual != null){
                    actual.setTreated(false);
                }
            }
        }
    }
    /**
     * Méthode qui met le champ changed des cellules à false
     */
    public void resetChanged(){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                
                Cell actual = grid[i][j];
                if( actual != null){
                    actual.setChanged(false);
                }
            }
        }
    }

    /**
     * Méthode permettant d'obtenir le nombre d'ennemis restant sur la grille
     * @return int le nombre d'ennemis restant sur la grille
     */
    public int getRemainingEnnemies(){
        int ret = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Position pos = new Position(i, j);
                if(grid[pos.getI()][pos.getJ()] != null && (grid[pos.getI()][pos.getJ()].getCellType() == CellType.ENEMY || grid[pos.getI()][pos.getJ()].getCellType() == CellType.WANDERER || grid[i][j].getCellType() == CellType.BOSS)){
                    ret++;
                }
            }
        }
        return ret;
    }

    /**
     * Méthode permettant d'informer si la case est valide
     * @param pos Position la position de la case
     * @return true si c'est le cas, false sinon
     */
    public static boolean isValid(Position pos, Cell[][] grid){
        return pos.isValid(grid.length, grid[0].length);
    }

    /**
     * Méthode permettant d'informer si une cellule donnée peut se déplacer vers une direction donnée
     * étant donné son environnement
     * @param pos Position la position de la cellule
     * @param dir Direction la direction à laquelle la cellule va se déplacer
     * @param grid Cell[][] la grille de cellule
     * @return true si c'est le cas, false sinon
     */
    public static boolean canMove(Position pos, Direction dir ,Cell[][] grid){
        Position newPos = pos.getFrontPosition(dir);
        // Cas ou les positions données ne sont pas valide.
        if(!Grid.isValid(pos, grid)){
            return false;
        }
        if(!Grid.isValid(newPos, grid)){
            return false;
        }
        Cell treated = newPos.getCell(grid);
        boolean isMoveable = pos.getCell(grid).getCellType().canMove(pos.getCell(grid).getDirection(),dir);

        // Regarde si la cellule peut bouger
        if(treated != null && isMoveable){
            CellType treatedType = treated.getCellType();
            if(treatedType == CellType.ENEMY || treatedType == CellType.TRASH || treatedType == CellType.WANDERER || treatedType == CellType.BOSS){
                return true;
            }
            isMoveable = treated.getCellType().canMove(treated.getDirection(),dir);
        }
        return
                //Check si la case où on veux aller est vide
                ((treated == null) ||
                        //Check si la case où on veux aller est occupé et si oui check si l'on peux bouger la case ainsi que celle derrière si besoin
                        ((treated != null) && (isMoveable) && (canMove(newPos, dir, grid)))
                );
    }

    /**
     * Fonction permettant de faire avancer la cellule dans la grille de cellule qui gère le jeu
     * @param pos Position la position de la cellule
     * @param dir Direction la direction ou la cellule va bouger
     * @param grid Cell[][] la grille de l'état courant
     * @param newGrid Cell[][] la grille du prochain état
     */
    public static void move(Position pos, Direction dir, Cell[][] grid ,Cell[][] newGrid, ArrayList<AnimaCell> animaList){

        if(grid[pos.getI()][pos.getJ()] != null){
            Position newPos = pos.getFrontPosition(dir);

            if(newGrid[newPos.getI()][newPos.getJ()] != null){
                CellType type = newGrid[newPos.getI()][newPos.getJ()].getCellType();

                if (type == CellType.BOSS) {
                    // le boss absorbe la cellule qu'on traite si ce n'est pas une Cell HITMAN.
                    if (grid[pos.getI()][pos.getJ()].getCellType() == CellType.HITMAN) {
                        System.out.println("Hitman will collide with boss");
                        newGrid[pos.getI()][pos.getJ()] = null; // retirer la cellule qu'on traite
                        newGrid[newPos.getI()][newPos.getJ()] = null; // retirer le boss
                    } else {
                        // le boss tue toute cellule qui rentre en contact si ce n'est pas HITMAN
                        newGrid[pos.getI()][pos.getJ()] = null;
                    }
                    newGrid[pos.getI()][pos.getJ()] = null;
                }


                if(type == CellType.ENEMY || type == CellType.WANDERER){
                    // La cellule hitman ne meurt jamais
                    if (grid[pos.getI()][pos.getJ()].getCellType() == CellType.HITMAN) {
//                        AnimaCell.addToAnimaList(animaList, new AnimaCell("move", newGrid[newPos.getI()][newPos.getJ()], newPos.getJ(), newPos.getI(), newPos.getJ(), newPos.getI()));
                        newGrid[newPos.getI()][newPos.getJ()] = null;
                        return;
                    }
                    //On ajout l'animation d'un ennemi mais qui ne bouge pas
                    //Puis on rajoute l'animation pour la cellule qui détruit l'ennemi
//                    AnimaCell.addToAnimaList(animaList, new AnimaCell("move", newGrid[newPos.getI()][newPos.getJ()], newPos.getJ(), newPos.getI(), newPos.getJ(), newPos.getI()));
//                    AnimaCell.addToAnimaList(animaList, new AnimaCell("move", grid[pos.getI()][pos.getJ()], pos.getJ(), pos.getI(), newPos.getJ(), newPos.getI()));
                    newGrid[pos.getI()][pos.getJ()] = null; // retirer la cellule qu'on traite
                    newGrid[newPos.getI()][newPos.getJ()] = null; // retirer la cellule devant elle
                }

                else if(type == CellType.TRASH){
//                    AnimaCell.addToAnimaList(animaList, new AnimaCell("move", grid[pos.getI()][pos.getJ()], pos.getJ(), pos.getI(), newPos.getJ(), newPos.getI()));
                    newGrid[pos.getI()][pos.getJ()] = null;
                }

            } else{
                newGrid[pos.getI()][pos.getJ()] = null;
                newGrid[newPos.getI()][newPos.getJ()] = grid[pos.getI()][pos.getJ()];
                AnimaCell.addToAnimaList(animaList, new AnimaCell("move", newGrid[newPos.getI()][newPos.getJ()], pos.getJ(), pos.getI(), newPos.getJ(), newPos.getI()));
                newGrid[newPos.getI()][newPos.getJ()].setChanged(true);
            }
        }
    }

    /**
     * Cette classe contient des méthodes permettant de renvoyer le voisinnage proche ou lointain d'une cellule
     * 
     * @version 1.3
     * @author fuyutaaProjects fuyutaaProjects, Baptiste lx
     */
    public static class GridCheckers {

        /**
         * Méthode qui renvoie les cases adjacentes d'une case en . L'ordre des cases renvoyées est
         * défini en fontion des ordinaux des directions. Si la case est vide ou en dehors de la grille on mais null.
         *
         * @param pos Position la position de la case
         * @param grid Cell[][] la grille de cellule
         * @return Cell[] les cellules adjacentes
         */
        public static Cell[] checkNeighbor(Position pos, Cell[][] grid) {
            Cell[] returnedTab = new Cell[4];
            
            for (Direction d : Direction.values()) {
                Position newPos = pos.getFrontPosition(d);

                returnedTab[d.ordinal()] = Grid.isValid(newPos, grid) ? grid[newPos.getI()][newPos.getJ()] : null;
            }
            return returnedTab;
        }

        /**
         * Méthode qui à partir d'une position renvoie toutes les cellules se trouvant dans une direction donné
         *
         * @param pos Position la position ou commencer
         * @param direction Direction la direction ou checker
         * @param grid Cell[][] la grille de cellule
         * @return Cell[] le tableau des cellules se trouvant à partir de pos dans la direction donné
         */
        public static Cell[] checkDirection(Position pos, Direction direction, Cell[][] grid) {
            List<Cell> returnedListOfCells = new ArrayList<>();
            Cell actual = pos.getCell(grid);

            while (Grid.isValid(pos, grid) && actual != null && actual.getCellType() != CellType.WALL) {
                returnedListOfCells.add(actual);
                pos = pos.getFrontPosition(direction);
                actual = pos.getCell(grid);
            }
            return returnedListOfCells.toArray(new Cell[returnedListOfCells.size()]);
        }

    }
}
