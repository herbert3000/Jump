package litecom.inca;

public final class Point2d {

    public int x;
    public int y;

    public Point2d() {
        x = 0;
        y = 0;
    }

    public Point2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
