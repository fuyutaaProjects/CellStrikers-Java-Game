package src.main.java.com.views.menu.option;

import src.main.java.com.views.GridView;

/**
 * Enumération représentant les différentes tailles de grille possible.
 * Chaque taille de grille est associée à une action qui définit la marge de la grille.
 * @author Ethan dy
 *
 */
public enum GridSizeMap {
    GRID_SIZE_2("2", () -> {
        GridView.setMargin(0.5);
    }),
    GRID_SIZE_3("3 (default)", () -> {
        GridView.setMargin(0.6);
    }),
    GRID_SIZE_4("4", () -> {
        GridView.setMargin(0.7);
    }),
    GRID_SIZE_5("5", () -> {
        GridView.setMargin(0.8);
    });

    private final String label;
    private final Runnable action;

    /**
     * Constructeur de l'énumération GridSizeMap.
     * @param label Nom du bouton.
     * @param action L'action associée à la taille de la grille.
     */
    GridSizeMap(String label, Runnable action) {
        this.label = label;
        this.action = action;
    }

    /**
     * Retourne le label
     * @return String label de la taille de la grille
     */
    public String getLabel() {
        return label;
    }

    /**
     * Exécute l'action associée à la taille de la grille.
     */
    public void getGridSizeAction() {
        action.run();
    }
}
