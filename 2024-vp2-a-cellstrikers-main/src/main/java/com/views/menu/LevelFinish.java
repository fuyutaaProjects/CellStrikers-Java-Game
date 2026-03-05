package src.main.java.com.views.menu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import src.main.java.com.Game;
import src.main.java.com.Screen;
import src.main.java.com.level.LevelLoader;
import src.main.java.com.views.utilities.StyleButton;

/**
 * Cette classe représente l'écran de fin de niveau.
 * Elle hérite de la classe AbstractMenuView et permet d'afficher les options de fin de niveau.
 * Elle contient des boutons pour passer au niveau suivant ou retourner au menu principal.
 * 
 * @author dy Elias
 * @version 1.4
 */
public class LevelFinish extends AbstractMenuView{
    private int level; // Le prochain niveau
    private String levelType; // Le type du niveau (correspond au nom du dossier ou il est rangé)
    private JPanel buttonPanel; // Panel des boutons

    /**
     * Constructeur de la classe LevelFinish
     * Permet de récupérer le niveau suivant et le type de niveau
     *
     * @param currentLevel niveau actuel
     * @param levelType type de niveau (ex: "normal", "special", etc.)
     */
    public LevelFinish(Integer currentLevel, String levelType) {
        super("LEVEL_FINISH");
        level = currentLevel + 1; // Le prochain niveau
        this.levelType = levelType; // Le type du niveau (correspond au nom du dossier ou il est rangé)
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        JPanel buttonPanelBox = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Boite pour stocker le panel de bouton
        buttonPanelBox.setOpaque(false);
        buttonPanel = new JPanel(new GridLayout(2,1,0,15)); // Panel pour les boutons
        buttonPanel.setOpaque(false);

        // Bouton pour le niveau suivant
        JButton nextLevel = new JButton("Next level");
        StyleButton.styleButton(nextLevel, Screen.getSpriteSize() * 4, Screen.getSpriteSize());
        nextLevel.addActionListener((e) -> {
            LevelSelectionMenu.loadAndShowLevel(level, levelType);
        });

        // Bouton pour retourner au menu
        JButton back = new JButton("Back");
        StyleButton.styleButton(back, Screen.getSpriteSize() * 4, Screen.getSpriteSize());
        back.addActionListener(e -> {
            Game.mainPanel.remove(getPanel());
            CardNameMap.showCard(CardNameMap.MAIN_MENU.getLabel());
        });

        // Rajout des boutons si le niveau existe
        if(LevelLoader.levelExist(level, levelType)){
            buttonPanel.add(nextLevel);
        }
        buttonPanel.add(back);
        buttonPanelBox.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(buttonPanelBox, BorderLayout.SOUTH);
    }

    @Override
    protected JPanel createPanel() {
        return new BackgroundPanel();
    }

    @Override
    protected String getTitle() {
        return "Level clear !";
    }

}
