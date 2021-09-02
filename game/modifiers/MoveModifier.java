package game.modifiers;

import game.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import litecom.inca.Point3d;
//import litecom.sound.tb;

public class MoveModifier extends Modifier {

    public static final byte DIR_X = 1;
    public static final byte DIR_Y = 2;
    public static final byte DIR_Z = 3;
    public byte dir;
    public double phase;
    public double speed1;
    public double speed2;
    public double delay1;
    public double delay2;
    public double len;
    private double p;
    private double timer;
    private int state;

    public MoveModifier() {
        state = -1;
    }

    private void setState(int i) {
        state = i;
        timer = 0.0D;
    }

    private boolean isDone(double d) {
        if (d == 0.0D)
            return false;
        if (d < 0.0D)
            return core.world.flag[(int)(-d)];
        return timer > d;
    }

    private void play(String s) {
        Point3d mb1 = core.player.pos;
        if (super.segPos.dist(new Point3d((int)(mb1.x / 100D), (int)(mb1.y / 100D), (int)(mb1.z / 100D))) < 6D)
            core.soundManager.play(s);
    }

    public boolean update(double d) {
        double d1 = p;
        double d2 = Math.abs(len);
        switch(state) {
        default:
            break;

        case -1: 
            double d3 = phase;
            if (d3 != 0.0D ? d3 >= 0.0D ? timer > d3 || false : core.world.flag[(int)(-d3)] : false) {
                state = 0;
                timer = 0.0D;
                play("hiss-start");
            }
            break;

        case 0:
            p += speed1 * d;
            if (p < d2)
                break;
            p = d2;
            timer = 0.0D;
            state = 1;
            timer = 0.0D;
            play("hiss-stopp");
            break;

        case 1:
            double d4 = delay2;
            if(d4 != 0.0D ? d4 >= 0.0D ? timer <= d4 && true : !core.world.flag[(int)(-d4)] : true)
                break;
            state = 2;
            timer = 0.0D;
            play("hiss-start");
            break;

        case 2:
            p -= speed2 * d;
            if (p > 0.0D)
                break;
            p = 0.0D;
            timer = 0.0D;
            state = 3;
            timer = 0.0D;
            play("hiss-stopp");
            break;

        case 3:
            double d5 = delay1;
            if (d5 != 0.0D ? d5 >= 0.0D ? timer <= d5 && true : !core.world.flag[(int)(-d5)] : true)
                break;
            state = 0;
            timer = 0.0D;
            play("hiss-start");
            break;
        }
        timer += d;
        double d6 = p - d1;
        double d7 = len / Math.abs(len);
        d6 /= d;
        if (dir == 1)
            super.delta.x = d6 * d7;
        else if (dir == 2)
            super.delta.y = d6 * d7;
        else
            super.delta.z = d6 * d7;
        return true;
    }

    public void save(DataOutputStream dataoutputstream) throws Exception {
        dataoutputstream.writeByte(dir);
        dataoutputstream.writeFloat((float)phase);
        dataoutputstream.writeFloat((float)speed1);
        dataoutputstream.writeFloat((float)speed2);
        dataoutputstream.writeFloat((float)delay1);
        dataoutputstream.writeFloat((float)delay2);
        dataoutputstream.writeFloat((float)len);
    }

    public void load(DataInputStream datainputstream) throws Exception {
        dir = datainputstream.readByte();
        phase = datainputstream.readFloat();
        speed1 = datainputstream.readFloat();
        speed2 = datainputstream.readFloat();
        delay1 = datainputstream.readFloat();
        delay2 = datainputstream.readFloat();
        len = datainputstream.readFloat();
    }

    public String getName() {
        return "Move";
    }
}
