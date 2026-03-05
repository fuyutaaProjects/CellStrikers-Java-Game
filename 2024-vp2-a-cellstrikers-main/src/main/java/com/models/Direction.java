package src.main.java.com.models;

/**
 * Enumération pour définir les directions possibles dans le jeu.
 * Chaque direction est associée à des coordonnées (i, j) pour faciliter les déplacements.
 *
 * @author Ethan dy
 */
public enum Direction {
    N,E,S,W;
    //  Abscisse des directions
    private static int[] di = {0,1,0,-1};
    //  Ordonnée des directions
    private static int[] dj = {-1,0,1,0};
    //  Opposé des directions
    private static Direction[] opposite = {S,W,N,E};

//////////////////////////////////////////////////GETTER////////////////////////////////////////////////////////////////

    public int getI(){
        return di[this.ordinal()];
    }
    public int getJ() {
        return dj[this.ordinal()];
    }
    public Direction getOpposite(){
        return opposite[this.ordinal()];
    }

    /**
     * Fait tourner de direction en fonction de k
     * Avec k correspondant au nombre de quart de cercles dont on veux tourner
     * @param k int
     * @return Direction
     */
    public Direction turn(int k){
        int n = values().length;
        return values()[(n + (this.ordinal() + k) % n) % n];
    }

    /**
     * Regarde si la direction est vertical
     * @return boolean
     */
    public boolean isVertical(){
        return getJ()!=0;
    }

    /**
     * Renvoie l'angle absolue de la direction dir en tournant vers la droite
     * @return double
     */
    public double getAngleDroite(){
        return this.ordinal() * 90;
    }

    /**
     * Renvoie l'angle absolue de la direction dir en tournant vers la gauche
     * @return double
     */
    public double getAngleGauche(){
        return (4 - this.ordinal()) * -90;
    }

    /**
     * Renvoie l'angle entre this et la direction dir en tournant vers la droite
     * @param dir Direction
     * @return double
     */
    public double getAngleDroiteTo(Direction dir){
        if(getAngleDroite() == 0 && dir.ordinal() != 0){
            return 360 - dir.ordinal() * 90;
        }
        return getAngleDroite() - dir.ordinal() * 90;
    }

    /**
     * Renvoie l'angle entre this et la direction dir en tournant vers la gauche
     * @param dir Direction
     * @return double
     */
    public double getAngleGaucheTo(Direction dir){
        if(getAngleGauche() != 0 && dir.ordinal() == 0){
            return -90;
        }
        return getAngleGauche() + (4 - dir.ordinal()) * 90;
    }

    /**
     * Transforme un angle en Direction si l'angle est un multiple de 90
     * @param angle double
     * @return Direction
     */
    public static Direction angleToDirection(double angle){
        if(angle%90 != 0.0){return null;}
        int dir_int = (int) angle%90;
        while(dir_int < 0 || dir_int > 3){
            if(dir_int < 0){dir_int += 4;}
            if(dir_int > 3){dir_int -= 4;}
        }
        return Direction.values()[dir_int];
    }
}