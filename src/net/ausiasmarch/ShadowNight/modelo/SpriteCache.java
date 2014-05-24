package net.ausiasmarch.ShadowNight.modelo;

import net.ausiasmarch.ShadowNight.util.ImageUtils;

/**
 * ---------------------------------------------------------------------------
 * Curso Básico de desarrollo de VideoJuegos en Java 2D SpriteCache.java
 * Gestiona la carga y el almacenamiento de sprites en un HashMap
 *
 * @author Armando Maya y Jose M Coronado
 * ----------------------------------------------------------------------------
 */
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class SpriteCache {
    // Map para el almacenamiento de sprites (imagenes)
    private Map<String, BufferedImage> sprites;
    // El unico objeto SpriteCache del juego
    private static SpriteCache single = new SpriteCache();

    // Constructor
    private SpriteCache() {
        sprites = new HashMap<>();    // Crea un HashMap de sprites
    }

    /**
     * Obtiene el unico SpriteCache usado en el juego
     *
     * @return SpriteCache
     */
    public static SpriteCache get() {
        return single;
    }
     
    /**
     * Obtiene un Sprite (una imagen) a partir del nombre completo (ruta
     * incluida) del fichero de imagen
     *
     * @param ref referencia al nombre del fichero (incluida la ruta)
     * @return
     */
    public BufferedImage getSprite(String ref) { 
        String fileName = ref;  // Referencia al fichero de imagen            

        // Obtiene la posicion de la ultima / de la ruta
        int n = ref.lastIndexOf("/");

        // Si n es distinto de cero, se encontro la /
        if (n != 0) {
            // Obtiene el nombre del fichero de imagen (sin la ruta)
            fileName = ref.substring(n + 1);
        }

        // Si una imagen  con ese nombre esta almacenado en el Map...
        if (sprites.get(fileName) != null) {
            return sprites.get(fileName);  // la devolvemos
        }

        // Si no, cargamos la imagen con la referencia (ruta) indicada 
        BufferedImage image = (BufferedImage) ImageUtils.loadImage(ref);

        // Si no se encontro la imagen con esa referencia
        if (image == null) {
            System.exit(0);             // cerramos la aplicación 
        }   
        
        sprites.put(fileName, image);   // Lo guardamos en el map

        return image;                   // devolvemos la imagen
    }

}
