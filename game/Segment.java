package game;

import litecom.Debug;
import litecom.inca.Point3d;

public class Segment {

    public static final double xlen = 100D;
    public static final double ylen = 100D;
    public static final double zlen = 100D;
    public static final int maxPlatforms = 4;
    public Platform platform[];
    public Point3d segPos;
    public Sensor sensor;
    public Sprite sprite;

    public Segment(Point3d point3d) {
        platform = new Platform[4];
        segPos = point3d;
    }

    public boolean hasPlatform(Platform platform1) {
        for (int i = 0; i < 4; i++) {
            if (platform[i] == platform1)
                return true;
        }
        return false;
    }

    public void addPlatform(Platform platform1) {
        boolean flag = false;
        for (int i = 0; i < 4; i++) {
            if (platform[i] != null)
                continue;
            platform[i] = platform1;
            flag = true;
            break;
        }

        if (!flag)
            Debug.out("Warning! No space in segment.");
    }

    public void nukePlatform(Platform platform1) {
        boolean flag = false;
        for (int i = 0; i < 4; i++) {
            if (platform[i] != platform1)
                continue;
            platform[i] = null;
            flag = true;
            break;
        }

        if (!flag)
            Debug.out("Warning! Platform did not exist in segment.");
    }

    public boolean isSolid() {
        return isSolid(segPos.x * 100D + 50D, segPos.y * 100D + 50D, segPos.z * 100D + 50D);
    }

    public boolean isSolid(double x, double y, double z) {
        for (int i = 0; i < 4; i++) {
            if (platform[i] != null && x > platform[i].pos.x - 50D && y > platform[i].pos.y - 50D && z > platform[i].pos.z - 50D && x < platform[i].pos.x + 50D && y < platform[i].pos.y + 50D && z < platform[i].pos.z + 50D)
                return true;
        }
        return false;
    }

    public Platform getPlatform(double x, double y, double z) {
        for (int i = 0; i < 4; i++) {
            if (platform[i] != null) {
                Platform platform1 = platform[i];
                if (x > platform1.pos.x - 50D && y > platform1.pos.y - 50D && z > platform1.pos.z - 50D && x < platform1.pos.x + 50D && y < platform1.pos.y + 50D && z < platform1.pos.z + 50D)
                    return platform1;
            }
        }
        return null;
    }

    public String toString() {
        return segPos + " " + sensor + " " + sprite + " " + platform[0];
    }
}
