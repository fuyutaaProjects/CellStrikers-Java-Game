package src.main.java.com;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import src.main.java.com.controllers.GridController;
import src.main.java.com.controllers.Interaction;
import src.main.java.com.models.Grid;
import src.main.java.com.views.GridView;
import src.main.java.com.views.effects.RetroGlassPane;

/**
 * La classe Screen représente l'écran de jeu ou est afficher la grille des cellules
 * Elle contient diverses informations comme des informations sur l'affichage
 * 
 * @author dy Elias
 * @version 1.2
 */
public class Screen extends JPanel implements Runnable {

    // * PARAMÈTRES DE L'ÉCRAN
    private static final int TILE_SIZE = 16; //La taille d'une tuile
    private static int spriteMultiplicator = 3; //Le multiplicateur de taille de sprites, utilisé dans le scaling d'écran via les options.
    private static int spriteSize = TILE_SIZE*spriteMultiplicator; //Les dimensions d'un sprite

    private static int screenWidth = spriteSize*15; // Largeur de l'écran
    private static int screenHeight = spriteSize*15; // Hauteur de l'écran

    private static int FPS = 1; // FPS (Frames Par Seconde)

    private static Thread gameThread;// Thread pour le jeu
    private volatile boolean running = false;
    private volatile boolean paused = false;
    public static boolean animating;
    private static boolean stopping = false;
    private static boolean onestep = false;

    private Grid gameGrid; // Grille du jeu
    private GridView gridView;  //affichage du jeu

    private GridController controller; // Controller du jeu

    private RetroGlassPane glassPane; // GlassPane qui fait les effets visuels

    /**
     * Constructeur de PanneauJeu qui initialise certains de ses attributs et le paramètre
     */
    public Screen(RetroGlassPane glassPane) {
        // Définir la taille préférée du panneau
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        // Définir la couleur de fond du panneau
        this.setBackground(Color.WHITE);
        // Activer le double buffering pour améliorer les performances
        this.setDoubleBuffered(true);
        
        // Rendre le panneau focusable pour recevoir les événements clavier
        this.setFocusable(true);

        // Le GlassPane permettantde faire les effets visuels
        this.glassPane = (RetroGlassPane) glassPane;
    }


     /**
     * Méthode qui permet de récupérer la valeur de l'attribut ecranHauteur qui est la hauteur du PanneauJeu
     * @return un entier qui est la valeur de l'attribut ecranHauteur
     */
    public static int getScreenHeight() {
        return screenHeight;
    }
    /**
     * Méthode qui permet de récupérer la valeur de l'attribut ecranLargeur qui est la largeur du PanneauJeu
     * @return un entier qui est la valeur de l'attribut ecranLargeur
     */
    public static int getScreenWidth() {
        return screenWidth;
    }
    /**
     * Méthode permettant d'obtenir la valeur du champ spriteTaille qui est la dimension d'un Sprite
     * @return un entier qui est la valeur du champ spriteTaille
     */
    public static int getSpriteSize() {
        return spriteSize;
    }
    /**
     * Méthode permettant d'obtenir la valeur du champ tailleTuile qui est la dimension d'une tuile
     * @return un entier qui est la valeur du champ tailleTuile
     */
    public static int getTileSize() {
        return TILE_SIZE;
    }
    /**
     * Méthode permettant d'obtenir la valeur du champ spriteMultiplicator qui est le multiplicateur de taille pour les sprites
     * @return un entier qui est la valeur du champ multiplicateur
     */
    public static int getSpriteMultiplicator() {
        return spriteMultiplicator;
    }

    /**
     * Méthode permettant d'obtenir la valeur du champs FPS qui est le nombre d'images par secondes
     * @return un entier qui est le nombre de FPS
     */
    public static int getFPS() {
        return FPS;
    }
    public GridView getGridView() {
        return gridView;
    }

    /**
     * Getter si le thread est en cours.
     *
     * @return boolean
     */
    public boolean isRunning() {
        return running;
    }
    
    public void setGridView(GridView gridView) {
        this.gridView = gridView;
    }
    public void setController(GridController controller) {
        this.controller = controller;
    }

    public void setGameGrid(Grid gameGrid) {
        this.gameGrid = gameGrid;
    }
    /**
     * Méthode permettant de changer la taille de l'écran,
     * utilisé par le scaling de l'écran dans les Options
     * @param width int La largeur de l'écran 
     * @param height int La hauteur de l'écran
     */
    public void setScreenSize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
        
        // update des multiplicateurs (pour le moment uiMutplicator n'est pas utilisé)
        if (width == 1920 && height == 1080) {
            spriteMultiplicator = 4;
        } else if (width == 1280 && height == 720) {
            spriteMultiplicator = 3;
            // on ne change pas la taille des UIs
        }

        spriteSize = TILE_SIZE * spriteMultiplicator;
    
        // la vraie mise a jour
        GridView.updateCellSize();
        Game.mainPanel.setPreferredSize(new Dimension(screenWidth, screenHeight));
        Game.mainPanel.revalidate(); // reload
        Game.mainPanel.repaint(); // redraw

        if (gameGrid != null) {
            int gridHeight = gameGrid.getGrid().length * GridView.cellSize;
            int gridWidth = gameGrid.getGrid()[0].length * GridView.cellSize;
            Interaction.offsetY = (screenHeight - gridHeight) / 2;
            Interaction.offsetX = (screenWidth - gridWidth) / 2;
        }

    }
    
    public static void setSpriteMultiplicator(int newSpriteMultiplicator) {
        spriteMultiplicator = newSpriteMultiplicator;
        spriteSize = TILE_SIZE * spriteMultiplicator;
        GridView.updateCellSize();

    }
    /**
     * Méthode pour charger un niveau
     * @param gameGrid la grille du jeu
     * Voir MANUEL_DEVS.md pour une explication en détail de l'affichage allant du main menu au lancement d'un level.
     */
    public void loadLevel(Grid gameGrid, String texture_pack){
        running = false;
        paused = true;
        this.gameGrid = gameGrid;
        this.gridView = new GridView(gameGrid,texture_pack);
        repaint();
    }

    /**
     * Méthode pour démarrer le thread du jeu
     */
    public void launchGameThread() {
        if (gameThread == null || !gameThread.isAlive()) {
            gameThread = new Thread(this);
            stopping = false;
            running = true;
            paused = false;
            animating = false;
            gameThread.start();
        }
    }

    /**
     * Méthode pour lancer le thread du jeu pour une step
     */
    public void launchGameThreadForOneStep(){
        onestep = true;
        launchGameThread();
    }

    /**
     * Arrête le thread du jeu
     */
    public void stopGameThread(){
        running = false;
        paused = true;
        gameThread = null;
    }

    /**
     * Arrête le thread du jeu et supprime les animations
     */
    public void stopGame() {
        System.out.println("stopping game");
        gameGrid.resetAnimaList();
        stopping = true;
        animating = false;
        running = false;
        paused = false;
        System.out.println("boolean after stopping : " + running + "/" + paused + "/" + animating + "/" + stopping);
    }
    

    /**
     * Méthode pour exécuter le thread du jeu (la boucle du jeu)
     */
    @Override
    public void run() {
        // Intervalle de mise à jour (en nanosecondes)
        double intervalMaj = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long actualTime;
        while (running){
            System.out.println("boolean run : " + running + "/" + paused + "/" + animating);
            actualTime = System.nanoTime();
            delta += (actualTime - lastTime) / intervalMaj;
            lastTime = actualTime;
            if(delta >= 1){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if(!stopping){
                    update();
                    gridView.repaint();
//                        if(!gameGrid.getAnimaList().isEmpty()){
//                            animating = true;
//                        }
                    if(onestep){
                        onestep = false;
                        running = false;
                        paused = true;
                    }

                }

//                    else if(animating){
//                        gridView.repaint();
//                    }
                delta--;
                gridView.repaint();
            }
            if(animating){
                try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                gridView.repaint();
            }

        }
    }

    // Voir MANUEL_DEVS.md pour une explication en détail de l'affichage allant du main menu au lancement d'un level.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (gameGrid != null && gridView != null) {
            Game.mainPanel.add(gridView,"gridView");
            Game.card.show(Game.mainPanel,"gridView");
        }

        glassPane.repaint();

        g2.dispose();
    }

    /**
     * Permet de charger les futurs comportements des cellules de la grille.
     */
    private void update(){
        if(!stopping){
            gameGrid.updateAll();
            controller.finish();
        }
    }
}
