package src.main.java.com.views.menu.option;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import src.main.java.com.Screen;
import src.main.java.com.views.GridView;
import src.main.java.com.views.menu.AbstractMenuView;
import src.main.java.com.views.menu.CardNameMap;
import src.main.java.com.views.utilities.StyleButton;

/**
 * Classe représentant le menu d'options pour la taille de la grille.
 * Elle hérite de la classe AbstractMenuView et utilise un gridBagLayout.
 * Elle permet à l'utilisateur de choisir la taille de la grille pour le jeu.
 * Elle possède un panel de simulation qui permet de visualiser les changements de taille.
 * @author Ethan dy
 */
public class OptionsMenuGridSize extends AbstractMenuView {
    public OptionsMenuGridSize() {
        super("OPTIONS_MENU_GRIDSIZE");
    }

    @Override
    protected void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        int gridYCounter = 1;

        // Création du panel de simulation
        SimulationPanel simulationPanel = new SimulationPanel();
        simulationPanel.setPreferredSize(new Dimension(Screen.getScreenWidth() / Screen.getSpriteMultiplicator()
                                        ,Screen.getScreenHeight() / Screen.getSpriteMultiplicator()));
        simulationPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        gbc.gridy = gridYCounter++;
        panel.add(simulationPanel, gbc);

////////////////////////////////////////////////////// CREATION DES BOUTONS/////////////////////////////////////////////

        for (GridSizeMap gridSize : GridSizeMap.values()) {
            JButton button = new JButton(gridSize.getLabel());
            StyleButton.styleButton(button, 250, 50);
            button.addActionListener(e -> {
                gridSize.getGridSizeAction();
                simulationPanel.repaint();
                panel.repaint();
            });
            gbc.gridy = gridYCounter++;
            panel.add(button, gbc);
        }

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
    protected LayoutManager getLayout() {
        return new GridBagLayout();
    }

    @Override
    protected String getTitle() {
        return "Grid Size";
    }

    /**
      * Panel pour simuler un écran avec une grille de level. Permets de visualiser les changements de taille.
      * On va créer un panel, mettre son contour visible, puis dessiner une grille avec drawRect qui répresente un level.
      * Cette grille est alors centrée dans le panel.
    */
    class SimulationPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();
            int width = getWidth();
            int height = getHeight();

            //  Dessiner le contour sur les bords du panel, pour visualiser la taille de l'écran
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, width, height);

            int rows = 6;
            int cols = 10;

            // Taille de la grille en fonction de la marge
            int gridWidthAvailable = (int)(width * GridView.margin);
            int gridHeightAvailable = (int)(height * GridView.margin);

            // Taille des cases
            int cellsize = Math.min(gridWidthAvailable / cols, gridHeightAvailable / rows);

            // Taille de la grille
            int gridWidth = cols * cellsize;
            int gridHeight = rows * cellsize;

            // Décalage pour centrer la grille
            int offsetX = (width - gridWidth) / 2;
            int offsetY = (height - gridHeight) / 2;

            // Dessiner la grille
            g2d.setPaint(Color.WHITE);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    int posX = offsetX + (j * cellsize);
                    int posY = offsetY + (i * cellsize);
                    g2d.drawRect(posX, posY, cellsize, cellsize);
                }
            }
            g2d.dispose();
        }
    }
}
