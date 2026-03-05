package src.main.java.com.views.sandboxEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;

import src.libs.JStick;
import src.main.java.com.Screen;
import src.main.java.com.models.Cell;
import src.main.java.com.models.Grid;

/**
 * Classe qui étend LevelEditor, elle représente l'affichage du jeu
 * dans le mode sandBox
 * 
 * @author dy Elias
 * @version 1.6
 */
public class SandboxMod extends LevelEditor{

    private JStick joystick;
    private JLabel locationLabel; // Le Jlabel informant où est la caméra au niveau de la grille
    private int endy;

    /**
     * Constructeur de la classe qui initialise les principaux attributs de la classe
     * et initialise et affiche le joystick, le JLabel informant des coordonnées
     * et le JButton pour changer de mode
     * @param grid
     */
    public SandboxMod(Grid grid) {
        super(grid);
        Cell[][] cellGrid = grid.getGrid();

        this.setDisplayRange(cellGrid);
        
        // Initialisation du joystick : 
        this.joystick = new JStick();
        this.joystick.setPadColour(Color.BLUE);
        this.joystick.setStickColour(new Color(28, 28, 28));

        // Initialisation de locationLabel :
        locationLabel = new JLabel();
        locationLabel.setFont(new Font("arial", 0, cellSize/2));
        locationLabel.setForeground(Color.WHITE);
        this.setLocationLabel(this.getDisplayRange()[1], this.getDisplayRange()[0]);

        // dimension
        locationLabel.setSize(200, 200);

        updateLayout();
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                updateLayout();
            }
        });

        this.add(joystick);
        this.add(locationLabel);
    }

    public JStick getJoyStick() {
        return this.joystick;
    }

    /**
     * Méthode permettant de changer les coordonnées de la grille qui sont affichés
     * à l'écran à l'aide de locationLabel
     * @param x int 
     * @param y int
     */
    public void setLocationLabel(int x, int y){
        String text = "<html> Center Coordinates : <br>";
        text += "x : " + (x + endy/2) + " y : " + (y + endy/2) + "<br>";
        text += "Top-Left coordinates : <br>";
        text += "x : " + x + "  y : " + y + "<br>";
        text += " </html>";
        locationLabel.setText(text);
    }

    @Override
    public void setDisplayRange(Cell[][] cellGrid){
        //Pour changer le nombre de cellule qui sont affichés :
        int width = cellGrid[0].length;
        int height = cellGrid.length;

        // Les coordonnées ou l'affichage commence
        int starty = 0;
        int startx = 0;

        if(width > 15){
            startx = cellGrid[0].length/2;
        }if(height > 15){
            starty = cellGrid.length/2;
        }

        // Le nombre de cellules affichés en x et y
        int endx = Math.min(10, width - startx);
        this.endy = Math.min(10, height - starty);

        this.setDisplayRange(new int[]{
            starty, startx, starty + this.endy, startx + endx
        });
    }

    @Override
    public void updateLayout() {
        super.updateLayout();
        // Positionnement et dimensionnement
        if (joystick != null) {
            this.joystick.setPreferredSize(new Dimension(cellSize*2, cellSize*2));
            this.joystick.setBounds(50, Screen.getScreenHeight() - cellSize*2 - 150, cellSize*2, cellSize*2);
        }
        if (locationLabel != null) {
            locationLabel.setFont(new Font("arial", 0, cellSize/2));
            locationLabel.setLocation(cellSize*2 + 50, Screen.getScreenHeight() - cellSize*2 - 250);
        }
    }

}
