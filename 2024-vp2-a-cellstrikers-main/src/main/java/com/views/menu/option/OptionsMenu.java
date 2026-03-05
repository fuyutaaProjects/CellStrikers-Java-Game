package src.main.java.com.views.menu.option;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import src.main.java.com.Game;
import src.main.java.com.views.effects.RetroGlassPane;
import src.main.java.com.views.menu.AbstractMenuView;
import src.main.java.com.views.menu.CardNameMap;
import src.main.java.com.views.utilities.BuildSliderPanel;
import src.main.java.com.views.utilities.StyleButton;

/**
 *
 * Classe représentant le menu d'options du jeu.
 * Elle hérite de la classe AbstractMenuView et utilise un GridBagLayout.
 * Elle permet à l'utilisateur de configurer les options du jeu, telles que la taille de l'écran, la taille de la grille
 * les volumes audio et l'effet rétro.
 * @author Ethan dy
 */
public class OptionsMenu extends AbstractMenuView {
    public OptionsMenu() {
        super("OPTIONS_MENU");
    }

    @Override
    protected void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        int gridYCounter = 1;

        // Bouton du menu des resolutions
        JButton optionsMenuScreenSizeButton = new JButton("Screen Size");
        StyleButton.styleButton(optionsMenuScreenSizeButton, 250, 50);
        optionsMenuScreenSizeButton.addActionListener(e -> CardNameMap.showCard(CardNameMap.OPTIONS_MENU_SCREEN_SIZE.getLabel()));
        gbc.gridy = gridYCounter++;
        panel.add(optionsMenuScreenSizeButton, gbc);

        // Bouton du menu de la taille de la grille
        JButton optionsMenuGridSizeButton = new JButton("Grid Size");
        StyleButton.styleButton(optionsMenuGridSizeButton, 250, 50);
        optionsMenuGridSizeButton.addActionListener(e -> CardNameMap.showCard(CardNameMap.OPTIONS_MENU_GRIDSIZE.getLabel()));
        gbc.gridy = gridYCounter++;
        panel.add(optionsMenuGridSizeButton, gbc);

        //Master Volume Slider
        int currentMasterVolume = Math.round(Game.master_volume * 100);
        JSlider masterVolumeSlider = new JSlider(0, 100, currentMasterVolume);
        JPanel masterVolumePanel = BuildSliderPanel.buildSliderPanel(masterVolumeSlider, "Master Volume", currentMasterVolume, 250, 50);
        gbc.gridy = gridYCounter++;
        panel.add(masterVolumePanel, gbc);

        masterVolumeSlider.addChangeListener(e -> {
            Game.master_volume = masterVolumeSlider.getValue() / 100f;
            Game.setBgVolume();
        });

        panel.add(masterVolumePanel, gbc);

        //Music Volume Slider
        int currentMusicVolume = Math.round(Game.bg_volume * 100);
        JSlider musicVolumeSlider = new JSlider(0, 100, currentMusicVolume);
        JPanel musicVolumePanel = BuildSliderPanel.buildSliderPanel(musicVolumeSlider, "Music Volume", currentMusicVolume, 250, 50);
        gbc.gridy = gridYCounter++;
        panel.add(musicVolumePanel, gbc);

        musicVolumeSlider.addChangeListener(e -> {
            Game.bg_volume = musicVolumeSlider.getValue() / 100f;
            Game.setBgVolume();
        });

        panel.add(musicVolumePanel, gbc);

        //SFX Volume Slider
        int currentSFXVolume = Math.round(Game.sfx_volume * 100);
        JSlider sfxVolumeSlider = new JSlider(0, 100, currentSFXVolume);
        JPanel sfxVolumePanel = BuildSliderPanel.buildSliderPanel(sfxVolumeSlider, "SFX Volume", currentSFXVolume, 250, 50);
        gbc.gridy = gridYCounter++;
        panel.add(sfxVolumePanel, gbc);

        sfxVolumeSlider.addChangeListener(e -> {
            Game.sfx_volume = sfxVolumeSlider.getValue() / 100f;
            Game.setSFXVolume();
        });

        panel.add(sfxVolumePanel, gbc);


        // Bouton pour activer ou désactiver le retro effect (crt)
        JButton retroToggleButton = new JButton("Retro Effect: " + (Game.retroEffect ? "ON" : "OFF"));
        StyleButton.styleButton(retroToggleButton, 250, 50);
        retroToggleButton.addActionListener(e -> {
            Game.retroEffect = !Game.retroEffect;
            retroToggleButton.setText("Retro Effect: " + (Game.retroEffect ? "ON" : "OFF"));
            Game.updateRetroEffect();
        });
        gbc.gridy = gridYCounter++;
        panel.add(retroToggleButton, gbc);


        int currentIntensity = Math.round(RetroGlassPane.retroEffectIntensity * 100);
        JSlider retroIntensitySlider = new JSlider(0, 100, currentIntensity);
        JPanel retroIntensityPanel = BuildSliderPanel.buildSliderPanel(retroIntensitySlider, "Retro Intensity", currentIntensity, 250, 50);
        retroIntensitySlider.addChangeListener(e -> {
            RetroGlassPane.retroEffectIntensity = retroIntensitySlider.getValue() / 100f;
            Game.updateRetroEffect();
        });

        gbc.gridy = gridYCounter++;
        panel.add(retroIntensityPanel, gbc);

        // Bouton pour retourner au menu principal
        JButton backButton = new JButton("Back");
        StyleButton.styleButton(backButton, 250, 50);
        backButton.addActionListener(e -> CardNameMap.showCard(CardNameMap.MAIN_MENU.getLabel()));
        gbc.gridy = gridYCounter++;
        panel.add(backButton, gbc);
    }

    @Override
    protected String getTitle() {
        return "Options";
    }

    @Override
    protected JPanel createPanel() {
        return new BackgroundPanel();
    }

    @Override
    protected LayoutManager getLayout() {
        return new GridBagLayout();
    }

}
