package src.main.java.com.views;

import java.awt.*;
import java.util.ArrayList;
import src.main.java.com.audio.Audio;
import src.main.java.com.models.*;

/**
 * Classe AnimaCell
 * Permet de gérer les animations des cellules sur la grille
 * @author Daniel dk
 */
public class AnimaCell{
    //Vu que les animations sont éxecutés après les changements sur grid, on stocke les valeurs des 
    private int x, y; //Coordonnées de base de la cellule
    private int midX, midY; //Coordonnées intermédiaires ou passera la cellule avant la fin (not in use)
    private int endX, endY; //Coordonnées de la cellule à la fin de l'animation
    private int xa, ya; //Coordonnées de la cellule sur l'écran en fonction de progress

    private Direction dir; //Direction de base de la cellule
    private Cell cell; //Cellule sur laquelle on applique l'animation (on prend sa texture et aussi certains de ses paramètres)
    private Cell gen_cell; //Cellule génératrice pour les animations de génération de cellule (not in use)

    public static int offsetX2;
    public static int offsetY2;

    private Texture texture; //Texture de la cellule (sert à y facilité l'accès)

    private double progress; //Entre 0.0 et 1.0 désignant la progression de l'animation
    private double speed = 1.0/40.0; //Nombre de frames d'animations (on prend le diviseur, soit 1.0/40.0 équivaut à 40 frames pour une animation)

    private ArrayList<String> tags = new ArrayList<>(); //Liste contenant les tags d'une animations
    //Explication des tags :
    //Chaque type d'animations a un ou plusieurs tags (dût au différent effet de plusieurs cellules)
    //Il y a un tag par "origine", par exemple le tag move indique qu'un mover est animé en train de bouger, ou push qui indique qu'une cellule est poussé
    //
    //move / gen / push / switch : mouvement de cellule
    //(move : avancé de mover; gen : génération de cellule; push : poussée d'une cellule; switch : intervertion de 2 cellules)
    //spin-right : rotation vers la droite
    //spin-left : rotation vers la gauche

    public static Audio SFX_sound = new Audio();
    static{
        SFX_sound.load("SFX_cell_1.wav");
    }

    //Constructeur universelle pour les merges d'animations
    public AnimaCell(ArrayList<String> tags, Cell cell, int startX, int startY, int midX, int midY, int endX, int endY, Direction dir, Cell gen_cell){
        this.cell = cell;
        this.x = startX;
        this.y = startY;
        this.midX = midX;
        this.midY = midY;
        this.endX = endX;
        this.endY = endY;
        this.dir = dir;
        this.gen_cell = gen_cell;
        this.tags = tags;
        this.texture = GridView.getCorrespondingTexture(cell);
    }

    //Constructeur pour les mouvements
    public AnimaCell(String tag, Cell cell, int startX, int startY, int endX, int endY){
        this.cell = cell;
        this.x = startX;
        this.y = startY;
        this.endX = endX;
        this.endY = endY;
        this.tags.add(tag);
        this.texture = GridView.getCorrespondingTexture(cell);
    }

    //Constructeur pour les spins
    public AnimaCell(String tag, Cell cell, int startX, int startY, Direction dir){
        this.cell = cell;
        this.x = startX;
        this.y = startY;
        this.endX = x;
        this.endY = y;
        this.dir = dir;
        this.tags.add(tag);
        this.texture = GridView.getCorrespondingTexture(cell);
    }
    //Constructeur pour les générateurs
    public AnimaCell(String tag, Cell gen_cell, Cell cell, int startX, int startY, int endX, int endY){
        this.cell = cell;
        this.gen_cell = gen_cell;
        this.x = startX;
        this.y = startY;
        this.endX = endX;
        this.endY = endY;
        this.tags.add(tag);
        this.texture = GridView.getCorrespondingTexture(cell);
    }


    //Constructeur pour les switcher
    //
    // Je le garde si jamais je voudrai améliorer les animations des spinners
    //
    public AnimaCell(String tag, Cell cell, int startX, int startY, int midX, int midY, int endX, int endY){
        this.cell = cell;
        this.x = startX;
        this.y = startY;
        this.midX = midX;
        this.midY = midY;
        this.endX = endX;
        this.endY = endY;
        this.tags.add(tag);
        this.texture = GridView.getCorrespondingTexture(cell);
    }

    /**
     * Insuffle la vie dans les animations
     * Fonction s'occupant des animations des cellules
     * @param g
     * @param cellsize
     * @param offsetX
     * @param offsetY
     */
    public void anima(Graphics g, int cellsize, int offsetX, int offsetY){
        xa = offsetX + (x-offsetX2)*cellsize;
        ya = offsetY + (y-offsetY2)*cellsize;
        //System.out.println("anima : " + x + "/" + y + " | " + endX + "/" + endY + " | " + tags + " | " + progress + " | " + speed + " | " + cell + " | " + cell.getCellType() + " (AnimaCell)");
        //System.out.println("anima : " + x + "/" + y + " | " + midX + "/" + midY + " | " + endX + "/" + endY + " | " + progress);
        double currentAngle = 0.0;
        //========================= Spin ============================
        //On calcule ici l'angle que l'on veux appliquer à la cellule avec progress qui permet de définir la "progression" entre l'état de base et l'état de fin
        if(isAnimaTag("spin-right")){
            currentAngle += dir.getAngleDroite() - dir.getAngleGaucheTo(cell.getDirection())*progress;
        }
        else if(isAnimaTag("spin-left")){
            currentAngle += dir.getAngleGauche() - dir.getAngleDroiteTo(cell.getDirection())*progress;
        }
        else currentAngle = cell.getDirection().getAngleDroite();
        //========================= Move ============================
        //Ici on met à jour les coordonnées sur l'écran (et non la grid) de l'image et on met à jour en fonction de la "progression"
        if(isAnimaTag("move") || isAnimaTag("gen") || isAnimaTag("push") || isAnimaTag("switch")){
            xa = (xa+(int)((endX - x)*cellsize*progress));
            ya = (ya+(int)((endY - y)*cellsize*progress));
            //SFX Part
            if(progress == 0.0){
                Audio.play("SFX_cell_1.wav");
            }
        }
        //========================= Draw ============================
        //Ici on fait le rendue de la cellule en prenant compte des mouvements et rotations possibles
        Graphics2D g2d = (Graphics2D) g.create();
        double angle = Math.toRadians(currentAngle);
        g2d.rotate(angle, xa + cellsize / 2, ya + cellsize / 2);

        g2d.drawImage(
            texture.getTexture().getImage(),
            xa-offsetX2*cellsize,
            ya-offsetY2*cellsize,
            cellsize,
            cellsize,
            null);
        g2d.rotate(-angle, xa + cellsize / 2, ya + cellsize / 2);
        progress += speed;
    }

    /**
     * Permet de savoir si le String tag passé en paramètre est un animaTag de l'animation sur laquelle il est éxecuté
     * @param tag String
     * @return boolean
     */
    public boolean isAnimaTag(String tag){
        for(int uwu = 0; uwu < tags.size(); uwu++){
            if(tags.get(uwu) == tag){
                return true;
            }
        }
        return false;
    }

    /**
     * Renvoie le nombre d'animatag que possède la cellule
     * @param tag
     * @return int
     */
    public int numAnimaTag(String tag){
        int owo = 0;
        for(int uwu = 0; uwu < tags.size(); uwu++){
            if(tags.get(uwu) == tag){
                owo++;
            }
        }
        return owo;
    }

    /**
     * Permet de fussioner 2 animations et de renvoyer le résultat, sert dans les cas où plusieurs évenements sont appliqués à une seule cellule
     * Par exemple si on a 2 animations pour une même cellule, l'une qui fait avance la cellule et l'autre qui la fait tourner, cela vas les regrouper pour en faire une seule
     * @param ani AnimaCell
     * @return AnimaCell
     */
    public AnimaCell mergeAnimaCell(AnimaCell ani){
        tags.addAll(ani.tags);
        int midX_ = x + (midX - x) + (ani.midX - ani.x);
        int midY_ = y + (midY - y) + (ani.midY - ani.y);
        int endX_ = x + (endX - x) + (ani.endX - ani.x);
        int endY_ = y + (endY - y) + (ani.endY - ani.y);
        if(ani.gen_cell != null){
            gen_cell = ani.gen_cell;
        }
        Direction dir_ = dir;
        if(dir_ == null){
            dir_ = ani.dir;
        }
        else if(ani.dir != null && dir_ != null && dir_ != ani.dir){
            double angle_sum = dir_.getAngleDroite() + dir_.getAngleDroiteTo(ani.dir);
            dir_ = Direction.angleToDirection(angle_sum);
        }
        return new AnimaCell(tags, cell, x, y,midX_, midY_, endX_, endY_, dir_, gen_cell);
    }

    /**
     * Permet de rajouter une animation à une liste d'animations en mergeant toutes animations qui s'éxecute sur une même cellule (à voir la fonction mergeAnimaCell pour les merges)
     */
    public static void addToAnimaList(ArrayList<AnimaCell> animaList, AnimaCell ani){
        for(int uwu = 0; uwu < animaList.size(); uwu++){
            AnimaCell aniL = animaList.get(uwu);
            if(aniL.getCell() == ani.getCell() || (aniL.x == ani.x && aniL.y == ani.y && aniL.cell.getCellType() == ani.cell.getCellType())){
                AnimaCell animaMerged = aniL.mergeAnimaCell(ani);
                animaList.set(uwu, animaMerged);
                return;
            }
        }
        animaList.add(ani);
    }

    /**
     * Renvoie la coordonnée x courrante sur l'écran
     * @return int
     */
    public int getCurrentX(){
        return (int) ((1 - progress) * x + progress * endX);
    }

    /**
     * Renvoie la coordonnée y courrante sur l'écran
     * @return int
     */
    public int getCurrentY(){
        return (int) ((1 - progress) * y + progress * endY);
    }

    /**
     * Renvoie la cellule enregistré dans l'animation
     * @return Cell
     */
    public Cell getCell(){
        return cell;
    }

    /**
     * Renvoie la progression de l'animation (entre 0.0 et 1.0)
     * @return double
     */
    public double getProgress(){
        return progress;
    }

    /**
     * Renvoie la liste des animaTag de la cellule
     * @return ArrayList<String>
     */
    public ArrayList<String> getTags(){
        return tags;
    }
}