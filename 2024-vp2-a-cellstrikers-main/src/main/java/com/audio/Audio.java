package src.main.java.com.audio;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

/**
 * La classe Audio gère le chargement, la lecture, la pause, et la répétition des fichiers audio.
 * Elle utilise la bibliothèque Javax Sound pour manipuler les fichiers audio.
 * Elle permet également de régler le volume et de vérifier si l'audio est en cours de lecture.
 *
 * @author Daniel dk
 */
public class Audio {
    private static boolean audio_enabled = true; // true en cas normal, false si en phase de test
    private Clip audio; // L'objet Clip utilisé pour jouer l'audio
    public boolean isplaying; // Indique si l'audio est en cours de lecture
    private float volume = 0.5f;
    private FloatControl volumeControl = null;

    /**
     * Constructeur par défaut de la classe Audio.
     * Initialise l'objet audio et définit l'état de lecture sur faux.
     */
    public Audio() {
        audio = null;
        isplaying = false;
    }

    /**
     * Vérifie si l'audio est en cours de lecture.
     * 
     * @return true si l'audio est chargé, false si il est null.
     */
    public boolean audio(){
        if(audio == null){
            return false;
        }
        else return true;
    }

    /**
     * Vérifie si l'audio est en cours de lecture.
     * 
     * @return true si l'audio est en lecture, false sinon.
     */
    public boolean isplaying() {
        return isplaying;
    }

    /**
     * Charge le fichier audio spécifié dans l'objet Clip.
     * 
     * @param fileName le nom du fichier audio à charger (avec l'extension).
     */
    public void load(String fileName) {
        if (audio_enabled) {
            try {
                URL soundURL = getClass().getResource("/src/resources/soundtrack/" + fileName);
                if(soundURL == null){
                    return;
                }
                AudioInputStream audioIS = AudioSystem.getAudioInputStream(soundURL);
    
                if (AudioSystem.isLineSupported(new Line.Info(Clip.class))) {
                    audio = AudioSystem.getClip();
                    audio.open(audioIS);
                
                    // Get the volume control
                    if (audio.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                        volumeControl = (FloatControl) audio.getControl(FloatControl.Type.MASTER_GAIN);
                        applyVolume(); // Apply volume after loading
                    }
                } else {
                    System.out.println("No audio output available, disabling audio");
                    audio_enabled = false;
                }
            } catch (UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                System.out.println("Audio disabled");
                audio_enabled = false;
            }
        }
    }

    /**
     * Joue le Clip contenu dans l'instance d'Audio sur laquelle on éxecute cette fonction
     * A utiliser pour des musiques de fond ou des songs qui ne sont pas overlap
     * Pour des SFX, voir la version statique de la fonction play(String filepath)
     */
    public void play() {
        if (audio != null && audio_enabled) {
            audio.setFramePosition(0);
            audio.start();
            isplaying = true;

            //Listener pour détecter la fin d'un son
            audio.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    isplaying = false;
                }
            });
        }
    }

    /**
     * Joue un audio de façon statique en utilisant un chemin en String
     * A utilisez pour de préférence pour des sons courts qui sont répétées souvent, comme des SFX
     * Pour des musiques, voir la méthode play() de la classe Audio
     * @param filepath String
     */
    public static void play(String filepath) {
        Audio audio = new Audio();
        audio.load(filepath);
        audio.play();
    }

    /**
     * Met l'audio en boucle de manière continue.
     * Cette méthode nécessite que l'audio ait déjà été chargé.
     */
    public void loop() {
        if (audio != null && audio_enabled) {
            audio.loop(audio.LOOP_CONTINUOUSLY);
            isplaying = true;
        }
    }

    /**
     * Arrête temporairement l'audio.
     * L'audio peut être repris à partir de la position actuelle.
     */
    public void stop() {
        if (audio != null && audio.isRunning() && audio_enabled) {
            audio.stop();
        }
    }

    /**
     * Ferme l'audio et libère les ressources associées.
     * Après cet appel, l'audio ne pourra plus être utilisé sans le recharger.
     */
    public void close() {
        if (audio != null && audio_enabled) {
            audio.close();
        }
    }

    private void applyVolume(){
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float dB = (float) (Math.log10(Math.max(volume, 0.0001)) * 20);
            volumeControl.setValue(Math.max(min, Math.min(dB, max)));
        }
    }

    public void setVolume(float newVolume){
        this.volume = Math.max(0.0f, Math.min(newVolume, 1.0f));
        applyVolume();
    }

    public float getVolume(){
        return this.volume;
    }

    /**
     * Met à jour la valeur audio_diasbled_for_test, comme son nom l'indique
     * Cela sert seulement dans le cas des tests qui crash si audio est actif
     * La méthode est statique pour permettre de désactiver toutes instances d'audio en une seule ligne
     * 
     * @param bool un boolean, true dans le cas normal, false quand les tests javafx sont en cours
     */
    public static void set_audio_enabled(boolean bool){
        audio_enabled = bool;
    } 
}
