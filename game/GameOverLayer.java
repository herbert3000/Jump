package game;

//import java.applet.Applet;
import java.awt.*;
import litecom.gfxe.Layer;
//import litecom.gfxe.LayerAnimator;

public class GameOverLayer extends Layer {

    private Image img;
    private double x;

    public void focus() {
        super.graphics.setColor(Color.black);
        super.graphics.fillRect(0, 0, super.xlen, super.ylen);
        img = super.a.getImage(super.a.getDocumentBase(), "gfx/gameover.gif");
        x = -50D;
    }

    public void animate(double d) {
        super.graphics.setColor(Color.black);
        super.graphics.fillRect(0, 0, super.xlen, super.ylen);
        super.graphics.drawImage(img, 0, 0, null);
        int i = (int)x - core.fm.stringWidth("Game") / 2;
        int j = super.ylen / 2 - 10;
        super.graphics.setColor(Color.black);
        super.graphics.drawString("Game", i + 1, j + 1);
        super.graphics.setColor(Color.yellow);
        super.graphics.drawString("Game", i, j);
        i = super.xlen - (int)x - core.fm.stringWidth("Over") / 2;
        j = super.ylen / 2 + 10;
        super.graphics.setColor(Color.black);
        super.graphics.drawString("Over", i + 1, j + 1);
        super.graphics.setColor(Color.yellow);
        super.graphics.drawString("Over", i, j);
        x += ((double)(super.xlen / 2) - x) / (0.5D / d);
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
