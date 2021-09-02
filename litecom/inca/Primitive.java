package litecom.inca;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Primitive {

    public static VirtualWorld vWorld;
    public static Graphics screen;
    public static int screenX;
    public static int screenY;
    public static int screenXLen;
    public static int screenYLen;
    public VertConnection vert[];
    public Point3d mid;
    public Color color;
    public Shape3d owner;
    public Point3d normal;
    public double dist;

    public Primitive() {
        mid = new Point3d();
    }

    public void setConnections(VertConnection vert[]) {
        this.vert = vert;
    }

    public void setOwner(Shape3d shape3d) {
        owner = shape3d;
    }

    public void updateMid() {
        mid.x = vert[0].p.x;
        mid.y = vert[0].p.y;
        mid.z = vert[0].p.z;
        for (int i = 1; i < vert.length; i++)
            mid.add(vert[i].p);

        mid.div(vert.length);
        dist = Math.sqrt(mid.x * mid.x + mid.y * mid.y + mid.z * mid.z);
    }

    public abstract void draw();

    public abstract Primitive copy();
}
