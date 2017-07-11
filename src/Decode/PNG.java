package Decode;

import Util.LZ77;
import Util.RasterImageFrame;
import Util.chunk.*;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

/**
 * Created by baislsl on 17-7-9.
 */
public class PNG {
    private IHDR ihdr;
    private ArrayList<IDAT> idats = new ArrayList<>();
    private PLTE plte;
    private IEND iend;


    public PNG() {
    }


    public void save(String path) {
        try {
            PrintWriter out = new PrintWriter(path);
            byte[] data = idats.get(0).outputStream();
            for (byte b : data) {
                out.write(b);
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int paethPredictor(int a, int b, int c) {
        int p = a + b - c;
        int pa = Math.abs(p - a), pb = Math.abs(p - b), pc = Math.abs(p - c);
        if (pa <= pb && pa <= pc) return a;
        if (pb <= pc) return b;
        return c;
    }

    // apply reverse Filter Algorithms to byte data
    // bpp = 3
    private static byte[] reverseFilterAlgorithm(byte[] data, int width, int height, int bpp) {
        int[] filterType = new int[height];
        int[][] blocks = new int[height][width * bpp];
        int dataIndex = 0;
        for (int i = 0; i < height; i++) {
            filterType[i] = Byte.toUnsignedInt(data[dataIndex++]);
            for (int j = 0; j < width * bpp; j++) {
                blocks[i][j] = Byte.toUnsignedInt(data[dataIndex++]);
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width * bpp; j++) {
                int prior = (i == 0) ? 0 : blocks[i - 1][j];
                int rawBpp = (j < bpp) ? 0 : blocks[i][j - bpp];
                int bppPrior = (i == 0 || j < bpp) ? 0 : blocks[i - 1][j - bpp];
                switch (filterType[i]) {
                    case 0: // none
                        break;
                    case 1: // sub
                        blocks[i][j] = blocks[i][j] + rawBpp;
                        break;
                    case 2: // up
                        blocks[i][j] = blocks[i][j] + prior;
                        break;
                    case 3: //average
                        blocks[i][j] = blocks[i][j] + (rawBpp + prior) / 2;
                        break;
                    case 4: // paeth
                        blocks[i][j] = blocks[i][j] + paethPredictor(rawBpp, prior, bppPrior);
                        break;
                    default:
                }
                blocks[i][j] &= 0xff;
            }
        }

        dataIndex = 0;
        for (int i = 0; i < height; i++) {
            data[dataIndex++] = (byte) (filterType[i] & 0xff);
            for (int j = 0; j < width * bpp; j++) {
                data[dataIndex++] = (byte) blocks[i][j];
            }
        }
        return data;
    }

    private static Color[][] getColorData(byte[] data, int width, int height,
                                          int colorType, int bitDepth) {
        Color[][] colors = new Color[width][height];
        int dataIndex = 0;

        if (colorType == 2 && bitDepth == 8) {
            for (int i = 0; i < height; i++) {
                ++dataIndex;
                for (int j = 0; j < width; j++) {
                    colors[j][i] = new Color(data[dataIndex]&0xff, data[dataIndex + 1]&0xff, data[dataIndex + 2]&0xff);
                    dataIndex += 3;
                }
            }
        } else if (colorType == 6 && bitDepth == 8) {
            for (int i = 0; i < height; i++) {
                ++dataIndex;
                for (int j = 0; j < width; j++) {
                    colors[j][i] = new Color(data[dataIndex]&0xff, data[dataIndex + 1]&0xff, data[dataIndex + 2]&0xff, data[dataIndex + 3]&0xff);
                    dataIndex += 4;
                }
            }
        }
        return colors;
    }

    public void show() throws DataFormatException {
        byte[] uncompressData = getImageData();
        int width = (int) ihdr.getWidth(), height = (int) ihdr.getHeight();
        byte[] transferData = reverseFilterAlgorithm(uncompressData, width, height, ihdr.getBpp());
        Color[][] colors = getColorData(transferData, width, height, ihdr.getColorType(), ihdr.getBitDepth());
        JFrame frame = new RasterImageFrame((int) ihdr.getWidth(), (int) ihdr.getHeight(), colors);
        frame.setTitle("PNG");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        writeInPixel();

    }

    public byte[] getIDATData() {
        int dataSize = 0;
        for (IDAT idat : idats) {
            dataSize += idat.dataLength();
        }
        byte[] data = new byte[dataSize];
        int curPos = 0;
        for (IDAT idat : idats) {
            System.arraycopy(idat.getData(), 0, data, curPos, (int) idat.dataLength());
            curPos += idat.dataLength();
        }
        return data;
    }

    public byte[] getImageData() throws DataFormatException {
        byte[] data = getIDATData();
        byte[] output = LZ77.uncompress(data);
        System.out.println("Size after decode=" + output.length);
        return output;
    }


    public void add(IHDR ihdr) throws DecodeException {
        if (this.ihdr != null)
            throw new DecodeException("Duplicate ihdr chunk");
        this.ihdr = ihdr;
    }

    public void add(IDAT idat) throws DecodeException {
        idats.add(idat);
    }

    public void add(PLTE plte) throws DecodeException {
        if (this.plte != null)
            throw new DecodeException("Duplicate plte chunk");
        this.plte = plte;
    }

    public void add(IEND iend) throws DecodeException {
        if (this.iend != null)
            throw new DecodeException("Duplicate iend chunk");
        this.iend = iend;
    }

    private void writeInPixel() {
        try {
            byte[] output = getImageData();
            int width = (int) ihdr.getWidth(), height = (int) ihdr.getHeight();
            byte[] transferData = reverseFilterAlgorithm(output, width, height, ihdr.getBpp());
            Color[][] colors = getColorData(transferData, width, height, ihdr.getColorType(), ihdr.getBitDepth());
            PrintWriter printWriter = new PrintWriter("imageData.txt");
            int index = 0;
            for (int i = 0; i < ihdr.getHeight(); i++) {
                ++index;
                printWriter.write("\n");
                for (int j = 0; j < ihdr.getWidth(); j++) {
                    printWriter.write(colors[j][i].toString());
//                    printWriter.write(
//                            "( " + Integer.toString((int) output[index] & 0xff) +
//                                    ", " + Integer.toString((int) output[index + 1] & 0xff)
//                                    + ", " + Integer.toString((int) output[index + 2] & 0xff) + "), "
//                    );
                    index += 3;
                }
            }
            printWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
