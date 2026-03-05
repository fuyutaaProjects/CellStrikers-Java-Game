package src.main.java.com.views;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;

import src.main.java.com.models.Direction;

/**
 * La classe ImageLoader contient des méthodes permettant de charger des images et de les tourner
 * 
 * <h4>Voici les principales méthodes de la classe: </h4>
 * 
 * <ul>
 * <li><strong>BufferedImage getTurnedImage(Direction dir, Image img)</strong> Méthode permettant de retourner une image</li>
 * <li><strong>ImageIcon loadImage(String imagePath)</strong> Méthode permettant à partir d'un chemin vers une image donné 
 * de renvoyer l'ImageIcon correspondant</li>
 * <li><strong>ImageIcon resize(ImageIcon imageIcon, int width, int height)</strong> Méthode permettant de redimensionner une image</li>
 * </ul>
 * 
 * @author Elias dy
 * @version 1.1
 */
public class ImageLoader {

    /**
     * Méthode permettant de retourner une image
     * @param dir la direction dans laquelle on veut que l'image soit tournée
     * @param img l'image à tourner
     * @return l'image retournée
     */
    public static BufferedImage getTurnedImage(Direction dir, Image img){

        int widthImg = img.getWidth(null);
        int heightImg = img.getHeight(null);

        int angle = switch (dir){
            case N -> 0;
            case S -> 180;
            case E -> 90;
            case W -> 270;
        };
        double radians = Math.toRadians(angle);
        AffineTransform transform = AffineTransform.getRotateInstance(radians, widthImg / 2.0, heightImg / 2.0);
        BufferedImage rotatedImage = new BufferedImage(widthImg, heightImg, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotatedImage.createGraphics();

        g2d.drawImage(img, transform, null);
        g2d.dispose();

        return rotatedImage;
    }
    /**
     * Méthode permettant à partir d'un chemin vers une image donné 
     * de renvoyer l'ImageIcon correspondant
     * @param imagePath chemin vers l'image
     * @return null si le chemin est inavlide, l'imageIcon sinon
     */
    public static ImageIcon loadImage(String imagePath){
        URL imageUrl = new ImageLoader().getClass().getClassLoader().getResource(imagePath);
        
        if (imageUrl != null){
            return new ImageIcon(imageUrl);
        }else{
            System.out.println("Image not found at:" + imagePath);
            return null;
        }
    }
    /**
     * Méthode permettant de redimensionner une image
     * @param imageIcon l'image à redimensionner
     * @param width la nouvelle largeur de l'image
     * @param height la nouvelle hauteur de l'image
     * @return l'image redimensionné
     */
    public static ImageIcon resize(ImageIcon imageIcon, int width, int height){
        Image image = imageIcon.getImage();
        Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }
}
