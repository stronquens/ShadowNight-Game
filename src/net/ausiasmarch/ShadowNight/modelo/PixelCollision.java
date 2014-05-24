package net.ausiasmarch.ShadowNight.modelo;

import java.awt.image.BufferedImage;

/**
 * PixelCollision.java
 * Detecta las colisiones por pixel de dos imagenes
 * @author Armando Maya y Jose M Coronado
 */
public class PixelCollision {
     /**
     * Colision por pixel
     *
     * @param x1 la coordenada x del primer sprite
     * @param y1 la coordenada y del primer sprite
     * @param image1 imagen del frame del primer sprite
     * @param x2 la coordenada x del segundo sprite
     * @param y2 la coordenada y del segundo sprite
     * @param image2 imagen del frame del segundo sprite
     * @return True si este actor colisiona con otro
     */
    public static boolean isPixelCollide(float x1, float y1, BufferedImage image1,
            float x2, float y2, BufferedImage image2) {

        // inicializacion
        double width1 = x1 + image1.getWidth() - 1,
                height1 = y1 + image1.getHeight() - 1,
                width2 = x2 + image2.getWidth() - 1,
                height2 = y2 + image2.getHeight() - 1;

        int xstart = (int) Math.max(x1, x2),
                ystart = (int) Math.max(y1, y2),
                xend = (int) Math.min(width1, width2),
                yend = (int) Math.min(height1, height2);

        // Ancho y alto del rectangulo interseccion
        int toty = Math.abs(yend - ystart);
        int totx = Math.abs(xend - xstart);

        for (int n = 1; n < toty - 1; n++) {
            int ny = Math.abs(ystart - (int) y1) + n;
            int ny1 = Math.abs(ystart - (int) y2) + n;

            for (int m = 1; m < totx - 1; m++) {
                int nx = Math.abs(xstart - (int) x1) + m;
                int nx1 = Math.abs(xstart - (int) x2) + m;
                try {
                    if (((image1.getRGB(nx, ny) & 0xFF000000) != 0x00)
                            && ((image2.getRGB(nx1, ny1) & 0xFF000000) != 0x00)) {
                        return true;   // colisionan !!!!
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);

                }
            }
        }

        return false;
    }
}
