package com.baislsl.png.ui;

import com.baislsl.png.decode.DecodeException;
import com.baislsl.png.decode.PNG;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class PngImageFrame extends JFrame {

    public PngImageFrame(PNG png) throws DecodeException {
        this((int) png.getWidth(), (int) png.getHeight(), png.getColor());
    }

    public PngImageFrame(int width, int height, Color[][] data) {
        BufferedImage image = getImage(width, height, data);
        add(new JLabel(new ImageIcon(image)));
        pack();
    }

    public BufferedImage getImage(int width, int height, Color[][] data) {
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
