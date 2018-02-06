package com.baislsl.png.Util.chunk;

import com.baislsl.png.Decode.DecodeException;
import com.baislsl.png.Util.ByteHandler;

/**
 * Created by baislsl on 17-7-9.
 */
public class IHDR extends Chunk {
    private long width, height;
    private int bitDepth, colorType, compressionMethod, filterMethod, interlaceMethod;

    final private static int[] colorTypeValid = {0, 2, 3, 4, 6};
    final private static int[][] mapColorBitDepth = {
            {1, 2, 4, 8, 16},   // color type = 0
            {},
            {8, 16},            // color type = 2
            {1, 2, 4, 8},       // color type = 3
            {8, 16},            // color type = 4
            {},
            {8, 16}             // color type = 6
    };

    // the number of bytes per complete pixel, rounding up to one
    public int getBpp(){
        if(colorType == 2){             // Each pixel is an R,G,B triple.
            return 3;
        } else if(colorType == 6){      // Each pixel is an R,G,B triple, followed by an alpha sample.
            return 4;
        } else if(colorType == 3){      // palette index, roll up to 1
            return 1;
        }
        else {
            System.out.println("Error when find bpp");
            return -100;
        }
    }

    public IHDR(byte[] length, byte[] type, byte[] data, byte[] crc) throws DecodeException {
        super(length, type, data, crc);
        //if(this.type != ChunkType.IEND)
        //throw new DecodeException("Error for IHDR build name");
        build();
        checkLegal();
    }

    public void showInfo() {
        System.out.println("width=" + width);
        System.out.println("height=" + height);
        System.out.println("bitDepth=" + bitDepth);
        System.out.println("colorType=" + colorType);
        System.out.println("compressionMethod=" + compressionMethod);
        System.out.println("filterMethod=" + filterMethod);
        System.out.println("interlaceMethod=" + interlaceMethod);
    }


    private void build() {
        this.width = ByteHandler.byteToLong(data);
        this.height = ByteHandler.byteToLong(data, 4);
        this.bitDepth = Byte.toUnsignedInt(data[8]);
        this.colorType = Byte.toUnsignedInt(data[9]);
        this.compressionMethod = Byte.toUnsignedInt(data[10]);
        this.filterMethod = Byte.toUnsignedInt(data[11]);
        this.interlaceMethod = Byte.toUnsignedInt(data[12]);

    }


    private void checkLegal() throws DecodeException {
        boolean legal = false;
        for (int c : colorTypeValid) {
            if (c == colorType) {
                legal = true;
                break;
            }
        }
        if (!legal) {
            throw new DecodeException("Initialize IHDR : color type not legal to be " + colorType);
        }
        for (int b : mapColorBitDepth[colorType]) {
            if (b == bitDepth) {
                return;
            }
        }
        throw new DecodeException("Initialzie IHDR : bit depth " + bitDepth + " not valid matching color type " + colorType);
    }


    public long getWidth() {
        return this.width;
    }

    public long getHeight() {
        return this.height;
    }

    public int getBitDepth() {
        return bitDepth;
    }

    public int getColorType() {
        return colorType;
    }

    public int getCompressionMethod() {
        return compressionMethod;
    }

    public int getFilterMethod() {
        return filterMethod;
    }

    public int getInterlaceMethod() {
        return interlaceMethod;
    }


}
