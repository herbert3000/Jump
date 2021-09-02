package game;

import java.util.Vector;
import litecom.gfxe.Animator;
import litecom.inca.*;

public class GameClipper implements Clipper {

    public boolean clearClose;

    public GameClipper() {
        clearClose = false;
    }

    public void clipShapes(VirtualWorld virtualWorld, Vector vector) {
        Camera camera = core.currCamera;
        virtualWorld.nVisible = 0;
        virtualWorld.nPrims = 0;
        for (int i = 0; i < vector.size(); i++) {
            Shape3d shape3d = (Shape3d)vector.elementAt(i);
            Point3d point3d = new Point3d(shape3d.position);
            if (point3d.dist(camera.position) > 300D) {
                point3d.add(camera.position.neg());
                point3d.rotate(camera.rotation);
                if (point3d.z < 0.0D || Math.sqrt(point3d.x * point3d.x + point3d.y * point3d.y + point3d.z * point3d.z) > core.vWorld.fogEndZ + shape3d.r && core.vWorld.fog) {
                    shape3d.visible = false;
                    continue;
                }
                Point2d point2d = point3d.project();
                int j = (int)((shape3d.r * camera.fov) / (point3d.z - shape3d.r)) + 1;
                if (point2d.x < -j || point2d.y < -j || point2d.x >= ((Animator) (core.main)).xlen + j || point2d.y > ((Animator) (core.main)).ylen + j) {
                    shape3d.visible = false;
                    continue;
                }
            } else if (clearClose) {
                shape3d.visible = false;
                continue;
            }
            shape3d.visible = true;
            virtualWorld.nVisible++;
            virtualWorld.nPrims += shape3d.primitive.length;
        }
    }
}
