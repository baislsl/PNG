package Util;

import Decode.DecodeException;
import Util.chunk.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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


    public void show(){
        Color[][] data = getImageData();
        JFrame frame = new RasterImageFrame((int)ihdr.getWidth(), (int)ihdr.getHeight(), data);
        frame.setTitle("RasterImageTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    public Color[][] getImageData(){
        Color[][] data = new Color[(int)ihdr.getWidth()][(int)ihdr.getHeight()];



        return data;
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


}
