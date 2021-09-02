package game;

//import java.applet.Applet;
import java.awt.*;
import litecom.gfxe.Layer;
//import litecom.gfxe.LayerAnimator;
import litecom.Progressor;

public class LoadShapesLayer extends Layer implements Runnable, Progressor {

    Thread thread;
    private boolean done;
    private double progress;
    private Image bg;

    public LoadShapesLayer() {
        done = false;
        progress = 0.0D;
    }

    public void focus() {
        bg = super.a.getImage(super.a.getDocumentBase(), "gfx/logo-2.gif");
        thread = new Thread(this);
        thread.start();
    }

    public void progress(double d) {
        progress += d;
    }

    public void run() {
        core.shapeHolder = new ShapeHolder();
        core.shapeHolder.init(this);
        done = true;
        thread = null;
    }

    public void animate(double d) {
        super.graphics.setColor(Color.black);
        super.graphics.fillRect(0, 0, super.xlen, super.ylen);
        super.graphics.drawImage(bg, 0, 0, null);
        super.graphics.setColor(Color.yellow);
        super.graphics.fillArc(10, super.ylen / 4, super.xlen - 20, super.ylen / 2, 0, (int)Math.round((progress / 11D) * 360D));
        FontMetrics fontmetrics = super.graphics.getFontMetrics();
        super.graphics.setColor(Color.white);
        super.graphics.drawString(Locale.LSLLoadingShapes, super.xlen / 2 - fontmetrics.stringWidth("Loading shapes...") / 2, super.ylen - 30);
        Thread.yield();
        if (done)
            super.a.setLayer(core.loadLevelLayer);
    }
}
