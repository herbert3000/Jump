package litecom.inca;

import java.awt.Color;
import java.util.Vector;

public final class VirtualWorld {

    public int nPrims;
    public int nVisible;
    private Vector<Shape3d> shapes;
    public boolean fog;
    public boolean fogFadeToWhite;
    public double fogStartZ;
    public double fogEndZ;
    public Color fogColor;
    //private boolean clipping;
    private Primitive primitive[];
    private QSortAlgorithm sorter;
    private Clipper clipper;
    public ViewPort currentVP;
    public Camera currentCam;
    public double kuk;
    public double kuk2;

    public VirtualWorld() {
        shapes = new Vector<Shape3d>();
        sorter = new QSortAlgorithm();
        kuk = 0.9D;
        kuk2 = 0.1D;
        Primitive.vWorld = this;
        Point3d.vWorld = this;
        clipper = new DefaultClipper();
    }

    public synchronized void render(ViewPort viewport, Camera camera) {
        Primitive.screen = viewport.screen;
        Primitive.screenXLen = viewport.xlen;
        Primitive.screenYLen = viewport.ylen;
        Point3d.fov = camera.fov;
        Point3d.xshift = viewport.xlen / 2;
        Point3d.yshift = viewport.ylen / 2;
        currentVP = viewport;
        currentCam = camera;
        clipper.clipShapes(this, shapes);
        primitive = new Primitive[nPrims];
        int i = 0;
        for (int j = 0; j < shapes.size(); j++) {
            Shape3d shape3d = (Shape3d)shapes.elementAt(j);
            if (shape3d.visible) {
            	if (shape3d.normal != null)
                    rotate(shape3d.normal.source, shape3d.normal.dest, shape3d.rotation);
                if (shape3d.scale.isOne()) {
                    rotate(shape3d.geometry.source, shape3d.geometry.dest, shape3d.rotation);
                } else {
                    scale(shape3d.geometry.source, shape3d.geometry.dest, shape3d.scale);
                    rotate(shape3d.geometry.dest, shape3d.geometry.dest, shape3d.rotation);
                }
                transform(shape3d.geometry.dest, shape3d.geometry.dest, shape3d.position);
                if (!camera.position.isZero())
                    transform(shape3d.geometry.dest, shape3d.geometry.dest, camera.position.neg());
                if (!camera.rotation.isZero())
                    rotate(shape3d.geometry.dest, shape3d.geometry.dest, camera.rotation);
                for (int l = 0; l < shape3d.primitive.length; l++) {
                    primitive[i] = shape3d.primitive[l];
                    primitive[i].updateMid();
                    i++;
                }
            }
        }
        sorter.sort(primitive);
        nPrims = 0;
        for (int k = 0; k < primitive.length; k++)
            primitive[k].draw();
    }

    public synchronized void addShape(Shape3d shape3d) {
        shapes.addElement(shape3d);
    }

    public synchronized void removeShape(Shape3d shape3d) {
        shapes.removeElement(shape3d);
    }

    public synchronized void removeShapeAt(int i) {
        shapes.removeElementAt(i);
    }

    public synchronized Shape3d getShape(int i) {
        return (Shape3d)shapes.elementAt(i);
    }

    //private void restructure() {}

    public void rotate(Point3d points1[], Point3d points2[], Point3d p) {
        if (p.isZero()) {
            for (int i = 0; i < points1.length; i++)
                if (points1[i] != null) {
                    points2[i].x = points1[i].x;
                    points2[i].y = points1[i].y;
                    points2[i].z = points1[i].z;
                }
            return;
        }
        for (int j = 0; j < points1.length; j++)
            if (points1[j] != null)
                points1[j].rotate(p, points2[j]);
    }

    private void scale(Point3d points1[], Point3d points2[], Point3d p) {
        if (p.isOne())
            return;
        for (int i = 0; i < points1.length; i++)
            if (points1[i] != null) {
                double d = points1[i].x;
                double d1 = points1[i].y;
                double d2 = points1[i].z;
                d *= p.x;
                d1 *= p.y;
                d2 *= p.z;
                points2[i].x = d;
                points2[i].y = d1;
                points2[i].z = d2;
            }
    }

    private void transform(Point3d points1[], Point3d points2[], Point3d p) {
        for (int i = 0; i < points1.length; i++)
            if (points1[i] != null) {
                double d = points1[i].x;
                double d1 = points1[i].y;
                double d2 = points1[i].z;
                points2[i].x = d + p.x;
                points2[i].y = d1 + p.y;
                points2[i].z = d2 + p.z;
            }
    }

    public synchronized void setFog(boolean flag, boolean flag1, double d, double d1) {
        fog = flag;
        fogFadeToWhite = flag1;
        fogStartZ = d;
        fogEndZ = d1;
    }

    public synchronized void setClipper(Clipper clipper) {
        this.clipper = clipper;
    }

    public Color shadeColor(Primitive primitive) {
        Color color = primitive.color;
        if (primitive.normal != null) {
            double d = (-primitive.normal.x - primitive.normal.y) + primitive.normal.z;
            if (d > 0.0D)
                d = Math.tan(d * 0.9D) * 0.1D;
            int i = (int)(d * 256D);
            int k = color.getRed() + i;
            int i1 = color.getGreen() + i;
            int k1 = color.getBlue() + i;
            if (k > 255)
                k = 255;
            if (i1 > 255)
                i1 = 255;
            if (k1 > 255)
                k1 = 255;
            if (k < 0)
                k = 0;
            if (i1 < 0)
                i1 = 0;
            if (k1 < 0)
                k1 = 0;
            color = new Color(k, i1, k1);
        }
        double d1 = primitive.dist;
        if (fog) {
            if (d1 > fogStartZ && d1 < fogEndZ) {
                int j = color.getRed();
                int l = color.getGreen();
                int j1 = color.getBlue();
                if (fogColor == null) {
                    int l1 = (int)(((d1 - fogStartZ) / (fogEndZ - fogStartZ)) * 255D);
                    if (!fogFadeToWhite)
                        l1 = -l1;
                    j += l1;
                    l += l1;
                    j1 += l1;
                    if (fogFadeToWhite) {
                        if (j > 255)
                            j = 255;
                        if (l > 255)
                            l = 255;
                        if (j1 > 255)
                            j1 = 255;
                    } else {
                        if (j < 0)
                            j = 0;
                        if (l < 0)
                            l = 0;
                        if (j1 < 0)
                            j1 = 0;
                    }
                } else {
                    double d2 = (d1 - fogStartZ) / (fogEndZ - fogStartZ);
                    j = (int)((double)j + (double)(fogColor.getRed() - j) * d2);
                    l = (int)((double)l + (double)(fogColor.getGreen() - l) * d2);
                    j1 = (int)((double)j1 + (double)(fogColor.getBlue() - j1) * d2);
                }
                return new Color(j, l, j1);
            }
            if (d1 > fogEndZ) {
                if (fogFadeToWhite)
                    return Color.white;
                else
                    return Color.black;
            } else {
                return color;
            }
        } else {
            return color;
        }
    }
}
