package net.ausiasmarch.ShadowNight.gui;

/**
 * ---------------------------------------------------------------------------
 * SHADOW NIGHT
 *
 * @author Armando Maya y Jose M Coronado
 * ----------------------------------------------------------------------------
 */
import jaco.mp3.player.MP3Player;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.Timer;
import net.ausiasmarch.ShadowNight.modelo.*;
import net.ausiasmarch.ShadowNight.util.ImageUtils;
import net.ausiasmarch.ShadowNight.util.Keyboard;
import static net.ausiasmarch.ShadowNight.modelo.GameWindow.*;
import static net.ausiasmarch.ShadowNight.modelo.ScreenStatus.*;
import net.ausiasmarch.ShadowNight.sound.WavPlayer;

public class ShadowNight extends Canvas implements GameWindow {
    /* La ventana principal de juego */

    private JFrame frame;
    /* Un doble bufer para acelerar la renderizacion */
    private BufferStrategy strategy;
    /* El entorno grafico de aceleracion que nos pirmite dibujar*/
    private Graphics2D g2D;
    /* Lista de actores del juego */
    private List<Actor> actors = new ArrayList<>();
    /* Lista de los actores que deben borrarse del juego */
    private List<Actor> deleteActors = new ArrayList<>();
    /* Rectangulo con los limites del escenario */
    private Rectangle stage;
    /* Contador de Shadows */
    private int shadowsCount = 0;
    /* Tiempo en ms usado para el movimiento */
    private long dt;
    /* True mientras el juego se esta ejecutando */
    private boolean gameRunning = true;
    /* Shadows iniciales */
    private int initialShadows;
    /* el jugador */
    private Player player;
    //Bonus
    private PumpkinBasket cesta;
    //El humo de la chimenea
    private Smoke humo;
    //Los shadows
    private Shadow shadow;
    // Los shadows grandes
    private BigShadow bigShadow;
    /* El pueblo */
    private House house;
    /* Sprite de una "pieza" del fondo del escenario */
    private BufferedImage backgroundTile;
    /* Sprite del fondo completo del esceanrio formado por los backgroundTil*/
    private BufferedImage background;
    /* Desplazamiento en Y para hacer scrool del escenario */
    private int backgroundY;
    /* La imagen del mensaje a mostrar cuando esperamos la pulsacion de una tecla */
    private BufferedImage message;
    /* Estado de la pantalla */
    private ScreenStatus screen;
    // Valor del instante de tiempo en ms cuando se informo de los fps
    private long timeFps;
    // Contador de frames
    private long frames = 0;
    // Ultimo instante de tiempo en ms
    private long lastTime = timeFps = getCurrentTimeMillis();
    //Animaciones
    private List<String> spritesPlayer;
    private List<String> spritesPlayerRight;
    private List<String> spritesPlayerLeft;
    private List<String> spritesPlayerCoheteLeft;
    private List<String> spritesPlayerCoheteRight;
    private List<String> spritesPlayerCohete;
    //El tiempo del reloj
    private int contador = 60;

    public ShadowNight() {
        // La Ventana de juego    
        frame = new JFrame(TITLE);
        // Altura y anchura de ventana
        int width = wMax, height = hMax;
        if (WIDTH_ > wMax && HEIGHT_ > hMax) {
            width = wMax;
            height = hMax;
        }
        // Dimensiones del canvas
        setBounds(0, 0, width, height);
        // Añade este (this) canvas a la ventanda
        frame.add(this);
        // Al cerrar la ventana finaliza la aplicacion
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Hace que awt ignore el repintando del canvas
        setIgnoreRepaint(false);
        // Hace la ventana visible
        frame.getContentPane().setBackground(Color.BLACK);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Crea el escenario 
        stage = new Rectangle(stageX, stageY, getWidth() - stageX * 2,
                getHeight() - stageY - STATUS_BAR - HEIGHT_BASE);

        // Inicia el sistema de eventos de teclado para el canvas  
        Keyboard.init(this);
        // Pone el foco en el canvas 
        requestFocus();
        // Elimina el cursor del raton
        BufferedImage cursor = ImageUtils.createCompatibleImage(10, 10,
                Transparency.BITMASK);
        Toolkit t = Toolkit.getDefaultToolkit();
        Cursor c = t.createCustomCursor(cursor, new Point(5, 5), "null");
        setCursor(c);
        // Crea un doble bufer para acceleracion grafica
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        // Inicia el juego
        startGame();
    }

    /**
     * Inicializa los elementos comunes del juego
     */
    private void startGame() {
        // Ponemos el titulo
        frame.setTitle(TITLE);
        // Pantalla inicial de juego activa
        screen = ScreenStatus.HOME;
        // Mensaje de la pantalla inicial 
        message = SpriteCache.get().getSprite(START_GAME);
        // Inicia el escenario
        initStage(INTRO);
        // Inicia los actores 
        initActors();
        playTheme(MAIN_THEME);
    }

    private void nextLevel2() {
        // Ponemos el titulo
        frame.setTitle(TITLE);
        // Pantalla inicial de juego activa
        screen = ScreenStatus.NEXT_LEVEL;
        // Inicia el escenario
        initStage(LEVEL_2);
        // Inicia los actores 
        initActors();
    }

    private void nextLevelFinal() {
        // Ponemos el titulo
        frame.setTitle(TITLE);
        // Pantalla inicial de juego activa
        screen = ScreenStatus.NEXT_LEVELFINAL;
        // Inicia el escenario
        initStage(LEVEL_FINAL);
        // Inicia los actores 
        initActors();
    }

    /**
     * Inicializa el escenario principal de juego
     */
    private void initStage(String stageName) {
        // Obtiene la imagen de fondo
        backgroundTile = SpriteCache.get().getSprite(stageName);
        // Crea una imagen opaca de fondo, compatible con la imagen cargada
        background = ImageUtils.createCompatibleImage(
                getWidth(), getHeight() + backgroundTile.getHeight(),
                Transparency.OPAQUE);
        // Obtiene un entorno grafico de background
        Graphics2D g = (Graphics2D) background.getGraphics();
        // Crea un rectangulo con las dimensiones de backgroundTile
        Rectangle rec = new Rectangle(0, 0, backgroundTile.getWidth(),
                backgroundTile.getHeight());
        // Crea una textura a partir de la iamgen backgroundTile usando el 
        // rectangulo  anterior. Se usara para pintar sobre el background
        TexturePaint tp = new TexturePaint(backgroundTile, rec);
        // Establece que se va a pintar con la textura creada
        g.setPaint(tp);
        // Rellena la imagen background con la textura creada  
        g.fillRect(0, 0, background.getWidth(), background.getHeight());
        // Valor inicial para hacer un scroll vertical
        backgroundY = 0;
    }

    /**
     * Inicia el juego, configurando el escenario, los actores y jugador
     */
    public void initActors() {
        // Lista de actores 
        actors = new ArrayList<>();
        deleteActors.clear();

        // HOUSE ...........................................................              
        house = new House(this, ImageUtils.getImagesNames(HOUSE, 1));
        house.setX(0);
        house.setY(getHeight() - (house.getHeight() + 60));
        addActor(house);

        // El Jugador .........................................................
        spritesPlayer = ImageUtils.getImagesNames(PLAYER, PLAYER_FRAMES);
        spritesPlayerRight = ImageUtils.getImagesNames(PLAYER_DRC, PLAYER_FRAMES);
        spritesPlayerLeft = ImageUtils.getImagesNames(PLAYER_IZQ, PLAYER_FRAMES);
        spritesPlayerCoheteRight = ImageUtils.getImagesNames(PLAYERCOHETE_DRC, PLAYER_FRAMES);
        spritesPlayerCoheteLeft = ImageUtils.getImagesNames(PLAYERCOHETE_IZQ, PLAYER_FRAMES);
        spritesPlayerCohete = ImageUtils.getImagesNames(PLAYERCOHETE, PLAYER_FRAMES);

        player = new Player(this, ImageUtils.getImagesNames(PLAYER, PLAYER_FRAMES));
        player.setStage(stage);
        player.setFrameDuration(DURATION_FRAMES_PLAYER);
        player.setX(stage.width / 2 - player.getWidth() / 2);
        player.setY(stage.height - (int) (1.5 * player.getHeight() / 2) - (house.getHeight()));

        addActor(player);

        //Humo de las casas
        humo = new Smoke(this, ImageUtils.getImagesNames(SMOKE, SMOKE_FRAMES));
        humo.setStage(stage);
        humo.setFrameDuration(80);
        humo.setX(655);
        humo.setY(385);
        addActor(humo);

        // Las shadows..........................................................
        List<Integer> types = new ArrayList<>();             // Lista de tipos
        int type = 0;

        if (screen == ScreenStatus.HOME) {
            int columns = Aleatory.nextInt(4, 6);
            initialShadows = shadowsCount = columns; // numero de shadows por oleada

            List<List<String>> shadowNames;
            shadowNames = ImageUtils.getListOfListNames(Arrays.asList(SHADOWS), Arrays.asList(SHADOW_FRAMES));
            do {
                type = Aleatory.nextInt(2);  // Obtiene un tipo aleatorio  
            } while (types.contains(type));

            types.add(type);  // Añade el tipo a la lista de tipos

            for (int j = 0; j < columns; j++) {
                // Crea un shadow con su lista de imagenes de frames segun su color        
                shadow = new Shadow(this, shadowNames.get(type));
                // Lo pone en el escenario
                shadow.setStage(stage);
                //Añade transparencia segun el tipo
                if (type == 0) {
                    shadow.setTranslucent(0.50f);
                }
                // Duracion de cada frame          
                shadow.setFrameDuration(DURATION_FRAMES_SHADOW);
                // Establece la Puntuacion segun el shadow
                shadow.setScore(Score.values()[type].getValue());
                // Coordenadas x e y 
                shadow.setX(Aleatory.nextInt(stageX + 10, getWidth() - shadow.getWidth()));
                shadow.setY(stageY - shadow.getHeight() * 2);

                shadow.setVx(0);
                shadow.setVy(0);

                // Añade el enemigo a la lista de actores
                addActor(shadow);

            }
        }
        // Si el nivel es el 2 se inician los actores del nivel 2
        if (screen == ScreenStatus.NEXT_LEVEL) {
            shadowsLevel2();
        }
        // Si el nivel es el final se inician los actores del nivel final
        if (screen == ScreenStatus.NEXT_LEVELFINAL) {
            shadowsLevelFinal();
        }
    }

    /**
     * Inicia los shadows del nivel 2
     */
    public void shadowsLevel2() {
        List<Integer> types = new ArrayList<>(); // Lista de tipos
        int type = 0;
        int columns = Aleatory.nextInt(MIN_COLS, MAX_COLS);

        initialShadows = shadowsCount = columns; // numero de shadows por oleada

        List<List<String>> shadowNames;
        shadowNames = ImageUtils.getListOfListNames(Arrays.asList(SHADOWS), Arrays.asList(SHADOW_FRAMES));

        do {
            type = Aleatory.nextInt(2);  // Obtiene un tipo aleatorio  
        } while (types.contains(type));

        types.add(type);  // Añade el tipo a la lista de tipos

        for (int j = 0; j < columns; j++) {
            // Crea un shadow con su lista de imagenes de frames segun su color        
            shadow = new Shadow(this, shadowNames.get(type));
            // Lo pone en el escenario
            shadow.setStage(stage);
            //SEGUN EL SHADOW SE PONE TRANSPARENCIA
            if (type == 0) {
                shadow.setTranslucent(0.50f);
            }
            // Duracion de cada frame          
            shadow.setFrameDuration(DURATION_FRAMES_SHADOW);
            // Establece la Puntuacion segun el shadow
            shadow.setScore(Score.values()[type].getValue());
            // Coordenadas x e y 
            shadow.setX(Aleatory.nextInt(stageX + 10, getWidth() - shadow.getWidth()));
            shadow.setY(stageY - shadow.getHeight() * 2);

            shadow.setVx(0);
            shadow.setVy(0);

            // Añade el enemigo a la lista de actores
            addActor(shadow);
        }
    }

    /**
     * Inicia los shadows del nivel final
     */
    public void shadowsLevelFinal() {
        List<Integer> types = new ArrayList<>(); // Lista de tipos
        int type = 0;
        int columns = Aleatory.nextInt(MIN_COLS, MAX_COLS);

        initialShadows = shadowsCount = columns; // numero de shadows por oleada

        List<List<String>> shadowNames;
        shadowNames = ImageUtils.getListOfListNames(Arrays.asList(SHADOWS_FIRE), Arrays.asList(SHADOW_FRAMES));

        do {
            type = Aleatory.nextInt(2);  // Obtiene un tipo aleatorio  
        } while (types.contains(type));

        types.add(type);  // Añade el tipo a la lista de tipos

        for (int j = 0; j < columns; j++) {
            // Crea un shadow con su lista de imagenes de frames segun su color        
            shadow = new Shadow(this, shadowNames.get(type));
            // Lo pone en el escenario
            shadow.setStage(stage);
            //SEGUN EL SHADOW SE PONE TRANSPARENCIA
            if (type == 0) {
                shadow.setTranslucent(0.50f);
            }
            // Duracion de cada frame          
            shadow.setFrameDuration(DURATION_FRAMES_SHADOW);
            // Establece la Puntuacion segun el shadow
            shadow.setScore(Score.values()[type].getValue());
            // Coordenadas x e y 
            shadow.setX(Aleatory.nextInt(stageX + 10, getWidth() - shadow.getWidth()));
            shadow.setY(stageY - shadow.getHeight() * 2);

            shadow.setVx(0);
            shadow.setVy(0);

            // Añade el enemigo a la lista de actores
            addActor(shadow);
        }
    }

    /**
     * Devuelve los shadows muertos al escenario
     */
    public void returnShadows() {
        List<Integer> types = new ArrayList<>();    // Lista de tipos
        int type = 0;
        int columns = 1;

        initialShadows = shadowsCount = columns; // numero de shadows por oleada
        List<List<String>> shadowNames;

        // Segun si la pantalla es la final ejecuta una naimacion para los shadows
        if (screen == ScreenStatus.FINAL) {
            // Si es el nivel final ejecuta la animacion de shadows con fuego
            shadowNames = ImageUtils.getListOfListNames(Arrays.asList(SHADOWS_FIRE), Arrays.asList(SHADOW_FRAMES));
        } else {
            shadowNames = ImageUtils.getListOfListNames(Arrays.asList(SHADOWS), Arrays.asList(SHADOW_FRAMES));
        }

        do {
            type = Aleatory.nextInt(2);  // Obtiene un tipo aleatorio  
        } while (types.contains(type));

        types.add(type);  // Añade el tipo a la lista de tipos*/

        for (int j = 0; j < columns; j++) {
            // Crea un shadow con su lista de imagenes de frames segun su color        
            shadow = new Shadow(this, shadowNames.get(type));
            // Lo pone en el escenario
            shadow.setStage(stage);
            // Segun el tipo aplica transparencia
            if (type == 0) {
                shadow.setTranslucent(0.50f);
            }
            // Duracion de cada frame          
            shadow.setFrameDuration(DURATION_FRAMES_SHADOW);
            // Establece la Puntuacion segun el shadow
            shadow.setScore(Score.values()[type].getValue());
            // Coordenadas x e y 
            shadow.setX(Aleatory.nextInt(stageX + 10, getWidth() - shadow.getWidth()));
            shadow.setY(stageY - shadow.getHeight() * 2);

            shadow.setVx(0);
            shadow.setVy(0);

            // Añade el enemigo a la lista de actores
            addActor(shadow);

        }
    }

    public void moveShadow() {

        for (int j = 0; j < actors.size(); j++) {
            Actor a = actors.get(j);
            if (a instanceof Shadow) {
                //Determina la direccion horizontal
                int direccion = Aleatory.nextInt(-2, 2);
                //si el nivel es el final la velocidad en el eje x aumenta para los shadows
                if (screen == ScreenStatus.FINAL) {
                    //La velocidad se multiplica en 3 en el nivel final por que se estan quemando
                    a.setVx(Aleatory.nextInt(SHADOW_SPEED_MIN * 3, SHADOW_SPEED_MAX * 3) * direccion);
                    a.setVy(Aleatory.nextInt(SHADOW_SPEED_MIN, SHADOW_SPEED_MAX));
                } else {
                    a.setVx(Aleatory.nextInt(SHADOW_SPEED_MIN, SHADOW_SPEED_MAX) * direccion);
                    a.setVy(Aleatory.nextInt(SHADOW_SPEED_MIN, SHADOW_SPEED_MAX));
                }
            }
        }
    }

    /**
     * Contra Reloj
     */
    Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {

            contador--;
// Reinicia el tiempo cada vez que se pasa de nivel
            if (contador == 0 && screen == ScreenStatus.GAME) {
                timer.stop();
                notifyNextLevel();
                contador = 60;
            }

            if (contador == 0 && screen == ScreenStatus.LEVEL2) {
                timer.stop();
                notifyLevelFinal();
                contador = 60;
            }

            if (contador == 0 && screen == ScreenStatus.FINAL) {
                timer.stop();
                notifyWinner();
                contador = 60;
            }
        }
    });

    /**
     * Añade un nuevo actor a la lista de actores
     *
     * @param actor
     */
    @Override
    public void addActor(Actor actor) {
        actors.add(actor);
    }

    /**
     * Añade un nuevo actor a la lista de actores para borrar
     *
     * @param actor
     */
    @Override
    public void addRemove(Actor actor) {
        deleteActors.add(actor);
    }

    /**
     * Obtiene el jugador
     *
     * @return Player
     */
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Obtiene las casas
     *
     * @return
     */
    @Override
    public House getHouse() {
        return house;
    }

    /**
     * Notificacion de que se ha completado la invasion
     */
    @Override
    public void notifyCompleteInvasion() {
        sleep(500);
        message = SpriteCache.get().getSprite(GAMEOVER_GOT);
        screen = ScreenStatus.GAME_OVER;
        timer.stop();
        contador = 60;

    }

    /**
     * Notifica que hay que cambiar de nivel al 2º nivel
     */
    @Override
    public void notifyNextLevel() {

        message = SpriteCache.get().getSprite(LEVEL_2);
        screen = ScreenStatus.NEXT_LEVEL;
        timer.stop();
        contador = 60;
        nextLevel2();

    }

    /**
     * Notifica que hay que cambiar de nivel al nivel final
     */
    //@Override
    public void notifyLevelFinal() {

        message = SpriteCache.get().getSprite(LEVEL_FINAL);
        screen = ScreenStatus.NEXT_LEVELFINAL;
        timer.stop();
        contador = 60;
        nextLevelFinal();
    }

    /**
     * Notificacion que el pueblo a sido destruido
     */
    @Override
    public void notifyHouseDeath() {
        message = SpriteCache.get().getSprite(GAMEOVER_GOT);
        screen = ScreenStatus.GAME_OVER;
        timer.stop();
        contador = 60;
        MP3Player.stop();
        WavPlayer.play(PERDIDO);
    }

    /**
     * Notificacion de que el jugador ha ganado
     */
    @Override
    public void notifyWinner() {
        getHouse();
        addRemove(house);
        message = SpriteCache.get().getSprite(GAMEOVER_WIN);
        screen = ScreenStatus.GAME_OVER;
        timer.stop();
        contador = 60;
        MP3Player.stop();
        WavPlayer.play(GANADO);
    }

    /**
     * Notificacion de que un shadow a sido eliminado
     *
     * @param shadowDeath
     */
    @Override
    public void notifyShadowDeath(Shadow shadowDeath) {
        shadowsCount--;
        int aparicion = Aleatory.nextInt(0, 15);
        // Cuando el numero de shadow disminuya en 1, comienzan a disparar
        if (shadowsCount == initialShadows - 1) {
            //inicia la cuenta atras
            timer.start();
            returnShadows();
            for (int j = 0; j < actors.size(); j++) {
                Actor a = actors.get(j);
                if (a instanceof Shadow) {
                    if (a != shadowDeath) {
                        // Direccion en el je x aleatoria
                        int direccion = Aleatory.nextInt(-2, 2);
                        //Si es el nivel final la velocidad en x se multiplica por 3
                        if (screen == ScreenStatus.FINAL) {
                            a.setVx(Aleatory.nextInt(SHADOW_SPEED_MIN * 3, SHADOW_SPEED_MAX * 3) * direccion);
                            a.setVy(Aleatory.nextInt(SHADOW_SPEED_MIN, SHADOW_SPEED_MAX));
                        } else {
                            a.setVx(Aleatory.nextInt(SHADOW_SPEED_MIN, SHADOW_SPEED_MAX) * direccion);
                            a.setVy(Aleatory.nextInt(SHADOW_SPEED_MIN, SHADOW_SPEED_MAX));
                        }
                    }
                }
            }
        }
        /* Si cuando muere un shadow el numero de aparicion es 1 y el 
         tiempo es menor de 30s sale una cesta*/
        if (aparicion == 1 && contador <= 40) {
            // El Bonus...........................................................
            cesta = new PumpkinBasket(this, ImageUtils.getImagesNames(CALABAZA, 1));
            cesta.setStage(stage);
            cesta.setFrameDuration(20);
            cesta.setX(Aleatory.nextInt(stageX + 10, getWidth() - cesta.getWidth()));
            cesta.setY(stageY - cesta.getHeight() * 2);
            cesta.setVx(SHADOW_SPEED_MIN);
            cesta.setVy(SHADOW_SPEED_MIN);
            addActor(cesta);
        }
        if (aparicion == 2 && screen == ScreenStatus.LEVEL2) { 
            int direccion = Aleatory.nextInt(-2, 2);
            bigShadow = new BigShadow(this, ImageUtils.getImagesNames(BIG_SHADOW, 5));
            bigShadow.setStage(stage);
            bigShadow.setFrameDuration(100);
            bigShadow.setX(Aleatory.nextInt(stageX + 10, getWidth() - bigShadow.getWidth()));
            bigShadow.setY(stageY - bigShadow.getHeight() * 2);
            bigShadow.setVx(Aleatory.nextInt(SHADOW_SPEED_MIN, SHADOW_SPEED_MAX) * direccion);
            bigShadow.setVy(Aleatory.nextInt(SHADOW_SPEED_MIN, SHADOW_SPEED_MAX));
            addActor(bigShadow);
        }

    }

    /*
     * Comprueba las colisiones entre dos actores  cualesquiera
     */
    private void checkCollisions() {
        // Para cada actor...
        for (int i = 0; i < actors.size(); i++) {

            Actor a1 = actors.get(i);         // Obtiene el actor a1 

            // Para cada actor...
            for (int j = i + 1; j < actors.size(); j++) {

                Actor a2 = actors.get(j);  // Obtiene el actor a2 

                // Si a1 colisiona por pixel con a2...
                if (a1.pixelCollidesWith(a2)) {
                    a1.collision(a2); // Colision actor a1 - a2                                   
                    a2.collision(a1); // Colision actor a2 - a1

                }
            }
        }
    }

    /**
     * Obtiene el contexto grafico de dibujo del doble bufer
     *
     * @return Graphics2D
     */
    public Graphics2D getDrawGraphics() {
        return (Graphics2D) strategy.getDrawGraphics();
    }

    /**
     * Visualiza el escenario en el contexto grafico
     */
    public void displayStage() {
        // Dibuja el escenario
        g2D.drawImage(background, 0, 0, getWidth(), getHeight(),
                0, backgroundY, getWidth(), backgroundY + getHeight(), this);
    }

    /**
     * Renderiza el juego
     */
    public void drawGame(float interpolation) {
        // Dibuja los actores sobre el contexto grafico uasndo la interpolacion calculada
        for (Actor actor : actors) {
            // Interpola la posicion de dibujo (renderizado)
            actor.interpolate(interpolation, dt);
            // Dibuja este actor sobre el contexto grafico
            actor.draw(g2D);
        }
    }

    /**
     * Actualiza el juego: mueve los personajes y hace que actuen
     */
    private void updateGame() {
        // Procesa la actuacion normal de los actores
        for (int i = 0; i < actors.size(); i++) {
            actors.get(i).act(dt);   // hace que el actor actue     
        }
    }

    /**
     * Visualiza la informacion de la barra de estado del juego
     */
    private void displayStatusBar() {
        displayStatusBarImage();
        displayScore();        // visualiza la puntuacion
        displayShields();      // visualizaa los escudos
        displayGritones();     // visualiza los gritones
        displayTime();         // visualiza el tiempo
    }

    /**
     * Visualiza la cuenta atras
     */
    private void displayTime() {
        int w, x, y, wt, hs, fs;
        w = getWidth() / 6;           // sexta parte del ancho de la ventana 
        hs = (int) (STATUS_BAR / 2);  // altura de la barra de shield

        fs = (int) (hs * 0.85);                       // altura de la fuente 
        Font font = new Font("Bradley Hand Itc", Font.BOLD, fs); // tipo de fuente     

        FontMetrics fm = g2D.getFontMetrics(font);    // metrica tipo de fuente 
        g2D.setFont(font);                     // tipo de fuente a usar
        g2D.setPaint(FONT_COLOR);              // color con el que se pintara
        wt = fm.stringWidth("Time:");         // ancho de este texto  

        x = w * 5;
        y = (getHeight() - STATUS_BAR) + (STATUS_BAR / 2) + fs / 3;
        g2D.drawString("Time:", x, y);
        g2D.setPaint(SCORE_COLOR);
        x = x + wt;
        g2D.drawString(" " + contador + " ", x, y);

    }

    /**
     * Visualiza la puntuacion
     *
     * @param g
     */
    private void displayScore() {
        int w, x, y, wt, hs, fs;
        w = getWidth() / 6;           // sexta parte del ancho de la ventana 
        hs = (int) (STATUS_BAR / 2);  // altura de la barra de shield

        fs = (int) (hs * 0.85);                       // altura de la fuente 
        Font font = new Font("Bradley Hand Itc", Font.BOLD, fs); // tipo de fuente     

        FontMetrics fm = g2D.getFontMetrics(font);    // metrica tipo de fuente 
        g2D.setFont(font);                     // tipo de fuente a usar
        g2D.setPaint(FONT_COLOR);              // color con el que se pintara
        wt = fm.stringWidth("Score:");         // ancho de este texto  

        x = (int) (w / 2 - wt / 2) / 2;
        y = (getHeight() - STATUS_BAR) + (STATUS_BAR / 2) + fs / 3;
        g2D.drawString("Score:", x, y);
        g2D.setPaint(SCORE_COLOR);
        x = x + wt;
        g2D.drawString(" " + player.getScore() + " ", x, y);
    }

    /**
     * Visualiza los escudos
     *
     * @param g
     */
    private void displayShields() {
        int w, ws, ws2, hs, x, y, fs;
        w = getWidth() / 6;              // sexta parte del ancho de la ventana        
        hs = (int) (STATUS_BAR / 2);     // altura de la barra de shield

        fs = (int) (hs * 0.85);                       // altura de la fuente 
        Font font = new Font("Bradley Hand Itc", Font.BOLD, fs); // tipo de fuente     
        FontMetrics fm = g2D.getFontMetrics(font);   // metrica tipo de fuente 

        g2D.setFont(font);                    // tipo de fuente a usar
        g2D.setPaint(FONT_COLOR);            // color con el que se pintara
        int wt = fm.stringWidth("Life: ");  // ancho de este texto  

        x = (int) (w + (w / 2 - wt / 2)) - 75;
        y = (getHeight() - STATUS_BAR) + (STATUS_BAR / 2) + fs / 3;
        g2D.drawString("Life: ", x, y);

        ws = 2 * w;             // ancho de la barra de shields       
        x = (2 * w) - 100;              // coord. x barra de shields 
        // coord. y barra shield
        y = (getHeight() - STATUS_BAR) + (STATUS_BAR / 2 - hs / 2);
        g2D.setPaint(DSHIELD_COLOR);     // descargado
        g2D.fillRect(x, y, ws, hs);

        ws2 = (house.getShields() * ws) / Player.MAX_SHIELDS;
        g2D.setPaint(LSHIELD_COLOR);
        g2D.fillRect(x, y, ws2, hs);  // carga actual
    }

    /**
     * Añade la imagen de la barra de estado
     */
    private void displayStatusBarImage() {

        String status_bar = ImageUtils.getImagesNames(STATUS_BAR_IMG, 1).get(0);
        BufferedImage sprite = SpriteCache.get().getSprite(status_bar);
        int x, y;

        x = 5;
        y = getHeight() - STATUS_BAR + (STATUS_BAR / 2 - sprite.getHeight() / 2);

        g2D.drawImage(sprite, x, y, this);

    }

    /**
     * Visualiza la carga y descarga de los gritones
     *
     * @param g
     */
    private void displayGritones() {

        String griton = ImageUtils.getImagesNames(CARAMELO_GRITON, 1).get(0);
        BufferedImage sprite = SpriteCache.get().getSprite(griton);
        int w, x, y;
        w = getWidth() / 6;     // sexta parte del ancho de la ventana  
        x = 4 * w + (w / 2 - 5 * sprite.getHeight() / 2);
        y = getHeight() - STATUS_BAR + (STATUS_BAR / 2 - sprite.getHeight() / 2) + 10;

        for (int i = 0; i < player.getClusterBombs(); i++) {
            g2D.drawImage(ImageUtils.resize(sprite, 30, 30), x + i * sprite.getWidth() / 2, y, this);
        }
    }

    /**
     * Visualiza el mensaje de pulsar una tecla
     */
    public void displayMessageKeyPress() {
        g2D.drawImage(message, getWidth() / 2 - message.getWidth() / 2,
                getHeight() / 2 - message.getHeight() / 2, this);
    }

    /**
     * Hace una pausa
     */
    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtien el tiempo actual del sistema
     *
     * @return
     */
    private long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * BUCLE Principal de Juego
     */
    private void loopGame() {
        // Mientras el juego se este ejecutandose (gameRunnig = true)
        while (gameRunning) {
            // Obtiene el contexto grafico y visualiza el escenario
            g2D = getDrawGraphics();
            // Muestra y actualiza el escenario
            displayStage();
            // Actualiza y renderiza el juego 
            frameRendering();
            // Entradas de usuario
            keyInputUser();
            // Completa el dibujo,limpia el entorno grafico y vuelca el buffer 
            g2D.dispose();
            strategy.show();
        }
    }

    private void frameRendering() {
        // Frecuencia de tiempo en ms para la logica(actualizacion)del juego 
        final long LOGIC_TIME = 1000 / FRAMES_SECOND;
        // Iteracion del bucle para el control de MAX_FRAMESKIP
        long loops;
        // Interpolacion para el renderizado
        float interpolation;
        // Actual instante de tiempo en ms
        long currentTime;

        // Si estamos la pantalla GAME (estamos jugando)
        if (screen == ScreenStatus.GAME || screen == ScreenStatus.LEVEL2
                || screen == ScreenStatus.FINAL) {
            // Obtenemos el instante de tiempo actual 
            currentTime = getCurrentTimeMillis();
            // Frecuencia de actualizacion de la logica juego
            dt = LOGIC_TIME;
            // Si la diferencia entre los tiempos es mayor o igual que la 
            // frecuencia de actualizacion
            if (currentTime - lastTime >= LOGIC_TIME) {
                // Calculamos el numero de veces que llamaremos a updateGame
                loops = (currentTime - lastTime) / LOGIC_TIME;
                // 1- ACTUALIZACION ......................................... 
                for (int i = 0; i < loops; i++) {
                    // Actualizamos el juego 
                    updateGame();
                    // Comprobamos las colisiones 
                    checkCollisions();
                    // Quitamos los actores eliminados 
                    actors.removeAll(deleteActors);
                    deleteActors.clear();
                    lastTime += LOGIC_TIME;
                }

                // Actualizamos lastTime
                timeFps += dt;

                frames++;
                // Si ha pasado un segundo,informamos del rendimiento
                if (timeFps >= 1000) {
                    frame.setTitle(TITLE + " ( " + frames + " FPS )");
                    frames = 0;
                    timeFps = 0;
                }
            } else {
                // Hacemos una pausa
                sleep(LOGIC_TIME - (currentTime - lastTime));
            }

            // 2- RENDERIZACION .............................................
            // Interpolacion para el dibujo  
            interpolation = Math.min(0.5f, (float) (currentTime - lastTime)
                    / LOGIC_TIME);
            // Dibuja (renderiza) el juego usando interpolacion
            // Como maximo, las llamadas a drawGame se haran a una velocidad 
            // de FRAMES_SECOND dependiendo del hardware
            drawGame(interpolation);
            // Mostramos la barra de estado
            displayStatusBar();
        }

        // Si esperamos la pulsacion de una tecla en HOME o GAME_OVER
        if (screen == ScreenStatus.HOME || screen == ScreenStatus.NEXT_LEVEL
                || screen == ScreenStatus.NEXT_LEVELFINAL || screen == ScreenStatus.GAME_OVER) {
            // Mostramos el mensaje correspondiente      
            displayMessageKeyPress();

        }
    }

    /**
     * Valida las entradas de teclado realizadas por el usuario
     */
    private void keyInputUser() {
        switch (screen) {
            // Pantalla inicial
            case HOME:
                // Si pulso ESCAPE
                if (Keyboard.isPressed(KeyEvent.VK_ESCAPE)) {
                    gameRunning = false; //System.exit(0); // Salimos del juego
                }
                // Si pulso ENTER
                if (Keyboard.isPressed(KeyEvent.VK_ENTER)) {
                    screen = ScreenStatus.GAME;     // Pantalla de juego  
                    initStage(SPACE);              // Iniciamos el escenario 
                    moveShadow();
                    WavPlayer.play(COLISION);
                    playTheme(LEVEL1_THEME);
                }
                if (Keyboard.isPressed(KeyEvent.VK_C)) {
                    screen = ScreenStatus.CONTROLS;
                    initStage(CONTROL);
                }
                break;

            // Controles   
            case CONTROLS:
                if (Keyboard.isPressed(KeyEvent.VK_C)) {
                    screen = ScreenStatus.HOME;
                    initStage(INTRO);
                }
                break;

            // Pantalla de juego    
            case GAME:
                movementPlayer(); // Resolvemos el movimiento del player       
                tryToFire();    // Resolvemos los disparos del player
                break;

            case NEXT_LEVEL:
                // Si pulso ESCAPE
                if (Keyboard.isPressed(KeyEvent.VK_ESCAPE)) {
                    gameRunning = false; //System.exit(0); // Salimos del juego
                }
                // Si pulso ENTER
                if (Keyboard.isPressed(KeyEvent.VK_ENTER)) {
                    screen = ScreenStatus.LEVEL2;     // Pantalla de juego                 
                    initStage(SPACE_LEVEL2);
                    moveShadow();
                    WavPlayer.play(COLISION);
                    playTheme(LEVEL2_THEME);
                }
                break;

            case LEVEL2:
                movementPlayer(); // Resolvemos el movimiento del player              
                tryToFire();    // Resolvemos los disparos del player
                break;

            case NEXT_LEVELFINAL:
                // Si pulso ESCAPE
                if (Keyboard.isPressed(KeyEvent.VK_ESCAPE)) {
                    gameRunning = false; //System.exit(0); // Salimos del juego
                }
                // Si pulso ENTER
                if (Keyboard.isPressed(KeyEvent.VK_ENTER)) {
                    screen = ScreenStatus.FINAL;     // Pantalla de juego                 
                    initStage(SPACE_FINAL);
                    moveShadow();
                    WavPlayer.play(COLISION);
                    playTheme(LEVEL2_THEME);
                }
                break;

            case FINAL:
                movementPlayer(); // Resolvemos el movimiento del player              
                tryToFire();    // Resolvemos los disparos del player
                break;

            // Pantalla GAMEOVER    
            case GAME_OVER:
                sleep(500);
                // Si se pulso la tecla ESCAPE
                if (Keyboard.isPressed(KeyEvent.VK_ESCAPE)) {
                    System.exit(0); // Salimos del juego
                }
                // Si se pulso la tecla enter
                if (Keyboard.isPressed(KeyEvent.VK_SPACE)) {
                    startGame(); // iniciamos el juego
                    WavPlayer.stop(GANADO);
                    WavPlayer.stop(PERDIDO);
                }
        }
    }

    /**
     * Resuelve el movimiento del Player
     */
    private void movementPlayer() {
        // Movimiento de la nave
        player.setVerticalMovement(0);
        player.setHorizontalMovement(0);

        if (player.getY() > 320) {

            player.setSpriteImages(spritesPlayer);

            if (Keyboard.isPressed(KeyEvent.VK_LEFT)) {
                player.setHorizontalMovement(-PLAYER_SPEED);
                player.setFrameDuration(5);
                player.setSpriteImages(spritesPlayerLeft);
            }
            if (Keyboard.isPressed(KeyEvent.VK_RIGHT)) {
                player.setHorizontalMovement(PLAYER_SPEED);
                player.setFrameDuration(5);
                player.setSpriteImages(spritesPlayerRight);
            }
            if (Keyboard.isPressed(KeyEvent.VK_UP)) {
                player.setVerticalMovement(-PLAYER_SPEED);
                player.setFrameDuration(DURATION_FRAMES_PLAYER);
                player.setSpriteImages(spritesPlayer);

            }
            if (Keyboard.isPressed(KeyEvent.VK_DOWN)) {
                player.setVerticalMovement(PLAYER_SPEED);
                player.setFrameDuration(DURATION_FRAMES_PLAYER);
                player.setSpriteImages(spritesPlayer);
            }
        } else {
            player.setSpriteImages(spritesPlayerCohete);
            if (Keyboard.isPressed(KeyEvent.VK_LEFT)) {
                player.setHorizontalMovement(-PLAYER_SPEED);
                player.setFrameDuration(20);
                player.setSpriteImages(spritesPlayerCoheteLeft);
            }
            if (Keyboard.isPressed(KeyEvent.VK_RIGHT)) {
                player.setHorizontalMovement(PLAYER_SPEED);
                player.setFrameDuration(20);
                player.setSpriteImages(spritesPlayerCoheteRight);
            }
            if (Keyboard.isPressed(KeyEvent.VK_UP)) {
                player.setVerticalMovement(-PLAYER_SPEED);
                player.setFrameDuration(5);
                player.setSpriteImages(spritesPlayerCohete);

            }
            if (Keyboard.isPressed(KeyEvent.VK_DOWN)) {
                player.setVerticalMovement(PLAYER_SPEED);
                player.setFrameDuration(5);
                player.setSpriteImages(spritesPlayerCohete);
            }
        }
    }

    /**
     * Resuelve el intento de disparo por parte del player
     */
    private void tryToFire() {
        if (Keyboard.isPressed(KeyEvent.VK_SPACE)) {
            player.fireMissile();
            Keyboard.setPressed(KeyEvent.VK_SPACE, false);
        }
        if (Keyboard.isPressed(KeyEvent.VK_B)) {
            player.fireCluster();
            Keyboard.setPressed(KeyEvent.VK_B, false);
        }
    }

    /**
     * **********************************************************************
     * METODO PRINCIPAL DEL JUEGO. El punto de entrada al juego.
     *
     * @param args
     * ************************************************************************
     */
    public static void main(String[] args) {
        ShadowNight inv = new ShadowNight();
        // Ejecuta el bucle principal del juego
        inv.loopGame();
    }

    private void playTheme(String theme) {
        MP3Player.stop();
        MP3Player.play(theme);
    }
}
