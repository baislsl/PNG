package com.baislsl.png.util;

import org.junit.Test;

import java.util.zip.CRC32;

import static org.junit.Assert.*;

public class CRCTest {

    @Test
    public void crc() {
        byte[] data = {
                0x49, 0x45, 0x4e, 0x44
        };
        long result = CRC.crc(data, data.length);
        assertEquals(0xae426082L, result);
    }
}