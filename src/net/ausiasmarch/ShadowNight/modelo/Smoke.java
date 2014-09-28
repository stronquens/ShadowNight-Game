package net.ausiasmarch.ShadowNight.modelo;

import java.util.List;

/**
 * -------------------------------------------------------------------------
 * Curso Básico de desarrollo de VideoJuegos en Java 2D
 * Smoke.java
 * Humo de las casas
 *
 * @author Armando Maya y Jose M Coronado
 *--------------------------------------------------------------------------
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
