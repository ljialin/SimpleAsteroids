package hyperopt;

import planetwar.EvoAgentSearchSpaceAsteroids;
import utilities.ElapsedTimer;

public class TestSolutionAsteroids {
    public static void main(String[] args) {
        // use this code to re-rest a particular point in the search space
        ElapsedTimer timer = new ElapsedTimer();
        int[] solution = {0, 1, 1, 0, 5};
        int nChecks = 10;
        new HyperParamTuneRunner().runChecks(new EvoAgentSearchSpaceAsteroids(), solution, nChecks);
        System.out.println(timer);
    }
}
