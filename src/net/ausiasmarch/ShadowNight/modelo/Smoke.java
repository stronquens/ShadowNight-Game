package net.ausiasmarch.ShadowNight.modelo;

import java.util.List;

/**
 *
 * @author Armando
 */
public class Smoke extends Actor {

    public Smoke(GameWindow gw, List<String> names) {
        super(names);
        translucent = 0.5f;
    }

    @Override
    public void collision(Actor actor) {

    }
}
