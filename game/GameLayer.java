package game;

//import game.modifiers.Modifier;
//import java.applet.Applet;
import java.awt.*;
import java.util.Enumeration;
//import java.util.Vector;
import litecom.ExceptionWindow;
import litecom.gfxe.*;
import litecom.Debug;
import litecom.inca.*;
//import litecom.sound.SoundManager;
//import litecom.sound.ScoreClient2;

public class GameLayer extends Layer {

    private Player player;
    private Image bg;
    private Image key[];
    private int bgXlen;
    private int bgYlen;
    private int sleep;
    private double highestY;
    private boolean showStatus;
    private boolean firstTime;
    private String message;
    private double messageTimer;
    private boolean drawBGImage;
    private boolean displayFramerate;
    private Explosion exp;
    double pain;
    double pointShade;
    boolean slomo;
    int nSectors;
    long frameCounter;
    double endCounter;
    double counter;
    double points;
    double mouseX;
    double mouseZ;
    private String cheatStr[] = {
        "mums", "leif", "nexxt", "xygoto", "gurgel"
    };
    public int currDetailLevel;
    int cheatIndex;
    int currC;

    public GameLayer() {
        key = new Image[4];
        showStatus = false;
        firstTime = true;
        message = "";
        messageTimer = -1D;
        drawBGImage = true;
        displayFramerate = false;
        slomo = false;
        mouseX = -1D;
        mouseZ = -1D;
        currDetailLevel = -1;
        currC = -1;
    }

    public void focus() {
        super.graphics.setColor(Color.black);
        super.graphics.fillRect(0, 0, super.xlen, super.ylen);
        super.a.requestFocus();
        if (firstTime) {
            init();
            firstTime = false;
        }
        loadBGImage();
        pain = 0.0D;
        pointShade = 0.0D;
        endCounter = -1D;
        player = core.player;
        points = core.player.getPoints();
        Debug.out(this, "Calling garbage collector.");
        System.gc();
        ((Animator) (super.a)).frameTimer.reset();
        player.setPosRot(new Point3d(core.world.playerStartPos), new Point3d(core.world.playerStartRot));
        Camera camera = new Camera();
        camera.position.x = player.pos.x;
        camera.position.y = player.pos.y + 300D;
        camera.position.z = player.pos.z;
        camera.rotation = new Point3d(-Math.PI/2, 0.0D, 0.0D);
        core.cameraManager.steadyCam.setCamera(camera);
        core.cameraManager.steadyCam.finish();
        core.cameraManager.steadyCam.update(1.0E-05D);
        core.cameraManager.setCamera(core.lastCamera);
        if (currDetailLevel == -1) {
            currDetailLevel = -123;
            setDetailLevel(3);
        } else {
            int i = currDetailLevel;
            currDetailLevel = -123;
            setDetailLevel(i);
        }
        core.world.clearFlags();
        core.soundManager.play("level-start");
    }

    public synchronized void animate(double d) {
        try {
            if (slomo) {
                d = 0.02D;
                try {
                    Thread.sleep(800L);
                } catch(Exception _ex) { }
            }
            nSectors = core.world.sectors.size();
            Sector sector;
            for (Enumeration<?> enumeration = core.world.sectors.elements(); enumeration.hasMoreElements(); sector.update(d))
                sector = (Sector)enumeration.nextElement();

            Sector asector[] = new Sector[nSectors];
            core.world.sectors.copyInto(asector);
            for (int i = 0; i < asector.length; i++)
                if(asector[i].killMe && !asector[i].modifier.resetable()) {
                    Debug.out("Killing sector: " + asector[i]);
                    core.world.removeSector(asector[i]);
                }

            core.world.updateSprites(d);
            if (!player.killed && !core.world.flag[111]) {
                double d1 = player.life;
                double d2 = player.getPoints();
                player.update(d);
                if (d1 > player.life) {
                    pain += (d1 - player.life) / 160D;
                    if (pain < 0.6D)
                        pain = 0.6D;
                }
                if (d2 != (double)player.getPoints())
                    pointShade = 0.2D;
            }
            core.cameraManager.update(d);
            if((player.killed || core.world.flag[111]) && endCounter == -1D) {
                endCounter = 0.0D;
                core.lastCamera = core.cameraManager.getCurrCamIndex();
                core.cameraManager.setCamera(6);
                if (core.world.flag[111])
                    core.soundManager.play("level-end");
                else if(player.pos.y > -1500D) {
                    core.soundManager.play("explode");
                    exp = new Explosion(new Point3d(player.pos), 150);
                    core.vWorld.addShape(exp);
                    player.hidePlayer();
                }
            }
            if (endCounter >= 0.0D)
                endCounter += d;
            if (exp != null && exp.update(d)) {
                core.vWorld.removeShape(exp);
                exp = null;
            }
            drawBg();
            core.vWorld.render(core.vp, core.currCamera);
            if (pain > 0.0D && !player.killed) {
                super.graphics.setXORMode(Color.black);
                if (pain > 1.0D)
                    super.graphics.setColor(Color.white);
                else
                    super.graphics.setColor(new Color((float)pain, (float)pain, (float)pain));
                super.graphics.fillRect(0, 0, super.xlen, super.ylen);
                super.graphics.setPaintMode();
                pain -= d / 2D;
            }
            if (pointShade > 0.0D) {
                super.graphics.setXORMode(Color.white);
                super.graphics.setColor(new Color(1.0F, (float)(1.0D - pointShade), 1.0F));
                super.graphics.fillRect(0, 0, super.xlen, super.ylen);
                super.graphics.setPaintMode();
                pointShade -= d;
                if (pointShade < 0.0D)
                    pointShade = 0.0D;
            }
            if (player.pos.y > highestY)
                highestY = player.pos.y;
            if (showStatus)
                showDebug(d);
            else
                drawInfo(d);
            Thread.yield();
            if (sleep > 0)
                try {
                    Thread.sleep(sleep);
                }
                catch(Exception _ex) { }
            if (endCounter > 2.4D)
                if (core.world.flag[111]) {
                    core.currLevel++;
                    if (core.currLevel > 17) {
                        endPlayer();
                        super.a.setLayer(core.endGameLayer);
                    } else {
                        core.player.hidePlayer();
                        super.a.setLayer(core.loadLevelLayer);
                    }
                } else {
                    player.lives--;
                    if (player.lives < 0) {
                        endPlayer();
                        super.a.setLayer(core.gameOverLayer);
                    } else {
                        Debug.out("One life less: " + player.lives);
                        player.life = 100D;
                        player.killed = false;
                        player.hidePlayer();
                        super.a.setLayer(this);
                    }
                    core.world.resetSectors();
                }
            if (player != null)
                player.checkScores();
            frameCounter++;
        } catch(Exception exception) {
            exception.printStackTrace();
            new ExceptionWindow(exception, "problem in main game loop");
        }
    }

    private void endPlayer() {
        if (player.getPoints() > core.scoreClient2.minScore() && player.getPoints() != 0)
            new HighscoreWindow(this, player.getPoints(), core.currLevel, false);
        Debug.out("Reseting player+currLevel");
        player.remove();
        player = core.player = null;
        core.currLevel = 1;
    }

    private void drawBg() {
        if (core.world.bgWhite)
            super.graphics.setColor(Color.white);
        else
            super.graphics.setColor(Color.black);
        super.graphics.fillRect(0, 0, super.xlen, super.ylen);
        if (drawBGImage) {
            for (core.currCamera.rotation.x %= Math.PI*2; core.currCamera.rotation.x < 0.0D; core.currCamera.rotation.x += Math.PI*2);
            if (core.currCamera.rotation.x > Math.PI)
                core.currCamera.rotation.x -= Math.PI*2;
            int i;
            for (i = (int)((core.currCamera.rotation.y / Math.PI*2) * (double)bgXlen * 3D); i < 0; i += bgXlen);
            i %= bgXlen;
            int j = (int)(((core.currCamera.rotation.x / 1.2707963267948965D) * (double)bgYlen) / 4D) - bgYlen / 4;
            if (j < -bgYlen / 2)
                j = -bgYlen / 2;
            if (j > bgYlen / 2)
                j = bgYlen / 2;
            super.graphics.drawImage(bg, i, j, null);
            super.graphics.drawImage(bg, i - bgXlen, j, null);
        }
    }

    private void drawInfo(double d) {
        super.graphics.setColor(Color.black);
        super.graphics.drawRect(3, 3, 101, 11);
        super.graphics.setColor(Color.red);
        super.graphics.fillRect(4, 4, (int)player.life, 10);
        if (points < (double)player.getPoints())
            points += 200D * d + ((double)player.getPoints() - points) / 4D;
        if (points > (double)player.getPoints())
            points = player.getPoints();
        for (int i = 0; i < 4; i++)
            if (player.key[i])
                super.graphics.drawImage(key[i], super.xlen - 3 - i * key[0].getWidth(super.a) - key[0].getWidth(super.a), 2, null);

        String s = "Score: " + (int)points;
        int j = super.xlen - core.fm.stringWidth(s) - 4;
        int l = super.ylen - core.fm.getDescent() - 2;
        fancyDrawString(s, j, l);
        s = "Lives: " + player.lives;
        j = 3;
        l = super.ylen - core.fm.getDescent() - 2;
        fancyDrawString(s, j, l);
        if (messageTimer >= 0.0D) {
            int k = super.xlen / 2 - core.fm.stringWidth(message) / 2;
            int i1 = super.ylen / 2;
            fancyDrawString(message, k, i1);
            messageTimer -= d;
        } else {
            message = "";
        }
        if (d >= 0.16D) {
            super.graphics.setColor(Color.red);
            String s1 = "Too slow";
            super.graphics.drawString(s1, super.xlen / 2 - core.fm.stringWidth(s1) / 2, super.ylen - core.fm.getDescent() - 20);
            super.graphics.setFont(new Font("Arial, Helvetica, Helv", 0, 10));
            FontMetrics fontmetrics = super.graphics.getFontMetrics();
            s1 = Locale.GLChangeDetailLevel;
            super.graphics.drawString(s1, super.xlen / 2 - fontmetrics.stringWidth(s1) / 2, super.ylen - fontmetrics.getDescent() - 6);
            super.graphics.setFont(core.font);
        }
        if (displayFramerate) {
            String s2 = ((Animator) (super.a)).frameTimer.getFrameRate() + " fps";
            fancyDrawString(s2, super.xlen / 2 - core.fm.stringWidth(s2) / 2, core.fm.getAscent() + 3);
        }
    }

    public void setMessage(String s) {
        if (s.equals(message))
        	return;
        
        core.soundManager.play("pling4");
        message = s;
        messageTimer = 3D;
    }

    private void fancyDrawString(String s, int x, int y) {
        super.graphics.setColor(Color.black);
        super.graphics.drawString(s, x + 1, y + 1);
        super.graphics.setColor(Color.white);
        super.graphics.drawString(s, x, y);
    }

    private void showDebug(double d) {
        int i = 10;
        super.graphics.setColor(Color.blue);
        super.graphics.drawString("Player:", 0, i);
        i += 13;
        super.graphics.drawString("pos: " + player.pos.round(), 10, i);
        i += 13;
        Point3d point3d = new Point3d(player.pos.x / 100D, player.pos.y / 100D, player.pos.z / 100D);
        super.graphics.drawString("segPos: " + point3d.round(), 10, i);
        i += 13;
        super.graphics.drawString("delta: " + player.delta.round(), 10, i);
        i += 13;
        super.graphics.drawString("realDelta: " + player.realDelta.round(), 10, i);
        i += 13;
        super.graphics.drawString("state: " + player.state, 10, i);
        i += 13;
        super.graphics.drawString("speed: " + player.speed, 10, i);
        i += 13;
        super.graphics.drawString("collision: (" + player.collidedX + ", " + player.collidedY + ", " + player.collidedZ + ")", 10, i);
        i += 13;
        super.graphics.drawString("dynHit: " + player.dynHit, 10, i);
        i += 13;
        super.graphics.drawString("jumpFactor: " + Player.jumpFactor, 10, i);
        i += 25;
        super.graphics.drawString("World:", 0, i);
        i += 13;
        super.graphics.drawString("fps/frameRate: " + ((Animator) (super.a)).frameTimer.getFrameRate() + "/" + (double)(int)(d * 100D) / 100D, 10, i);
        i += 13;
        super.graphics.drawString("nShapes: " + core.vWorld.nVisible, 10, i);
        i += 13;
        super.graphics.drawString("visible prims: " + core.vWorld.nPrims, 10, i);
        i += 13;
        super.graphics.drawString("nSprites: " + core.world.sprites.size(), 10, i);
        i += 13;
        super.graphics.drawString("hy: " + highestY, 10, i);
        i += 13;
        super.graphics.drawString("sleep: " + sleep, 10, i);
        i += 13;
        super.graphics.drawString("nSectors: " + nSectors, 10, i);
        i += 13;
    }

    private void init() {
        super.a.maxStepTime = 0.16D;
        MediaTracker mediatracker = new MediaTracker(super.a);
        for (int i = 0; i < 4; i++) {
            key[i] = super.a.getImage(super.a.getDocumentBase(), "gfx/key" + (i + 1) + ".gif");
            mediatracker.addImage(key[i], 0);
        }
        
        try {
            mediatracker.waitForAll();
        } catch(Exception _ex) { }
    }

    private void loadBGImage() {
        bg = super.a.getImage(super.a.getDocumentBase(), "gfx/" + core.world.bgImageFn);
        MediaTracker mediatracker = new MediaTracker(super.a);
        mediatracker.addImage(bg, 0);
        try {
            mediatracker.waitForAll();
        } catch(Exception _ex) { }
        bgXlen = bg.getWidth(null);
        bgYlen = bg.getHeight(null);
        Debug.out("Rescaling image..");
        double d = super.xlen;
        double d1 = super.ylen * 2;
        Image image = super.a.createImage((int)d, (int)d1);
        Graphics g = image.getGraphics();
        g.drawImage(bg, 0, 0, (int)d, (int)d1, null);
        bg = image;
        bgXlen = bg.getWidth(null);
        bgYlen = bg.getHeight(null);
        Debug.out("New dims: " + bgXlen + ", " + bgYlen);
    }

    public synchronized boolean mouseDown(Event event, int x, int y) {
        mouseX = x;
        mouseZ = y;
        return true;
    }

    public synchronized boolean mouseDrag(Event event, int x, int y) {
        try {
            player.dir.y += (mouseX - (double)x) / 100D;
            player.dir.x += ((mouseZ - (double)y) / 100D) * player.mouseFactor;
            if (player.dir.x > 1.2707963267948965D)
                player.dir.x = 1.2707963267948965D;
            if (player.dir.x < -1.2707963267948965D)
                player.dir.x = -1.2707963267948965D;
            mouseX = x;
            mouseZ = y;
        } catch(Exception exception) {
            new ExceptionWindow(exception, "Exception eccured during mouseDrag event.");
        }
        return true;
    }

    private void cheat(int i) {
        switch(i) {
        
        case 0:
            for (int j = 0; j < 4; j++)
                core.player.key[j] = true;
            break;

        case 1:
            core.player.life = 100D;
            break;

        case 2:
            core.world.flag[111] = true;
            break;

        case 3:
            new GotoLevelWindow();
            break;

        case 4:
            core.player.lives++;
            break;

        default:
            break;
        }
        setMessage("Cheater!");
        core.player.cheater = true;
        core.player.resetPoints();
    }

    public void setDetailLevel(int i) {
        int j = 700;
        int k = currDetailLevel;
        currDetailLevel = i;
        if (currDetailLevel > 3)
            currDetailLevel = 0;
        if (core.world.bgWhite)
            j = 200;
        if (k != currDetailLevel) {
            switch(currDetailLevel) {
            case 0:
                core.vWorld.setFog(true, false, j - 200, 800D);
                drawBGImage = false;
                break;

            case 1:
                core.vWorld.setFog(true, false, j, 800D);
                drawBGImage = true;
                break;

            case 2:
                core.vWorld.setFog(true, false, j, 1150D);
                drawBGImage = true;
                break;

            case 3:
                core.vWorld.setFog(true, false, j, 1500D);
                drawBGImage = true;
                break;
            }
            if (k >= 0)
                setMessage(Locale.GLDetailLevel + core.DETAIL_NAME[currDetailLevel]);
        }
        if (core.world.bgWhite) {
            core.vWorld.fogColor = Color.white;
        } else {
            core.vWorld.fogColor = Color.black;
        }
    }

    public synchronized boolean keyDown(Event event, int i) {
        try {
            switch(i) {
            case 68: // 'D'
            case 100: // 'd'
                setDetailLevel(currDetailLevel + 1);
                break;

            case 80: // 'P'
            case 112: // 'p'
                showStatus = !showStatus;
                break;

            case 70: // 'F'
            case 102: // 'f'
                displayFramerate = !displayFramerate;
                break;

            case 86: // 'V'
            case 118: // 'v'
                core.cameraManager.nextCamerap();
                break;

            case 82: // 'R'
            case 114: // 'r'
                player.reverseMouse();
                break;

            case 113: // 'q'
                sleep += 10;
                break;

            case 81: // 'Q'
                sleep -= 10;
                break;

            case 106: // 'j'
                Player.jumpFactor += 10D;
                break;

            case 74: // 'J'
                Player.jumpFactor -= 10D;
                break;

            case 119: // 'w'
                highestY = 0.0D;
                break;

            case 32: // ' '
                player.setJump(true);
                break;

            case 65: // 'A'
            case 97: // 'a'
            case 1004: 
                player.setForward(true);
                break;

            case 90: // 'Z'
            case 122: // 'z'
            case 1005: 
                player.setBackward(true);
                break;

            case 1006: 
            	player.setJump(true);
                //player.setLeft(true);
                break;

            case 1007: 
                player.setRight(true);
                break;

            case 1002: 
                player.setUp(true);
                break;

            case 1003: 
                player.setDown(true);
                break;

            case 49: // '1'
                core.cameraManager.setCamera(0);
                break;

            case 50: // '2'
                core.cameraManager.setCamera(1);
                break;

            case 51: // '3'
                core.cameraManager.setCamera(2);
                break;

            case 52: // '4'
                core.cameraManager.setCamera(3);
                break;

            case 53: // '5'
                core.cameraManager.setCamera(4);
                break;

            case 54: // '6'
                core.cameraManager.setCamera(5);
                break;

            case 107: // 'k'
                core.cameraManager.setCamera(6);
                break;
            }
            if (currC == -1) {
                for (int j = 0; j < cheatStr.length; j++) {
                    if (i != cheatStr[j].charAt(0))
                        continue;
                    currC = j;
                    cheatIndex = 1;
                    break;
                }

            } else if (i != cheatStr[currC].charAt(cheatIndex)) {
                currC = -1;
            } else {
                cheatIndex++;
                if (cheatIndex == cheatStr[currC].length()) {
                    Debug.out("Cheat: " + currC);
                    cheat(currC);
                    currC = -1;
                    cheatIndex = 0;
                }
            }
        } catch(Exception exception) {
            new ExceptionWindow(exception, "Exception occured during keyDown event(key: " + i + ").");
        }
        return true;
    }

    public synchronized boolean keyUp(Event event, int i) {
        try {
            switch(i) {
            case 32: // ' '
                player.setJump(false);
                break;

            case 65: // 'A'
            case 97: // 'a'
            case 1004: 
                player.setForward(false);
                break;

            case 90: // 'Z'
            case 122: // 'z'
            case 1005: 
                player.setBackward(false);
                break;

            case 1006: 
            	player.setJump(false);
                //player.setLeft(false);
                break;

            case 1007: 
                player.setRight(false);
                break;

            case 1002: 
                player.setUp(false);
                break;

            case 1003: 
                player.setDown(false);
                break;
            }
        } catch(Exception exception) {
            new ExceptionWindow(exception, "Exception eccured during keyUp event.");
        }
        return true;
    }
}
