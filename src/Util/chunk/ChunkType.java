package Util.chunk;

/**
 * Created by baislsl on 17-7-9.
 */
public enum ChunkType {

    IHDR("IHDR"),
    PLTE("PLTE"),
    IDAT("IDAT"),
    IEND("IEND");


    byte[] types;

    ChunkType(String name) {
        for (int i = 0; i < 4; i++)
            types[i] = (byte) name.charAt(i);

    }

    ChunkType(byte[] types) {
        this.types = types;
    }

}
