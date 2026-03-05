package src.main.java.com.views.menu;

import src.main.java.com.Game;
import static src.main.java.com.Game.mainPanel;
import static src.main.java.com.Game.screen;
import src.main.java.com.views.menu.option.OptionsMenu;
import src.main.java.com.views.menu.option.OptionsMenuGridSize;
import src.main.java.com.views.menu.option.OptionsMenuScreenSize;

/**
 * Enumération des noms de cartes pour le CardLayout.
 * Permet de gérer les différents menus et écrans du jeu.
 * @author Ethan dy
 *
 */
public enum CardNameMap {
    MAIN_MENU("MAIN_MENU"),
    LEVEL_SELECTION_MENU("LEVEL_SELECTION_MENU"),
    SCREEN("SCREEN"),
    SPECIAL_MODE("SPECIAL_MODE"),
    HITMAN_MODE("HITMAN_MODE"),
    OPTIONS_MENU("OPTIONS_MENU"),
    OPTIONS_MENU_SCREEN_SIZE("OPTIONS_MENU_SCREEN_SIZE"),
    OPTIONS_MENU_GRIDSIZE("OPTIONS_MENU_GRIDSIZE"),
    GRID_SCALE("GRID_SCALE"),
    LEVEL_FINISH("LEVEL_FINISH"),;

    private String label;

    CardNameMap(String label) {
        this.label = label;
    }

    /**
     * Retourne le label de la carte
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * Ajoute les différents menus au CardLayout principal.
     */
    public static void addMenuToLayout() {
        MainMenu mainMenu = new MainMenu();
        LevelSelectionMenu levelSelectionMenu = new LevelSelectionMenu("regularLevels");
        LevelSelectionMenu specialModeLevel = new LevelSelectionMenu("specialLevels");
        LevelSelectionMenu hitmanModeLevels = new LevelSelectionMenu("hitmanLevels");
        OptionsMenu optionsMenu = new OptionsMenu();
        OptionsMenuScreenSize optionsMenuScreenSize = new OptionsMenuScreenSize();
        OptionsMenuGridSize optionsMenuGridSize = new OptionsMenuGridSize();

        mainPanel.add(mainMenu.getPanel(), CardNameMap.MAIN_MENU.getLabel());
        mainPanel.add(levelSelectionMenu.getPanel(), CardNameMap.LEVEL_SELECTION_MENU.getLabel());
        mainPanel.add(screen, CardNameMap.SCREEN.getLabel());
        mainPanel.add(specialModeLevel.getPanel(), CardNameMap.SPECIAL_MODE.getLabel());
        mainPanel.add(hitmanModeLevels.getPanel(), CardNameMap.HITMAN_MODE.getLabel());
        mainPanel.add(optionsMenu.getPanel(), CardNameMap.OPTIONS_MENU.getLabel());
        mainPanel.add(optionsMenuScreenSize.getPanel(), CardNameMap.OPTIONS_MENU_SCREEN_SIZE.getLabel());
        mainPanel.add(optionsMenuGridSize.getPanel(), CardNameMap.OPTIONS_MENU_GRIDSIZE.getLabel());
    }

    /**
     * Affiche la carte spécifiée dans le CardLayout principal.
     * @param cardName le nom de la carte à afficher
     */
    public static void showCard(String cardName) {
        Game.card.show(Game.mainPanel, cardName);
    }
}

