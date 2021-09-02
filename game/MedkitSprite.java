package game;

import litecom.inca.*;

public class MedkitSprite extends Sprite {

    double s1;
    double s2;

    public MedkitSprite(Point3d point3d) {
        super.pos = World.makePos(point3d);
        super.shape = core.shapeHolder.getShape(4);
        super.shape.position = super.pos;
        super.pos.y -= 20D;
        super.sLink = core.world.segment[(int)point3d.x][(int)point3d.y][(int)point3d.z];
        super.sLink.sprite = this;
        core.vWorld.addShape(super.shape);
        core.world.addSprite(this);
        s1 = Math.sqrt((point3d.x - (double)(World.xlen / 2)) * (point3d.x - (double)(World.xlen / 2)) + (point3d.y - (double)(World.ylen / 2)) * (point3d.y - (double)(World.ylen / 2)) + (point3d.z - (double)(World.zlen / 2)) * (point3d.z - (double)(World.zlen / 2)));
        s2 = s1;
        super.shape.rotation.y = s1;
    }

    public void update(double d) {
        super.shape.rotation.x = Math.sin(s1) / 4D;
        super.shape.rotation.z = Math.cos(s2) / 4D;
        super.shape.rotation.y += d * 2D;
        s1 += d * 3D;
        s2 += d * 2D;
    }

    public Sprite dup(Point3d point3d) {
        removeSprite();
        return new MedkitSprite(point3d);
    }

    public String toString() {
    	return "MedkitSprite(" + World.makeSegPos(super.pos) + ")";
    }
}
