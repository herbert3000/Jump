package game;

import litecom.inca.*;

public class KeySprite extends Sprite {

    double ySin;
    Point3d startPos;
    public int key;

    public KeySprite(Point3d point3d, int i) {
        super.pos = World.makePos(point3d);
        key = i;
        super.shape = core.shapeHolder.getShape(5 + key);
        super.shape.position = super.pos;
        startPos = new Point3d(super.pos);
        super.sLink = core.world.segment[(int)point3d.x][(int)point3d.y][(int)point3d.z];
        super.sLink.sprite = this;
        core.vWorld.addShape(super.shape);
        core.world.addSprite(this);
        ySin = Math.sqrt((point3d.x - (double)(World.xlen / 2)) * (point3d.x - (double)(World.xlen / 2)) + (point3d.y - (double)(World.ylen / 2)) * (point3d.y - (double)(World.ylen / 2)) + (point3d.z - (double)(World.zlen / 2)) * (point3d.z - (double)(World.zlen / 2)));
        super.shape.rotation.y = ySin;
    }

    public void update(double d) {
        super.shape.rotation.y += d * 2D;
        super.shape.position.y = startPos.y + Math.sin(ySin) * 10D;
        ySin += d * 6D;
    }

    public Sprite dup(Point3d point3d) {
        removeSprite();
        return new KeySprite(point3d, key);
    }

    public String toString() {
        return "KeySprite(" + key + ")";
    }
}
