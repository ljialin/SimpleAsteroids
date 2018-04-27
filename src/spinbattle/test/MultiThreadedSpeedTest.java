package spinbattle.test;

import spinbattle.core.SpinGameState;
import utilities.ElapsedTimer;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedSpeedTest extends Thread {

    public static void main(String[] args) throws Exception {
        ElapsedTimer t = new ElapsedTimer();
        int nThreads = 2;
        List<MultiThreadedSpeedTest> threadList = new ArrayList<>();
        for (int i=0; i<nThreads; i++) {
            // start() is called in the constructor
            threadList.add(new MultiThreadedSpeedTest());
        }
        for (MultiThreadedSpeedTest thread : threadList) {
            thread.join();
        }
        long elapsed = t.elapsed();
        System.out.println(t);
        System.out.format("%.0fk ticks / s\n ", SpinGameState.totalTicks * 1.0 / elapsed);
    }

    private MultiThreadedSpeedTest () {
        start();
    }

    public void run() {
        SpeedTest.main(null);
    }
}
