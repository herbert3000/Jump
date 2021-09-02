package litecom.inca;

import java.awt.Color;
import java.io.*;
import java.net.URL;
//import java.net.URLConnection;
import java.util.StringTokenizer;
import java.util.Vector;
import litecom.Debug;

public class IncaUtils {

    public static Shape3d Z(int i, int j, double d, double d1) {
        return new Shape3d(makeTorusGeom(i, j, d, d1, 0.0D, 0.0D, 0.0D), makeTorusFaces(i, j));
    }

    public static Shape3d U(int i, int j, double d, double d1, double d2, double d3, double d4) {
        return new Shape3d(makeTorusGeom(i, j, d, d1, d2, d3, d4), makeTorusFaces(i, j));
    }

    public static Shape3d createTorus(int i, int j, double d, double d1) {
        return new Shape3d(makeTorusGeom(i, j, d, d1, 0.0D, 0.0D, 0.0D), makeTorusTriFaces(i, j));
    }

    public static Shape3d a(int j, int k, double d, double d1, double d2, double d3, double d4) {
        return new Shape3d(makeTorusGeom(j, k, d, d1, d2, d3, d4), makeTorusTriFaces(j, k));
    }

    private static Geometry makeTorusGeom(int i, int j, double d, double d1, double d2, double d3, double d4) {
        Debug.out("Creating torus geometry with " + i * j + " faces...");
        Geometry a1 = new Geometry(i * j);
        int l = 0;
        double d5 = 0.0D;
        double d6 = 0.0D;
        double d7 = 0.0D;
        for (int i1 = 0; i1 < i; i1++) {
            double d8 = 0.0D;
            Math.cos(d5);
            Math.sin(d5);
            for (int j1 = 0; j1 < j; j1++) {
                double d9 = Math.cos(d5) * (d + Math.cos(d8 + Math.sin(d5) * d4) * (d1 + Math.sin(d7) * d3));
                double d10 = Math.sin(d5) * (d + Math.cos(d8 + Math.sin(d5) * d4) * (d1 + Math.sin(d7) * d3));
                double d11 = Math.sin(d8 + Math.sin(d5) * d4) * (d1 + Math.sin(d7) * d3);
                a1.source[l].x = d9;
                a1.source[l].y = d10;
                a1.source[l].z = d11;
                d8 += Math.PI * 2 / (double)j;
                l++;
            }
            d5 += Math.PI * 2 / (double)i;
            d6 += Math.PI * 2 / (double)i;
            d7 += (Math.PI * 2 / (double)i) * d2;
        }

        a1.copyDest();
        Debug.out("Done!");
        return a1;
    }

    private static Primitive[] makeTorusFaces(int i, int j) {
        int k = i * j;
        Debug.out("Creating " + k + " faces...");
        PolyPrimitive pp[] = new PolyPrimitive[k];
        int l = 0;
        double d = 0.0D;
        double d1 = 0.0D;
        for (int j1 = 0; j1 < i; j1++) {
            for (int k1 = 0; k1 < j; k1++) {
                VertConnection vc[] = new VertConnection[4];
                if (k1 == j - 1) {
                    vc[3] = new VertConnection(l);
                    vc[2] = new VertConnection(((l / j) * j) % k);
                    vc[1] = new VertConnection(((l / j) * j + j) % k);
                    vc[0] = new VertConnection((l + j) % k);
                } else {
                    vc[3] = new VertConnection(l);
                    vc[2] = new VertConnection((l + 1) % k);
                    vc[1] = new VertConnection((l + j + 1) % k);
                    vc[0] = new VertConnection((l + j) % k);
                }
                int l1 = (int)((Math.sin(d) + 1.0D) * 127D);
                int i2 = (int)((Math.cos(d) + 1.0D) * 127D);
                int j2 = (int)((Math.sin(d + 2D + d1) + 1.0D) * 127D);
                pp[l] = new PolyPrimitive(new Color(l1, i2, j2));
                pp[l].setConnections(vc);
                l++;
                d1 += 0.9D;
            }
            d += Math.PI * 2 / (double)i;
        }
        Debug.out("Done!");
        return pp;
    }

    public static Primitive[] makeTorusTriFaces(int i, int j) {
        int k = i * j * 2;
        Debug.out("Creating " + k + " faces...");
        PolyPrimitive pp[] = new PolyPrimitive[k];
        int l = 0;
        double d = 0.0D;
        double d1 = 0.0D;
        for (int j1 = 0; j1 < i; j1++) {
            for (int k1 = 0; k1 < j; k1++) {
                VertConnection vc[] = new VertConnection[3];
                if (k1 == j - 1) {
                    vc[2] = new VertConnection(l / 2);
                    vc[1] = new VertConnection(((l / 2 / j) * j) % (k / 2));
                    vc[0] = new VertConnection((l / 2 + j) % (k / 2));
                } else {
                    vc[2] = new VertConnection(l / 2);
                    vc[1] = new VertConnection((l / 2 + 1) % (k / 2));
                    vc[0] = new VertConnection((l / 2 + j) % (k / 2));
                }
                pp[l] = new PolyPrimitive(Color.red);
                pp[l].setConnections(vc);
                l++;
                vc = new VertConnection[3];
                if (k1 == j - 1) {
                    vc[2] = new VertConnection(((l / 2 / j) * j) % (k / 2));
                    vc[1] = new VertConnection(((l / 2 / j) * j + j) % (k / 2));
                    vc[0] = new VertConnection((l / 2 + j) % (k / 2));
                } else {
                    vc[2] = new VertConnection((l / 2 + 1) % (k / 2));
                    vc[1] = new VertConnection((l / 2 + j + 1) % (k / 2));
                    vc[0] = new VertConnection((l / 2 + j) % (k / 2));
                }
                pp[l] = new PolyPrimitive(Color.red);
                pp[l].setConnections(vc);
                l++;
                d1 += 0.9D;
            }
            d += Math.PI * 2 / (double)i;
        }
        Debug.out("Done!");
        return pp;
    }

    public static Shape3d createCube(double d) {
        return createCube(d, d, d);
    }

    public static Shape3d createCube(double d, double d1, double d2) {
        double d3 = d / 2D;
        double d4 = d1 / 2D;
        double d5 = d2 / 2D;
        Geometry geometry = new Geometry(8);
        geometry.source[0].x = d3;
        geometry.source[1].x = d3;
        geometry.source[2].x = -d3;
        geometry.source[3].x = -d3;
        geometry.source[0].y = d4;
        geometry.source[1].y = -d4;
        geometry.source[2].y = -d4;
        geometry.source[3].y = d4;
        geometry.source[0].z = d5;
        geometry.source[1].z = d5;
        geometry.source[2].z = d5;
        geometry.source[3].z = d5;
        geometry.source[4].x = d3;
        geometry.source[5].x = d3;
        geometry.source[6].x = -d3;
        geometry.source[7].x = -d3;
        geometry.source[4].y = d4;
        geometry.source[5].y = -d4;
        geometry.source[6].y = -d4;
        geometry.source[7].y = d4;
        geometry.source[4].z = -d5;
        geometry.source[5].z = -d5;
        geometry.source[6].z = -d5;
        geometry.source[7].z = -d5;
        geometry.copyDest();
        PolyPrimitive pp[] = new PolyPrimitive[6];
        VertConnection vc[] = new VertConnection[4];
        vc[0] = new VertConnection(3);
        vc[1] = new VertConnection(2);
        vc[2] = new VertConnection(1);
        vc[3] = new VertConnection(0);
        pp[0] = new PolyPrimitive(Color.green);
        pp[0].setConnections(vc);
        vc = new VertConnection[4];
        vc[0] = new VertConnection(4);
        vc[1] = new VertConnection(0);
        vc[2] = new VertConnection(1);
        vc[3] = new VertConnection(5);
        pp[1] = new PolyPrimitive(Color.blue);
        pp[1].setConnections(vc);
        vc = new VertConnection[4];
        vc[0] = new VertConnection(7);
        vc[1] = new VertConnection(6);
        vc[2] = new VertConnection(2);
        vc[3] = new VertConnection(3);
        pp[2] = new PolyPrimitive(Color.black);
        pp[2].setConnections(vc);
        vc = new VertConnection[4];
        vc[0] = new VertConnection(0);
        vc[1] = new VertConnection(4);
        vc[2] = new VertConnection(7);
        vc[3] = new VertConnection(3);
        pp[3] = new PolyPrimitive(Color.red);
        pp[3].setConnections(vc);
        vc = new VertConnection[4];
        vc[0] = new VertConnection(5);
        vc[1] = new VertConnection(1);
        vc[2] = new VertConnection(2);
        vc[3] = new VertConnection(6);
        pp[4] = new PolyPrimitive(Color.yellow);
        pp[4].setConnections(vc);
        vc = new VertConnection[4];
        vc[0] = new VertConnection(4);
        vc[1] = new VertConnection(5);
        vc[2] = new VertConnection(6);
        vc[3] = new VertConnection(7);
        pp[5] = new PolyPrimitive(Color.pink);
        pp[5].setConnections(vc);
        Shape3d shape3d = new Shape3d(geometry, pp);
        return shape3d;
    }

    public static Shape3d loadShape(URL url, String s) {
        return new Shape3d(loadGeometry(url, s + ".geom"), loadFaces(url, s + ".faces"));
    }

    private static Geometry loadGeometry(URL url, String s) {
        Vector<Point3d> vector = new Vector<Point3d>();
        Debug.out("Loading geometry from " + s + "...");
        URL url1 = null;
        try {
            url1 = new URL(url, s);
        } catch(Exception exception) {
            System.out.println("Error opening file: " + s);
            System.out.println(exception);
            System.exit(1);
        }
        try {
            DataInputStream datainputstream = new DataInputStream(url1.openConnection().getInputStream());
            String s1;
            while ((s1 = datainputstream.readLine()) != null) {
                StringTokenizer stringtokenizer = new StringTokenizer(s1, "(), ");
                String s2 = stringtokenizer.nextToken().trim();
                String s3 = stringtokenizer.nextToken().trim();
                String s4 = stringtokenizer.nextToken().trim();
                Point3d point3d = new Point3d(Double.valueOf(s2).doubleValue(), Double.valueOf(s3).doubleValue(), Double.valueOf(s4).doubleValue());
                vector.addElement(point3d);
            }
            datainputstream.close();
        } catch(Exception exception1) {
            System.out.println("Error while reading file: " + s);
            System.out.println(exception1);
            System.exit(0);
        }
        Point3d apoint3d[] = new Point3d[vector.size()];
        for (int i = 0; i < apoint3d.length; i++)
            apoint3d[i] = (Point3d)vector.elementAt(i);

        Geometry geometry = new Geometry(apoint3d);
        geometry.copyDest();
        Debug.out("Done - " + vector.size() + " verts.");
        return geometry;
    }

    private static Primitive[] loadFaces(URL url, String s) {
        Vector<PolyPrimitive> vector = new Vector<PolyPrimitive>();
        Debug.out("Loading faces from " + s + "...");
        URL url1 = null;
        try {
            url1 = new URL(url, s);
        } catch(Exception exception) {
            System.out.println("Error opening file: " + s);
            System.out.println(exception);
            System.exit(1);
        }
        try {
            DataInputStream datainputstream = new DataInputStream(url1.openConnection().getInputStream());
            String s1;
            while((s1 = datainputstream.readLine()) != null) {
                StringTokenizer stringtokenizer = new StringTokenizer(s1, "(), ");
                int j = Integer.parseInt(stringtokenizer.nextToken().trim());
                int k = Integer.parseInt(stringtokenizer.nextToken().trim());
                int l = Integer.parseInt(stringtokenizer.nextToken().trim());
                VertConnection vc[] = new VertConnection[3];
                vc[0] = new VertConnection(j);
                vc[1] = new VertConnection(k);
                vc[2] = new VertConnection(l);
                PolyPrimitive pp = new PolyPrimitive(Color.gray);
                pp.setConnections(vc);
                vector.addElement(pp);
            }
            datainputstream.close();
        } catch(Exception _ex) {
            System.out.println("Could not load faces...");
            System.exit(1);
        }
        PolyPrimitive pp[] = new PolyPrimitive[vector.size()];
        for (int i = 0; i < pp.length; i++)
            pp[i] = (PolyPrimitive)vector.elementAt(i);

        Debug.out("Done - " + vector.size() + " faces.");
        return pp;
    }

    public static void colorFaces(Shape3d shape3d, Color color1, Color color2, boolean flag) {
        float len = shape3d.primitive.length;
        float r1 = color1.getRed();
        float g1 = color1.getGreen();
        float b1 = color1.getBlue();
        float r2 = color2.getRed();
        float g2 = color2.getGreen();
        float b2 = color2.getBlue();
        r2 = (r2 - r1) / len / 256F;
        g2 = (g2 - g1) / len / 256F;
        b2 = (b2 - b1) / len / 256F;
        r1 /= 256F;
        g1 /= 256F;
        b1 /= 256F;
        for (int i = 0; i < shape3d.primitive.length; i++) {
            shape3d.primitive[i].color = new Color(r1, g1, b1);
            if (flag) {
                r1 += r2 * 2.0F;
                g1 += g2 * 2.0F;
                b1 += b2 * 2.0F;
                if (i == shape3d.primitive.length / 2) {
                    r2 = -r2;
                    g2 = -g2;
                    b2 = -b2;
                }
            } else {
                r1 += r2;
                g1 += g2;
                b1 += b2;
            }
            if (r1 >= 1.0F)
                r1 = 0.99F;
            if (g1 >= 1.0F)
                g1 = 0.99F;
            if (b1 >= 1.0F)
                b1 = 0.99F;
            if (r1 < 0.0F)
                r1 = 0.0F;
            if (g1 < 0.0F)
                g1 = 0.0F;
            if (b1 < 0.0F)
                b1 = 0.0F;
        }
    }

    public static Shape3d createPyramid(double d) {
        double d1 = d / 2D;
        Geometry geometry = new Geometry(4);
        geometry.source[0].x = 0.0D;
        geometry.source[1].x = 0.0D;
        geometry.source[2].x = d1;
        geometry.source[3].x = -d1;
        geometry.source[0].y = 0.0D;
        geometry.source[1].y = -d1;
        geometry.source[2].y = d1;
        geometry.source[3].y = d1;
        geometry.source[0].z = d1;
        geometry.source[1].z = -d1;
        geometry.source[2].z = -d1;
        geometry.source[3].z = -d1;
        geometry.copyDest();
        PolyPrimitive pp[] = new PolyPrimitive[4];
        VertConnection vc[] = new VertConnection[3];
        vc[0] = new VertConnection(1);
        vc[1] = new VertConnection(2);
        vc[2] = new VertConnection(0);
        pp[0] = new PolyPrimitive(Color.green);
        pp[0].setConnections(vc);
        vc = new VertConnection[3];
        vc[0] = new VertConnection(3);
        vc[1] = new VertConnection(1);
        vc[2] = new VertConnection(0);
        pp[1] = new PolyPrimitive(Color.blue);
        pp[1].setConnections(vc);
        vc = new VertConnection[3];
        vc[0] = new VertConnection(2);
        vc[1] = new VertConnection(3);
        vc[2] = new VertConnection(0);
        pp[2] = new PolyPrimitive(Color.yellow);
        pp[2].setConnections(vc);
        vc = new VertConnection[3];
        vc[0] = new VertConnection(2);
        vc[1] = new VertConnection(1);
        vc[2] = new VertConnection(3);
        pp[3] = new PolyPrimitive(Color.red);
        pp[3].setConnections(vc);
        return new Shape3d(geometry, pp);
    }
}
