package src.main.java.com.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import src.main.java.com.Screen;
import src.main.java.com.controllers.Interaction;
import src.main.java.com.models.*;
import src.main.java.com.views.utilities.StyleButton;

/**
 * Classe représentant la grille du jeu.
 * Elle hérite de JPanel et permet d'afficher la grille de jeu.
 * Elle contient des méthodes pour dessiner la grille, gérer les animations et les interactions.
 * Elle contient également des boutons pour contrôler le jeu.
 */
public class GridView extends JPanel{
    public static int cellSize;//Taille des textures des cellules en px
    public static double margin = 0.7; // Marge entre la grille et l'écran
    private Grid grid;

    // Boutons du jeu
    private JButton backButton;
    private JButton launchGameButton;
    private JButton pauseGameButton;
    private JButton restartButton;
    private JButton playState;
    private JPanel topPanel; // Panel pour stocker les boutons

    // Constantes
    public static final int BUTTON_WIDTH = 120;
    public static final int BUTTON_HEIGHT = 40;
    private final int MIN_CELL_SIZE = 32; // Taille minimum de la cellule
    private final int MAX_CELL_SIZE = 128; // Taille maximum de la cellule

    private JLabel labelDrag; // Label pour le drag and drop

    /*
     * indice 0: case de la grille d'indice i ou commencera l'affichage <br>
     * indice 1: case d'indice j ou commencera l'affichage <br>
     * indice 2: case d'indice i ou finira l'affichage <br>
     * indice 3: case d'indice j ou finira l'affichage <br>
     */
    private static int[] displayRange;

    //BackGround
    private static BufferedImage[][] bgSave; //Valeur qui sauvegarde le background du jeu

    public static boolean animating = Screen.animating;

    //Apparence du jeu
    public static String texture_pack;
    //Texture pack possible
    //(si on met une valeur null, vide ou autre, les textures seront celles de base)
    //Si il y a plusieurs entrées possibles pour un même style, ils seront séparées par ;
    //neon;disco : background type discothèque
    //wise_tree : wise tree

    /**
     * Constructeur de la classe GridView
     * @param grid Grid
     * @param texture_pack_ String texture pack
     */
    public GridView(Grid grid, String texture_pack_){
        setPreferredSize(new Dimension(Screen.getScreenWidth(), Screen.getScreenHeight()));
        setLayout(null);
        setBackground(Color.BLACK);
        this.grid = grid;
        this.setDisplayRange(grid.getGrid());
        texture_pack = texture_pack_;
        this.bgSave = new BufferedImage[grid.getGrid().length][grid.getGrid()[0].length];

        labelDrag = new JLabel();
        add(labelDrag);

        topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Création des boutons
        backButton = new JButton("Back");
        launchGameButton = new JButton("Launch");
        pauseGameButton = new JButton("Pause");
        restartButton = new JButton("Restart");
        playState = new JButton("State");
        pauseGameButton.setVisible(false);

        // Rajout du style
        StyleButton.styleButton(backButton,BUTTON_WIDTH,BUTTON_HEIGHT);
        StyleButton.styleButton(launchGameButton,BUTTON_WIDTH,BUTTON_HEIGHT);
        StyleButton.styleButton(pauseGameButton,BUTTON_WIDTH,BUTTON_HEIGHT);
        StyleButton.styleButton(restartButton,BUTTON_WIDTH,BUTTON_HEIGHT);
        StyleButton.styleButton(playState,BUTTON_WIDTH,BUTTON_HEIGHT);

        // Rajout des boutons
        JPanel buttonPanel = new JPanel(new GridLayout(1,5,20,0));
        buttonPanel.setOpaque(false);

        buttonPanel.add(backButton);
        buttonPanel.add(launchGameButton);
        buttonPanel.add(pauseGameButton);
        buttonPanel.add(restartButton);
        buttonPanel.add(playState);

        topPanel.add(buttonPanel, BorderLayout.NORTH);
        add(topPanel);

        // Permet d'adapter la taille en fonction de l'écran et de garder les boutons au milieu
        updateLayout();
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                updateLayout();
            }
        });
    }

///////////////////////////////////////////////GETTER///////////////////////////////////////////////////////////////////

    public JButton getBackButton(){
        return backButton;
    }
    public JButton getLaunchGameButton(){
        return launchGameButton;
    }
    public JButton getPauseGameButton(){
        return pauseGameButton;
    }
    public JButton getRestartButton(){
        return restartButton;
    }
    public JButton getPlayState(){
        return playState;
    }
    public int[] getDisplayRange() {
        return displayRange;
    }

    public static double getMargin() {
        return margin;
    }

    public Grid getGrid() {
        return grid;
    }
    public JPanel getTopPanel() {
        return topPanel;
    }

///////////////////////////////////////////////SETTER///////////////////////////////////////////////////////////////////

    public void setPlayState(JButton playState){
        this.playState = playState;
    }

    public void setMode(String texture_pack_){
        this.texture_pack = texture_pack_;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public static void setMargin(double margin) {
        GridView.margin = margin;
    }

    /**
     * Méthode permettant de changer le champ display range qui est
     * le champ d'affichage de la grille
     * 
     *  indice 0: case de la grille d'indice i ou commencera l'affichage
     *  indice 1: case d'indice j ou commencera l'affichage
     *  indice 2: case d'indice i ou finira l'affichage
     *  indice 3: case d'indice j ou finira l'affichage
     * @param displayRange int[] Le nouveau displayRange
     */
    public void setDisplayRange(int[] displayRange) {
        if(displayRange.length != 4){
            throw new IllegalArgumentException("displayRange doit être de taille 4");
        }
        this.displayRange = displayRange;
    }

    /**
     * Méthode permettant de changer le champ display range qui est
     * le champ d'affichage de la grille
     * 
     *  indice 0: case de la grille d'indice i ou commencera l'affichage
     *  indice 1: case d'indice j ou commencera l'affichage
     *  indice 2: case d'indice i ou finira l'affichage
     *  indice 3: case d'indice j ou finira l'affichage
     * @param grid Cell[][] grid la grille à partir de laquelle sera créer le displayRange. Le display range
     * vaudra donc cela : {0, 0, grid.length, grid[0].length}
     */
    public void setDisplayRange(Cell[][] grid){
        this.setDisplayRange(new int[]{0, 0, grid.length, grid[0].length});
    }

    public void setbgSave(BufferedImage[][] bgSave){
        GridView.bgSave = bgSave;
    }

    /**
     * Fonction qui permet de mettre à jour la taille des boutons et de les centrer
     */
    public void updateLayout() {
        int width = getWidth();
        this.topPanel.setBounds(0, 10, getWidth(), 70);
        repaint();
        revalidate();
    }

    /**
     * Fonction qui permet de dessiner et de mettre à jour la grille du jeu au centre de l'écran.
     *
     * @param g Graphics
     * @autor Ethan dy
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        applyRenderingHints(g2d);

        int width = getWidth();
        int height = getHeight();

        int rows = getDisplayRange()[2] - getDisplayRange()[0]; // grid.getGrid().length
        int cols = getDisplayRange()[3] - getDisplayRange()[1]; // grid.getGrid()[0].length;

        //sSystem.out.println(rows + " " + cols);

        int gridWidthAvailable = (int)(width * margin);
        int gridHeightAvailable = (int)(height * margin);

        int cellSizeWithoutRatio = Math.min(gridWidthAvailable / cols, gridHeightAvailable / rows); // Taille de la cellule sans ratio
        cellSize = Math.max(MIN_CELL_SIZE, Math.min(MAX_CELL_SIZE, cellSizeWithoutRatio)); // Taille de la cellule avec ratio

        int gridWidth = cellSize * cols;
        int gridHeight = cellSize * rows;

        // Calcul le décalage et actualise le décalage de l'interaction
        int offsetX = ((width - gridWidth) / 2);
        int offsetY = ((height - gridHeight) / 2);
        Interaction.offsetX = offsetX;
        Interaction.offsetY = offsetY;
            drawGrid(g2d,grid,offsetX,offsetY);

//        animating = Screen.animating;
//        if(!animating){
//            drawGrid(g2d,grid,offsetX,offsetY);
//        }
//        else{
//            drawAnimaList(g2d, grid, offsetX, offsetY, texture_pack);
//        }
    }

    /**
     * Permet de dessiner la grille du jeu à partir des coordonnées données
     * de façon statique, les animations sont gérés par drawAnimaList
     *
     * @param g2d Graphics2D ou on va dessiner la grille
     * @param gameGrid Grid grille à dessiner
     * @param offsetX int décalage en X
     * @param offsetY int décalage en Y
     */
    private void drawGrid(Graphics2D g2d, Grid gameGrid, int offsetX, int offsetY){
        drawBackgroundGrid(g2d, gameGrid.getInteractableGrid(), gameGrid.getGrid()[0].length, gameGrid.getGrid().length, offsetX, offsetY);

        for (int x = getDisplayRange()[0]; x < getDisplayRange()[2]; x++) {
            for (int y = getDisplayRange()[1]; y < getDisplayRange()[3]; y++) {

                Cell cell = grid.getGrid()[x][y];
                BufferedImage IC_0n = null;
                if (cell == null){
                    IC_0n = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
                }

                if (cell != null) {
                    IC_0n = getCorrespondingTexture(grid.getGrid()[x][y]).getTurnedImage(grid.getGrid()[x][y].getDirection());
                }

                if (IC_0n == null) {
                    // Met une erreur
                    drawErrorSymbol(g2d, offsetX + (y * cellSize), offsetY + (x * cellSize));
                    continue;
                }

                int imageWidth = IC_0n.getWidth();
                int imageHeight = IC_0n.getHeight();

                double scaleX = (double) cellSize / imageWidth;
                double scaleY = (double) cellSize / imageHeight;

                int posX = offsetX + ((y-displayRange[1]) * cellSize);
                int posY = offsetY + ((x-displayRange[0]) * cellSize);

                AffineTransform transform = new AffineTransform();
                transform.translate(posX, posY);
                transform.scale(scaleX, scaleY);

                g2d.drawImage(IC_0n, transform, this);
            }
        }
        g2d.dispose();
    }

    /**
    * Rajoute un cercle rouge avec une croix violette pour symboliser les cellules
    * dont les textures n'ont pas été correctement chargée
    * @param g Graphics pour dessiner
    * @param x int coordonnée x
    * @param y int coordonnée y
    */
    protected static void drawErrorSymbol(Graphics g, int x, int y){
        g.setColor(Color.RED);
        g.fillOval(x, y, cellSize, cellSize); // Draw red circle
        g.setColor(Color.MAGENTA);
        g.drawLine(x + 10, y + 10, x + cellSize - 10, y + cellSize - 10);
        g.drawLine(x + cellSize - 10, y + 10, x + 10, y + cellSize - 10);
    }

    /**
     * Fonction qui permet de commencer l'animation de drag and drop
     * @param cell Cellule qu'il faut déplacer
     * @param position Position de la cellule
     */
    public void startDrag(Cell cell, Position position){
        if (cell != null){
            Texture texture = getCorrespondingTexture(cell);
            if (texture != null){
                ImageIcon image = new ImageIcon(texture.getTurnedImage(cell.getDirection()));
                image = resize(image,cellSize,cellSize);
                labelDrag.setIcon(image);
                labelDrag.setSize(cellSize, cellSize);
                labelDrag.setLocation(position.getI(), position.getJ());
                labelDrag.setVisible(true);
            }
        }
        repaint();
    }

    /**
     * Fonction qui permet de mettre à jour la position du drag and drop
     * @param position Position de la cellule
     */
    public void updateDrag(Position position) {
        labelDrag.setLocation(position.getI(), position.getJ());
        repaint();
    }

    /**
     * Fonction qui permet de stopper le drag and drop
     */
    public void stopDrag() {
        labelDrag.setVisible(false);
        repaint();
    }

    /**
     * Fonction qui permet de redimensionner une imageIcon
     * @param imageIcon image à redimensionner
     * @param widht taille de l'image voulue
     * @param height taille de l'image voulue
     * @return ImageIcon redimensionnée
     */
    public ImageIcon resize(ImageIcon imageIcon, int widht, int height){
        return ImageLoader.resize(imageIcon, widht, height);
    }


//========================================================================= BackGround =======================================================================================

    /**
     * Permet de dessiner le fond du jeu derrière la Grid pour ne pas laisser un fond vide.
     *
     * @param g Graphics pour dessiner
     * @param bgGrid boolean[][]
     * @param gridWidth int
     * @param gridHeight int
     * @param offsetX int
     * @param offsetY int
     */
    private static void drawBackgroundGrid(Graphics g, boolean[][] bgGrid, int gridWidth, int gridHeight, int offsetX,int offsetY){
        if(texture_pack.equals("wise_tree")){
            File file = new File("src/resources/background/wise_tree.png");
            ImageIcon ic = new ImageIcon(file.getAbsolutePath());
            g.drawImage(ic.getImage(), 0, 0, Screen.getScreenWidth(),Screen.getScreenHeight(),null);
        }
        if(texture_pack.equals("disco") || texture_pack.equals("neon")){
            File file = new File("src/resources/background/neonmixtape.png");
            ImageIcon ic = new ImageIcon(file.getAbsolutePath());
            g.drawImage(ic.getImage(), 0, 0, Screen.getScreenWidth(),Screen.getScreenHeight(),null);
        }
        if(texture_pack.equals("marble") || texture_pack.equals("library")){
            File file = new File("src/resources/background/library.png");
            ImageIcon ic = new ImageIcon(file.getAbsolutePath());
            g.drawImage(ic.getImage(), 0, 0, Screen.getScreenWidth(),Screen.getScreenHeight(),null);
        }
        if(texture_pack.equals("mushroom") || texture_pack.equals("risk_of_rain_2")){
            File file = new File("src/resources/background/mushroom.png");
            ImageIcon ic = new ImageIcon(file.getAbsolutePath());
            g.drawImage(ic.getImage(), 0, 0, Screen.getScreenWidth(),Screen.getScreenHeight(),null);
        }
        if(texture_pack.equals("witch_coven")){
            File file = new File("src/resources/background/ender_lilies_witch_coven.png");
            ImageIcon ic = new ImageIcon(file.getAbsolutePath());
            g.drawImage(ic.getImage(), 0, 0, Screen.getScreenWidth(),Screen.getScreenHeight(),null);
        }
        if(texture_pack.equals("verboten_domain")){
            File file = new File("src/resources/background/ender_lilies_verboten_domain.png");
            ImageIcon ic = new ImageIcon(file.getAbsolutePath());
            g.drawImage(ic.getImage(), 0, 0, Screen.getScreenWidth(),Screen.getScreenHeight(),null);
        }
        if(texture_pack.equals("white_blight")){
            File file = new File("src/resources/background/ender_lilies_white_blight.png");
            ImageIcon ic = new ImageIcon(file.getAbsolutePath());
            g.drawImage(ic.getImage(), 0, 0, Screen.getScreenWidth(),Screen.getScreenHeight(),null);
        }
        if(texture_pack.equals("subterrean_lab")){
            File file = new File("src/resources/background/ender_magnolia_subterrean_lab.png");
            System.out.println("lab");
            ImageIcon ic = new ImageIcon(file.getAbsolutePath());
            g.drawImage(ic.getImage(), 0, 0, Screen.getScreenWidth(),Screen.getScreenHeight(),null);
        }
        if(texture_pack.equals("tethered_steeple")){
            File file = new File("src/resources/background/ender_magnolia_tethered_steeple.png");
            ImageIcon ic = new ImageIcon(file.getAbsolutePath());
            g.drawImage(ic.getImage(), 0, 0, Screen.getScreenWidth(),Screen.getScreenHeight(),null);
        }
        if(bgSave == null){
            bgSave = new BufferedImage[gridHeight][gridWidth];
        }
        for(int y = displayRange[0]; y < displayRange[2]; y++){
            for(int x = displayRange[1]; x < displayRange[3]; x++){

                if(y < bgGrid.length && x < bgGrid[0].length){
                    boolean isPlacable = bgGrid[y][x]; // true = bg_px, false = bg_nx
                    BufferedImage IC_0n;

                    if(bgSave != null && bgSave[y][x] != null){
                        IC_0n = bgSave[y][x];
                    }
                    // Récupère l'image de la case de fond
                    else try{
                        IC_0n = getBackgroundTexture(isPlacable,x,y);
                    } catch (IOException e) {
                        System.out.println("Error when loading background tile texture");
                        IC_0n = null;
                    }

                    if(IC_0n != null && IC_0n != null){
                        int cx = offsetX + (x - displayRange[1]) * cellSize;
                        int cy = offsetY + (y - displayRange[0])* cellSize;
                        g.drawImage(IC_0n, cx, cy, cellSize, cellSize, null);
                    }
                }
            }
        }
    }

    /**
     * Permet de récuperer la texture de la case de fond.
     *
     * @param isPlaceable boolean
     * @param x int
     * @param y int
     * @return ImageIcon
     */
    protected static BufferedImage getBackgroundTexture(boolean isPlaceable, int x, int y) throws IOException {
        String textureName;
        if(texture_pack.equals("disco") || texture_pack.equals("neon")){
            textureName = (isPlaceable ? "disco_p" : "disco_n") + 1;
        }
        else if(texture_pack.equals("marble") || texture_pack.equals("library")){
            textureName = (isPlaceable ? "marble_p" : "marble_n") + 1;
        }
        else if(texture_pack.equals("mushroom") || texture_pack.equals("risk_of_rain_2")){
            textureName = (isPlaceable ? "mushroom_p" : "mushroom_n") + 1;
        }
        else if(texture_pack.equals("witch_coven")){
            textureName = (isPlaceable ? "witch_coven_p" : "witch_coven_n") + 1;
        }
        else if(texture_pack.equals("verboten_domain")){
            textureName = (isPlaceable ? "verboten_p" : "verboten_n") + 1;
        }
        else if(texture_pack.equals("white_blight")){
            textureName = (isPlaceable ? "white_p" : "white_n") + 1;
        }
        else if(texture_pack.equals("subterrean_lab")){
            textureName = (isPlaceable ? "lab_p" : "lab_n") + 1;
        }
        else if(texture_pack.equals("tethered_steeple")){
            textureName = (isPlaceable ? "steeple_p" : "steeple_n") + 1;
        }
        else{
            textureName = isPlaceable ? "bg_p" : "bg_n";
            int maxPlaceableTextureNum = 2;   //Nombre de textures pour les cases où on peux intéragir avec les cellules
            int maxNotPlaceableTextureNum = 2;//Nombre de textures pour les cases où on ne peux pas intéragir
            // Variation des textures
            Random r = new Random();
            int random = r.nextInt(10);
            if (random <= 7) {
                textureName += "1";
            } else {
                textureName += 2 + r.nextInt((isPlaceable ? maxPlaceableTextureNum : maxNotPlaceableTextureNum) - 1);
            }
        }

        try {
            // Génère l'URL du chemin vers la texture
            String path = "/src/resources/texture/" + textureName + ".png";
            URL texturePath = Render.class.getResource(path);

            if (texturePath == null) {
                System.out.println("Image non trouvée : " + path);
                return null;
            }

            // Chargement de l'image avec imageIO
            BufferedImage image = ImageIO.read(texturePath);

            // Sauvegarde l'image dans bgSave pour éviter de la recharger
            if (bgSave != null) {
                bgSave[y][x] = image;
            }

            return image;

        } catch (IOException e) {
            // Renvoie de l'erreur
            System.out.println("Erreur lors du chargement de l'image '" + textureName + "': " + e.getMessage());
            return null;
        }
    }

    /**
     * Fonction qui renvoie la texture de la cellule qu'on lui donne.
     *
     * @param cell Cell dont on veut la texture
     * @return Texture correspondant à la cellule
     */
    
    public static Texture getCorrespondingTexture(Cell cell){
        for(Texture t : Render.cell_texture){
            if(
                (cell == null && t.cell_type() == null) || //On check si on a une cellule valide
                ((cell != null && t.cell_type() == cell.getCellType()) && //On regarde si on a la texture avec le bon type de cellule
                ((t.dir() != null && (t.dir() == cell.getDirection() || //On regarde si l'orientation de la cellule correspond à celle de la texture
                cell.getCellType() == CellType.SLIDE && t.dir().isVertical() == cell.getDirection().isVertical()) || //Avec un cas spéciale pour les slides qui n'existent que en vertical ou horizontal
                (t.dir() == null))))){
                    return t;
            }
        }
        System.out.println("Error, couldn't find corresponding texture !");
        return null;
    }

    /**
     * Fonction qui dessine un bouton en fonction de la taille de la police d'écriture
     *
     * @param button JButton à styliser
     * @param fontsize int taille de la police
     */
    public void styleButton(JButton button, int fontsize) {
        button.setFont(new Font("Arial", Font.BOLD, fontsize));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(57, 62, 70));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(70, 70, 80));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(57, 62, 70));
            }
        });
    }

    /**
     * Permet de mettre à jour la taille des cellules en fonction de la taille de l'écran.
     */
    public static void updateCellSize() {
        cellSize = Screen.getTileSize() * Screen.getSpriteMultiplicator();
    }

    /**
     * Permet d'appliquer les renderingHints sur un graphics2d donné affin de rendre l'affichage plus propre et beau.
     *
     * @param g2d Graphics2D où appliquer les rendering hints
     */
    public void applyRenderingHints(Graphics2D g2d) {
        // Arrondis plus lisse (anticrénelage pour les formes géométriques)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Priorise la qualité à la vitesse de rendue
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        // Garde les pixels bien nets et visibles
           g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        // Améliore la qualité de l'affichage des pixels alpha
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    }
    //========================================================================= Animations =======================================================================================

    /**
    * Version alternative de drawGrid qui gérer les animations
    * @param g Graphics pour dessiner
    * @param gameGrid Cell[][] grille à dessiner
    * @param offsetX int
    * @param offsetY int
    * @param Mode String texture pack
    * @return void
    */
    private static void drawAnimaList(Graphics g, Grid gameGrid, int offsetX, int offsetY, String Mode){

        ArrayList<AnimaCell> animaList = gameGrid.getAnimaList();
        if(animaList == null || animaList.isEmpty()){
            Screen.animating = false;
            animating = false;
            return;
        }

        AnimaCell.offsetX2 = displayRange[0];
        AnimaCell.offsetY2 = displayRange[1];

        //On supprime les animations fini en début de programme pour éviter une frame où les cases animés sont absentes
        for(int owo = animaList.size()-1; owo >= 0; owo--){
            AnimaCell anima = animaList.get(owo);
            if(anima.getProgress() > 1){
                animaList.get(owo).getCell().setChanged(true);
                animaList.remove(owo);
            }
        }

        if(gameGrid == null || gameGrid.getGrid() == null){return;}
        Cell[][] grid = gameGrid.getGrid();
        boolean[][] interactGrid = gameGrid.getInteractableGrid();
        drawBackgroundGrid(g, interactGrid, grid[0].length, grid.length,offsetX,offsetY);
        
        for (int row = 0; row < grid.length; row++){
            for (int col = 0; col < grid[row].length; col++){
                Cell cell = grid[row][col];
                int x = offsetX+col * (cellSize);
                int y = offsetY+row * (cellSize);
                ImageIcon IC_0n;
                if(cell == null){
                    IC_0n = getCorrespondingTexture(cell).getTexture();
                }
                else if(!cell.isChanged() || animaList.isEmpty()){
                    if(cell.getCellType() == CellType.ENEMY || cell.getCellType() == CellType.TRASH || cell.getCellType() == CellType.WALL){
                        IC_0n = new ImageIcon(getCorrespondingTexture(cell).getTurnedImage(Direction.N));
                    }
                    else IC_0n = new ImageIcon(getCorrespondingTexture(cell).getTurnedImage(cell.getDirection()));
                    if (IC_0n != null && IC_0n.getImage() != null){
                        g.drawImage(IC_0n.getImage(), x, y, cellSize, cellSize, null);
                    }
                    else drawErrorSymbol(g, x, y);
                }
            }
        }

        for(int owo = animaList.size()-1; owo >= 0; owo--){
            AnimaCell anima = animaList.get(owo);
            if(anima.getProgress() <= 1){
                anima.anima(g, cellSize, offsetX, offsetY);
            }
        }
        
        if(animaList == null || animaList.isEmpty()){
            Screen.animating = false;
            animating = false;
            gameGrid.resetChanged();
        }
    }
}
