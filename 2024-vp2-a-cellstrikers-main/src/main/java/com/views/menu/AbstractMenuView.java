package src.main.java.com.views.menu;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Classe abstraite qui permet de faire un template des menus.
 * Elle contient les éléments principales d'un menu, un titre, un layout de base et un panel principal.
 * @author Ethan dy
 */
public abstract class AbstractMenuView implements MenuView {
    protected final JPanel panel;
    private final String name;

    /**
     * Constructeur de la classe AbstractMenuView qui utilise les fonctions redéfinies pour initialiser le menu
     * @param name nom du menu
     */
    public AbstractMenuView(String name) {
        this.name = name;
        this.panel = createPanel();
        setupLayout();
        setTitle();
        initializeComponents();
    }

    /// Méthode abstraite qui doit être redéfinie pour initialiser les composants du menu
    protected abstract void initializeComponents();

    /**
     * Méthode qui peut être redéfinie pour changer le panel de base, par exemple avoir BackgroundPanel
     * @return Jpanel panel principal du menu
     */
    protected JPanel createPanel() {
        return new JPanel();
    }

    /**
     * Méthode qui peut être redéfinie pour changer le titre du menu
     * @return String titre du menu
     */
    protected String getTitle() {
        return "Default Title"; // Titre par défaut
    }

    /**
     * Méthode pour mettre un titre de base sur le menu
     */
    protected void setTitle() {
        JLabel title = new JLabel(getTitle(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        this.panel.add(title);
    }

    /**
     * Méthode qui peut être redéfinie pour changer le layout du panel principal
     * @return LayoutManager layout du panel principal
     */
    protected LayoutManager getLayout() {
        return new BorderLayout(); // Layout de base
    }

    /**
     * Méthode qui permet de définir le layout du panel principal avec une marge pour pas toucher les bords
     */
    private void setupLayout(){
        panel.setLayout(getLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(
                    20,20,20,20));  // Marge pour pas toucher les bords
    }

    /**
     * Méthode qui renvoit le panel principal
     * @return JPanel panel principal
     */
    @Override
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Méthode qui renvoit le nom du menu
     * @return String nom du menu
     */
    @Override
    public String getName() {
        return name;
    }

    // Classe interne qui permet d'avoir le fond d'écran et qu'il s'adapte à la taille de la fenêtre
    protected class BackgroundPanel extends JPanel {
        private BufferedImage backgroundImage;

        public BackgroundPanel(){
            try {
                backgroundImage = ImageIO.read(new File("src/resources/background/mainMenu.png"));
            } catch (IOException e) {
                System.err.println("Erreur de chargement du fond : " + e);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
