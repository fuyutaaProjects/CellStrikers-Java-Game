package src.main.java.com.views.menu;

import src.main.java.com.Game;

/**
 * Enumération pour définir les actions des boutons du menu principal avec leurs labels.
 * @author Ethan dy
 */
public enum ButtonMenuAction {
    START("Start", () -> CardNameMap.showCard(CardNameMap.LEVEL_SELECTION_MENU.getLabel())),
    SPECIAL_MODE("Special Mode", () -> CardNameMap.showCard(CardNameMap.SPECIAL_MODE.getLabel())),
    HITMAN_MODE("Hitman Mode", () -> CardNameMap.showCard(CardNameMap.HITMAN_MODE.getLabel())),
    LEVEL_EDITOR("Level Editor", () -> Game.showGridScalePanel(true)),
    SANDBOX("Sandbox", () -> Game.showGridScalePanel(false)),
    OPTIONS("Options", () -> CardNameMap.showCard(CardNameMap.OPTIONS_MENU.getLabel())),
    QUIT("Quit", () -> {System.exit(0);});

    private String label;
    private Runnable action;

    /**
     * Constructeur de l'énumération ButtonMenuAction
     * @param label nom du bouton
     * @param action action à exécuter pour le bouton
     */
    ButtonMenuAction(String label, Runnable action) {
        this.label = label;
        this.action = action;
    }

    /**
     * Retour le label du bouton
     * @return String label du bouton
     */
    public String getLabel() {
        return label;
    }

    /**
     * Méthode pour exécuter les actions des boutons
     */
    public void execute() {
        action.run();
    }
}
