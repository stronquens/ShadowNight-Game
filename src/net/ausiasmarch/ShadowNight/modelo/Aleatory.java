package net.ausiasmarch.ShadowNight.modelo;

import java.util.Random;

/**
 * -------------------------------------------------------------------------
 * Curso Básico de desarrollo de VideoJuegos en Java 2D
 * Aleatory.java
 * Genera valores aleatorios
 *--------------------------------------------------------------------------
 */
public class Aleatory {

    /**
     * Genera un numero entero aleatorio entre 0 y maxValue - 1
     *
     * @param maxValue El maximo valor posible a generar
     * @return El valor entero generado
     */
    public static int nextInt(int maxValue) {
       return (new Random(System.nanoTime())).nextInt(maxValue);
    }
    
    /**
     * Genera un numero entero aleatorio entre 0 y maxValue - 1
     *
     * @param maxValue El maximo valor posible a generar
     * @return El valor entero generado
     */
    public static int nextInt(int maxValue, boolean flag) {
        int valor = 0;
        if (flag == true )
           valor = (new Random(System.nanoTime())).nextInt(maxValue);
        else {
          do {
             valor = (new Random(System.nanoTime())).nextInt(maxValue);
          }while (valor == 0);
        }  
        return valor;
    }
    
    /**
     * Genera un numero entero aleatorio entre n y m (ambos inclusives)
     *
     * @param n El minimo valor posible a generar
     * @param m El maximo valor posible a generar
     * @return El valor entero generado
     */
    public static int nextInt(int n, int m) {
        return (int) Math.floor(Math.random() * (m - n + 1) + n);
    }
    
    /**
     * Genera un numero doble aleatorio entre 0.0 y 1.0 (exclusivo)
     * @return El valor entero generado
     */
    public static double nextDouble() {
        return Math.random();
    }
    
    /**
     * Genera un numero doble aleatorio entre n y m (ambos inclusives)
     * @return El valor entero generado
     */
    public static double nextDouble(double n, double m) {
        return  Math.random()*(m - n)+ n;
    }

    
    
}
