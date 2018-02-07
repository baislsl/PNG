package com.baislsl.png;

import com.baislsl.png.decode.Decoder;
import com.baislsl.png.decode.PNG;
import com.baislsl.png.util.CRC;
import com.baislsl.png.ui.PngImageFrame;

import javax.swing.*;
import java.awt.*;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;

import static com.baislsl.png.util.LZ77.*;

/**
 * Created by baislsl on 17-7-9.
 */
public class Test {
    public static void main(String[] args){

        try{
            for(int i=1;i<=2;i++){
                Decoder decoder = new Decoder("/home/baislsl/java/png-decode/src/main/resources/test" + i +".png");
                PNG png = decoder.readInPNG();
                Color[][] colors = png.getColor();
                JFrame frame = new PngImageFrame((int) png.getWidth(), (int) png.getHeight(), colors);
                frame.setTitle("PNG");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

                // png.save("testOutput.png");
            }

            // CRCtest();
            // LZ77Test();
            // Decoder.readInPNG("./src/com.baislsl.png.test/com.baislsl.png.test.png");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void LZ77Test(){
        byte[] data = {
                0x23, 0x45, 0x67, 0x78,
        };

        byte[] encode = compress(data);
        byte[] decode = null;
        try{
            decode = uncompress(encode);
            for(byte b : decode){
                System.out.print(Integer.toHexString((int)b) + " ");
            }
            System.out.println();
        }catch (DataFormatException e){
            e.printStackTrace();
        }

        for(byte b : decode){
            System.out.print(Integer.toHexString((int)b) + " ");
        }

    }



    public static void CRCtest(){
        byte[] data = {
                0x49, 0x45, 0x4e, 0x44
        };

        long result = CRC.crc(data, data.length);
        System.out.println(Long.toHexString(result));

        // use the crc32 algorithm java provide
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        result = crc32.getValue();
        System.out.println(Long.toHexString(result));
    }

}
