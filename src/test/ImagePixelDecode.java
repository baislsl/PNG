package test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * Created by baislsl on 17-7-10.
 */
public class ImagePixelDecode {

    public static void main(String[] args) {
        try {
            // BufferedImage image = new BufferedImage("./src/test3/png")
            InputStream f = new FileInputStream("./src/test/test3.png");
            PrintWriter output = new PrintWriter("stdImageData.txt");
            BufferedImage image = ImageIO.read(f);
            Raster raster = image.getRaster();
            int width = image.getWidth(), height = image.getHeight();
            int[] rgba = new int[4];
            for (int i = 0; i < height; i++) {
                output.write("\n");
                for (int j = 0; j < width; j++) {
                    int[] colorInfo = raster.getPixel(j, i, rgba);
                    output.write("(" + colorInfo[0] + ", " + colorInfo[1] + ", " + colorInfo[2] + "), ");
                }
            }
            output.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
