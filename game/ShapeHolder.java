package game;

//import java.applet.Applet;
import java.awt.Color;
import litecom.Debug;
import litecom.inca.*;
import litecom.Progressor;

public class ShapeHolder {

    public static final int nShapes = 11;
    public static final String path = "models/";
    private Shape3d shape[];

    public ShapeHolder() {
        shape = new Shape3d[11];
    }

    public void init() {
        init(null);
    }

    public void init(Progressor progressor) {
        try {
            shape[0] = IncaFile.loadShape(core.main.getDocumentBase(), "models/gubbe.i3d", progressor);
            shape[1] = IncaFile.loadShape(core.main.getDocumentBase(), "models/hjul.i3d", progressor);
            shape[2] = IncaFile.loadShape(core.main.getDocumentBase(), "models/star.i3d", progressor);
            shape[3] = IncaUtils.createCube(40D);
            for (int i = 0; i < 8; i++)
                if (i == 0 || i == 3 || i == 4 || i == 7) {
                    shape[3].geometry.source[i].x /= 2D;
                    shape[3].geometry.source[i].y /= 2D;
                    shape[3].geometry.source[i].z /= 2D;
                }

            shape[3].createNormals(-1D);
            IncaUtils.colorFaces(shape[3], new Color(220, 200, 250), Color.blue, false);
            if (progressor != null)
                progressor.progress(1.0D);
            shape[4] = IncaFile.loadShape(core.main.getDocumentBase(), "models/medkit.i3d", progressor);
            shape[5] = IncaFile.loadShape(core.main.getDocumentBase(), "models/key.i3d", progressor);
            shape[6] = new Shape3d(shape[5]);
            IncaUtils.colorFaces(shape[6], Color.gray, Color.white, false);
            if (progressor != null)
                progressor.progress(1.0D);
            shape[7] = new Shape3d(shape[5]);
            IncaUtils.colorFaces(shape[7], Color.blue, Color.cyan, false);
            if (progressor != null)
                progressor.progress(1.0D);
            shape[8] = new Shape3d(shape[5]);
            IncaUtils.colorFaces(shape[8], Color.red, Color.red.darker(), false);
            if (progressor != null)
                progressor.progress(1.0D);
            shape[9] = IncaFile.loadShape(core.main.getDocumentBase(), "models/crystal.i3d", progressor);
            shape[9].setXORMode(true);
            shape[10] = IncaUtils.Z(8, 4, 30D, 20D);
            shape[10].createNormals(-1D);
            IncaUtils.colorFaces(shape[10], new Color(220, 200, 100), new Color(250, 100, 50), true);
            if (progressor != null) {
                progressor.progress(1.0D);
                return;
            }
        } catch(Exception exception) {
            Debug.out("EXCEPTION: " + exception);
            exception.printStackTrace();
        }
    }

    public Shape3d getShape(int i) {
        return new Shape3d(shape[i]);
    }
}
