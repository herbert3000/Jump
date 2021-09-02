package game;

import game.modifiers.Modifier;
import java.io.*;
import java.net.URL;
//import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Vector;
import litecom.Debug;
import litecom.inca.*;
import litecom.Progressor;

public class World {

    public static double gravity = -1500D;
    public static int xlen;
    public static int ylen;
    public static int zlen;
    public static int MAGIC = 0xabcdef;
    public static int maxFlags = 120;
    public Segment segment[][][];
    public Vector<Platform> platforms;
    public Vector<Sector> sectors;
    public Vector<Sensor> sensors;
    public Vector<Sprite> sprites;
    public String bgImageFn;
    public boolean bgWhite;
    public boolean flag[];
    public Point3d playerStartPos;
    public Point3d playerStartRot;

    public World() {
        bgImageFn = "";
        bgWhite = false;
        flag = new boolean[maxFlags];
        playerStartPos = new Point3d();
        playerStartRot = new Point3d();
        Debug.out(this, "<init>");
    }

    public void updateSprites(double d) {
        if (sprites == null)
            return;
        Sprite asprites[] = new Sprite[sprites.size()];
        sprites.copyInto(asprites);
        for (int i = 0; i < asprites.length; i++)
            asprites[i].update(d);
    }

    public void createWorld() {
        clearWorld();
        platforms = new Vector<Platform>();
        sectors = new Vector<Sector>();
        sensors = new Vector<Sensor>();
        sprites = new Vector<Sprite>();
        segment = new Segment[xlen][ylen][zlen];
        for (int i = 0; i < xlen; i++) {
            for (int j = 0; j < ylen; j++) {
                for (int k = 0; k < zlen; k++)
                    segment[i][j][k] = new Segment(new Point3d(i, j, k));
            }
        }
    }

    public void clearFlags() {
        for (int i = 0; i < maxFlags; i++)
            flag[i] = false;
    }

    public void resetSectors() {
        Sector sector;
        for(Enumeration<Sector> enumeration = sectors.elements(); enumeration.hasMoreElements(); sector.reset())
            sector = (Sector)enumeration.nextElement();
    }

    public void addSensor(Sector sensor) {
        sectors.addElement(sensor);
    }

    public Sector getSector(String s) {
        for (Enumeration<Sector> enumeration = sectors.elements(); enumeration.hasMoreElements();) {
            Sector sector = (Sector)enumeration.nextElement();
            if (sector.name.equals(s))
                return sector;
        }
        return null;
    }

    public void removeSector(Sector sector) {
        sector.kill();
        sectors.removeElement(sector);
    }

    public void addSensor(Sensor sensor) {
        sensors.addElement(sensor);
    }

    public Sensor getSensor(String s){
        for (Enumeration<Sensor> enumeration = sensors.elements(); enumeration.hasMoreElements();) {
            Sensor sensor = (Sensor)enumeration.nextElement();
            if (sensor.name.equals(s))
                return sensor;
        }
        return null;
    }

    public void removeSensor(Sensor sensor) {
        sensors.removeElement(sensor);
    }

    public void addSprite(Sprite sprite) {
        sprites.addElement(sprite);
    }

    public void removeSprite(Sprite sprite) {
        sprites.removeElement(sprite);
    }

    public void addPlatform(Platform platform) {
        platforms.addElement(platform);
        showPlatform(platform);
    }

    public void removePlatform(Platform platform) {
        hidePlatform(platform);
        core.vWorld.removeShape(platform.s);
        platforms.removeElement(platform);
    }

    private void addPlatformAt(Platform platform, Point3d point3d) {
        int i = (int)(point3d.x / 100D);
        int j = (int)(point3d.y / 100D);
        int k = (int)(point3d.z / 100D);
        if (i < 0 || j < 0 || k < 0 || i >= xlen || j >= ylen || k >= zlen)
            return;
        if (!segment[i][j][k].hasPlatform(platform))
            segment[i][j][k].addPlatform(platform);
    }

    private void removePlatformAt(Platform platform, Point3d point3d) {
        int i = (int)(point3d.x / 100D);
        int j = (int)(point3d.y / 100D);
        int k = (int)(point3d.z / 100D);
        if (i < 0 || j < 0 || k < 0 || i >= xlen || j >= ylen || k >= zlen)
            return;
        if (segment[i][j][k].hasPlatform(platform))
            segment[i][j][k].nukePlatform(platform);
    }

    public void showPlatform(Platform platform) {
        int i = 0;
        int j = 0;
        int k = 0;
        if (platform.del.x != 0.0D)
            i = 1;
        if (platform.del.y != 0.0D)
            j = 1;
        if (platform.del.z != 0.0D)
            k = 1;
        for (int i1 = -i; i1 <= i; i1++) {
            for (int j1 = -j; j1 <= j; j1++) {
                for (int k1 = -k; k1 <= k; k1++)
                    addPlatformAt(platform, new Point3d(platform.pos.x + 49D * (double)i1, platform.pos.y + 49D * (double)j1, platform.pos.z + 49D * (double)k1));
            }
        }
    }

    public void hidePlatform(Platform platform) {
        int i = 0;
        int j = 0;
        int k = 0;
        if (platform.del.x != 0.0D)
            i = 1;
        if (platform.del.y != 0.0D)
            j = 1;
        if (platform.del.z != 0.0D)
            k = 1;
        for (int i1 = -i; i1 <= i; i1++) {
            for (int j1 = -j; j1 <= j; j1++) {
                for (int k1 = -k; k1 <= k; k1++)
                    removePlatformAt(platform, new Point3d(platform.pos.x + 49D * (double)i1, platform.pos.y + 49D * (double)j1, platform.pos.z + 49D * (double)k1));
            }
        }
    }

    public void clearWorld() {
        if (platforms != null) {
            int i = platforms.size();
            for (int j = 0; j < i; j++)
                removePlatform((Platform)platforms.firstElement());
        }
        if (sensors != null) {
            int i = sensors.size();
            for (int j = 0; j < i; j++)
                removeSensor((Sensor)sensors.firstElement());
        }
        if (sectors != null) {
            int i = sectors.size();
            for (int j = 0; j < i; j++)
                removeSector((Sector)sectors.firstElement());
        }
        if (sprites != null) {
            int i = sprites.size();
            for (int j = 0; j < i; j++)
                ((Sprite)sprites.firstElement()).removeSprite();
        }
    }

    public boolean isSolid(double d, double d1, double d2) {
        int i = (int)(d / 100D);
        int j = (int)(d1 / 100D);
        int k = (int)(d2 / 100D);
        if (d < 0.0D)
            i--;
        if (d1 < 0.0D)
            j--;
        if (d2 < 0.0D)
            k--;
        if (i < 0 || i >= xlen || j < 0 || j >= ylen || k < 0 || k >= zlen)
            return false;
        else
            return segment[i][j][k].isSolid(d, d1, d2);
    }

    public Platform getPlatform(double d, double d1, double d2) {
        int i = (int)(d / 100D);
        int j = (int)(d1 / 100D);
        int k = (int)(d2 / 100D);
        if (d < 0.0D)
            i--;
        if (d1 < 0.0D)
            j--;
        if (d2 < 0.0D)
            k--;
        if (i < 0 || i >= xlen || j < 0 || j >= ylen || k < 0 || k >= zlen)
            return null;
        else
            return segment[i][j][k].getPlatform(d, d1, d2);
    }

    public Segment getSegment(Point3d point3d) {
        int i = (int)(point3d.x / 100D);
        int j = (int)(point3d.y / 100D);
        int k = (int)(point3d.z / 100D);
        if (point3d.x < 0.0D)
            i--;
        if (point3d.y < 0.0D)
            j--;
        if (point3d.z < 0.0D)
            k--;
        if (i < 0 || i >= xlen || j < 0 || j >= ylen || k < 0 || k >= zlen)
            return null;
        else
            return segment[i][j][k];
    }

    public void optimizePlatforms(Progressor progressor) {
        optimizeFaces(progressor);
        optimizeVerts();
    }

    private void optimizeFaces(Progressor progressor) {
        double d = 1 / (xlen * ylen * zlen) / 3;
        int i = 0;
        int j = 0;
        for (int k = 0; k < ylen; k++) {
            for (int k1 = 0; k1 < zlen; k1++) {
                for (int k2 = 0; k2 < xlen; k2++) {
                    if (segment[k2][k][k1].isSolid())
                        i += 6;
                    if (k2 < xlen - 1 && segment[k2][k][k1].isSolid() && segment[k2 + 1][k][k1].isSolid() && segment[k2][k][k1].platform[0].owner == segment[k2 + 1][k][k1].platform[0].owner) {
                        j++;
                        segment[k2][k][k1].platform[0].s.primitive[1] = null;
                    }
                    if (k2 > 0 && segment[k2][k][k1].isSolid() && segment[k2 - 1][k][k1].isSolid() && segment[k2][k][k1].platform[0].owner == segment[k2 - 1][k][k1].platform[0].owner) {
                        j++;
                        segment[k2][k][k1].platform[0].s.primitive[2] = null;
                    }
                    if (progressor != null)
                        progressor.progress(d);
                }
            }
        }

        for (int l = 0; l < ylen; l++) {
            for (int l1 = 0; l1 < xlen; l1++) {
                for (int l2 = 0; l2 < zlen; l2++) {
                    if (l2 < zlen - 1 && segment[l1][l][l2].isSolid() && segment[l1][l][l2 + 1].isSolid() && segment[l1][l][l2].platform[0].owner == segment[l1][l][l2 + 1].platform[0].owner) {
                        j++;
                        segment[l1][l][l2].platform[0].s.primitive[0] = null;
                    }
                    if (l2 > 0 && segment[l1][l][l2].isSolid() && segment[l1][l][l2 - 1].isSolid() && segment[l1][l][l2].platform[0].owner == segment[l1][l][l2 - 1].platform[0].owner) {
                        j++;
                        segment[l1][l][l2].platform[0].s.primitive[5] = null;
                    }
                    if (progressor != null)
                        progressor.progress(d);
                }
            }
        }
        
        for (int i1 = 0; i1 < zlen; i1++) {
            for (int i2 = 0; i2 < xlen; i2++) {
                for (int i3 = 0; i3 < ylen; i3++) {
                    if (i3 < ylen - 1 && segment[i2][i3][i1].isSolid() && segment[i2][i3 + 1][i1].isSolid() && segment[i2][i3][i1].platform[0].owner == segment[i2][i3 + 1][i1].platform[0].owner) {
                        j++;
                        segment[i2][i3][i1].platform[0].s.primitive[3] = null;
                    }
                    if (i3 > 0 && segment[i2][i3][i1].isSolid() && segment[i2][i3 - 1][i1].isSolid() && segment[i2][i3][i1].platform[0].owner == segment[i2][i3 - 1][i1].platform[0].owner) {
                        j++;
                        segment[i2][i3][i1].platform[0].s.primitive[4] = null;
                    }
                    if (progressor != null)
                        progressor.progress(d);
                }
            }
        }

        for (int j1 = 0; j1 < ylen; j1++) {
            for (int j2 = 0; j2 < xlen; j2++) {
                for (int j3 = 0; j3 < zlen; j3++) {
                    if (segment[j2][j1][j3].isSolid())
                        segment[j2][j1][j3].platform[0].s.updatePrims();
                }
            }
        }
        
        Debug.out("Optimized: " + j + "/" + i + " faces.");
    }

    private void optimizeVerts() {
        int i = 0;
        int j = 0;
        for (int k = 0; k < ylen; k++) {
            for (int i1 = 0; i1 < xlen; i1++) {
                for (int k1 = 0; k1 < zlen; k1++) {
                    if (segment[i1][k][k1].isSolid()) {
                        Shape3d ob1 = segment[i1][k][k1].platform[0].s;
                        boolean aflag[] = new boolean[ob1.geometry.source.length];
                        i += ob1.geometry.source.length;
                        for (int j2 = 0; j2 < ob1.geometry.source.length; j2++)
                            aflag[j2] = false;

                        for (int k2 = 0; k2 < ob1.primitive.length; k2++) {
                            Primitive gb1 = ob1.primitive[k2];
                            for (int i3 = 0; i3 < gb1.vert.length; i3++)
                                aflag[gb1.vert[i3].index] = true;
                        }

                        for (int l2 = 0; l2 < ob1.geometry.source.length; l2++) {
                            if (!aflag[l2]) {
                                ob1.geometry.source[l2] = null;
                                j++;
                            }
                        }
                    }
                }
            }
        }

        for (int l = 0; l < ylen; l++) {
            for (int j1 = 0; j1 < xlen; j1++) {
                for (int l1 = 0; l1 < zlen; l1++) {
                    if (segment[j1][l][l1].isSolid())
                        segment[j1][l][l1].platform[0].s.updateVerts();
                }
            }
        }

        Debug.out("Optimized: " + j + "/" + i + " verts.");
    }

    private void writeString(DataOutputStream dataoutputstream, String s) throws Exception {
        for (int i = 0; i < s.length(); i++)
            dataoutputstream.writeChar(s.charAt(i));

        dataoutputstream.writeChar(10);
    }

    private String readString(DataInputStream datainputstream) throws Exception {
        char c;
        String s;
        for(s = ""; (c = datainputstream.readChar()) != '\n'; s = s + c);
        return s;
    }

    public void saveLevel(String s) throws Exception {
        Debug.out("Saving level to " + s + "...");
        DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(s));
        dataoutputstream.writeInt(MAGIC);
        dataoutputstream.writeInt(xlen);
        dataoutputstream.writeInt(ylen);
        dataoutputstream.writeInt(zlen);
        Debug.out("\tsize: (" + xlen + ", " + ylen + ", " + zlen + ")");
        dataoutputstream.writeFloat((float)playerStartPos.x);
        dataoutputstream.writeFloat((float)playerStartPos.y);
        dataoutputstream.writeFloat((float)playerStartPos.z);
        dataoutputstream.writeFloat((float)playerStartRot.y);
        writeString(dataoutputstream, bgImageFn);
        if (bgWhite)
            dataoutputstream.writeByte(1);
        else
            dataoutputstream.writeByte(0);
        dataoutputstream.writeByte(sectors.size());
        for (int i = 0; i < sectors.size(); i++) {
            Sector sector = (Sector)sectors.elementAt(i);
            String s1 = sector.modifier.getClass().getName();
            Debug.out("\tsector: " + sector.name + "/" + s1);
            writeString(dataoutputstream, sector.name);
            writeString(dataoutputstream, s1);
            sector.modifier.save(dataoutputstream);
        }

        dataoutputstream.writeByte(sensors.size());
        for (int j = 0; j < sensors.size(); j++) {
            Sensor sensor = (Sensor)sensors.elementAt(j);
            Debug.out("\tsensor: " + sensor.name + "/" + sensor.destFlag);
            writeString(dataoutputstream, sensor.name);
            dataoutputstream.writeByte(sensor.destFlag);
            dataoutputstream.writeByte((byte)sensor.key);
            if (sensor.button)
                dataoutputstream.writeByte(1);
            else
                dataoutputstream.writeByte(0);
        }

        for (int x = 0; x < xlen; x++) {
            for (int y = 0; y < ylen; y++) {
                for (int z = 0; z < zlen; z++) {
                    Segment seg = segment[x][y][z];
                    if (seg.sensor == null)
                        dataoutputstream.writeByte(-1);
                    else
                        dataoutputstream.writeByte(sensors.indexOf(seg.sensor));
                    if (seg.sprite == null)
                        dataoutputstream.writeByte(-1);
                    else
                        dataoutputstream.writeByte(SpriteHolder.getIndex(seg.sprite));
                    if (seg.platform[0] != null) {
                        Platform platform = seg.platform[0];
                        if (platform.xored)
                            dataoutputstream.writeByte(-platform.set);
                        else
                            dataoutputstream.writeByte(platform.set);
                        if (platform.owner == null)
                            dataoutputstream.writeByte(-1);
                        else
                            dataoutputstream.writeByte(sectors.indexOf(platform.owner));
                    } else {
                        dataoutputstream.writeByte(0);
                    }
                }
            }
        }

        dataoutputstream.close();
        Debug.out("Done!");
    }

    public void loadLevel(URL url, String s) throws Exception {
        loadLevel(url, s, null);
    }

    public void loadLevel(URL url, String s, Progressor progressor) throws Exception {
        double d = 2D;
        new Point3d();
        Debug.out("Loading level: " + s + "...");
        URL url1 = null;
        url1 = new URL(url, s);
        DataInputStream datainputstream = new DataInputStream(url1.openConnection().getInputStream());
        clearWorld();
        if (datainputstream.readInt() != MAGIC)
            throw new Exception("Not a level file!");
        xlen = datainputstream.readInt();
        ylen = datainputstream.readInt();
        zlen = datainputstream.readInt();
        Debug.out("\tsize: (" + xlen + ", " + ylen + ", " + zlen + ")");
        createWorld();
        playerStartPos.x = datainputstream.readFloat();
        playerStartPos.y = datainputstream.readFloat();
        playerStartPos.z = datainputstream.readFloat();
        playerStartRot.x = 0.0D;
        playerStartRot.y = datainputstream.readFloat();
        playerStartRot.z = 0.0D;
        bgImageFn = readString(datainputstream);
        bgWhite = datainputstream.readByte() == 1;
        int i = datainputstream.readByte();
        Debug.out("\tsectors: " + i);
        for (int j = 0; j < i; j++) {
            String s1 = readString(datainputstream);
            String s2 = readString(datainputstream);
            Modifier modifier = (Modifier)Class.forName(s2).newInstance();
            modifier.load(datainputstream);
            Sector sector = new Sector(s1, modifier);
            Debug.out("\tsector: " + sector.name + "/" + s2);
            addSensor(sector);
            if (progressor != null)
                progressor.progress(0.5D / (double)i);
        }

        if(i != 0)
            d -= 0.5D;
        int k = datainputstream.readByte();
        Debug.out("\tsensors: " + k);
        for (int l = 0; l < k; l++) {
            String s3 = readString(datainputstream);
            Sensor sensor = new Sensor(s3);
            sensor.destFlag = datainputstream.readByte();
            sensor.key = datainputstream.readByte();
            sensor.button = datainputstream.readByte() == 1;
            Debug.out("\tsensor: " + sensor.name + "/" + sensor.destFlag);
            addSensor(sensor);
            if (progressor != null)
                progressor.progress(0.5D / (double)k);
        }

        if(k != 0)
            d -= 0.5D;
        Debug.out("\tloading data...");
        for (int i1 = 0; i1 < xlen; i1++) {
            for (int j1 = 0; j1 < ylen; j1++) {
                for (int k1 = 0; k1 < zlen; k1++) {
                    Segment seg = segment[i1][j1][k1];
                    byte byte0 = datainputstream.readByte();
                    if (byte0 >= 0)
                        seg.sensor = (Sensor)sensors.elementAt(byte0);
                    byte0 = datainputstream.readByte();
                    if (byte0 >= 0)
                        seg.sprite = SpriteHolder.getSprite(byte0, new Point3d(i1, j1, k1));
                    int i2 = datainputstream.readByte();
                    if (i2 != 0) {
                        Point3d point3d = new Point3d((double)i1 * 100D + 50D, (double)j1 * 100D + 50D, (double)k1 * 100D + 50D);
                        Platform platform = new Platform(point3d);
                        platform.set = i2;
                        if (platform.set < 0) {
                            platform.set = -platform.set;
                            platform.setXORMode(true);
                        }
                        byte byte1 = datainputstream.readByte();
                        if (byte1 >= 0) {
                            Sector sector = (Sector)sectors.elementAt(byte1);
                            sector.addPlatform(platform);
                        }
                        platform.colorFaces();
                        addPlatform(platform);
                    }
                    if (progressor != null)
                        progressor.progress(d / (double)(xlen * ylen * zlen));
                }
            }
        }

        datainputstream.close();
        Debug.out("Done!");
    }

    public static Point3d makePos(Point3d point3d) {
        return new Point3d(point3d.x * 100D + 50D, point3d.y * 100D + 50D, point3d.z * 100D + 50D);
    }

    public static Point3d makeSegPos(Point3d point3d) {
        return new Point3d((int)(point3d.x / 100D), (int)(point3d.y / 100D), (int)(point3d.z / 100D));
    }
}
