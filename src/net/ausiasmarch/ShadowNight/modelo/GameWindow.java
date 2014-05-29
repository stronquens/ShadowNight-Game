package net.ausiasmarch.ShadowNight.modelo;

/**
 * --------------------------------------------------------------------------
 * Interface GameWindows.java Caracteristicas generales de la ventana de juego.
 *
 * @author Armando Maya y Jose M Coronado
 * ---------------------------------------------------------------------------
 */
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

public interface GameWindow {
    // Ancho y alto de la ventana de juego segun resolucion de pantalla

    public final int wSreen = Toolkit.getDefaultToolkit().getScreenSize().width;
    public final int hSreen = Toolkit.getDefaultToolkit().getScreenSize().height;
    // Valores maximo y minimo de ancho y alto de pantalla
    public final int wMax = 1280;
    public final int hMax = 720;
    // Porcentaje visible 
    public final int WIDTH_ = (int) (wSreen);    // Ancho de ventana
    public final int HEIGHT_ = (int) (hSreen);   // Alto de ventana   
    public final static String PATH_IMAGE = "res/image/"; // path imagenes 
    public final static String PATH_MUSIC = "res/music/"; // path musica
    public final static String PATH_SOUND = "res/sound/"; // path sonidos
    public final String TITLE = "Shadow-Night";         // Titulo 
    // Fotogramas por seg del juego 
    public final int FRAMES_SECOND = 25;
    // Numero de filas que avanzaran horizontalmente los shadows 
    public final int SHADOWS_ADVANCE = 3;
    // ACTORES ..............................................................   

    // Filas y columnas de shadows
    public final int MIN_COLS = 10;
    public final int MAX_COLS = 15;

    // Archivos de imagen
    public final String[] SHADOWS = {PATH_IMAGE + "fantasma.png", PATH_IMAGE + "spider.png"};
    public final String[] SHADOWS_FIRE = {PATH_IMAGE + "fantasmafuego.png", PATH_IMAGE + "spiderfuego.png"};
    public final String PLAYER = PATH_IMAGE + "PlayerSentadillas.png";
    public final String PLAYER_DRC = PATH_IMAGE + "Player_Drc.png";
    public final String PLAYER_IZQ = PATH_IMAGE + "Player_Izq.png";
    public final String PLAYERCOHETE_DRC = PATH_IMAGE + "PlayerCoheteDrc.png";
    public final String PLAYERCOHETE_IZQ = PATH_IMAGE + "PlayerCoheteIzq.png";
    public final String PLAYERCOHETE = PATH_IMAGE + "PlayerCohete.png";
    public final String[] CARAMELO_GRITON1 = {PATH_IMAGE + "carameloGriton.png", PATH_IMAGE + "carameloGriton.png",
        PATH_IMAGE + "carameloGriton.png"};
    public final String SMOKE = PATH_IMAGE + "frame.png";
    public final String BIG_SHADOW = PATH_IMAGE + "dark.png";

    public final String CARAMELO_GRITON = PATH_IMAGE + "carameloGriton.png";
    public final String STATUS_BAR_IMG = PATH_IMAGE + "status_bar_final.png";

    public final String EXPLOSION_A = PATH_IMAGE + "ExplosionA.png";
    public final String EXPLOSION_B = PATH_IMAGE + "ExplosionA.png";
    public final String MISSILE = PATH_IMAGE + "caramelo.png";
    public final String CALABAZA = PATH_IMAGE + "Calabaza.png";
    public final String CESTA_CALABAZA = PATH_IMAGE + "cestaCalabaza.png";

    // Velocidades en pixels segun los frames
    public final int SHADOW_SPEED_MIN = FRAMES_SECOND + 5;
    public final int SHADOW_SPEED_MAX = FRAMES_SECOND + 10;
    public final int PLAYER_SPEED = FRAMES_SECOND * 15;
    public final int MISSILE_SPEED = FRAMES_SECOND * 10;
    public final int BOMB_SPEED = FRAMES_SECOND * 20;
    public int SHADOW_LOST_SHIELDS = 10;

    //Numero de frames
    public final int PLAYER_FRAMES = 20;
    public final Integer[] SHADOW_FRAMES = {4, 13};
    public final int MISSILE_FRAMES = 1;
    public final int GRITON_FRAMES = 3;
    public final int EXPLOSION_A_FRAMES = 15;
    public final int EXPLOSION_B_FRAMES = 9;
    public final int CALABAZA_FRAMES = 39;
    public final int SMOKE_FRAMES = 12;

    // Duracion de los Frames
    public final int DURATION_FRAMES_SHADOW = 20;
    public final int DURATION_FRAMES_PLAYER = 20;
    public final int DURATION_FRAMES_DSHADOW = 70;
    public final int DURATION_FRAMES_GRITON = 1;
    public final int DURATION_FRAMES_EXPLOSION = 8;
    public final int DURATION_FRAMES_MISSILE = 50;

    // ESCENARIO ............................................................
    public final int stageX = 20;   // Coordenada x escenario  
    public final int stageY = 20;   // coordenada y esenario
    public final String INTRO = PATH_IMAGE + "intro1.png";   // Escenario
    public final String SPACE = PATH_IMAGE + "fondo1.png";   // Escenario
    public final String SPACE_LEVEL2 = PATH_IMAGE + "fondo2.png";   // Escenario final
    public final String SPACE_FINAL = PATH_IMAGE + "fondo3.png";   // Escenario final
    public final String HOUSE = PATH_IMAGE + "house.png";   // La tierra
    public final String CONTROL = PATH_IMAGE + "controles.png"; // Controles

    // BARRA DE ESTADO .....................................................
    public final int STATUS_BAR = 67;      // Altura barra de estado
    public final int HEIGHT_BASE = 3;      // Altura linea base
    public final int FONT_SIZE = 20;       // Altura texto    
    public final Color FPS_COLOR = Color.LIGHT_GRAY; // Color fps

    public final Color FONT_COLOR = new Color(1, 3, 40);     // Color texto
    public final Color SCORE_COLOR = new Color(212, 130, 82); // Color puntuacion   
    public final Color LSHIELD_COLOR = new Color(125, 1, 1); // Carga escudos
    public final Color DSHIELD_COLOR = new Color(1, 4, 30);  // Descarga escudos
    // Separacion entre filas
    public final double ALFA = 1.7;

    // FINALIZACION JUEGO .....................................................
    public final String START_GAME = PATH_IMAGE + "introtexto.png";
    public final String PRESS_SPACE = PATH_IMAGE + "PressAnyKey.png";
    public final String LEVEL_2 = PATH_IMAGE + "level2.png";
    public final String LEVEL_FINAL = PATH_IMAGE + "levelfinal.png";
    public final String GAMEOVER_WIN = PATH_IMAGE + "backgroundWin.png";
    public final String GAMEOVER_GOT = PATH_IMAGE + "backgroundLose.png";

    // Archivos de MUSICA.......................................................
    public final static String MAIN_THEME = PATH_MUSIC + "Swing Those Lips.mp3";
    public final static String LEVEL1_THEME = PATH_MUSIC + "Downtown Jump.mp3";
    public final static String LEVEL2_THEME = PATH_MUSIC + "Check it out.mp3";

    // Archivos de SONIDO.......................................................
    public final static String GRITO = PATH_SOUND + "Grito.wav";
    public final static String COLISION = PATH_SOUND + "Colision2.wav";
    public final static String LANZAR = PATH_SOUND + "Lanzar.wav";
    public final static String COHETE = PATH_SOUND + "Cohete.wav";
    public final static String PERDIDO = PATH_SOUND + "Perdido.wav";
    public final static String GANADO = PATH_SOUND + "Ganada.wav";
    public final static String BOING = PATH_SOUND + "Boing.wav";
    public final static String GRITO_GRAVE = PATH_SOUND + "GritoGrave.wav";
    public final static String ENGULLE = PATH_SOUND + "mastica.wav";
    public final static String MASTICA = PATH_SOUND + "mastica2.wav";
    public final static String YEAH = PATH_SOUND + "Yea.wav";
    public final static String OOOO = PATH_SOUND + "Oo.wav";

    // METODOS DE NOTIFICACION ****************************************   
    // Añade un actor ala lista de actores 
    public void addActor(Actor actor);
    // Añade un actor a la lista de actores a borrar

    public void addRemove(Actor actor);

    // Obtiene la nave del jugador 
    public House getHouse();

    public Player getPlayer();
    // Notifica que la invasion se ha completado

    public void notifyCompleteInvasion();
    // Notifica que el jugador gana 

    public void notifyWinner();
    // Notifica la muerte del jugador

    public void notifyHouseDeath();
    // Notifica la muerte de un shadow

    public void notifyShadowDeath(Shadow shadow);
    // Notifica que los shadows se mueven solo horizontalmente

    public void notifyNextLevel();
    // Notifica el paso al siguiente nivel
}
