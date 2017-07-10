package test;

import Decode.Decoder;
import Decode.PNG;
import Util.*;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static Util.LZ77.*;

/**
 * Created by baislsl on 17-7-9.
 */
public class Test {
    public static void main(String[] args){
        try{
            Decoder decoder = new Decoder("./src/test/test.png");
            PNG png = decoder.readInPNG();
            png.show();
            png.save("testOutput.png");
            // CRCtest();
            // LZ77Test();
            // Decoder.readInPNG("./src/test/test.png");
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
