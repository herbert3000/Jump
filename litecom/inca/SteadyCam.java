package litecom.inca;

public class SteadyCam {

    public static final double moveTime = 1.5D;
    public Camera source;
    public Camera dest;
    public Camera camera;
    double timer;
    public boolean inCamera;

    public SteadyCam(Camera camera1) {
        timer = -1D;
        inCamera = true;
        dest = camera1;
        camera = new Camera();
    }

    public void update(double d) {
        if (timer >= 0.0D) {
            inCamera = false;
            camera.fov = morph(source.fov, dest.fov, timer, false);
            camera.position.x = morph(source.position.x, dest.position.x, timer, false);
            camera.position.y = morph(source.position.y, dest.position.y, timer, false);
            camera.position.z = morph(source.position.z, dest.position.z, timer, false);
            camera.rotation.x = morph(source.rotation.x, dest.rotation.x, timer, true);
            camera.rotation.y = morph(source.rotation.y, dest.rotation.y, timer, true);
            camera.rotation.z = morph(source.rotation.z, dest.rotation.z, timer, true);
            timer += d;
            if (timer > moveTime) {
                timer = -1D;
                return;
            }
        } else {
            inCamera = true;
            camera.duplicate(dest);
        }
    }

    private double morph(double d, double d1, double d2, boolean flag) {
        double d3 = (Math.cos(Math.PI - (d2 / moveTime) * Math.PI) + 1.0D) / 2D;
        if (flag) {
            d %= Math.PI * 2;
            d1 %= Math.PI * 2;
            for (; d < 0.0D; d += Math.PI * 2);
            for (; d1 < 0.0D; d1 += Math.PI * 2);
            if (Math.abs(d1 - d) > Math.PI)
                if (d1 < d)
                    d1 += Math.PI * 2;
                else
                    d1 -= Math.PI * 2;
        }
        return d + (d1 - d) * d3;
    }

    public void setCamera(Camera camera1) {
        source = new Camera(camera);
        dest = camera1;
        timer = 0.0D;
    }

    public void finish() {
        timer = -1D;
    }
}
