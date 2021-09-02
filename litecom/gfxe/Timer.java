package litecom.gfxe;

public class Timer {

    private long lastTick;
    private long frameLastTick;
    private long firstTick;
    private int nFrames;
    private int totalFrames;
    //private int sleep;
    private double frameRate;
    private boolean firstTime;
    private double lastTime[];
    int counter;
    double lastD;
    long startTick;
    double frameCountInterval;

    public Timer() {
        firstTime = true;
        lastTime = new double[10];
        lastD = 0.05D;
        frameCountInterval = 1.0D;
    }

    public double getRealFrameSpeed() {
        long millis = System.currentTimeMillis();
        double d = ((double)millis - (double)lastTick) / 1000D;
        return d;
    }

    public double getFrameSpeed() {
        long millis = System.currentTimeMillis();
        if (firstTime) {
            firstTick = millis;
            lastTick = millis;
            frameLastTick = millis;
            firstTime = false;
        }
        nFrames++;
        totalFrames++;
        if (millis - frameLastTick > 1000L) {
            frameRate = (double)nFrames / (double)((millis - frameLastTick) / 1000L);
            nFrames = 0;
            frameLastTick = millis;
        }
        double d = ((double)millis - (double)lastTick) / 1000D;
        lastTime[totalFrames % lastTime.length] = d;
        if (totalFrames != 0) {
            double d1 = totalFrames < lastTime.length ? totalFrames : lastTime.length - 1;
            d = 0.0D;
            for (int i = 0; (double)i < d1; i++)
                d += lastTime[i];

            d /= d1;
        }
        lastTick = millis;
        if (d == 0.0D)
            d = 0.0001D;
        lastD = d;
        return d;
    }

    public int getFrameRate() {
        return (int)frameRate;
    }

    public int getAverageFrameRate() {
        return (int)((double)totalFrames / (double)((System.currentTimeMillis() - firstTick) / 1000L));
    }

    public void reset() {
        totalFrames = 0;
    }
}
