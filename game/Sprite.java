package game;

import litecom.inca.*;

public abstract class Sprite {

    Shape3d shape;
    Segment sLink;
    Point3d pos;

    public void removeSprite() {
        core.world.removeSprite(this);
        core.vWorld.removeShape(shape);
        sLink.sprite = null;
    }

    public abstract void update(double d);

    public abstract Sprite dup(Point3d point3d);
}
