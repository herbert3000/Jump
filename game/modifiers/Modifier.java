package game.modifiers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import litecom.inca.Point3d;

public class Modifier {

    public Point3d delta;
    public Point3d segPos;

    public Modifier() {
        delta = new Point3d();
    }

    public void setSegPos(Point3d mb1) {
        segPos = mb1;
    }

    public boolean resetable() {
        return false;
    }

    public void reset() {}

    public boolean update(double d) {
        return false;
    }

    public void save(DataOutputStream dataoutputstream) throws Exception {}

    public void load(DataInputStream datainputstream) throws Exception {}

    public String getName() {
        return "<undef>";
    }
}
