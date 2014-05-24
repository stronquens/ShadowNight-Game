package net.ausiasmarch.ShadowNight.modelo;

import java.util.List;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.MISSILE_SPEED;

/** --------------------------------------------------------------------------
 * Curso Básico de desarrollo de VideoJuegos en Java 2D.
 * Un disparo de Caramelo
 *
 * @author Armando Maya y Jose M Coronado
 * --------------------------------------------------------------------------
 */
public class Missile extends Actor {
    
    /**
     * Constructor
     * @param gm
     * @param names 
     */
    public Missile(GameWindow gw, List<String> names) {
        super(names);
        window = gw;
    }

    @Override
    public void act(long dt) {
        super.act(dt);       
        // Mueve este misil
        super.move(dt);  
        
        // Si el misil sale por el lado superior del escenario
        if (y < 0) {          
            // Añade el misil a la lista de actores a borrar    
            window.addRemove(this);               
            return;
        }

        vy -= MISSILE_SPEED;        
    }

    @Override
    public void collision(Actor actor) {}

   }
