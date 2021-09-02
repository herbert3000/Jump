package litecom.inca;

//import java.io.PrintStream;

public class Shape3d {

    public Geometry geometry;
    public Geometry normal;
    public Primitive primitive[];
    public Point3d scale;
    public Point3d rotation;
    public Point3d position;
    public double r;
    public boolean visible;

    public Shape3d() {
        scale = new Point3d(1, 1, 1);
        rotation = new Point3d();
        position = new Point3d();
    }

    public Shape3d(Shape3d shape3d) {
        scale = new Point3d(1, 1, 1);
        rotation = new Point3d();
        position = new Point3d();
        geometry = new Geometry(shape3d.geometry);
        if (shape3d.normal != null)
            normal = new Geometry(shape3d.normal);
        primitive = new Primitive[shape3d.primitive.length];
        for (int i = 0; i < primitive.length; i++)
            primitive[i] = shape3d.primitive[i].copy();

        update();
    }

    public Shape3d(Geometry geometry1, Primitive aprimitive[]) {
        scale = new Point3d(1, 1, 1);
        rotation = new Point3d();
        position = new Point3d();
        geometry = geometry1;
        primitive = aprimitive;
        for (int i = 0; i < primitive.length; i++)
            primitive[i].owner = this;

        update();
    }

    public void update() {
        for (int i = 0; i < primitive.length; i++) {
            Primitive primitive1 = primitive[i];
            primitive1.setOwner(this);
            for (int j = 0; j < primitive1.vert.length; j++) {
                if (primitive1.vert[j].index >= geometry.dest.length) {
                    System.out.println("VertConnections pointing outside geometry.");
                    System.exit(1);
                }
                primitive1.vert[j].p = geometry.dest[primitive1.vert[j].index];
            }
            if (normal != null)
                primitive[i].normal = normal.dest[i];
        }

        r = 0.0D;
        Point3d point3d = new Point3d();
        for (int k = 0; k < geometry.source.length; k++) {
            Point3d mb2 = geometry.source[k];
            double d = Point3d.dist(point3d, mb2);
            if (d > r)
                r = d;
        }
    }

    public void createNormals(double d) {
        normal = new Geometry(primitive.length);
        for (int i = 0; i < primitive.length; i++) {
            normal.source[i] = calcNormal(primitive[i], d);
            normal.dest[i] = new Point3d(normal.source[i]);
            primitive[i].normal = normal.dest[i];
        }
    }

    private Point3d calcNormal(Primitive p, double d) {
        double d1 = geometry.source[p.vert[0].index].x - geometry.source[p.vert[1].index].x;
        double d2 = geometry.source[p.vert[0].index].y - geometry.source[p.vert[1].index].y;
        double d3 = geometry.source[p.vert[0].index].z - geometry.source[p.vert[1].index].z;
        double d4 = geometry.source[p.vert[0].index].x - geometry.source[p.vert[2].index].x;
        double d5 = geometry.source[p.vert[0].index].y - geometry.source[p.vert[2].index].y;
        double d6 = geometry.source[p.vert[0].index].z - geometry.source[p.vert[2].index].z;
        double d7 = d * (d2 * d6 - d3 * d5);
        double d9 = d * (d3 * d4 - d1 * d6);
        double d8 = d * (d1 * d5 - d4 * d2);
        Point3d point3d = new Point3d(d7, d9, d8);
        point3d.normalize();
        return point3d;
    }

    public void updatePrims() {
        int i = 0;
        for (int j = 0; j < primitive.length; j++)
            if (primitive[j] != null)
                i++;

        Primitive aprimitive[] = new Primitive[i];
        int k = 0;
        for (int l = 0; l < primitive.length; l++)
            if (primitive[l] != null) {
                aprimitive[k] = primitive[l];
                k++;
            }
        primitive = aprimitive;
    }

    public void updateVerts() {
        int i = 0;
        for (int j = 0; j < geometry.source.length; j++)
            if (geometry.source[j] != null)
                i++;

        Point3d source[] = new Point3d[i];
        Point3d dest[] = new Point3d[i];
        int k = 0;
        for (int l = 0; l < geometry.source.length; l++)
            if (geometry.source[l] != null) {
                source[k] = geometry.source[l];
                dest[k] = geometry.dest[l];
                k++;
            }

        geometry.source = source;
        geometry.dest = dest;
        for (int i1 = 0; i1 < primitive.length; i1++) {
            Primitive gb1 = primitive[i1];
            for (int j1 = 0; j1 < gb1.vert.length; j1++) {
                for (int k1 = 0; k1 < geometry.dest.length; k1++) {
                    if (geometry.dest[k1] != gb1.vert[j1].p)
                        continue;
                    gb1.vert[j1].index = k1;
                    break;
                }
            }
        }
    }

    public void center() {
        Point3d point3d = new Point3d();
        for (int i = 0; i < geometry.source.length; i++)
            point3d.add(geometry.source[i]);

        point3d.div(geometry.source.length);
        for (int i = 0; i < geometry.source.length; i++)
            geometry.source[i].sub(point3d);
    }

    public void setXORMode(boolean flag) {
        for (int i = 0; i < primitive.length; i++)
            if (primitive[i] instanceof PolyPrimitive)
                ((PolyPrimitive)primitive[i]).XORMode = flag;
    }

    public void setDoubleSided(boolean flag) {
        for (int i = 0; i < primitive.length; i++)
            if (primitive[i] instanceof PolyPrimitive)
                ((PolyPrimitive)primitive[i]).doubleSided = flag;
    }
}
