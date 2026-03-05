package src.main.java.com.views;

import src.main.java.com.models.Cell;
import src.main.java.com.models.CellType;
import src.main.java.com.views.utilities.CellTextureLoader;

public class Render {
    public static final int cellSize = 32;//Taille des textures des cellules en px

    //La classe Render s'occupe de créer un affiche du jeu 
    //Cela comprend l'affichage de la grid, les bouttons de jeu et le menu pause
    //
    //Voici un guide de chaque texture des cellules
    //
    //Type       |Case Vide      Mur        Cellule         Cellule limité           Rotors                        Générateurs                              Mover
    //Direction  |                                          Horiz.  Verti.       Gauche   Droite            Haut    Bas   Droite   Gauche         Haut     Bas    Droite   Gauche
    //Texture S  |   .            X            #              =        ║            L       R              Z        S        D        Q            A        v        >        <
    //           |+     +      +-----+      +-----+        +-----+  +-----+      +-----+   +-----+      +-----+  +-----+  +-----+  +-----+      +-----+  +-----+  +-----+  +-----+
    //Texture B  |             |     |      |     |        | --- |  | | | |      | <-\ |   | /-> |      | / \ |  | | | |  | --\ |  | /-- |      | / \ |  | \ / |  | \ \ |  | / / |
    //           |             |     |      |     |        | --- |  | | | |      | \-/ |   | \-/ |      | | | |  | \ / |  | --/ |  | \-- |      | / \ |  | \ / |  | / / |  | \ \ |
    //           |+     +      +-----+      +-----+        +-----+  +-----+      +-----+   +-----+      +-----+  +-----+  +-----+  +-----+      +-----+  +-----+  +-----+  +-----+
    //           |
    //Type       |      Cellule limité 1-Way                Trash        Enemy1     Enemy2      Enemy3      Enemy4
    //Direction  |  Haut     Bas    Droite   Gauche                     
    //Texture S  |   8        2        6        4             T            ¤           ¤           ¤           ¤
    //           |+-----+  +-----+  +-----+  +-----+       +-----+      +-----+     +-----+     +-----+     +-----+ Les 4 ennemies différentes servent juste de décoration
    //Texture B  || / \ |  | --- |  | | \ |  | / | |       | =n= |      | ¤.¤ |     | U U |     | * * |     | O O | Ce seront des skins alternatifs choisi au hasard
    //           || --- |  | \ / |  | | / |  | \ | |       | ║║║ |      | >-< |     |# w #|     |  ^  |     |  -  | pour le cas où on voudrai implémenter des textures 
    //           |+-----+  +-----+  +-----+  +-----+       +-----+      +-----+     +-----+     +-----+     +-----+ qui seront choisi parmi une liste
    //           |
    //Type       |.......                      .....
    //Direction  |                Haut     Bas    Droite   Gauche
    //Texture S  |   .             .        .        .        .
    //           |+-----+       +-----+  +-----+  +-----+  +-----+
    //Texture B  ||     |       |     |  |     |  |     |  |     |
    //           ||     |       |     |  |     |  |     |  |     |
    //           |+-----+       +-----+  +-----+  +-----+  +-----+
    //======================================================================== Texture List ======================================================================================
    public static Texture[] cell_texture = CellTextureLoader.loadTextures("src/main/java/com/jsons/cellTextureList.json");

    //======================================================================== Ascii Method ======================================================================================

    /**
     * Permet d'afficher en ASCII une grille de jeu.
     *
     * @param grid Cell[][]
     * @param compact boolean
     */
    public static void map_ascii_print(Cell[][] grid, boolean compact){
        //Renvoie une représentation du jeu sur le terminal sous forme de texture ascii
        //Dans la représentation, certain cellules ne sont représentés que dans une seule direction lorsque la direction n'a pas d'effet sur son fonctionnement (ex push ou enemy)
        if(compact == false){
            String output = "";
            for(int y = 0; y < grid.length; y++){
                output = "";
                for(int x = 0; x < grid[0].length; x++){
                    Texture t = getCorrespondingTexture(grid[y][x]);
                    if(t != null){
                        output += t.smallTexture();
                    }
                }
                System.out.println(output);
            }
        }
        if(compact == true){
            String[][] outputList = new String[4][grid[0].length];
            for(int y = 0; y < grid.length; y++){
                outputList = new String[4][grid[0].length];
                for(int x = 0; x < grid[0].length; x++){
                    Texture t = getCorrespondingTexture(grid[y][x]);
                    String[] bigT = t.bigTexture();
                    if(t != null){
                        for(int uwu = 0; uwu < 4; uwu++){
                            outputList[uwu][x] = bigT[uwu];
                        }
                    }
                }
                String output = "";
                for(int uwu = 0; uwu < 4; uwu++){
                    output = "";
                    for(int x = 0; x < grid[0].length; x++){
                        output += outputList[uwu][x];
                    }
                    if(y == 0 && uwu == 0){
                        System.out.println("+-" + stringDupli("-", output.length()) + "-+");
                    }
                    System.err.println("| " + output + " |");
                    if(y == grid.length-1 && uwu == 3){
                        System.out.println("+-" + stringDupli("-", output.length()) + "-+");
                    }
                }
            }
        }
    }
//========================================================================= Auxiliaries ======================================================================================

    /**
     * Renvoie la texture de la cellule donnée
     *
     * @param cell Cell
     * @return Texture
     */
    public static Texture getCorrespondingTexture(Cell cell){
        for(Texture t : cell_texture){
            if(
            (cell == null && t.cell_type() == null) || //On check si c'est une case vide
            ((cell != null && t.cell_type() == cell.getCellType()) && //On regarde si on a la texture avec le bon type de cellule
            ((t.dir() != null && (t.dir() == cell.getDirection() || //On regarde si l'orientation de la cellule correspond à celle de la texture
            cell.getCellType() == CellType.SLIDE && t.dir().isVertical() == cell.getDirection().isVertical()) || ////Avec un cas spéciale pour les slides qui n'existent que en vertical ou horizontal
            (t.dir() == null))))){
                return t;
            }
        }
        System.out.println("Error, couldn't find corresponding texture !");
        return null;
    }

    /**
     * Duplique une chaine de caractères n-fois.
     *
     * @param s String
     * @param n int
     * @return String
     */
    private static String stringDupli(String s, int n){
        String swaws = "";
        for(int uwu = 0; uwu < n; uwu++){
            swaws += s;
        }
        return swaws;
    }

    /**
     * Permet de cloner une grille de cellule
     *
     * @param list Cell[][]
     * @return Cell[][]
     */
    public static Cell[][] clone_list(Cell[][] list){
        //Sert à cloner une liste de cellules
        Cell[][] clone = new Cell[list.length][list[0].length];
        for(int ywy = 0; ywy < list.length; ywy++){
            for(int xwx = 0; xwx < list[0].length; xwx++){
                clone[ywy][xwx] = list[ywy][xwx];
            }
        }
        return clone;
    }
}