package litecom;

public class Sem {

    private int i;

    public Sem() {
        i = 0;
    }

    public synchronized void set() {
        i = 1;
        notifyAll();
    }

    public synchronized void reset() {
        while (i <= 0) 
            try {
                wait();
            }
            catch(InterruptedException _ex) { }
        i = 0;
    }

    public synchronized void down() {
        while (i <= 0) 
            try {
                wait();
            }
            catch(InterruptedException _ex) { }
        i--;
    }

    public synchronized void up() {
        i++;
        notifyAll();
    }
}
