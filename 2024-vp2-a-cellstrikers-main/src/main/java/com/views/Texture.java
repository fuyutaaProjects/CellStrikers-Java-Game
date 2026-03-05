package src.main.java.com.views;

import javax.swing.ImageIcon;

import src.main.java.com.models.CellType;
import src.main.java.com.models.Direction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * La classe Texture permet de charger et manipuler des textures pour le jeu.
 * Elle gère les textures des types de cellule et permet de les afficher dans différentes directions.
 */
public class Texture{
    private CellType cell_type;
    private Direction direction;
    private String small;
    private String[] big;
    private String[][] big_list;
    private String cell;
    private ImageIcon texture;


    public Texture(String name, CellType cell_type, Direction direction, String small_texture, String[] big_texture){
        this.cell = name;
        this.cell_type = cell_type;
        this.direction = direction;
        this.small = small_texture;
        this.big = big_texture;
        this.texture = loadImage();
    }

    public Texture(String name, CellType cell_type, Direction direction, String small_texture, String[][] big_textures){
        this.cell = name;
        this.cell_type = cell_type;
        this.direction = direction;
        this.small = small_texture;
        this.big_list = big_textures;
        this.texture = loadImage();
    }

    /**
     * Permet de charger une image dans les fichiers ressources du jeu.
     *
     * @return ImageIcon
     */
    private ImageIcon loadImage(){

        if(cell_type == null){
            BufferedImage emptyImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
            return new ImageIcon(emptyImage);
        }
        return ImageLoader.loadImage("src/resources/texture/" + cell + ".png");
    }

////////////////////////////////////////////////////GETTER//////////////////////////////////////////////////////////////

    public Object cell_type(){return cell_type;}
    public Direction dir(){return direction;}
    public String smallTexture(){return small;}
    public ImageIcon getTexture(){return texture;}

    /**
     * Permet de faire tourner une image pour l'adapter à sa direction.
     *
     * @param dir Direction
     * @return ImageIcon
     */
    public BufferedImage getTurnedImage(Direction dir){
        return ImageLoader.getTurnedImage(dir, getTexture().getImage());
    }
    /**
     * Renvoie une texture ASCII plus grosse.
     *
     * @return String[]
     */
    public String[] bigTexture(){
        if(big != null){
            return big;
        }
        else if(big_list != null){
            Random r = new Random();
            return big_list[r.nextInt(big_list.length)];
        }
        return null;
    }
}