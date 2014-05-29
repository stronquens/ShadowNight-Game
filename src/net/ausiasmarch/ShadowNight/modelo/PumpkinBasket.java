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
 * @author Armando Maya y Jose M Coronado
 */
public class PumpkinBasket extends Actor {

    /* Ultimo shadow colisionado */
    protected Shadow lastBasketCollided;
    /* True si puede disparar */
    protected boolean deadBasket;
    /* Numero de gritones que reabastece */
    private int score;

    /* Lista de nombres de explosion */
    protected List<String> explosionNames;
    /*El numero de veces que esta cayendo*/
    private int fallen = 0;

    public PumpkinBasket(GameWindow gw, List<String> names) {
        super(names);
        window = gw;
        frameDuration = DURATION_FRAMES_SHADOW;// Duracion frame de la calabaza
        score = 8;
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

        // Si la calabaza fue eliminada y estamos en el ultimo fotograma, se desintegra
        if (deadBasket == true && getFrameNumber() == getSpriteImages().size() - 1) {
            // La añadimos a la lista de eliminados
            window.addRemove(this);
            // La calabaza ha sido eliminada
            deadBasket = false;
        }

        // Mueve esta calabaza
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
     * Detecta la colision de esta calabaza con otro actor
     * @param actor
     */
    public void collision(Actor actor) {
        if (actor instanceof Player || actor instanceof House) {
            if (!deadBasket) {
                if (actor instanceof Player) {
                    // Aumentamos la puntuacion del jugador
                    window.getPlayer().setClusterBombs(score);
                    deadBasket = true;
                    WavPlayer.stop(window.GRITO_GRAVE);
                    WavPlayer.play(window.YEAH);
                }
                if (actor instanceof House) {
                    deadBasket = true;
                    WavPlayer.stop(window.GRITO_GRAVE);
                    WavPlayer.play(window.OOOO);
                }

            }

        }
        if (actor instanceof Missile || actor instanceof CandyScreamer) {
            // Si la calabaza colisiona la primera vez con el misil
            if (fallen == 0) {
                window.addRemove(actor);
                setSpriteImages(ImageUtils.getImagesNames(window.CESTA_CALABAZA, 1));
                WavPlayer.play(window.GRITO_GRAVE);
                setFrameDuration(1);
                // Situamos bien la nueva imagen
                setX(this.getX() + 50);
                setY(this.getY() + 100);
                // Solo cae verticalmente
                setVx(0);
                setVy(200);
                // Aumentamos el numero de veces q ha colisionado con el misil
                fallen++;
            }
        }

    }
}
