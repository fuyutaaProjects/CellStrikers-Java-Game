package src.main.java.com.views.menu;

import javax.swing.JPanel;

/**
 * Interface pour définir la structure d'une vue de menu.
 * Elle contient des méthodes pour obtenir le panneau et le nom de la vue.
 * @author Ethan dy
 *
 */
public interface MenuView {
    JPanel getPanel();
    String getName();
}
