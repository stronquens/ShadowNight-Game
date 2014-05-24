package net.ausiasmarch.ShadowNight.modelo;
import java.util.List;

/**
 * -------------------------------------------------------------------------
 * Curso Básico de desarrollo de VideoJuegos en Java 2D
 * Explosion.java
 * Una explosion en el espacio
 *
 * @author Armando Maya y Jose M Coronado
 * ---------------------------------------------------------------------------
 */
public class Explosion extends Actor{
    public Explosion(GameWindow gw, List<String> names) {
        super(names);
        window = gw;
        setFrameDuration(GameWindow.DURATION_FRAMES_EXPLOSION);
        translucent = 0.65f;
    }

    @Override
    public void act(long dt) {     
        super.act(dt);           
        // Si estamos en el ultimo fotograma...
        if (getFrameNumber() == getSpriteNames().size() - 1){         
            window.addRemove(this);    // Añade la explosion a la lista de actores a borrar
        }       
    }
    
    @Override
    public void collision(Actor actor) {
    }
     
}
