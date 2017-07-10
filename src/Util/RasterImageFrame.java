package Util;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

/**
 * This frame shows an image with a Mandelbrot set.
 */
public class RasterImageFrame extends JFrame {

    public RasterImageFrame(int width, int height, Color[][] data)
    {
        BufferedImage image = makeMandelbrot(width, height, data);
        add(new JLabel(new ImageIcon(image)));
        pack();
    }

    /**
     * Makes the Mandelbrot image.
     * @param width the width
     * @parah height the height
     * @return the image
     */
    public BufferedImage makeMandelbrot(int width, int height, Color[][] data)
    {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = image.getRaster();
        ColorModel model = image.getColorModel();

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                int argb = data[i][j].getRGB();
                Object colorData = model.getDataElements(argb, null);
                raster.setDataElements(i, j, colorData);
            }
        return image;
    }
}
