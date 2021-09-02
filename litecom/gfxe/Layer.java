package litecom.gfxe;

import java.awt.Event;
import java.awt.Graphics;

public abstract class Layer {

    protected LayerAnimator a;
    protected Graphics graphics;
    protected int xlen;
    protected int ylen;

    public void setAnimator(LayerAnimator layeranimator) {
        a = layeranimator;
        graphics = ((Animator) (layeranimator)).offGfx;
        xlen = ((Animator) (layeranimator)).xlen;
        ylen = ((Animator) (layeranimator)).ylen;
    }

    public void focus() {}

    public void blur() {}

    public boolean mouseDown(Event event, int i, int j) {
        return false;
    }

    public boolean mouseUp(Event event, int i, int j) {
        return false;
    }

    public boolean mouseDrag(Event event, int i, int j) {
        return false;
    }

    public boolean mouseMove(Event event, int i, int j) {
        return false;
    }

    public boolean keyDown(Event event, int i) {
        return false;
    }

    public boolean keyUp(Event event, int i) {
        return false;
    }

    public abstract void animate(double d);
}
