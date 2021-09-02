package game;

//import java.applet.Applet;
import java.awt.*;
import litecom.ExceptionWindow;
import litecom.gfxe.Layer;
//import litecom.gfxe.LayerAnimator;
import litecom.inca.*;
import litecom.Progressor;

public class LoadLevelLayer extends Layer implements Runnable, Progressor {

    Thread thread;
    boolean done;
    double progress;
    private VirtualWorld world;
    private Camera camera;
    private Shape3d shape;
    private ViewPort vp;
    private boolean initDone;
    private Image bg;
    double xs;
    double ys;

    public LoadLevelLayer() {
        initDone = false;
    }

    public void focus() {
        progress = 0.0D;
        done = false;
        init();
        thread = new Thread(this);
        thread.start();
    }

    private void init() {
        if (initDone) 
            return;
        
        bg = super.a.getImage(super.a.getDocumentBase(), "gfx/logo-2.gif");
        world = new VirtualWorld();
        vp = new ViewPort(super.graphics, super.xlen, super.ylen);
        camera = new Camera();
        camera.position.z = -400D;
        shape = IncaUtils.createCube(70D);
        shape.createNormals(-1D);
        IncaUtils.colorFaces(shape, Color.yellow, Color.red, false);
        world.addShape(shape);
        initDone = true;
    }

    public void progress(double d) {
        progress += d;
    }

    public void run() {
        try {
            core.vWorld = new VirtualWorld();
            core.vWorld.setClipper(core.gameClipper = new GameClipper());
            core.vp = new ViewPort(super.graphics, super.xlen, super.ylen);
            core.world = new World();
            core.world.loadLevel(super.a.getDocumentBase(), "levels/" + core.currLevel + ".level", this);
            if (core.player == null)
                core.player = new Player();
            core.player.clearKeys();
            core.cameraManager = new CameraManager();
            core.world.optimizePlatforms(this);
        }
        catch(Exception exception) {
            new ExceptionWindow(exception, "could not load level");
        }
        done = true;
        thread = null;
    }

    public void animate(double d) {
        if (d > 0.1D)
            d = 0.1D;
        super.graphics.setColor(Color.black);
        super.graphics.fillRect(0, 0, super.xlen, super.ylen);
        super.graphics.drawImage(bg, 0, 0, null);
        world.render(vp, camera);
        shape.rotation.y = Math.sin(ys) / 5D;
        shape.rotation.x += d / 2D;
        shape.rotation.z = Math.cos(xs) / 5D;
        shape.scale.x = progress * 3D;
        ys += d;
        xs += d * 0.9D;
        super.graphics.setColor(Color.white);
        String s1 = Locale.LLLLoadingLevel + core.currLevel + "(" + Math.round((progress / 2D) * 100D) + "%)...";
        super.graphics.drawString(s1, super.xlen / 2 - core.fm.stringWidth(s1) / 2, super.ylen - 30);
        Thread.yield();
        if (done)
            super.a.setLayer(core.gameLayer);
    }
}
