package litecom.inca;

import java.awt.Color;
//import java.awt.Graphics;

public class PolyPrimitive extends Primitive {
    public boolean doubleSided;
    public boolean XORMode;

    public PolyPrimitive(Color color) {
        doubleSided = false;
        XORMode = false;
        super.color = color;
    }

    public void draw() {
        if (super.dist > Primitive.vWorld.fogEndZ && Primitive.vWorld.fog)
            return;
        int ai[] = new int[super.vert.length];
        int ai1[] = new int[super.vert.length];
        int i = 0;
        int j = 15;
        for (int k = 0; k < super.vert.length; k++) {
            if (super.vert[k].p.z <= 1.0D) {
                super.vert[k].p.z = 1.0D;
                i++;
            }
            Point2d point2d = super.vert[k].p.project();
            int i1 = 0;
            if(point2d.x < 0)
                i1++;
            if(point2d.y < 0)
                i1 += 2;
            if(point2d.x > Primitive.screenXLen)
                i1 += 4;
            if(point2d.y > Primitive.screenYLen)
                i1 += 8;
            j &= i1;
            ai[k] = point2d.x;
            ai1[k] = point2d.y;
        }

        if (i == super.vert.length)
            return;
        if (j != 0)
            return;
        if (!doubleSided) {
            int l = (ai[0] - ai[1]) * (ai1[2] - ai1[1]) - (ai1[0] - ai1[1]) * (ai[2] - ai[1]);
            if (l > 0)
                return;
        }
        if (XORMode)
            Primitive.screen.setXORMode(Color.black);
        Primitive.screen.setColor(Primitive.vWorld.shadeColor(this));
        Primitive.screen.fillPolygon(ai, ai1, super.vert.length);
        if (XORMode)
            Primitive.screen.setPaintMode();
        Primitive.vWorld.nPrims++;
    }

    public Primitive copy() {
        PolyPrimitive polyprimitive = new PolyPrimitive(super.color);
        polyprimitive.doubleSided = doubleSided;
        polyprimitive.XORMode = XORMode;
        polyprimitive.vert = new VertConnection[super.vert.length];
        for(int i = 0; i < super.vert.length; i++)
            ((Primitive) (polyprimitive)).vert[i] = new VertConnection(super.vert[i]);

        return polyprimitive;
    }
}
