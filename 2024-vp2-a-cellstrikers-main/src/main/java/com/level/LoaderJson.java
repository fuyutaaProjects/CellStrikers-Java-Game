package src.main.java.com.level;

import src.libs.json.JSONArray;
import src.libs.json.JSONObject;
import src.main.java.com.models.Cell;
import src.main.java.com.models.CellType;
import src.main.java.com.models.Direction;
import src.main.java.com.models.Grid;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Classe permettant de charger et de sauvegarder des niveaux au format JSON.
 * Elle contient des méthodes pour lire des fichiers JSON, extraire des données et écrire des fichiers JSON.
 * Elle est utilisée pour gérer les niveaux du jeu.
 */
public class LoaderJson {

    /**
     * Récupère tous les fichiers JSON dans un dossier donné.
     *
     * @param folderName String - Chemin du dossier
     * @return File[] - Tableau de fichiers JSON triés par ordre numérique
     */
    public static File[] getJsonFilesInDirectory(String folderName) {
        File folder = new File(folderName);

        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Erreur : Le dossier " + folderName + " n'existe pas ou n'est pas un dossier.");
            return new File[0];
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        // les levels sont automatiquement triés par ordre alphabétique, donc pas besoin de les trier en fait
        return files;
    }

    /**
     * Lit un fichier JSON et retourne son contenu sous forme de JSONObject.
     *
     * @param file File - Fichier JSON
     * @return JSONObject - Contenu du fichier JSON
     * @throws IOException
     */
    public static JSONObject getJSONObjectFromFileObject(File file) throws IOException {
        String content = new String(Files.readAllBytes(file.toPath()));
        return new JSONObject(content);
    }

    /**
     * Permet de convertir un JSONArray en une grille de chaînes de caractères.
     *
     * @param gridArray JSONArray Grille des cellules
     * @return String[][] Grille des informations des cellules
     */
    public static String[][] parseGrid(JSONArray gridArray) {
        int rows = gridArray.length();
        int cols = gridArray.getJSONArray(0).length();
        String[][] grid = new String[rows][cols];

        // Rajout des informations dans la grille
        for (int y = 0; y < rows; y++) {
            JSONArray row = gridArray.getJSONArray(y);
            for (int x = 0; x < cols; x++) {
                grid[y][x] = row.isNull(x) ? null : row.getString(x);
            }
        }

        return grid;
    }

    /**
     * Permet de convertir un JSONArray en une grille de booléens.
     *
     * @param interactGridArray JSONArray Grille des boolean
     * @return boolean[][] Grille des boolean
     */
    public static boolean[][] parseInteractGrid(JSONArray interactGridArray) {
        int rows = interactGridArray.length();
        int cols = interactGridArray.getJSONArray(0).length();
        boolean[][] interactGrid = new boolean[rows][cols];

        // Rajout dans la grille la valeur correspondante dans la JSONArray
        for (int y = 0; y < rows; y++) {
            JSONArray row = interactGridArray.getJSONArray(y);
            for (int x = 0; x < cols; x++) {
                interactGrid[y][x] = row.getBoolean(x);
            }
        }

        return interactGrid;
    }


    /**
     * Permet de créer une grille de cellule en fonction d'une grille de chaine de caractère.
     *
     * @param cells String[][] Tableaux stockés dans le JSON
     * @return Cell[][] Grille utilisable pour créer la Grid
     */
    public static Cell[][] gridmaker(String[][] cells) {
        int rows = cells.length;
        int cols = cells[0].length;
        Cell[][] result = new Cell[rows][cols];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                String cellType = cells[y][x]; // On récupère la String qui correspond a une cellule
                if(cellType == null){
                    result[y][x] = null; // Cas ou la case est vide
                }
                else{
                    // On sépare la direction du nom de la cellule
                    String type = cells[y][x].substring(0, cells[y][x].length()-1);
                    String direction = "" + cells[y][x].charAt(cells[y][x].length()-1);
                    Cell cellMaker = null;

                    // On compare les noms des types de cellule et on créer la cellule quand on trouve
                    for (CellType types : CellType.values()) {
                        if (types.name().equals(type)) {
                            cellMaker = new Cell(types);
                        }
                    }

                    // On compare les directions pour rajouter la direction dans la cellule
                    if (cellMaker != null){
                        for (Direction directions : Direction.values()) {
                            if (directions.name().equals(direction)) {
                                cellMaker.setDirection(directions);
                            }
                        }
                    }
                    result[y][x] = cellMaker;
                }
            }
        }
        return result;
    }

    /**
     * Permet d'écrire les informations d'une grille en JSON qui est lisible par le loader.
     *
     * @param grid Grid Grille des cellules du niveau
     * @param filePath le chemin vers le fichier qui acceuillera la grille
     * @throws IOException
     */
    public static void writeInFile(Grid grid, String filePath) throws IOException {
        // On met les informations dans un format pour le JSON
        JSONArray interactGridObjet = interactGridToJSON(grid.getInteractableGrid());
        JSONArray gridObjet = gridToJSON(grid.getGrid());

        // On les met dans JSONObject
        JSONObject save = new JSONObject();
        save.put("grid", gridObjet);
        save.put("interactGrid", interactGridObjet);
        save.put("background", "wise_tree");

        // On écrit le fichier JSON
        FileWriter fileWriter = new FileWriter(filePath,false);
        fileWriter.write(save.toString());
        fileWriter.close();
    }

    /**
     * Permet d'écrire les informations concernant les interactions d'une grille pour ensuite les écrire dans un JSON.
     *
     * @param interactGrid boolean[][] Grille de boolean du niveau
     * @return JSONARRAY Format pour les tableaux JSON
     */
    private static JSONArray interactGridToJSON(boolean[][] interactGrid) {
        JSONArray interactGridArray = new JSONArray();
        JSONArray row = new JSONArray();
        for (int i= 0; i < interactGrid.length; i++) {
            for (int j = 0; j < interactGrid[i].length; j++) {
                // Ecrit les valeurs
                if (interactGrid[i][j]) {
                    row.put("true");
                }else{
                    row.put("false");
                }
            }
            // Rajoute la ligne du tableau et prépare la prochaine
            interactGridArray.put(row);
            row = new JSONArray();
        }
        return interactGridArray;
    }

    /**
     * Renvoie les informations d'une grille de cellule écrivable dans un fichier JSON.
     *
     * @param grid Cell[][] Grille de cellule du niveau
     * @return JSONARRAY Format pour les tableaux JSON
     */
    private static JSONArray gridToJSON (Cell[][] grid) {
        JSONArray gridArray = new JSONArray();
        JSONArray row = new JSONArray();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == null){
                    row.put("null"); // Cas de la case vide
                }else{
                    // On stocke le nom du type de cellule et la direction sous la forme "CELLTYPEDIRECTION"
                    String cellName = "";
                    cellName += grid[i][j].getCellType().name();
                    cellName += grid[i][j].getDirection().name();
                    row.put(cellName);
                }
            }
            // Rajoute la ligne du tableau et prépare la prochaine
            gridArray.put(row);
            row = new JSONArray();
        }
        return gridArray;
    }
}
