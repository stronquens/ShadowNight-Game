package net.ausiasmarch.ShadowNight.util;

/**
 * ImageUtils.java. Una clase usada para la creacion y el tratamiento de
 * imagenes.
 *
 * @author Luis Mateo
 */
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageUtils {

    private static int widthImage;
    private static int heightImage;

    /**
     * Constructor privado que impide crear objetos de esta clase
     */
    private ImageUtils() {
    }

    /**
     * Obtiene un objeto Image a partir de un Object URL
     */
    public static Image getImageFromURL(URL url) {
        return Toolkit.getDefaultToolkit().getImage(url);
    }

    /**
     * Obtiene un objeto Image a partir del nombre de la imagen
     */
    public static Image getImageFromString(String name) {
        return Toolkit.getDefaultToolkit().getImage(name);
    }

    /**
     * Carga un BufferedImage a partir del nombre de la imagen
     */
    public static BufferedImage loadImage(String nombre) {
        URL url;
        StringBuilder sb = new StringBuilder();
        try {
            url = ImageUtils.class.getClassLoader().getResource(nombre);
            return ImageIO.read(url);
        } catch (Exception e) {
            sb.append("No se pudo cargar la imagen ");
            sb.append(nombre);
            sb.append("\nError: ");
            sb.append(e.getClass().getName());
            sb.append(" - ");
            sb.append(e.getMessage());
            System.out.println(sb.toString());
            return null;
        }
    }

    /**
     * Carga un BufferedImage a partir de la url de la imagen
     */
    public static BufferedImage loadImage(URL url) {
        StringBuilder sb = new StringBuilder();
        try {
            return ImageIO.read(url);
        } catch (Exception e) {
            sb.append("No se pudo cargar la imagen ");
            sb.append(url);
            sb.append("\nError: ");
            sb.append(e.getClass().getName());
            sb.append(" - ");
            sb.append(e.getMessage());
            return null;
        }
    }

    /**
     * Convierte un objeto Image a un BufferedImage
     *
     * @param img El objeto Image a convertir
     * @return El BufferedImage obtenido
     */
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        // Crea un buffered image con trasparencia
        BufferedImage bimage = new BufferedImage(img.getWidth(null),
                img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Dibuja la imagen en el buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        // Retorna el buffered image
        return bimage;
    }

    /**
     * Convierte un BufferedImage a Image
     *
     * @param bimage El BufferedImage a convertir
     * @return El objeto Image obtenido
     */
    public static Image toImage(BufferedImage bimage) {
        // Casting para convertir de BufferedImage a Image
        Image img = (Image) bimage;
        return img;
    }

    /**
     * Convierte un objeto Image en un objeto ImageIcon
     */
    public static ImageIcon parseImageIcon(Image image) {
        return new ImageIcon(image);
    }

    /**
     * Convierte un objeto ImageIcon en un objeto Image
     */
    public static Image parseImage(ImageIcon icon) {
        return icon.getImage();
    }

    /**
     * Redimensiona un objeto image a una anchura y altura determinada
     *
     * @param img El objeto Image a redimensionar
     * @param width La nueva anchura
     * @param height La nueva altura
     * @return El objeto imagen redimensionado
     */
    public static BufferedImage resize(Image img, int width, int height){
        // Resize into a BufferedImage
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimg.createGraphics();
        bGr.drawImage(img, 0, 0, width, height, null);
        bGr.dispose();
        return bimg;
    }

    /**
     * Redimensiona las imagenes de una lista de imagenes
     *
     * @param list La lista de imagenes
     * @param width El ancho
     * @param height El alto
     * @return La lista de imagenes redimensionadas
     */
    public static List<BufferedImage> resize(List<Image> list,int width, int height) {
        List<BufferedImage> imagesList = new ArrayList<>();
        for (Image image : list) {
            imagesList.add(resize(image, width, height));
        }
        return imagesList;
    }

    /**
     * Redimensiona una imagen para ajustarla a un contenedor
     *
     * @param bufferedImage la imagen que se desea redimensionar
     * @param newW el nuevo ancho que se desea redimensionar
     * @param newH el nuevo alto que se desea redimensionar
     *
     * @return BufferedImage redimensionada
     */
    public static BufferedImage resizeToContainer(BufferedImage imagen, int wCont, int hCont) {
        widthImage = new ImageIcon(imagen).getIconWidth();
        heightImage = new ImageIcon(imagen).getIconHeight();
        double scaleW, scaleH;    // escala por defecto 1:1

        // Escalado de la imagen
        if (widthImage > wCont && heightImage > hCont) {
            scaleH = (hCont * 1.0) / heightImage;
            scaleW = scaleH;
            imagen = getScaledImage(imagen, scaleW, scaleH);
            setImageDimension(imagen);
        }

        if (widthImage >= wCont && heightImage <= hCont) {
            scaleW = (wCont * 1.0) / widthImage;
            scaleH = scaleW;
            imagen = getScaledImage(imagen, scaleW, scaleH);
            setImageDimension(imagen);
        }

        if (widthImage <= wCont && heightImage >= hCont) {
            scaleH = (hCont * 1.0) / heightImage;
            scaleW = scaleH;
            imagen = getScaledImage(imagen, scaleW, scaleH);
            setImageDimension(imagen);
        }

        return imagen;
    }

    /**
     * Crea un 'tiled image' con un objeto image con una anchura y altura
     *
     * @param img El objeto Image fuente
     * @param width El ancho de la imagen a crear
     * @param height La altura de la imagen a crear
     * @return La imagen creada
     */
    public static Image createTiledImage(Image img, int width, int height) {
        // Crea un objeto Image null
        Image image = null;
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // El ancho y alto de la imagen
        int imageWidth = img.getWidth(null);
        int imageHeight = img.getHeight(null);
        // Inicia los contadores para x e y
        int numX = (width / imageWidth) + 2;
        int numY = (height / imageHeight) + 2;
        // Crea el contexto grafico
        Graphics2D bGr = bimg.createGraphics();
        for (int y = 0; y < numY; y++) {
            for (int x = 0; x < numX; x++) {
                bGr.drawImage(img, x * imageWidth, y * imageHeight, null);
            }
        }
        // Convierte y retorna la imagen
        image = toImage(bimg);
        return image;
    }

    /**
     * Crea una imagen vacia con transparencia
     *
     * @param width El ancho de la nueva imagen
     * @param height El alto de la nueva imagen
     * @return la nueva imagen
     */
    public static Image getEmptyImage(int width, int height) {
        BufferedImage img = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        return toImage(img);
    }

    /**
     * Obtiene una imagen coloreada con un color especifico
     *
     * @param color El color de relleno
     * @param width El ancho
     * @param height El alto
     * @return La imagen creada
     */
    public static Image getColoredImage(Color color, int width, int height) {
        BufferedImage img = toBufferedImage(getEmptyImage(width, height));
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.dispose();
        return img;
    }

    /**
     * Clona una imagen. Desspues de la clanacion, una copia es retornada.
     *
     * @param img La imagen a clonar
     * @return El clon de la imagen
     */
    public static Image clone(Image img) {
        BufferedImage bimg = toBufferedImage(getEmptyImage(img.getWidth(null),
                img.getHeight(null)));
        Graphics2D g = bimg.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return toImage(bimg);
    }

    /**
     * Rota una imagen.
     *
     * @param img La imagen a rotar
     * @param angle El angulo en grados
     * @return The rotated image
     */
    public static Image rotate(Image img, double angle) {
        double sin = Math.abs(Math.sin(Math.toRadians(angle))),
                cos = Math.abs(Math.cos(Math.toRadians(angle)));
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h
                * cos + w * sin);
        BufferedImage bimg = toBufferedImage(getEmptyImage(neww, newh));
        Graphics2D g = bimg.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(Math.toRadians(angle), w / 2, h / 2);
        g.drawRenderedImage(toBufferedImage(img), null);
        g.dispose();
        return toImage(bimg);
    }

    /**
     * Rota las imagenes de una lista de imagenes
     *
     * @param list La lista de imagenes
     * @param angle El angulo de rotacion
     * @return La lista de imagenes rotadas
     */
    public static List<Image> rotate(List<Image> list, double angle) {
        List<Image> imagesList = new ArrayList<>();
        for (Image image : list) {
            imagesList.add(rotate(image, angle));
        }
        return imagesList;
    }

    /**
     * Pone color en una imagen transparente.
     */
    public static Image mask(Image img, Color color) {
        BufferedImage bimg = toBufferedImage(getEmptyImage(img.getWidth(null),
                img.getHeight(null)));
        Graphics2D g = bimg.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        for (int y = 0; y < bimg.getHeight(); y++) {
            for (int x = 0; x < bimg.getWidth(); x++) {
                int col = bimg.getRGB(x, y);
                if (col == color.getRGB()) {
                    bimg.setRGB(x, y, col & 0x00ffffff);
                }
            }
        }
        return toImage(bimg);
    }

    /**
     * Establece las dimensiones de una imagen
     *
     * @param imagen
     */
    private static void setImageDimension(Image imagen) {
        widthImage = new ImageIcon(imagen).getIconWidth();
        heightImage = new ImageIcon(imagen).getIconHeight();
    }

    /**
     * Obtiene una imagen escalada
     */
    public static BufferedImage getScaledImage(Image imagen, double scaleW, double scaleH) {

        BufferedImage bi = toBufferedImage(imagen);

        int w = (int) (bi.getWidth() * scaleW);
        int h = (int) (bi.getHeight() * scaleH);

        bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = bi.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        AffineTransform at = AffineTransform.getScaleInstance(scaleW, scaleH);
        g2.drawRenderedImage(toBufferedImage(imagen), at);
        g2.dispose();
        return bi;
    }

    /**
     * Crea una imagen en color de ancho x alto
     */
    public static Image createImageColor(int ancho, int alto, int color) {
        BufferedImage bufferedImage = new BufferedImage(
                ancho, alto, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {
                bufferedImage.setRGB(i, j, color);
            }
        }
        return bufferedImage;
    }

    /**
     * Crea una nueva imagen transparente de ancho x alto
     */
    public static BufferedImage createTranslucentImage(int ancho, int alto) {
        BufferedImage bi = new BufferedImage(
                ancho, alto, BufferedImage.TRANSLUCENT);
        return bi;
    }

    /**
     * Le aplica la transparencia seleccionada a una BufferedImage
     *
     * @param bufferedImage la imagen que se desea hacer transparente algun
     * color
     * @param transparency variable tipo float entre el rango 0.0 - 1.0 que
     * indica el porcentaje de transparencia
     *
     * @return BufferedImage con el porcentaje de transparencia seleccionada
     */
    public static BufferedImage makeTranslucentImage(BufferedImage image,
            float transparency) {
        BufferedImage bi = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics2D g = bi.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                transparency));
        g.drawImage(image, null, 0, 0);
        g.dispose();
        return bi;
    }

    /**
     * Hace que el color seleccionado sea transparente en un BufferedImage
     *
     * @param bufferedImage la imagen que se desea hacer transparente
     * @param color el color que se desea hacer transparente
     *
     * @return BufferedImage con el color seleccionado transparente
     */
    public static BufferedImage makeColorTransparent(BufferedImage image,
            Color color) {
        BufferedImage bi = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(image, null, 0, 0);
        g.dispose();
        for (int i = 0; i < bi.getHeight(); i++) {
            for (int j = 0; j < bi.getWidth(); j++) {
                if (bi.getRGB(j, i) == color.getRGB()) {
                    bi.setRGB(j, i, 0x8F1C1C);
                }
            }
        }
        return bi;
    }

    /**
     * Divide una Imagen en filas x columnas del mismo tamaño
     */
    public static List<BufferedImage> divideImage(Object obj, int rows, int cols) {
        BufferedImage bi;

        if (obj instanceof Image) {
            bi = toBufferedImage((Image) obj);
        } else if (obj instanceof ImageIcon) {
            bi = toBufferedImage(((ImageIcon) obj).getImage());
        } else {
            bi = (BufferedImage) obj;
        }

        int altoBi = bi.getHeight() / rows;
        int anchoBi = bi.getWidth() / cols;

        List<BufferedImage> imagenes = new ArrayList<>();
        BufferedImage subImagen;

        for (int f = 0; f < rows; f++) {
            for (int c = 0; c < cols; c++) {
                subImagen = bi.getSubimage(c * anchoBi, f * altoBi,
                        anchoBi, altoBi);
                imagenes.add(subImagen);
            }
        }

        return imagenes;
    }

    /**
     * Divide una Imagen en filas x columnas del mismo tamaño
     */
    public static List<BufferedImage> divideImage(BufferedImage bi, int rows, int cols,
            int width, int height) {
        int x, y;
        List<BufferedImage> imagenes = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                x = j * width;
                y = i * height;
                imagenes.add(bi.getSubimage(x, y, width, height));
            }
        }

        return imagenes;
    }

    /**
     * Gira una imagen en sentido vertival
     *
     * @param bufferedImage la imagen que se desea girar
     *
     * @return BufferedImage es la imagen girada
     */
    public static BufferedImage flipVertical(BufferedImage bufferedImage) {
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();
        BufferedImage bi = new BufferedImage(w, h,
                bufferedImage.getColorModel().getTransparency());
        Graphics2D g = bi.createGraphics();
        g.drawImage(bufferedImage, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return bi;
    }

    /**
     * Gira una imagen en sentido horizontal
     *
     * @param bufferedImage la imagen que se desea girar
     *
     * @return BufferedImage es la imagen girada
     */
    public static BufferedImage flipHorizontal(BufferedImage bufferedImage) {
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();
        BufferedImage bi = new BufferedImage(w, h, bufferedImage.getType());
        Graphics2D g = bi.createGraphics();
        g.drawImage(bufferedImage, 0, 0, w, h, w, 0, 0, h, null);
        g.dispose();
        return bi;
    }

    /**
     * Convierte un lista de Image en una lista de BufferedImage
     */
    public static List<BufferedImage> getListBufferedImage(List<Image> list) {
        List<BufferedImage> bListImages = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            bListImages.add(toBufferedImage(list.get(i)));
        }
        return bListImages;
    }

    /**
     * Convierte un lista de BufferdImage en una lista de Image
     */
    public static List<Image> getListImage(List<BufferedImage> list) {
        List<Image> listImages = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listImages.add(toImage(list.get(i)));
        }
        return listImages;
    }

    /**
     * Obtiene el ancho de una Image
     */
    public static int getWidth(Image imagen) {
        int width = new ImageIcon(imagen).getIconWidth();
        return width;
    }

    /**
     * Obtiene el alto de una Image
     */
    public static int getHeight(Image imagen) {
        int height = new ImageIcon(imagen).getIconHeight();
        return height;
    }

    /**
     * Obtiene el ancho de un IconImage
     */
    public static int getWidth(ImageIcon imagen) {
        return imagen.getIconWidth();
    }

    /**
     * Obtiene el alto de un IconImage
     */
    public static int getHeight(ImageIcon imagen) {
        return imagen.getIconHeight();
    }

    /**
     * Convierte una imagen a escala de grises (blanco y negro)
     */
    public static BufferedImage convertToGrayscale(BufferedImage source) {

        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        BufferedImage imagenGris = op.filter(source, null);
        return (imagenGris);
    }

    /**
     * Obtiene una imagen a partir de un objeto InputStream
     */
    public static BufferedImage getImagenFromInputStream(InputStream ie) {
        try {
            BufferedImage bi = ImageIO.read(ie);
            return bi;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene un objeto InputStream a partir de un objeto Image
     */
    public static InputStream getInputStreamFromImage(Image imagen, String format) {

        BufferedImage bi = toBufferedImage(imagen);

        ByteArrayOutputStream buffer_img = new ByteArrayOutputStream();

        try {
            ImageIO.write(bi, format, buffer_img);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        byte[] bytes = buffer_img.toByteArray();
        return new ByteArrayInputStream(bytes);

    }

    /**
     * Muestra una imajen en un JLabel
     *
     * @param imagen
     * @param jLabel
     * @param jc
     */
    public static void showImageLabel(BufferedImage imagen, JLabel jLabel, JComponent jc) {
        BufferedImage imagenReducida = resize(imagen, jc.getWidth(), jc.getHeight());
        ImageIcon icon = new ImageIcon(imagenReducida);

        jLabel.setSize(icon.getIconWidth(), icon.getIconHeight());
        jLabel.setIcon(icon);
        jLabel.setLocation(jc.getWidth() / 2 - jLabel.getWidth() / 2,
                jc.getHeight() / 2 - jLabel.getHeight() / 2);
    }

    /**
     * Pone borde a una imegen
     *
     * @param imagen
     * @return
     */
    public static Image getImageWithBorder(Image imagen) {
        int w = getWidth(imagen);
        int h = getHeight(imagen);

        Graphics g = imagen.getGraphics();
        g.setColor(Color.WHITE);
        //lado superior
        int[] xpointsT = {0, w, w - 1, 1};
        int[] ypointsT = {0, 0, 1, 1};
        g.fillPolygon(xpointsT, ypointsT, 4);
        //lado izquierdo
        int[] xpointsL = {0, 0, 1, 1};
        int[] ypointsL = {0, h, h - 1, 1};
        g.fillPolygon(xpointsL, ypointsL, 4);
        //lado inferior
        g.setColor(Color.DARK_GRAY);
        int[] xpointsB = {0, w, w - 1, 1};
        int[] ypointsB = {h, h, h - 1, h - 1};
        g.fillPolygon(xpointsB, ypointsB, 4);
        //lado derecho
        int[] xpointsR = {w, w, w - 1, w - 1};
        int[] ypointsR = {0, h, h - 1, 1};
        g.fillPolygon(xpointsR, ypointsR, 4);

        return imagen;
    }

    /**
     * Obtiene una lista de nombres de ficheros de imagenes numerados
     *
     * @param name nombre origen del archivo de imagen
     * @param frames numero de imagenes
     * @param ext extension del archivo de imagen
     * @return
     */
    public static List<String> getImagesNames(String name, int m, String ext) {
        List<String> imageNameList = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            imageNameList.add(name + i + ext);
        }
        return imageNameList;
    }

    /**
     * Obtiene una lista de nombres de ficheros de imagenes numerados
     *
     * @param name nombre origen del archivo de imagen
     * @param frames numero de imagenes
     * @param ext extension del archivo de imagen
     * @return
     */
    public static List<String> getImagesNames(String ref, int m) {
        List<String> imageNameList = new ArrayList<>();

        String fileName = ref;  // Referencia completa al fichero de imagen            

        // Obtiene la posicion del . antes de la extension
        int n = fileName.indexOf(".");

        int pos;

        // Si n es distinto de cero, se encontro el .
        if (n != 0) {
            pos = n;
        } else {
            pos = fileName.length();
        }

        // Crea una lista con de nombres de ficheros de imagenes
        for (int i = 0; i < m; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(fileName);
            imageNameList.add(sb.insert(pos, i).toString());
        }

        return imageNameList;  // devuelve la lista
    }

    /**
     * Crea un imagen compatinble con otra imagen.
     *
     * @param sourceImage un BufferedImage
     * @return Image
     */
    public static BufferedImage createCompatibleImage(int width, int height,
            int transparency) {
        GraphicsConfiguration gc =
                GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().getDefaultConfiguration();

        BufferedImage imageComp = gc.createCompatibleImage(width, height,
                transparency);
        return imageComp;
    }

    /**
     * Obtiene una lista de listas con los nombres de ficheros de imagenes
     * numeradas
     *
     * @param l lista de ficheros de imagen
     * @param m numero de imagenes de la lista
     * @return
     */
    public static List<List<String>> getListOfListNames(List<String> l, int m) {
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {
            list.add(ImageUtils.getImagesNames(l.get(i), m));
        }
        return list;
    }

    /**
     * Obtiene una lista de listas con los nombres de ficheros de imagenes
     * numeradas
     *
     * @param l lista con los nombes de ficheros de imagen
     * @param m Lista con los numeros de imagenes de la lista
     * @return
     */
    public static List<List<String>> getListOfListNames(List<String> l, List<Integer> m) {
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {
            list.add(ImageUtils.getImagesNames(l.get(i), m.get(i)));
        }
        return list;
    }

    /**
     * Aplica un filtro de color a una imagen, cambiando un color 1 por otro
     * color 2
     *
     * @param imagen
     * @param color1
     * @param color2
     * @param tolerancia
     * @return
     */
    public BufferedImage aplicarFiltro(BufferedImage imagen, Color color1,
            Color color2, int tolerancia) {
        int red, green, blue;
        BufferedImage bi = null;

        if (imagen != null) {
            bi = new BufferedImage(imagen.getWidth(), imagen.getHeight(),
                    imagen.getType());

            Color colorImagen;
            red = color1.getRed();
            green = color1.getGreen();
            blue = color1.getBlue();

            for (int i = 0; i < imagen.getWidth(); i++) {
                for (int j = 0; j < imagen.getHeight(); j++) {
                    colorImagen = new Color(imagen.getRGB(i, j));
                    if (Math.pow(red - colorImagen.getRed(), 2)
                            + Math.pow(green - colorImagen.getGreen(), 2)
                            + Math.pow(blue - colorImagen.getBlue(), 2)
                            <= Math.pow(tolerancia, 2)) {
                        colorImagen = new Color(color2.getRed(),
                                color2.getGreen(), color2.getBlue());

                        bi.setRGB(i, j, colorImagen.getRGB());
                    }
                }
            }

        }
        return bi;
    }
}