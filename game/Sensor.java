package game;

public class Sensor {

    public byte destFlag;
    public boolean button;
    public int key;
    public String name;

    public Sensor(String s) {
        button = true;
        key = -1;
        name = s;
    }

    public String toString() {
        return "Sensor: " + name + "(" + destFlag + ")";
    }
}
