package game.modifiers;

import game.core;
import game.World;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import litecom.Debug;
//import litecom.inca.Point3d;

public class FallModifier extends Modifier {

    public double len;
    public int flag;
    double p;
    boolean falling;
    double acc;
    double s;

    public FallModifier() {
        falling = false;
        s = -1D;
    }

    public boolean update(double d) {
        if (s == -1D)
            s = super.segPos.x / 2D;
        if (p == -len) {
            super.delta.y = 0.0D;
            return false;
        }
        double d1 = p;
        if (core.world.flag[flag])
            falling = true;
        if (falling) {
            acc += World.gravity * d * 0.3D;
            p += acc * d;
        } else {
            p = Math.sin(s) * 6D;
            s += d * 6D;
        }
        if (p < -len)
            p = -len;
        double d2 = p - d1;
        d2 /= d;
        super.delta.y = d2;
        return true;
    }

    public void reset() {
        Debug.out(this, "Reseting...");
        p = 0.0D;
        falling = false;
        acc = 0.0D;
        s = 0.0D;
    }

    public boolean resetable() {
        return true;
    }

    public void save(DataOutputStream dataoutputstream) throws Exception {
        dataoutputstream.writeFloat((float)len);
        dataoutputstream.writeByte((byte)flag);
    }

    public void load(DataInputStream datainputstream) throws Exception {
        len = datainputstream.readFloat();
        flag = datainputstream.readByte();
    }

    public String getName() {
        return "Fall";
    }
}
