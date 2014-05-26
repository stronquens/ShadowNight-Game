package net.ausiasmarch.ShadowNight.modelo;

import java.util.List;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.COLISION;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.DURATION_FRAMES_SHADOW;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.EXPLOSION_B;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.EXPLOSION_B_FRAMES;
import net.ausiasmarch.ShadowNight.sound.WavPlayer;
import net.ausiasmarch.ShadowNight.util.ImageUtils;

/**
 * ---------------------------------------------------------------------------
 * Curso Básico de desarrollo de VideoJuegos en Java 2D Shadow.java
 *
 * @author Armando Maya y Jose M Coronado
 * ----------------------------------------------------------------------------
 */
public class Shadow extends Actor {

    /* Ultimo shadow colisionado */
    protected Shadow lastShadowCollided;
    /* True si puede disparar */
    protected boolean deadShadow;
    /* Movimieento horizontal solamente */
    protected boolean horizontalMovement;
    /* Puntuacion de este shadow */
    private int score;
    /* explosion creada */
    protected boolean explosion;
    /* Lista de nombres de explosion */
    protected List<String> explosionNames;

    public Shadow(GameWindow gw, List<String> names) {
        super(names);
        window = gw;
        horizontalMovement = true; // Al inicio solo se mueven horizontalmente      
        frameDuration = DURATION_FRAMES_SHADOW;     // Duracion frame shadow
        translucent = 1.0f;
        lastShadowCollided = null;   // Aun no ha colisionado con otro shadow
        deadShadow = false;          // No ha sido eliminado
        horizontalMovement = true;  // Al inicio mueve horizontalmente
        explosion = false;

        explosionNames = ImageUtils.getImagesNames(EXPLOSION_B, EXPLOSION_B_FRAMES);
    }



    /**
     * Establece la puntuacion para este shadow
     *
     * @param int
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Obtiene la puntuacion para este shadow
     *
     * @return int
     */
    public int getScore() {
        return score;
    }

    /**
     * Define lo que sabe hacer
     */
    @Override
    public void act(long dt) {
        super.act(dt);

            if (vx < 0 && x < stage.x) {
                // choco con el lado izquierdo
                vx = -vx;
               // x = stage.x;
            }
            if (vx > 0 && x + width > (stage.x + stage.width)) {
                // choco con el lado derecho
                vx = -vx;
               // x = (stage.x + stage.width) - width;
            }

        // Si el shadow fue eliminado y estamos en el ultimo fotograma, se desintegra
        if (deadShadow == true && getFrameNumber() == getSpriteImages().size() - 1) {
            // Crea una explosion
            if (explosion) {
                Explosion explo = new Explosion(window, explosionNames);
                explo.setStage(stage);
                explo.setX(x);
                explo.setY(y);
                // Añade la explosion a la lista de actores
                window.addActor(explo);
            }
            // Lo añadimos a la lista de eliminados
            window.addRemove(this);
            // Notificamos su eliminacion   
            window.notifyShadowDeath(this);
            // Aumentamos la puntuacion del jugador       
            window.getPlayer().addScore(score);
            // El shadow ha sido eliminado
            deadShadow = false;
        }

        // Mueve este shadow
        super.move(dt);
    }



    @Override
    /*
     * Detecta la colision de este shadow con otro actor
     * @param actor
     */
    public void collision(Actor actor) {
        if (actor instanceof Missile || actor instanceof CandyScreamer || actor instanceof House) {
            playSound(COLISION);
            int value = getSpriteImages().size();
            int insert = 7;
            // Si el shadow no ha sido ya eliminado
            if (!deadShadow) {
                insertSprites(0, insert);
                updateSprites(2 * insert + 1, value + insert);
                // Si shadow es tocado por un misil o un bomba 
                if (actor instanceof Missile || actor instanceof CandyScreamer) {
                    // Añade el misil o la bomba a a lista de actores eliminados 
                    window.addRemove(actor);
                    explosion = true;
                }
                // Si actor es la nave del jugador
                if (actor instanceof House) {
                    // Disminuye la cantidad de shield de la nave 
                    window.getHouse().removeShields(score);
                    explosion = false;
                }
            }
            deadShadow = true;
        }
    }
}