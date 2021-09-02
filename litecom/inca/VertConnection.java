package litecom.inca;

public class VertConnection {

    public int index;
    public Point3d p;

    public VertConnection(int i) {
        index = i;
    }

    public VertConnection(VertConnection vc) {
        index = vc.index;
    }
}
