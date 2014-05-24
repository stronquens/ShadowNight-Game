package net.ausiasmarch.ShadowNight.modelo;

import java.util.List;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.EXPLOSION_A;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.EXPLOSION_A_FRAMES;
import static net.ausiasmarch.ShadowNight.modelo.Player.MAX_SHIELDS;
import net.ausiasmarch.ShadowNight.util.ImageUtils;

/**
 * -------------------------------------------------------------------------
 * Curso Básico de desarrollo de VideoJuegos en Java 2D
 * El pueblo
 *
 * @author Armando Maya y Jose M Coronado
 * -------------------------------------------------------------------------
 */
public class House extends Actor {
    /* Constructor */

    public static final int MAX_SHIELDS = 500;
    private int shields;
    private List<String> explosionNames;

    public House(GameWindow gw, List<String> names) {
        super(names);
        window = gw;
        shields = MAX_SHIELDS;
        explosionNames = ImageUtils.getImagesNames(EXPLOSION_A, EXPLOSION_A_FRAMES);
    }

    @Override
    public void act(long dt) {
    }

    public int getShields() {
        return shields;
    }

    /**
     * Establece los escudos
     *
     * @param shields
     */
    public void setShields(int shields) {
        this.shields = shields;
    }

    public void addShields(int i) {
        shields += i;
        if (shields > MAX_SHIELDS) {
            shields = MAX_SHIELDS;
        }
    }

    public void removeShields(int i) {
        shields -= i;
        if (shields < 0) {
            shields = 0;
        }
    }

    public void collision(Actor actor) {
        if (actor instanceof Shadow) {
            removeShields(GameWindow.SHADOW_LOST_SHIELDS); // Invasion completada 
        }

        // Si los escudos estan por debajo e cero
        if (getShields() == 0) {
            window.notifyHouseDeath();   // notifica la muerte del jugador
        }

    }
}
