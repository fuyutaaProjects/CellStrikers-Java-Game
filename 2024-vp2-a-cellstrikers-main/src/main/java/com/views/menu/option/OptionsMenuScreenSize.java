package src.main.java.com.views.menu.option;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JPanel;
import src.main.java.com.views.menu.AbstractMenuView;
import src.main.java.com.views.menu.CardNameMap;
import src.main.java.com.views.utilities.StyleButton;

/**
 * Classe représentant le menu d'options pour la taille de l'écran.
 * Elle hérite de la classe AbstractMenuView et utilise un GridBagLayout.
 * Elle permet à l'utilisateur de choisir la taille de l'écran pour le jeu.
 * @author Ethan dy
 */
public class OptionsMenuScreenSize extends AbstractMenuView {

    public OptionsMenuScreenSize() {
        super("OPTIONS_MENU_SCREEN_SIZE");
    }

    @Override
    protected void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        int gridYCounter = 1;

////////////////////////////////////////////////////// CREATION DES BOUTONS/////////////////////////////////////////////

        // Génération des boutons de résolution
        for (ResolutionMap resolution : ResolutionMap.values()) {
            JButton button = new JButton(resolution.getLabel());
            StyleButton.styleButton(button, 250, 50);
            button.addActionListener(e -> {
                resolution.getResolutionAction();
            });
            gbc.gridy = gridYCounter++;
            panel.add(button, gbc);
        }

        // Bouton retour
        JButton backButton = new JButton("Back");
        StyleButton.styleButton(backButton, 250, 50);
        backButton.addActionListener(e -> CardNameMap.showCard(CardNameMap.OPTIONS_MENU.getLabel()));
        gbc.gridy = gridYCounter++;
        panel.add(backButton, gbc);

    }

    @Override
    protected JPanel createPanel() {
        return new BackgroundPanel();
    }

    @Override
    protected String getTitle() {
        return "Taille de l'écran";
    }

    @Override
    protected LayoutManager getLayout() {
        return new GridBagLayout();
    }



}
