package src.main.java.com.views.menu.option;

import src.main.java.com.Game;
import src.main.java.com.Screen;

/**
 * Enumération représentant les différentes résolutions d'écran possibles.
 * Chaque résolution est associée à une action qui définit la taille de l'écran.
 * @author Ethan dy
 */
public enum ResolutionMap {
    RESOLUTION_800x600("800 x 600", () -> {
        setScreenSize(800, 600);
    }),
    RESOLUTION_1024x768("1024 x 768", () -> {
        setScreenSize(1024, 768);
    }),
    RESOLUTION_1280x720("1280 x 720", () -> {
        setScreenSize(1280, 720);
    }),
    RESOLUTION_1920x1080("1920 x 1080", () -> {
        setScreenSize(1920, 1080);
    });

    private final String label;
    private final Runnable action;

    /**
     * Constructeur de l'énumération ResolutionMap.
     * @param label Nom du bouton.
     * @param action L'action associée à la résolution d'écran.
     */
    ResolutionMap(String label, Runnable action) {
        this.label = label;
        this.action = action;
    }

    /**
     * Retourne le label de la résolution d'écran.
     * @return String label de la résolution
     */
    public String getLabel() {
        return label;
    }

    /**
     * Exécute l'action associée à la résolution d'écran.
     */
    public void getResolutionAction() {
        action.run();
    }

    /**
     * Fonction permet de changer la taille de l'écran.
     * @param width int
     * @param height int
     */
    private static void setScreenSize(int width, int height) {
        Game.screen.setScreenSize(width, height);
        if (width >= 1920 && height >= 1080) {
            Screen.setSpriteMultiplicator(4);
        }else if (width >= 1280 && height >= 720){
            Screen.setSpriteMultiplicator(3);
        }else if (width >= 800 && height >= 800){
            Screen.setSpriteMultiplicator(2);
        }
        Game.window.pack(); // Applique l'ajustement de la taille d'écran
    }
}
