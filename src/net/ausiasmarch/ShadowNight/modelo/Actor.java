package net.ausiasmarch.ShadowNight.modelo;

/**
 * ----------------------------------------------------------------------
 * Acto.java Encapsula las caracteristicas generales de cualquier actor o
 * personaje del juego
 *
 * @author Armando Maya y Jose M Coronado
 * ------------------------------------------------------------------------
 */
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import net.ausiasmarch.ShadowNight.sound.WavPlayer;
import net.ausiasmarch.ShadowNight.util.ImageUtils;

public abstract class Actor {

    protected float x, y;              // Coordenadas x e y en pixels
    protected float posX, posY;         // Posicion x e y interpoladas
    protected int vx, vy;               // velocidad  
    protected int width, height;        // Anchura y altura
    protected List<String> spriteNames; // Lista de nombres de sprite
    protected Rectangle stage;          // Escenario de juego
    protected SpriteCache spriteCache;  // Sprite cache
    protected GameWindow window;        // Ventana de juego
    protected long frameDuration;    // Duración del frame en ms
    protected long lastFrameChange;  // Tiempo ultimo frame cambiado en ms
    protected int frameNumber;       // Numero de frame
    protected int currentFrame;      // Actual frame
    protected float translucent;     // Porcentaje de tranlucidez 
    // Lista de BufferedImage de los sprites 
    protected List<BufferedImage> spriteImages = new ArrayList<>();
    private Rectangle me = new Rectangle();  // Rectangle de este actor
    private Rectangle him = new Rectangle(); // Rectangle de otro actor

    /**
     * Contructor
     *
     * @param gw
     */
    public Actor(List<String> spriteNames) {
        this.spriteNames = spriteNames;
        spriteCache = SpriteCache.get();
        currentFrame = 0;
        frameNumber = 0;
        translucent = 1.0f;                  // Por defecto es opaco
        createSpriteImageList(spriteNames);             // Crea la lista de imagenes   
    }

    /**
     * Crea una lista de imagenes (BufferedImage) de los sprites
     */
    private void createSpriteImageList(List<String> spriteNames) {
        spriteImages = new ArrayList<>();
        //currentFrame = frameNumber = 0;
        for (String s : spriteNames) {
            BufferedImage image = spriteCache.getSprite(s);
            spriteImages.add(image);
        }
        // Misma andchura y altura para todos los sprits  
        width = spriteImages.get(0).getWidth();
        height = spriteImages.get(0).getHeight();
    }

    public void setSpriteImages(List<String> spriteNames) {
        createSpriteImageList(spriteNames);
    }

    public void setSpriteAnimation(List<String> spriteNames) {
        this.spriteNames = spriteNames;
    }

    /**
     * Obtiene la lista de imagenes de sprites
     *
     * @return List<BufferedImage> Lista de imagenes
     */
    public List<BufferedImage> getSpriteImages() {
        return spriteImages;
    }

    /**
     * Obtiene la coordenada X
     *
     * @return double
     */
    public float getX() {
        return x;
    }

    /**
     * Pone el valor de la coordenada X
     *
     * @params x coordenada X
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Obtiene la coordenada Y
     *
     * @return double
     */
    public float getY() {
        return y;
    }

    /**
     * Pone el valor de la coordenada Y
     *
     * @params y coordenada Y
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Obtiene la altura
     *
     * @return int
     */
    public int getHeight() {
        return height;
    }

    /**
     * Pone el valor de la altura
     *
     * @params height altura
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Obtiene la anchura
     *
     * @return int
     */
    public int getWidth() {
        return width;
    }

    /**
     * Pone el valor de la anchura
     *
     * @params width anchura
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Obtiene la velocidad en eje X
     *
     * @return vx
     */
    public int getVx() {
        return vx;
    }

    /**
     * Pone el valor de la velocidad en eje X
     *
     * @param vx
     */
    public void setVx(int i) {
        vx = i;
    }

    /**
     * Obtiene la velocidad en eje Y
     *
     * @return vy
     */
    public int getVy() {
        return vy;
    }

    /**
     * Pone el valor de la velocidad en eje Y
     *
     * @param vy
     */
    public void setVy(int vy) {
        this.vy = vy;
    }

    /**
     * Establece el escenario (ractangulo) de juego
     *
     * @params stage
     */
    public void setStage(Rectangle stage) {
        this.stage = stage;
    }

    /**
     * Pone el valor del porcentaje de translucidez
     *
     * @param tranlucent
     */
    public void setTranslucent(float translucent) {
        this.translucent = translucent;
    }

    /**
     * Obtiene el frame actual
     *
     * @params int
     */
    public int getFrameNumber() {
        return frameNumber;
    }

    /**
     * Obtiene la lista de nombres de sprite
     *
     * @return String
     */
    public List<String> getSpriteNames() {
        return spriteNames;
    }

    /**
     * Pone el la duracion de un frame
     *
     * @params frameDuration duracion
     */
    public void setFrameDuration(int frameDuration) {
        this.frameDuration = frameDuration;
    }

    public void insertSprites(int first, int n) {
        List<BufferedImage> images = new ArrayList<>();
        int j = 1;
        for (int i = first; i < spriteImages.size(); i++) {
            images.add(spriteImages.get(i));
            if (j <= n) {
                BufferedImage img = ImageUtils.createTranslucentImage(width, height);
                images.add(img);
                j++;
            }
        }
        spriteImages = images;
    }

    /**
     * Actualiza lo sprites
     *
     * @param first
     * @param n
     */
    public synchronized void updateSprites(int first, int n) {
        float transl = 0.9f;
        float value = translucent / n;
        for (int i = first; i < n; i++) {
            BufferedImage img = spriteImages.get(i);
            img = ImageUtils.makeTranslucentImage(img, transl);
            spriteImages.set(i, img);
            transl = transl - value;
        }

    }

    /**
     * Dibuja este actor en el contexto gtafico previsto
     *
     * @param g
     */
    public void draw(Graphics2D g) {
        //Obtiene la imagen del sprite con el frameNumber especificado;
        // System.out.println(frameNumber);
        BufferedImage image = spriteImages.get(frameNumber);
        // Obtiene una imagen translucida de la imagen
        image = ImageUtils.makeTranslucentImage(image, translucent);
        g.drawImage(image, (int) x, (int) y, null);
    }

    /**
     * Establece la logica (la accion realizada) asociada con este actor. Este
     * método Se llamará periódicamente en base a los eventos del juego
     *
     * @param dt tiempo trnascurrido en milisegundos
     */
    public synchronized void act(long dt) {
        lastFrameChange += dt;  // tiempo del el ultimo frame cambiado en ms
        if (lastFrameChange > frameDuration) {
            lastFrameChange = 0;
            frameNumber++;
            if (frameNumber >= spriteImages.size()) {
                frameNumber = 0;
            }
        }
    }

    /**
     * Mueve este actor a las coordenadas x,y en pixels/seg
     *
     * @param dt tiempo trnascurrido en milisegundos
     */
    public void move(float dt) {
        x += (dt * vx) / 1000;
        y += (dt * vy) / 1000;
    }
    /* Mueve este actor a las coordenadas interpoladas x,y con una vlocidad en pixels/seg y durante 
     * un determinado intervalo de tiempo dt de en ms
     *
     * @param interpolacion para obtener las coord. x e y de visualizacion
     * @param dt intervalo de tiempo en milisegundos invertido en el movimiento del actor
     */

    public synchronized void interpolate(float interpolation, long dt) {
        posX = x + ((vx * dt / 1000) * interpolation);
        posY = y + ((vy * dt / 1000) * interpolation);
    }

    /**
     * Comprueba si este Actor colisiona con otro usando colision por
     * rectangulos
     *
     * @param other El otro actor con el que comprobar la cosision
     * @return True si este actor colisiona con otro
     */
    public boolean collidesWith(Actor other) {
        me.setBounds((int) x, (int) y, width, height);
        him.setBounds((int) other.x, (int) other.y, other.width, other.height);
        return me.intersects(him);
    }

    /**
     * Comprueba si este Actor colisiona con otro usando colision por pixel
     *
     * @param other El otro actor con el que comprobar la cosision
     * @return True si este actor colisiona con otro
     */
    public boolean pixelCollidesWith(Actor other) {
        me.setBounds((int) x, (int) y, width, height);
        him.setBounds((int) other.x, (int) other.y, other.width, other.height);

        // Listas de imagenes de los frames de cada actor
        List<BufferedImage> lThis = getSpriteImages();
        List<BufferedImage> lOther = other.getSpriteImages();

        // Si hay colision por rectangulo
        if (me.intersects(him)) {
            // Obtiene la imagen del frame que se esta montrando actualmente de cada actor
            BufferedImage img1 = lThis.get(getFrameNumber());

            BufferedImage img2 = lOther.get(other.getFrameNumber());
            // True si hay colision por pixel entre las imagenes de los frames actuales de 
            // los dos actores en las posiciones x e y en la que se encumntran
            return PixelCollision.isPixelCollide(x, y, img1, other.x, other.y, img2);
        }

        return false;
    }

    /**
     * Detecta las colision de este actor con otro actor
     *
     * @param actor
     */
    public abstract void collision(Actor actor);

    public void playSound(String sound) {
        WavPlayer.play(sound);
    }

}
