package litecom.gfxe;

import java.applet.Applet;
import java.awt.*;
import litecom.Sem;
import litecom.Debug;

public abstract class Animator extends Applet implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public Timer frameTimer;
    public int xlen;
    public int ylen;
    protected Thread animatorThread;
    public Graphics mainGfx;
    public Graphics offGfx;
    public Image offImg;
    public double maxStepTime;
    private Sem sem;

    public Animator() {
        frameTimer = new Timer();
        maxStepTime = 1.0D;
        sem = new Sem();
    }

    public void init() {
    	//resize(350, 250); // resize to correct dimensions
    	resize(1920, 980);
    	
        Debug.out("Animator init.");
        xlen = size().width;
        ylen = size().height;
        mainGfx = getGraphics();
        offImg = createImage(xlen, ylen);
        offGfx = offImg.getGraphics();
        setBackground(Color.black);
        offGfx.setColor(Color.black);
        offGfx.fillRect(0, 0, xlen, ylen);
        Debug.out("Animator done init.");
    }

    public void start() {
        Debug.out("Animator start.");
        if (animatorThread == null) {
            animatorThread = new Thread(this);
            animatorThread.start();
        }
    }

    public void stop() {
        Debug.out("Animator stop - average framerate: " + frameTimer.getAverageFrameRate());
        animatorThread = null;
    }

    public void run() {
        Debug.out("Animator run - start");
        while (animatorThread != null)  {
            double d = frameTimer.getFrameSpeed();
            if (d > maxStepTime)
                d = maxStepTime;
            animate(d);
            repaint();
            sem.reset();
            Thread.yield();
        }
        Debug.out("Animator run - end");
    }

    public synchronized void paint(Graphics g) {
    	Toolkit.getDefaultToolkit().sync();
        g.drawImage(offImg, 0, 0, null);
        Toolkit.getDefaultToolkit().sync();
        sem.set();
    }

    public void update(Graphics g) {
        paint(g);
    }

    protected abstract void animate(double d);
}
