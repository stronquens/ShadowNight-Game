package net.ausiasmarch.ShadowNight.util;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Keyborad.java Un sistema de dcontrol de teclado
 *
 * @author Luis Mateo
 */
public class Keyboard {
    /**
     * El estado de las teclas
     */
    private static boolean[] keys = new boolean[1024];

    /**
     * Initializa el keyboard handler
     */
    public static void init() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new KeyHandler(),
                AWTEvent.KEY_EVENT_MASK);
    }

    /**
     * Initializa el keyboard handler
     *
     * @param c The component that we will listen to
     */
    public static void init(Component c) {
        c.addKeyListener(new KeyHandler());
    }

    /**
     * Comprueba si una tecla fue pulsada
     *
     * @param key el código de la tecla a comprobar
     * @return True si la tecla fue pulsada
     */
    public static boolean isPressed(int key) {
        return keys[key];
    }

    /**
     * Establece el estado de la tecla
     *
     * @param key El código de tecla
     * @param pressed El estado actual de la tecla
     */
    public static void setPressed(int key, boolean pressed) {
        keys[key] = pressed;
    }

    /**
     *************************************************************************
     * Clase interna KeyHandler (Sistema de entrada por teclado). 
     * Los metodos de esta clase se usan para manejar entradas de teclado
     * del usuario.
     * ***********************************************************************
     */
    private static class KeyHandler extends KeyAdapter implements AWTEventListener {
        /**
         * Notifica que se ha pulsado una tecla
         *
         * @param e Detalles del evento
         */
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.isConsumed()) {
                return;
            }
            keys[e.getKeyCode()] = true;
        }

        /**
         * Notifica se ha dejedo de pulsar una tecla
         *
         * @param e Detalles del evento
         */
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.isConsumed()) {
                return;
            }

      //      KeyEvent nextPress = (KeyEvent) Toolkit.getDefaultToolkit().
      //              getSystemEventQueue().peekEvent(KeyEvent.KEY_PRESSED);
      //      if ((nextPress == null) || (nextPress.getWhen() != e.getWhen())) {
                keys[e.getKeyCode()] = false;
      //      }

        }

        /**
         * Notifica que un evento ha occurrido en el sistema de eventos AWT
         *
         * @param e Detalles del evento
         */
 
        @Override
        public void eventDispatched(AWTEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                keyPressed((KeyEvent) e);
            }
            if (e.getID() == KeyEvent.KEY_RELEASED) {
                keyReleased((KeyEvent) e);
            }
        }
      
    }
}
