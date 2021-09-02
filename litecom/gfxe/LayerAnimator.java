package litecom.gfxe;

import java.awt.Event;
import java.util.Vector;
import litecom.Debug;

public abstract class LayerAnimator extends Animator implements LoaderTarget {

	private static final long serialVersionUID = 1L;
	
	private int currLayer;
    private Vector<Layer> layers;
    public Loader loader;

    public LayerAnimator() {
        currLayer = -1;
        layers = new Vector<Layer>();
        loader = new Loader(this);
    }

    public void init() {
        super.init();
        loader = new Loader(this);
        loader.start();
    }

    public synchronized Layer getCurrLayer() {
        if (currLayer == -1)
            return null;
        else
            return (Layer)layers.elementAt(currLayer);
    }

    public synchronized void addLayer(Layer layer) {
        Debug.out(this, "Adding layer: " + layer.getClass().getName());
        layer.setAnimator(this);
        layers.addElement(layer);
    }

    public synchronized void setLayer(int i) {
        Layer layer = getCurrLayer();
        if (layer != null)
            layer.blur();
        currLayer = i;
        layer = getCurrLayer();
        if (layer != null)
            layer.focus();
        Debug.out(this, "Setting layer: " + layer.getClass().getName() + "(" + i + ")");
    }

    public synchronized void setLayer(Layer layer) {
        setLayer(layers.indexOf(layer));
    }

    public synchronized int getLayer() {
        return currLayer;
    }

    public synchronized void animate(double d) {
        if (currLayer == -1 || !loader.initPhaseDone) {
            loader.paint(super.offGfx, super.xlen, super.ylen);
            try {
                Thread.sleep(500L);
                return;
            } catch(Exception _ex) {
                return;
            }
        } else {
            Layer layer = getCurrLayer();
            layer.animate(d);
        }
    }

    public abstract void realInit();

    public boolean mouseMove(Event event, int i, int j) {
        if (!loader.initPhaseDone)
            return false;
        Layer layer = getCurrLayer();
        if (layer != null)
            layer.mouseMove(event, i, j);
        return true;
    }

    public boolean mouseDrag(Event event, int i, int j) {
        if (!loader.initPhaseDone)
            return false;
        Layer layer = getCurrLayer();
        if (layer != null)
            layer.mouseDrag(event, i, j);
        return true;
    }

    public boolean mouseUp(Event event, int i, int j) {
        if (!loader.initPhaseDone)
            return false;
        Layer layer = getCurrLayer();
        if (layer != null)
            layer.mouseUp(event, i, j);
        return true;
    }

    public boolean mouseDown(Event event, int i, int j) {
        if (!loader.initPhaseDone)
            return false;
        Layer layer = getCurrLayer();
        if (layer != null)
            layer.mouseDown(event, i, j);
        return true;
    }

    public boolean keyUp(Event event, int i) {
        if (!loader.initPhaseDone)
            return false;
        Layer layer = getCurrLayer();
        if (layer != null)
            layer.keyUp(event, i);
        return true;
    }

    public boolean keyDown(Event event, int i) {
        if (!loader.initPhaseDone)
            return false;
        Layer layer = getCurrLayer();
        if (layer != null)
            layer.keyDown(event, i);
        return true;
    }
}
