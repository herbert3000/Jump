package litecom.inca;

import java.util.Vector;

public class DefaultClipper implements Clipper {

    public void clipShapes(VirtualWorld virtualworld, Vector vector) {
        virtualworld.nVisible = 0;
        virtualworld.nPrims = 0;
        for (int i = 0; i < vector.size(); i++) {
            Shape3d shape3d = (Shape3d)vector.elementAt(i);
            shape3d.visible = true;
            virtualworld.nVisible++;
            virtualworld.nPrims += shape3d.primitive.length;
        }
    }
}
