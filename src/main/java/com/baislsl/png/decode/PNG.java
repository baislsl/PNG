package com.baislsl.png.decode;

import com.baislsl.png.encrypt.LZ77;
import com.baislsl.png.chunk.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.util.zip.DataFormatException;

/**
 * Created by baislsl on 17-7-9.
 */
public class PNG {
    private final static Logger LOG = LoggerFactory.getLogger(PNG.class);
    private IHDR ihdr;
    private IDATManager idats = new IDATManager();
    private PLTE plte;
    private IEND iend;

    public PNG() {
    }

    public PNG(IHDR ihdr, IDATManager idats, PLTE plte, IEND iend) {
        this.ihdr = ihdr;
        this.idats = idats;
        this.plte = plte;
        this.iend = iend;
    }

    public Color[][] getColor() throws DecodeException {
        byte[] rawData = idats.getIDATData();
        byte[] uncompressData = applyLZ77(rawData);
        byte[][] transferData = applyReverseFilter(uncompressData);
        Color[][] colors = applyColorTransfer(transferData);
        return colors;
    }

    private Color[][] applyColorTransfer(byte[][] data) throws DecodeException {
        int bpp = ihdr.getBpp();
        int width = (int) ihdr.getWidth();
        int height = (int) ihdr.getHeight();
        int colorType = ihdr.getColorType();
        int bitDepth = ihdr.getBitDepth();
        Color[][] colors = new Color[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                switch (colorType) {
                    case 2:
                        if (bitDepth == 8) {  // bpp = 3
                            colors[j][i] = new Color(
                                    data[i][bpp * j] & 0xff,
                                    data[i][bpp * j + 1] & 0xff,
                                    data[i][bpp * j + 2] & 0xff
                            );
                        } else if (bitDepth == 16) {   // bpp = 6
                            colors[j][i] = new Color(
                                    ((data[i][bpp * j] << 8) | (data[i][bpp * j + 1])) & 0xffff,
                                    ((data[i][bpp * j + 2] << 8) | (data[i][bpp * j + 3])) & 0xffff,
                                    ((data[i][bpp * j + 4] << 8) | (data[i][bpp * j + 5])) & 0xffff
                            );
                        }
                        break;
                    case 6:
                        if (bitDepth == 8) {  // bpp = 4
                            colors[j][i] = new Color(
                                    data[i][bpp * j] & 0xff,
                                    data[i][bpp * j + 1] & 0xff,
                                    data[i][bpp * j + 2] & 0xff,
                                    data[i][bpp * j + 3] & 0xff
                            );
                        } else if (bitDepth == 16) {   // bpp = 8
                            colors[j][i] = new Color(
                                    ((data[i][bpp * j] << 8) | (data[i][bpp * j + 1])) & 0xffff,
                                    ((data[i][bpp * j + 2] << 8) | (data[i][bpp * j + 3])) & 0xffff,
                                    ((data[i][bpp * j + 4] << 8) | (data[i][bpp * j + 5])) & 0xffff,
                                    ((data[i][bpp * j + 6] << 8) | (data[i][bpp * j + 7])) & 0xffff
                            );
                        }
                        break;
                    case 3:
                        int gap = 8 / bitDepth;
                        int a = (1 << bitDepth) - 1;
                        colors[j][i] = plte.getColor(data[i][j / gap] & a);
                        break;
                    default:
                        throw new DecodeException("Do not support color type " + colorType);
                }
            }
        }

        return colors;
    }

    private byte[] applyLZ77(byte[] data) throws DecodeException {
        byte[] result;
        try {
            result = LZ77.uncompress(data);
        } catch (DataFormatException e) {
            LOG.error("LZ77 decode error", e);
            throw new DecodeException(e);
        }
        LOG.info("Size after decode={}", result.length);
        return result;
    }

    private byte[][] applyReverseFilter(byte[] data) {
        int width = (int) ihdr.getWidth(), height = (int) ihdr.getHeight();
        return ReverseFilter.apply(data, width, height, ihdr.getBpp());
    }

    public void setIdats(IDATManager idats) {
        this.idats = idats;
    }

    public void setIhdr(IHDR ihdr) {
        this.ihdr = ihdr;
    }

    public void setPlte(PLTE plte) {
        this.plte = plte;
    }

    public void setIend(IEND iend) {
        this.iend = iend;
    }

    public void add(IDAT idat) throws DecodeException {
        idats.add(idat);
    }

    public long getWidth() {
        return ihdr.getWidth();
    }

    public long getHeight() {
        return ihdr.getHeight();
    }

}
