package game.modifiers;

import game.core;
//import game.ib;
import java.io.DataInputStream;
import java.io.DataOutputStream;
//import litecom.inca.mb;
//import litecom.sound.tb;

public class ButtonModifier extends Modifier {

    double len;
    int flag;
    double p;
    boolean firstTime;
    boolean lastHadFlagDown;

    public ButtonModifier() {
        len = 10D;
        firstTime = false;
        lastHadFlagDown = false;
    }

    public boolean update(double d) {
        double d1 = p;
        if (firstTime) {
            p = len;
            firstTime = false;
        } else if (core.world.flag[flag]) {
            if (!lastHadFlagDown)
                core.soundManager.play("tick1");
            lastHadFlagDown = true;
            p -= 250D * d;
            if (p < 0.0)
                p = 0.0;
        } else {
            if (lastHadFlagDown)
                core.soundManager.play("tick2");
            lastHadFlagDown = false;
            p += 250D * d;
            if (p > len)
                p = len;
        }
        double d2 = p - d1;
        d2 /= d;
        super.delta.y = d2;
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
        return "Button";
    }
}
