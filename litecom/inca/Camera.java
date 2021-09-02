package litecom.inca;

public class Camera {

    public double fov;
    public Point3d position;
    public Point3d rotation;

    public Camera() {
        fov = 256D;
        position = new Point3d();
        rotation = new Point3d();
    }

    public Camera(Camera camera) {
        fov = 256D;
        position = new Point3d();
        rotation = new Point3d();
        duplicate(camera);
    }

    public void duplicate(Camera camera) {
        fov = camera.fov;
        position.x = camera.position.x;
        position.y = camera.position.y;
        position.z = camera.position.z;
        rotation.x = camera.rotation.x;
        rotation.y = camera.rotation.y;
        rotation.z = camera.rotation.z;
    }

    public void lookAt(Point3d point3d) {
        double dx = point3d.x - position.x;
        double dy = point3d.y - position.y;
        double dz = point3d.z - position.z;
        rotation.y = Math.atan(dz / dx);
        if (dx < 0.0D)
            rotation.y += Math.PI;
        rotation.y -= Math.PI / 2;
        double d3 = Math.sqrt(dx * dx + dz * dz);
        rotation.x = Math.atan(dy / d3);
        if (d3 < 0.0D)
            rotation.x += Math.PI;
        rotation.z = 0.0D;
    }
}
