package net.ausiasmarch.ShadowNight.modelo;

import java.util.List;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.COLISION;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.DURATION_FRAMES_SHADOW;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.EXPLOSION_B;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.EXPLOSION_B_FRAMES;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.PLAYER;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.PLAYER_FRAMES;
import net.ausiasmarch.ShadowNight.sound.WavPlayer;
import net.ausiasmarch.ShadowNight.util.ImageUtils;

/**
 *
 * @author al038513
 */
public class PumpkinBasket extends Actor {

    /* Ultimo shadow colisionado */
    protected Shadow lastBasketCollided;
    /* True si puede disparar */
    protected boolean deadBasket;
    /* Puntuacion de este shadow */
    private int score;

    /* Lista de nombres de explosion */
    protected List<String> explosionNames;
    /*El numero de veces que esta cayendo*/
    private int fallen = 0;

    public PumpkinBasket(GameWindow gw, List<String> names) {
        super(names);
        window = gw;
        frameDuration = DURATION_FRAMES_SHADOW;     // Duracion frame shadow
        score = 8;
        //lastBasketCollided = null;   // Aun no ha colisionado con otro shadow
        deadBasket = false;          // No ha sido eliminado
        explosionNames = ImageUtils.getImagesNames(EXPLOSION_B, EXPLOSION_B_FRAMES);
    }

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
        if (deadBasket == true && getFrameNumber() == getSpriteImages().size() - 1) {
            // Lo añadimos a la lista de eliminados
            window.addRemove(this);
            // El shadow ha sido eliminado
            deadBasket = false;
        }

        // Mueve este shadow
        super.move(dt);
    }

    /**
     * Establece el numero de gritones que repone
     *
     * @param int
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Obtiene el numero de gritones que repone
     *
     * @return int
     */
    public int getScore() {
        return score;
    }

    @Override
    /*
     * Detecta la colision de este shadow con otro actor
     * @param actor
     */
    public void collision(Actor actor) {
        if (actor instanceof Player || actor instanceof House) {
            playSound(COLISION);
            if (!deadBasket) {
                if (actor instanceof Player) {
                    // Aumentamos la puntuacion del jugador
                    window.getPlayer().setClusterBombs(score);
                    deadBasket = true;
                    WavPlayer.stop(window.GRITO_GRAVE);
                }
                if (actor instanceof House) {
                    deadBasket = true;
                    WavPlayer.stop(window.GRITO_GRAVE);
                }

            }

        }
        if (actor instanceof Missile || actor instanceof CandyScreamer) {
            if (fallen == 0) {
                window.addRemove(actor);
                setSpriteImages(ImageUtils.getImagesNames(window.CESTA_CALABAZA, 1));              
                WavPlayer.play(window.GRITO_GRAVE);
                setFrameDuration(1);
                setX(this.getX() + 50);
                setY(this.getY() + 100);
                setVx(0);
                setVy(200);
                fallen++;
            }
        }

    }
}
