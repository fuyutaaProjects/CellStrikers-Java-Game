package src.main.java.com.controllers;


import javax.swing.JButton;
import javax.swing.Timer;
import src.libs.JStick;
import src.main.java.com.Screen;
import src.main.java.com.models.Grid;
import src.main.java.com.views.GridView;
import src.main.java.com.views.menu.saveLoad.LoadFileDialog;
import src.main.java.com.views.menu.saveLoad.SaveDialog;
import src.main.java.com.views.sandboxEditor.SandboxMod;


/**
 * Cette classe représente le controller du mode sandbox, 
 * elle hérite de LevelEditorController
 * 
 * @author dy Elias
 * @version 1.5
 */
public class SandboxController extends LevelEditorController{

    private final static String SAVE_FOLDER = "sandbox/";

    private SandboxMod sandboxView;

    JStick joystick;
    private Timer joystickTimer; // pour appliquer le mouvement en continu tant que le joystick est maintenu dans une direction
    private int moveX = 0;
    private int moveY = 0;

    /**
     * Constructeur de la classe qui permet d'initialiser les principaux attributs
     * de la classe
     * @param screen Screen l'écran de jeu
     * @param gameGrid Grid la grille du jeu
     */
    public SandboxController(Screen screen, Grid gameGrid, Interaction interaction) {
        super(screen, gameGrid, interaction);

        this.sandboxView = (SandboxMod) screen.getGridView();

        // Ajout du comportement du joystick :
        joystick = sandboxView.getJoyStick();

        this.interaction.setOffsetX((screen.getWidth() - (sandboxView.getDisplayRange()[3] - sandboxView.getDisplayRange()[1]) * GridView.cellSize)/2);
        this.interaction.setOffsetY((screen.getHeight() - (sandboxView.getDisplayRange()[2] - sandboxView.getDisplayRange()[0]) * GridView.cellSize)/2);

        // Modification du comportement du bouton de sauvegarde pour changer de répertoire :
        JButton save = sandboxView.getSaveButton();
        save.removeActionListener(save.getActionListeners()[0]);

        save.addActionListener(e -> {
            SaveDialog saveDialog = new SaveDialog(gameGrid, SAVE_PATH + SAVE_FOLDER);
            saveDialog.setVisible(true);
        });

        JButton load = sandboxView.getLoadButton();
        load.removeActionListener(load.getActionListeners()[0]);

        // Modification du comportement du bouton de chargement de sauvegarde pour changer de répertoire :
        load.addActionListener(e -> {
            LoadFileDialog dialog = new LoadFileDialog(SAVE_PATH + SAVE_FOLDER, gameGrid, sandboxView);
            dialog.setVisible(true);
        });

        joystickTimer = new Timer(100, e -> {
            if (moveX != 0 || moveY != 0) {
                moveView(moveX, moveY);
            }
        });
        joystickTimer.start();

        joystick.addJoystickListener(new JStick.JoystickListener()
        {
            @Override
            public void onJoystickMoved(JStick j) {
                moveX = (int) (10 * j.getStickX()) / 4;
                moveY = (int) (10 * j.getStickY()) / 4;
            }

            @Override
            public void onJoystickClicked(JStick j){
            }
        });
    }
    public static String getSaveFolder() {
        return SAVE_FOLDER;
    }

    private void moveView(int x, int y) {
        if(sandboxView.getDisplayRange()[0] - y >= 0 && sandboxView.getDisplayRange()[2] - y < gameGrid.getGrid().length){
            sandboxView.getDisplayRange()[0] -= y;
            sandboxView.getDisplayRange()[2] -= y;
        }
        if(sandboxView.getDisplayRange()[1] + x >= 0 && sandboxView.getDisplayRange()[3] + x < gameGrid.getGrid()[0].length){
            sandboxView.getDisplayRange()[1] += x;
            sandboxView.getDisplayRange()[3] += x;
        }
        sandboxView.setLocationLabel(sandboxView.getDisplayRange()[1], sandboxView.getDisplayRange()[0]);
        sandboxView.repaint();
    }

}