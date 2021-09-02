package litecom.inca;

import java.awt.Color;
import java.io.*;
import java.net.URL;
//import java.net.URLConnection;
import litecom.Debug;
import litecom.Progressor;

public class IncaFile {

    public static final int INCA_ID = 47806;
    public static final int CHUNK_ID = 666;
    public static final int CHUNK_GEOMETRY = 1;
    public static final int CHUNK_FACES = 2;
    public static final int CHUNK_NORMALS = 3;
    public static final int CHUNK_END = 4;

    public static void saveShape(Shape3d shape3d, String s) throws Exception {
        Debug.out("Saving shape to " + s + "...");
        DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(s));
        dataoutputstream.writeInt(INCA_ID);
        int i = shape3d.geometry.source.length;
        if (shape3d.primitive != null)
            i += shape3d.primitive.length;
        if (shape3d.normal != null)
            i += shape3d.normal.source.length;
        dataoutputstream.writeInt(i);
        dataoutputstream.writeInt(CHUNK_ID);
        dataoutputstream.writeInt(CHUNK_GEOMETRY);
        dataoutputstream.writeInt(shape3d.geometry.source.length);
        for (int j = 0; j < shape3d.geometry.source.length; j++) {
            dataoutputstream.writeFloat((float)shape3d.geometry.source[j].x);
            dataoutputstream.writeFloat((float)shape3d.geometry.source[j].y);
            dataoutputstream.writeFloat((float)shape3d.geometry.source[j].z);
        }

        if (shape3d.primitive != null) {
            dataoutputstream.writeInt(CHUNK_ID);
            dataoutputstream.writeInt(CHUNK_FACES);
            dataoutputstream.writeInt(shape3d.primitive.length);
            for (int k = 0; k < shape3d.primitive.length; k++)
                if (shape3d.primitive[k] instanceof PolyPrimitive) {
                    PolyPrimitive polyprimitive = (PolyPrimitive)shape3d.primitive[k];
                    dataoutputstream.writeInt(((Primitive) (polyprimitive)).vert.length);
                    for (int m = 0; m < ((Primitive) (polyprimitive)).vert.length; m++)
                        dataoutputstream.writeShort((short)((Primitive) (polyprimitive)).vert[m].index);

                    dataoutputstream.writeShort((short)((Primitive) (polyprimitive)).color.getRed());
                    dataoutputstream.writeShort((short)((Primitive) (polyprimitive)).color.getGreen());
                    dataoutputstream.writeShort((short)((Primitive) (polyprimitive)).color.getBlue());
                } else {
                    throw new Exception("Inca files only support PolyPrimitives.");
                }
        }
        if (shape3d.normal != null) {
            dataoutputstream.writeInt(CHUNK_ID);
            dataoutputstream.writeInt(CHUNK_NORMALS);
            dataoutputstream.writeInt(shape3d.normal.source.length);
            for (int m = 0; m < shape3d.normal.source.length; m++) {
                dataoutputstream.writeFloat((float)shape3d.normal.source[m].x);
                dataoutputstream.writeFloat((float)shape3d.normal.source[m].y);
                dataoutputstream.writeFloat((float)shape3d.normal.source[m].z);
            }
        }
        dataoutputstream.writeInt(CHUNK_ID);
        dataoutputstream.writeInt(CHUNK_END);
        dataoutputstream.close();
        Debug.out("Done!");
    }

    public static Shape3d loadShape(String s) throws Exception {
        Debug.out("Loading shape(no url): " + s + "...");
        return loadShape(new DataInputStream(new FileInputStream(s)), null);
    }

    public static Shape3d loadShape(URL url, String s) throws Exception {
        return loadShape(url, s, null);
    }

    public static Shape3d loadShape(URL url, String s, Progressor progressor) throws Exception {
        Debug.out("Loading shape: " + s + "...");
        URL url1 = null;
        url1 = new URL(url, s);
        return loadShape(new DataInputStream(url1.openConnection().getInputStream()), progressor);
    }

    private static Shape3d loadShape(DataInputStream datainputstream, Progressor progressor) throws Exception {
        if (datainputstream.readInt() != INCA_ID)
            throw new Exception("Not an inca file.");
        double d = datainputstream.readInt();
        Shape3d shape3d = new Shape3d();
        boolean flag = true;
        while (flag)  {
            if (datainputstream.readInt() != CHUNK_ID)
                throw new Exception("Corrupt inca file.");
            int type = datainputstream.readInt();
            switch(type) {
            default:
                break;

            case CHUNK_GEOMETRY:
                int j = datainputstream.readInt();
                Debug.out("\tgeometry: " + j + " verts");
                shape3d.geometry = new Geometry(j);
                for (int k = 0; k < j; k++) {
                    shape3d.geometry.source[k].x = datainputstream.readFloat();
                    shape3d.geometry.source[k].y = datainputstream.readFloat();
                    shape3d.geometry.source[k].z = datainputstream.readFloat();
                    if (progressor != null)
                        progressor.progress(1.0D / d);
                }
                break;

            case CHUNK_FACES:
                int m = datainputstream.readInt();
                Debug.out("\tprimitives: " + m + " faces");
                shape3d.primitive = new PolyPrimitive[m];
                for (int i1 = 0; i1 < m; i1++) {
                    int k1 = datainputstream.readInt();
                    VertConnection vc[] = new VertConnection[k1];
                    for (int i2 = 0; i2 < k1; i2++)
                        vc[i2] = new VertConnection(datainputstream.readShort());

                    PolyPrimitive pp = new PolyPrimitive(new Color(datainputstream.readShort(), datainputstream.readShort(), datainputstream.readShort()));
                    pp.setConnections(vc);
                    shape3d.primitive[i1] = pp;
                    if (progressor != null)
                        progressor.progress(1.0D / d);
                }
                break;

            case CHUNK_NORMALS:
                int j1 = datainputstream.readInt();
                Debug.out("\tnormals: " + j1 + " vectors");
                shape3d.normal = new Geometry(j1);
                for (int i2 = 0; i2 < j1; i2++) {
                    shape3d.normal.source[i2].x = datainputstream.readFloat();
                    shape3d.normal.source[i2].y = datainputstream.readFloat();
                    shape3d.normal.source[i2].z = datainputstream.readFloat();
                    if (progressor != null)
                        progressor.progress(1.0D / d);
                }
                break;

            case CHUNK_END:
                Debug.out("\tend!");
                flag = false;
                break;
            }
        }
        shape3d.update();
        Debug.out("Done!");
        return shape3d;
    }
}
