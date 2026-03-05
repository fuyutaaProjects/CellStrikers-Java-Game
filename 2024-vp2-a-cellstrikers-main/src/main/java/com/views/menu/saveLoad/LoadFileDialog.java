package src.main.java.com.views.menu.saveLoad;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;
import src.libs.json.JSONArray;
import src.libs.json.JSONObject;
import src.main.java.com.Game;
import src.main.java.com.Screen;
import src.main.java.com.level.LoaderJson;
import src.main.java.com.models.Cell;
import src.main.java.com.models.Grid;
import src.main.java.com.views.GridView;
import src.main.java.com.views.utilities.StyleButton;

/**
 * Cette classe représente le JDialog permettant de choisir qu'elle grille load
 * parmis un ensemble de grille contenue dans un fichier
 * 
 * Elle contient la classe @see LoadFilePanel
 * 
 * @author dy Elias
 * @version 1.3
 */
public class LoadFileDialog extends JDialog{
    
    /**
     * Méthode permettant d'initialiser les composants du JDialog et de
     * les faire fonctionner
     * 
     * @param filePath String le chemin vers le fichier où sauvegarder
     * @param gameGrid Grid la grille qui sera remplacer
     * @param view GridView la classe qui affiche la grille gameGrid
     */
    public LoadFileDialog(String filePath, Grid gameGrid, GridView view){
        this.setSize(new Dimension(Screen.getScreenWidth()/2, Screen.getScreenHeight()/2));
        this.setTitle("Save");
        this.setLocationRelativeTo(Game.window);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setModal(true);

        // Création du panneau
        LoadFilePanel load = new LoadFilePanel(filePath, new Dimension(this.getWidth(), this.getHeight()), gameGrid, view, () -> {this.dispose();});
        this.add(load);
    }
    /**
     * Cette classe permet de créer un JScrollPane permettant de choisir qu'elle grille
     * load parmis un ensemble de grille se trouvant dans un répertoire
     */
    public static class LoadFilePanel extends JScrollPane{

        /**
         * Constructeur de la classe qui permet d'afficher ses différents composant
         * @param filePath String le chemin vers le fichier où sauvegarder
         * @param dim Dimension les dimension du conteneur qui acceuille le JScrollPane
         * @param gameGrid Grid la grille qui sera remplacer
         * @param view GridView la classe qui affiche la grille gameGrid
         * @param quitEvent Runnable l'évènement pour quitter
         */
        public LoadFilePanel(String filePath, Dimension dim, Grid gameGrid, GridView view, Runnable quitEvent){
            this.setSize(dim);
            this.setUi();

            JPanel pan = new JPanel();

            pan.setLayout(new GridBagLayout());
            pan.setBackground(new Color(17, 13, 44));

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridheight = 1;
            constraints.gridwidth = 1;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 0;
            constraints.insets = new Insets(Screen.getSpriteSize()/4, 0, Screen.getSpriteSize()/4, 0);
            constraints.anchor = GridBagConstraints.PAGE_START;

            // Ajout du titre :
            JLabel title = new JLabel("Choose the file to load if you want :");
            title.setFont(new Font("Verdana", 1, this.getWidth()/(Screen.getSpriteSize()/2)));
            title.setForeground(Color.WHITE);

            pan.add(title, constraints);
            constraints.gridy++;
            constraints.insets = new Insets(0, 0, 0, 0);
            constraints.weighty = 1;
            
            // Ajout des boutons correspondant aux fichiers :
            File[] jsonFile = LoaderJson.getJsonFilesInDirectory(filePath);
    
            for (File file : jsonFile) {
                JButton loadButton = new JButton(file.getName());
                StyleButton.styleButton(loadButton, this.getWidth(), Screen.getSpriteSize());
    
                // Ajout du comportement du bouton :
                loadButton.addActionListener((e) -> {
                    load(file, gameGrid, view);
                    quitEvent.run();
                });
                pan.add(loadButton, constraints);
                constraints.gridy++;
            }
           constraints.gridy++;
           constraints.weighty = 1;
           constraints.fill = GridBagConstraints.VERTICAL;
           pan.add(Box.createVerticalGlue(), constraints);

           // Fix : force une taille plus grande
           int totalHeight = Screen.getSpriteSize() + jsonFile.length * (Screen.getSpriteSize()*2);
           
           pan.setPreferredSize(new Dimension(dim.width, totalHeight));
           this.setViewportView(pan);
           this.revalidate();
           this.repaint(); 
        }

        /**
         * Méthode permettant de charger la grille
         * @param save File le fichier de sauvegarde
         * @param gameGrid Grid la grille qui sera remplacée
         * @param view GridView ce qui affiche gameGrid
         */
        private void load(File save, Grid gameGrid, GridView view){
            String content = null;
            try {
                // Charge la sauvegarde
                content = new String(Files.readAllBytes(save.toPath()));
                JSONObject levelData = new JSONObject(content);

                JSONArray gridArray = levelData.getJSONArray("grid");
                JSONArray interactGridArray = levelData.getJSONArray("interactGrid");

                String[][] stringCellsGrid = LoaderJson.parseGrid(gridArray);
                Cell[][] cellsGrid = LoaderJson.gridmaker(stringCellsGrid);
                boolean[][] interactGrid = LoaderJson.parseInteractGrid(interactGridArray);

                // Ajout de la grille :
                view.setGrid(gameGrid);
                view.setDisplayRange(cellsGrid);
                gameGrid.setGrid(cellsGrid);
                gameGrid.setInteractableGrid(interactGrid);
                view.repaint();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            view.setbgSave(new BufferedImage[gameGrid.getGrid().length][gameGrid.getGrid()[0].length]);
        }

        /**
         * Méthode permettant de changer l'ui du paneau
         */
        private void setUi(){
            this.getHorizontalScrollBar().setUI(generateUi());
            this.getVerticalScrollBar().setUI(generateUi());
        }

        /**
         * Méthode permettant de générer une BasicScrollBarUI pour les 
         * JScrollBar du panneau
         * @return une BasicScrollBarUI
         */
        private BasicScrollBarUI generateUi(){
            BasicScrollBarUI ui = new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(83, 13, 44); // Couleur du curseur
                    this.trackColor = new Color(2, 1, 15); // Couleur de la piste
                }
            
                @Override
                protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(thumbColor);
                    g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
                    g2.dispose();
                }

                @Override
                protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(trackColor);
                    g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
                    g2.dispose();
                }
            };
            return ui;
        }
            
    }
}
