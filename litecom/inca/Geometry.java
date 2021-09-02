package litecom.inca;

public class Geometry {

    public Point3d source[];
    public Point3d dest[];

    public Geometry(Point3d apoint3d[]) {
        source = apoint3d;
        dest = new Point3d[apoint3d.length];
        for (int i = 0; i < apoint3d.length; i++)
            dest[i] = new Point3d(apoint3d[i]);

    }

    public Geometry(int i) {
        source = new Point3d[i];
        dest = new Point3d[i];
        for (int j = 0; j < i; j++) {
            source[j] = new Point3d();
            dest[j] = new Point3d();
        }
    }

    public Geometry(Geometry geometry) {
        this(geometry.source.length);
        for (int i = 0; i < source.length; i++)
            source[i] = new Point3d(geometry.source[i]);

        copyDest();
    }

    public void copyDest() {
        for (int i = 0; i < dest.length; i++)
            dest[i] = new Point3d(source[i]);
    }
}
