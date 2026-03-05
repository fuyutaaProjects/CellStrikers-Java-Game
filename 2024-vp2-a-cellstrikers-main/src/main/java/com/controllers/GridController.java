package src.main.java.com.controllers;

import src.main.java.com.Game;
import src.main.java.com.Screen;
import src.main.java.com.models.Grid;
import src.main.java.com.views.GridView;
import src.main.java.com.views.menu.CardNameMap;
import src.main.java.com.views.menu.LevelFinish;
import src.main.java.com.views.menu.LevelSelectionMenu;

/**
 * La classe GridController gère la logique du jeu, y compris les interactions avec la grille de jeu,
 * la mise à jour de l'affichage et la gestion des événements utilisateur.
 * Elle est responsable de la communication entre le modèle (Grid) et la vue (GridView).
 *
 * @author Ethan dy
 */
public class GridController {
    protected Grid gameGrid;  // Modèle
    protected GridView viewGrid;  // Vue
    protected Screen screen;  // Ecran pour afficher
    protected int ennemyRemain; //Le nombre d'ennemis qu'il reste
    private Integer currentLevel;
    private String levelType;
    protected Interaction interaction; // Gestion des interactions du joueur

    /**
     * Constructeur de la classe GridController.
     *
     * @param gameGrid Grille du jeu
     * @param screen Ecran pour afficher
     * @param currentLevel Niveau actuel
     * @param levelType Type de niveau
     * @param interaction Gestion des interactions du joueur
     */
    public GridController(Grid gameGrid, Screen screen, Integer currentLevel, String levelType, Interaction interaction) {
        this(gameGrid, screen, currentLevel, levelType, false, interaction);
    }

    /**
     * Constructeur de la classe GridController.
     *
     * @param gameGrid Grille du jeu
     * @param screen Ecran pour afficher
     * @param currentLevel Niveau actuel
     * @param levelType Type de niveau
     * @param specialMode Mode spécial ou non
     * @param interaction Gestion des interactions du joueur
     */
    public GridController(Grid gameGrid, Screen screen, Integer currentLevel, String levelType, boolean specialMode, Interaction interaction) {
        this.gameGrid = gameGrid;
        this.viewGrid = screen.getGridView();
        this.screen = screen;
        this.currentLevel = currentLevel;
        this.levelType = levelType;
        ennemyRemain = gameGrid.getRemainingEnnemies();

        // Rajout des interactions pour le joueur
        viewGrid.addMouseListener(interaction);
        viewGrid.addMouseMotionListener(interaction);

        // Actions des boutons
        viewGrid.getBackButton().addActionListener(e -> {
            screen.stopGame();
            CardNameMap.showCard(CardNameMap.MAIN_MENU.getLabel());
        });
        viewGrid.getPlayState().addActionListener(e -> {
            interaction.changeSelect(false);
            screen.launchGameThreadForOneStep();
            gameGrid.newGrid();
            refresh();
        });
        viewGrid.getLaunchGameButton().addActionListener(e -> {
            interaction.changeSelect(false);
            viewGrid.getPauseGameButton().setVisible(true);
            gameGrid.newGrid();
            screen.launchGameThread();
            
            viewGrid.getLaunchGameButton().setVisible(false);
            refresh();
        });
        viewGrid.getPauseGameButton().addActionListener(e -> {
            if (!screen.isRunning()){
                screen.launchGameThread();
                viewGrid.getPauseGameButton().setText("Pause");
            }else{
                screen.stopGameThread();
                viewGrid.getPauseGameButton().setText("Resume");
            }
            refresh();
            finish();
        });
        viewGrid.getRestartButton().addActionListener(e -> {
            viewGrid.getPauseGameButton().setVisible(false);
            screen.stopGame();

            LevelSelectionMenu.loadAndShowLevel(currentLevel, levelType);
            
            refresh();
            finish();
            interaction.changeSelect(true);
        });
    }

    /**
     * Permet d'appeler la fonction repaint de la vue et la réactualiser.
     */
    public void refresh(){
        viewGrid.repaint();
    }
    /**
     * Méthode pour finir la partie lorsqu'il n'y a plus d'ennemi
     * et afficher l'écran de fin de partie
     */
    public void finish(){
        ennemyRemain = gameGrid.getRemainingEnnemies();
        if(ennemyRemain == 0){
            screen.stopGameThread();
            Grid.resetAnimaList();
            LevelFinish levelFinish = new LevelFinish(currentLevel, levelType);
            Game.mainPanel.add(levelFinish.getPanel(), CardNameMap.LEVEL_FINISH.getLabel());
            CardNameMap.showCard(CardNameMap.LEVEL_FINISH.getLabel());
        }
    }

}
