package game;

import litecom.gfxe.Animator;
import litecom.Debug;
import litecom.inca.*;

public class CameraManager {

    public CameraManager() {
        camera = new Camera[7];
        currCam = -1;
        for (int i = 0; i < 7; i++)
            camera[i] = new Camera();

        camera[0].fov = 155 - (400 - ((Animator) (core.main)).xlen) / 2;
        camera[1].rotation.x = -Math.PI / 2;
        steadyCam = new SteadyCam(null);
        setCamera(0);
        core.currCamera = steadyCam.camera;
    }

    public synchronized void update(double d) {
        Camera camera1 = camera[currCam];
        switch(currCam) {
        case 0:
            camera1.rotation.x = core.player.dir.x;
            camera1.rotation.y = core.player.dir.y;
            camera1.rotation.z = core.player.dir.z;
            playerYsin += (d * core.player.speed) / 30D;
            camera1.position.x = core.player.pos.x - Math.sin(camera1.rotation.y) * 25D;
            camera1.position.z = core.player.pos.z + Math.cos(camera1.rotation.y) * 25D;
            camera1.position.y = core.player.smoothY + Math.sin(playerYsin) * 4D + 45D;
            break;

        case 1:
            camera1.rotation.y += (core.player.dir.y - camera1.rotation.y) / (0.4D / d);
            camera1.rotation.x += ((core.player.dir.x - Math.PI / 2) / 2D - camera1.rotation.x) / (0.4D / d);
            camera1.position.x = core.player.pos.x + Math.sin(camera1.rotation.y) * (400D * Math.cos(camera1.rotation.x));
            camera1.position.y = core.player.smoothY - Math.sin(camera1.rotation.x) * 400D;
            camera1.position.z = core.player.pos.z - Math.cos(camera1.rotation.y) * (400D * Math.cos(camera1.rotation.x));
            break;

        case 2:
            camera1.rotation.y += (core.player.dir.y - camera1.rotation.y) / (0.4D / d);
            camera1.rotation.x = -Math.PI / 6;
            camera1.position.x = core.player.pos.x + Math.sin(camera1.rotation.y) * (400D * Math.cos(camera1.rotation.x));
            camera1.position.y = core.player.smoothY - Math.sin(camera1.rotation.x) * 400D;
            camera1.position.z = core.player.pos.z - Math.cos(camera1.rotation.y) * (400D * Math.cos(camera1.rotation.x));
            break;

        case 3:
            camera1.rotation.x = -Math.PI / 6;
            camera1.position.x = core.player.pos.x + Math.sin(camera1.rotation.y) * 400D;
            camera1.position.y = core.player.smoothY + 400D;
            camera1.position.z = core.player.pos.z - Math.cos(camera1.rotation.y) * 400D;
            camera1.lookAt(core.player.pos);
            break;

        case 4:
            camera1.rotation.y += (core.player.dir.y - (camera1.rotation.y + Math.PI / 2)) / (0.4D / d);
            camera1.rotation.x = -Math.PI / 6;
            camera1.position.x = core.player.pos.x + Math.sin(camera1.rotation.y) * 400D;
            camera1.position.y = core.player.smoothY + 400D;
            camera1.position.z = core.player.pos.z - Math.cos(camera1.rotation.y) * 400D;
            break;

        case 6:
            updateEndCam(d);
            break;

        case 5:
            camera1.lookAt(core.player.pos);
            break;
        }
        steadyCam.update(d);
        if(currCam == 0 || currCam == 6 || currCam == 3 || currCam == 5) {
            core.gameClipper.clearClose = false;
        } else {
            core.gameClipper.clearClose = steadyCam.inCamera;
        }
    }

    private void updateEndCam(double d) {
        Camera camera1 = camera[currCam];
        camera1.position.x = core.player.pos.x + Math.cos(endCounter) * 300D;
        camera1.position.z = core.player.pos.z + Math.sin(endCounter) * 300D;
        camera1.position.y = core.player.pos.y + endCounter * 300D;
        camera1.lookAt(core.player.pos);
        endCounter += d * 3D;
    }

    public synchronized void setCamera(int i) {
        core.player.showPlayer();
        currCam = i;
        if (currCam == 0) {
            core.player.hidePlayer();
            core.player.dir.x = 0.0D;
        }
        if (currCam == 2)
            camera[currCam].rotation.y = core.player.dir.y + 0.6;
        Debug.out("CurrCam: " + currCam);
        steadyCam.setCamera(camera[currCam]);
        if (currCam == 6) {
            endCounter = 0.0D;
            steadyCam.finish();
        }
        if (currCam == 5) {
            camera[currCam].position.x = core.player.pos.x + 20D;
            camera[currCam].position.y = core.player.pos.y + 400D;
            camera[currCam].position.z = core.player.pos.z + 20D;
        }
    }

    public int getCurrCamIndex() {
        return currCam;
    }

    public void nextCamerap() {
        int i = currCam;
        i = ++i % 6;
        setCamera(i);
    }

    public static final int nCameras = 7;
    public static final int CAM_PLAYER = 0;
    public static final int CAM_RELATIVE = 1;
    public static final int CAM_CHOPPER = 2;
    public static final int CAM_LEFT = 3;
    public static final int CAM_RELLEFT = 4;
    public static final int CAM_FIXED = 5;
    public static final int CAM_END = 6;
    private Camera camera[];
    private int currCam;
    public SteadyCam steadyCam;
    private double playerYsin;
    double endCounter;
}
