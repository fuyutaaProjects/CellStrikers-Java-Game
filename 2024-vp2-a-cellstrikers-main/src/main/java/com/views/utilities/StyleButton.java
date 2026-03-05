package src.main.java.com.views.utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 * Classe utilitaire pour styliser les boutons dans l'interface graphique.
 * Elle permet de créer des boutons avec un style uniforme et une animation au survol.
 *
 * @author fuyutaaProjects
 */
public class StyleButton {
    
    /**
     * Fonction permet de dessiner un bouton stylisé avec une animation quand on le survole.
     * La taille utilisée en général sur le jeu est 250x50.
     * @param button JButton
     * 
     * @author fuyutaaProjects
     */
    public static void styleButton(JButton button, int width, int height) {
    button.setFont(new Font("Arial", Font.BOLD, 20));
    button.setForeground(Color.WHITE);
    button.setBackground(new Color(57, 62, 70));
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setOpaque(true);
    // en général c'est 250x50
    button.setPreferredSize(new java.awt.Dimension(width, height));

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
}
