package litecom.inca;

public final class Point3d {

    public static VirtualWorld vWorld;
    public static double fov;
    public static int xshift;
    public static int yshift;
    public double x;
    public double y;
    public double z;

    public Point3d() {
        x = 0.0D;
        y = 0.0D;
        z = 0.0D;
    }

    public Point3d(Point3d point3d) {
        x = point3d.x;
        y = point3d.y;
        z = point3d.z;
    }

    public Point3d(double x, double y, double z) {
    	this.x = x;
    	this.y = y;
    	this.z = z;
    }

    public Point3d(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3d copy() {
        return new Point3d(x, y, z);
    }

    public void normalize() {
        double d = 1.0D / Math.sqrt(x * x + y * y + z * z);
        x = x * d;
        y = y * d;
        z = z * d;
    }

    public boolean isZero() {
        return x == 0.0D && y == 0.0D && z == 0.0D;
    }

    public boolean isOne() {
        return x == 1.0D && y == 1.0D && z == 1.0D;
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public static double dist(Point3d p1, Point3d p2) {
        return Math.sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y) + (p2.z - p1.z) * (p2.z - p1.z));
    }

    public double dist(Point3d mb1) {
        return dist(this, mb1);
    }

    public void rotate(Point3d point3d) {
        Point3d point3d1 = new Point3d();
        rotate(point3d, point3d1);
        x = point3d1.x;
        y = point3d1.y;
        z = point3d1.z;
    }

    public void rotate(Point3d p1, Point3d p2) {
        double d = x;
        double d1 = y;
        double d2 = z;
        double d3 = Math.cos(p1.y);
        double d4 = Math.sin(p1.y);
        p2.x = d3 * d + d4 * d2;
        p2.y = d1;
        p2.z = -d4 * d + d3 * d2;
        d = p2.x;
        d1 = p2.y;
        d2 = p2.z;
        d3 = Math.cos(p1.x);
        d4 = Math.sin(p1.x);
        p2.x = d;
        p2.y = d3 * d1 - d4 * d2;
        p2.z = d4 * d1 + d3 * d2;
        d = p2.x;
        d1 = p2.y;
        d2 = p2.z;
        d3 = Math.cos(p1.z);
        d4 = Math.sin(p1.z);
        p2.x = d3 * d - d4 * d1;
        p2.y = d4 * d + d3 * d1;
        p2.z = d2;
    }

    public Point2d project() {
        Point2d point2d = new Point2d();
        double d = (x * fov) / z;
        double d1 = (-y * fov) / z;
        if (d > 32000D)
            d = 32000D;
        if (d < -32000D)
            d = -32000D;
        if (d1 > 32000D)
            d1 = 32000D;
        if (d1 < -32000D)
            d1 = -32000D;
        point2d.x = (int)d + xshift;
        point2d.y = (int)d1 + yshift;
        return point2d;
    }

    public Point3d neg() {
        return new Point3d(-x, -y, -z);
    }

    public Point3d add(Point3d p) {
        x += p.x;
        y += p.y;
        z += p.z;
        return this;
    }

    public Point3d add(double d) {
        x += d;
        y += d;
        z += d;
        return this;
    }

    public void sub(Point3d p) {
        x -= p.x;
        y -= p.y;
        z -= p.z;
    }

    public void sub(double d) {
        x -= d;
        y -= d;
        z -= d;
    }

    public Point3d div(Point3d p) {
        x /= p.x;
        y /= p.y;
        z /= p.z;
        return this;
    }

    public Point3d div(double d) {
        x /= d;
        y /= d;
        z /= d;
        return this;
    }

    public void mul(Point3d p) {
        x *= p.x;
        y *= p.y;
        z *= p.z;
    }

    public void mul(double d) {
        x *= d;
        y *= d;
        z *= d;
    }

    public boolean equals(Point3d p) {
        return p.x == x && p.y == y && p.z == z;
    }

    public boolean equalsInt(Point3d p) {
        return (int)p.x == (int)x && (int)p.y == (int)y && (int)p.z == (int)z;
    }

    public Point3d round() {
        return new Point3d(Math.round(x), Math.round(y), Math.round(z));
    }
}
