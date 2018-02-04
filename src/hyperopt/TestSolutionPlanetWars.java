package hyperopt;

import planetwar.EvoAgentSearchSpace;
import utilities.ElapsedTimer;

public class TestSolutionPlanetWars {
    public static void main(String[] args) {

        // use this code to re-rest a particular point in the search space
        int[] solution = {2, 1, 1, 0, 1};
        int nChecks = 100;
        ElapsedTimer timer = new ElapsedTimer();
        new HyperParamTuneRunner().runChecks(new EvoAgentSearchSpace(), solution, nChecks);
        System.out.println(timer);
    }
}
