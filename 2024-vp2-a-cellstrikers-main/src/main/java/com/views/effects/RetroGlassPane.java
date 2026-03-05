package src.main.java.com.views.effects;

import src.main.java.com.Game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import javax.swing.JComponent;

/**
 * Cette classe permet d'appliquer un effet rétro sur le JPanel de l'application.
 */
public class RetroGlassPane extends JComponent {

    public static float retroEffectIntensity = 1;   // Intensité de l'effet rétro
    private int spaceBetweenScanlines = 4;  // Espacement entre les lignes noires

    // Dimensions du panneau
    private int width;
    private int height;

    /**
     * Constructeur de la classe RetroGlassPane.
     * Initialise le panneau en le rendant transparent.
     */
    public RetroGlassPane() {
        setOpaque(false);

        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            super.paintComponent(g);

            // surveiller les changements de taille d'écran
            width = getWidth();
            height = getHeight();

            if (width == 0 || height == 0) return;

            Graphics2D g2d = (Graphics2D) g.create();

            if (Game.retroEffect) {
                applyScanlines(g2d); // les traits noirs
                applyVignette(g2d);  // l’effet sur les bords de l’écran
            };

            g2d.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Applique un effet de scanlines (lignes noires) sur le Graphics2D donné.
     *
     * @param g2d Le Graphics2D sur lequel appliquer l'effet.
     */
    private void applyScanlines(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 100));
        for (int y = 0; y < height; y += spaceBetweenScanlines) {
            g2d.drawLine(0, y, width, y);
        }
    }

    /**
     * Applique un effet de vignette sur le Graphics2D donné.
     *
     * @param g2d Le Graphics2D sur lequel appliquer l'effet.
     */
    private void applyVignette(Graphics2D g2d) {
        g2d.setComposite(AlphaComposite.SrcOver);
    
        float radius = Math.max(width, height) * 0.6f;
        Point center = new Point(width / 2, height / 2);
    
        RadialGradientPaint vignette = new RadialGradientPaint(
            center,
            radius,
            new float[] {0.0f, 0.8f, 1.0f},
            new Color[] {
                new Color(0, 0, 0, 0),     // centre
                new Color(0, 0, 0, 0),     // pré-bords
                new Color(0, 0, 0, (int)(220 * retroEffectIntensity)) // bords
            }
        );
    
        g2d.setPaint(vignette);
        g2d.fillRect(0, 0, width, height);
    }
}