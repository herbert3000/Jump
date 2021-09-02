package game.modifiers;

import game.core;
//import game.eb;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import litecom.inca.Point3d;
//import litecom.sound.tb;

public class SineModifier extends Modifier {

    public double amp;
    public double phase;
    public double period;
    public double dir;
    private Point3d p;
    double time;
    double lastc;

    public SineModifier() {
        p = new Point3d();
    }

    public SineModifier(double d, double d1, double d2, double d3) {
        p = new Point3d();
        amp = d;
        phase = d1;
        period = d2;
        dir = d3;
    }

    public boolean update(double d) {
        double d1 = 0.0D;
        double d2 = p.x;
        double d3 = (d1 + amp * Math.sin((Math.PI * 2 * time) / period + (phase / 180D) * Math.PI)) - d2;
        d3 /= d;
        if (dir == 0.0D)
            super.delta.x = d3;
        else if (dir == 1.0D)
            super.delta.y = d3;
        else
            super.delta.z = d3;
        p.x += d3 * d;
        Point3d mb1;
        if ((lastc > 0.0D && d3 < 0.0D || lastc < 0.0D && d3 > 0.0D) && super.segPos.dist(new Point3d((int)((mb1 = core.player.pos).x / 100D), (int)(mb1.y / 100D), (int)(mb1.z / 100D))) < 5D)
            core.soundManager.play("sinehiss");
        time += d;
        lastc = d3;
        return true;
    }

    public void save(DataOutputStream dataoutputstream) throws Exception {
        dataoutputstream.writeFloat((float)amp);
        dataoutputstream.writeFloat((float)phase);
        dataoutputstream.writeFloat((float)period);
        dataoutputstream.writeFloat((float)dir);
    }

    public void load(DataInputStream datainputstream) throws Exception {
        amp = datainputstream.readFloat();
        phase = datainputstream.readFloat();
        period = datainputstream.readFloat();
        dir = datainputstream.readFloat();
    }

    public String getName() {
        return "Sine";
    }
}
