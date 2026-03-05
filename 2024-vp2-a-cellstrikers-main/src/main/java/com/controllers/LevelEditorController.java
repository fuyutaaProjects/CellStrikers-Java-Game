package src.main.java.com.controllers;

import java.awt.GridLayout;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JPanel;
import src.main.java.com.Screen;
import src.main.java.com.models.Cell;
import src.main.java.com.models.CellType;
import src.main.java.com.models.Direction;
import src.main.java.com.models.Grid;
import src.main.java.com.views.menu.CardNameMap;
import src.main.java.com.views.menu.saveLoad.LoadFileDialog;
import src.main.java.com.views.menu.saveLoad.SaveDialog;
import src.main.java.com.views.sandboxEditor.LevelEditor;

/**
 * La classe LevelEditorController gère la logique de l'éditeur de niveau, elle hérite de GridController.
 * Elle est responsable de la gestion des interactions avec la grille de jeu dans le mode éditeur.
 * Elle permet de charger et sauvegarder des niveaux, de sélectionner des cellules
 * et de gérer les actions de l'utilisateur dans l'éditeur.
 *
 */
public class LevelEditorController extends GridController{
    private LevelEditor levelView;
    private boolean canSelect;
    public static final String SAVE_PATH = "src/main/java/com/jsons/";
    protected final static String SAVE_FOLDER = "editor/";

    private Cell cell; // Cellule qu'on veut rajouter (information envoyé par LevelEditor

    private Cell[][] grid;
    private boolean mode = true; // Si on est dans le mode simulation ou non
    
    public LevelEditorController(Screen screen, Grid gameGrid, Interaction interaction) {
        super(gameGrid, screen, 0, "specialLevels",interaction);
        this.levelView = (LevelEditor) screen.getGridView();
        this.screen = screen;
        screen.setController(this);
        this.gameGrid = gameGrid;
        this.canSelect = true;
        this.cell = null;
        this.interaction = interaction;

        for (int i = 0; i < gameGrid.getInteractableGrid().length;i++){
            Arrays.fill(gameGrid.getInteractableGrid()[i], true);
        }

        // Rajout de l'écouteur du cell selector
        levelView.setCellSelectorListener(e -> {
            selectCell(levelView.getSelectedCell(),levelView.getDeleteCell(),levelView.getCanRotate(),levelView.getChangeInter(), levelView.getSelectMode());
        });

        // Bouton pour charger sa sauvegarde
        levelView.getLoadButton().addActionListener(e -> {
            LoadFileDialog dialog = new LoadFileDialog(SAVE_PATH + SAVE_FOLDER, gameGrid, levelView);
            dialog.setVisible(true);
        });

        // Bouton pour sauvegarder un niveau
        levelView.getSaveButton().addActionListener(e -> {
            SaveDialog saveDialog = new SaveDialog(gameGrid, SAVE_PATH + SAVE_FOLDER);
            saveDialog.setVisible(true);
        });

        // Bouton pour quitter
        levelView.getBackButton().addActionListener(e -> {
            screen.stopGame();
            levelView.setSelectedCell(null);
            levelView.setCanRotate(false);
            levelView.setDeleteCell(false);
            levelView.setChangeInter(false);
            CardNameMap.showCard("MAIN_MENU");
            Grid.resetAnimaList();
        });

        // Modification du comportement du bouton restart : 
        JButton restart = levelView.getRestartButton();
        restart.removeActionListener(restart.getActionListeners()[0]);

        restart.addActionListener((e) -> {
            viewGrid.getPauseGameButton().setVisible(false);
            viewGrid.getLaunchGameButton().setVisible(true);
            screen.stopGame();
            gameGrid.setGrid(grid);
            Grid.resetAnimaList();

            refresh();
            interaction.changeSelect(true);
        });

        // Ajout du comportement de switch mode button
        JButton switchModeButton = levelView.getSwitchModeButton();

        switchModeButton.addActionListener((e) -> {
            boolean cond = switchModeButton.getText().equals("simulate");
            if(!cond){
                switchModeButton.setText("simulate");
                gameGrid.setGrid(grid);
                
            }else{
                switchModeButton.setText("create");
                // Copie de la grille actuelle
                grid = new Cell[gameGrid.getGrid().length][];

                for (int i = 0; i < gameGrid.getGrid().length; i++) {
                    grid[i] = new Cell[gameGrid.getGrid()[i].length];
                    for (int j = 0; j < gameGrid.getGrid()[i].length; j++) {
                        grid[i][j] = gameGrid.getGrid()[i][j];
                    }
                }
            }
            Grid.resetAnimaList();
            this.interaction.inEditor = !cond;
            restart.getActionListeners()[0].actionPerformed(e);

            // Affichage ou non des boutons concernant les états : 
            desativateButton(mode);
            levelView.repaint();
        });
    }

//////////////////////////////////////////////GETTER / SETTER///////////////////////////////////////////////////////////
    public Cell getCell() {
        return cell;
    }
    public void setCell(Cell cell) {
        this.cell = cell;
    }
    public static String getSaveFolder() {
        return SAVE_FOLDER;
    }
    
    @Override
    public void finish() {
    }

    /**
     * Permet de changer les informations stockées dans la vue pour ensuite les utiliser dans le controller.
     *
     * @param cellType CellType
     * @param delete boolean
     * @param rotate boolean
     */
    public void selectCell(CellType cellType,boolean delete,boolean rotate, boolean changeInter, boolean selectMode) {
        if (cellType != null) {
            // Placement d'une cellule
            setCell(new Cell(cellType, Direction.N));
        }else{
            setCell(null);
        }
        // On invoque la fonction dans interaction qui va s'occuper de gérer si les conditions sont valides
        interaction.changeCell(cell,delete,rotate,changeInter, selectMode, true);
    }

    /**
     * Méthode permettant de désactiver les boutons selon le mode sélectionner
     * @param cond boolean informe de si on se trouve dans le mode simulation ou non
     */
    private void desativateButton(boolean cond){
        JPanel buttonPanel = new JPanel(new GridLayout(1,0,20,0));
        buttonPanel.setOpaque(false);
        if(mode) {
            mode = false;
            JButton restart = levelView.getRestartButton();
            restart.setEnabled(true);
            restart.setVisible(true);
            JButton play = levelView.getPlayState();
            play.setEnabled(true);
            play.setVisible(true);
            JButton pause = levelView.getPauseGameButton();
            pause.setEnabled(true);
            pause.setVisible(true);
            JButton launch = levelView.getLaunchGameButton();
            launch.setEnabled(true);
            launch.setVisible(true);
            levelView.getTopPanel().remove(0);
            buttonPanel.add(levelView.getBackButton());
            buttonPanel.add(restart);
            buttonPanel.add(play);
            buttonPanel.add(pause);
            buttonPanel.add(launch);
            levelView.getRightPanel().setVisible(false);
            levelView.getCellSelector().setVisible(false);
            levelView.getCellSelector().setEnabled(false);
        }else{
            mode = true;
            JButton save = levelView.getSaveButton();
            JButton load = levelView.getLoadButton();
            levelView.getTopPanel().remove(0);
            buttonPanel.add(levelView.getBackButton());
            buttonPanel.add(save);
            buttonPanel.add(load);
            levelView.getRightPanel().setVisible(true);
            levelView.getCellSelector().setVisible(true);
            levelView.getCellSelector().setEnabled(true);
        }
        levelView.getTopPanel().add(buttonPanel);
    }
}
