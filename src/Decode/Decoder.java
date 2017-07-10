package Decode;

import Util.CRC;
import Util.chunk.IDAT;
import Util.chunk.IHDR;
import Util.chunk.PLTE;
import Util.ByteHandler;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static Util.ByteHandler.byteToLong;

/**
 * Created by baislsl on 17-7-9.
 */
public class Decoder {
    private final InputStream in;

    final private static char[] head = {
            0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a
    };

    public Decoder(String path) throws IOException {
        in = new FileInputStream(path);
    }

    public Decoder(InputStream in) throws IOException {
        this.in = in;

    }

    public PNG readInPNG() throws IOException, DecodeException {
        PNG png = new PNG();
        byte[] h = readBytes(8);
        for (int i = 0; i < 8; i++) {
            if ((h[i] & 0xff) != (int) head[i])
                throw new DecodeException("It seems that this is not a PNG files");
        }

        System.out.println(ByteHandler.byteToString(h));

        while (true) {
            byte[] length = readBytes(4);
            long size = byteToLong(length);
            byte[] type = readBytes(4);
            System.out.println(ByteHandler.byteToString(type));
            String chunkName = ByteHandler.byteToString(type).toUpperCase();
            if (chunkName.equals("IEND"))
                break;
            byte[] data = readBytes((int) size);
            byte[] crc = readBytes(4);

            if(chunkName.equals("IHDR")){
                IHDR ihdr = new IHDR(length, type, data, crc);
                png.add(ihdr);
                ihdr.showInfo();
            }else if(chunkName.equals("IDAT")){
                IDAT idat = new IDAT(length, type, data, crc);
                png.add(idat);
            }else if(chunkName.equals("PLET")){
                PLTE plte = new PLTE(length, type, data, crc);
                png.add(plte);
            }

            long crcL = byteToLong(crc);
            byte[] crcData = new byte[4 + (int) size];
            System.arraycopy(type, 0, crcData, 0, 4);
            System.arraycopy(data, 0, crcData, 4, (int) size);
            long checkCrc = CRC.crc(crcData, (int) size + 4);
            if (checkCrc != crcL) {
                throw new DecodeException("Error data stream for in correct crc");
            }

        }
        return png;
    }



    private byte[] readBytes(int size) throws IOException {
        byte[] result = new byte[size];
        in.read(result, 0, size);
        return result;
    }


}
