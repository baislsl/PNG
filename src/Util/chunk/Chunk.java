package Util.chunk;

import Util.ByteHandler;

/**
 * Created by baislsl on 17-7-9.
 */
public class Chunk {
    protected long length;
    protected ChunkType type;
    protected byte[] data;
    protected byte[] crc = new byte[4];

    Chunk(){}


    Chunk(byte[] length, byte[] type, byte[] data, byte[] crc) {
        this.length = ByteHandler.byteToLong(length);
       // this.type = Enum.valueOf(ChunkType, ByteHandler.byteToString(type).toUpperCase())
        this.data = data;
        this.crc = crc;


    }


    public long getLength(){
        return length;
    }

    public ChunkType getType(){
        return type;
    }

    public byte[] getData(){
        return data;
    }

    public byte[] getCrc(){
        return crc;
    }
}
