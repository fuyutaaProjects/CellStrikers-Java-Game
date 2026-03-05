package src.main.java.com;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import src.main.java.com.audio.Audio;
import src.main.java.com.audio.Playlist;
import src.main.java.com.controllers.*;
import src.main.java.com.models.Grid;
import src.main.java.com.views.AnimaCell;
import src.main.java.com.views.GridView;
import src.main.java.com.views.effects.RetroGlassPane;
import src.main.java.com.views.menu.CardNameMap;
import src.main.java.com.views.sandboxEditor.GridScalePanel;
import src.main.java.com.views.sandboxEditor.LevelEditor;
import src.main.java.com.views.sandboxEditor.SandboxMod;

/**
 * Classe principale du jeu CellStrikers.
 * Elle initialise la fenêtre, le panneau principal et la musique de fond.
 * Elle gère également les interactions entre les différents composants du jeu.
 * Initialise le cardLayout pour gérer les différents écrans du jeu.
 */
public class Game {
    // Fenêtre principale du jeu et éléments graphiques principaux
    public static JFrame window = new JFrame("CellStrikers");
    public static JPanel mainPanel = (JPanel) window.getContentPane();
    public static CardLayout card = new CardLayout();
    public static RetroGlassPane glassPaneForEffects = new RetroGlassPane();
    public static Screen screen = new Screen(glassPaneForEffects);

    // Sons
    public static Playlist bg_playlist;
    public static float master_volume = 0.5f;
    public static float bg_volume = 0.5f;
    public static float sfx_volume = 0.5f;
    public static boolean retroEffect = true;

    public static void main(String[] args) {
        //Création de la fenêtre
        mainPanel.setLayout(card);
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setMinimumSize(new Dimension(800, 600)); // Taille minimum de la fenêtre
        screen.setScreenSize(1280, 720);

        // Permet d'ajuster la taille de la fenêtre
        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                screen.setScreenSize(window.getContentPane().getWidth(), window.getContentPane().getHeight());
            }
        });

        // Rajout dans le layout les accès aux différents menus.
        CardNameMap.addMenuToLayout();

        //Traitement audio pour la soundtrack de fond
        Audio[] bg_song = Playlist.loadPlayList(new String[]{
            "Brotato Terrors - Currents.wav",
            "Brotato - Hyperspace.wav",
            "Brotato - Phase Break.wav",
            "Brotato Terrors - Shipwreacked.wav",
            "Brotato Terrors - Sonar.wav",
            "Cherry Blossom.wav",
            "Once in a Lullaby.wav",
            "PKI - surf.wav",
            "Power_Punch_2050_Powerful.wav",
            "The Fire is Gone.wav"
        });
        bg_playlist = new Playlist(bg_song, 0.25f);
        bg_playlist.play(true);

        // Affichage du menu principal
        card.show(mainPanel, CardNameMap.MAIN_MENU.getLabel());
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        // Effets visuels
        Game.window.setGlassPane(glassPaneForEffects);
        glassPaneForEffects.setVisible(true);
    }

    /**
     * Permet de charger un niveau donné sur l'écran et ensuite changer de page pour afficher l'écran.
     *
     * @param gameGrid Grid
     * @param mod String
     * @param currentLevel int
     */
    public static void showGamePanel(Grid gameGrid, String mod, Integer currentLevel, String levelType) {
        showGamePanel(gameGrid, mod, currentLevel, levelType, false);
    }
    /**
     * Permet de charger un niveau donné du mode spécial sur l'écran 
     * et ensuite changer de page pour afficher l'écran.
     *
     * @param gameGrid Grid
     * @param texture_pack String
     * @param currentLevel int
     * @param specialMode boolean
     */
    public static void showGamePanel(Grid gameGrid, String texture_pack, Integer currentLevel, String levelType, boolean specialMode){
        screen.loadLevel(gameGrid, texture_pack);
        Interaction interaction = new Interaction(screen.getGridView(),screen,gameGrid,true, false);
        GridController gridController = new GridController(gameGrid, screen, currentLevel, levelType, interaction);
        screen.setController(gridController);
        screen.setFocusable(true);
        screen.requestFocusInWindow();
        card.show(mainPanel, CardNameMap.SCREEN.getLabel());
    }

    /**
     * Méthode pour passer au mode éditeur de niveau
     * @param gameGrid Grid la grille du jeu
     */
    public static void showLevelEditor(Grid gameGrid){
        showLevelEditor(new LevelEditor(gameGrid));
    }

    /**
     * Méthode pour passer au mode éditeur de niveau
     * @param editor LevelEditor l'afficheur de l'éditeur de niveau
     */
    public static void showLevelEditor(LevelEditor editor){
        screen.loadLevel(editor.getGrid(), "wise_tree");
        screen.setGridView(editor);
        screen.setGridView(new LevelEditor(editor.getGrid()));
        Interaction interaction = new Interaction(screen.getGridView(),screen,editor.getGrid(),true, true);
        LevelEditorController LevelEditorController = new LevelEditorController(screen,editor.getGrid(), interaction);
        screen.setFocusable(true);
        screen.requestFocusInWindow();
        GridView.texture_pack = "wise_tree";
        card.show(mainPanel, CardNameMap.SCREEN.getLabel());
    }
    /**
     * Méthode pour passer au mode sandbox
     * @param gameGrid Grid la grille du jeu
     */
    public static void showSandboxMod(Grid gameGrid){
        showSandboxMod(new SandboxMod(gameGrid));
    }
     /**
     * Méthode pour passer au mode sandbox
     * @param sandboxMod SandboxMod l'afficheur du mode sandbox
     */
    public static void showSandboxMod(SandboxMod sandboxMod){
        screen.setGameGrid(sandboxMod.getGrid());
        screen.setGridView(sandboxMod);

        SandboxController sandboxController = new SandboxController(screen, sandboxMod.getGrid(), new InteractionSandbox(sandboxMod, screen, sandboxMod.getGrid(), true));
        screen.setController(sandboxController);
        screen.setFocusable(true);
        screen.requestFocusInWindow();
        GridView.texture_pack = "wise_tree";
        card.show(mainPanel, CardNameMap.SCREEN.getLabel());
    }
    /**
     * Méthode permettant d'afficher le GridScalePanel
     * @see GridScalePanel
     * @param inEditor boolean true si on se trouve dans l'éditeur de niveau
     * false si on se trouve dans le mode sandbox
     */
    public static void showGridScalePanel(boolean inEditor){
        GridScalePanel gridScalePanel = new GridScalePanel(inEditor);
        mainPanel.add(gridScalePanel, CardNameMap.GRID_SCALE.getLabel());

        gridScalePanel.setFocusable(true);
        gridScalePanel.requestFocusInWindow();
        card.show(mainPanel, CardNameMap.GRID_SCALE.getLabel());
    }

    /**
     * Méthode pour mettre à jour le volume de la musique de fond 
     * les mises à jour des valeurs de volumes se font par le biais des valeurs master_volume et bg_volume
     */
    public static void setBgVolume(){
        bg_playlist.setVolume(master_volume*bg_volume);
    }

    /**
     * Méthode pour mettre à jour le volume des SFX
     * les mises à jour des valeurs de volumes se font par le biais des valeurs master_volume et sfx_volume
     */
    public static void setSFXVolume(){
        AnimaCell.SFX_sound.setVolume(sfx_volume*master_volume);
    }

    /** 
     * Méthode pour activer ou désactiver l'effet rétro
     */
    public static void updateRetroEffect() {
        glassPaneForEffects.repaint();
        window.repaint();
    }
}
