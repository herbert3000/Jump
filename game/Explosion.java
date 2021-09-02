package game;

import java.awt.Color;
import litecom.inca.*;

public class Explosion extends Shape3d {

    public static final double gravity;
    int nPolys;
    double startSize;
    double startStrength;
    double lifeTime;
    Point3d delta[];
    double counter;

    static {
        gravity = World.gravity / 2D;
    }

    public Explosion(Point3d point3d, int i) {
        this(point3d, i, 40D, 650D, 2D);
    }

    public Explosion(Point3d point3d, int i, double d, double d1, double d2) {
    	counter = 0.0D;
        startSize = d;
        startStrength = d1;
        lifeTime = d2;
        nPolys = i;
        delta = new Point3d[i];
        super.position = point3d;
        super.geometry = getGeometry();
        super.primitive = getFaces();
        update();
    }

    private Geometry getGeometry() {
        Geometry geometry = new Geometry(nPolys * 3);
        for (int i = 0; i < nPolys; i++) {
            for (int j = 0; j < 3; j++) {
                geometry.source[i * 3 + j].x = Math.random() * startSize - startSize / 2D;
                geometry.source[i * 3 + j].y = Math.random() * startSize * 4D;
                geometry.source[i * 3 + j].z = Math.random() * startSize - startSize / 2D;
            }

            delta[i] = new Point3d(Math.random() * (startStrength * 2D) - startStrength, Math.random() * (startStrength * 2D) - startStrength, Math.random() * (startStrength * 2D) - startStrength);
        }
        geometry.copyDest();
        return geometry;
    }

    private PolyPrimitive[] getFaces() {
        PolyPrimitive pp[] = new PolyPrimitive[nPolys];
        for (int i = 0; i < nPolys; i++) {
            VertConnection vc[] = new VertConnection[3];
            vc[0] = new VertConnection(i * 3);
            vc[1] = new VertConnection(i * 3 + 1);
            vc[2] = new VertConnection(i * 3 + 2);
            pp[i] = new PolyPrimitive(new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));
            pp[i].setConnections(vc);
        }
        return pp;
    }

    public boolean update(double d) {
        for (int i = 0; i < nPolys; i++) {
            for (int j = 0; j < 3; j++) {
                super.geometry.source[i * 3 + j].x += delta[i].x * d;
                super.geometry.source[i * 3 + j].y += delta[i].y * d;
                super.geometry.source[i * 3 + j].z += delta[i].z * d;
            }
            delta[i].y += gravity * d;
        }
        counter += d;
        return counter > lifeTime;
    }

    public void kill() {}
}
