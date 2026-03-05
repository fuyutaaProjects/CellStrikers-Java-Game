package src.main.java.com.views.menu;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JPanel;
import src.main.java.com.views.utilities.StyleButton;

/**
 * Classe représentant le menu principal du jeu.
 * Elle hérite de la classe AbstractMenuView et utilise un GridBagLayout pour organiser les composants.
 * Elle contient les boutons de l'énumération ButtonMenuAction.
 * @author Ethan dy
 *
 */
public class MainMenu extends AbstractMenuView {

    /**
     * Constructeur de la classe MainMenu
     */
    public MainMenu() {
        super("MAIN_MENU");
    }

    @Override
    protected JPanel createPanel() {
        return new BackgroundPanel();
    }

    @Override
    protected LayoutManager getLayout() {
        return new GridBagLayout();
    }

    @Override
    protected void initializeComponents() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 0, 10, 0);
        constraints.gridy = 1;  // Commence a 1 pour le titre

        for (ButtonMenuAction actionButton : ButtonMenuAction.values()) {
            JButton button = new JButton(actionButton.getLabel());
            StyleButton.styleButton(button, 250, 50);
            button.addActionListener(e -> actionButton.execute());
            this.getPanel().add(button, constraints);
            constraints.gridy++;
        }
    }

    @Override
    protected String getTitle() {
        return "CellStrikers";
    }

}