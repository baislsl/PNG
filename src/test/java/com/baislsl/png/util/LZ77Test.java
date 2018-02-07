package com.baislsl.png.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.zip.DataFormatException;

import static com.baislsl.png.util.LZ77.compress;
import static com.baislsl.png.util.LZ77.uncompress;
import static org.junit.Assert.*;

public class LZ77Test {
    private byte[] data = {
            0x23, 0x45, 0x67, 0x78,
    };

    @Test
    public void compress() throws Exception {
        byte[] encode = LZ77.compress(data);
        byte[] decode = LZ77.uncompress(encode);
        assertArrayEquals(decode, data);
    }
}