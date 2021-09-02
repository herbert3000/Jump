package game;

//import java.applet.Applet;
import java.awt.*;
import litecom.gfxe.Layer;
//import litecom.gfxe.LayerAnimator;

public class EndGameLayer extends Layer {

    private Image img;

    public void focus() {
        super.graphics.setColor(Color.black);
        super.graphics.fillRect(0, 0, super.xlen, super.ylen);
        img = super.a.getImage(super.a.getDocumentBase(), "gfx/end.gif");
    }

    public void animate(double d) {
        super.graphics.drawImage(img, 0, 0, null);
        Thread.yield();
    }

    public boolean keyDown(Event event, int i) {
        super.a.setLayer(core.introLayer);
        return true;
    }

    public boolean mouseDown(Event event, int i, int j) {
        super.a.setLayer(core.introLayer);
        return true;
    }
}
