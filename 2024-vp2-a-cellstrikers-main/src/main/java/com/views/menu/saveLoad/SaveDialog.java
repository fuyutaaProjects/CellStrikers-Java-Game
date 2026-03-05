package src.main.java.com.views.menu.saveLoad;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import src.main.java.com.Game;
import src.main.java.com.Screen;
import src.main.java.com.level.LoaderJson;
import src.main.java.com.models.Grid;
import src.main.java.com.views.utilities.StyleButton;

/**
 * Le JDialog permettant la sauvegarde dans les modes sandbox et éditeur
 * 
 * @author dy Elias
 * @version 1.1
 */
public class SaveDialog extends JDialog{
    
    private boolean alreadyExist = false; // Informe de si le fichier existe déjà ou non
    private boolean blank = false; // Informe de si le JTextField a été laissé vide

    /**
     * Constructeur de la classe permettant d'afficher ses composants et de définir leur fonctionnement
     * @param grid Grid la grille à sauvegarder
     * @param savePath le chemin menant au répertoire ou mettre la sauvegarde
     */
    public SaveDialog(Grid grid, String savePath){
        this.setSize(new Dimension(Screen.getScreenWidth()/2, Screen.getScreenHeight()/2));
        this.setTitle("Save the actual grid");
        this.setLocationRelativeTo(Game.window);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setModal(true);

        // Création du panneau
        JPanel pan = new JPanel();
        this.add(pan);
        pan.setBackground(new Color(17, 13, 44));

        // Définition de la contrainte
        pan.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0.2;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(Screen.getSpriteSize()/2, 0, Screen.getSpriteSize()/2, 0);

        // Configuration et ajout du titre :
        JLabel title = new JLabel("Choose the name of the file :");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Verdana", 1, Screen.getTileSize()));

        pan.add(title, constraints);
        constraints.insets = new Insets(0, 0, Screen.getSpriteSize()/8,0);
        constraints.gridy++;
        constraints.gridwidth = 3;

        JPanel field = new JPanel(); // Pour le JTextField et le JLabel associe
        field.setBackground(new Color(17, 13, 44));
        // Configuration et ajout du JTextField :
        JTextField saveName = new JTextField(10);

        field.add(saveName);

        // Configuration et ajout du label informant de l'extension du fichier :
        JLabel extension = new JLabel(".json");
        extension.setForeground(Color.WHITE);
        field.add(extension);

        pan.add(field, constraints);
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        constraints.gridy ++;

        // Configuration et ajout du bouton d'annulation :
        JButton cancel = new JButton("Cancel");
        StyleButton.styleButton(cancel, Screen.getSpriteSize()*2, Screen.getSpriteSize()/2);
        cancel.setFont(new Font("Arial", 1, Screen.getSpriteSize()/3));
        pan.add(cancel, constraints);
        constraints.gridx++;

        cancel.addActionListener((e) -> {
            this.dispose();
        });

        // Configuration et ajout du bouton de sauvegarded :
        JButton save = new JButton("Save");
        StyleButton.styleButton(save, Screen.getSpriteSize()*2, Screen.getSpriteSize()/2);
        save.setFont(new Font("Arial", 1, Screen.getSpriteSize()/3));
        pan.add(save, constraints);
        constraints.gridy++;
        constraints.gridx = 0;

        // Message informant de si le fichier existe déjà
        JLabel message = new JLabel("<html>The file already exist. <br> if you re-pressed of save it will be rewrited</html>");
        message.setAlignmentX((int) JPanel.CENTER_ALIGNMENT);
        message.setForeground(Color.RED);
        pan.add(message, constraints);
        message.setVisible(alreadyExist);

        // Message d'erreur de si on a laissé le JTextField en blanc
        JLabel errorMessage = new JLabel("The file name can't be blank");
        errorMessage.setAlignmentX((int) JPanel.CENTER_ALIGNMENT);
        errorMessage.setForeground(Color.RED);
        pan.add(errorMessage, constraints);
        errorMessage.setVisible(blank);

        // Ajout du comportement du bouton de sauvegarde :
        save.addActionListener((e) -> {
            if(saveName.getText().equals("")){
                errorMessage.setVisible(true);
                repaint();
                blank = true;
            }else{
                String fileName = savePath + saveName.getText() + ".json";
            
                if(new File(fileName).exists() && !alreadyExist){
                    message.setVisible(true);
                    this.repaint();
                    alreadyExist = true;
                }else{
                    try{
                        alreadyExist = false;
                        LoaderJson.writeInFile(grid, fileName);
                        dispose();
                    }catch(IOException ex){
                        System.out.println(ex.getMessage());
                    }
                }
            }  
        });
    }
}
