package cma;

public class CMAThread extends Thread {
    CMASolver cma;

    public CMAThread(CMASolver cma) {
        this.cma = cma;
        start();
    }

    public void run() {
        cma.run();
    }
}
