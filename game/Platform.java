package game;

import java.awt.Color;
import litecom.Debug;
import litecom.inca.*;

public class Platform {

    public static final int nColorSets = 9;
    public static Color colorSet[];
    public static String colorSetName[] = {
        "Green", "Blue", "Red", "Orange", "Yellow", "Silver", "Cyan", "Magenta", "Lightblue"
    };
    public Segment sLink;
    public Point3d pos;
    public Point3d del;
    public Point3d startPos;
    public Shape3d s;
    public Sector owner;
    public int set;
    public boolean xored;
    private boolean firstTime;

    static  {
        colorSet = new Color[18];
        colorSet[0] = new Color(50, 200, 50);
        colorSet[1] = new Color(220, 200, 140);
        colorSet[2] = new Color(50, 100, 200);
        colorSet[3] = new Color(140, 200, 240);
        colorSet[4] = new Color(200, 50, 0);
        colorSet[5] = new Color(240, 150, 100);
        colorSet[6] = new Color(250, 110, 0);
        colorSet[7] = new Color(250, 220, 100);
        colorSet[8] = new Color(255, 230, 30);
        colorSet[9] = new Color(255, 240, 100);
        colorSet[10] = new Color(195, 195, 198);
        colorSet[11] = new Color(220, 203, 240);
        colorSet[12] = new Color(30, 192, 166);
        colorSet[13] = new Color(17, 108, 93);
        colorSet[14] = new Color(134, 30, 193);
        colorSet[15] = new Color(97, 31, 133);
        colorSet[16] = new Color(32, 128, 144);
        colorSet[17] = new Color(63, 73, 100);
    }

    public Platform(Point3d point3d) {
        del = new Point3d();
        set = 1;
        xored = false;
        firstTime = true;
        pos = point3d;
        startPos = new Point3d(point3d);
        s = IncaUtils.createCube(100D, 100D, 100D);
        s.position = point3d;
        core.vWorld.addShape(s);
    }

    public void colorFaces() {
        Point3d point3d = new Point3d((int)(pos.x / 100D), (int)(pos.y / 100D), (int)(pos.z / 100D));
        if (set <= 0)
            set = 1;
        int i = (set - 1) * 2;
        Color color;
        Color color1;
        if ((point3d.x + point3d.z % 2D) % 2D == point3d.y % 2D) {
            color = colorSet[i].brighter();
            color1 = colorSet[i + 1].brighter();
        } else {
            color = colorSet[i];
            color1 = colorSet[i + 1];
        }
        s.primitive[4].color = color;
        s.primitive[3].color = color;
        s.primitive[0].color = color1;
        s.primitive[1].color = color1;
        s.primitive[2].color = color1;
        s.primitive[5].color = color1;
    }

    public void move(double d) {
        if (owner == null) {
            Debug.out(this, " someone called move, but I'm static.");
        } else {
            core.world.hidePlatform(this);
            Point3d point3d = new Point3d(del);
            point3d.mul(d);
            pos.add(point3d);
            core.world.showPlatform(this);
        }
    }

    public String toString() {
        String s = "Platform";
        if (owner != null)
            s = s + " sector: " + owner.name;
        return s;
    }

    public void setXORMode(boolean flag) {
        xored = flag;
        s.setXORMode(flag);
    }
}
