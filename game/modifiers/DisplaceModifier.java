package game.modifiers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import litecom.inca.Point3d;

public class DisplaceModifier extends Modifier {

    public Point3d displace;
    private boolean firstTime;

    public DisplaceModifier() {
        displace = new Point3d();
        firstTime = true;
    }

    public boolean update(double d) {
        if (firstTime) {
            super.delta.x = displace.x / d;
            super.delta.y = displace.y / d;
            super.delta.z = displace.z / d;
            firstTime = false;
            return true;
        } else {
            super.delta.x = 0.0D;
            super.delta.y = 0.0D;
            super.delta.z = 0.0D;
            return false;
        }
    }

    public void save(DataOutputStream dataoutputstream) throws Exception {
        dataoutputstream.writeFloat((float)displace.x);
        dataoutputstream.writeFloat((float)displace.y);
        dataoutputstream.writeFloat((float)displace.z);
    }

    public void load(DataInputStream datainputstream) throws Exception {
        displace.x = datainputstream.readFloat();
        displace.y = datainputstream.readFloat();
        displace.z = datainputstream.readFloat();
    }

    public String getName() {
        return "Displace";
    }
}
