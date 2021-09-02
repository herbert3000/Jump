package game;

import java.awt.Font;
import java.awt.FontMetrics;
import litecom.gfxe.Loader;
import litecom.gfxe.LayerAnimator;
import litecom.inca.*;
import litecom.sound.SoundManager;
import litecom.sound.ScoreClient2;

public class core {
    public static final int DETAIL_SUPERLOW = 0;
    public static final int DETAIL_LOW = 1;
    public static final int DETAIL_MEDIUM = 2;
    public static final int DETAIL_HIGH = 3;
    public static final String DETAIL_NAME[] = {
        "GROTESQUE", "LOW", "MEDIUM", "HIGH"
    };
    public static int currLevel = 1;
    public static final int nLevels = 17;
    public static int lastCamera;
    public static Font font;
    public static FontMetrics fm;
    public static LayerAnimator main;
    public static Loader loader;
    public static GameLayer gameLayer;
    public static IntroLayer introLayer;
    public static GameOverLayer gameOverLayer;
    public static LoadLevelLayer loadLevelLayer;
    public static LoadShapesLayer loadShapesLayer;
    public static EndGameLayer endGameLayer;
    public static ScoreClient2 scoreClient2;
    public static GameClipper gameClipper;
    public static ShapeHolder shapeHolder;
    public static SoundManager soundManager;
    public static VirtualWorld vWorld;
    public static ViewPort vp;
    public static Camera currCamera;
    public static CameraManager cameraManager;
    public static World world;
    public static Player player;
    public static boolean editorMode;
}
