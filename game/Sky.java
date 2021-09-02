package game;

import java.awt.Font;
//import java.awt.Graphics;
//import java.io.PrintStream;
import litecom.ExceptionWindow;
import litecom.gfxe.*;
import litecom.Debug;
import litecom.sound.SoundManager;

public class Sky extends LayerAnimator implements LoaderTarget {

	private static final long serialVersionUID = 1L;
	public static final String version = "1.1.0 Build 103";

	public void realInit() {
		//System.out.println("Jump " + "1.2.0 Build 138" + " (c)1998-2000 Ninja Games - http://www.ninjagames.com\r\n\r\n");
        System.out.println(version + " (c)1998-99 LiteCom - http://www.litecom.net/java\r\n\r\n");
        super.loader.setMessage("Jump");
        super.loader.setSteps(5);
        Debug.start(this);
        Debug.showDebug = false;
        try {
            super.offGfx.setFont(core.font = new Font("Arial, Helvetica, Helv", 1, 14));
            core.fm = super.offGfx.getFontMetrics();
            super.loader.progress();
            core.main = this;
            core.loader = super.loader;
            core.editorMode = true;
            super.loader.progress();
            core.soundManager = new SoundManager(this, "sounds/");
            core.soundManager.add("pling1");
            core.soundManager.add("pling3");
            core.soundManager.add("pling4");
            core.soundManager.add("explode");
            core.soundManager.add("level-start");
            core.soundManager.add("level-end");
            core.soundManager.add("fall");
            core.soundManager.add("hopp-ner-hard");
            core.soundManager.add("hopp-ner");
            core.soundManager.add("hopp-upp");
            core.soundManager.add("squish");
            core.soundManager.add("tick1");
            core.soundManager.add("tick2");
            core.soundManager.add("hiss-start");
            core.soundManager.add("hiss-stopp");
            core.soundManager.add("sinehiss");
            super.loader.progress();
            addLayer(core.gameLayer = new GameLayer());
            addLayer(core.introLayer = new IntroLayer());
            addLayer(core.gameOverLayer = new GameOverLayer());
            super.loader.progress();
            addLayer(core.endGameLayer = new EndGameLayer());
            addLayer(core.loadLevelLayer = new LoadLevelLayer());
            addLayer(core.loadShapesLayer = new LoadShapesLayer());
            super.loader.progress();
            setLayer(core.introLayer);
        } catch(Exception exception) {
            new ExceptionWindow(exception, "startup failed");
        }
    }
}
