package src.main.java.com.views.specialMode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import src.main.java.com.Game;
import src.main.java.com.Screen;
import src.main.java.com.models.CellType;
import src.main.java.com.models.Grid;

/**
 * Cette classe permet de représenter la fenêtre permettant à l'utilisateur de choisir quels règles de priorités il souhaite
 * pour le niveau qu'il veut jouer dans le mode special mode.
 * 
 * @author dy Elias
 * @version 1.4
 */
public class RuleSelectDialog extends JDialog{

    private int i = 0;
    private int index = 0;
    private CellType[] order = new CellType[Grid.cellNumber]; // l'ordre de priorité des cellules

    /**
     * Constructeur de la classe permettant d'initialiser les composants du JDialog
     * @param gameGrid Grid la grille du jeu
     */
    public RuleSelectDialog(Grid gameGrid){
        // Paramètre de la fenêtre:
        this.setSize(new Dimension(Screen.getScreenWidth()/2, Screen.getScreenHeight()/2));
        this.setTitle("Choose the priority order of the cells");
        this.setLocationRelativeTo(Game.window);
        this.setDefaultCloseOperation(0);
        this.setResizable(false);
        this.setModal(true);

        //------------------------------------------------------------------

        //Paramètre des panneaux:
        JPanel mainPanel = new JPanel(new BorderLayout(Screen.getSpriteSize()/8, Screen.getSpriteSize()/8));
        mainPanel.setBackground(Color.BLACK);
        this.add(mainPanel);

        JPanel titlePanel = new JPanel(new GridBagLayout()); //Pour le titre
        titlePanel.setBackground(new Color(17, 13, 44));

        JPanel choicePanel = new JPanel(new GridLayout(1,2)); //Pour ce qui concerne l'affichage des boutons et de l'ordre
        JPanel orderPanel = new JPanel(new GridLayout(order.length, 1)); //Pour ce qui affiche l'ordre des cellules
        JPanel buttonPanel = new JPanel(new GridLayout(Grid.cellNumber/2, Grid.cellNumber)); //Pour les boutons des cellules
        JPanel confirmPanel = new JPanel(); //Pour la confirmation

        //Fond des panneaux:
        choicePanel.setBackground(Color.BLACK);
        orderPanel.setBackground(Color.BLACK);
        buttonPanel.setBackground(Color.BLACK);
        confirmPanel.setBackground(new Color(17, 13, 44));

        //Initialisation des textes donnant des indications:
        JLabel lab = new JLabel("Choose the priority order of the cell");
        lab.setHorizontalAlignment((int) JDialog.CENTER_ALIGNMENT);
        lab.setForeground(Color.WHITE);

        JLabel help = new JLabel("Click on the buttons by your wanted order: ");
        help.setHorizontalAlignment((int) JDialog.CENTER_ALIGNMENT);
        help.setForeground(Color.WHITE);

        GridBagConstraints textConstraint = new GridBagConstraints(); //Contrainte pour les 2 textes
        textConstraint.weighty = 2;
        textConstraint.gridy = 1;
        textConstraint.insets = new Insets(1, 20, 5, 20);
        titlePanel.add(lab, textConstraint);
        textConstraint.gridy = 2;
        textConstraint.insets = new Insets(1, 20, Screen.getSpriteSize()/3, 20);
        titlePanel.add(help, textConstraint);

        JButton[] choices = new JButton[Grid.cellNumber];//Boutons des cellules


        //Ajout des boutons de cellules:
        for (int j = 0; j < Grid.cellNumber; j++) {
            choices[j] = new JButton(CellType.values()[j].toString().toLowerCase());
            styleCellButton(choices[j]);

            buttonPanel.add(choices[j]);
        }

        //Fonctionnement des boutons
        for (i = 0; i < choices.length; i++) {
            int indexCopy = i;
            choices[i].addActionListener(e -> {
                //Ajout de la cellule dans l'ordre et masquage du boutton
                order[index] = CellType.values()[indexCopy];
                choices[indexCopy].setVisible(false);
                index++;

                //Ajout du texte sur l'orderPanel:
                JLabel text = new JLabel("      " + index + ". " + choices[indexCopy].getText());
                text.setForeground(Color.WHITE);
                orderPanel.add(text);
            });
        }
        i = 0;

        choicePanel.add(buttonPanel);
        choicePanel.add(orderPanel);

        //Fonctionnement et ajout du bouton pour rénitialiser
        JButton renit = new JButton("Reinitialisation");
        renit.addActionListener(e -> {
            for (JButton choice : choices) {
                choice.setVisible(true);
            }
            orderPanel.removeAll();
            orderPanel.repaint();
            index = 0;
        });

        //Fonctionnement et ajout des deux boutons pour quitter
        JButton defaultChoice = new JButton("Default");
        defaultChoice.addActionListener(e -> {
            this.dispose();
            index = 0;
        });
        JButton finish = new JButton("Finish"); //Pour valider les changements

        finish.addActionListener(e -> {
            gameGrid.setOrder(order);
            this.dispose();
        });
        //Ajout du style:
        styleButton(defaultChoice);
        styleButton(finish);
        styleButton(renit);

        confirmPanel.add(renit);
        confirmPanel.add(defaultChoice);
        confirmPanel.add(finish);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(choicePanel, BorderLayout.CENTER);
        mainPanel.add(confirmPanel, BorderLayout.SOUTH);
    }
    /**
     * Méthode permettant de donner du style aux 3 bouttons du bas
     * @param button JButton le bouton qui va changer de style
     */
    private void styleButton(JButton button){

        button.setFont(new Font("Arial", 0, Screen.getSpriteSize()/4));
        button.setBackground(new Color(38, 21, 65));
        button.setForeground(Color.WHITE);

        button.setBorderPainted(false);
        button.setFocusPainted(false);
    }
    /**
     * Méthode permettant de donner du style aux boutons des cellules
     * @param button JButton le bouton de cellules qui va changer de style
     */
    private void styleCellButton(JButton button){

        button.setFont(new Font("Arial", 1, Screen.getSpriteSize()/5));
        button.setToolTipText(button.getText());
        button.setBackground(new Color(0, 26, 97));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(Screen.getSpriteSize(), Screen.getSpriteSize()/2));

        button.setBorderPainted(false);
        button.setFocusPainted(false);

        //Pour le hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 14, 51));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 26, 97));
            }
        });
    }
}
