package cma;

public class CMAServerTest {
    public static void main(String[] args) {
        // InteractiveFunction fun = new InteractiveFunction();
        QuadraticBowl fun = new QuadraticBowl();
        CMAServer server = new CMAServer();
        CMASolver cma = new CMASolver(server, 5, 50);
        // creating the thread auto-runs it
        CMAThread thread = new CMAThread(cma);

        int nTrials = 100;
        for (int i=0; i<nTrials; i++) {
            double[] x = server.getNext();
            double fitness = fun.valueOf(x);
            System.out.println(i + "\t " + fitness);
            server.returnFitness(fitness);
        }
    }
}
