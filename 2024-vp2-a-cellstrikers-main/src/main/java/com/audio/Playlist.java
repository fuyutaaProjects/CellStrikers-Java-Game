package src.main.java.com.audio;

import java.util.Random;

/**
 * Classe qui permet de gérer une playlist audio.
 * Elle permet de charger des fichiers audio, de les jouer, de régler le volume
 * et de jouer les fichiers audio dans un ordre aléatoire ou non.
 *
 * @author Daniel dk
 */
public class Playlist {
    public static boolean playListPlaying = false;
    public Audio[] playlist;
    public float volume;

    /**
     * Constructeur de la classe Playlist.
     *
     * @param playlist tableau de son audio
     * @param volume volume de la playlist
     */
    public Playlist(Audio[] playlist, float volume) {
        this.playlist = playlist;
        this.volume = Math.max(0.0f, Math.min(volume, 1.0f));
        applyVolumeToAll();
    }

    /**
     * Setter pour le volume de la playlist.
     *
     * @param volume volume de la playlist
     */
    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(volume, 1.0f));
        applyVolumeToAll();
    }

    /**
     * Applique le volume à tous les fichiers audio de la playlist.
     */
    private void applyVolumeToAll() {
        for (Audio a : playlist) {
            a.setVolume(this.volume);
        }
    }

    /**
     * Charge une playlist à partir d'un tableau de chemins de fichiers audio.
     *
     * @param pathList tableau de chemins de fichiers audio
     * @return tableau d'objets Audio
     */
    public static Audio[] loadPlayList(String[] pathList) {
        Audio[] playlist = new Audio[pathList.length];
        for (int uwu = 0; uwu < pathList.length; uwu++) {
            playlist[uwu] = new Audio();
            playlist[uwu].load(pathList[uwu]);
        }
        return playlist;
    }

    /**
     * Joue la playlist.
     * @param random true pour jouer la playlist dans un ordre aléatoire, false pour jouer dans l'ordre
     */
    public void play(boolean random) {
        playListPlaying = true;

        new Thread(() -> {
            try {
                Thread.sleep(500); // Small delay before starting
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Random r = new Random();
            int lastPlayedIndex = -1;

            while (playListPlaying) {
                if (random) {
                    boolean[] played = new boolean[playlist.length];
                    int countPlayed = 0;

                    while (countPlayed < playlist.length && playListPlaying) {
                        int rand;
                        do {
                            rand = r.nextInt(playlist.length);
                        } while (played[rand] || rand == lastPlayedIndex);

                        played[rand] = true;
                        countPlayed++;
                        lastPlayedIndex = rand;

                        playlist[rand].setVolume(volume);
                        playlist[rand].play();

                        while (playlist[rand].isplaying && playListPlaying) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    for (int uwu = 0; uwu < playlist.length && playListPlaying; uwu++) {
                        playlist[uwu].setVolume(volume);
                        playlist[uwu].play();

                        while (playlist[uwu].isplaying && playListPlaying) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start(); // Runs in a new thread
    }
}
