package src.main.java.com.views.sandboxEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import src.main.java.com.models.CellType;
import src.main.java.com.models.Grid;
import src.main.java.com.views.GridView;
import src.main.java.com.views.utilities.StyleButton;

/**
 * Classe représentant l'éditeur de niveau.
 * Elle hérite de la classe GridView et permet de créer et de modifier des niveaux.
 * Elle contient des boutons pour sélectionner, supprimer, tourner et interagir avec les cellules.
 * @author Ethan dy
 */
public class LevelEditor extends GridView{
    // Composant en plus
    private JPanel cellSelector;
    private JPanel botPanel;
    private JPanel rightPanel;

    private JButton saveButton;
    private JButton loadButton;
    private JButton switchModeButton = new JButton("simulate"); // Bouton permettant de switch entre le mode simulation et le mode editeur

    // Liste des noms de chaque cellule
    private final String[] nameIcon = {"repulse","flipper","generator","counter_spinner","spinner","mover",
                                       "nudge","pig", "push","slide","directional","enemy","trash","wall", "hitman", "boss"};

    // Information d'une cellule et le listener
    private CellType selected;
    private ActionListener cellSelectorListener;

    // Boolean pour les interactions à faire pour les interactions
    private boolean deleteCell = false;
    private boolean canRotate = false;
    private boolean changeInter = false;
    private boolean selectMode = false;

    public LevelEditor(Grid grid) {
        super(grid, "");

        // Création des nouveaux boutons
        this.saveButton = new JButton("Save");
        this.loadButton = new JButton("Load");

        StyleButton.styleButton(saveButton,BUTTON_WIDTH,BUTTON_HEIGHT);
        StyleButton.styleButton(loadButton,BUTTON_WIDTH,BUTTON_HEIGHT);

        StyleButton.styleButton(switchModeButton, BUTTON_WIDTH, BUTTON_HEIGHT);
        switchModeButton.setBounds(getBackButton().getX(), BUTTON_HEIGHT*3, BUTTON_WIDTH, BUTTON_HEIGHT);

        // Placement des boutons
        JPanel buttonPanel = new JPanel(new GridLayout(1,3,20,0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(getBackButton());
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        getTopPanel().remove(0);
        getTopPanel().add(buttonPanel);

        // Boutons inutiles
        getRestartButton().setEnabled(false);
        getPlayState().setEnabled(false);
        getPauseGameButton().setEnabled(false);
        getLaunchGameButton().setEnabled(false);
        getRestartButton().setVisible(false);
        getPauseGameButton().setVisible(false);
        getPlayState().setVisible(false);
        getLaunchGameButton().setVisible(false);

        botPanel = new JPanel(new BorderLayout());
        botPanel.setOpaque(false);

        cellSelector = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cellSelector.setOpaque(false);

        // Ajouter des boutons pour sélectionner les cellules
        int i = 0;
        for (CellType type : CellType.values()) {
            JButton button = new JButton();

            // Permet d'obtenir l'image de la cellule qu'on veut poser
            URL imagePath = getClass().getResource("/src/resources/texture/" + nameIcon[i] + ".png");
            if (imagePath == null) {
                System.out.println("Image not found: " + nameIcon[i]);
                return;
            }
            ImageIcon cellIcon = resizeImage(new ImageIcon(imagePath));
            button.setIcon(cellIcon);

            // Enlève le fond du bouton
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);

            button.addActionListener(e -> {
                setSelectedCell(type);
                deleteCell = false;
                canRotate = false;
                changeInter = false;
                selectMode = false;
                if (cellSelectorListener != null) {
                    cellSelectorListener.actionPerformed(null);
                }
            });
            cellSelector.add(button);
            i++;
        }

        // Créer un panneau pour les boutons de sélection, suppression, rotation et interaction
        this.rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(200,50));

        JButton selectionButton = new JButton("Selection");
        StyleButton.styleButton(selectionButton,BUTTON_WIDTH,BUTTON_HEIGHT);
        selectionButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        selectionButton.addActionListener(e -> {
            setSelectedCell(null);
            deleteCell = false;
            canRotate = false;
            changeInter = false;
            selectMode = true;
            if (cellSelectorListener != null) {
                cellSelectorListener.actionPerformed(null);
            }
        });

        // Bouton pour supprimer une cellule
        JButton deleteButton = new JButton("Delete");
        StyleButton.styleButton(deleteButton,BUTTON_WIDTH,BUTTON_HEIGHT);
        deleteButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        deleteButton.addActionListener(e -> {
            setSelectedCell(null);
            deleteCell = true;
            canRotate = false;
            changeInter = false;
            selectMode = false;
            if (cellSelectorListener != null) {
                cellSelectorListener.actionPerformed(null);
            }
        });

        // Bouton pour tourner une cellule
        JButton turnButton = new JButton("Turn");
        StyleButton.styleButton(turnButton,BUTTON_WIDTH,BUTTON_HEIGHT);
        turnButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        turnButton.addActionListener(e -> {
            setSelectedCell(null);
            deleteCell = false;
            canRotate = true;
            changeInter = false;
            selectMode = false;
            if (cellSelectorListener != null) {
                cellSelectorListener.actionPerformed(null);
            }
        });


        // Bouton pour changer les interactions
        JButton interactButton = new JButton("Interaction");
        StyleButton.styleButton(interactButton,BUTTON_WIDTH,BUTTON_HEIGHT);
        interactButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        interactButton.addActionListener(e -> {
            setSelectedCell(null);
            deleteCell = false;
            canRotate = false;
            changeInter = true;
            selectMode = false;
            if (cellSelectorListener != null) {
                cellSelectorListener.actionPerformed(null);
            }
        });

        rightPanel.add(selectionButton);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(deleteButton);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(turnButton);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(interactButton);

        add(rightPanel, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(cellSelector);
        // Rends le scrollPane transparent
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        // Permet de faire apparaître la barre de défilement uniquement si nécessaire
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        botPanel.add(scrollPane, BorderLayout.CENTER);
        updateLayout();
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                updateLayout();
            }
        });
        this.add(botPanel);
        this.add(switchModeButton); //TODO Déplacer peut être ça
    }

//////////////////////////////////////////////GETTER////////////////////////////////////////////////////////////////////

    @Override
    public JButton getBackButton() {
        return super.getBackButton();
    }

    @Override
    public JButton getLaunchGameButton() {
        return super.getLaunchGameButton();
    }
    
    public JButton getSwitchModeButton() {
        return switchModeButton;
    }

    @Override
    public JButton getPauseGameButton() {
        return super.getPauseGameButton();
    }
    public JPanel getRightPanel() {
        return rightPanel;
    }

    @Override
    public JButton getRestartButton() {
        return super.getRestartButton();
    }

    @Override
    public JButton getPlayState() {
        return super.getPlayState();
    }

    public CellType getSelectedCell() {
        return selected;
    }

    public boolean getDeleteCell() {
        return deleteCell;
    }

    public boolean getCanRotate() {
        return canRotate;
    }
    public boolean getChangeInter() { return changeInter; }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getLoadButton() {
        return loadButton;
    }
    public JPanel getCellSelector() {
        return cellSelector;
    }
    public boolean getSelectMode() { return selectMode; }

///////////////////////////////////////////////////////SETTER///////////////////////////////////////////////////////////
///
    public void setSelectedCell(CellType type) {
        selected = type;
    }
    public void setCellSelectorListener(ActionListener listener) {
        this.cellSelectorListener = listener;
    }
    public void setDeleteCell(boolean deleteCell) {
        this.deleteCell = deleteCell;
    }
    public void setCanRotate(boolean canRotate) {
        this.canRotate = canRotate;
    }
    public void setChangeInter(boolean changeInter) { this.changeInter = changeInter; }


    @Override
    public void updateLayout() {
        super.updateLayout();
        if (botPanel != null) {
            rightPanel.setBounds(getWidth() - 200, getHeight()-75-(BUTTON_HEIGHT*4 + 30), 200, getHeight());
            botPanel.setBounds(0, getHeight()-75, getWidth(), 75);
        }
    }

    /**
     * Méthode permettant de redimensionner une image pour la liste de cellule
     * @param image image à redimensionner
     * @return ImageIcon redimensionnée
     */
    public ImageIcon resizeImage(ImageIcon image) {
        Image img = image.getImage();
        Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        return new ImageIcon(scaledImg);
    }
}
