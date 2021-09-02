package game;

import game.modifiers.Modifier;
import java.util.Enumeration;
import java.util.Vector;
import litecom.Debug;
import litecom.inca.Point3d;

public class Sector {

    public Vector<Platform> platforms;
    public Modifier modifier;
    public String name;
    public boolean killMe;
    public Point3d segPos;

    public Sector(String name, Modifier modifier) {
        killMe = false;
        this.modifier = modifier;
        this.name = name;
        platforms = new Vector<Platform>();
    }

    public void addPlatform(Platform platform) {
        platforms.addElement(platform);
        platform.owner = this;
        platform.del = modifier.delta;
        updateSegPos();
    }

    public void updateSegPos() {
        if (platforms.size() == 0)
            return;
        
        Point3d point3d = new Point3d();
        for (int i = 0; i < platforms.size(); i++) {
            Platform platform = (Platform)platforms.elementAt(i);
            if (i == 0)
                point3d = new Point3d(platform.pos);
            else
                point3d.add(platform.pos);
        }

        point3d.div(platforms.size());
        segPos = World.makeSegPos(point3d);
        modifier.setSegPos(segPos);
    }

    public void removePlatform(Platform platform) {
        Debug.out("removing platform from " + this);
        platforms.removeElement(platform);
        platform.owner = null;
        platform.del = new Point3d();
    }

    public void update(double d) {
        killMe = !modifier.update(d);
        Platform platform;
        for (Enumeration<Platform> enumeration = platforms.elements(); enumeration.hasMoreElements(); platform.move(d))
            platform = (Platform)enumeration.nextElement();

        Point3d point3d = new Point3d(modifier.delta);
        point3d.x /= 100D;
        point3d.y /= 100D;
        point3d.z /= 100D;
        point3d.mul(d);
        segPos.add(point3d);
    }

    public void reset() {
        if (modifier.resetable()) {
            Debug.out(this, "Reseting...");
            for (int i = 0; i < platforms.size(); i++) {
                Platform platform = (Platform)platforms.elementAt(i);
                core.world.hidePlatform(platform);
                platform.pos.x = platform.startPos.x;
                platform.pos.y = platform.startPos.y;
                platform.pos.z = platform.startPos.z;
                core.world.showPlatform(platform);
            }
            updateSegPos();
            modifier.reset();
        }
    }

    public void kill() {
        Platform aplatform[] = new Platform[platforms.size()];
        platforms.copyInto(aplatform);
        for (int i = 0; i < aplatform.length; i++)
            removePlatform(aplatform[i]);
    }

    public String toString() {
        return "Sector(" + name + ")";
    }
}
