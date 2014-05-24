package net.ausiasmarch.ShadowNight.modelo;

import java.awt.Rectangle;
import java.util.List;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.*;

/*
 * -------------------------------------------------------------------------
 * Curso Básico de desarrollo de VideoJuegos en Java 2D
 * Bomb.java
 * Bomba de fragmentación
 *
 * @author Luis
 *--------------------------------------------------------------------------
 */
public class CandyScreamer extends Actor {

    public CandyScreamer(GameWindow gw, Rectangle stage, float x, float y,
            ShotDirection dir, List<String> names) {
        super(names);
        window = gw;
        this.x = x;
        this.y = y;
        this.stage = stage;

        switch (dir) {
            case UL:
                vx = -BOMB_SPEED;
                vy = -BOMB_SPEED;
                break;
            case U:
                vx = 0;
                vy = -BOMB_SPEED;
                break;
            case UR:
                vx = BOMB_SPEED;
                vy = -BOMB_SPEED;
                break;
        }

    }

    @Override
    public void act(long dt) {
        // Mueve esta bomba
        super.move(dt);

        if (x < stage.x || x > stage.width
                || y < stage.y || y > stage.height) {
            window.addRemove(this); // Añade la bomba a la lista de actores a borrar   
        }

    }

    @Override
    public void collision(Actor actor) {

    }

}
