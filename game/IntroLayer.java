package game;

//import java.applet.Applet;
import java.awt.*;
import java.net.URL;
import java.util.StringTokenizer;
//import java.util.Vector;
import litecom.ExceptionWindow;
import litecom.gfxe.Layer;
//import litecom.gfxe.LayerAnimator;
import litecom.sound.ScoreClient2;

public class IntroLayer extends Layer {

    private Image logo;
    private boolean initDone;
    private boolean firstTime;
    //private boolean stopBounce;
    private boolean exit;
    double counter;
    double scoreY;

    public IntroLayer() {
        initDone = false;
        firstTime = true;
        counter = 0.0D;
    }

    public void focus() {
        init();
        //stopBounce = false;
        scoreY = super.ylen + 30;
        exit = false;
    }

    private void init() {
        if (initDone)
            return;
        
        super.a.showStatus(Locale.ILLoadingIntro);
        logo = super.a.getImage(super.a.getDocumentBase(), "gfx/logo.gif");
        super.a.showStatus(Locale.ILLoadingHighScores);
        try {
            String s1 = core.main.getParameter("scoreServerPath");
            if (s1 == null)
                s1 = "/cgi-bin/ss21/";
            if (!s1.endsWith("/"))
                s1 = s1 + "/";
            URL url = new URL(core.main.getDocumentBase(), s1);
            core.scoreClient2 = new ScoreClient2(url, "jump.prop", 100);
            core.scoreClient2.update();
        } catch(Exception exception) {
            new ExceptionWindow(exception, "Could not load scores.");
        }
        initDone = true;
    }

    public void animate(double d) {
        core.main.showStatus("");
        super.graphics.setColor(Color.black);
        super.graphics.fillRect(0, 0, super.xlen, super.ylen);
        super.graphics.drawImage(logo, 0, 0, null);
        drawScores(d);
        if (exit)
            if (firstTime) {
                super.a.setLayer(core.loadShapesLayer);
                firstTime = false;
            } else {
                super.a.setLayer(core.loadLevelLayer);
            }
        Thread.yield();
    }

    private void drawScores(double d) {
        super.graphics.setFont(new Font("Arial, Helvetica, Helv", 0, 10));
        super.graphics.getFontMetrics();
        ScoreClient2 scoreclient2 = core.scoreClient2;
        if (scoreclient2 == null || scoreclient2.scores == null)
            return;
        int i = (int)(scoreY % 10D);
        int j = super.xlen - 40;
        int k = (int)(-scoreY / 10D);
        int l = k + 100;
        if (k > scoreclient2.scores.size()) {
            scoreY = super.ylen + 30;
            return;
        }
        try {
            for (int i1 = k; i1 < l; i1++) {
                if (i1 > scoreclient2.scores.size())
                    break;
                if (i1 >= 0 && i1 < scoreclient2.scores.size()) {
                    StringTokenizer stringtokenizer = new StringTokenizer(String.valueOf(scoreclient2.scores.elementAt(i1)), "\t");
                    String s1 = stringtokenizer.nextToken();
                    String s2 = stringtokenizer.nextToken();
                    String s3 = stringtokenizer.nextToken();
                    super.graphics.setColor(Color.black);
                    super.graphics.drawString(String.valueOf(i1 + 1), 11, i + 1);
                    super.graphics.drawString(s1, 41, i + 1);
                    super.graphics.drawString(s2, 86, i + 1);
                    super.graphics.drawString(s3, j + 1, i + 1);
                    super.graphics.setColor(Color.white);
                    super.graphics.drawString(String.valueOf(i1 + 1), 10, i);
                    super.graphics.drawString(s1, 40, i);
                    super.graphics.drawString(s2, 85, i);
                    super.graphics.drawString(s3, j, i);
                } else if (i1 == -2) {
                    super.graphics.setColor(Color.black);
                    super.graphics.drawString(Locale.ILRank, 11, i + 1);
                    super.graphics.drawString(Locale.ILScore, 41, i + 1);
                    super.graphics.drawString(Locale.ILName, 86, i + 1);
                    super.graphics.drawString(Locale.ILLevel, j + 1, i + 1);
                    super.graphics.setColor(Color.white);
                    super.graphics.drawString(Locale.ILRank, 10, i);
                    super.graphics.drawString(Locale.ILScore, 40, i);
                    super.graphics.drawString(Locale.ILName, 85, i);
                    super.graphics.drawString(Locale.ILLevel, j, i);
                }
                i += 10;
            }
        } catch(Exception exception) {
            new ExceptionWindow(exception, "Could not parse scores.");
        }
        scoreY -= d * 15D;
        super.graphics.setFont(core.font);
    }

    public boolean keyDown(Event event, int i) {
        exit = true;
        return true;
    }

    public boolean mouseDown(Event event, int i, int j) {
        exit = true;
        return true;
    }
}
