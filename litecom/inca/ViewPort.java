package litecom.inca;

import java.awt.Graphics;

public class ViewPort {

    //public static final int _fld012C = 1;
    //public static final int _fld012D = 2;
    public int _fld012E;
    public Graphics screen;
    public int xlen;
    public int ylen;

    public ViewPort(Graphics g, int i, int j) {
        _fld012E = 1;
        screen = g;
        xlen = i;
        ylen = j;
    }
}
