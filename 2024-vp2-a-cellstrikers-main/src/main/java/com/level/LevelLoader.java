package src.main.java.com.level;
import src.libs.json.JSONArray;
import src.libs.json.JSONObject;
import src.main.java.com.Game;
import src.main.java.com.models.Cell;
import src.main.java.com.models.Grid;
import src.main.java.com.views.specialMode.RuleSelectDialog;

import java.io.IOException;
import java.nio.file.Files;

import java.io.File;

/**
 * Classe permettant de charger un niveau en fonction de son id et de son type.
 */
public class LevelLoader {

    /**
     * Méthode permettant de vérifier si le level qu'on veut charger
     * est valide ou non
     * @param levelId Integer l'indice du niveau
     * @param levelType String type du niveau (correspond au nom du dossier ou il est rangé)
     * @return true si c'est le cas, false sinon
     * @author dy Elias
     */
    public static boolean levelExist(Integer levelId, String levelType){
        String levelPath = "src/main/java/com/jsons/levels/" + levelType + "/" + Integer.toString(levelId) + ".json";
        return new File(levelPath).exists();
    }
    /**
     * Permet de charger un niveau en fonction du numéro.
     *
     * @param levelId Integer (l'indice du niveau)
     * @param levelType String (correspond au nom du dossier)
     * 
     * @author fuyutaaProjects
     */
    public static void loadLevel(Integer levelId, String levelType) {
        
        // on load le level depuis son json correspondant 
        String levelPath = "src/main/java/com/jsons/levels/" + levelType + "/" + Integer.toString(levelId) + ".json";
        File file = new File(levelPath);
    
        if (!file.exists()) {
            System.err.println("erreur path invalide lors du loading de level : " + file.getAbsolutePath());
            return;
        }
    
        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            JSONObject levelData = new JSONObject(content);
    
            // récupération des grid par les keys "grid" et "interactGrid" dans le json
            JSONArray gridArray = levelData.getJSONArray("grid");
            JSONArray interactGridArray = levelData.getJSONArray("interactGrid");
            
            // récupération du background
            String texture_pack = levelData.optString("texture_pack", "default");

            // on récupère les grilles textuelles des jsons
            String[][] stringCellsGrid = LoaderJson.parseGrid(gridArray);
            boolean[][] interactGrid = LoaderJson.parseInteractGrid(interactGridArray);
            
            // conversion des grilles textuelles en grilles de vraies cellules java (exemple: on lit "trsh" à [i][j] dans 
            // le json et on ajoute alors une copie de la cellule trash à [i][j] dans notre grid java)
            Cell[][] cellsGrid = LoaderJson.gridmaker(stringCellsGrid);
    
            // Créer la grille et afficher le niveau
            Grid gameGrid = new Grid(cellsGrid, interactGrid);
            
            Game.showGamePanel(gameGrid, texture_pack, levelId, levelType);

            if(levelType.equals("specialLevels")){
                RuleSelectDialog select = new RuleSelectDialog(gameGrid);
                select.setVisible(true);
            }
        }
        catch (IOException e) {
            System.out.println("erreur lors de la lecture du fichier du level " + levelId);
            e.printStackTrace();
        }
    }
}