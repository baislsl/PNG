package com.baislsl.png.Util.chunk;

import com.baislsl.png.Decode.DecodeException;

import java.awt.*;

/**
 * Created by baislsl on 17-7-9.
 */
public class PLTE extends Chunk {
    private Color[] color;

    public PLTE(byte[] length, byte[] type, byte[] data, byte[] crc) throws DecodeException{
        super(length, type, data, crc);
        build();
    }


    private void build() throws DecodeException{
        if(this.length % 3 != 0 )
            throw new DecodeException("PLTE length can not be divide by 3");

        int size = (int)length / 3;
        color = new Color[size];
        for(int i=0;i<size;i++){
            color[i] = new Color(Byte.toUnsignedInt(data[i*3]),
                    Byte.toUnsignedInt(data[i*3 + 1]), Byte.toUnsignedInt(data[i*3 + 2]) );
        }
    }

    public Color getColor(int i){
        return color[i];
    }

    public int getPaletteSize(){
        return color.length;
    }


}
