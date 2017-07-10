package test;

import Decode.Decoder;
import Util.*;

import java.io.ByteArrayOutputStream;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Created by baislsl on 17-7-9.
 */
public class Test {
    public static void main(String[] args){

        for(int i=0;i<1000;i++){
            // System.out.print("0x" + Integer.toHexString((i*123)%99) + ", ");
        }
        try{
            Decoder decoder = new Decoder("./src/test/test.png");
            PNG png = decoder.decode();
            // CRCtest();
            // LZ77Test();
            // Decoder.decode("./src/test/test.png");
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

    // DEFLATE use LZ77 algorithm and Huffman coding
    // 压缩类Deflate
    // 解压类Inflater
    public static byte[] compress(byte input[]) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Deflater compressor = new Deflater(1);
        try {
            compressor.setInput(input);
            compressor.finish();
            final byte[] buf = new byte[2048];
            while (!compressor.finished()) {
                int count = compressor.deflate(buf);
                bos.write(buf, 0, count);
            }
        } finally {
            compressor.end();
        }
        return bos.toByteArray();
    }

    public static byte[] uncompress(byte[] input) throws DataFormatException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Inflater decompressor = new Inflater();
        try {
            decompressor.setInput(input);
            final byte[] buf = new byte[2048];
            while (!decompressor.finished()) {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            }
        } finally {
            decompressor.end();
        }
        return bos.toByteArray();
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
