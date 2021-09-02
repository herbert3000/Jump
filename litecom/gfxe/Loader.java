package litecom.gfxe;

import java.awt.*;
import litecom.Trace;

public class Loader extends Thread {

    public boolean initPhaseDone;
    private int progress;
    private int nSteps;
    private LoaderTarget lt;
    private String name;
    private int barXLen;
    private int barYLen;
    private int fadeIn;

    public Loader(LoaderTarget loadertarget) {
        name = "game";
        lt = loadertarget;
    }

    public synchronized void paint(Graphics g, int i, int j) {
        barXLen = i - 20;
        barYLen = barXLen / 10;
        g.setColor(Color.black);
        g.fillRect(0, 0, i, j);
        g.setColor(Color.green);
        if (fadeIn < 255)
            fadeIn++;
        if (nSteps == 0) {
            FontMetrics fontmetrics = g.getFontMetrics();
            String s = "Loading " + name + " : " + progress + " steps.";
            g.drawString(s, i / 2 - fontmetrics.stringWidth(s) / 2, j / 2);
        } else {
            int k = i / 2 - barXLen / 2;
            int l = j / 2 - barYLen / 2;
            g.drawString(name, i / 2 - g.getFontMetrics().stringWidth(name) / 2, l / 2);
            g.drawRect(k, l, barXLen, barYLen);
            g.fillRect(k, l, (int)(((float)progress / (float)nSteps) * (float)barXLen), barYLen);
        }
        Thread.yield();
    }

    public void setMessage(String s) {
        name = s;
    }

    public void setSteps(int i) {
        nSteps = i;
    }

    public void run() {
        Trace.out(this, "run - start");
        lt.realInit();
        initPhaseDone = true;
        Trace.out(this, "run - end - steps: " + progress);
    }

    public synchronized void progress() {
        progress++;
    }
}
