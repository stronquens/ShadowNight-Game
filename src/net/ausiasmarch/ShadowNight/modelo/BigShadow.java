package net.ausiasmarch.ShadowNight.modelo;

import java.util.List;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.COLISION;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.ENGULLE;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.MASTICA;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.DURATION_FRAMES_SHADOW;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.EXPLOSION_B;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.EXPLOSION_B_FRAMES;
import net.ausiasmarch.ShadowNight.sound.WavPlayer;
import net.ausiasmarch.ShadowNight.util.ImageUtils;

/**
 *
 * @author Armando
 */
public class BigShadow extends Actor {

    /* Indica si el bigShadow esta muerto */
    protected boolean deadBigShadow;
    /* Daño que quita al pueblo */
    private int damage = 50;
    /* explosion creada */
    protected boolean explosion;
    /* Lista de nombres de explosion */
    protected List<String> explosionNames;
    //Numero de veces que hay que atacarle
    protected int life = 20;
    // Reduce las bombas a cero
    int bombs = 0;

    public BigShadow(GameWindow gw, List<String> names) {
        super(names);
        window = gw;
        frameDuration = DURATION_FRAMES_SHADOW;     // Duracion frame shadow
        deadBigShadow = false;          // No ha sido eliminado
        explosion = false;
        translucent = 0.7f; // Transparencia
        explosionNames = ImageUtils.getImagesNames(EXPLOSION_B, EXPLOSION_B_FRAMES);
    }

    /**
     * Establece la puntuacion para este shadow
     *
     * @param int
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Obtiene la puntuacion para este shadow
     *
     * @return int
     */
    public int getDamage() {
        return damage;
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
        }
        if (vx > 0 && x + width > (stage.x + stage.width)) {
            // choco con el lado derecho
            vx = -vx;
        }

        // Si el shadow fue eliminado y estamos en el ultimo fotograma, se desintegra
        if (deadBigShadow == true && getFrameNumber() == getSpriteImages().size() - 1) {
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
            // Aumentamos la puntuacion del jugador       
            window.getPlayer().addScore(damage);
            // El shadow ha sido eliminado
            deadBigShadow = false;
            life = 5;
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
            // Si el shadow no ha sido ya eliminado
            if (!deadBigShadow) {
                // Si shadow es tocado por un misil o un bomba 
                if (actor instanceof Missile) {
                    playSound(COLISION);
                    window.addRemove(actor);
                    life = life - 1;
                    explosion = true;
                    //si la vida llega a 0 el BigShadow muere
                    if (life == 0) {
                        // El BigShadow muere
                        deadBigShadow = true;
                    }
                }
                // Si el BigShadow colisiona con un griton
                if (actor instanceof CandyScreamer) {
                    WavPlayer.play(ENGULLE);
                    // Se come el griton
                    window.addRemove(actor);
                }

                // Si actor es la nave del jugador
                if (actor instanceof House) {
                    // Disminuye la cantidad de shield de la nave 
                    window.getHouse().removeShields(damage);
                    WavPlayer.play(window.MASTICA);
                    window.getPlayer().setClusterBombs(bombs);
                    explosion = true;
                    deadBigShadow = true;
                }
            }
        }
    }
}
