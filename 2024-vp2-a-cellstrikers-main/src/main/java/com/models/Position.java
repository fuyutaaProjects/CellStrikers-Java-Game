package src.main.java.com.models;

/**
 * Classe permettant de stocker des coordonnées i et j pour mieux gérer les informations
 * On peut calcuelr plus facilement des coordonnées futures
 *
 * @author Ethan dy
 */
public class Position {
    private int i;  // abscisse
    private int j;  // ordonnée

    /**
     * Constructeur
     * @param i int
     * @param j int
     */
    public Position(int i, int j){
        this.i = i;
        this.j = j;
    }

//////////////////////////////////////////////GETTER / SETTER///////////////////////////////////////////////////////////

    public int getI() {
        return this.i;
    }
    public int getJ() {
        return this.j;
    }
    public void setI(int i) {
        this.i = i;
    }
    public void setJ(int j) {
        this.j = j;
    }

    /**
     * Renvoie la position que la direction pointe
     * @param direction Direction
     * @return Position
     */
    public Position getFrontPosition(Direction direction){
       return this.getFrontPosition(direction, 1);
    }

    /**
     * Renvoie la position que la direction pointe avec un facteur
     *
     * @param direction Direction pointée
     * @param k nombre de case en plus qu'on veut rajouter à la position
     * @return Position nouvelle position
     */
    public Position getFrontPosition(Direction direction, int k){
        int nextI = this.i+direction.getJ()*k;
        int nextJ = this.j+direction.getI()*k;
        
        return new Position(nextI,nextJ);
    }

    /**
     * Renvoie la position opposé à la direction
     * @param direction Direction
     * @return position
     */
    public Position getBackPosition(Direction direction){
        Direction opposite=direction.getOpposite();
        int backI = this.i+opposite.getJ();
        int backJ = this.j+opposite.getI();
        return new Position(backI,backJ);
    }

    /**
     * Compare deux points entres eux
     * @param pos2 Position
     * @return boolean
     */
    public boolean equals(Position pos2){
        return this.i==pos2.i && this.j==pos2.j;
    }

    /**
     * Regarde si le point est bien dans les dimensions données
     * @param width int
     * @param height int
     * @return boolean
     */
    public boolean isValid(int width, int height){
        return this.i >= 0 && this.i < width && this.j >= 0 && this.j < height;
    }

    /**
     * Renvoie la cellule aux coordonnées I,J.
     *
     * @param grid Cell[][]
     * @return Cell
     */
    public Cell getCell(Cell[][] grid){
        return this.isValid(grid.length, grid[0].length) ? grid[this.i][this.j] :  null;
    }

    @Override
    public String toString() {
        return "Position{i = "+ i +", j = "+ j +" }";
    }
}
