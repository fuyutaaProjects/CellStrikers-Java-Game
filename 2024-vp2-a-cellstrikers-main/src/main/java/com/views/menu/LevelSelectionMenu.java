package src.main.java.com.views.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import src.main.java.com.controllers.LevelEditorController;
import src.main.java.com.level.LevelLoader;
import src.main.java.com.level.LoaderJson;
import src.main.java.com.views.utilities.StyleButton;

/**
 * Classe représentant le menu de sélection de niveau.
 * Elle hérite de la classe AbstractMenuView et permet de charger et afficher les niveaux disponibles.
 * Elle contient des méthodes pour trier les fichiers de niveaux et afficher les boutons correspondants.
 * @author Ethan dy
 *
 */
public class LevelSelectionMenu extends AbstractMenuView {
    private String levelType;

    /**
     * Constructeur de la classe LevelSelectionMenu
     * @param levelType type de niveau (ex: "normal", "special", etc.)
     */
    public LevelSelectionMenu(String levelType) {
        super("LEVEL_SELECTION_MENU");
        this.levelType = levelType;
        System.out.println(levelType);
        initializeComponents();
    }

    @Override
    protected JPanel createPanel() {
        return new BackgroundPanel();
    }

    @Override
    protected void initializeComponents() {
    ///////////////////////////////////////// Milieu de l'écran ////////////////////////////////////////////////////////

        // Permet de stocker les boutons de manière a ce qu'ils s'affichent en ligne
        JPanel panelLevel = new JPanel(new GridLayout(0,5,10,30));
        panelLevel.setOpaque(false);

        // ScrollPane qui permet de faire défiler les boutons quand il y en a trop ou que la fenêtre est trop petite.
        JScrollPane scrollPane = new JScrollPane(panelLevel);

        // Rends le scrollPane transparent
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        // Permet de faire apparaître la barre de défilement uniquement si nécessaire
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Permet de stocker le panel des boutons pour éviter le changement de taille à cause du BorderLayout
        JPanel panelLevelBox = new JPanel(new BorderLayout(50,50));
        panelLevelBox.setOpaque(false);

        // Ajout des boutons pour chaque niveau
        File[] files = LoaderJson.getJsonFilesInDirectory(LevelEditorController.SAVE_PATH + "levels/" + levelType + "/");
        sortFilesByNumericOrder(files); // car le level 10 s'affiche avant le level 2 sinon, donc on va trier numériquement
        for (File file : files) {
            try {
                Integer levelId = Integer.parseInt( file.getName().replace(".json", "") );

                JButton button = new JButton(levelId.toString());
                StyleButton.styleButton(button, 75, 75);
                button.addActionListener(e -> LevelSelectionMenu.loadAndShowLevel(levelId, this.levelType));

                panelLevel.add(button);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement du fichier " + file.getName() + ": " + e.getMessage());
            }
        }

        panelLevelBox.add(scrollPane);
        panel.add(panelLevelBox, BorderLayout.CENTER);

/////////////////////////////////////////////// Bas de l'écran /////////////////////////////////////////////////////////

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBottom.setOpaque(false);
        JButton backButton = new JButton("Back");
        StyleButton.styleButton(backButton, 250, 50);
        backButton.addActionListener(e -> CardNameMap.showCard(CardNameMap.MAIN_MENU.getLabel()));
        panelBottom.add(backButton);
        panel.add(panelBottom, BorderLayout.SOUTH);
    }

    @Override
    protected String getTitle() {
        return "Select a Level";
    }

    @Override
    protected void setTitle() {
        JLabel title = new JLabel(getTitle(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        this.panel.add(title, BorderLayout.NORTH);
    }

    /**
     * Méthode qui prend un tableau de fichiers et les trie par ordre numérique pour afficher les niveaux correctement
     * @param files
     */
    private void sortFilesByNumericOrder(File[] files) {
        for (int i=0; i<files.length; i++){
            int minIdx = i;
            for (int j=i+1; j<files.length;j++){
                int minIdxFileName = Integer.parseInt(files[minIdx].getName().replace(".json", ""));
                int jIdxFileName = Integer.parseInt(files[j].getName().replace(".json", ""));

                if (jIdxFileName < minIdxFileName){
                    minIdx = j;
                }
            }
            if (minIdx != i){
                File temp = files[i];
                files[i] = files[minIdx];
                files[minIdx] = temp;
            }
        }
    }

    /**
     * Méthode qui permet de charger un niveau et de l'afficher
     *
     * @param levelId numéro du niveau
     * @param levelType type du niveau
     */
    public static void loadAndShowLevel(int levelId, String levelType) {
        LevelLoader.loadLevel(levelId, levelType);
    }
}