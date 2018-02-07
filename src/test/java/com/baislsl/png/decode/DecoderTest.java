package com.baislsl.png.decode;

import com.baislsl.png.ui.PngImageFrame;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.*;

public class DecoderTest {

    @Test
    public void readInPNG() throws Exception {
        for (int i = 1; i <= 7; i++) {
            Decoder decoder = new Decoder(DecoderTest.class.getResourceAsStream("/test" + i + ".png"));
            PNG png = decoder.readInPNG();
            Color[][] colors = png.getColor();
            JFrame frame = new PngImageFrame((int) png.getWidth(), (int) png.getHeight(), colors);
            frame.setTitle("PNG");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    }
}