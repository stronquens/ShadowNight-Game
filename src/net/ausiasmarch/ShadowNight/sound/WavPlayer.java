package net.ausiasmarch.ShadowNight.sound;

/*
 * --------------------------------------------------------------------------
 * Curso Básico de desarrollo de VideoJuegos en Java 2D
 * WavPlayer.java
 * Carga y reproduce archivos wav.
 *
 * @author Luis Mateo
 * --------------------------------------------------------------------------
 */
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WavPlayer {
    // El unico objeto SpriteCache del juego
    private static WavPlayer single = new WavPlayer();
    // Map para el almacenamiento de efectos de sonido
    private static Map<String, AudioClip> sound = new HashMap<>();
    // El audio esta reproduciendose
    private static boolean isPlay = false;
    // Nombre del fichero de audio
    private static String fileName;


    /**
     * Carga un recurso de tipo AudioClip
     */
    protected static Object loadResourceFile(String file) {
        URL url;
        url = WavPlayer.class.getClassLoader().getResource(file);
        return Applet.newAudioClip(url);
    }

    /**
     * Obtiene el nombre del fichero de audio a partir de la ruta completa
     *
     * @param path
     * @return
     */
    private static String getFileName(String path) {
        fileName = path;  // Referencia al fichero de sonido            

        // Obtiene la posicion de la ultima / de la ruta
        int n = path.lastIndexOf("/");

        // Si n es distinto de cero, se encontro la /
        if (n != 0) {
            // Obtiene el nombre del fichero de sonido (sin la ruta)
            fileName = path.substring(n + 1);
        }

        return fileName;
    }

    public static AudioClip getAudioClip(String ref) {
        try {
            // Obtenemos el nombre del archivo de audio (sin la ruta) 
            fileName = getFileName(ref);
            
            // Si el AudioClip con ese nombre esta almacenado en el Map...
            if (sound.get(fileName) != null) {
                return sound.get(fileName);  // la devolvemos
            }

            AudioClip audioClip = (AudioClip) loadResourceFile(ref);
              
            // Si no se encontro el archivo de audio con esa referencia
            if (audioClip == null) {
                throw new RuntimeException("No se encontro el archivo " + ref);
            }

            // En caso contrario, lo guardamos en el map
            sound.put(fileName, audioClip);
            // Devolvemos el AudioClip
            return audioClip;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Reproduce un sonido una vez
     *
     * @param name
     */
    public static void play(final String name) {
        final AudioClip audio =  getAudioClip(name);
        if (audio == null){
            return;
        } 
         new Thread(
                new Runnable() {
            @Override
            public void run() {
                audio.play();
            }
        }).start();
        isPlay = true; 
    }

    /**
     * Reproduce un sonido en un bucle
     *
     * @param name
     */
    public static void loop(final String name) {
        isPlay = true; 
        final AudioClip audio =  getAudioClip(name);
        if (audio == null){
            return;
        } 
        new Thread(
                new Runnable() {
            @Override
            public void run() {
                audio.loop();
            }
        }).start();
    }

    /**
     * Detiene un sonido en un bucle
     *
     * @param name
     */
    public static void stop(final String name) {
        new Thread(
                new Runnable() {
            @Override
            public void run() {
                getAudioClip(name).stop();
            }
        }).start();
        isPlay = false;
    }

   
    /**
     * @return the isPlay
     */
    public static boolean isPlay() {
        return isPlay;
    }

}
