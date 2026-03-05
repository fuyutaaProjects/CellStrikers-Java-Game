package src.main.java.com.views.sandboxEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import src.main.java.com.Game;
import src.main.java.com.Screen;
import src.main.java.com.controllers.LevelEditorController;
import src.main.java.com.controllers.SandboxController;
import src.main.java.com.models.Cell;
import src.main.java.com.models.Grid;
import src.main.java.com.views.GridView;
import src.main.java.com.views.menu.CardNameMap;
import src.main.java.com.views.menu.saveLoad.LoadFileDialog;
import src.main.java.com.views.utilities.StyleButton;

/**
 * Cette classe hérite de JPanel et permet de représenter
 * le panneau permettant de choisir la taille de la grille dans le mode
 * sandbox et le mode éditeur de niveau
 * 
 * @author dy Elias
 * @version 1.2
 */
public class GridScalePanel extends JPanel{
    boolean inEditor;// Si on est dans l'éditeur ou non

    JSpinner widthSelector;
    JSpinner heightSelector;

    /**
     * Constructeur de la classe qui permet d'initialiser ses champs
     * et composants et d'afficher ses composants
     * @param inEditor boolean si on veut aller vers l'éditeur de niveau ou non
     */
    public GridScalePanel(boolean inEditor){
        this.setLayout(new GridLayout(2, 1));
        this.setSize(new Dimension(Screen.getScreenWidth(), Screen.getScreenHeight()));
        this.inEditor = inEditor;

        // Définition des panneaux :
        JPanel elementPanel = new JPanel(new GridBagLayout()); //Panneau pour tous les éléments
        elementPanel.setBackground(new Color(17, 13, 44));

        // Contrainte du GridBagLayout :
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridheight = 1;
        constraints.gridy = 0;
        constraints.weighty = 0.5;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel mainTitle = new JLabel("Create a new grid :");
        mainTitle.setForeground(Color.WHITE);
        mainTitle.setFont(new Font("Arial", 1, Screen.getSpriteSize()));

        elementPanel.add(mainTitle, constraints);
        constraints.gridy++;
        constraints.weighty = 0;

        JPanel scalePanel = new JPanel(); // Pour les JSpinner et leur texte associé
        JPanel buttonPanel = new JPanel(); // Pour les deux boutons

        // Paramétrage des JSpinner : 

        int maxValue; //Taille maximale de la grille

        if(inEditor){
            maxValue = 20;
        }else{
            maxValue = 1000;
        }
        SpinnerNumberModel heightRange = new SpinnerNumberModel(10, 1, maxValue, 1);
        SpinnerNumberModel widthRange = new SpinnerNumberModel(10, 1, maxValue, 1);

        widthSelector = new JSpinner(widthRange);
        heightSelector = new JSpinner(heightRange);

        JLabel widthLabel = new JLabel("Width");
        scalePanel.add(widthLabel);
        scalePanel.add(widthSelector);

        JLabel heightLabel = new JLabel("x Height");
        scalePanel.add(heightLabel);
        scalePanel.add(heightSelector);

        // Ajout du titre :
        JLabel title = new JLabel("Choose the size of the grid : ");
        title.setHorizontalAlignment((int) JPanel.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", 1, Screen.getSpriteSize()/2));

        // Création des boutons et ajout de leur comportement :
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener((e) -> {
            createGrid();
        });
        JButton back = new JButton("Back");
        StyleButton.styleButton(back, Screen.getSpriteSize()*3, Screen.getSpriteSize()/2);
        back.addActionListener((e) -> {
            CardNameMap.showCard(CardNameMap.MAIN_MENU.getLabel());
        });

        StyleButton.styleButton(confirm, Screen.getSpriteSize()*3, Screen.getSpriteSize()/2);

        buttonPanel.add(back);
        buttonPanel.add(confirm);

        // Ajout des Panneaux :
        elementPanel.add(title, constraints);
        constraints.gridy++;
        
        elementPanel.add(scalePanel, constraints);
        constraints.gridy++;

        elementPanel.add(buttonPanel, constraints);
        constraints.gridy++;
        constraints.weighty = 1;
        elementPanel.add(Box.createVerticalGlue(), constraints);

        // Pour le LoadFilePanel :
        Grid grid = new Grid(new Cell[1][1], new boolean[1][1]);
        String filePath;

        if(inEditor){
            filePath = LevelEditorController.SAVE_PATH + LevelEditorController.getSaveFolder();
        }else{
            filePath = SandboxController.SAVE_PATH + SandboxController.getSaveFolder();
        }
        SandboxMod view = new SandboxMod(grid);
        GridView.texture_pack = "wise_tree";

        this.add(new LoadFileDialog.LoadFilePanel(filePath, new Dimension(this.getWidth(), this.getHeight()/2), grid, view, () -> {
            if(inEditor){
                Game.showLevelEditor((LevelEditor) view);
            }else{
                Game.showSandboxMod(view);
            }
        }));
        this.add(elementPanel);
    }

    /**
     * Méthode permettant de créer la grille de jeu 
     * avec les valeurs séléctionné par le joueur et
     * permet de passer vers l'éditeur de niveau ou le mode
     * sandbox selon la valeur de inEditor
     */
    private void createGrid(){
        int width = (int) widthSelector.getValue();
        int height = (int) heightSelector.getValue();

        Grid grid = new Grid(new Cell[height][width], new boolean[height][width]);

        if(inEditor){
            Game.showLevelEditor(grid);
        }else{
            Game.showSandboxMod(grid);
        }
    }
}
