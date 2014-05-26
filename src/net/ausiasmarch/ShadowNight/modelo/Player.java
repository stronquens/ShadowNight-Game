package net.ausiasmarch.ShadowNight.modelo;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import net.ausiasmarch.ShadowNight.util.ImageUtils;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.*;
import static net.ausiasmarch.ShadowNight.modelo.Player.MAX_SHIELDS;
import net.ausiasmarch.ShadowNight.sound.WavPlayer;
import net.ausiasmarch.ShadowNight.util.Keyboard;

/**
 * ---------------------------------------------------------------------------
 * Curso Básico de desarrollo de VideoJuegos en Java 2D Player.java El personaje
 *
 * @author Armando Maya y Jose M Coronado
 * --------------------------------------------------------------------------
 */
public class Player extends Actor {

    public final int MAX_BOMBS = 8;               // Maximo de bombas
    public static final int MAX_SHIELDS = 500;    // Maximo de escudos   
    private int clusterBombs;                     // Contador de bombas 
    private List<String> missileNames;            // Lista de misiles    
    private long lastFire = 0;                    // ultimo disparo 
    private long firingInterval = 300;            // Intervalo entre disparos 
    private List<String> explosionNames;
    private List<List<String>> carameloGritonNames; // Lista de imagen explosion 
    private int score;                            // Puntuacion  
    // Escudos  

    public Player(GameWindow gw, List<String> names) {
        super(names);
        window = gw;
        
        clusterBombs = MAX_BOMBS;
        score = 0;

        missileNames = ImageUtils.getImagesNames(MISSILE, MISSILE_FRAMES);
        carameloGritonNames = ImageUtils.getListOfListNames(Arrays.asList(CARAMELO_GRITON1), GRITON_FRAMES);
        explosionNames = ImageUtils.getImagesNames(EXPLOSION_A, EXPLOSION_A_FRAMES);

    }

    /**
     * Define la actuacion del juhgador
     */
    @Override
    public void act(long dt) {
        super.act(dt);

        if (x < stage.x) {
            vy = 0;
            x = stage.x;
        } else if (x + width > (stage.x + stage.width)) {
            vy = 0;
            x = stage.x + stage.width - width;
        } else if (y < stage.y) {
            vx = 0;
            y = stage.y;
        } else if (y + height > (stage.y + stage.height - 15)) {
            vx = 0;
            y = stage.y + stage.height - height - 15;
        }
        
        // Mueve este jugador
        super.move(dt);
    }

    /**
     * Movimiento horizontal
     *
     * @param int moveSpeed Velocidad en horozontal
     */
    public void setHorizontalMovement(int moveSpeed) {
        vx = moveSpeed;
    }

    /**
     * Movimiento vertical
     *
     * @param int moveSpeed Velocidad en vertical
     */
    public void setVerticalMovement(int moveSpeed) {
        vy = moveSpeed;
    }

    /**
     * Obtiene el numero de bombas de fragmantacion usadas
     *
     * @return int
     */
    public int getClusterBombs() {
        return clusterBombs;
    }

    /**
     * Establece el numero de gritones que se usaraan
     *
     * @param clusterBombs
     */
    public void setClusterBombs(int clusterBombs) {
        this.clusterBombs = clusterBombs;
    }

    /**
     * Establece la lista de los nombres de los misiles usados
     *
     * @param missileImageName
     */
    public void setMissileNames(List<String> missileNames) {
        this.missileNames = missileNames;
    }

    /**
     * Establece la lista de los nombres de los gritones usadas
     *
     * @param bombImageNames
     */
    public void setBombNames(List<List<String>> bombNames) {
        this.carameloGritonNames = bombNames;
    }

    /**
     * Establece la lista de nombres de explosion del shadow
     *
     * @param ExplosionImageName lista de nombres de la explosion
     */
    public void setExplosionNames(List<String> explosionNames) {
        this.explosionNames = explosionNames;
    }

    /**
     * Obtiene la puntuacion conseguida
     *
     * @return int
     */
    public int getScore() {
        return score;
    }

    /**
     * Establece la puntuacion
     *
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Obtiene los escudos
     *
     * @return int
     */
    /**
     * Incrementa la puntuacion
     *
     * @param i
     */
    public void addScore(int i) {
        score += i;
    }

    /**
     * Dispara un grupo de bombas
     */
    public void fireCluster() {
        if (clusterBombs == 0) {
            return;
        }
        playSound(GRITO);

        if (System.currentTimeMillis() - lastFire < firingInterval) {
            return;
        }

        lastFire = System.currentTimeMillis();
        clusterBombs--;

        for (int i = 0; i < 3; i++) {
            CandyScreamer carameloGriton = new CandyScreamer(window, stage, x, y, ShotDirection.values()[i],
                    carameloGritonNames.get(i));
            carameloGriton.setStage(stage);
            carameloGriton.setFrameDuration(DURATION_FRAMES_GRITON);
            window.addActor(carameloGriton);
        }
    }

    /**
     * Disparo de un Misil
     */
    public void fireMissile() {
        if (System.currentTimeMillis() - lastFire < firingInterval) {
            return;
        }
        playSound(LANZAR);
        lastFire = System.currentTimeMillis();
        // misiles izquierdo y derecho
        fireRightMissile();
        fireLeftMissile();

    }

    /**
     * Dispara misil izquiedo
     */
    private void fireRightMissile() {
        Missile rightMissile = new Missile(window, missileNames);
        rightMissile.setStage(stage);
        rightMissile.setFrameDuration(DURATION_FRAMES_MISSILE);
        rightMissile.setX(x + (width - 40));
        rightMissile.setY(y + 42 - rightMissile.getHeight() / 2);
        window.addActor(rightMissile);
    }

    /**
     * Dispara misil izquiedo
     */
    private void fireLeftMissile() {
        Missile leftMissile = new Missile(window, missileNames);
        leftMissile.setStage(stage);
        leftMissile.setFrameDuration(DURATION_FRAMES_MISSILE);
        leftMissile.setX(x + (width - 65));
        leftMissile.setY(y + 28 - leftMissile.getHeight() / 2);
        window.addActor(leftMissile);
    }

    /**
     * Comprueba con quien colisiona el jugador
     *
     * @param actor
     */
    @Override
    public void collision(Actor actor) {
        if (actor instanceof House) {
            y = stage.y + stage.height - height - 180;
            playSound(BOING);
        }
    }
}
