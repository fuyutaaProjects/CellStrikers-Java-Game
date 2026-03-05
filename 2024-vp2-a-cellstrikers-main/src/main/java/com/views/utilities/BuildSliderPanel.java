package src.main.java.com.views.utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

/**
 * Classe utilitaire pour créer un panneau de slider avec un label.
 * Elle permet de styliser le slider et le label pour les menus d'options.
 * @author fuyutaaProjects
 */
public class BuildSliderPanel {

    /**
     * @author fuyutaaProjects
     * @param slider le slider qu'on veut styliser est passé en paramètre
     * @param labelText JLabel affiché
     * @param initialValue Valeur de base affichée sur le texte affichant la value actuelle.
     * @param width
     * @param height
     * @return
     */
    public static JPanel buildSliderPanel(JSlider slider, String labelText, int initialValue, int width, int height) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(width, height));
        panel.setForeground(Color.WHITE);
        panel.setBackground(new Color(57, 62, 70));

        JLabel label = new JLabel(labelText + " : " + initialValue, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);

        slider.setPaintLabels(true);
        slider.setBackground(new Color(57, 62, 70));

        // Pour le comportement propre au slider (éditer le bon channel de son sfx/master si c'est un slider de son par exemple), il sera géré par un autre addChangeListener dans
        // OptionsMenu. On aurait pu faire un switch ici mais c'est moche un switch déjà, et aussi ça cause une dépendance à ce fichier: il faudrait que le cas soit géré. 
        slider.addChangeListener(e -> {
            int val = slider.getValue();
            label.setText(labelText + " : " + val);
        });

        panel.add(label);
        panel.add(slider);

        return panel;
    }

}
