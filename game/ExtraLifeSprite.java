package game;

import litecom.inca.*;

public class ExtraLifeSprite extends Sprite {

    double ySin;
    double yStartPos;

    public ExtraLifeSprite(Point3d point3d) {
        super.pos = World.makePos(point3d);
        super.shape = core.shapeHolder.getShape(10);
        super.shape.position = super.pos;
        super.pos.y -= 10D;
        yStartPos = super.pos.y;
        super.sLink = core.world.segment[(int)point3d.x][(int)point3d.y][(int)point3d.z];
        super.sLink.sprite = this;
        core.vWorld.addShape(super.shape);
        core.world.addSprite(this);
        super.shape.rotation.x = (point3d.x / (double)World.xlen) * Math.PI;
        super.shape.rotation.y = (point3d.y / (double)World.ylen) * Math.PI;
        super.shape.rotation.z = (point3d.z / (double)World.zlen) * Math.PI;
        ySin = Math.sqrt((point3d.x - (double)(World.xlen / 2)) * (point3d.x - (double)(World.xlen / 2)) + (point3d.y - (double)(World.ylen / 2)) * (point3d.y - (double)(World.ylen / 2)) + (point3d.z - (double)(World.zlen / 2)) * (point3d.z - (double)(World.zlen / 2)));
    }

    public void update(double d) {
        super.shape.rotation.y += d;
        super.shape.rotation.x += d;
        super.shape.rotation.z += d / 2D;
        super.shape.position.y = yStartPos + Math.sin(ySin) * 10D;
        ySin += d * 7D;
    }

    public Sprite dup(Point3d point3d) {
        removeSprite();
        return new ExtraLifeSprite(point3d);
    }

    public String toString() {
    	return "ExtraLifeSprite(" + World.makeSegPos(super.pos) + ")";
    }
}
